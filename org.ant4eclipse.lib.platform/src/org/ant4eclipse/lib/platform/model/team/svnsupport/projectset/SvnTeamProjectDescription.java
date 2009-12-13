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


import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.platform.model.team.projectset.internal.AbstractTeamProjectDescription;

public class SvnTeamProjectDescription extends AbstractTeamProjectDescription {

  /** The team project set, this description belongs to */
  private SvnTeamProjectSet _teamProjectSet;

  /** The svn url of this project */
  private String            _url;

  /** the revision of this project */
  private String            _revision;

  /**
   * 
   * @param projectname
   *          The (local) name of the project
   * @param url
   *          The url of the project in the SVN repository
   * @param revision
   *          The revision of the project (might be null which means HEAD)
   */
  public SvnTeamProjectDescription(SvnTeamProjectSet teamProjectSet, String projectname, String url, String revision) {
    super(projectname);
    Assure.notNull(teamProjectSet);
    Assure.notNull(url);

    this._teamProjectSet = teamProjectSet;
    this._url = url;
    this._revision = (revision != null ? revision : "HEAD");
  }

  /**
   * Constructs a new SvnTeamProjectDescription without a revision (=revision defaults to HEAD)
   * 
   * @param projectname
   *          The (local) name of the project
   * @param url
   *          The url of the project in the SVN repository
   */
  public SvnTeamProjectDescription(SvnTeamProjectSet teamProjectSet, String projectname, String url) {
    this(teamProjectSet, projectname, url, null);
  }

  public String getRevision() {
    return this._revision;
  }

  public String getUrl() {
    return this._url;
  }

  /**
   * Returns the Team Project Set, this description belongs to
   * 
   * @return
   */
  public SvnTeamProjectSet getTeamProjectSet() {
    return this._teamProjectSet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[SvnTeamProjectDescription:");/* Inaccessible getter for private field _projectname */
    buffer.append(" projectname: " + getProjectName());
    buffer.append(" _url: ");
    buffer.append(this._url);
    buffer.append(" _revision: ");
    buffer.append(this._revision);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
    SvnTeamProjectDescription castedObj = (SvnTeamProjectDescription) o;
    return ((this._url == null ? castedObj._url == null : this._url.equals(castedObj._url)) && (this._revision == null ? castedObj._revision == null
        : this._revision.equals(castedObj._revision)));
  }
}
