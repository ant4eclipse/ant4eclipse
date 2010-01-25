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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.util.ManifestHelper;

import org.ant4eclipse.pde.internal.ant.LibraryHelper;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.pde.tools.PdeBuildHelper;

import org.ant4eclipse.platform.PlatformExceptionCode;

import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.ScopedMacroDefinition;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.ant.platform.core.task.AbstractExecuteProjectTask;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.osgi.framework.Version;

import java.io.File;
import java.util.jar.Manifest;

/**
 * <p>
 * The {@link ExecutePluginProjectTask} can be used to iterate over the libraries defined in a eclipse plug-in project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecutePluginProjectTask extends AbstractExecuteProjectTask implements PdeExecutorValues {

  /** the name of the SCOPE_LIBRARY */
  private static final String SCOPE_NAME_LIBRARY = "ForEachPluginLibrary";

  /** the library scope */
  public static final String  SCOPE_LIBRARY      = "SCOPE_LIBRARY";

  /** the name of the SCOPE_PROJECT */
  private static final String SCOPE_NAME_PROJECT = "ForProject";

  /** the project scope */
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
  public Object createDynamicElement(String name) {

    // handle 'forEachPluginLibrary' element
    if (SCOPE_NAME_LIBRARY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY);
    }
    // handle 'forProject' element
    else if (SCOPE_NAME_PROJECT.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PROJECT);
    }

    // default: not handled
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
    for (ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      // execute SCOPE_LIBRARY
      if (SCOPE_LIBRARY.equals(scopedMacroDefinition.getScope())) {
        executeLibraryScopedMacroDef(macroDef);
      }
      // execute SCOPE_PROJECT
      else if (SCOPE_PROJECT.equals(scopedMacroDefinition.getScope())) {
        executeProjectScopedMacroDef(macroDef);
      }
      // scope unknown
      else {
        throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_EXECUTION_SCOPE, scopedMacroDefinition.getScope());
      }
    }
  }

  /**
   * <p>
   * Executes a project scoped macro definition.
   * </p>
   * 
   * @param macroDef
   *          the project scoped macro definition
   */
  private void executeProjectScopedMacroDef(MacroDef macroDef) {

    // execute the macro instance
    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

        // set the platform execution values
        getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

        // add plug-in project specific values
        addPluginProjectMacroExecutionValues(values);

        // return the values
        return values;
      }
    });
  }

  /**
   * <p>
   * Execute a library scoped macro definition.
   * </p>
   * 
   * @param macroDef
   *          the macro definition
   */
  private void executeLibraryScopedMacroDef(MacroDef macroDef) {

    // get the libraries
    Library[] libraries = LibraryHelper.getLibraries(getEclipseProject());

    // Iterate over all the libraries
    for (final Library library : libraries) {

      // execute the macro instance
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          // add the library name
          values.getProperties().put(LIBRARY_NAME, library.getName());
          values.getProperties().put(LIBRARY_SOURCE_NAME, LibraryHelper.getSourceNameForLibrary(library.getName()));

          // add boolean for 'self' library
          values.getProperties().put(LIBRARY_IS_SELF, String.valueOf(library.isSelf()));

          // add the platform specific values
          getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

          // get source and output directories
          File[] sourceFiles = getEclipseProject().getChildren(library.getSource());
          File[] outputFiles = getEclipseProject().getChildren(library.getOutput());

          // add source and output directories
          values.getProperties().put(SOURCE_DIRECTORIES, convertToString(sourceFiles));
          values.getProperties().put(OUTPUT_DIRECTORIES, convertToString(outputFiles));
          values.getReferences().put(SOURCE_DIRECTORIES_PATH, convertToPath(sourceFiles));
          values.getReferences().put(OUTPUT_DIRECTORIES_PATH, convertToPath(outputFiles));

          // add the plug-in project specific execution values
          addPluginProjectMacroExecutionValues(values);

          // return the values
          return values;
        }
      });
    }
  }

  /**
   * <p>
   * Adds eclipse plug-in project specific values to the given {@link MacroExecutionValues}.
   * </p>
   * 
   * @param values
   *          the macro execution values
   */
  private void addPluginProjectMacroExecutionValues(MacroExecutionValues values) {

    // get the plug-in project role
    PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());

    // get the bundle source
    BundleSource bundleSource = (BundleSource) pluginProjectRole.getBundleDescription().getUserObject();

    // get the bundle manifest
    Manifest manifest = bundleSource.getBundleManifest();

    // set the bundle symbolic name
    values.getProperties().put(BUNDLE_SYMBOLIC_NAME, ManifestHelper.getBundleSymbolicName(manifest));

    // "calculate" effective version, that is the version with replaced qualifier
    Version effectiveVersion = PdeBuildHelper.resolveVersion(pluginProjectRole.getBundleDescription().getVersion(),
        pluginProjectRole.getBuildProperties().getQualifier());

    // add the bundle version
    values.getProperties().put(BUNDLE_RESOLVED_VERSION, effectiveVersion.toString());
    values.getProperties().put(BUNDLE_VERSION, pluginProjectRole.getBundleDescription().getVersion().toString());

    PluginBuildProperties buildProperties = pluginProjectRole.getBuildProperties();
    // values.getProperties().put(BUILD_PROPERTIES_BINARY_INCLUDES, buildProperties.getBinaryIncludesAsString());
    // values.getProperties().put(BUILD_PROPERTIES_BINARY_EXCLUDES, buildProperties.getBinaryExcludesAsString());

    values.getProperties().put(BUILD_LIBRARYSOURCEROOTS, buildProperties.getLibrariesSourceRoots(".src"));
  }
}
