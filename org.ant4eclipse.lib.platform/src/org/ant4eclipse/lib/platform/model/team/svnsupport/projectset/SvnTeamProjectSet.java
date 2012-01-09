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
package org.ant4eclipse.lib.platform.model.team.svnsupport.projectset;

import org.ant4eclipse.lib.platform.model.team.projectset.internal.AbstractTeamProjectSet;

/**
 * Represents a Team Project Set that is based on a Subversion repository
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class SvnTeamProjectSet extends AbstractTeamProjectSet {

  /**
   * Username that should be used when executing a SVN operation on this TeamProjectSet.
   * 
   * Might be null
   */
  private String _user;

  /**
   * Password that should be used when executing a SVN operation on this TeamProjectSet.
   * 
   * Might be null
   */
  private String _password;

  public SvnTeamProjectSet( String name ) {
    super( name );
  }

  /**
   * Adds the SvnTeamProjectDescription to this team project set
   * 
   * @param description
   *          the description to add.
   */
  // Assure.notNull( "description", description );
  public void addTeamProjectDescription( SvnTeamProjectDescription description ) {
    super.addTeamProjectDescription( description );
  }

  public boolean isCvsProjectSet() {
    return false;
  }

  public boolean isSvnProjectSet() {
    return true;
  }

  public boolean hasUser() {
    return _user != null;
  }

  public boolean hasPassword() {
    return _password != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserAndPassword( String user, String pwd ) {
    _user = user;
    _password = pwd;
  }

  public String getPassword() {
    return _password;
  }

  public String getUser() {
    return _user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[SvnTeamProjectSet: _name: %s _projectDescriptions: %s _user: %s _password: %s]",
        getName(), getProjectDescriptions(), _user, _password );
  }
  
} /* ENDCLASS */
