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
package org.ant4eclipse.pde;

import org.ant4eclipse.pde.ant.PdeProjectFileSetTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { PdeProjectFileSetTest.class /**
 * , GetRequiredBundlesTaskTest.class,
 * ExecutePluginProjectTaskTest.class, ExecutePluginLibraryTaskTest.class, FragmentTest.class, JarInJarTest.class
 */
})
public class AllTests {
} /* ENDCLASS */
