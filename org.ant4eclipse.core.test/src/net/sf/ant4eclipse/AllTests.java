package net.sf.ant4eclipse;

import net.sf.ant4eclipse.core.Ant4EclipseConfigurationPropertiesTest;
import net.sf.ant4eclipse.core.AssertTest;
import net.sf.ant4eclipse.core.dependencygraph.DependencyGraphTest;
import net.sf.ant4eclipse.core.logging.A4ELogging_FailureTest;
import net.sf.ant4eclipse.core.service.ServiceRegistryTest;
import net.sf.ant4eclipse.core.util.JarUtilitiesTest;
import net.sf.ant4eclipse.core.util.UtilitiesTest;
import net.sf.ant4eclipse.core.xquery.XQueryHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { JarUtilitiesTest.class, Ant4EclipseConfigurationPropertiesTest.class, AssertTest.class,
    XQueryHandlerTest.class, UtilitiesTest.class, ServiceRegistryTest.class, A4ELogging_FailureTest.class,
    DependencyGraphTest.class })
public class AllTests {
}
