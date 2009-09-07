/**********************************************************************
 * Copyright (c) 2005-2007 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.pde.tools.PdeBuildHelper;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.platform.ant.core.task.AbstractExecuteProjectTask;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.types.FileList;
import org.osgi.framework.Version;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecutePluginProjectTask extends AbstractExecuteProjectTask implements PdeExecutorValues {

  /** - */
  private static final String SCOPE_NAME_LIBRARY = "ForEachPluginLibrary";

  /** - */
  public static final String  SCOPE_LIBRARY      = "SCOPE_LIBRARY";

  /** - */
  private static final String SCOPE_NAME_PROJECT = "ForProject";

  /** - */
  public static final String  SCOPE_PROJECT      = "SCOPE_PROJECT";

  /**
   * <p>
   * Creates a new instance of type {@link ExecutePluginProjectTask}.
   * </p>
   * 
   */
  public ExecutePluginProjectTask() {
    super("executePluginProject");
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(final String name) {

    if (SCOPE_NAME_LIBRARY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY);
    } else if (SCOPE_NAME_PROJECT.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PROJECT);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      final MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      // execute SCOPE_LIBRARY
      if (SCOPE_LIBRARY.equals(scopedMacroDefinition.getScope())) {
        executeLibraryScopedMacroDef(macroDef);
      }
      // execute SCOPE_PROJECT
      else if (SCOPE_PROJECT.equals(scopedMacroDefinition.getScope())) {
        executeProjectScopedMacroDef(macroDef);
      }
      // delegate to template method
      else {
        // TODO: NLS
        throw new RuntimeException("Unknown Scope '" + scopedMacroDefinition.getScope() + "'");
      }
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param macroDef
   */
  private void executeProjectScopedMacroDef(MacroDef macroDef) {

    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

        getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

        addPluginProjectMacroExecutionValues(values);

        return values;
      }
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeLibraryScopedMacroDef(MacroDef macroDef) {

    // 2. Get libraries
    final PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());
    final PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();
    final Library[] libraries = pluginBuildProperties.getOrderedLibraries();
    final List<String> binaryIncludes = Arrays.asList(pluginBuildProperties.getBinaryIncludes());

    for (final Library library : libraries) {

      if (binaryIncludes.contains(library.getName())) {

        executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

          public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

            values.getProperties().put(LIBRARY_NAME, library.getName());

            if (library.isSelf()) {
              values.getProperties().put(LIBRARY_IS_SELF, "true");
              computeBinaryIncludeFilelist();
            } else {
              values.getProperties().put(LIBRARY_IS_SELF, "false");
            }

            getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

            File[] sourceFiles = getEclipseProject().getChildren(library.getSource());
            File[] outputFiles = getEclipseProject().getChildren(library.getOutput());

            values.getProperties().put(SOURCE_DIRECTORIES, convertToString(sourceFiles));
            values.getProperties().put(OUTPUT_DIRECTORIES, convertToString(outputFiles));

            values.getReferences().put(SOURCE_DIRECTORIES_PATH, convertToPath(sourceFiles));
            values.getReferences().put(OUTPUT_DIRECTORIES_PATH, convertToPath(outputFiles));

            // for (final String sourceFolderName : library.getSource()) {
            // final String outputFolderName = getJavaProjectRole().getOutputFolderForSourceFolder(sourceFolderName);
            // final File sourceFolder = getEclipseProject().getChild(sourceFolderName);
            // final File outputFolder = getEclipseProject().getChild(outputFolderName);
            // compilerArguments.addSourceFolder(sourceFolder, outputFolder);
            // }

            addPluginProjectMacroExecutionValues(values);

            return values;
          }
        });
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   */
  private void computeBinaryIncludeFilelist() {
    // TODO
    final PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());
    final PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();
    final Library[] libraries = pluginBuildProperties.getOrderedLibraries();
    final List<String> binaryIncludes = Arrays.asList(pluginBuildProperties.getBinaryIncludes());

    FileList fileList = new FileList();
    fileList.setDir(getEclipseProject().getFolder());

  }

  /**
   * <p>
   * </p>
   * 
   * @param values
   * @throws BuildException
   */
  private void addPluginProjectMacroExecutionValues(MacroExecutionValues values) throws BuildException {
    final PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());

    // "calculate" effective version, that is the version with replaced qualifier
    final Version effectiveVersion = PdeBuildHelper.resolveVersion(pluginProjectRole.getBundleDescription()
        .getVersion(), pluginProjectRole.getBuildProperties().getQualifier());

    // TODO
    values.getProperties().put(BUNDLE_RESOLVED_VERSION, effectiveVersion.toString());
    values.getProperties().put(BUNDLE_VERSION, pluginProjectRole.getBundleDescription().getVersion().toString());

    PluginBuildProperties buildProperties = pluginProjectRole.getBuildProperties();
    values.getProperties().put(BUILD_PROPERTIES_BINARY_INCLUDES, buildProperties.getBinaryIncludesAsString());
    values.getProperties().put(BUILD_PROPERTIES_BINARY_EXCLUDES, buildProperties.getBinaryExcludesAsString());
  }
}
