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
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.pde.internal.tools.BundleDependenciesResolver;
import org.ant4eclipse.pde.internal.tools.BundleDependenciesResolver.BundleDependency;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.model.resource.EclipseProject;
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

  /** the constant for the container type 'org.eclipse.pde.core.requiredPlugins' */
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

    // get the PluginProjectRole
    final PluginProjectRole pluginProjectRole = (PluginProjectRole) context.getCurrentProject().getRole(
        PluginProjectRole.class);

    // get the BundleDescription
    final BundleDescription bundleDescription = pluginProjectRole.getBundleDescription();

    // get the target platform argument
    // TODO
    JdtClasspathContainerArgument containerArgument = context.getJdtClasspathContainerArgument("target.platform");

    // get the TargetPlatformRegistry
    TargetPlatformRegistry registry = TargetPlatformRegistry.Helper.getRegistry();

    // TODO!! ERROR MESSAGE
    Assert.notNull(registry);
    Assert.notNull(containerArgument);

    // get the TargetPlatform
    TargetPlatform targetPlatform = registry.getInstance(context.getWorkspace(), containerArgument.getValue(),
        new TargetPlatformConfiguration());

    // get the state
    State state = targetPlatform.getState();

    // get the resolved bundle description...
    final BundleDescription resolvedBundleDescription = state.getBundle(bundleDescription.getSymbolicName(),
        bundleDescription.getVersion());

    // resolve the bundle
    List<BundleDependency> bundleDependencies = new BundleDependenciesResolver()
        .resolveBundleClasspath(resolvedBundleDescription);

    // add all ResolvedClasspathEntries to the class path
    for (BundleDependency bundleDependency : bundleDependencies) {

      List<EclipseProject> referencedPluginProjects = bundleDependency.getReferencedPluginProjects();
      for (EclipseProject referencedPluginProject : referencedPluginProjects) {
        context.addReferencedProjects(referencedPluginProject);
      }

      context.addClasspathEntry(bundleDependency.getResolvedClasspathEntry());
    }
  }
}