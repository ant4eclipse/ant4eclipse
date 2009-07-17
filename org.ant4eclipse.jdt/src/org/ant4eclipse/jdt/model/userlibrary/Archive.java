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
package org.ant4eclipse.jdt.model.userlibrary;

import java.io.File;

/**
 * Simple library entry within the user library configuration file.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface Archive {

  /**
   * Returns the location of the classes. Maybe a directory or a Jar.
   * 
   * @return The location of the classes. Maybe a directory or a Jar.
   */
  File getPath();

  /**
   * Returns the location of the sources.
   * 
   * @return The location of the sources.
   */
  File getSource();

  /**
   * Returns the location of the java docs.
   * 
   * @return The location of the java docs.
   */
  String getJavaDoc();

} /* ENDCLASS */