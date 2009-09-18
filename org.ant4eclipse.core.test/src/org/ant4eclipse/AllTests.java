package org.ant4eclipse;

import org.ant4eclipse.core.Ant4EclipseConfiguratorTest;
import org.ant4eclipse.core.AssertTest;
import org.ant4eclipse.core.ClassNameTest;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseConditionTest;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataTypeTest;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseTaskTest;
import org.ant4eclipse.core.ant.AntCallTest;
import org.ant4eclipse.core.dependencygraph.DependencyGraphTest;
import org.ant4eclipse.core.logging.A4ELogging_FailureTest;
import org.ant4eclipse.core.logging.LoggingUsageTest;
import org.ant4eclipse.core.nls.NLSTest;
import org.ant4eclipse.core.service.PropertiesBasedServiceRegistryConfigurationTest;
import org.ant4eclipse.core.service.ServiceRegistryTest;
import org.ant4eclipse.core.util.UtilitiesTest;
import org.ant4eclipse.core.xquery.XQueryHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ClassNameTest.class, AssertTest.class, Ant4EclipseConfiguratorTest.class,
    XQueryHandlerTest.class, UtilitiesTest.class, PropertiesBasedServiceRegistryConfigurationTest.class,
    ServiceRegistryTest.class, NLSTest.class, A4ELogging_FailureTest.class, LoggingUsageTest.class,
    DependencyGraphTest.class, AbstractAnt4EclipseConditionTest.class, AbstractAnt4EclipseDataTypeTest.class,
    AbstractAnt4EclipseTaskTest.class, AntCallTest.class })
public class AllTests {
}
