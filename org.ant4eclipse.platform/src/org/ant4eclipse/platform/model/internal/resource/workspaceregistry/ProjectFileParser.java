/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.model.internal.resource.workspaceregistry;

import java.io.File;
import java.util.StringTokenizer;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;
import org.ant4eclipse.platform.model.internal.resource.BuildCommandImpl;
import org.ant4eclipse.platform.model.internal.resource.EclipseProjectImpl;
import org.ant4eclipse.platform.model.internal.resource.LinkedResourceImpl;
import org.ant4eclipse.platform.model.internal.resource.ProjectNatureImpl;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.variable.EclipseVariableResolver;


/**
 * <p>
 * Parser that reads an eclipse <code>.project</code> file and creates an
 * {@link org.ant4eclipse.platform.model.resource.EclipseProject EclipseProject}.
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
  public static EclipseProjectImpl parseProject(final EclipseProjectImpl eclipseProject) {
    Assert.notNull(eclipseProject);

    // retrieve the '.project' file
    final File projectFile = eclipseProject.getChild(".project");

    final XQueryHandler queryhandler2 = new XQueryHandler(projectFile.getAbsolutePath());

    // create Queries
    final XQuery projectNameQuery = queryhandler2.createQuery("//projectDescription/name");
    final XQuery commentQuery = queryhandler2.createQuery("//projectDescription/comment");
    final XQuery referencedProjectQuery = queryhandler2.createQuery("//projectDescription/projects/project");
    final XQuery natureQuery = queryhandler2.createQuery("//projectDescription/natures/nature");
    final XQuery buildCommandNameQuery = queryhandler2
        .createQuery("//projectDescription/buildSpec/{buildCommand}/name");
    final XQuery linkedResourceNameQuery = queryhandler2
        .createQuery("//projectDescription/linkedResources/{link}/name");
    final XQuery linkedResourceTypeQuery = queryhandler2
        .createQuery("//projectDescription/linkedResources/{link}/type");
    final XQuery linkedResourceLocationQuery = queryhandler2
        .createQuery("//projectDescription/linkedResources/{link}/location");
    final XQuery linkedResourceLocationURIQuery = queryhandler2
        .createQuery("//projectDescription/linkedResources/{link}/locationURI");

    XQueryHandler.queryFile(projectFile, queryhandler2);

    final String projectName = projectNameQuery.getSingleResult();
    final String comment = commentQuery.getSingleResult();
    final String[] referencedProjects = referencedProjectQuery.getResult();
    final String[] natures = natureQuery.getResult();
    final String[] buildCommandNames = buildCommandNameQuery.getResult();
    final String[] linkedResourceNames = linkedResourceNameQuery.getResult();
    final String[] linkedResourceTypes = linkedResourceTypeQuery.getResult();
    final String[] linkedResourceLocations = linkedResourceLocationQuery.getResult();
    final String[] linkedResourceLocationURIs = linkedResourceLocationURIQuery.getResult();

    // set specified name
    eclipseProject.setSpecifiedName(projectName);

    // set comment
    eclipseProject.setComment(comment);

    // set referenced projects
    for (int i = 0; i < referencedProjects.length; i++) {
      eclipseProject.addReferencedProject(referencedProjects[i]);
    }

    // set project natures
    for (int i = 0; i < natures.length; i++) {
      eclipseProject.addNature(new ProjectNatureImpl(natures[i]));
    }

    // set build commands
    for (int i = 0; i < buildCommandNames.length; i++) {
      eclipseProject.addBuildCommand(new BuildCommandImpl(buildCommandNames[i]));
    }

    // set linked resources
    for (int i = 0; i < linkedResourceNames.length; i++) {

      // retrieve location and locationURI
      final String locationuri = linkedResourceLocationURIs[i];
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
        final int first = newlocation.indexOf('/');
        if (first != -1) {
          // only the part until the first slash can refer to a variable name
          newlocation = newlocation.substring(0, first);
        }
        newlocation = resolveLocation(eclipseProject, newlocation);
        if (newlocation != null) {
          final File test = new File(newlocation);
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
      final String relative = Utilities.calcRelative(eclipseProject.getFolder(), new File(location));
      final int typeAsInt = Integer.parseInt(linkedResourceTypes[i]);
      final LinkedResourceImpl linkedResource = new LinkedResourceImpl(linkedResourceNames[i], location, relative,
          typeAsInt);
      eclipseProject.addLinkedResource(linkedResource);
    }

    return eclipseProject;
  }

  /*
   * TODO:
   * 
   * Mostly unlikely, some weird path may be presented, so checking for extraneous separators may be worthwhile in the
   * long run.
   */
  private static final String resolveLocation(final EclipseProjectImpl p, final String path) {
    if (path == null) {
      return null;
    }
    final String S = "/";
    final StringTokenizer t = new StringTokenizer(path, S);
    final StringBuffer b = new StringBuffer(path.length() * 3);
    while (t.hasMoreElements()) {
      final String segment = (String) t.nextElement();
      if (segment == null) {
        return null;
      }
      final String resolved = getLocation(p, segment);
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
  private static final String getLocation(final EclipseProjectImpl eclipseProject, final String variable) {
    String key = "${" + variable + "}";

    String location = getEclipseVariableResolver().resolveEclipseVariables(key, eclipseProject, null);
    if (key.equals(location)) {
      // fallback for the internal prefs of an eclipse .metadata dir
      key = "${pathvariable." + variable + "}";
      location = getEclipseVariableResolver().resolveEclipseVariables(key, eclipseProject, null);
      if (key.equals(location)) {
        // the result is the key itself, so resolving failed
        location = null;
      }
    }
    return (location);
  }

  private static EclipseVariableResolver getEclipseVariableResolver() {
    final EclipseVariableResolver resolver = (EclipseVariableResolver) ServiceRegistry.instance().getService(
        EclipseVariableResolver.class.getName());
    return resolver;
  }

} /* ENDCLASS */
