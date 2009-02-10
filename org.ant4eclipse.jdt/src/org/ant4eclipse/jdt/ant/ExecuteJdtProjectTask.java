package org.ant4eclipse.jdt.ant;

import java.util.List;

import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent;
import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentDelegate;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectPathTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteJdtProjectTask extends AbstractProjectPathTask implements JdtClasspathContainerArgumentComponent,
    DynamicElement, MacroExecutionComponent<String> {

  private final MacroExecutionDelegate<String>        _macroExecutionDelegate;

  /** - */
  private final JdtClasspathContainerArgumentDelegate _jdtClasspathContainerArgumentDelegate;

  /** - */
  private final JdtExecutorValuesProvider             _executorValuesProvider;

  public static final String                          SCOPE_SOURCE_DIRECTORY   = "SCOPE_SOURCE_DIRECTORY";

  public static final String                          SCOPE_TARGET_DIRECTORY   = "SCOPE_TARGET_DIRECTORY";

  public static final String                          SCOPE_SOURCE_DIRECTORIES = "SCOPE_SOURCE_DIRECTORIES";

  public static final String                          SCOPE_TARGET_DIRECTORIES = "SCOPE_TARGET_DIRECTORIES";

  /**
   * 
   */
  public ExecuteJdtProjectTask() {

    this._macroExecutionDelegate = new MacroExecutionDelegate<String>(this, "executeJdtProject");

    this._jdtClasspathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();

    this._executorValuesProvider = new JdtExecutorValuesProvider(this);
  }

  /**
   * @see org.apache.tools.ant.DynamicElement#createDynamicElement(java.lang.String)
   */
  public final Object createDynamicElement(final String name) throws BuildException {
    if ("ForEachSourceDirectory".equalsIgnoreCase(name)) {
      return this._macroExecutionDelegate.createScopedMacroDefinition(SCOPE_SOURCE_DIRECTORY);
    } else if ("ForEachOutputDirectory".equalsIgnoreCase(name)) {
      return this._macroExecutionDelegate.createScopedMacroDefinition(SCOPE_TARGET_DIRECTORY);
    } else if ("ForAllSourceDirectories".equalsIgnoreCase(name)) {
      return this._macroExecutionDelegate.createScopedMacroDefinition(SCOPE_SOURCE_DIRECTORIES);
    } else if ("ForAllOutputDirectories".equalsIgnoreCase(name)) {
      return this._macroExecutionDelegate.createScopedMacroDefinition(SCOPE_TARGET_DIRECTORIES);
    }

    return onCreateDynamicElement(name);
  }

  protected Object onCreateDynamicElement(final String name) {
    return null;
  }

  public NestedSequential createScopedMacroDefinition(final String scope) {
    return this._macroExecutionDelegate.createScopedMacroDefinition(scope);
  }

  public void executeMacroInstance(final MacroDef macroDef, final MacroExecutionValues macroExecutionValues) {
    this._macroExecutionDelegate.executeMacroInstance(macroDef, macroExecutionValues);
  }

  public List<ScopedMacroDefinition<String>> getScopedMacroDefinitions() {
    return this._macroExecutionDelegate.getScopedMacroDefinitions();
  }

  public String getPrefix() {
    return this._macroExecutionDelegate.getPrefix();
  }

  public void setPrefix(final String prefix) {
    this._macroExecutionDelegate.setPrefix(prefix);
  }

  public JdtClasspathContainerArgument createJdtClasspathContainerArgument() {
    return this._jdtClasspathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }

  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments();
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<String> scopedMacroDefinition : this._macroExecutionDelegate
        .getScopedMacroDefinitions()) {

      final MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      if (SCOPE_SOURCE_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeSourceDirectoryScopedMacroDef(macroDef);
      } else if (SCOPE_TARGET_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeOutputDirectoryScopedMacroDef(macroDef);
      } else if (SCOPE_SOURCE_DIRECTORIES.equals(scopedMacroDefinition.getScope())) {
        executeSourceDirectoriesScopedMacroDef(macroDef);
      } else if (SCOPE_TARGET_DIRECTORIES.equals(scopedMacroDefinition.getScope())) {
        // TODO!
        // executeOutputDirectoriesScopedMacroDef(macroDef);
      } else {
        if (!onExecuteScopeMacroDefintion(scopedMacroDefinition)) {
          // TODO
          throw new RuntimeException("Unknown Scope '" + scopedMacroDefinition.getScope() + "'");
        }
      }
    }
  }

  protected boolean onExecuteScopeMacroDefintion(final ScopedMacroDefinition<String> scopedMacroDefinition) {
    return false;
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

      this._executorValuesProvider.provideExecutorValues(getJavaProjectRole(),
          this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments(), executionValues);

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

      this._macroExecutionDelegate.executeMacroInstance(macroDef, executionValues);
    }
  }

  private void executeSourceDirectoriesScopedMacroDef(final MacroDef macroDef) {
    if (getJavaProjectRole().getSourceFolders().length > 0) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      this._executorValuesProvider.provideSourceDirectoriesScopedExecutorValues(getJavaProjectRole(),
          this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments(), executionValues);

      this._macroExecutionDelegate.executeMacroInstance(macroDef, executionValues);
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

      this._executorValuesProvider.provideExecutorValues(getJavaProjectRole(),
          this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments(), executionValues);

      executionValues.getProperties().put("output.directory",
          this.convertToString(getEclipseProject().getChild(outFolder)));

      executionValues.getReferences().put("output.directory.path",
          this.convertToPath(getEclipseProject().getChild(outFolder)));

      this._macroExecutionDelegate.executeMacroInstance(macroDef, executionValues);
    }
  }

  /**
   * <p>
   * Helper method that returns the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   * </p>
   * 
   * @return the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   */
  private JavaProjectRole getJavaProjectRole() {
    return JavaProjectRole.Helper.getJavaProjectRole(getEclipseProject());
  }

  public JdtExecutorValuesProvider getExecutorValuesProvider() {
    return this._executorValuesProvider;
  }
}
