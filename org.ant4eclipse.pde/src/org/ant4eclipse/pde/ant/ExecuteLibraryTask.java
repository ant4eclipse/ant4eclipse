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
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.jdt.ant.JdtExecutorValues;

import org.ant4eclipse.pde.PdeExceptionCode;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.platform.ant.core.task.AbstractExecuteProjectTask;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * <p>
 * The {@link ExecuteLibraryTask} can be used to iterate over the content of a library defined in a plug-in project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteLibraryTask extends AbstractExecuteProjectTask {

  /** the name of the SCOPE_SOURCE_DIRECTORY */
  private static final String SCOPE_NAME_SOURCE_DIRECTORY = "ForEachSourceDirectory";

  /** the name of the SCOPE_OUTPUT_DIRECTORY */
  private static final String SCOPE_NAME_OUTPUT_DIRECTORY = "ForEachOutputDirectory";

  /** the name of the SCOPE_LIBRARY */
  private static final String SCOPE_NAME_LIBRARY          = "ForLibrary";

  /** the source directory scope */
  public static final String  SCOPE_SOURCE_DIRECTORY      = "SCOPE_SOURCE_DIRECTORY";

  /** the output directory scope */
  public static final String  SCOPE_OUTPUT_DIRECTORY      = "SCOPE_OUTPUT_DIRECTORY";

  /** the library scope */
  public static final String  SCOPE_LIBRARY               = "SCOPE_LIBRARY";

  /** the name of the library */
  private String              _libraryName;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteLibraryTask}.
   * </p>
   */
  public ExecuteLibraryTask() {
    super("executePluginLibrary");
  }

  /**
   * <p>
   * Returns the library name.
   * </p>
   * 
   * @return the library name.
   */
  public String getLibraryName() {
    return this._libraryName;
  }

  /**
   * <p>
   * Sets the library name.
   * </p>
   * 
   * @param libraryName
   *          the library name.
   */
  public void setLibraryName(String libraryName) {
    this._libraryName = libraryName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {

    // check require fields
    requireWorkspaceAndProjectNameSet();
    if (this._libraryName == null || this._libraryName.trim().equals("")) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "library");
    }

    // check if the specified library exists
    if (!getPluginProjectRole().getBuildProperties().hasLibrary(this._libraryName)) {
      throw new Ant4EclipseException(PdeExceptionCode.LIBRARY_NAME_DOES_NOT_EXIST, getLibraryName(),
          getEclipseProject().getSpecifiedName());
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(String name) {

    if (SCOPE_NAME_SOURCE_DIRECTORY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_SOURCE_DIRECTORY);
    } else if (SCOPE_NAME_OUTPUT_DIRECTORY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_OUTPUT_DIRECTORY);
    } else if (SCOPE_NAME_LIBRARY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // execute scoped macro definitions
    for (ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      if (SCOPE_SOURCE_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeLibrarySourceDirectoryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_OUTPUT_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeLibraryTargetDirectoryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_LIBRARY.equals(scopedMacroDefinition.getScope())) {
        executeLibraryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else {
        throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_EXECUTION_SCOPE, scopedMacroDefinition.getScope());
      }
    }
  }

  /**
   * <p>
   * Iterates over the source directories of a library.
   * </p>
   * 
   * @param macroDef
   *          the {@link MacroDef}
   */
  private void executeLibrarySourceDirectoryScopedMacroDef(MacroDef macroDef) {

    // Step 1: Get the library
    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(this._libraryName);

    // Step 2: Iterate over the source entries
    for (final String librarySourceDirectory : library.getSource()) {

      // execute the macro instance
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          // get the standard platform values
          getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

          // add common library properties
          addCommonLibraryProperties(library, values);

          // add additional values
          values.getProperties().put(JdtExecutorValues.SOURCE_DIRECTORY,
              convertToString(getEclipseProject().getChild(librarySourceDirectory)));
          values.getProperties().put(JdtExecutorValues.SOURCE_DIRECTORY_NAME, librarySourceDirectory);
          values.getReferences().put(JdtExecutorValues.SOURCE_DIRECTORY_PATH,
              convertToPath(getEclipseProject().getChild(librarySourceDirectory)));

          // return the result
          return values;
        }
      });
    }
  }

  /**
   * <p>
   * Iterates over the output directories of a library.
   * </p>
   * 
   * @param macroDef
   *          the {@link MacroDef}
   */
  private void executeLibraryTargetDirectoryScopedMacroDef(MacroDef macroDef) {

    // Step 1: Get the library
    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(this._libraryName);

    // Step 2: Iterate over the output entries
    for (final String libraryOutputDirectory : library.getOutput()) {

      // execute the macro instance
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          // get the standard platform values
          getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

          // add common library properties
          addCommonLibraryProperties(library, values);

          // add additional values
          values.getProperties().put(JdtExecutorValues.OUTPUT_DIRECTORY,
              convertToString(getEclipseProject().getChild(libraryOutputDirectory)));
          values.getProperties().put(JdtExecutorValues.OUTPUT_DIRECTORY_NAME, libraryOutputDirectory);
          values.getReferences().put(JdtExecutorValues.OUTPUT_DIRECTORY_PATH,
              convertToPath(getEclipseProject().getChild(libraryOutputDirectory)));

          // return the result
          return values;
        }
      });
    }
  }

  /**
   * <p>
   * Executes the macro one time.
   * </p>
   * 
   * @param macroDef
   *          the {@link MacroDef}
   */
  private void executeLibraryScopedMacroDef(MacroDef macroDef) {

    // Step 1: Get the library
    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(this._libraryName);

    // Step 2: Execute the macro instance
    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

        // get the standard platform values
        getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

        // add common library properties
        addCommonLibraryProperties(library, values);

        // return the result
        return values;
      }
    });
  }

  /**
   * <p>
   * Helper method that returns the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   * </p>
   * 
   * @return the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   */
  protected final PluginProjectRole getPluginProjectRole() {
    return getEclipseProject().getRole(PluginProjectRole.class);
  }

  /**
   * <p>
   * </p>
   * 
   * @param library
   * @param values
   */
  private void addCommonLibraryProperties(Library library, MacroExecutionValues values) {

    values.getProperties().put(PdeExecutorValues.LIBRARY_NAME, this._libraryName);

    if (library.isSelf()) {
      values.getProperties().put(PdeExecutorValues.LIBRARY_IS_SELF, "true");
    } else {
      values.getProperties().put(PdeExecutorValues.LIBRARY_IS_SELF, "false");
    }
  }
}
