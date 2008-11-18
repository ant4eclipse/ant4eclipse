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

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.ant4eclipse.core.Ant4EclipseConfigurationProperties;
import org.ant4eclipse.core.logging.Ant4EclipseLogger;
import org.ant4eclipse.core.logging.DefaultAnt4EclipseLogger;
import org.ant4eclipse.platform.model.team.cvssupport.projectset.CvsTeamProjectSetFactory;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSetFactory;
import org.ant4eclipse.testframework.ServiceRegistryConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectSetFileParserImplTest {
  
  @Before
  public void configure() {
    Ant4EclipseConfigurationProperties.initialize();
    
    Properties properties = new Properties();
    properties.put(Ant4EclipseLogger.class.getName(), DefaultAnt4EclipseLogger.class.getName());
    ServiceRegistryConfigurator.configureServiceRegistry(properties);
  }

  @After
  public void dispose() {
    Ant4EclipseConfigurationProperties.dispose();
  }
  
  @Test
  public void test_Factories() {
    TeamProjectSetFileParserImpl projectSetFileParserImpl = new TeamProjectSetFileParserImpl();
    projectSetFileParserImpl.initialize();
    TeamProjectSetFactory factoryForProvider = projectSetFileParserImpl.getFactoryForProvider("org.eclipse.team.cvs.core.cvsnature");
    assertTrue(factoryForProvider instanceof CvsTeamProjectSetFactory);
  }
  
  

}
