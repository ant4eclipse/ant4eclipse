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
package org.ant4eclipse.ant.platform.core.task;


import org.ant4eclipse.ant.platform.core.PathComponent;
import org.ant4eclipse.ant.platform.core.delegate.PathDelegate;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * <p>
 * Abstract base class for all tasks that resolve paths from an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectPathTask extends AbstractProjectBasedTask implements PathComponent {

  /** the path delegate */
  private PathDelegate _pathDelegate;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractProjectPathTask}.
   * </p>
   */
  public AbstractProjectPathTask() {
    super();

    // create the delegate
    this._pathDelegate = new PathDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  public final String getDirSeparator() {
    return this._pathDelegate.getDirSeparator();
  }

  /**
   * {@inheritDoc}
   */
  public final String getPathSeparator() {
    return this._pathDelegate.getPathSeparator();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isDirSeparatorSet() {
    return this._pathDelegate.isDirSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPathSeparatorSet() {
    return this._pathDelegate.isPathSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void setDirSeparator(String newdirseparator) {
    this._pathDelegate.setDirSeparator(newdirseparator);
  }

  /**
   * {@inheritDoc}
   */
  public final void setPathSeparator(String newpathseparator) {
    this._pathDelegate.setPathSeparator(newpathseparator);
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File[] entries) {
    return this._pathDelegate.convertToPath(entries);
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File[] entries) {
    return this._pathDelegate.convertToString(entries);
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File entry) {
    return this._pathDelegate.convertToPath(entry);
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File entry) {
    return this._pathDelegate.convertToString(entry);
  }
}
