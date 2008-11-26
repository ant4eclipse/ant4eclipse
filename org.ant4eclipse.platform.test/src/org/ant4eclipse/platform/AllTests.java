package org.ant4eclipse.platform;

import org.ant4eclipse.platform.ant.HasNatureTest;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifierRegistryTest;
import org.ant4eclipse.platform.model.team.cvssupport.CvsRootTest;
import org.ant4eclipse.platform.model.team.projectset.internal.ProjectSetFileParserImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { HasNatureTest.class, ProjectRoleIdentifierRegistryTest.class, CvsRootTest.class,
    ProjectSetFileParserImplTest.class })
public class AllTests {
}
