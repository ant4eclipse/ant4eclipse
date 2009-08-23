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
package org.ant4eclipse.pydt.test.data;

/**
 * Descriptional datastructure allowing to compare the results of the ant tasks.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProjectDescription {

  private String _primaryprojectname;

  private String _secondaryprojectname;

  /**
   * Initialises this datastructure.
   */
  public ProjectDescription() {
    _primaryprojectname = null;
    _secondaryprojectname = null;
  }

  /**
   * Changes the primary (main) project name.
   * 
   * @param primaryprojectname
   *          The primary project name. Neither <code>null</code> nor empty.
   */
  public void setPrimaryProjectname(final String primaryprojectname) {
    _primaryprojectname = primaryprojectname;
  }

  /**
   * Returns the primary (main) project name.
   * 
   * @return The primary (main) project name. Neither <code>null</code> nor empty.
   */
  public String getPrimaryProjectname() {
    return _primaryprojectname;
  }

  /**
   * Changes the secondary project name.
   * 
   * @param secondaryprojectname
   *          The secondary project name. Neither <code>null</code> nor empty.
   */
  public void setSecondaryProjectname(final String secondaryprojectname) {
    _secondaryprojectname = secondaryprojectname;
  }

  /**
   * Returns the secondary project name.
   * 
   * @return The secondary project name. Neither <code>null</code> nor empty.
   */
  public String getSecondaryProjectname() {
    return _secondaryprojectname;
  }

} /* ENDCLASS */
