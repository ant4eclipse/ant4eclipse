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

import org.ant4eclipse.ant.core.AbstractAnt4EclipseTask;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.WorkspaceRegistry;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * Allow scripts to refresh a specified workspace definition
 * 
 * @author Nils Hartmann
 * 
 */
public class RefreshWorkspaceTask extends AbstractAnt4EclipseTask {

  private String _workspaceId;

  @Override
  protected void doExecute() {
    ServiceRegistryAccess.instance().getService(WorkspaceRegistry.class).refreshWorkspace(getWorkspaceId());
  }

  @Override
  protected void preconditions() throws BuildException {
    if (this._workspaceId == null || this._workspaceId.length() == 0) {
      throw new BuildException("Parameter 'workspaceId' must be set");
    }
  }

  public String getWorkspaceId() {
    return this._workspaceId;
  }

  /**
   * Set the id of the workspace that should be refreshed
   * 
   * @param workspaceId
   */
  public void setWorkspaceId(String workspaceId) {
    this._workspaceId = workspaceId;
  }

}
