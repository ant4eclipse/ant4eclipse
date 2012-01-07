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
package org.ant4eclipse.lib.jdt.ecj;

import java.io.File;

/**
 * <p>
 * Describes a source file that should be compiled.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface SourceFile {

  /**
   * <p>
   * Returns the source folder.
   * </p>
   * 
   * @return the source folder.
   */
  File getSourceFolder();

  /**
   * <p>
   * Returns the source file name.
   * </p>
   * 
   * @return the source file name.
   */
  String getSourceFileName();

  /**
   * <p>
   * Returns the source file.
   * </p>
   * 
   * @return the source file.
   */
  File getSourceFile();

  /**
   * <p>
   * Returns the destination folder.
   * </p>
   * 
   * @return the destination folder.
   */
  File getDestinationFolder();

  /**
   * <p>
   * Returns the file encoding.
   * </p>
   * 
   * @return the file encoding.
   */
  String getEncoding();
  
} /* ENDINTERFACE */
