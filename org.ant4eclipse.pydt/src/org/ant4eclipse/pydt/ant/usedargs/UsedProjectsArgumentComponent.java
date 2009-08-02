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
package org.ant4eclipse.pydt.ant.usedargs;

/**
 * Subelement representation used as an python specific argument for the GetUsedProjectsTask.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class UsedProjectsArgumentComponent {

  private boolean _export = true;

  /**
   * Enables/disable resolving of exported projects.
   * 
   * @param newexport
   *          <code>true</code> <=> Resolve exported projects, too.
   */
  public void setExport(boolean newexport) {
    _export = newexport;
  }

  /**
   * Returns <code>true</code> if exported projects shall be resolved, too.
   * 
   * @return <code>true</code> <=> Resolve exported projects, too.
   */
  public boolean isExport() {
    return _export;
  }

} /* ENDCLASS */
