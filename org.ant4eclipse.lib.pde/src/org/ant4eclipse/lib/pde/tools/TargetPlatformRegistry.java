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
package org.ant4eclipse.lib.pde.tools;

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

import java.util.List;

/**
 * <p>
 * The target platform factory can be used to get an instance of type TargetPlatform. The created instances are stored
 * in a map.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public interface TargetPlatformRegistry extends A4EService {

  /**
   * <p>
   * </p>
   * 
   * @param identifier
   * @param targetPlatformDefinition
   */
  void addTargetPlatformDefinition( String identifier, TargetPlatformDefinition targetPlatformDefinition );

  /**
   * <p>
   * </p>
   * 
   * @param identifier
   * @return
   */
  boolean hasTargetPlatformDefinition( String identifier );

  /**
   * <p>
   * Returns the {@link TargetPlatformDefinitionDataType}.
   * </p>
   * 
   * @return
   */
  TargetPlatformDefinition getTargetPlatformDefinition( String identifier );

  /**
   * <p>
   * </p>
   * 
   * @param identifier
   * @param platformConfiguration
   */
  void addPlatformConfiguration( String identifier, PlatformConfiguration platformConfiguration );

  /**
   * <p>
   * </p>
   * 
   * @param identifier
   * @return
   */
  boolean hasPlatformConfiguration( String identifier );

  /**
   * <p>
   * </p>
   * 
   * @param identifier
   * @return
   */
  PlatformConfiguration getPlatformConfiguration( String identifier );

  /**
   * <p>
   * Returns the target platform definition IDs.
   * </p>
   * 
   * @return the target platform definition IDs.
   */
  List<String> getTargetPlatformDefinitionIds();

  /**
   * <p>
   * Returns an instance of TargetPlatform with the given targetLocation and workspace.
   * <p>
   * 
   * @param workspace
   *          the workspace which contains the projects to build if any
   * @param targetLocation
   *          the location of the platform against which the workspace plugins will be compiled and tested. May be null.
   *          If null, only plugins from the workspace will be used.
   * 
   * @return the TargetPlatform
   */
  TargetPlatform getInstance( Workspace workspace, String targetPlatformDefinitionIdentifier,
      PlatformConfiguration targetPlatformConfiguration );

  /**
   * @param id
   */
  void setCurrent( TargetPlatform targetPlatform );

  /**
   * <p>
   * Returns the current {@link TargetPlatform} instance. If no current {@link TargetPlatformRegistry} is set, an
   * Exception will be thrown.
   * </p>
   * 
   * @return the current Application Platform. If no current platform is in use an exception will be thrown.
   */
  TargetPlatform getCurrent();

  /**
   * <p>
   * Returns <code>true</code> if a current {@link DmServer} is specified.
   * </p>
   * 
   * @return
   */
  boolean hasCurrent();

  /**
   * <p>
   * Refreshes all target platforms that are defined in this registry.
   * </p>
   */
  void refreshAll();

} /* ENDINTERFACE */
