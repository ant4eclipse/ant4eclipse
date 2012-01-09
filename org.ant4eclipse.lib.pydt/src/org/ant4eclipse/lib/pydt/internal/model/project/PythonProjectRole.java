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
package org.ant4eclipse.lib.pydt.internal.model.project;

import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.lib.pydt.model.RawPathEntry;
import org.ant4eclipse.lib.pydt.model.ReferenceKind;

import java.util.List;

/**
 * Declaration of functionalities used to access python related information of a project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PythonProjectRole extends ProjectRole {

  /**
   * Returns a list of all raw path entries associated with the current project.
   * 
   * @return A list of all raw path entries. Not <code>null</code>.
   */
  List<RawPathEntry> getRawPathEntries();

  /**
   * Returns a list of all raw path entries associated with the current project.
   * 
   * @param kind
   *          Only return entries related to a specific kind. Not <code>null</code>.
   * 
   * @return A list of all raw path entries. Not <code>null</code>.
   */
  List<RawPathEntry> getRawPathEntries( ReferenceKind kind );

  /**
   * Returns <code>true</code> if the Python DLTK framework for python has been used. This is just a small if it's
   * necessary to distinguish the related frameworks since we only provide one role implementation. Otherwise we would
   * always need to use the identifiers or we would be forced to provide two role implementations including
   * redundancies.
   * 
   * @return <code>true</code> <=> The python DLTK framework for python has been used. Otherwise the PyDev framework.
   */
  boolean isDLTK();

} /* ENDINTERFACE */
