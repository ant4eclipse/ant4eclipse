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

import org.ant4eclipse.platform.model.team.cvssupport.projectset.CvsTeamProjectSetFactory;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSetFactory;
import org.ant4eclipse.testframework.Ant4EclipseTestCase;
import org.junit.Test;

public class ProjectSetFileParserImplTest extends Ant4EclipseTestCase {

  @Override
  protected void setupAnt4EclipseConfigurationProperties() {
    // we want to use 'default' properties read from ant4eclipse-configuration.properties
  }

  @Test
  public void test_Factories() {
    TeamProjectSetFileParserImpl projectSetFileParserImpl = new TeamProjectSetFileParserImpl();
    projectSetFileParserImpl.initialize();
    TeamProjectSetFactory factoryForProvider = projectSetFileParserImpl
        .getFactoryForProvider("org.eclipse.team.cvs.core.cvsnature");
    assertTrue(factoryForProvider instanceof CvsTeamProjectSetFactory);
  }

}
