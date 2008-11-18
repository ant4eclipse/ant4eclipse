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

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.platform.model.resource.EclipseProject;


public interface RawClasspathEntry extends ClasspathEntry {

  /** the constant for a Container-ClasspathEntry */
  public static final int CPE_CONTAINER = 0;

  /** the constant for a Library-ClasspathEntry */
  public static final int CPE_LIBRARY   = 1;

  /** the constant for a EclipseProject-ClasspathEntry */
  public static final int CPE_PROJECT   = 2;

  /** the constant for a Source-ClasspathEntry */
  public static final int CPE_SOURCE    = 3;

  /** the constant for a Variable-ClasspathEntry */
  public static final int CPE_VARIABLE  = 4;

  /** the constant for an Output-ClasspathEntry */
  public static final int CPE_OUTPUT    = 5;

  public EclipseProject getDeclaringEclipseProject();

  public EclipseProject getReferencedEclipseProject();

  /**
   * @return
   */
  public boolean isProjectRelative();

  public String getProjectRelativePath();

  /**
   * Returns whether or not the entry has an output location.
   * 
   * @return true <=> This entry has an output location.
   */
  public boolean hasOutputLocation();

  /**
   * Returns the output location.
   * 
   * @return the output location.
   */
  public String getOutputLocation();

  /**
   * @return Returns the exported.
   */
  public boolean isExported();

}