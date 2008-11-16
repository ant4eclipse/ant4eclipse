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
package net.sf.ant4eclipse.model.platform.team.projectset;

import java.io.File;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.xquery.XQuery;
import net.sf.ant4eclipse.core.xquery.XQueryHandler;
import net.sf.ant4eclipse.model.platform.team.cvssupport.projectset.CvsProjectSetFileParser;
import net.sf.ant4eclipse.model.platform.team.svnsupport.projectset.SubversionProjectSetFileParser;

/**
 * Reads an eclipse team project set file and constructs a
 * {@link net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSet TeamProjectSet} for it.
 * 
 * <p>
 * For the format of the psf-file used by Eclipse see org.eclipse.team.internal.ccvs.ui.CVSProjectSetSerializer
 * 
 * @see #parseTeamProjectSet(File)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class ProjectSetFileParser {

  /**
   * Provider ID used by the default Eclipse CVS plugin
   */
  public final static String CVS_PROVIDER_ID        = "org.eclipse.team.cvs.core.cvsnature";

  /**
   * Provider ID used in PSF files from the Subversive Eclipse Plugin
   */
  public final static String SUBVERSIVE_PROVIDER_ID = "org.polarion.team.svn.core.svnnature";

  /**
   * Provider ID used in PSF files from the Subclipse Eclipse Plugin
   */
  public final static String SUBCLIPSE_PROVIDER_ID  = "org.tigris.subversion.subclipse.core.svnnature";

  /**
   * FIXME MMi: Provider ID used by the default Eclipse SVS plugin
   */
  public final static String SVN_PROVIDER_ID        = "org.eclipse.team.svn.core.svnnature";

  /**
   * Parses a project set file.
   * 
   * @param projectSetFile
   *          The file which contains the definition of a team project set.
   * 
   * @return A TeamProjectSet instance providing the content of the project set file.
   */
  public static TeamProjectSet parseTeamProjectSet(final File projectSetFile) {
    Assert.isFile(projectSetFile);

    final XQueryHandler queryhandler2 = new XQueryHandler();

    // queries for the 'provider-id' attribute
    final XQuery providerIdQuery = queryhandler2.createQuery("//psf/provider/@id");
    // query for the 'reference' elements
    final XQuery referenceQuery = queryhandler2.createQuery("//psf/provider/project/@reference");

    // parse the file
    XQueryHandler.queryFile(projectSetFile, queryhandler2);

    final String providerId = providerIdQuery.getSingleResult();

    ProjectSetFileParser parser = null;

    if (CVS_PROVIDER_ID.equals(providerId)) {
      parser = new CvsProjectSetFileParser();
    } else if (SUBVERSIVE_PROVIDER_ID.equals(providerId)) {
      parser = new SubversionProjectSetFileParser();
    } else if (SUBCLIPSE_PROVIDER_ID.equals(providerId)) {
      parser = new SubversionProjectSetFileParser();
    } else if (SVN_PROVIDER_ID.equals(providerId)) {
      parser = new SubversionProjectSetFileParser();
    }

    if (parser == null) {
      // TODO
      throw new RuntimeException("Unkown provider id '" + providerId + "' in psf file '" + projectSetFile + "'");
    }

    final TeamProjectSet projectSet = parser.createTeamProjectSet(projectSetFile);

    // retrieve the result
    final String[] projects = referenceQuery.getResult();

    for (int i = 0; i < projects.length; i++) {
      final TeamProjectDescription description = parser.parseTeamProjectDescription(projects[i]);
      projectSet.addTeamProjectDescription(description);
    }

    return projectSet;
  }

  /**
   * Parses the given Team Project Set file.
   * 
   * <p>
   * This method must be implemented by subclasses that implements a parser for a specific provider.
   * 
   * @param projectSetFile
   *          The project set file. Never null
   * @return The parsed Project Set File. Must not be null
   */
  protected abstract TeamProjectSet createTeamProjectSet(File projectSetFile);

  /**
   * Creates a TeamProjectDescription for the given reference-String
   * 
   * @param reference
   *          The reference string read out of the Project Set-File
   * @return a new, provider-dependent, TeamProjectDescription
   */
  protected abstract TeamProjectDescription parseTeamProjectDescription(String reference);
}
