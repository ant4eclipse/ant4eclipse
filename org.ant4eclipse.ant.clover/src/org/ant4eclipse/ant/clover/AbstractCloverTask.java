package org.ant4eclipse.ant.clover;

import org.ant4eclipse.ant.platform.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.lib.core.util.ClassLoadingHelper;
import org.ant4eclipse.lib.jdt.internal.model.project.JavaProjectRoleImpl;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;

import com.cenqua.clover.CloverInstr;

/**
 * <p>
 * Abstract base class for all ant4eclipse clover tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractCloverTask extends AbstractProjectBasedTask {

  /** the CLOVER_INSTR postfix */
  protected static final String POSTFIX_CLOVER_INSTR = "_CLOVER_INSTR";

  /** the database path */
  private String                _databasePath;

  /**
   * <p>
   * Returns the database path.
   * </p>
   * 
   * @return
   */
  public final String getDatabasePath() {
    return _databasePath;
  }

  /**
   * <p>
   * Sets the database path.
   * </p>
   * 
   * @param databasePath
   */
  public final void setDatabasePath(String databasePath) {
    _databasePath = databasePath;
  }

  /**
   * <p>
   * Returns the java project role.
   * </p>
   * 
   * @return the java project role.
   */
  public final JavaProjectRoleImpl getJavaProjectRole() {
    return (JavaProjectRoleImpl) getEclipseProject().getRole(JavaProjectRole.class);
  }

  /**
   * <p>
   * Returns the plug-in project role.
   * </p>
   * 
   * @return the plug-in project role.
   */
  public final PluginProjectRole getPluginProjectRole() {
    return (PluginProjectRole) getEclipseProject().getRole(PluginProjectRole.class);
  }

  /**
   * <p>
   * Returns the {@link PluginBuildProperties}.
   * </p>
   * 
   * @return
   */
  public final PluginBuildProperties getPluginBuildProperties() {
    return getPluginProjectRole().getBuildProperties();
  }

  /**
   * <p>
   * Returns the clover path.
   * </p>
   * 
   * @return the clover path.
   */
  protected final String getCloverPath() {

    // get the class path entries
    String[] result = ClassLoadingHelper.getClasspathEntriesFor(CloverInstr.class);

    // find the 'clover.jar'
    for (String entry : result) {
      if (entry.endsWith("clover.jar")) {
        return entry;
      } else if (entry.contains("clover")) {
        return entry;
      }
    }

    // return the result
    return result[0];
  }
}
