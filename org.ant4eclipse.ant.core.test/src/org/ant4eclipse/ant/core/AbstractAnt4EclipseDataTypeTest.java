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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.apache.tools.ant.Project;

public class AbstractAnt4EclipseDataTypeTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "AbstractAnt4EclipseDataTypeTest.xml";
  }

  public void testAbstractAnt4EclipseDataType() {
    expectLog( "testAbstractAnt4EclipseDataType", "setAttribute(myAttribute)" );
  }

  public static class Ant4EclipseDataType extends AbstractAnt4EclipseDataType {

    public Ant4EclipseDataType( Project project ) {
      super( project );
    }

    /**
     * Changes the user library configuration file.
     * 
     * @param userlib
     *          The new user library configuration file.
     */
    public void setAttribute( String attribute ) {
      getProject().log( "setAttribute(" + attribute + ")" );
    }
  }
  
} /* ENDCLASS */
