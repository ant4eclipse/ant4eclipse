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
package org.ant4eclipse.platform.model.team.cvssupport.projectset;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.model.team.cvssupport.CvsRoot;
import org.ant4eclipse.platform.model.team.projectset.internal.AbstractTeamProjectDescription;

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
  public CvsTeamProjectDescription(String projectname, CvsRoot cvsroot, String nameInRepository, String tag) {
    super(projectname);
    Assert.notNull(cvsroot);
    Assert.notNull(nameInRepository);

    this._cvsRoot = cvsroot;
    this._nameInRepository = nameInRepository;
    this._branchOrVersionTag = tag;

    if ("".equals(this._branchOrVersionTag)) {
      this._branchOrVersionTag = null;
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
  public CvsTeamProjectDescription(String projectname, String cvsroot, String nameInRepository, String tag) {
    this(projectname, new CvsRoot(cvsroot), nameInRepository, tag);
  }

  /**
   * Returns the tag of the branch or version.
   * 
   * @return Returns the tag of the branch or version.
   */
  public String getBranchOrVersionTag() {
    return this._branchOrVersionTag;
  }

  /**
   * Returns the cvsRoot.
   * 
   * @return Returns the cvsRoot.
   */
  public CvsRoot getCvsRoot() {
    return this._cvsRoot;
  }

  /**
   * Returns the resolved CvsRoot (e.g. :pserver:user:pwd@localhost:C:/cvsRepository). Requires that
   * isCvsUserAndPasswordSet() returns <code>true</code>. If isCvsUserAndPasswordSet() returns <code>false</code>, a
   * PreconditionViolatedException will be thrown.
   * 
   * @return Returns the resolved CvsRoot.
   */
  public CvsRoot getResolvedCvsRoot() {
    Assert.assertTrue(isCvsUserSet(), "CvsUser and CvsPwd have to be set!");

    return this._cvsRoot.getResolvedRoot(this._cvsUser, this._cvsPwd);
  }

  private String _cvsPwd = null;

  /**
   * Returns the name of the project in the repository.
   * 
   * @return Returns the name of the project in the repository.
   */
  public String getNameInRepository() {
    return this._nameInRepository;
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
    return this._branchOrVersionTag != null;
  }

  /**
   * Returns whether the cvs user and the cvs password is set.
   * 
   * @return Returns whether the cvs user and the cvs password is set.
   */
  public boolean isCvsUserSet() {
    return this._cvsUser != null;
  }

  /**
   * Sets the cvs user und password.
   * 
   * @param cvsUser
   *          the cvs user.
   * @param cvsPwd
   *          the cvs password might be null
   */
  public void setCvsUserAndPassword(String cvsUser, String cvsPwd) {
    Assert.notNull(cvsUser);

    A4ELogging.debug("setCvsUserAndPassword(%s, %s)", cvsUser, cvsPwd);

    this._cvsUser = cvsUser;
    this._cvsPwd = cvsPwd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[CvsTeamProjectDescription:");
    buffer.append(" projectname: ");
    buffer.append(getProjectName());
    buffer.append(" cvsRoot: ");
    buffer.append(this._cvsRoot);
    buffer.append(" nameInRepository: ");
    buffer.append(this._nameInRepository);
    buffer.append(" branchOrVersionTag: ");
    buffer.append(this._branchOrVersionTag);
    buffer.append(" cvsUser: ");
    buffer.append(this._cvsUser);
    buffer.append(" cvsPwd: ");
    buffer.append(this._cvsPwd);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!super.equals(o)) {
      return false;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    CvsTeamProjectDescription castedObj = (CvsTeamProjectDescription) o;
    return ((this._cvsRoot == null ? castedObj._cvsRoot == null : this._cvsRoot.equals(castedObj._cvsRoot))
        && (this._nameInRepository == null ? castedObj._nameInRepository == null : this._nameInRepository
            .equals(castedObj._nameInRepository))
        && (this._branchOrVersionTag == null ? castedObj._branchOrVersionTag == null : this._branchOrVersionTag
            .equals(castedObj._branchOrVersionTag))
        && (this._cvsUser == null ? castedObj._cvsUser == null : this._cvsUser.equals(castedObj._cvsUser)) && (this._cvsPwd == null ? castedObj._cvsPwd == null
        : this._cvsPwd.equals(castedObj._cvsPwd)));
  }

  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = 31 * hashCode + (this._cvsRoot == null ? 0 : this._cvsRoot.hashCode());
    hashCode = 31 * hashCode + (this._nameInRepository == null ? 0 : this._nameInRepository.hashCode());
    hashCode = 31 * hashCode + (this._branchOrVersionTag == null ? 0 : this._branchOrVersionTag.hashCode());
    hashCode = 31 * hashCode + (this._cvsUser == null ? 0 : this._cvsUser.hashCode());
    hashCode = 31 * hashCode + (this._cvsPwd == null ? 0 : this._cvsPwd.hashCode());
    return hashCode;
  }

}
