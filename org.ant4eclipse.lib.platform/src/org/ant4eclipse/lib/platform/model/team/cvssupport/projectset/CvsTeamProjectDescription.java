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
package org.ant4eclipse.lib.platform.model.team.cvssupport.projectset;

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.team.cvssupport.CvsRoot;
import org.ant4eclipse.lib.platform.model.team.projectset.internal.AbstractTeamProjectDescription;

public class CvsTeamProjectDescription extends AbstractTeamProjectDescription {

  /** the cvsRoot for the project * */
  private CvsRoot _cvsRoot;

  /** the name of the project in the repository */
  private String  _nameInRepository;

  /** the name of the branch or version */
  private String  _branchOrVersionTag;

  /** the cvs user */
  private String  _cvsUser = null;

  /** the cvs password */

  /**
   * Creates a new instance of type TeamProjectDescription
   * 
   * @param projectname
   *          the name of the project
   * @param cvsroot
   *          the cvsroot of the project
   * @param nameInRepository
   *          the name of the project in the repository
   * @param tag
   *          the tag
   */
  
  // Assure.notNull( "cvsroot", cvsroot );
  // Assure.notNull( "nameInRepository", nameInRepository );
  public CvsTeamProjectDescription( String projectname, CvsRoot cvsroot, String nameInRepository, String tag ) {
    super( projectname );
    _cvsRoot = cvsroot;
    _nameInRepository = nameInRepository;
    _branchOrVersionTag = tag;
    if( "".equals( _branchOrVersionTag ) ) {
      _branchOrVersionTag = null;
    }
  }

  /**
   * Creates a new instance of type TeamProjectDescription
   * 
   * @param projectname
   *          the name of the project
   * @param cvsroot
   *          the cvsroot of the project
   * @param nameInRepository
   *          the name of the project in the repository
   * @param tag
   *          the tag
   */
  public CvsTeamProjectDescription( String projectname, String cvsroot, String nameInRepository, String tag ) {
    this( projectname, new CvsRoot( cvsroot ), nameInRepository, tag );
  }

  /**
   * Returns the tag of the branch or version.
   * 
   * @return Returns the tag of the branch or version.
   */
  public String getBranchOrVersionTag() {
    return _branchOrVersionTag;
  }

  /**
   * Returns the cvsRoot.
   * 
   * @return Returns the cvsRoot.
   */
  public CvsRoot getCvsRoot() {
    return _cvsRoot;
  }

  /**
   * Returns the resolved CvsRoot (e.g. :pserver:user:pwd@localhost:C:/cvsRepository). Requires that
   * isCvsUserAndPasswordSet() returns <code>true</code>. If isCvsUserAndPasswordSet() returns <code>false</code>, a
   * PreconditionViolatedException will be thrown.
   * 
   * @return Returns the resolved CvsRoot.
   */
  // Assure.assertTrue( isCvsUserSet(), "CvsUser and CvsPwd have to be set!" );
  public CvsRoot getResolvedCvsRoot() {
    return _cvsRoot.getResolvedRoot( _cvsUser, _cvsPwd );
  }

  private String _cvsPwd = null;

  /**
   * Returns the name of the project in the repository.
   * 
   * @return Returns the name of the project in the repository.
   */
  public String getNameInRepository() {
    return _nameInRepository;
  }

  /**
   * Returns whether the project is from the cvs head.
   * 
   * @return Returns whether the project is from the cvs head.
   */
  public boolean isHead() {
    return !hasBranchOrVersionTag();
  }

  /**
   * Returns whether the project has a branch or version tag.
   * 
   * @return Returns whether the project has a branch or version tag.
   */
  public boolean hasBranchOrVersionTag() {
    return _branchOrVersionTag != null;
  }

  /**
   * Returns whether the cvs user and the cvs password is set.
   * 
   * @return Returns whether the cvs user and the cvs password is set.
   */
  public boolean isCvsUserSet() {
    return _cvsUser != null;
  }

  /**
   * Sets the cvs user und password.
   * 
   * @param cvsUser
   *          the cvs user.
   * @param cvsPwd
   *          the cvs password might be null
   */
  // Assure.notNull( "cvsUser", cvsUser );
  public void setCvsUserAndPassword( String cvsUser, String cvsPwd ) {
    A4ELogging.debug( "setCvsUserAndPassword(%s, %s)", cvsUser, cvsPwd );
    _cvsUser = cvsUser;
    _cvsPwd = cvsPwd;
  }

  // Assure.notNull( "properties", properties );
  @Override
  protected void addSpecificProperties( StringMap properties ) {

    // add basic properties
    properties.put( "cvs.nameInRepository", _nameInRepository );
    properties.put( "cvs.isHead", Boolean.toString( isHead() ) );
    properties.put( "cvs.hasBranchOrVersionTag", Boolean.toString( hasBranchOrVersionTag() ) );
    if( hasBranchOrVersionTag() ) {
      properties.put( "cvs.branchOrTag", getBranchOrVersionTag() );
    }

    // add cvsRoot as properties
    CvsRoot cvsRoot = getCvsRoot();
    properties.put( "cvs.cvsRoot", cvsRoot.toString() );
    properties.put( "cvs.cvsRoot.connectionType", cvsRoot.getConnectionType() );
    properties.put( "cvs.cvsRoot.host", cvsRoot.getHost() );
    properties.put( "cvs.cvsRoot.repository", cvsRoot.getRepository() );
    final boolean hasUser = cvsRoot.hasUser();
    properties.put( "cvs.cvsRoot.hasUser", Boolean.toString( hasUser ) );
    if( hasUser ) {
      properties.put( "cvs.cvsRoot.user", cvsRoot.getUser() );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[CvsTeamProjectDescription: projectname: %s cvsRoot: %s nameInRepository: %s branchOrVersionTag: %s cvsUser: %s cvsPwd: %s]",
        getProjectName(), _cvsRoot, _nameInRepository, _branchOrVersionTag, _cvsUser, _cvsPwd );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( !super.equals( o ) ) {
      return false;
    }
    if( o == null ) {
      return false;
    }
    if( o.getClass() != getClass() ) {
      return false;
    }
    CvsTeamProjectDescription castedObj = (CvsTeamProjectDescription) o;
    return((_cvsRoot == null ? castedObj._cvsRoot == null : _cvsRoot.equals( castedObj._cvsRoot ))
        && (_nameInRepository == null ? castedObj._nameInRepository == null : _nameInRepository
            .equals( castedObj._nameInRepository ))
        && (_branchOrVersionTag == null ? castedObj._branchOrVersionTag == null : _branchOrVersionTag
            .equals( castedObj._branchOrVersionTag ))
        && (_cvsUser == null ? castedObj._cvsUser == null : _cvsUser.equals( castedObj._cvsUser )) && (_cvsPwd == null ? castedObj._cvsPwd == null
        : _cvsPwd.equals( castedObj._cvsPwd )));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = 31 * hashCode + (_cvsRoot == null ? 0 : _cvsRoot.hashCode());
    hashCode = 31 * hashCode + (_nameInRepository == null ? 0 : _nameInRepository.hashCode());
    hashCode = 31 * hashCode + (_branchOrVersionTag == null ? 0 : _branchOrVersionTag.hashCode());
    hashCode = 31 * hashCode + (_cvsUser == null ? 0 : _cvsUser.hashCode());
    hashCode = 31 * hashCode + (_cvsPwd == null ? 0 : _cvsPwd.hashCode());
    return hashCode;
  }

} /* ENDCLASS */
