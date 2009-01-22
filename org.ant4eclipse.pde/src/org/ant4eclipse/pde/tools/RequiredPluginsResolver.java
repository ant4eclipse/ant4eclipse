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
package org.ant4eclipse.pde.tools;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;

/**
 * <p>
 * ContainerResolver for resolving the 'org.eclipse.pde.core.requiredPlugins' container.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class RequiredPluginsResolver implements ClasspathContainerResolver {

  public static final String CONTAINER_TYPE_PDE_REQUIRED_PLUGINS = "org.eclipse.pde.core.requiredPlugins";

  /** state */
  private State              _state;

  /**
   * @param targetPlatform
   */
  public RequiredPluginsResolver() {
    //
  }

  /**
   * @see net.sf.ant4eclipse.tools.jdt.container.ClasspathContainerResolver#canResolveContainer(java.lang.String)
   */
  public boolean canResolveContainer(final ClasspathEntry classpathEntry) {
    return false;
  }

  /**
   * @see net.sf.ant4eclipse.tools.jdt.container.ClasspathContainerResolver#initialize()
   */
  public void initialize() {
    // TODO: konfigurierbar machen
    // this._state = this._targetPlatform.resolve(true, false);
  }

  /**
   * @see net.sf.ant4eclipse.tools.jdt.container.ClasspathContainerResolver#isInitialized()
   */
  public boolean isInitialized() {
    return (this._state != null);
  }

  /**
   * @see net.sf.ant4eclipse.tools.jdt.container.ClasspathContainerResolver#reset()
   */
  public void reset() {
    this._state = null;
  }

  /**
   * @see net.sf.ant4eclipse.tools.jdt.container.ClasspathContainerResolver#resolveContainer(net.sf.ant4eclipse.tools.jdt.resolver.container.ClasspathContainerResolver.
   *      ContainerResolverContext)
   */
  public void resolveContainer(final ClasspathEntry classpathEntry, final ClasspathResolverContext context) {
    Assert.notNull(context);

    final PluginProjectRole pluginProjectRole = (PluginProjectRole) context.getCurrentProject().getRole(
        PluginProjectRole.class);
    final BundleDescription bundleDescription = pluginProjectRole.getBundleDescription();

    // get the resolved bundle description...
    final BundleDescription resolvedBundleDescription = this._state.getBundle(bundleDescription.getSymbolicName(),
        bundleDescription.getVersion());

    ClasspathHelper.resolveBundleClasspath(context, resolvedBundleDescription);
  }
}