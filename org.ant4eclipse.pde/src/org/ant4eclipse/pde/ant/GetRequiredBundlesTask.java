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
package org.ant4eclipse.pde.ant;


import org.ant4eclipse.pde.internal.tools.BundleDependenciesResolver;
import org.ant4eclipse.pde.internal.tools.TargetPlatformImpl;
import org.ant4eclipse.pde.internal.tools.UnresolvedBundleException;
import org.ant4eclipse.pde.internal.tools.BundleDependenciesResolver.BundleDependency;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.pde.tools.TargetPlatformRegistry;

import org.ant4eclipse.platform.ant.core.GetPathComponent;
import org.ant4eclipse.platform.ant.core.delegate.GetPathDelegate;
import org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectPathTask;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.apache.tools.ant.BuildException;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * The {@link GetRequiredBundlesTask} task can be used to resolve the required bundles for a given set of bundles.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetRequiredBundlesTask extends AbstractProjectPathTask implements TargetPlatformAwareComponent,
    GetPathComponent {

  /** the workspace delegate */
  private WorkspaceDelegate               _workspaceDelegate;

  /** the target platform delegate */
  private TargetPlatformAwareDelegate     _targetPlatformAwareDelegate;

  /** */
  private GetPathComponent                _getPathComponent;

  // /** indicates if optional dependencies should be resolved */
  // private boolean _includeOptionalDependencies = true;

  // /** indicates if the specified bundles should be part of the result */
  // private boolean _includeSpecifiedBundles = true;

  /** indicates if workspace bundles should be part of the result */
  private boolean                         _includeWorkspaceBundles = true;

  /** indicates if the bundle class path should be resolved */
  private boolean                         _resolveBundleClasspath  = true;

  /** the bundle symbolic name */
  private String                          _bundleSymbolicName;

  /** the bundle version */
  private String                          _bundleVersion;

  /** the bundle specifications */
  private LinkedList<BundleSpecification> _bundleSpecifications;

  /** the target platform */
  private TargetPlatform                  _targetPlatform;

  /** */
  private Set<BundleDescription>          _resolvedBundleDescriptions;

  /**
   * <p>
   * Creates a new instance of type GetRequiredBundles.
   * </p>
   */
  public GetRequiredBundlesTask() {

    // create the delegates
    this._workspaceDelegate = new WorkspaceDelegate(this);
    this._getPathComponent = new GetPathDelegate(this);
    this._targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();

    this._bundleSpecifications = new LinkedList<BundleSpecification>();
    this._resolvedBundleDescriptions = new HashSet<BundleDescription>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Workspace getWorkspace() {
    return this._workspaceDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final File getWorkspaceDirectory() {
    return this._workspaceDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isWorkspaceDirectorySet() {
    return this._workspaceDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireWorkspaceDirectorySet() {
    this._workspaceDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Deprecated
  public final void setWorkspace(File workspace) {
    this._workspaceDelegate.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._workspaceDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  /**
   * {@inheritDoc}
   */
  public final String getTargetPlatformId() {
    return this._targetPlatformAwareDelegate.getTargetPlatformId();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isTargetPlatformIdSet() {
    return this._targetPlatformAwareDelegate.isTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireTargetPlatformIdSet() {
    this._targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformAwareDelegate.setTargetPlatformId(targetPlatformId);
  }

  /**
   * {@inheritDoc}
   */
  public String getPathId() {
    return this._getPathComponent.getPathId();
  }

  /**
   * {@inheritDoc}
   */
  public String getProperty() {
    return this._getPathComponent.getProperty();
  }

  /**
   * {@inheritDoc}
   */
  public File[] getResolvedPath() {
    return this._getPathComponent.getResolvedPath();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPathIdSet() {
    return this._getPathComponent.isPathIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPropertySet() {
    return this._getPathComponent.isPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRelative() {
    return this._getPathComponent.isRelative();
  }

  /**
   * {@inheritDoc}
   */
  public void populatePathId() {
    this._getPathComponent.populatePathId();
  }

  /**
   * {@inheritDoc}
   */
  public void populateProperty() {
    this._getPathComponent.populateProperty();
  }

  /**
   * {@inheritDoc}
   */
  public void requirePathIdOrPropertySet() {
    this._getPathComponent.requirePathIdOrPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  public void setPathId(String id) {
    this._getPathComponent.setPathId(id);
  }

  /**
   * {@inheritDoc}
   */
  public void setProperty(String property) {
    this._getPathComponent.setProperty(property);
  }

  /**
   * {@inheritDoc}
   */
  public void setRelative(boolean relative) {
    this._getPathComponent.setRelative(relative);
  }

  /**
   * {@inheritDoc}
   */
  public void setResolvedPath(File[] resolvedPath) {
    this._getPathComponent.setResolvedPath(resolvedPath);
  }

  /**
   * <p>
   * </p>
   * 
   * @return the includeWorkspaceBundles
   */
  public boolean isIncludeWorkspaceBundles() {
    return this._includeWorkspaceBundles;
  }

  /**
   * <p>
   * </p>
   * 
   * @param includeWorkspaceBundles
   *          the includeWorkspaceBundles to set
   */
  public void setIncludeWorkspaceBundles(boolean includeWorkspaceBundles) {
    this._includeWorkspaceBundles = includeWorkspaceBundles;
  }

  /**
   * <p>
   * Returns the bundle symbolic name
   * </p>
   * 
   * @return the bundle symbolic name
   */
  public String getBundleSymbolicName() {
    return this._bundleSymbolicName;
  }

  /**
   * <p>
   * Sets the bundle symbolic name
   * </p>
   * 
   * @param bundleSymbolicName
   *          the bundleSymbolicName to set
   */
  public void setBundleSymbolicName(String bundleSymbolicName) {
    this._bundleSymbolicName = bundleSymbolicName;
  }

  /**
   * <p>
   * Returns the bundle version
   * </p>
   * 
   * @return the bundleVersion
   */
  public String getBundleVersion() {
    return this._bundleVersion;
  }

  /**
   * <p>
   * Sets the bundle version
   * </p>
   * 
   * @param bundleVersion
   *          the bundleVersion to set
   */
  public void setBundleVersion(String bundleVersion) {
    this._bundleVersion = bundleVersion;
  }

  /**
   * <p>
   * Adds the {@link BundleSpecification} to the list of root bundles.
   * </p>
   * 
   * @param specification
   *          the bundle specification
   */
  public void addConfiguredBundle(BundleSpecification specification) {

    // assert not null
    Assure.notNull(specification);

    // assert symbolic name is set
    if (Utilities.hasText(specification._symbolicName)) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "bundleSymbolicName");
    }

    // assert valid version
    try {
      specification.getVersion();
    } catch (Exception e) {
      throw new Ant4EclipseException(PdeExceptionCode.INVALID_VERSION, "bundleSymbolicName", "bundle");
    }

    // add specification
    this._bundleSpecifications.add(specification);
  }

  /**
   * <p>
   * Creates a new {@link BundleSpecification} instance.
   * </p>
   * 
   * @return a new {@link BundleSpecification} instance.
   */
  public BundleSpecification createBundle() {
    return new BundleSpecification();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // step 1: clear the result list
    this._resolvedBundleDescriptions.clear();

    // step 2: get the target platform
    initTargetPlatform();

    // step 3: add the bundle specification attribute to the the list of (root) bundle specifications
    if (Utilities.hasText(this._bundleSymbolicName)) {
      this._bundleSpecifications.add(new BundleSpecification(this._bundleSymbolicName, this._bundleVersion));
    }

    // step 4: resolve required bundles for all bundle specifications
    for (BundleSpecification bundleSpecification : this._bundleSpecifications) {

      // get the resolved bundle description from the target platform
      BundleDescription bundleDescription = this._targetPlatform.getResolvedBundle(bundleSpecification
          .getSymbolicName(), bundleSpecification.getVersion());

      // if not resolved bundle description is found, throw an exception
      if (bundleDescription == null) {
        throw new Ant4EclipseException(PdeExceptionCode.SPECIFIED_BUNDLE_NOT_FOUND, bundleSpecification
            .getSymbolicName(), bundleSpecification.getVersion());
      }

      // resolve the required ones
      resolveReferencedBundles(bundleDescription);
    }

    // step 5: resolve the path
    List<File> result = new LinkedList<File>();

    for (BundleDescription bundleDescription : this._resolvedBundleDescriptions) {

      // don't add the bundle if bundle source is an eclipse project and _includeWorkspaceBundles == false
      BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();
      if (this._includeWorkspaceBundles || !(bundleSource.isEclipseProject())) {

        // get the layout resolver
        BundleLayoutResolver layoutResolver = BundleDependenciesResolver.getBundleLayoutResolver(bundleDescription);

        // add the files
        if (this._resolveBundleClasspath) {
          File[] files = layoutResolver.resolveBundleClasspathEntries();
          result.addAll(Arrays.asList(files));
        } else {
          result.add(layoutResolver.getLocation());
        }
      }
    }

    // set the resolved path
    setResolvedPath(result.toArray(new File[0]));

    // set the path
    if (isPathIdSet()) {
      populatePathId();
    }

    // set the property
    if (isPropertySet()) {
      populateProperty();
    }
  }

  /**
   * <p>
   * Helper method. Initializes the target platform.
   * </p>
   */
  private void initTargetPlatform() {

    // create the configuration
    TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
    configuration.setPreferProjects(true);

    // get the target platform registry
    TargetPlatformRegistry targetPlatformRegistry = ServiceRegistry.instance().getService(TargetPlatformRegistry.class);

    // set the target platform
    this._targetPlatform = targetPlatformRegistry.getInstance(getWorkspace(), getTargetPlatformId(), configuration);
  }

  /**
   * <p>
   * Resolves the referenced bundles for the given bundle description.
   * </p>
   * 
   * @param bundleDescription
   *          the referenced bundles for the given bundle description.
   */
  private void resolveReferencedBundles(BundleDescription bundleDescription) {

    // step 1: add the bundle description to the list of resolved descriptions or
    // return if the description already has been resolved
    // TODO: maybe we have to check if the bundle description has attached fragments (in case it is indirectly
    // referenced?)
    if (this._resolvedBundleDescriptions.contains(bundleDescription) /* || _excludedBundles.contains(bundleDescription) */) {
      return;
    } else {
      this._resolvedBundleDescriptions.add(bundleDescription);
    }

    // step 2: resolve bundle dependencies
    List<BundleDependency> bundleDependencies = null;

    try {
      bundleDependencies = new BundleDependenciesResolver().resolveBundleClasspath(bundleDescription);
    } catch (UnresolvedBundleException e) {
      // throw a BUNDLE_NOT_RESOLVED_EXCEPTION
      throw new Ant4EclipseException(PdeExceptionCode.BUNDLE_NOT_RESOLVED_EXCEPTION, TargetPlatformImpl
          .dumpResolverErrors(bundleDescription, true));
    }

    // step 3: resolve the referenced bundles
    for (BundleDependency bundleDependency : bundleDependencies) {

      // resolve the host
      resolveReferencedBundles(bundleDependency.getHost());

      // resolve the fragments
      for (BundleDescription fragment : bundleDependency.getFragments()) {
        resolveReferencedBundles(fragment);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {

    // require fields
    requirePathIdOrPropertySet();
    requireTargetPlatformIdSet();

    // if attribute 'bundleSymbolicName' is set, no 'bundle' element is allowed
    if (Utilities.hasText(this._bundleSymbolicName) && !this._bundleSpecifications.isEmpty()) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_X_OR_ELEMENT_Y, "bundleSymbolicName", "bundle");
    }

    // if attribute 'bundleVersion' is set, 'bundleSymbolicName' must be specified
    if (!Utilities.hasText(this._bundleSymbolicName) && Utilities.hasText(this._bundleVersion)) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_X_WITHOUT_ATTRIBUTE_Y, "bundleVersion",
          "bundleSymbolicName");
    }
  }

  /**
   * <p>
   * Encapsulates a bundle specification.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class BundleSpecification {

    /** the symbolicName */
    private String _symbolicName;

    /** the version */
    private String _version;

    /**
     * <p>
     * Creates a new instance of type {@link BundleSpecification}.
     * </p>
     */
    public BundleSpecification() {
      // nothing to do here...
    }

    /**
     * <p>
     * Creates a new instance of type {@link BundleSpecification}.
     * </p>
     * 
     * @param symbolicName
     *          the symbolic name
     * @param version
     *          the version
     */
    public BundleSpecification(String symbolicName, String version) {
      this._symbolicName = symbolicName;
      this._version = version;
    }

    /**
     * <p>
     * Returns the symbolic name.
     * </p>
     * 
     * @return the symbolicName
     */
    public String getSymbolicName() {
      return this._symbolicName;
    }

    /**
     * <p>
     * Sets the symbolic name.
     * </p>
     * 
     * @param symbolicName
     *          the symbolicName to set
     */
    public void setSymbolicName(String symbolicName) {
      this._symbolicName = symbolicName;
    }

    /**
     * <p>
     * Returns the bundle version.
     * </p>
     * 
     * @return the version
     */
    public Version getVersion() {
      return this._version != null ? new Version(this._version) : null;
    }

    /**
     * <p>
     * Sets the bundle version.
     * </p>
     * 
     * @param version
     *          the version to set
     */
    public void setVersion(String version) {
      this._version = version;
    }

    /**
     * <p>
     * Returns <code>true</code> if the bundle version is set.
     * </p>
     * 
     * @return <code>true</code> if the bundle version is set, <code>false</code> otherwise.
     */
    public boolean hasBundleVersion() {
      return this._version != null;
    }
  }
}
