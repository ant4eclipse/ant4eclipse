package org.ant4eclipse.jdt.ant;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.ant4eclipse.core.ant.TaskHelper;
import org.ant4eclipse.core.ant.delegate.MacroExecutionDelegate;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.platform.ant.AbstractProjectBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * @author
 */
public class ExecuteJdtProjectTask extends AbstractProjectBasedTask {

  public static final int                    SOURCE_DIRECTORY_SCOPE   = 0;

  public static final int                    TARGET_DIRECTORY_SCOPE   = 1;

  private static final int                   SOURCE_DIRECTORIES_SCOPE = 0;

  private static final int                   TARGET_DIRECTORIES_SCOPE = 0;

  /** list of all macro definitions */
  private final List<MacroDefinitionWrapper> _macroDefs;

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate       _delegate;

  /**
	 * 
	 */
  public ExecuteJdtProjectTask() {
    this._delegate = new MacroExecutionDelegate(this);
    this._macroDefs = new LinkedList<MacroDefinitionWrapper>();
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameOrProjectSet();

    final EclipseProject eclipseProject = getEclipseProject();

    // TODO: ASSERT
    final JavaProjectRole javaProjectRole = JavaProjectRole.Helper.getJavaProjectRole(eclipseProject);

    // TODO: CONTAINER ARGS
    final ResolvedClasspath resolvedClasspath = JdtResolver.resolveProjectClasspath(getEclipseProject(), false, false,
        new Properties());

    final File[] cp = resolvedClasspath.getClasspathFiles();
    final String resolvedpath = TaskHelper.convertToString(cp, File.pathSeparator, File.separator, getProject());

    for (final MacroDefinitionWrapper macroDefinitionWrapper : this._macroDefs) {

      final MacroDef macroDef = macroDefinitionWrapper.getMacroDef();

      if (macroDefinitionWrapper.isSourceDirectoryScope()) {
        executeSourceDirectoryScopedMacroDef(macroDef, javaProjectRole, resolvedpath);
      } else if (macroDefinitionWrapper.isTargetDirectoryScope()) {
        executeOutputDirectoryScopedMacroDef(macroDef, javaProjectRole, resolvedpath);
      }
    }
  }

  public Object createForEachSourceDirectory() {
    return createMacroDef(SOURCE_DIRECTORY_SCOPE);
  }

  public Object createForEachOutputDirectory() {
    return createMacroDef(TARGET_DIRECTORY_SCOPE);
  }

  public Object createForAllSourceDirectories() {
    return createMacroDef(SOURCE_DIRECTORIES_SCOPE);
  }

  public Object createForAllOutputDirectories() {
    return createMacroDef(TARGET_DIRECTORIES_SCOPE);
  }

  private Object createMacroDef(final int scope) {
    final MacroDef macroDef = this._delegate.createMacroDef();
    this._macroDefs.add(new MacroDefinitionWrapper(macroDef, scope));
    return macroDef.createSequential();
  }

  private void executeSourceDirectoryScopedMacroDef(final MacroDef macroDef, final JavaProjectRole javaProjectRole,
      final String cp) {

    final String[] sourceFolders = javaProjectRole.getSourceFolders();

    for (final String sourceFolder : sourceFolders) {

      final Map<String, String> scopedProperties = new HashMap<String, String>();
      scopedProperties.put("source.directory", sourceFolder);
      scopedProperties.put("output.directory", javaProjectRole.getOutputFolderForSourceFolder(sourceFolder));
      scopedProperties.put("default.output.directory", javaProjectRole.getDefaultOutputFolder());
      scopedProperties.put("classpath", cp);

      this._delegate.executeMacroInstance(macroDef, "executeJdtProject", scopedProperties);
    }
  }

  private void executeOutputDirectoryScopedMacroDef(final MacroDef macroDef, final JavaProjectRole javaProjectRole,
      final String cp) {

    final String[] sourceFolders = javaProjectRole.getSourceFolders();

    for (final String sourceFolder : sourceFolders) {

      final Map<String, String> scopedProperties = new HashMap<String, String>();
      scopedProperties.put("output.directory", javaProjectRole.getOutputFolderForSourceFolder(sourceFolder));
      scopedProperties.put("default.output.directory", javaProjectRole.getDefaultOutputFolder());
      scopedProperties.put("classpath", cp);

      this._delegate.executeMacroInstance(macroDef, "executeJdtProject", scopedProperties);
    }
  }

  private class MacroDefinitionWrapper {

    private final MacroDef _macroDef;

    private final int      _scope;

    public MacroDefinitionWrapper(final MacroDef def, final int scope) {
      this._macroDef = def;
      this._scope = scope;
    }

    public MacroDef getMacroDef() {
      return this._macroDef;
    }

    public int getScope() {
      return this._scope;
    }

    public boolean isSourceDirectoryScope() {
      return this._scope == SOURCE_DIRECTORY_SCOPE;
    }

    public boolean isTargetDirectoryScope() {
      return this._scope == TARGET_DIRECTORY_SCOPE;
    }
  }
}
