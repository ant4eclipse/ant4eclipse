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
package org.ant4eclipse.platform.model.team.projectset.internal;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;
import org.ant4eclipse.platform.PlatformExceptionCode;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSetFactory;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSetFileParser;

/**
 * Reads an eclipse team project set file and constructs a
 * {@link org.ant4eclipse.model.platform.team.projectset.TeamProjectSet TeamProjectSet} for it.
 * 
 * <p>
 * For the format of the psf-file used by Eclipse see org.eclipse.team.internal.ccvs.ui.CVSProjectSetSerializer
 * 
 * @see #parseTeamProjectSet(File)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TeamProjectSetFileParserImpl implements TeamProjectSetFileParser, Lifecycle {

  /**
   * The prefix for properties describing a team provider.
   * 
   * <p>
   * The property must have the (eclipse) provider id as key (after the suffix) and the implementation class name as
   * value
   */
  public final static String                 TEAMPROVIDER_PREFIX = "teamprovider";

  /**
   * A Map containing all registered {@link TeamProjectSetFactory} instances
   */
  private Map<String, TeamProjectSetFactory> _factories;

  public void initialize() {
    Iterable<String[]> teamProviders = Ant4EclipseConfiguration.Helper.getAnt4EclipseConfiguration().getAllProperties(
        TEAMPROVIDER_PREFIX);
    Map<String, TeamProjectSetFactory> providers = new Hashtable<String, TeamProjectSetFactory>();

    for (String[] teamProvider : teamProviders) {
      String providerId = teamProvider[0];
      String implementationClassName = teamProvider[1];

      TeamProjectSetFactory factory = Utilities.newInstance(implementationClassName);
      A4ELogging.trace("Adding TeamProjectSetFactory '%s' for provider '%s'", new Object[] { factory, providerId });
      providers.put(providerId, factory);
    }

    this._factories = providers;
  }

  /**
   * Disposes this TeamProjectSetFileParserImpl instances
   */
  public void dispose() {
    this._factories = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.sf.ant4eclipse.core.Lifecycle#isInitialized()
   */
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

  public TeamProjectSetFactory getFactoryForProvider(String providerId) {
    Assert.notNull("Parameter 'providerId' must not be null", providerId);

    if (!_factories.containsKey(providerId)) {
      throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_TEAM_PROJECT_SET_PROVIDER, providerId);
    }

    return (TeamProjectSetFactory) _factories.get(providerId);
  }

}
