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
package org.ant4eclipse.pde.internal.tools;

import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * A bundle set represents a set of bundles stored in a specific location. A bundle can be a plug-in project or a binary
 * bundle that is packed as a OSGi bundle or a exploded directory.
 * </p>
 * <p>
 * A bundle set provides several methods for retrieving bundles from their location.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public interface BundleSet {

  /**
   * <p>
   * Returns the id of the bundle.
   * </p>
   * 
   * @return the id of the bundle.
   */
  public Object getId();

  /**
   * <p>
   * Initializes the bundle set (which means that the bundles will be reed from the underlying location).
   * </p>
   * <p>
   * If the bundle set already has been initialized, the method must return immediately. The initialization may a time
   * consuming operation since all bundles in the bundle set have to be parsed.
   * </p>
   */
  public void initialize();

  /**
   * <p>
   * Invalidates this bundle set. Invalidating a bundle set forces it to be refreshed the next time it is accessed.
   * </p>
   */
  public void invalidate();

  /**
   * <p>
   * Returns <code>true</code> if the bundle set has been initialized, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if the bundle set has been initialized, <code>false</code> otherwise.
   */
  public boolean isInitialised();

  /**
   * <p>
   * Refreshes the bundle set (which means that the bundles will be reed from the underlying location again).
   * </p>
   */
  public void refresh();

  /**
   * <p>
   * Returns all {@link BundleDescription BundleDescriptions} that are contained in this bundle set.
   * </p>
   * 
   * @return all {@link BundleDescription BundleDescriptions} that are contained in this bundle set.
   */
  public BundleDescription[] getAllBundleDescriptions();

  /**
   * <p>
   * Returns <code>true</code> if the {@link BundleSet} contains a Bundle with the given symbolic name.
   * </p>
   * 
   * @param symbolicName
   *          the symbolic name.
   * @return <code>true</code> if the {@link BundleSet} contains a Bundle with the given symbolic name.
   */
  public boolean contains(String symbolicName);
}