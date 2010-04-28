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
package org.ant4eclipse.lib.jdt.model.project;

import org.ant4eclipse.lib.jdt.model.ClasspathEntry;

/**
 * <p>
 * Represents a raw class path entry as defined in the underlying <code>.classpath</code> file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface RawClasspathEntry extends ClasspathEntry {

  /** the constant for a Container-ClasspathEntry */
  int CPE_CONTAINER = 0;

  /** the constant for a Library-ClasspathEntry */
  int CPE_LIBRARY   = 1;

  /** the constant for a EclipseProject-ClasspathEntry */
  int CPE_PROJECT   = 2;

  /** the constant for a Source-ClasspathEntry */
  int CPE_SOURCE    = 3;

  /** the constant for a Variable-ClasspathEntry */
  int CPE_VARIABLE  = 4;

  /** the constant for an Output-ClasspathEntry */
  int CPE_OUTPUT    = 5;

  /**
   * <p>
   * Returns whether or not the entry has an output location.
   * </p>
   * 
   * @return <code>true</code>, if this entry has an output location.
   */
  boolean hasOutputLocation();

  /**
   * <p>
   * Returns the output location or <code>null</code>, if no output location exists.
   * </p>
   * 
   * @return the output location.
   */
  String getOutputLocation();

  /**
   * <p>
   * Returns <code>true</code>, if this class path entry is exported.
   * </p>
   * 
   * @return <code>true</code>, if this class path entry is exported.
   */
  boolean isExported();

  /**
   * <p>
   * Returns the include patterns.
   * </p>
   * 
   * @return the include patterns.
   */
  String getIncludes();

  /**
   * <p>
   * Returns the exclude patterns.
   * </p>
   * 
   * @return the exclude patterns.
   */
  String getExcludes();
}