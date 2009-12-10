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
package org.ant4eclipse.pydt;

import org.ant4eclipse.pydt.ant.PythonPathDLTKTest;
import org.ant4eclipse.pydt.ant.PythonPathPyDevTest;
import org.ant4eclipse.pydt.ant.SourcePathDLTKTest;
import org.ant4eclipse.pydt.ant.SourcePathPyDevTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Testsuite used to invoke all tests available for the python setup.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { SourcePathDLTKTest.class, SourcePathPyDevTest.class, PythonPathDLTKTest.class,
    PythonPathPyDevTest.class })
public class AllTests {
} /* ENDCLASS */
