package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.jdt.ant.AbstractExecuteJdtProjectTask;
import org.ant4eclipse.jdt.ant.JdtExecutorValues;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.PlatformExceptionCode;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteLibraryTask extends AbstractExecuteJdtProjectTask implements TargetPlatformAwareComponent,
    JdtExecutorValues {

  /** - */
  private static final String         SCOPE_NAME_SOURCE_DIRECTORY = "ForEachSourceDirectory";

  /** - */
  private static final String         SCOPE_NAME_OUTPUT_DIRECTORY = "ForEachOutputDirectory";

  /** - */
  private static final String         SCOPE_NAME_LIBRARY          = "ForEachPluginLibrary";

  /** - */
  public static final String          SCOPE_SOURCE_DIRECTORY      = "SCOPE_SOURCE_DIRECTORY";

  /** - */
  public static final String          SCOPE_OUTPUT_DIRECTORY      = "SCOPE_OUTPUT_DIRECTORY";

  /** - */
  public static final String          SCOPE_PLUGIN_LIBRARY        = "SCOPE_PLUGIN_LIBRARY";

  /** - */
  private TargetPlatformAwareDelegate _targetPlatformAwareDelegate;

  /** - */
  private String                      _libraryName;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteLibraryTask}.
   * </p>
   * 
   */
  public ExecuteLibraryTask() {
    super("executePluginLibrary");

    // create the delegates
    _targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();
  }

  /**
   * {@inheritDoc}
   */
  public final String getTargetPlatformId() {
    return _targetPlatformAwareDelegate.getTargetPlatformId();
  }

  /**
   * {@inheritDoc}
   */
  public final void setTargetPlatformId(String targetPlatformId) {
    _targetPlatformAwareDelegate.setTargetPlatformId(targetPlatformId);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isTargetPlatformIdSet() {
    return _targetPlatformAwareDelegate.isTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireTargetPlatformIdSet() {
    _targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getLibraryName() {
    return _libraryName;
  }

  /**
   * <p>
   * </p>
   * 
   * @param libraryName
   */
  public void setLibraryName(String libraryName) {
    _libraryName = libraryName;
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(final String name) {

    if (SCOPE_NAME_SOURCE_DIRECTORY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_SOURCE_DIRECTORY);
    } else if (SCOPE_NAME_OUTPUT_DIRECTORY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_OUTPUT_DIRECTORY);
    } else if (SCOPE_NAME_LIBRARY.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PLUGIN_LIBRARY);
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

      if (SCOPE_SOURCE_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeLibrarySourceDirectoryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_OUTPUT_DIRECTORY.equals(scopedMacroDefinition.getScope())) {
        executeLibraryTargetDirectoryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_PLUGIN_LIBRARY.equals(scopedMacroDefinition.getScope())) {
        executeLibraryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else {
        throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_EXECUTION_SCOPE, scopedMacroDefinition.getScope());
      }
    }
  }

  private void executeLibrarySourceDirectoryScopedMacroDef(MacroDef macroDef) {
    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(_libraryName);

    for (final String librarySourceDirectory : library.getSource()) {

      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
              values);

          values.getProperties().put(SOURCE_DIRECTORY,
              convertToString(getEclipseProject().getChild(librarySourceDirectory)));

          values.getProperties().put(
              OUTPUT_DIRECTORY,
              convertToString(getEclipseProject().getChild(
                  getJavaProjectRole().getOutputFolderForSourceFolder(librarySourceDirectory))));

          values.getReferences().put(SOURCE_DIRECTORY_PATH,
              convertToPath(getEclipseProject().getChild(librarySourceDirectory)));

          values.getReferences().put(
              OUTPUT_DIRECTORY_PATH,
              convertToPath(getEclipseProject().getChild(
                  getJavaProjectRole().getOutputFolderForSourceFolder(librarySourceDirectory))));

          return values;
        }
      });
    }
  }

  private void executeLibraryTargetDirectoryScopedMacroDef(MacroDef macroDef) {

    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(_libraryName);

    for (final String libraryOutputDirectory : library.getOutput()) {

      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(), getJdtClasspathContainerArguments(),
              values);

          values.getProperties().put("output.directory",
              convertToString(getEclipseProject().getChild(libraryOutputDirectory)));

          values.getReferences().put("output.directory.path",
              convertToPath(getEclipseProject().getChild(libraryOutputDirectory)));

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
  private void executeLibraryScopedMacroDef(MacroDef macroDef) {
    // TODO Auto-generated method stub

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
