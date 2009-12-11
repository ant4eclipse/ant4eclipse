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
package org.ant4eclipse;

import org.ant4eclipse.core.Ant4EclipseConfiguratorTest;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseConditionTest;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataTypeTest;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseTaskTest;
import org.ant4eclipse.core.ant.AntCallTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { Ant4EclipseConfiguratorTest.class, AbstractAnt4EclipseConditionTest.class,
    AbstractAnt4EclipseDataTypeTest.class, AbstractAnt4EclipseTaskTest.class, AntCallTest.class })
public class AllTests {
}
