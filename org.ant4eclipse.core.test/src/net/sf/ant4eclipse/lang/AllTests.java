package net.sf.ant4eclipse.lang;

import net.sf.ant4eclipse.lang.dependencygraph.DependencyGraphTest;
import net.sf.ant4eclipse.lang.logging.A4ELogging_FailureTest;
import net.sf.ant4eclipse.lang.service.ServiceRegistryTest;
import net.sf.ant4eclipse.lang.util.UtilitiesTest;
import net.sf.ant4eclipse.lang.xquery.XQueryHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { AssertTest.class, XQueryHandlerTest.class, UtilitiesTest.class, ServiceRegistryTest.class,
    A4ELogging_FailureTest.class, DependencyGraphTest.class })
public class AllTests {
}
