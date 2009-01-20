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
package org.ant4eclipse.platform.ant.core.task;

import java.io.File;

import org.ant4eclipse.platform.ant.core.PathComponent;
import org.ant4eclipse.platform.ant.core.delegate.PathDelegate;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Abstract base class for all tasks that resolve paths from an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectPathTask extends AbstractProjectBasedTask implements PathComponent {

  private final PathDelegate _pathDelegate;

  public AbstractProjectPathTask() {
    super();

    this._pathDelegate = new PathDelegate(this);
  }

  public final String getDirSeparator() {
    return this._pathDelegate.getDirSeparator();
  }

  public final String getPathSeparator() {
    return this._pathDelegate.getPathSeparator();
  }

  public final boolean isDirSeparatorSet() {
    return this._pathDelegate.isDirSeparatorSet();
  }

  public final boolean isPathSeparatorSet() {
    return this._pathDelegate.isPathSeparatorSet();
  }

  public final void setDirSeparator(String newdirseparator) {
    this._pathDelegate.setDirSeparator(newdirseparator);
  }

  public final void setPathSeparator(String newpathseparator) {
    this._pathDelegate.setPathSeparator(newpathseparator);
  }

  public final Path convertToPath(File[] entries) {
    return this._pathDelegate.convertToPath(entries);
  }

  public final String convertToString(File[] entries) {
    return this._pathDelegate.convertToString(entries);
  }

  public final Path convertToPath(File entry) {
    return this._pathDelegate.convertToPath(entry);
  }

  public final String convertToString(File entry) {
    return this._pathDelegate.convertToString(entry);
  }
}
