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

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.pde.tools.TargetPlatformDefinition;
import org.ant4eclipse.pde.tools.TargetPlatformRegistry;
import org.ant4eclipse.platform.model.resource.Workspace;

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
  private TargetPlatform                              _currentTargetPlatform;

  /** the static map with all target platforms currently resolved */
  private final Map<Object, BundleAndFeatureSet>      _targetPlatformMap          = new HashMap<Object, BundleAndFeatureSet>();

  /** the target platform definitions */
  private final Map<String, TargetPlatformDefinition> _targetPlatformDefnitionMap = new HashMap<String, TargetPlatformDefinition>();

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
  public void setCurrent(final TargetPlatform targetPlatform) {
    this._currentTargetPlatform = targetPlatform;
  }

  /**
   * {@inheritDoc}
   */
  public void clear() {
    this._targetPlatformMap.clear();
    this._targetPlatformDefnitionMap.clear();
  }

  /**
   * {@inheritDoc}
   */
  public void addTargetPlatformDefinition(String identifier, TargetPlatformDefinition targetPlatformDefinition) {
    _targetPlatformDefnitionMap.put(identifier, targetPlatformDefinition);

  }

  /**
   * {@inheritDoc}
   */
  public TargetPlatform getInstance(Workspace workspace, String targetPlatformDefinitionIdentifier,
      TargetPlatformConfiguration targetPlatformConfiguration) {

    TargetPlatformDefinition targetPlatformDefinition = _targetPlatformDefnitionMap
        .get(targetPlatformDefinitionIdentifier);

    return getInstance(workspace, targetPlatformDefinition.getLocations(), targetPlatformConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  public TargetPlatformDefinition getTargetPlatformDefinition(String identifier) {
    return _targetPlatformDefnitionMap.get(identifier);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasTargetPlatformDefinition(String identifier) {
    return _targetPlatformDefnitionMap.containsKey(identifier);
  }

  /**
   * <p>
   * </p>
   * 
   * @param workspace
   * @return
   */
  private PluginAndFeatureProjectSet getPluginProjectSet(final Workspace workspace) {

    if (!this._targetPlatformMap.containsKey(workspace)) {
      this._targetPlatformMap.put(workspace, new PluginAndFeatureProjectSet(workspace));
    }

    return (PluginAndFeatureProjectSet) this._targetPlatformMap.get(workspace);
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
  private TargetPlatform getInstance(final Workspace workspace, final File[] targetLocations,
      final TargetPlatformConfiguration targetPlatformConfiguration) {

    Assert.assertTrue((workspace != null) || (targetLocations != null),
        "Parameter workspace or targetLocations has to be set !");

    // TargetPlatformKey
    final TargetPlatformKey key = new TargetPlatformKey(workspace, targetLocations, targetPlatformConfiguration);
    if (this._targetPlatformMap.containsKey(key)) {
      return (TargetPlatform) this._targetPlatformMap.get(key);
    }

    // get the workspace bundle set
    final BundleAndFeatureSet workspaceBundleSet = workspace != null ? getPluginProjectSet(workspace) : null;

    // get the binary bundle sets
    final BundleAndFeatureSet[] binaryPluginSets = targetLocations != null ? getBinaryPluginSet(targetLocations) : null;

    // create and return the target platform instance
    return new TargetPlatformImpl(workspaceBundleSet, binaryPluginSets, targetPlatformConfiguration);
  }

  /**
   * <p>
   * </p>
   * 
   * @param file
   * @return
   */
  private BinaryBundleAndFeatureSet getBinaryPluginSet(final File file) {

    if (!this._targetPlatformMap.containsKey(file)) {
      this._targetPlatformMap.put(file, new BinaryBundleAndFeatureSet(file));
    }

    return (BinaryBundleAndFeatureSet) this._targetPlatformMap.get(file);
  }

  /**
   * @param files
   * @return
   */
  private BinaryBundleAndFeatureSet[] getBinaryPluginSet(final File[] files) {

    //
    final List<BinaryBundleAndFeatureSet> result = new LinkedList<BinaryBundleAndFeatureSet>();

    //
    for (int i = 0; i < files.length; i++) {
      result.add(getBinaryPluginSet(files[i]));
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
    private final Workspace                   _workspace;

    /** the target locations */
    private final File[]                      _targetLocations;

    /** the target platform configuration */
    private final TargetPlatformConfiguration _targetPlatformConfiguration;

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
    public TargetPlatformKey(final Workspace workspace, final File[] targetLocations,
        final TargetPlatformConfiguration targetPlatformConfiguration) {

      this._targetLocations = targetLocations;
      this._targetPlatformConfiguration = targetPlatformConfiguration;
      this._workspace = workspace;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
      final int prime = 31;
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
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final TargetPlatformKey other = (TargetPlatformKey) obj;
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
     * @param array the object array
     * @return
     */
    private int hashCode(final Object[] array) {
      final int prime = 31;
      if (array == null) {
        return 0;
      }
      int result = 1;
      for (int index = 0; index < array.length; index++) {
        result = prime * result + (array[index] == null ? 0 : array[index].hashCode());
      }
      return result;
    }
  }
}
