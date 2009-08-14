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
 * PyDLTKProjectRole -- Python project role for the eclipse dltk framework.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PyDLTKProjectRole extends PythonProjectRole {

  String NATURE       = "org.eclipse.dltk.python.core.nature";

  String BUILDCOMMAND = "org.eclipse.dltk.core.scriptbuilder";

} /* ENDINTERFACE */