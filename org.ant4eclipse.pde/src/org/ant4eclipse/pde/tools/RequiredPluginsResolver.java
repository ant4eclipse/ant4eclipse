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

import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.ContainerTypes;
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
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

  /**
   * @see net.sf.ant4eclipse.tools.jdt.container.ClasspathContainerResolver#canResolveContainer(java.lang.String)
   */
  public boolean canResolveContainer(final ClasspathEntry classpathEntry) {
    return classpathEntry.getPath().startsWith(CONTAINER_TYPE_PDE_REQUIRED_PLUGINS);
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

    JdtClasspathContainerArgument containerArgument = context.getJdtClasspathContainerArgument("target.platform");

    TargetPlatformRegistry registry = TargetPlatformRegistry.Helper.getRegistry();

    Assert.notNull(registry);
    Assert.notNull(containerArgument);

    TargetPlatform targetPlatform = registry.getInstance(context.getWorkspace(), containerArgument.getValue(),
        new TargetPlatformConfiguration());

    State state = targetPlatform.getState();

    // get the resolved bundle description...
    final BundleDescription resolvedBundleDescription = state.getBundle(bundleDescription.getSymbolicName(),
        bundleDescription.getVersion());

    new BundleClasspathResolver().resolveBundleClasspath(context, resolvedBundleDescription);
  }
}