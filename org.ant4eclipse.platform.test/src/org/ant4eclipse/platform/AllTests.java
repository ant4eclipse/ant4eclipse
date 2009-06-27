package org.ant4eclipse.platform;

import org.ant4eclipse.platform.ant.ExecuteProjectBuildersTaskTest;
import org.ant4eclipse.platform.ant.GetProjectDirecoryTest;
import org.ant4eclipse.platform.ant.HasBuildCommandTest;
import org.ant4eclipse.platform.ant.HasNatureTest;
import org.ant4eclipse.platform.ant.delegate.MacroExecutionDelegateTest;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifierRegistryTest;
import org.ant4eclipse.platform.model.team.cvssupport.CvsRootTest;
import org.ant4eclipse.platform.model.team.projectset.internal.ProjectSetFileParserImplTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ExecuteProjectBuildersTaskTest.class, HasNatureTest.class, HasBuildCommandTest.class,
    GetProjectDirecoryTest.class, ProjectRoleIdentifierRegistryTest.class, CvsRootTest.class,
    ProjectSetFileParserImplTest.class, MacroExecutionDelegateTest.class })
public class AllTests {
}
