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
package org.ant4eclipse.platform.model.team.cvssupport.project;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.AbstractProjectRole;
import org.ant4eclipse.platform.model.team.cvssupport.CvsRoot;

import net.sf.ant4eclipse.core.Assert;

/**
 * Implements the cvs role of a project.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class CvsProjectRole extends AbstractProjectRole {

  /** the name for this role */
  public static final String NAME = "CvsProjectRole";

  /** the cvsRoot for the project * */
  private final CvsRoot      _cvsRoot;

  /** the name of the project in the repository */
  private final String       _projectNameInRepository;

  /** the name of the branch or version */
  private final String       _branchOrVersionTag;

  /**
   * Returns the cvs project role. If a cvs project role is not set, an exception will be thrown.
   * 
   * @return Returns the cvs project role.
   */
  public static CvsProjectRole getCvsProjectRole(final EclipseProject eclipseProject) {
    Assert.assertTrue(hasCvsProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
        + "\" must have CvsProjectRole!");

    return (CvsProjectRole) eclipseProject.getRole(CvsProjectRole.class);
  }

  /**
   * Returns whether a cvs project role is set or not.
   * 
   * @return Returns whether a cvs project role is set or not.
   */
  public static boolean hasCvsProjectRole(final EclipseProject eclipseProject) {
    return eclipseProject.hasRole(CvsProjectRole.class);
  }

  /**
   * Creates a new instance of type CvsProjectRole.
   * 
   * @param projectNameInRepository
   *          the name of the project in the repository
   * @param cvsRoot
   *          the cvsRoot for the project
   * @param branchOrVersionTag
   *          the name of the branch or version
   */
  public CvsProjectRole(final EclipseProject eclipseProject, final String projectNameInRepository,
      final String cvsRoot, final String branchOrVersionTag) {
    super(NAME, eclipseProject);

    Assert.notNull(projectNameInRepository);
    Assert.notNull(cvsRoot);

    this._cvsRoot = new CvsRoot(cvsRoot);
    this._projectNameInRepository = projectNameInRepository;
    this._branchOrVersionTag = branchOrVersionTag;
  }

  /**
   * Creates a new instance of type CvsProjectRole.
   * 
   * @param projectNameInRepository
   *          the name of the project in the repository
   * @param cvsRoot
   *          the cvsRoot for the project
   * @param branchOrVersionTag
   *          the name of the branch or version
   */
  public CvsProjectRole(final EclipseProject eclipseProject, final String projectNameInRepository,
      final CvsRoot cvsRoot, final String branchOrVersionTag) {
    super(NAME, eclipseProject);

    Assert.notNull(projectNameInRepository);
    Assert.notNull(cvsRoot);

    this._cvsRoot = cvsRoot;
    this._projectNameInRepository = projectNameInRepository;
    this._branchOrVersionTag = branchOrVersionTag;
  }

  /**
   * Returns whether the project is head.
   * 
   * @return true <=> The project is currently tagged with HEAD
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
   * Returns the branch or version tag, or null if no tag exists.
   * 
   * @return Returns the branch or version tag, or null if no tag exists.
   */
  public String getBranchOrVersionTag() {
    return this._branchOrVersionTag;
  }

  /**
   * Returns the name of the project in the repository.
   * 
   * @return Returns the name of the project in the repository.
   */
  public String getProjectNameInRepository() {
    return this._projectNameInRepository;
  }

  /**
   * Returns the cvs root.
   * 
   * @return Returns the cvs root.
   */
  public CvsRoot getCvsRoot() {
    return this._cvsRoot;
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final Object o) {
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
    final CvsProjectRole castedObj = (CvsProjectRole) o;
    return ((this._cvsRoot == null ? castedObj._cvsRoot == null : this._cvsRoot.equals(castedObj._cvsRoot))
        && (this._projectNameInRepository == null ? castedObj._projectNameInRepository == null
            : this._projectNameInRepository.equals(castedObj._projectNameInRepository)) && (this._branchOrVersionTag == null ? castedObj._branchOrVersionTag == null
        : this._branchOrVersionTag.equals(castedObj._branchOrVersionTag)));
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[CvsProjectRole:");
    buffer.append(" NAME: ");
    buffer.append(NAME);
    buffer.append(" cvsRoot: ");
    buffer.append(this._cvsRoot);
    buffer.append(" projectNameInRepository: ");
    buffer.append(this._projectNameInRepository);
    buffer.append(" branchOrVersionTag: ");
    buffer.append(this._branchOrVersionTag);
    buffer.append("]");
    return buffer.toString();
  }

}