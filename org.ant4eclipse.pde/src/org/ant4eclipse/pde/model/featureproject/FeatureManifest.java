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
package org.ant4eclipse.pde.model.featureproject;

import org.osgi.framework.Version;

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
  public String getApplication();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getColocationAffinity();

  /**
   * @return flag that, if "true", indicates that the feature cannot be installed in a group with other features.
   */
  public boolean isExclusive();

  /**
   * @return feature identifier (eg. com.xyz.myfeature), required
   */
  public String getId();

  /**
   * Returns the location of an associated image.
   * 
   * @return The location of an associated image.
   */
  public String getImage();

  /**
   * @return locale specification.
   */
  public String getLocale();

  /**
   * @return optional machine architecture specification.
   */
  public String getMachineArchitecture();

  /**
   * @return operating system specification.
   */
  public String getOperatingSystem();

  /**
   * Returns the plugin used for this feature.
   * 
   * @return The plugin used for this feature.
   */
  public String getPlugin();

  /**
   * Returns true if this feature is a primary one.
   * 
   * @return true <=> This feature is a primary one.
   */
  public boolean isPrimary();

  /**
   * @return display label identifying the organization providing this component.
   */
  public String getProviderName();

  /**
   * @return component version (eg. 1.0.3), required
   */
  public Version getVersion();

  /**
   * Returns the name of the windowing system.
   * 
   * @return The name of the windowing system.
   */
  public String getWindowingSystem();

  /**
   * @return displayable label (name).
   */
  public String getLabel();

  /**
   * @return all the referenced plugins.
   */
  public Plugin[] getPlugins();

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static interface Plugin {

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getDownloadSize();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public boolean isFragment();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getId();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getInstallSize();

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getLocale();

    public String getMachineArchitecture();

    public String getOperatingSystem();

    public boolean isUnpack();

    public Version getVersion();

    public String getWindowingSystem();

    public boolean hasDownloadSize();

    public boolean hasId();

    public boolean hasInstallSize();

    public boolean hasLocale();

    public boolean hasMachineArchitecture();

    public boolean hasOperatingSystem();

    public boolean hasVersion();

    public boolean hasWindowingSystem();

    public String getEffectiveVersion();

    public void setEffectiveVersion(String effectiveVersion);
  }
}
