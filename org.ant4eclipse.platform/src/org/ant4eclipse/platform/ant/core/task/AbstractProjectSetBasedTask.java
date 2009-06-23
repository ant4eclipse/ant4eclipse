package org.ant4eclipse.platform.ant.core.task;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.platform.ant.core.ProjectSetComponent;
import org.ant4eclipse.platform.ant.core.delegate.ProjectSetDelegate;
import org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

/**
 * <p>
 * Abstract base class for project set based tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectSetBasedTask extends AbstractAnt4EclipseTask implements ProjectSetComponent {

  /** the project set delegate */
  private final ProjectSetDelegate _projectSetDelegate;

  /** the workspace delegate */
  private final WorkspaceDelegate  _workspaceDelegate;

  /**
   * <p>
   * Create a new instance of type {@link AbstractProjectSetBasedTask}.
   * </p>
   */
  public AbstractProjectSetBasedTask() {
    // create the delegates
    this._projectSetDelegate = new ProjectSetDelegate(this);
    this._workspaceDelegate = new WorkspaceDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  public final String[] getProjectNames() {
    return this._projectSetDelegate.getProjectNames();
  }

  /**
   * {@inheritDoc}
   */
  public final TeamProjectSet getTeamProjectSet() {
    return this._projectSetDelegate.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isProjectNamesSet() {
    return this._projectSetDelegate.isProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isTeamProjectSetSet() {
    return this._projectSetDelegate.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireProjectNamesSet() {
    this._projectSetDelegate.requireProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireTeamProjectSetSet() {
    this._projectSetDelegate.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireTeamProjectSetOrProjectNamesSet() {
    this._projectSetDelegate.requireTeamProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void setProjectNames(String projectNames) {
    this._projectSetDelegate.setProjectNames(projectNames);
  }

  /**
   * {@inheritDoc}
   */
  public final void setTeamProjectSet(File projectSet) {
    this._projectSetDelegate.setTeamProjectSet(projectSet);
  }

  /**
   * {@inheritDoc}
   */
  public Workspace getWorkspace() {
    return this._workspaceDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public File getWorkspaceDirectory() {
    return this._workspaceDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isWorkspaceDirectorySet() {
    return this._workspaceDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isWorkspaceSet() {
    return this._workspaceDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireWorkspaceSet() {
    this._workspaceDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("deprecation")
  public void setWorkspace(File workspace) {
    this._workspaceDelegate.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public void setWorkspaceDirectory(File workspaceDirectory) {
    this._workspaceDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

}
