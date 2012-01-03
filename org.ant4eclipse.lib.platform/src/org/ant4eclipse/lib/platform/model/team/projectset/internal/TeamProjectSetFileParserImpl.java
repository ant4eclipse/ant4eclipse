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

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSetFactory;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSetFileParser;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Reads an eclipse team project set file and constructs a
 * {@link org.ant4eclipse.model.platform.team.projectset.TeamProjectSet TeamProjectSet} for it.
 * 
 * <p>
 * For the format of the psf-file used by Eclipse see org.eclipse.team.internal.ccvs.ui.CVSProjectSetSerializer
 * 
 * @see #parseTeamProjectSet(File)
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class TeamProjectSetFileParserImpl implements TeamProjectSetFileParser {

  private Map<String, TeamProjectSetFactory>   factorymap;

  /**
   * Initializes this parser implementation which supports to deal with varioues projectset
   * implementations.
   */
  public TeamProjectSetFileParserImpl() {
    factorymap = new Hashtable<String, TeamProjectSetFactory>();
    List<TeamProjectSetFactory> factories = A4ECore.instance().getServices( TeamProjectSetFactory.class );
    for( TeamProjectSetFactory factory : factories ) {
      String[] providerids = factory.getProviderIDs();
      for( String providerid : providerids ) {
        A4ELogging.trace( "Adding TeamProjectSetFactory '%s' for provider '%s'", factory, providerid );
        factorymap.put( providerid, factory );
      }
    }
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

    if (!this.factorymap.containsKey(providerId)) {
      throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_TEAM_PROJECT_SET_PROVIDER, providerId);
    }

    return this.factorymap.get(providerId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

} /* ENDCLASS */
