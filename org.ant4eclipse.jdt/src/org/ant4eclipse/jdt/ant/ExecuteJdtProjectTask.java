package org.ant4eclipse.jdt.ant;

import java.util.List;

import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent;
import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentDelegate;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.task.AbstractExecuteProjectTask;
import org.ant4eclipse.platform.ant.core.task.ScopedMacroDefinition;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;

enum Scope {
  SOURCE_DIRECTORY, TARGET_DIRECTORY, SOURCE_DIRECTORIES, TARGET_DIRECTORIES;
}

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteJdtProjectTask extends AbstractExecuteProjectTask<Scope> implements
    JdtClasspathContainerArgumentComponent, DynamicElement {

  /** - */
  private final JdtClasspathContainerArgumentDelegate _jdtClasspathContainerArgumentDelegate;

  /** - */
  private final JdtExecutorValuesProvider             _executorValuesProvider;

  /**
   * 
   */
  public ExecuteJdtProjectTask() {

    this._jdtClasspathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();
    this._executorValuesProvider = new JdtExecutorValuesProvider(this);
  }

  /**
   * @see org.apache.tools.ant.DynamicElement#createDynamicElement(java.lang.String)
   */
  public Object createDynamicElement(final String name) throws BuildException {
    if ("ForEachSourceDirectory".equalsIgnoreCase(name)) {
      return createMacroDef(Scope.SOURCE_DIRECTORY);
    } else if ("ForEachOutputDirectory".equalsIgnoreCase(name)) {
      return createMacroDef(Scope.TARGET_DIRECTORY);
    } else if ("ForAllSourceDirectories".equalsIgnoreCase(name)) {
      return createMacroDef(Scope.SOURCE_DIRECTORIES);
    } else if ("ForAllOutputDirectories".equalsIgnoreCase(name)) {
      return createMacroDef(Scope.TARGET_DIRECTORIES);
    }

    return null;
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
    for (final ScopedMacroDefinition<Scope> scopedMacroDefinition : getScopedMacroDefinitions()) {

      final MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      switch (scopedMacroDefinition.getScope()) {
      case SOURCE_DIRECTORY:
        executeSourceDirectoryScopedMacroDef(macroDef);
        break;
      case TARGET_DIRECTORY:
        executeOutputDirectoryScopedMacroDef(macroDef);
        break;
      case SOURCE_DIRECTORIES:
        executeSourceDirectoriesScopedMacroDef(macroDef);
        break;
      case TARGET_DIRECTORIES:
        // TODO!
        // executeOutputDirectoriesScopedMacroDef(macroDef);
        break;
      default:
        // TODO
        throw new RuntimeException("");
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

      executeMacroInstance(macroDef, executionValues);
    }
  }

  private void executeSourceDirectoriesScopedMacroDef(final MacroDef macroDef) {
    if (getJavaProjectRole().getSourceFolders().length > 0) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      this._executorValuesProvider.provideSourceDirectoriesScopedExecutorValues(getJavaProjectRole(),
          this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments(), executionValues);

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

      this._executorValuesProvider.provideExecutorValues(getJavaProjectRole(),
          this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments(), executionValues);

      executionValues.getProperties().put("output.directory",
          this.convertToString(getEclipseProject().getChild(outFolder)));

      executionValues.getReferences().put("output.directory.path",
          this.convertToPath(getEclipseProject().getChild(outFolder)));

      executeMacroInstance(macroDef, executionValues);
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
}
