package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.exception.Ant4EclipseException;

import org.ant4eclipse.jdt.ant.JdtExecutorValues;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;

import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;

import org.ant4eclipse.platform.PlatformExceptionCode;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.platform.ant.core.task.AbstractExecuteProjectTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteLibraryTask extends AbstractExecuteProjectTask implements JdtExecutorValues {

  /** the name of the SCOPE_SOURCE_DIRECTORY */
  private static final String SCOPE_NAME_SOURCE_DIRECTORY = "ForEachSourceDirectory";

  /** the name of the SCOPE_OUTPUT_DIRECTORY */
  private static final String SCOPE_NAME_OUTPUT_DIRECTORY = "ForEachOutputDirectory";

  /** the name of the SCOPE_LIBRARY */
  private static final String SCOPE_NAME_LIBRARY          = "ForLibrary";

  /** - */
  public static final String  SCOPE_SOURCE_DIRECTORY      = "SCOPE_SOURCE_DIRECTORY";

  /** - */
  public static final String  SCOPE_OUTPUT_DIRECTORY      = "SCOPE_OUTPUT_DIRECTORY";

  /** - */
  public static final String  SCOPE_LIBRARY               = "SCOPE_LIBRARY";

  /** - */
  private String              _libraryName;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteLibraryTask}.
   * </p>
   * 
   */
  public ExecuteLibraryTask() {
    super("executePluginLibrary");
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
      return createScopedMacroDefinition(SCOPE_LIBRARY);
    }

    return null;
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

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
   * </p>
   * 
   * @param macroDef
   */
  private void executeLibrarySourceDirectoryScopedMacroDef(MacroDef macroDef) {
    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(_libraryName);

    for (final String librarySourceDirectory : library.getSource()) {

      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

          values.getProperties().put(SOURCE_DIRECTORY,
              convertToString(getEclipseProject().getChild(librarySourceDirectory)));

          values.getReferences().put(SOURCE_DIRECTORY_PATH,
              convertToPath(getEclipseProject().getChild(librarySourceDirectory)));

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
  private void executeLibraryTargetDirectoryScopedMacroDef(MacroDef macroDef) {

    final Library library = getPluginProjectRole().getBuildProperties().getLibrary(_libraryName);

    for (final String libraryOutputDirectory : library.getOutput()) {

      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

          values.getProperties().put(OUTPUT_DIRECTORY,
              convertToString(getEclipseProject().getChild(libraryOutputDirectory)));

          values.getReferences().put(OUTPUT_DIRECTORY_PATH,
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
    
    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

        getPlatformExecutorValuesProvider().provideExecutorValues(getEclipseProject(), values);

        values.getProperties().put(PdeExecutorValues.LIBRARY_NAME, _libraryName);

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
    return PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());
  }
}
