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
package org.ant4eclipse.ant.pde;

import java.io.File;
import java.util.List;

import org.ant4eclipse.ant.platform.core.task.AbstractAnt4EclipseResourceCollection;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;

public class TargetPlatformFileSet extends AbstractAnt4EclipseResourceCollection implements
    TargetPlatformAwareComponent {

  private TargetPlatformAwareDelegate _targetPlatformComponentDelegate;

  public TargetPlatformFileSet(Project project) {
    super(project);

    this._targetPlatformComponentDelegate = new TargetPlatformAwareDelegate();
  }

  @Override
  protected void doComputeFileSet(List<Resource> resourceList) {

    TargetPlatform targetPlatform = this._targetPlatformComponentDelegate.getTargetPlatform(null);

    List<File> allBundleFiles = targetPlatform.getAllBundleFiles();

    for (File file : allBundleFiles) {
      resourceList.add(new FileResource(file));
    }

  }

  public void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformComponentDelegate.setTargetPlatformId(targetPlatformId);
  }

  public boolean isTargetPlatformIdSet() {
    return this._targetPlatformComponentDelegate.isTargetPlatformIdSet();
  }

  public String getTargetPlatformId() {
    return this._targetPlatformComponentDelegate.getTargetPlatformId();
  }

  public void requireTargetPlatformIdSet() {
    this._targetPlatformComponentDelegate.requireTargetPlatformIdSet();
  }

  public void setPlatformConfigurationId(String platformConfigurationId) {
    this._targetPlatformComponentDelegate.setPlatformConfigurationId(platformConfigurationId);
  }

  public boolean isPlatformConfigurationIdSet() {
    return this._targetPlatformComponentDelegate.isPlatformConfigurationIdSet();
  }

  public String getPlatformConfigurationId() {
    return this._targetPlatformComponentDelegate.getPlatformConfigurationId();
  }

}
