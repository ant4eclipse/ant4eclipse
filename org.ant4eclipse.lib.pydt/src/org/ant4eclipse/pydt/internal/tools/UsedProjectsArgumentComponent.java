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
package org.ant4eclipse.pydt.internal.tools;

/**
 * Subelement representation used as an python specific argument for the GetUsedProjectsTask.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class UsedProjectsArgumentComponent {

  /**
   * EnumeratedAttribute used to control the resolving process.
   */
  public static enum Mode {

    all, exported, direct;

  } /* ENDENUM */

  public static final UsedProjectsArgumentComponent DEFAULT = new UsedProjectsArgumentComponent();

  /** resolve projects that are declared as exported (thus their dependencies) */
  private boolean                                   _export = false;

  /** generally resolve all referenced projects */
  private boolean                                   _all    = true;

  /** do not resolve dependencies of referenced projects (even if they're declared as exported) */
  private boolean                                   _direct = false;

  /**
   * Only return directly referenced projects.
   * 
   * @return <code>true</code> <=> Directly returned projects.
   */
  public boolean isDirect() {
    return this._direct;
  }

  /**
   * Returns <code>true</code> if projects generally shall be resolved.
   * 
   * @return <code>true</code> <=> Projects shall be resolved in general.
   */
  public boolean isAll() {
    return this._all;
  }

  /**
   * Returns <code>true</code> if exported projects shall be resolved, too.
   * 
   * @return <code>true</code> <=> Resolve exported projects, too.
   */
  public boolean isExport() {
    return this._export;
  }

  /**
   * Changes the current resolving mode.
   * 
   * @param mode
   *          The new resolving mode.
   */
  public void setMode(Mode mode) {
    this._all = false;
    this._export = false;
    this._direct = false;
    if (mode != null) {
      if (mode == Mode.exported) {
        this._export = true;
      } else if (mode == Mode.direct) {
        this._direct = true;
      } else /* if (mode == Mode.all) */{
        this._all = true;
      }
    } else {
      // the default is 'all'
      this._all = true;
    }
  }

} /* ENDCLASS */
