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

import java.io.File;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.pde.PdeExceptionCode;
import org.ant4eclipse.pde.internal.tools.BundleDependenciesResolver;
import org.ant4eclipse.pde.internal.tools.BundleDependenciesResolver.BundleDependency;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;

/**
 * <p>
 * {@link ClasspathContainerResolver} for resolving the 'org.eclipse.pde.core.requiredPlugins' container.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class RequiredPluginsResolver implements ClasspathContainerResolver {

  /** the constant for the container type 'org.eclipse.pde.core.requiredPlugins' */
  public static final String CONTAINER_TYPE_PDE_REQUIRED_PLUGINS = "org.eclipse.pde.core.requiredPlugins";

  /**
   * {@inheritDoc}
   */
  public boolean canResolveContainer(final ClasspathEntry classpathEntry) {
    return classpathEntry.getPath().startsWith(CONTAINER_TYPE_PDE_REQUIRED_PLUGINS);
  }

  /**
   * {@inheritDoc}
   */
  public void resolveContainer(final ClasspathEntry classpathEntry, final ClasspathResolverContext context) {
    Assert.notNull(context);

    // Step 1: get the plug-in project role
    final EclipseProject eclipseProject = context.getCurrentProject();
    final PluginProjectRole pluginProjectRole = (PluginProjectRole) eclipseProject.getRole(PluginProjectRole.class);

    // Step 2: get the bundle description
    final BundleDescription pluginProjectDescription = pluginProjectRole.getBundleDescription();

    // Step 3: get the target platform
    TargetPlatform targetPlatform = getTargetPlatform(context);

    // Step 5: resolve the bundle dependencies
    final BundleDescription resolvedBundleDescription = targetPlatform.getResolvedBundle(pluginProjectDescription.getSymbolicName(),
        pluginProjectDescription.getVersion());

    resolveBundleClassPath(context, resolvedBundleDescription);

//    // Step 7: if the plug-in project is a fragment, we have to add the host as well
//    if (BundleDependenciesResolver.isFragment(resolvedBundleDescription)) {
//
//      // Step 7.1: get the host description
//      BundleDescription hostDescription = BundleDependenciesResolver.getHost(resolvedBundleDescription);
//
//      resolveBundleClassPath(context, hostDescription);
//
//      // Step 7.2: get the bundle source
//      final BundleSource bundleSource = (BundleSource) hostDescription.getUserObject();
//
//      // Step 7.3: add the bundle source to the context
//      if (bundleSource.isEclipseProject()) {
//        context.addReferencedProjects(bundleSource.getAsEclipseProject());
//      }
//      
//      // get the layout resolver for the host and add the host's class path entries to the class path
//      BundleLayoutResolver hostLayoutResolver = BundleDependenciesResolver.getBundleLayoutResolver(hostDescription);
//      File[] hostClasspathEntries = hostLayoutResolver.resolveBundleClasspathEntries();
//      context.addClasspathEntry(new ResolvedClasspathEntry(hostClasspathEntries));
//    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param context
   * @param resolvedBundleDescription
   */
  private void resolveBundleClassPath(final ClasspathResolverContext context,
      final BundleDescription resolvedBundleDescription) {
    List<BundleDependency> bundleDependencies = new BundleDependenciesResolver()
        .resolveBundleClasspath(resolvedBundleDescription);

    // Step 6: add all dependencies to the class path
    for (BundleDependency bundleDependency : bundleDependencies) {

      // Step 6.1: add the referenced eclipse projects - this information is needed to compute the correct build order
      List<EclipseProject> referencedPluginProjects = bundleDependency.getReferencedPluginProjects();
      for (EclipseProject referencedPluginProject : referencedPluginProjects) {
        context.addReferencedProjects(referencedPluginProject);
      }

      // Step 6.2: add the class path entries - these entries are used for the class path
      context.addClasspathEntry(bundleDependency.getResolvedClasspathEntry());
    }
  }

  /**
   * <p>
   * Returns the target platform.
   * </p>
   * 
   * @param context
   *          the context
   * @return the target platform.
   */
  private TargetPlatform getTargetPlatform(final ClasspathResolverContext context) {

    // Step 4.1: get the TargetPlatform
    TargetPlatformRegistry registry = TargetPlatformRegistry.Helper.getRegistry();
    
    // Step 1: get the target platform argument
    // TODO: get the target.platform argument
    JdtClasspathContainerArgument containerArgument = context.getJdtClasspathContainerArgument("target.platform");

    if (containerArgument == null) {
      throw new Ant4EclipseException(PdeExceptionCode.NO_TARGET_PLATFORM_SET);
    }

    // Step 4.2: get the TargetPlatform
    TargetPlatform targetPlatform = registry.getInstance(context.getWorkspace(), containerArgument.getValue(),
        new TargetPlatformConfiguration());
    return targetPlatform;
  }
}