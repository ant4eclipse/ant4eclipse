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
package org.ant4eclipse.ant;


import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;

/**
 * Base type for all ant4eclipse types.
 * 
 * <p>
 * Used to configure ant4eclipse runtime environment if necessary
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class Ant4EclipseType extends DataType {

  public Ant4EclipseType(final Project project) {
    setProject(project);

    Ant4EclipseConfiguration.configureAnt4Eclipse(project);
  }

}
