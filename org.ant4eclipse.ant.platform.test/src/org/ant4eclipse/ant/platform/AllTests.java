/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.platform;

import org.ant4eclipse.ant.platform.delegate.MacroExecutionDelegateTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( Suite.class )
@Suite.SuiteClasses( { ExecuteProjectBuildersTaskTest.class, HasNatureTest.class, HasBuildCommandTest.class,
    GetProjectDirecoryTest.class, MacroExecutionDelegateTest.class } )
public class AllTests {
} /* ENDCLASS */
