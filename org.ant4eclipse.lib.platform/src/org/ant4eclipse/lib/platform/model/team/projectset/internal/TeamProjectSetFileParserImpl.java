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
package org.ant4eclipse.lib.platform.model.team.projectset.internal;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSetFactory;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSetFileParser;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

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
  public static final String                 TEAMPROVIDER_PREFIX = "teamprovider";

  /**
   * A Map containing all registered {@link TeamProjectSetFactory} instances
   */
  private Map<String, TeamProjectSetFactory> _factories;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize() {
    Ant4EclipseConfiguration config = ServiceRegistryAccess.instance().getService(Ant4EclipseConfiguration.class);
    Iterable<Pair<String, String>> teamProviders = config.getAllProperties(TEAMPROVIDER_PREFIX);
    Map<String, TeamProjectSetFactory> providers = new Hashtable<String, TeamProjectSetFactory>();

    for (Pair<String, String> teamProvider : teamProviders) {
      TeamProjectSetFactory factory = Utilities.newInstance(teamProvider.getSecond());
      A4ELogging.trace("Adding TeamProjectSetFactory '%s' for provider '%s'", factory, teamProvider.getFirst());
      providers.put(teamProvider.getFirst(), factory);
    }

    this._factories = providers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    this._factories = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TeamProjectSet parseTeamProjectSetFile(File projectSetFile) {
    Assure.isFile("projectSetFile", projectSetFile);

    XQueryHandler queryhandler2 = new XQueryHandler();

    // queries for the 'provider-id' attribute
    XQuery providerIdQuery = queryhandler2.createQuery("/psf/provider/@id");
    // query for the 'reference' elements
    XQuery referenceQuery = queryhandler2.createQuery("/psf/provider/project/@reference");

    // parse the file
    XQueryHandler.queryFile(projectSetFile, queryhandler2);

    // determine team project set-provider
    String providerId = providerIdQuery.getSingleResult();

    // get the factory for this provider
    TeamProjectSetFactory projectSetFactory = getFactoryForProvider(providerId);
    TeamProjectSet projectSet = projectSetFactory.createTeamProjectSet(projectSetFile.getName());

    // retrieve the result
    String[] projects = referenceQuery.getResult();

    // create TeamProjectDescriptions for each project
    for (String project : projects) {
      projectSetFactory.addTeamProjectDescription(projectSet, project);
    }

    return projectSet;

  }

  public TeamProjectSetFactory getFactoryForProvider(String providerId) {
    Assure.notNull("providerId", providerId);

    if (!this._factories.containsKey(providerId)) {
      throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_TEAM_PROJECT_SET_PROVIDER, providerId);
    }

    return this._factories.get(providerId);
  }

}
