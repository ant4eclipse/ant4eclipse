package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteJdtProjectTask extends AbstractExecuteJdtProjectTask {

  public static final String SCOPE_SOURCE_DIRECTORY = "SCOPE_SOURCE_DIRECTORY";

  public static final String SCOPE_TARGET_DIRECTORY = "SCOPE_TARGET_DIRECTORY";

  public static final String SCOPE_PROJECT          = "SCOPE_PROJECT";

  public ExecuteJdtProjectTask() {
    super("executeJdtProject");
  }

  protected ExecuteJdtProjectTask(final String prefix) {
    super(prefix);
  }

  /**
   * @see org.apache.tools.ant.DynamicElement#createDynamicElement(java.lang.String)
   */
  public final Object createDynamicElement(final String name) throws BuildException {
    if ("ForEachSourceDirectory".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_SOURCE_DIRECTORY);
    } else if ("ForEachOutputDirectory".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_TARGET_DIRECTORY);
    } else if ("ForProject".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PROJECT);
    }

    return onCreateDynamicElement(name);
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   * @return
   */
  protected Object onCreateDynamicElement(final String name) {
    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param scopedMacroDefinition
   * @return
   */
  protected boolean onExecuteScopeMacroDefintion(final ScopedMacroDefinition<String> scopedMacroDefinition) {
    return false;
  }

  /**
   * <p>
   * </p>
   * 
   * @param executionValues
   */
  protected void addAdditionalExecutionValues(final MacroExecutionValues executionValues) {
    //
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      final MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      if (SCOPE_SOURCE_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeSourceDirectoryScopedMacroDef(macroDef);
      } else if (SCOPE_TARGET_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeOutputDirectoryScopedMacroDef(macroDef);
      } else if (SCOPE_PROJECT.equals(scopedMacroDefinition.getScope())) {
        executeProjectScopedMacroDef(macroDef);
      } else {
        if (!onExecuteScopeMacroDefintion(scopedMacroDefinition)) {
          // TODO
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
  private void executeSourceDirectoryScopedMacroDef(final MacroDef macroDef) {

    final String[] sourceFolders = getJavaProjectRole().getSourceFolders();

    for (final String sourceFolder : sourceFolders) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
          executionValues);

      executionValues.getProperties().put("source.directory",
          this.convertToString(getEclipseProject().getChild(sourceFolder)));

      executionValues.getProperties().put(
          "output.directory",
          this.convertToString(getEclipseProject().getChild(
              getJavaProjectRole().getOutputFolderForSourceFolder(sourceFolder))));

      executionValues.getReferences().put("source.directory.path",
          this.convertToPath(getEclipseProject().getChild(sourceFolder)));

      executionValues.getReferences().put(
          "output.directory.path",
          this.convertToPath(getEclipseProject().getChild(
              getJavaProjectRole().getOutputFolderForSourceFolder(sourceFolder))));

      addAdditionalExecutionValues(executionValues);

      executeMacroInstance(macroDef, executionValues);
    }
  }

  private void executeProjectScopedMacroDef(final MacroDef macroDef) {
    if (getJavaProjectRole().getSourceFolders().length > 0) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      getExecutorValuesProvider().provideSourceDirectoriesScopedExecutorValues(getJavaProjectRole(),
          getJdtClasspathContainerArguments(), executionValues);

      addAdditionalExecutionValues(executionValues);

      executeMacroInstance(macroDef, executionValues);
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeOutputDirectoryScopedMacroDef(final MacroDef macroDef) {

    final String[] outFolders = getJavaProjectRole().getAllOutputFolders();

    for (final String outFolder : outFolders) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
          executionValues);

      executionValues.getProperties().put("output.directory",
          this.convertToString(getEclipseProject().getChild(outFolder)));

      executionValues.getReferences().put("output.directory.path",
          this.convertToPath(getEclipseProject().getChild(outFolder)));

      addAdditionalExecutionValues(executionValues);

      executeMacroInstance(macroDef, executionValues);
    }
  }
}
