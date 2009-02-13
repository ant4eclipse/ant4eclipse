package org.ant4eclipse.pde.ant;

import org.ant4eclipse.jdt.ant.AbstractExecuteJdtProjectTask;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.taskdefs.MacroDef;

public class ExecuteLibraryTask extends AbstractExecuteJdtProjectTask {

  public static final String          SCOPE_LIBRARY_SOURCE_DIRECTORY   = "SCOPE_LIBRARY_SOURCE_DIRECTORY";

  public static final String          SCOPE_LIBRARY_TARGET_DIRECTORY   = "SCOPE_LIBRARY_TARGET_DIRECTORY";

  public static final String          SCOPE_LIBRARY_SOURCE_DIRECTORIES = "SCOPE_LIBRARY_SOURCE_DIRECTORIES";

  public static final String          SCOPE_LIBRARY_TARGET_DIRECTORIES = "SCOPE_LIBRARY_TARGET_DIRECTORIES";

  private TargetPlatformAwareDelegate _targetPlatformAwareDelegate;

  private String                      _libraryName;

  public ExecuteLibraryTask() {
    super("executeLibrary");

    _targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();
  }

  public final String getTargetPlatformId() {
    return _targetPlatformAwareDelegate.getTargetPlatformId();
  }

  public final boolean isTargetPlatformId() {
    return _targetPlatformAwareDelegate.isTargetPlatformId();
  }

  public final void setTargetPlatformId(String targetPlatformId) {
    _targetPlatformAwareDelegate.setTargetPlatformId(targetPlatformId);
  }

  public String getLibraryName() {
    return _libraryName;
  }

  public void setLibraryName(String libraryName) {
    _libraryName = libraryName;
  }

  public Object createDynamicElement(final String name) {

    if ("ForEachSourceDirectory".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY_SOURCE_DIRECTORY);
    } else if ("ForEachOutputDirectory".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY_TARGET_DIRECTORY);
    } else if ("ForAllSourceDirectories".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY_SOURCE_DIRECTORIES);
    } else if ("ForAllOutputDirectories".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY_TARGET_DIRECTORIES);
    }

    return null;
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // TODO: CHECK!
    JdtClasspathContainerArgument containerArgument = createJdtClasspathContainerArgument();
    containerArgument.setKey("target.platform");
    containerArgument.setValue(getTargetPlatformId());

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      if (SCOPE_LIBRARY_SOURCE_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeLibrarySourceDirectoryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_LIBRARY_TARGET_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeLibraryTargetDirectoryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_LIBRARY_SOURCE_DIRECTORIES.equals(scopedMacroDefinition.getScope())) {
        executeLibrarySourceDirectoriesScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_LIBRARY_TARGET_DIRECTORIES.equals(scopedMacroDefinition.getScope())) {
        executeLibraryTargetDirectoriesScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else {
        throw new RuntimeException("Unknown Scope '" + scopedMacroDefinition.getScope() + "'");
      }
    }
  }

  private void executeLibrarySourceDirectoryScopedMacroDef(MacroDef macroDef) {
    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(_libraryName);

    for (String librarySourceDirectory : library.getSource()) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
          executionValues);

      executionValues.getProperties().put("source.directory",
          this.convertToString(getEclipseProject().getChild(librarySourceDirectory)));

      executionValues.getProperties().put(
          "output.directory",
          this.convertToString(getEclipseProject().getChild(
              getJavaProjectRole().getOutputFolderForSourceFolder(librarySourceDirectory))));

      executionValues.getReferences().put("source.directory.path",
          this.convertToPath(getEclipseProject().getChild(librarySourceDirectory)));

      executionValues.getReferences().put(
          "output.directory.path",
          this.convertToPath(getEclipseProject().getChild(
              getJavaProjectRole().getOutputFolderForSourceFolder(librarySourceDirectory))));

      executeMacroInstance(macroDef, executionValues);
    }
  }

  private void executeLibraryTargetDirectoryScopedMacroDef(MacroDef macroDef) {

    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(_libraryName);

    for (String libraryOutputDirectory : library.getOutput()) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
          executionValues);

      executionValues.getProperties().put(
          "output.directory",
          this.convertToString(getEclipseProject().getChild(
              libraryOutputDirectory)));

      executionValues.getReferences().put(
          "output.directory.path",
          this.convertToPath(getEclipseProject().getChild(
              libraryOutputDirectory)));

      executeMacroInstance(macroDef, executionValues);
    }
  }

  private void executeLibrarySourceDirectoriesScopedMacroDef(MacroDef macroDef) {
    System.err.println("SCOPE_SOURCE_DIRECTORIES");
  }

  private void executeLibraryTargetDirectoriesScopedMacroDef(MacroDef macroDef) {
    System.err.println("SCOPE_TARGET_DIRECTORIES");
  }

  /**
   * <p>
   * Helper method that returns the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   * </p>
   * 
   * @return the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   */
  protected final PluginProjectRole getPluginProjectRole() {
    return PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());
  }
}
