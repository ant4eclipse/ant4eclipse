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
package org.ant4eclipse.pydt.test.data;

import java.net.URL;

/**
 * Common api used to produce project suites dynamically.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface ProjectSuiteApi {

  /**
   * Creates a simple project which is practically empty.
   * 
   * @param script
   *          The location of an ANT build script. Maybe <code>null</code>.
   * @param multiplefolders
   *          <code>true</code> <=> Create multiple source folders.
   * 
   * @return The name of the empty project. Neither <code>null</code> nor empty.
   */
  String createEmptyProject(final URL script, final boolean multiplefolders);

} /* ENDINTERFACE */
