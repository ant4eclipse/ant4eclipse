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
package net.sf.ant4eclipse.model.platform.team.projectset.internal;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import net.sf.ant4eclipse.ant.platform.team.TeamExceptionCode;
import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.Lifecycle;
import net.sf.ant4eclipse.core.exception.Ant4EclipseException;
import net.sf.ant4eclipse.core.logging.A4ELogging;
import net.sf.ant4eclipse.core.util.Utilities;
import net.sf.ant4eclipse.core.xquery.XQuery;
import net.sf.ant4eclipse.core.xquery.XQueryHandler;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSet;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSetFactory;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSetFileParser;

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
public abstract class ProjectSetFileParserImpl implements TeamProjectSetFileParser, Lifecycle {

  // /**
  // * Provider ID used by the default Eclipse CVS plugin
  // */
  // public final static String CVS_PROVIDER_ID = "org.eclipse.team.cvs.core.cvsnature";
  //
  // /**
  // * Provider ID used in PSF files from the Subversive Eclipse Plugin
  // */
  // public final static String SUBVERSIVE_PROVIDER_ID = "org.polarion.team.svn.core.svnnature";
  //
  // /**
  // * Provider ID used in PSF files from the Subclipse Eclipse Plugin
  // */
  // public final static String SUBCLIPSE_PROVIDER_ID = "org.tigris.subversion.subclipse.core.svnnature";
  //
  // /**
  // * FIXME MMi: Provider ID used by the default Eclipse SVS plugin
  // */
  // public final static String SVN_PROVIDER_ID = "org.eclipse.team.svn.core.svnnature";

  private Map _factories;

  // /**
  // * Parses a project set file.
  // *
  // * @param projectSetFile
  // * The file which contains the definition of a team project set.
  // *
  // * @return A TeamProjectSet instance providing the content of the project set file.
  // */
  // public static TeamProjectSet parseTeamProjectSet(final File projectSetFile) {
  //
  // ProjectSetFileParserImpl parser = null;
  //
  // if (CVS_PROVIDER_ID.equals(providerId)) {
  // parser = new CvsTeamProjectSetFactory();
  // } else if (SUBVERSIVE_PROVIDER_ID.equals(providerId)) {
  // parser = new SubversionProjectSetFactory();
  // } else if (SUBCLIPSE_PROVIDER_ID.equals(providerId)) {
  // parser = new SubversionProjectSetFactory();
  // } else if (SVN_PROVIDER_ID.equals(providerId)) {
  // parser = new SubversionProjectSetFactory();
  // }
  //
  // if (parser == null) {
  // // TODO
  // throw new RuntimeException("Unkown provider id '" + providerId + "' in psf file '" + projectSetFile + "'");
  // }
  //
  // final TeamProjectSet projectSet = parser.createTeamProjectSet(projectSetFile);
  //
  // // retrieve the result
  // final String[] projects = referenceQuery.getResult();
  //
  // for (int i = 0; i < projects.length; i++) {
  // final TeamProjectDescription description = parser.parseTeamProjectDescription(projects[i]);
  // projectSet.addTeamProjectDescription(description);
  // }
  //
  // return projectSet;
  // }

  public void dispose() {
    this._factories = null;

  }

  public final static String TEAMPROVIDERS_PROPERTIES = "net/sf/ant4eclipse/model/platform/team/projectset/teamproviders.properties";

  public void initialize() {
    Properties properties = Utilities.readPropertiesFromClasspath(TEAMPROVIDERS_PROPERTIES);

    Map providers = new Hashtable();

    Iterator entries = properties.entrySet().iterator();
    while (entries.hasNext()) {
      Map.Entry entry = (Entry) entries.next();
      String providerId = entry.getKey().toString();
      String implementationClassName = entry.getValue().toString();

      TeamProjectSetFactory factory = (TeamProjectSetFactory) Utilities.newInstance(implementationClassName);
      A4ELogging.trace("Adding TeamProjectSetFactory '%s' for provider '%s'", new Object[] { factory, providerId });
      providers.put(providerId, factory);
    }
  }

  public boolean isInitialized() {
    return (this._factories != null);
  }

  public TeamProjectSet parseTeamProjectSetFile(File projectSetFile) {
    Assert.isFile(projectSetFile);

    final XQueryHandler queryhandler2 = new XQueryHandler();

    // queries for the 'provider-id' attribute
    final XQuery providerIdQuery = queryhandler2.createQuery("//psf/provider/@id");
    // query for the 'reference' elements
    final XQuery referenceQuery = queryhandler2.createQuery("//psf/provider/project/@reference");

    // parse the file
    XQueryHandler.queryFile(projectSetFile, queryhandler2);

    // determine team project set-provider
    final String providerId = providerIdQuery.getSingleResult();

    // get the factory for this provider
    TeamProjectSetFactory projectSetFactory = getFactoryForProvider(providerId);
    final TeamProjectSet projectSet = projectSetFactory.createTeamProjectSet(projectSetFile.getName());

    // retrieve the result
    final String[] projects = referenceQuery.getResult();

    // create TeamProjectDescriptions for each project
    for (int i = 0; i < projects.length; i++) {
      projectSetFactory.addTeamProjectDescription(projectSet, projects[i]);
    }

    return projectSet;

  }

  protected TeamProjectSetFactory getFactoryForProvider(String providerId) {
    Assert.notNull("Parameter 'providerId' must not be null", providerId);

    if (!_factories.containsKey(providerId)) {
      throw new Ant4EclipseException(TeamExceptionCode.UNKNOWN_TEAM_PROJECT_SET_PROVIDER, providerId);
    }

    return (TeamProjectSetFactory) _factories.get(providerId);
  }

}
