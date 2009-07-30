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

import org.ant4eclipse.platform.model.resource.role.ProjectRole;

/**
 * PyDLTKProjectRole -- Python project role for the eclipse dltk framework.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PyDLTKProjectRole extends ProjectRole {

  String PYDLTK_NATURE = "org.eclipse.dltk.python.core.nature";

} /* ENDINTERFACE */