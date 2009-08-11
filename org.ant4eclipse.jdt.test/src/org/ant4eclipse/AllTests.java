package org.ant4eclipse;

import org.ant4eclipse.jdt.ant.ClasspathContainersTest;
import org.ant4eclipse.jdt.ant.ClasspathVariablesTest;
import org.ant4eclipse.jdt.ant.ExecuteJdtProjectTest;
import org.ant4eclipse.jdt.ant.GetJdtClassPathTest;
import org.ant4eclipse.jdt.ant.GetJdtClassPath_UnkownContainerTest;
import org.ant4eclipse.jdt.ant.UserLibrariesTest;
import org.ant4eclipse.jdt.tools.BuildOrderResolverTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { GetJdtClassPathTest.class, GetJdtClassPath_UnkownContainerTest.class,
    ExecuteJdtProjectTest.class, ClasspathVariablesTest.class, ClasspathContainersTest.class,
    BuildOrderResolverTest.class, UserLibrariesTest.class })
public class AllTests {
}
