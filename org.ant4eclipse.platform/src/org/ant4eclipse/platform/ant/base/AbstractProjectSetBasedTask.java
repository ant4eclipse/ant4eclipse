package org.ant4eclipse.platform.ant.base;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.platform.ant.delegate.ProjectSetDelegate;
import org.ant4eclipse.platform.ant.delegate.WorkspaceDelegate;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

public abstract class AbstractProjectSetBasedTask extends AbstractAnt4EclipseTask {

  private final ProjectSetDelegate _projectSetDelegate;

  private final WorkspaceDelegate  _workspaceDelegate;

  public AbstractProjectSetBasedTask() {
    this._projectSetDelegate = new ProjectSetDelegate(this);
    this._workspaceDelegate = new WorkspaceDelegate(this);
  }

  public final String[] getProjectNames() {
    return this._projectSetDelegate.getProjectNames();
  }

  public final TeamProjectSet getTeamProjectSet() {
    return this._projectSetDelegate.getTeamProjectSet();
  }

  public final boolean isProjectNamesSet() {
    return this._projectSetDelegate.isProjectNamesSet();
  }

  public final boolean isTeamProjectSetSet() {
    return this._projectSetDelegate.isTeamProjectSetSet();
  }

  public final void requireProjectNamesSet() {
    this._projectSetDelegate.requireProjectNamesSet();
  }

  public final void requireTeamProjectSetSet() {
    this._projectSetDelegate.requireTeamProjectSetSet();
  }

  public final void requireTeamProjectSetOrProjectNamesSet() {
    this._projectSetDelegate.requireTeamProjectSetOrProjectNamesSet();
  }

  public final void setProjectNames(String projectNames) {
    this._projectSetDelegate.setProjectNames(projectNames);
  }

  public final void setTeamProjectSet(File projectSet) {
    this._projectSetDelegate.setTeamProjectSet(projectSet);
  }

  public Workspace getWorkspace() {
    return this._workspaceDelegate.getWorkspace();
  }

  public File getWorkspaceDirectory() {
    return this._workspaceDelegate.getWorkspaceDirectory();
  }

  public boolean isWorkspaceDirectorySet() {
    return this._workspaceDelegate.isWorkspaceSet();
  }

  public boolean isWorkspaceSet() {
    return this._workspaceDelegate.isWorkspaceSet();
  }

  public void requireWorkspaceSet() {
    this._workspaceDelegate.requireWorkspaceSet();
  }

  public void setWorkspace(File workspace) {
    this._workspaceDelegate.setWorkspace(workspace);
  }

  public void setWorkspaceDirectory(File workspaceDirectory) {
    this._workspaceDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

}
