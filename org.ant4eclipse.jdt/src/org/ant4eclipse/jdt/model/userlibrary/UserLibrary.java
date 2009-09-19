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
package org.ant4eclipse.jdt.model.userlibrary;

import java.io.File;

/**
 * Description of an user library.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface UserLibrary {

  /**
   * Returns the name of this user library.
   * 
   * @return The name of this user library.
   */
  String getName();

  /**
   * Returns true if this library affects the boot class path.
   * 
   * @return true <=> This library affects the boot class path.
   */
  boolean isSystemLibrary();

  /**
   * Returns a list of archives that are registered with this library entry.
   * 
   * @return A list of archives that are registered with this library entry.
   */
  Archive[] getArchives();

  /**
   * Returns a list of archive files that are registered with this library entry.
   * 
   * @return A list of archive files that are registered with this library entry.
   */
  File[] getArchiveFiles();

} /* ENDCLASS */
