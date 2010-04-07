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
package org.ant4eclipse.lib.pde.model.validator;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.lib.platform.model.resource.validator.AbstractProjectValidator;

/**
 * <p>
 * This validator checks whether the currently configured outputpath is part of the build.properties.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class OutputPathValidator extends AbstractProjectValidator {

  /**
   * Initialises this validator using the supplied key.
   * 
   * @param key
   *          The key which is used to generate the failure information. Neither <code>null</code> nor empty.
   */
  public OutputPathValidator(String key) {
    super(key, PluginProjectRole.class);
  }

  /**
   * {@inheritDoc}
   */
  public void validate(ProjectRole role) {

    PluginProjectRole pluginrole = (PluginProjectRole) role;
    EclipseProject project = pluginrole.getEclipseProject();

    // this test currently looks strange but future versions of eclipse will support non-java plugins, so we better
    // check for it
    if (project.hasRole(JavaProjectRole.class)) {

      JavaProjectRole javarole = project.getRole(JavaProjectRole.class);

      // get the output folders from the normal java nature
      File[] outputfolders = project.getChildren(javarole.getAllOutputFolders(),
          EclipseProject.PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME);
      Set<String> jdtoutputpathes = new HashSet<String>();
      for (File outputfolder : outputfolders) {
        jdtoutputpathes.add(outputfolder.getName().replace('\\', '/'));
      }

      PluginBuildProperties buildproperties = pluginrole.getBuildProperties();
      if (buildproperties == null) {
        // at this point a missing build.properties file is a warning only, as it is generally possible to work with
        // plug-in projects
        // that don't have a build.properties file (you can for example get the classpath of this project).
        // A missing build.properties is only an *error* if the project should be build and that is handled in the
        // PluginProjectChecker
        addWarning(
            project,
            "Plug-in project does not have a build.properties file. You won't be able to build this project using the ant4eclipse tasks.");
      } else {

        // get the library representing the project itself
        PluginBuildProperties.Library library = buildproperties.getLibrary(".");
        if (library != null) {

          // check each output path if it's available as an output path
          for (String output : library.getOutput()) {
            if (jdtoutputpathes.contains(output)) {
              jdtoutputpathes.remove(output);
            } else {
              /** @todo [03-Dec-2009:KASI] I18N */
              addError(project, String.format(
                  "build.properties declares output path '%s' which has not been set as an output folder", output));
            }
          }
        }

        /**
         * @todo [03-Dec-2009:KASI] Hm, should we check for this ? Some people might use different output for test code
         *       which should not become part of the deployed artifacts. On the other hand such a message might be
         *       handy. Perhaps we should provide a switch to adjust the behaviour here.
         */
        Iterator<String> iterator = jdtoutputpathes.iterator();
        while (iterator.hasNext()) {
          String output = iterator.next();
          addWarning(project, String.format(
              "build.properties does not contain output path '%s'. you will probably miss some classes", output));
        }
      }

    }

  }
} /* ENDCLASS */
