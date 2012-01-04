/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.lib.platform.internal.model.resource.workspaceregistry;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.internal.model.resource.BuildCommandImpl;
import org.ant4eclipse.lib.platform.internal.model.resource.EclipseProjectImpl;
import org.ant4eclipse.lib.platform.internal.model.resource.LinkedResourceImpl;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.LinkedResourcePathVariableService;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;
import org.ant4eclipse.lib.platform.model.resource.variable.EclipseStringSubstitutionService;

import java.io.File;
import java.util.StringTokenizer;

/**
 * <p>
 * Parser that reads an eclipse <code>.project</code> file and creates an
 * {@link org.ant4eclipse.lib.platform.model.resource.EclipseProject EclipseProject}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectFileParser {

  /**
   * <p>
   * Parses the '<code>.project</code>' file of the given eclipse project.
   * </p>
   * 
   * @param eclipseProject
   * 
   * @return the supplied {@link EclipseProject} instance.
   */
  public static EclipseProjectImpl parseProject(EclipseProjectImpl eclipseProject) {
    Assure.notNull("eclipseProject", eclipseProject);

    // retrieve the '.project' file
    File projectFile = eclipseProject.getChild(".project");

    XQueryHandler queryhandler2 = new XQueryHandler(projectFile.getAbsolutePath());

    // create Queries
    XQuery projectNameQuery = queryhandler2.createQuery("/projectDescription/name");
    XQuery commentQuery = queryhandler2.createQuery("/projectDescription/comment");
    XQuery referencedProjectQuery = queryhandler2.createQuery("/projectDescription/projects/project");
    XQuery natureQuery = queryhandler2.createQuery("/projectDescription/natures/nature");
    XQuery buildCommandNameQuery = queryhandler2.createQuery("/projectDescription/buildSpec/{buildCommand}/name");
    XQuery linkedResourceNameQuery = queryhandler2.createQuery("/projectDescription/linkedResources/{link}/name");
    XQuery linkedResourceTypeQuery = queryhandler2.createQuery("/projectDescription/linkedResources/{link}/type");
    XQuery linkedResourceLocationQuery = queryhandler2
        .createQuery("/projectDescription/linkedResources/{link}/location");
    XQuery linkedResourceLocationURIQuery = queryhandler2
        .createQuery("/projectDescription/linkedResources/{link}/locationURI");

    XQueryHandler.queryFile(projectFile, queryhandler2);

    String projectName = projectNameQuery.getSingleResult();
    String comment = commentQuery.getSingleResult();
    String[] referencedProjects = referencedProjectQuery.getResult();
    String[] natures = natureQuery.getResult();
    String[] buildCommandNames = buildCommandNameQuery.getResult();
    String[] linkedResourceNames = linkedResourceNameQuery.getResult();
    String[] linkedResourceTypes = linkedResourceTypeQuery.getResult();
    String[] linkedResourceLocations = linkedResourceLocationQuery.getResult();
    String[] linkedResourceLocationURIs = linkedResourceLocationURIQuery.getResult();

    // set specified name
    eclipseProject.setSpecifiedName(projectName);

    // set comment
    eclipseProject.setComment(comment);

    // set referenced projects
    for (String referencedProject : referencedProjects) {
      eclipseProject.addReferencedProject(referencedProject);
    }

    // set project natures
    for (String nature : natures) {
      eclipseProject.addNature(ProjectNature.createNature(nature));
    }

    // set build commands
    for (String buildCommandName : buildCommandNames) {
      eclipseProject.addBuildCommand(new BuildCommandImpl(buildCommandName));
    }

    // set linked resources
    for (int i = 0; i < linkedResourceNames.length; i++) {

      // retrieve location and locationURI
      String locationuri = linkedResourceLocationURIs[i];
      String location = linkedResourceLocations[i];

      // 
      if (locationuri != null) {
        location = resolveLocation(eclipseProject, locationuri);
        if (location == null) {
          // resolving the variable failed for some reason
          // TODO!!
          throw (new RuntimeException("couldn't resolve variable '" + locationuri + "'"));
        }
      } else {
        // this is needed since variable names are stored under the <location> element
        // in eclipse versions before 3.2. this is some sort of guessing since
        String newlocation = location;
        int first = newlocation.indexOf('/');
        if (first != -1) {
          // only the part until the first slash can refer to a variable name
          newlocation = newlocation.substring(0, first);
        }
        newlocation = resolveLocation(eclipseProject, newlocation);
        if (newlocation != null) {
          File test = new File(newlocation);
          if (test.exists()) {
            if (first != -1) {
              // the content has been cut down, so we need to add the relative path here
              location = newlocation + location.substring(first);
            } else {
              location = newlocation;
            }
          }
        }
      }

      // 
      File locationAsFile = new File(location);
      if (!locationAsFile.exists() && location.contains("/")) {
        String variableName = location.substring(0, location.indexOf("/"));

        LinkedResourcePathVariableService variableService = ServiceRegistryAccess.instance().getService(
            LinkedResourcePathVariableService.class);

        String variable = variableService.getLinkedResourcePath(variableName);

        if (variable != null) {
          location = variable + location.substring(location.indexOf("/"));
          locationAsFile = new File(location);
        }
      }

      // throw exception if file does not exist
      if (!locationAsFile.exists()) {
        throw new Ant4EclipseException(PlatformExceptionCode.LINKED_RESOURCE_DOES_NOT_EXIST, locationAsFile
            .getAbsolutePath(), projectName);
      }

      String relative = Utilities.calcRelative(eclipseProject.getFolder(), locationAsFile);
      int typeAsInt = Integer.parseInt(linkedResourceTypes[i]);
      LinkedResourceImpl linkedResource = new LinkedResourceImpl(linkedResourceNames[i], location, relative, typeAsInt);
      eclipseProject.addLinkedResource(linkedResource);
    }

    return eclipseProject;
  }

  /**
   * Determines whether a given directory is an eclipse project directory
   * 
   * @param directory
   *          the directory to test
   * @return true, if the directory is an eclipse project directory
   */
  public static boolean isProjectDirectory(File directory) {
    return directory != null && directory.exists() && directory.isDirectory()
        && new File(directory, ".project").exists();
  }

  /*
   * TODO:
   * 
   * Mostly unlikely, some weird path may be presented, so checking for extraneous separators may be worthwhile in the
   * long run.
   */
  private static final String resolveLocation(EclipseProjectImpl p, String path) {
    if (path == null) {
      return null;
    }
    String S = "/";
    StringTokenizer t = new StringTokenizer(path, S);
    StringBuffer b = new StringBuffer(path.length() * 3);
    while (t.hasMoreElements()) {
      String segment = (String) t.nextElement();
      if (segment == null) {
        return null;
      }
      String resolved = getLocation(p, segment);
      if (resolved != null) {
        b.append(resolved);
      } else {
        b.append(segment);
      }
      if (t.hasMoreElements()) {
        b.append(S);
      }
    }
    return b.toString();
  }

  /**
   * Returns the location while expanding the supplied variable.
   * 
   * @param eclipseProject
   *          The project that will be used for the variable expansion.
   * @param variable
   *          The name of the variable.
   * 
   * @return The expanded variable or null in case no expansion happened.
   */
  private static final String getLocation(EclipseProjectImpl eclipseProject, String variable) {
    String key = "${" + variable + "}";

    String location = getEclipseVariableResolver().substituteEclipseVariables(key, eclipseProject, null);
    if (key.equals(location)) {
      // fallback for the internal prefs of an eclipse .metadata dir
      key = "${pathvariable." + variable + "}";
      location = getEclipseVariableResolver().substituteEclipseVariables(key, eclipseProject, null);
      if (key.equals(location)) {
        // the result is the key itself, so resolving failed
        location = null;
      }
    }
    return location;
  }

  private static EclipseStringSubstitutionService getEclipseVariableResolver() {
    EclipseStringSubstitutionService resolver = ServiceRegistryAccess.instance().getService(
        EclipseStringSubstitutionService.class);
    return resolver;
  }

} /* ENDCLASS */
