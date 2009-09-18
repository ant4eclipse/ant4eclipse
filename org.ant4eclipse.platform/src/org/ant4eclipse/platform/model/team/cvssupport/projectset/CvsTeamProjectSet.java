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

import org.ant4eclipse.platform.model.team.cvssupport.projectset.CvsTeamProjectDescription;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectDescription;
import org.ant4eclipse.platform.model.team.projectset.internal.AbstractTeamProjectSet;

import java.util.Iterator;

public class CvsTeamProjectSet extends AbstractTeamProjectSet {

  public CvsTeamProjectSet(String name) {
    super(name);
  }

  /**
   * Sets the cvs user and password for each contained TeamProjectDescription.
   * 
   * @param cvsUser
   *          the cvs user.
   * @param cvsPwd
   *          the cvs password might be null
   */
  public void setUserAndPassword(String cvsUser, String cvsPwd) {
    Assert.notNull(cvsUser);

    A4ELogging.debug("setUserAndPassword(%s, %s)", cvsUser, cvsPwd);

    for (Iterator<TeamProjectDescription> iterator = getProjectDescriptions().iterator(); iterator.hasNext();) {
      CvsTeamProjectDescription description = (CvsTeamProjectDescription) iterator.next();
      description.setCvsUserAndPassword(cvsUser, cvsPwd);
    }
  }

  /**
   * Overwritten method to make sure that the given TeamProjectDescription is an instance of a CvsTeamProjectDescription
   */
  public void addTeamProjectDescription(CvsTeamProjectDescription description) {
    Assert.notNull(description);
    super.addTeamProjectDescription(description);
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[CvsTeamProjectSet:");
    buffer.append(" name: ");
    buffer.append(getName());
    buffer.append(" { ");
    for (Iterator<TeamProjectDescription> iterator = getProjectDescriptions().iterator(); iterator.hasNext();) {
      TeamProjectDescription description = iterator.next();
      buffer.append(description);

      if (iterator.hasNext()) {
        buffer.append(",");
      }
    }
    buffer.append(" } ");
    buffer.append("]");
    return buffer.toString();
  }

}
