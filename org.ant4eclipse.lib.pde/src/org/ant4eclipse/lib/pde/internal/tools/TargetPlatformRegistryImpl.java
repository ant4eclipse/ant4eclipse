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
package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;


import org.ant4eclipse.platform.model.resource.Workspace;

import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.ant4eclipse.lib.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.lib.pde.tools.TargetPlatformDefinition;
import org.ant4eclipse.lib.pde.tools.TargetPlatformRegistry;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * The {@link TargetPlatformRegistryImpl} can be used to retrieve instances of type {@link TargetPlatform}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TargetPlatformRegistryImpl implements TargetPlatformRegistry {

  /** the current {@link TargetPlatform}, maybe null **/
  private TargetPlatform                         _currentTargetPlatform;

  /** the static map with all target platforms currently resolved */
  private Map<Object, BundleAndFeatureSet>       _bundleAndFeatureSetMap     = new HashMap<Object, BundleAndFeatureSet>();

  /** the target platform definitions */
  private Map<String, TargetPlatformDefinition>  _targetPlatformDefnitionMap = new HashMap<String, TargetPlatformDefinition>();

  /** - */
  private Map<TargetPlatformKey, TargetPlatform> _targetPlatformMap          = new HashMap<TargetPlatformKey, TargetPlatform>();

  /**
   * {@inheritDoc}
   */
  public TargetPlatform getCurrent() {
    return this._currentTargetPlatform;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasCurrent() {
    return this._currentTargetPlatform != null;
  }

  /**
   * {@inheritDoc}
   */
  public void setCurrent(TargetPlatform targetPlatform) {
    this._currentTargetPlatform = targetPlatform;
  }

  /**
   * {@inheritDoc}
   */
  public void clear() {
    this._bundleAndFeatureSetMap.clear();
    this._targetPlatformDefnitionMap.clear();
    this._targetPlatformMap.clear();
  }

  /**
   * {@inheritDoc}
   */
  public void addTargetPlatformDefinition(String identifier, TargetPlatformDefinition targetPlatformDefinition) {
    this._targetPlatformDefnitionMap.put(identifier, targetPlatformDefinition);

  }

  /**
   * {@inheritDoc}
   */
  public List<String> getTargetPlatformDefinitionIds() {
    return Collections.unmodifiableList(new LinkedList<String>(this._targetPlatformDefnitionMap.keySet()));
  }

  /**
   * {@inheritDoc}
   */
  public TargetPlatform getInstance(Workspace workspace, String targetPlatformDefinitionIdentifier,
      TargetPlatformConfiguration targetPlatformConfiguration) {

    if (A4ELogging.isTraceingEnabled()) {
      A4ELogging.trace("getInstance(%s, %s, %s)", workspace, targetPlatformDefinitionIdentifier,
          targetPlatformConfiguration);
    }

    TargetPlatformDefinition targetPlatformDefinition = this._targetPlatformDefnitionMap
        .get(targetPlatformDefinitionIdentifier);

    if (targetPlatformDefinition == null) {
      throw new Ant4EclipseException(PdeExceptionCode.NOT_TARGET_PLATFORM_DEFINITION,
          targetPlatformDefinitionIdentifier);
    }

    return getInstance(workspace, targetPlatformDefinition.getLocations(), targetPlatformConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  public TargetPlatformDefinition getTargetPlatformDefinition(String identifier) {
    return this._targetPlatformDefnitionMap.get(identifier);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasTargetPlatformDefinition(String identifier) {
    return this._targetPlatformDefnitionMap.containsKey(identifier);
  }

  /**
   * <p>
   * </p>
   * 
   * @param workspace
   * @return
   */
  private PluginAndFeatureProjectSet getPluginProjectSet(Workspace workspace) {

    if (!this._bundleAndFeatureSetMap.containsKey(workspace)) {
      this._bundleAndFeatureSetMap.put(workspace, new PluginAndFeatureProjectSet(workspace));
    }

    return (PluginAndFeatureProjectSet) this._bundleAndFeatureSetMap.get(workspace);
  }

  /**
   * <p>
   * Returns an instance of type {@link TargetPlatform} with the specified configuration.
   * </p>
   * 
   * @param workspace
   *          the workspace instance
   * @param targetLocations
   *          the target locations
   * @param targetPlatformConfiguration
   *          the target platform configuration
   * @return an instance of type {@link TargetPlatform} with the specified configuration.
   */
  private TargetPlatform getInstance(Workspace workspace, File[] targetLocations,
      TargetPlatformConfiguration targetPlatformConfiguration) {

    Assert.assertTrue((workspace != null) || (targetLocations != null),
        "Parameter workspace or targetLocations has to be set !");

    // TargetPlatformKey
    TargetPlatformKey key = new TargetPlatformKey(workspace, targetLocations, targetPlatformConfiguration);
    if (this._targetPlatformMap.containsKey(key)) {
      return this._targetPlatformMap.get(key);
    }

    // get the workspace bundle set
    BundleAndFeatureSet workspaceBundleSet = workspace != null ? getPluginProjectSet(workspace) : null;

    // get the binary bundle sets
    BundleAndFeatureSet[] binaryPluginSets = targetLocations != null ? getBinaryPluginSet(targetLocations) : null;

    // create and return the target platform instance
    TargetPlatform targetPlatform = new TargetPlatformImpl(workspaceBundleSet, binaryPluginSets,
        targetPlatformConfiguration, targetLocations);

    this._targetPlatformMap.put(key, targetPlatform);

    return targetPlatform;
  }

  /**
   * <p>
   * </p>
   * 
   * @param file
   * @return
   */
  private BinaryBundleAndFeatureSet getBinaryPluginSet(File file) {

    if (!this._bundleAndFeatureSetMap.containsKey(file)) {
      this._bundleAndFeatureSetMap.put(file, new BinaryBundleAndFeatureSet(file));
    }

    return (BinaryBundleAndFeatureSet) this._bundleAndFeatureSetMap.get(file);
  }

  /**
   * @param files
   * @return
   */
  private BinaryBundleAndFeatureSet[] getBinaryPluginSet(File[] files) {

    //
    List<BinaryBundleAndFeatureSet> result = new LinkedList<BinaryBundleAndFeatureSet>();

    //
    for (File file : files) {
      result.add(getBinaryPluginSet(file));
    }

    //
    return result.toArray(new BinaryBundleAndFeatureSet[0]);
  }

  /**
   * <p>
   * The key of a target platform.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class TargetPlatformKey {

    /** the workspace */
    private Workspace                   _workspace;

    /** the target locations */
    private File[]                      _targetLocations;

    /** the target platform configuration */
    private TargetPlatformConfiguration _targetPlatformConfiguration;

    /**
     * <p>
     * Creates a new instance of type {@link TargetPlatformKey}.
     * </p>
     * 
     * @param workspace
     *          the workspace
     * @param targetLocations
     *          the target locations
     * @param targetPlatformConfiguration
     *          the target platform configuration
     */
    public TargetPlatformKey(Workspace workspace, File[] targetLocations,
        TargetPlatformConfiguration targetPlatformConfiguration) {

      this._targetLocations = targetLocations;
      this._targetPlatformConfiguration = targetPlatformConfiguration;
      this._workspace = workspace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + hashCode(this._targetLocations);
      result = prime * result
          + ((this._targetPlatformConfiguration == null) ? 0 : this._targetPlatformConfiguration.hashCode());
      result = prime * result + ((this._workspace == null) ? 0 : this._workspace.hashCode());
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      TargetPlatformKey other = (TargetPlatformKey) obj;
      if (!getOuterType().equals(other.getOuterType())) {
        return false;
      }
      if (!Arrays.equals(this._targetLocations, other._targetLocations)) {
        return false;
      }
      if (this._targetPlatformConfiguration == null) {
        if (other._targetPlatformConfiguration != null) {
          return false;
        }
      } else if (!this._targetPlatformConfiguration.equals(other._targetPlatformConfiguration)) {
        return false;
      }
      if (this._workspace == null) {
        if (other._workspace != null) {
          return false;
        }
      } else if (!this._workspace.equals(other._workspace)) {
        return false;
      }
      return true;
    }

    /**
     * <p>
     * Helper method.
     * </p>
     * 
     * @return the outer type
     */
    private TargetPlatformRegistryImpl getOuterType() {
      return TargetPlatformRegistryImpl.this;
    }

    /**
     * <p>
     * Helper method.
     * </p>
     * 
     * @param array
     *          the object array
     * @return
     */
    private int hashCode(Object[] array) {
      int prime = 31;
      if (array == null) {
        return 0;
      }
      int result = 1;
      for (Object element : array) {
        result = prime * result + (element == null ? 0 : element.hashCode());
      }
      return result;
    }
  }
}
