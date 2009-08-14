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
package org.ant4eclipse.pydt.model.project;

import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;

/**
 * PyDevProjectRole -- Projectrole for the PyDev python support.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PyDevProjectRole extends PythonProjectRole {

  String PYDEV_NATURE       = "org.python.pydev.pythonNature";

  String PYDEV_BUILDCOMMAND = "org.python.pydev.PyDevBuilder";

} /* ENDINTERFACE */