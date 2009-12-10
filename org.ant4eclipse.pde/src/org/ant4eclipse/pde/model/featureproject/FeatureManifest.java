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
package org.ant4eclipse.pde.model.featureproject;

import org.osgi.framework.Version;

import java.util.List;

/**
 * <p>
 * A feature is a way of grouping and describing different functionality that makes up a product. Features do not
 * contain any code. They merely describe a set of plug-ins that provide the function for the feature and information
 * about how to update it. Features are packaged in a feature archive file and described using a feature manifest.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface FeatureManifest {

  /**
   * <p>
   * Returns the name of the application.
   * </p>
   * 
   * @return The name of the application.
   */
  String getApplication();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getColocationAffinity();

  /**
   * @return flag that, if "true", indicates that the feature cannot be installed in a group with other features.
   */
  boolean isExclusive();

  /**
   * @return feature identifier (eg. com.xyz.myfeature), required
   */
  String getId();

  /**
   * Returns the location of an associated image.
   * 
   * @return The location of an associated image.
   */
  String getImage();

  /**
   * @return locale specification.
   */
  String getLocale();

  /**
   * @return optional machine architecture specification.
   */
  String getMachineArchitecture();

  /**
   * @return operating system specification.
   */
  String getOperatingSystem();

  /**
   * Returns the plugin used for this feature.
   * 
   * @return The plugin used for this feature.
   */
  String getPlugin();

  /**
   * Returns true if this feature is a primary one.
   * 
   * @return true <=> This feature is a primary one.
   */
  boolean isPrimary();

  /**
   * @return display label identifying the organization providing this component.
   */
  String getProviderName();

  /**
   * @return component version (eg. 1.0.3), required
   */
  Version getVersion();

  /**
   * Returns the name of the windowing system.
   * 
   * @return The name of the windowing system.
   */
  String getWindowingSystem();

  /**
   * @return displayable label (name).
   */
  String getLabel();

  /**
   * @return all the referenced plugins.
   */
  List<Plugin> getPlugins();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<Includes> getIncludes();

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  interface Plugin {

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getDownloadSize();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean isFragment();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getId();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getInstallSize();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getLocale();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getMachineArchitecture();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getOperatingSystem();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean isUnpack();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    Version getVersion();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getWindowingSystem();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasDownloadSize();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasId();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasInstallSize();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasLocale();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasMachineArchitecture();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasOperatingSystem();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasVersion();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    boolean hasWindowingSystem();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    String getEffectiveVersion();

    /**
     * <p>
     * </p>
     * 
     * @param effectiveVersion
     */
    void setEffectiveVersion(String effectiveVersion);
  }

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  interface Includes {
    /**
     * <p>
     * </p>
     * 
     * @return the id
     */
    String getId();

    /**
     * <p>
     * </p>
     * 
     * @return the version
     */
    Version getVersion();

    /**
     * <p>
     * </p>
     * 
     * @return the name
     */
    String getName();

    /**
     * <p>
     * </p>
     * 
     * @return the optional
     */
    boolean isOptional();

    /**
     * <p>
     * </p>
     * 
     * @return the searchLocation
     */
    String getSearchLocation();

    /**
     * <p>
     * </p>
     * 
     * @return the operatingSystem
     */
    String getOperatingSystem();

    /**
     * <p>
     * </p>
     * 
     * @return the machineArchitecture
     */
    String getMachineArchitecture();

    /**
     * <p>
     * </p>
     * 
     * @return the windowingSystem
     */
    String getWindowingSystem();

    /**
     * <p>
     * </p>
     * 
     * @return the locale
     */
    String getLocale();
  }
}
