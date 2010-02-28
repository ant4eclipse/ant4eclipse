/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
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

import org.ant4eclipse.lib.platform.model.team.cvssupport.projectset.CvsTeamProjectSetFactory;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSetFactory;
import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.junit.Assert;
import org.junit.Test;

public class ProjectSetFileParserImplTest extends ConfigurableAnt4EclipseTestCase {

  @Test
  public void factories() {
    TeamProjectSetFileParserImpl projectSetFileParserImpl = new TeamProjectSetFileParserImpl();
    projectSetFileParserImpl.initialize();
    TeamProjectSetFactory factoryForProvider = projectSetFileParserImpl
        .getFactoryForProvider("org.eclipse.team.cvs.core.cvsnature");
    Assert.assertTrue(factoryForProvider instanceof CvsTeamProjectSetFactory);
  }

} /* ENDCLASS */
