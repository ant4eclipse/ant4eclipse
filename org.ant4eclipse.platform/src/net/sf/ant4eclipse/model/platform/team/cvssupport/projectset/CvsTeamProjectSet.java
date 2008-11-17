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
package net.sf.ant4eclipse.model.platform.team.cvssupport.projectset;

import java.util.Iterator;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.logging.A4ELogging;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectDescription;
import net.sf.ant4eclipse.model.platform.team.projectset.internal.AbstractTeamProjectSet;

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

    A4ELogging.debug("setUserAndPassword(%s, %s)", new Object[] { cvsUser, cvsPwd });

    for (Iterator iterator = getProjectDescriptions().iterator(); iterator.hasNext();) {
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
    for (Iterator iterator = getProjectDescriptions().iterator(); iterator.hasNext();) {
      TeamProjectDescription description = (TeamProjectDescription) iterator.next();
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
