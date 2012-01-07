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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.data.VersionTest;
import org.ant4eclipse.lib.core.dependencygraph.DependencyGraphTest;
import org.ant4eclipse.lib.core.nls.NLSTest;
import org.ant4eclipse.lib.core.util.ManifestHelperTest;
import org.ant4eclipse.lib.core.util.UtilitiesTest;
import org.ant4eclipse.lib.core.xquery.XQueryHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( Suite.class )
@Suite.SuiteClasses( { ClassNameTest.class, VersionTest.class, DependencyGraphTest.class,
    NLSTest.class, ManifestHelperTest.class, UtilitiesTest.class, XQueryHandlerTest.class } )
public class AllCoreTests {
} /* ENDCLASS */
