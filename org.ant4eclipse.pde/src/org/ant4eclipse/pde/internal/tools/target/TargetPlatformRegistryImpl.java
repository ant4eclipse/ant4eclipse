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
package org.ant4eclipse.pde.internal.tools.target;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.pde.tools.TargetPlatformDefinition;
import org.ant4eclipse.pde.tools.target.TargetPlatform;
import org.ant4eclipse.pde.tools.target.TargetPlatformConfiguration;
import org.ant4eclipse.pde.tools.target.TargetPlatformRegistry;
import org.ant4eclipse.platform.model.resource.Workspace;

/**
 * <p>
 * The {@link TargetPlatformRegistryImpl} can be used to retrieve instances of type {@link TargetPlatform}. The registry
 * also implements a map that stores instances that already have been requested.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TargetPlatformRegistryImpl implements TargetPlatformRegistry {

  /** the current {@link TargetPlatform}, maybe null **/
  private TargetPlatform                              _currentTargetPlatform;

  /** the cache for plug-in project sets **/
  private final Map                                   _pluginProjectSetMap        = new HashMap();

  /** the cache for **/
  private final Map                                   _binaryBundleSetMap         = new HashMap();

  /** the static map with all target platforms currently resolved */
  private final Map                                   _targetPlatformMap          = new HashMap();

  /** */
  private final Map<String, TargetPlatformDefinition> _targetPlatformDefnitionMap = new HashMap<String, TargetPlatformDefinition>();

  /*
   * (non-Javadoc)
   *
   * @see net.sf.ant4eclipse.tools.pde.target.TargetPlatformRegistry#getCurrent()
   */
  public TargetPlatform getCurrent() {
    return this._currentTargetPlatform;
  }

  public boolean hasCurrent() {
    return this._currentTargetPlatform != null;
  }

  public void setCurrent(final TargetPlatform targetPlatform) {
    this._currentTargetPlatform = targetPlatform;
  }

  public TargetPlatform getInstance(final Workspace workspace, final File[] targetLocations,
      final TargetPlatformConfiguration targetPlatformConfiguration) {

    Assert.assertTrue((workspace != null) || (targetLocations != null),
        "Parameter workspace or targetLocations has to be set !");

    System.err.println(Arrays.asList(targetLocations));

    // TargetPlatformKey
    final TargetPlatformKey key = new TargetPlatformKey(workspace, targetLocations, targetPlatformConfiguration);
    if (this._targetPlatformMap.containsKey(key)) {
      return (TargetPlatform) this._targetPlatformMap.get(key);
    }

    final BundleSet workspaceBundleSet = workspace != null ? getPluginProjectSet(workspace) : null;

    final BundleSet[] binaryPluginSets = targetLocations != null ? getBinaryPluginSet(targetLocations) : null;

    return new TargetPlatformImpl(workspaceBundleSet, binaryPluginSets, targetPlatformConfiguration);
  }

  /**
   * Removes all target platforms from the factory.
   */
  public void clear() {
    //
    this._pluginProjectSetMap.clear();
    //
    this._binaryBundleSetMap.clear();
    //
    this._targetPlatformMap.clear();

    this._targetPlatformDefnitionMap.clear();
  }

  public void addTargetPlatformDefinition(String identifier, TargetPlatformDefinition targetPlatformDefinition) {
    _targetPlatformDefnitionMap.put(identifier, targetPlatformDefinition);

  }

  public TargetPlatform getInstance(Workspace workspace, String targetPlatformDefinitionIdentifier,
      TargetPlatformConfiguration targetPlatformConfiguration) {

    TargetPlatformDefinition targetPlatformDefinition = _targetPlatformDefnitionMap
        .get(targetPlatformDefinitionIdentifier);

    return getInstance(workspace, targetPlatformDefinition.getLocations(), targetPlatformConfiguration);
  }

  public TargetPlatformDefinition getTargetPlatformDefinition(String identifier) {
    return _targetPlatformDefnitionMap.get(identifier);
  }

  public boolean hasTargetPlatformDefinition(String identifier) {
    return _targetPlatformDefnitionMap.containsKey(identifier);
  }

  private PluginProjectSet getPluginProjectSet(final Workspace workspace) {

    if (!this._targetPlatformMap.containsKey(workspace)) {
      this._targetPlatformMap.put(workspace, new PluginProjectSet(workspace));
    }

    return (PluginProjectSet) this._targetPlatformMap.get(workspace);
  }

  private BinaryBundleSet getBinaryPluginSet(final File file) {

    if (!this._targetPlatformMap.containsKey(file)) {
      this._targetPlatformMap.put(file, new BinaryBundleSet(file));
    }

    return (BinaryBundleSet) this._targetPlatformMap.get(file);
  }

  /**
   * @param files
   * @return
   */
  private BinaryBundleSet[] getBinaryPluginSet(final File[] files) {

    //
    final List result = new LinkedList();

    //
    for (int i = 0; i < files.length; i++) {
      result.add(getBinaryPluginSet(files[i]));
    }

    //
    return (BinaryBundleSet[]) result.toArray(new BinaryBundleSet[0]);
  }

  /**
   * TargetPlatformKey --
   *
   * @author Wuetherich-extern
   */
  private class TargetPlatformKey {

    private final Workspace                   _workspace;

    private final File[]                      _targetLocations;

    private final TargetPlatformConfiguration _targetPlatformConfiguration;

    public TargetPlatformKey(final Workspace workspace, final File[] targetLocations,
        final TargetPlatformConfiguration targetPlatformConfiguration) {
      this._targetLocations = targetLocations;
      this._targetPlatformConfiguration = targetPlatformConfiguration;
      this._workspace = workspace;
    }

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

    private TargetPlatformRegistryImpl getOuterType() {
      return TargetPlatformRegistryImpl.this;
    }

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
