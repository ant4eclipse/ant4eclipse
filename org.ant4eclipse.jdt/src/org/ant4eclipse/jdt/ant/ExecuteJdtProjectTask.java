package org.ant4eclipse.jdt.ant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.ant4eclipse.core.ant.delegate.MacroExecutionDelegate;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.platform.ant.base.AbstractProjectPathTask;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * @author
 */
public class ExecuteJdtProjectTask extends AbstractProjectPathTask {

  private enum Scope {
    SOURCE_DIRECTORY_SCOPE, TARGET_DIRECTORY_SCOPE, SOURCE_DIRECTORIES_SCOPE, TARGET_DIRECTORIES_SCOPE;
  }

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate      _delegate;

  /* list of all macro definitions */
  private final List<ScopedMacroDefinition> _macroDefs;

  /** - */
  private JavaProjectRole                   _javaProjectRole;

  private Map<String, String>               _scopedProperties;

  /**
	 * 
	 */
  public ExecuteJdtProjectTask() {
    this._delegate = new MacroExecutionDelegate(this);
    this._macroDefs = new LinkedList<ScopedMacroDefinition>();
  }

  /**
   * @return
   */
  public Object createForEachSourceDirectory() {
    return createMacroDef(Scope.SOURCE_DIRECTORY_SCOPE);
  }

  /**
   * @return
   */
  public Object createForEachOutputDirectory() {
    return createMacroDef(Scope.TARGET_DIRECTORY_SCOPE);
  }

  /**
   * @return
   */
  public Object createForAllSourceDirectories() {
    return createMacroDef(Scope.SOURCE_DIRECTORIES_SCOPE);
  }

  /**
   * @return
   */
  public Object createForAllOutputDirectories() {
    return createMacroDef(Scope.TARGET_DIRECTORIES_SCOPE);
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // set java project role
    this._javaProjectRole = JavaProjectRole.Helper.getJavaProjectRole(getEclipseProject());

    // create general purpose scoped properties
    // TODO: CONTAINER ARGS
    this._scopedProperties = createGeneralPurposeScopedProperties(new Properties());

    // execute scoped macro definitions
    for (final ScopedMacroDefinition scopedMacroDefinition : this._macroDefs) {

      final MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      switch (scopedMacroDefinition.getScope()) {
      case SOURCE_DIRECTORY_SCOPE:
        executeSourceDirectoryScopedMacroDef(macroDef);
        break;
      case TARGET_DIRECTORY_SCOPE:
        executeOutputDirectoryScopedMacroDef(macroDef);
        break;
      case SOURCE_DIRECTORIES_SCOPE:

        break;
      case TARGET_DIRECTORIES_SCOPE:

        break;
      default:
        // TODO
        throw new RuntimeException("");
      }
    }
  }

  private Map<String, String> createGeneralPurposeScopedProperties(final Properties containerArgs) {

    final Map<String, String> scopedProperties = new HashMap<String, String>();

    // platform-specific properties
    scopedProperties.put("project.name", getEclipseProject().getSpecifiedName());
    scopedProperties.put("project.directory", convertToString(getEclipseProject().getFolder()));
    // scopedProperties.put("project.directory.path", convertToPath(getEclipseProject().getFolder()));

    // jdt-specific properties
    scopedProperties.put("classpath.absolute.compiletime", resolveClasspath(false, false, containerArgs));
    scopedProperties.put("classpath.relative.compiletime", resolveClasspath(true, false, containerArgs));
    scopedProperties.put("classpath.absolute.runtime", resolveClasspath(false, true, containerArgs));
    scopedProperties.put("classpath.relative.runtime", resolveClasspath(true, true, containerArgs));
    scopedProperties.put("default.output.directory", this._javaProjectRole.getDefaultOutputFolder());

    //
    return scopedProperties;
  }

  private String resolveClasspath(final boolean resolveRelative, final boolean isRuntimeClasspath,
      final Properties containerArgs) {

    final ResolvedClasspath resolvedClasspath = JdtResolver.resolveProjectClasspath(getEclipseProject(),
        resolveRelative, isRuntimeClasspath, containerArgs);

    return convertToString(resolvedClasspath.getClasspathFiles());
  }

  private Object createMacroDef(final Scope scope) {
    final MacroDef macroDef = this._delegate.createMacroDef();
    this._macroDefs.add(new ScopedMacroDefinition(macroDef, scope));
    return macroDef.createSequential();
  }

  /**
   * @param macroDef
   * @param javaProjectRole
   * @param classpathes
   */
  private void executeSourceDirectoryScopedMacroDef(final MacroDef macroDef) {

    final String[] sourceFolders = this._javaProjectRole.getSourceFolders();

    for (final String sourceFolder : sourceFolders) {

      final Map<String, String> scopedProperties = new HashMap<String, String>();
      scopedProperties.put("source.directory", getEclipseProject().getChild(sourceFolder).getAbsolutePath());
      scopedProperties.put("output.directory", this._javaProjectRole.getOutputFolderForSourceFolder(sourceFolder));
      scopedProperties.putAll(this._scopedProperties);

      this._delegate.executeMacroInstance(macroDef, "executeJdtProject", scopedProperties,
          new HashMap<String, Object>());
    }
  }

  // private void executeSourceDirectoriesScopedMacroDef(final MacroDef macroDef) {
  //
  // final String[] sourceFolders = this._javaProjectRole.getSourceFolders();
  //
  // TaskHelper.convertToString(sourceFolders, getPathSeparator(), getDirSeparator(), this.project);
  //
  // final Map<String, String> scopedProperties = new HashMap<String, String>();
  // scopedProperties.put("source.directory", sourceFolder);
  // scopedProperties.put("output.directory", this._javaProjectRole.getOutputFolderForSourceFolder(sourceFolder));
  // scopedProperties.putAll(this._scopedProperties);
  //
  // this._delegate.executeMacroInstance(macroDef, "executeJdtProject", scopedProperties);
  // }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeOutputDirectoryScopedMacroDef(final MacroDef macroDef) {

    final String[] outFolders = this._javaProjectRole.getAllOutputFolders();

    for (final String outFolder : outFolders) {

      final Map<String, String> scopedProperties = new HashMap<String, String>();
      scopedProperties.put("output.directory", outFolder);
      scopedProperties.putAll(this._scopedProperties);

      this._delegate.executeMacroInstance(macroDef, "executeJdtProject", scopedProperties,
          new HashMap<String, Object>());
    }
  }

  /**
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class ScopedMacroDefinition {

    /** the macro definition */
    private final MacroDef _macroDef;

    /** the scope of the macro definition */
    private final Scope    _scope;

    /**
     * @param def
     * @param scope
     */
    public ScopedMacroDefinition(final MacroDef def, final Scope scope) {
      this._macroDef = def;
      this._scope = scope;
    }

    /**
     * @return
     */
    public MacroDef getMacroDef() {
      return this._macroDef;
    }

    /**
     * @return
     */
    public Scope getScope() {
      return this._scope;
    }
  }
}
