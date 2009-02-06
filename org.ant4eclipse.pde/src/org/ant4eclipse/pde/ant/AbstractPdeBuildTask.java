/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.platform.ant.core.task.AbstractProjectBasedTask;

/**
 * <p>
 * Abstract base class for PDE build tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AbstractPdeBuildTask extends AbstractProjectBasedTask {

  @Override
  protected void doExecute() {
    // TODO Auto-generated method stub

  }

  // /** the target platform definition */
  // private TargetPlatformDefinitionDataType _targetPlatformDefinition;
  //
  // /**
  // * <p>
  // * Creates a new instance of type AbstractPdeBuildTask.
  // * </p>
  // *
  // */
  // public AbstractPdeBuildTask() {
  // super();
  // }
  //
  // /**
  // * <p>
  // * Returns the target platform against which the workspace plugins will be compiled and tested.
  // * </p>
  // *
  // * @return the target platform against which the workspace plugins will be compiled and tested.
  // */
  // public final File[] getTargetPlatformLocations() {
  // if (this._targetPlatformDefinition != null) {
  // return this._targetPlatformDefinition.getLocations();
  // } else {
  // return new File[] {};
  // }
  // }
  //
  // /**
  // * <p>
  // * Sets the target platform against which the workspace plugins will be compiled and tested.
  // * </p>
  // *
  // * @param targetPlatformLocation
  // * the target platform against which the workspace plugins will be compiled and tested.
  // */
  // public final void setTargetPlatformLocation(final File targetPlatformLocation) {
  // Assert.isDirectory(targetPlatformLocation);
  // final TargetPlatformDefinitionDataType platformDefinition = new TargetPlatformDefinitionDataType();
  // platformDefinition.addConfiguredLocation(new TargetPlatformDefinitionDataType.Location(targetPlatformLocation));
  // this.addTargetPlatform(platformDefinition);
  // }
  //
  // public final void addTargetPlatform(final TargetPlatformDefinitionDataType platformDefinition) {
  // Assert.notNull(platformDefinition);
  //
  // this._targetPlatformDefinition = platformDefinition;
  // }
  //
  // /**
  // * <p>
  // * Returns whether the target platform location is set.
  // * </p>
  // *
  // * @return whether the target platform location is set.
  // */
  // public final boolean isTargetPlatformLocationSet() {
  // return this._targetPlatformDefinition != null;
  // }
}
