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
package org.ant4eclipse.ant.jdt;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteJdtProjectTask extends AbstractExecuteJdtProjectTask implements JdtExecutorValues {

  /** the constant for SCOPE_PROJECT_ELEMENT_NAME */
  private static final String SCOPE_PROJECT_ELEMENT_NAME          = "ForProject";

  /** the constant for SCOPE_TARGET_DIRECTORY_ELEMENT_NAME */
  private static final String SCOPE_TARGET_DIRECTORY_ELEMENT_NAME = "ForEachOutputDirectory";

  /** the constant for SCOPE_SOURCE_DIRECTORY_ELEMENT_NAME */
  private static final String SCOPE_SOURCE_DIRECTORY_ELEMENT_NAME = "ForEachSourceDirectory";

  /** the constant for SCOPE_SOURCE_DIRECTORY */
  public static final String  SCOPE_SOURCE_DIRECTORY              = "SCOPE_SOURCE_DIRECTORY";

  /** the constant for SCOPE_TARGET_DIRECTORY */
  public static final String  SCOPE_TARGET_DIRECTORY              = "SCOPE_TARGET_DIRECTORY";

  /** the constant for SCOPE_PROJECT */
  public static final String  SCOPE_PROJECT                       = "SCOPE_PROJECT";

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteJdtProjectTask}.
   * </p>
   */
  public ExecuteJdtProjectTask() {
    super("executeJdtProject");
  }

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteJdtProjectTask}.
   * </p>
   * 
   * @param prefix
   *          the prefix
   */
  protected ExecuteJdtProjectTask(String prefix) {
    super(prefix);
  }

  /**
   * {@inheritDoc}
   */
  public final Object createDynamicElement(String name) throws BuildException {

    // handle SCOPE_SOURCE_DIRECTORY
    if (SCOPE_SOURCE_DIRECTORY_ELEMENT_NAME.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_SOURCE_DIRECTORY);
    }
    // handle SCOPE_TARGET_DIRECTORY
    else if (SCOPE_TARGET_DIRECTORY_ELEMENT_NAME.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_TARGET_DIRECTORY);
    }
    // handle SCOPE_PROJECT
    else if (SCOPE_PROJECT_ELEMENT_NAME.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PROJECT);
    }

    // delegate to template method
    return onCreateDynamicElement(name);
  }

  /**
   * <p>
   * Override this method to provide support for additional sub-elements defined in an ant build file.
   * </p>
   * 
   * @param name
   *          the name of the sub element
   * @return
   */
  protected Object onCreateDynamicElement(String name) {
    // default implementation returns null
    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param scopedMacroDefinition
   * @return
   */
  protected boolean onExecuteScopeMacroDefintion(ScopedMacroDefinition<String> scopedMacroDefinition) {
    // default implementation returns false
    return false;
  }

  /**
   * <p>
   * </p>
   * 
   * @param executionValues
   */
  protected void addAdditionalExecutionValues(MacroExecutionValues executionValues) {
    // adds additional execution values
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

      // execute SCOPE_SOURCE_DIRECTORY
      if (SCOPE_SOURCE_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeSourceDirectoryScopedMacroDef(macroDef);
      }
      // execute SCOPE_TARGET_DIRECTORY
      else if (SCOPE_TARGET_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeOutputDirectoryScopedMacroDef(macroDef);
      }
      // execute SCOPE_PROJECT
      else if (SCOPE_PROJECT.equals(scopedMacroDefinition.getScope())) {
        executeProjectScopedMacroDef(macroDef);
      }
      // delegate to template method
      else {
        if (!onExecuteScopeMacroDefintion(scopedMacroDefinition)) {
          // TODO: NLS
          throw new RuntimeException("Unknown Scope '" + scopedMacroDefinition.getScope() + "'");
        }
      }
    }
  }

  /**
   * @param macroDef
   * @param javaProjectRole
   * @param classpathes
   */
  private void executeSourceDirectoryScopedMacroDef(MacroDef macroDef) {

    for (final String sourceFolder : getJavaProjectRole().getSourceFolders()) {

      // execute macro
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
              values);

          // add source and output directory
          values.getProperties().put(SOURCE_DIRECTORY_NAME, sourceFolder);
          values.getProperties().put(SOURCE_DIRECTORY, convertToString(getEclipseProject().getChild(sourceFolder)));
          values.getProperties().put(OUTPUT_DIRECTORY_NAME, sourceFolder);
          values.getProperties().put(
              OUTPUT_DIRECTORY,
              convertToString(getEclipseProject().getChild(
                  getJavaProjectRole().getOutputFolderForSourceFolder(sourceFolder))));

          // refs
          values.getReferences().put(SOURCE_DIRECTORY_PATH, convertToPath(getEclipseProject().getChild(sourceFolder)));
          values.getReferences().put(
              OUTPUT_DIRECTORY_PATH,
              convertToPath(getEclipseProject().getChild(
                  getJavaProjectRole().getOutputFolderForSourceFolder(sourceFolder))));

          // add additional execution values if necessary
          addAdditionalExecutionValues(values);

          // return the values
          return values;
        }
      });
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeOutputDirectoryScopedMacroDef(MacroDef macroDef) {

    // iterate over all output folders
    for (final String outFolder : getJavaProjectRole().getAllOutputFolders()) {

      // execute macro
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          // get the default jdt executor values
          getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
              values);

          // add output directory
          values.getProperties().put(OUTPUT_DIRECTORY_NAME, outFolder);
          values.getProperties().put(OUTPUT_DIRECTORY, convertToString(getEclipseProject().getChild(outFolder)));
          values.getReferences().put(OUTPUT_DIRECTORY_PATH, convertToPath(getEclipseProject().getChild(outFolder)));

          // call template method
          addAdditionalExecutionValues(values);

          // return the values
          return values;
        }
      });
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeProjectScopedMacroDef(MacroDef macroDef) {

    // execute macro
    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(final MacroExecutionValues values) {

        // get the default jdt executor values
        getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
            values);

        // add additional execution values if necessary
        addAdditionalExecutionValues(values);

        // return the values
        return values;
      }
    });
  }
}
