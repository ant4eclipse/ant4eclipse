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

import org.ant4eclipse.core.util.Pair;

import java.net.URL;

/**
 * Common api used to produce project suites dynamically.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface ProjectSuiteApi {

  // internal libraries, external libraries, external folders and runtimes

  int    KIND_MULTIPLESOURCEFOLDERSPRIMARY   = 1 << 0;

  int    KIND_MULTIPLESOURCEFOLDERSSECONDARY = 1 << 1;

  // the name to use for source folders with generated source
  String NAME_GENERATEDSOURCE                = "generated-source";

  /**
   * Creates a simple project which is practically empty.
   * 
   * @param script
   *          The location of an ANT build script. Maybe <code>null</code>.
   * @param projectsettings
   *          A list of flags used to control the creation of the project.
   * 
   * @return The name of the empty project. Neither <code>null</code> nor empty.
   */
  String createEmptyProject(final URL script, final int projectsettings);

  /**
   * Creates two projects where project one depends from project two.
   * 
   * @param script
   *          The location of an ANT build script. Maybe <code>null</code>.
   * @param projectsettings
   *          A list of flags used to control the creation of the project.
   * 
   * @return The name of the main project. Neither <code>null</code> nor empty.
   */
  Pair<String, String> createComplexProject(final URL script, final int projectsettings);

  /**
   * Creates two projects where both projects depends on each other.
   * 
   * @param script
   *          The location of an ANT build script. Maybe <code>null</code>.
   * @param projectsettings
   *          A list of flags used to control the creation of the project.
   * 
   * @return The name of the main project. Neither <code>null</code> nor empty.
   */
  Pair<String, String> createCyclicProject(final URL script, final int projectsettings);

} /* ENDINTERFACE */
