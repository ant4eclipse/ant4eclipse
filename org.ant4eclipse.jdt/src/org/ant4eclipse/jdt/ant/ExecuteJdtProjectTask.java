package org.ant4eclipse.jdt.ant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgument;
import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent;
import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentDelegate;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectPathTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * @author
 */
public class ExecuteJdtProjectTask extends AbstractProjectPathTask implements JdtClasspathContainerArgumentComponent,
    DynamicElement {

  private enum Scope {
    SOURCE_DIRECTORY, TARGET_DIRECTORY, SOURCE_DIRECTORIES, TARGET_DIRECTORIES;
  }

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate                _macroExecutionDelegate;

  private final JdtClasspathContainerArgumentDelegate _jdtClasspathContainerArgumentDelegate;

  private final JdtExecutorValuesProvider             _executorValuesProvider;

  /* list of all macro definitions */
  private final List<ScopedMacroDefinition<Scope>>    _macroDefs;

  /**
   * 
   */
  public ExecuteJdtProjectTask() {
    this._macroExecutionDelegate = new MacroExecutionDelegate(this, "executeJdtProject");
    this._macroDefs = new LinkedList<ScopedMacroDefinition<Scope>>();

    this._jdtClasspathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();

    this._executorValuesProvider = new JdtExecutorValuesProvider(this);
  }

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

  public Map<String, Object> getJdtClasspathContainerArguments() {
    return this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments();
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<Scope> scopedMacroDefinition : this._macroDefs) {

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

      this._executorValuesProvider.provideExecutorValues(getJavaProjectRole().getEclipseProject(),
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

    final String[] sourceFolders = getJavaProjectRole().getSourceFolders();

    if (sourceFolders.length > 0) {
      final MacroExecutionValues executionValues = new MacroExecutionValues();

      this._executorValuesProvider.provideExecutorValues(getJavaProjectRole().getEclipseProject(),
          this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments(), executionValues);

      executionValues.getProperties().put("source.directories",
          this.convertToString(getEclipseProject().getChildren(sourceFolders)));

      executionValues.getReferences().put("source.directories.path",
          this.convertToPath(getEclipseProject().getChildren(sourceFolders)));

      final String outFolder = getJavaProjectRole().getDefaultOutputFolder();

      executionValues.getProperties().put("output.directory",
          this.convertToString(getEclipseProject().getChild(outFolder)));

      executionValues.getReferences().put("output.directory.path",
          this.convertToPath(getEclipseProject().getChild(outFolder)));

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

      this._executorValuesProvider.provideExecutorValues(getJavaProjectRole().getEclipseProject(),
          this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments(), executionValues);

      executionValues.getProperties().put("output.directory",
          this.convertToString(getEclipseProject().getChild(outFolder)));

      executionValues.getReferences().put("output.directory.path",
          this.convertToPath(getEclipseProject().getChild(outFolder)));

      this._macroExecutionDelegate.executeMacroInstance(macroDef, executionValues);
    }
  }

  private Object createMacroDef(final Scope scope) {
    final MacroDef macroDef = this._macroExecutionDelegate.createMacroDef();
    this._macroDefs.add(new ScopedMacroDefinition<Scope>(macroDef, scope));
    return macroDef.createSequential();
  }

  private JavaProjectRole getJavaProjectRole() {
    return JavaProjectRole.Helper.getJavaProjectRole(getEclipseProject());
  }
}
