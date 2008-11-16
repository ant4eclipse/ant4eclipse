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

import java.io.File;
import java.util.StringTokenizer;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.logging.A4ELogging;
import net.sf.ant4eclipse.model.platform.resource.internal.factory.FileParserException;
import net.sf.ant4eclipse.model.platform.team.projectset.ProjectSetFileParser;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectDescription;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSet;

/**
 * Parses a ProjectSetFile provided by the default Eclipse CVS plugin
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class CvsProjectSetFileParser extends ProjectSetFileParser {

  /** REPOSITORY_LOCATION */
  private final static int REPOSITORY_LOCATION        = 1;

  /** PROJECT_NAME_IN_REPOSITORY */
  private final static int PROJECT_NAME_IN_REPOSITORY = 2;

  /** PROJECT_NAME_IN_WORKSPACE */
  private final static int PROJECT_NAME_IN_WORKSPACE  = 3;

  /** BRANCH_OR_VERSION_TAG */
  private final static int BRANCH_OR_VERSION_TAG      = 4;

  /**
   * Creates a new CvsTeamProjectSet instance for the given projectSetFile
   */
  protected TeamProjectSet createTeamProjectSet(File projectSetFile) throws FileParserException {
    Assert.isFile(projectSetFile);
    CvsTeamProjectSet projectSet = new CvsTeamProjectSet(projectSetFile.getName());
    return projectSet;
  }

  /**
   * Parses the given reference of a CVS Project Set-file. 
   * 
   * <p>This mehtod is made public to make it accessible from the tests.
   */
  public TeamProjectDescription parseTeamProjectDescription(String reference) throws FileParserException {
    Assert.notNull(reference);
    A4ELogging.trace("parseReference ('%s')", reference);

    StringTokenizer stringTokenizer = new StringTokenizer(reference, ",");
    int tokensCount = stringTokenizer.countTokens();

    if (tokensCount < 4) {
      throw new FileParserException("Invalid PSF-reference. Expected to have at least four tokens, but have: " + tokensCount
          + " in reference '" + reference + "'");
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

    // TODO: test this:
    // default: use repository name as workspace name
    // if (counter < PROJECT_NAME_IN_WORKSPACE + 1) {
    // token[PROJECT_NAME_IN_WORKSPACE] = token[PROJECT_NAME_IN_REPOSITORY];
    // }
    
    String branchOrVersion = (tokensCount>4) ? token[BRANCH_OR_VERSION_TAG] : "HEAD";

    return new CvsTeamProjectDescription(token[PROJECT_NAME_IN_WORKSPACE], token[REPOSITORY_LOCATION],
        token[PROJECT_NAME_IN_REPOSITORY], branchOrVersion);
  }
}
