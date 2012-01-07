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
package org.ant4eclipse.lib.core.osgi;

import java.io.File;
import java.util.List;
import java.util.jar.Manifest;

/**
 * <p>
 * A bundle layout resolver can be used to resolve the layout of a specific bundle type (e.g. jared bundle, exploded
 * bundle, eclipse plug-in project).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface BundleLayoutResolver {

  /** constants to describe PROJECT type */
  byte PROJECT = 1;

  /** constants to describe LIBRARY type */
  byte LIBRARY = 2;

  /**
   * <p>
   * Returns the type of the contained bundle ({@link BundleLayoutResolver#PROJECT} or
   * {@link BundleLayoutResolver#LIBRARY}).
   * </p>
   * 
   * @return the type of the contained bundle.
   */
  byte getType();

  /**
   * <p>
   * Returns the location of the contained bundle.
   * </p>
   * <p>
   * This method must return a value != <code>null</code>.
   * </p>
   * 
   * @return the location.
   */
  File getLocation();

  /**
   * <p>
   * Returns the manifest of the contained bundle.
   * </p>
   * <p>
   * This method must return a value != <code>null</code>.
   * </p>
   * 
   * @return the manifest.
   */
  Manifest getManifest();

  /**
   * <p>
   * Returns the resolved bundle class path.
   * </p>
   * <p>
   * This method must return a value != <code>null</code>.
   * </p>
   * 
   * @return the resolved bundle class path.
   */
  List<File> resolveBundleClasspathEntries();

} /* ENDINTERFACE */
