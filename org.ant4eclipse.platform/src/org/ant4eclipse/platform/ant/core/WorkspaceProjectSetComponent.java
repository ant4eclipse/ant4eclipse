package org.ant4eclipse.platform.ant.core;

/**
 * <p>
 * Extends the interfaces {@link ProjectSetComponent} and {@link WorkspaceComponent} and allows to define all projects
 * in the workspace as a project set.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface WorkspaceProjectSetComponent extends ProjectSetComponent, WorkspaceComponent {

  /**
   * <p>
   * Set the 'allProjects' flag. If this flag is set to <code>true</code>, the method
   * {@link ProjectSetComponent#getProjectNames()} returns the names of all projects contained in the underlying
   * workspace (regardless if a project set or project names are defined).
   * </p>
   * 
   * @param allprojects
   *          the flag
   */
  void setAllProjects(final boolean allprojects);

  /**
   * <p>
   * Returns the 'allProjects' flag.
   * </p>
   * 
   * @return the 'allProjects' flag.
   */
  boolean isAllProjects();

  /**
   * <p>
   * Throws an exception if neither <code>allProject</code> nor <code>projectSet</code> nor <code>projectNames</code>
   * are set.
   * </p>
   * 
   */
  void requireAllProjectsOrProjectSetOrProjectNamesSet();
}
