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
package org.ant4eclipse;


import org.ant4eclipse.lib.core.DefaultConfiguratorTest;
import org.ant4eclipse.lib.core.AssureTest;
import org.ant4eclipse.lib.core.ClassNameTest;
import org.ant4eclipse.lib.core.ant.AbstractAnt4EclipseConditionTest;
import org.ant4eclipse.lib.core.ant.AbstractAnt4EclipseDataTypeTest;
import org.ant4eclipse.lib.core.ant.AbstractAnt4EclipseTaskTest;
import org.ant4eclipse.lib.core.ant.AntCallTest;
import org.ant4eclipse.lib.core.dependencygraph.DependencyGraphTest;
import org.ant4eclipse.lib.core.logging.FailureTest;
import org.ant4eclipse.lib.core.logging.LoggingUsageTest;
import org.ant4eclipse.lib.core.nls.NLSTest;
import org.ant4eclipse.lib.core.service.PropertiesBasedServiceRegistryConfigurationTest;
import org.ant4eclipse.lib.core.service.ServiceRegistryTest;
import org.ant4eclipse.lib.core.util.UtilitiesTest;
import org.ant4eclipse.lib.core.xquery.XQueryHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ClassNameTest.class, AssureTest.class, DefaultConfiguratorTest.class,
    XQueryHandlerTest.class, UtilitiesTest.class, PropertiesBasedServiceRegistryConfigurationTest.class,
    ServiceRegistryTest.class, NLSTest.class, FailureTest.class, LoggingUsageTest.class,
    DependencyGraphTest.class, AbstractAnt4EclipseConditionTest.class, AbstractAnt4EclipseDataTypeTest.class,
    AbstractAnt4EclipseTaskTest.class, AntCallTest.class })
public class AllTests {
}
