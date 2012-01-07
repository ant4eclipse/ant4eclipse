/**********************************************************************
 * Copyright (c) 2005-2006 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.lib.platform.model.team.projectset;

import org.ant4eclipse.lib.core.A4EService;

import java.util.List;

/**
 * Factory-Interface for construction TeamProjectSet- and TeamProjectDescription instances.
 * 
 * <p>
 * The implementating classes must be registered in the file <tt>teamproviders.properties</tt>.
 * 
 * <p>
 * Implementations of TeamProjectSetFactories should be stateless, since they are reused.
 * 
 */
public interface TeamProjectSetFactory extends A4EService {

  /**
   * Creates a new, provider-specific, TeamProjectSet instance for the team project set with the given name.
   * 
   * @param projectSetName
   *          The name of the team project set
   * @return The new TeamProjectSet-instance without any TeamProjectDescription
   */
  TeamProjectSet createTeamProjectSet( String projectSetName );

  /**
   * Creates a TeamProjectDescription for the given reference-String and adds it to the team project set
   * 
   * @param projectSet
   *          The project set this reference belongs to.
   * @param reference
   *          The reference string read out of the Project Set-File
   */
  void addTeamProjectDescription( TeamProjectSet projectSet, String reference );

  /**
   * Returns a list of all provider IDs for the implementing TeamProjectSetFactory instance.
   * 
   * @return A list of all provider IDs for the implementing TeamProjectSetFactory instance. Neither <code>null</code>
   *         nor empty.
   */
  List<String> getProviderIDs();

} /* ENDINTERFACE */
