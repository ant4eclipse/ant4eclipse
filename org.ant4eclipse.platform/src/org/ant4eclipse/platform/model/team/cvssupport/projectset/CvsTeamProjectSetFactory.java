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

import java.util.StringTokenizer;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.platform.ant.team.TeamExceptionCode;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSetFactory;


/**
 * Parses a ProjectSetFile provided by the default Eclipse CVS plugin
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class CvsTeamProjectSetFactory implements TeamProjectSetFactory {

  /** REPOSITORY_LOCATION */
  private final static int REPOSITORY_LOCATION        = 1;

  /** PROJECT_NAME_IN_REPOSITORY */
  private final static int PROJECT_NAME_IN_REPOSITORY = 2;

  /** PROJECT_NAME_IN_WORKSPACE */
  private final static int PROJECT_NAME_IN_WORKSPACE  = 3;

  /** BRANCH_OR_VERSION_TAG */
  private final static int BRANCH_OR_VERSION_TAG      = 4;

  public TeamProjectSet createTeamProjectSet(String projectSetName) {
    Assert.notNull(projectSetName);
    CvsTeamProjectSet projectSet = new CvsTeamProjectSet(projectSetName);
    return projectSet;
  }

  /**
   * Parses the given reference of a CVS Project Set-file.
   * 
   */
  public void addTeamProjectDescription(TeamProjectSet projectSet, String reference) {
    Assert.instanceOf("projectSet", projectSet, CvsTeamProjectSet.class);
    Assert.notNull(reference);
    A4ELogging.trace("parseReference ('%s')", reference);

    StringTokenizer stringTokenizer = new StringTokenizer(reference, ",");
    int tokensCount = stringTokenizer.countTokens();

    if (tokensCount < 4) {
      throw new Ant4EclipseException(TeamExceptionCode.INVALID_PSF_REFERENCE, "at least four", tokensCount, reference);
    }
    if (tokensCount > 5) {
      // bug 1569122
      // it might happen that a project has more than one CVS tag, thus more tokens. Eclipse seems to
      // use *always* the first, regardless whether there are more tags specified.
      // (See org.eclipse.team.internal.ccvs.ui.CVSProjectSetSerializer#addToWorkspace)
      A4ELogging.warn("Unusual reference in psf file. Expected to have five tokens, but have: " + tokensCount
          + " in reference '" + reference + "'. Ignoring extra tokens.");
    }

    String[] token = new String[tokensCount];
    int counter = 0;

    while (stringTokenizer.hasMoreTokens()) {
      token[counter] = stringTokenizer.nextToken();
      counter++;
    }

    String branchOrVersion = (tokensCount > 4) ? token[BRANCH_OR_VERSION_TAG] : "HEAD";

    CvsTeamProjectSet cvsTeamProjectSet = (CvsTeamProjectSet) projectSet;

    CvsTeamProjectDescription cvsTeamProjectDescription = new CvsTeamProjectDescription(
        token[PROJECT_NAME_IN_WORKSPACE], token[REPOSITORY_LOCATION], token[PROJECT_NAME_IN_REPOSITORY],
        branchOrVersion);

    cvsTeamProjectSet.addTeamProjectDescription(cvsTeamProjectDescription);
  }
}
