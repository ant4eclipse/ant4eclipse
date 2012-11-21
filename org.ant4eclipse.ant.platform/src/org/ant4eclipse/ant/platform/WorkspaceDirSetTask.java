/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.platform;

import java.io.File;
import java.util.List;

import org.ant4eclipse.ant.platform.core.WorkspaceComponent;
import org.ant4eclipse.ant.platform.core.delegate.WorkspaceDelegate;
import org.ant4eclipse.ant.platform.core.task.AbstractAnt4EclipseResourceCollection;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * Exposes all project directories of a specified Workspace as an Ant {@link ResourceCollection}
 * 
 * @author Nils Hartmann
 * 
 */
public class WorkspaceDirSetTask extends AbstractAnt4EclipseResourceCollection implements WorkspaceComponent {

  private WorkspaceDelegate _workspaceDelegate;

  public WorkspaceDirSetTask(Project project) {
    super(project);

    this._workspaceDelegate = new WorkspaceDelegate(this);

  }

  @Override
  public void setRefid(Reference ref) {
    if (isWorkspaceDirectorySet() || isWorkspaceIdSet()) {
      throw tooManyAttributes();
    }

    super.setRefid(ref);
  }

  @Deprecated
  public void setWorkspace(String workspace) {
    this._workspaceDelegate.setWorkspace(workspace);

  }

  public void setWorkspaceDirectory(String workspaceDirectory) {
    this._workspaceDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  public File getWorkspaceDirectory() {
    return this._workspaceDelegate.getWorkspaceDirectory();
  }

  public boolean isWorkspaceDirectorySet() {
    return this._workspaceDelegate.isWorkspaceDirectorySet();
  }

  public void setWorkspaceId(String identifier) {
    this._workspaceDelegate.setWorkspaceId(identifier);
  }

  public String getWorkspaceId() {
    return this._workspaceDelegate.getWorkspaceId();
  }

  public boolean isWorkspaceIdSet() {
    return this._workspaceDelegate.isWorkspaceIdSet();
  }

  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    this._workspaceDelegate.requireWorkspaceDirectoryOrWorkspaceIdSet();

  }

  public Workspace getWorkspace() {
    return this._workspaceDelegate.getWorkspace();
  }

  @Override
  protected void doComputeFileSet(List<Resource> resourceList) {
    EclipseProject[] allProjects = getWorkspace().getAllProjects();

    for (EclipseProject project : allProjects) {

      FileResource fileResource = new FileResource(project.getFolder());
      resourceList.add(fileResource);
    }

  }
}
