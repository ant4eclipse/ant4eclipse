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
package org.ant4eclipse.jdt.model.project;

import org.ant4eclipse.jdt.model.*;
import org.ant4eclipse.platform.model.resource.*;

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

  EclipseProject getDeclaringEclipseProject();

  EclipseProject getReferencedEclipseProject();

  /**
   * @return
   */
  boolean isProjectRelative();

  String getProjectRelativePath();

  /**
   * Returns whether or not the entry has an output location.
   * 
   * @return true <=> This entry has an output location.
   */
  boolean hasOutputLocation();

  /**
   * Returns the output location.
   * 
   * @return the output location.
   */
  String getOutputLocation();

  /**
   * @return Returns the exported.
   */
  boolean isExported();

}