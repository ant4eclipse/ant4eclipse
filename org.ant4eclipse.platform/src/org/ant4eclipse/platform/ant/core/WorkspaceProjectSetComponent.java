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
   * @param allWorkspaceProjects
   *          the flag
   */
  void setAllWorkspaceProjects(boolean allWorkspaceProjects);

  /**
   * <p>
   * Returns the 'allWorkspaceProjects' flag.
   * </p>
   * 
   * @return the 'allWorkspaceProjects' flag.
   */
  boolean isAllWorkspaceProjects();

  /**
   * <p>
   * Throws an exception if neither <code>allWorkspaceProjects</code> nor <code>projectSet</code> nor
   * <code>projectNames</code> are set.
   * </p>
   * 
   */
  void requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet();
}
