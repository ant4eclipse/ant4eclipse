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
package org.ant4eclipse.lib.pde.tools;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.internal.tools.BundleDependenciesResolver;
import org.ant4eclipse.lib.pde.internal.tools.TargetPlatformImpl;
import org.ant4eclipse.lib.pde.internal.tools.UnresolvedBundleException;
import org.ant4eclipse.lib.pde.internal.tools.UnresolvedBundlesAnalyzer;
import org.ant4eclipse.lib.pde.internal.tools.BundleDependenciesResolver.BundleDependency;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.eclipse.osgi.service.resolver.BundleDescription;

import java.util.List;

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
  public boolean canResolveContainer(ClasspathEntry classpathEntry) {
    return classpathEntry.getPath().startsWith(CONTAINER_TYPE_PDE_REQUIRED_PLUGINS);
  }

  /**
   * {@inheritDoc}
   */
  public void resolveContainer(ClasspathEntry classpathEntry, ClasspathResolverContext context) {
    Assure.notNull("context", context);

    // Step 1: get the plug-in project role
    EclipseProject eclipseProject = context.getCurrentProject();
    PluginProjectRole pluginProjectRole = eclipseProject.getRole(PluginProjectRole.class);

    // Step 2: get the bundle description
    BundleDescription pluginProjectDescription = pluginProjectRole.getBundleDescription();

    // Step 3: get the target platform
    TargetPlatform targetPlatform = getTargetPlatform(context);

    // Step 5: resolve the bundle dependencies
    BundleDescription resolvedBundleDescription = targetPlatform.getResolvedBundle(pluginProjectDescription
        .getSymbolicName(), pluginProjectDescription.getVersion());

    resolveBundleClassPath(context, resolvedBundleDescription, targetPlatform);

    // // Step 7: if the plug-in project is a fragment, we have to add the host as well
    // if (BundleDependenciesResolver.isFragment(resolvedBundleDescription)) {
    //
    // // Step 7.1: get the host description
    // BundleDescription hostDescription = BundleDependenciesResolver.getHost(resolvedBundleDescription);
    //
    // resolveBundleClassPath(context, hostDescription);
    //
    // // Step 7.2: get the bundle source
    // BundleSource bundleSource = (BundleSource) hostDescription.getUserObject();
    //
    // // Step 7.3: add the bundle source to the context
    // if (bundleSource.isEclipseProject()) {
    // context.addReferencedProjects(bundleSource.getAsEclipseProject());
    // }
    //      
    // // get the layout resolver for the host and add the host's class path entries to the class path
    // BundleLayoutResolver hostLayoutResolver = BundleDependenciesResolver.getBundleLayoutResolver(hostDescription);
    // File[] hostClasspathEntries = hostLayoutResolver.resolveBundleClasspathEntries();
    // context.addClasspathEntry(new ResolvedClasspathEntry(hostClasspathEntries));
    // }
  }

  /**
   * <p>
   * Resolves the bundle class path.
   * </p>
   * 
   * @param context
   * @param resolvedBundleDescription
   * @param targetPlatform
   */
  private void resolveBundleClassPath(ClasspathResolverContext context, BundleDescription resolvedBundleDescription,
      TargetPlatform targetPlatform) {

    // declare the bundle dependencies
    List<BundleDependency> bundleDependencies = null;

    try {
      bundleDependencies = new BundleDependenciesResolver().resolveBundleClasspath(resolvedBundleDescription);
    } catch (UnresolvedBundleException e) {

      // try to find the root cause
      BundleDescription description = new UnresolvedBundlesAnalyzer(targetPlatform).getRootCause(e
          .getBundleDescription());

      // throw a BUNDLE_NOT_RESOLVED_EXCEPTION
      throw new Ant4EclipseException(PdeExceptionCode.BUNDLE_NOT_RESOLVED_EXCEPTION, TargetPlatformImpl
          .dumpResolverErrors(description, true));
    }

    // add all dependencies to the class path
    for (BundleDependency bundleDependency : bundleDependencies) {

      // add the referenced eclipse projects - this information is needed to compute the correct build order
      List<EclipseProject> referencedPluginProjects = bundleDependency.getReferencedPluginProjects();
      for (EclipseProject referencedPluginProject : referencedPluginProjects) {
        context.addReferencedProjects(referencedPluginProject);
      }

      // add the class path entries - these entries are used for the class path
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
  private TargetPlatform getTargetPlatform(ClasspathResolverContext context) {

    // get the TargetPlatform
    TargetPlatformRegistry registry = ServiceRegistryAccess.instance().getService(TargetPlatformRegistry.class);

    // get the container arguments
    JdtClasspathContainerArgument targetPlatformContainerArgument = context
        .getJdtClasspathContainerArgument("targetPlatformId");

    JdtClasspathContainerArgument platformConfigurationIdContainerArgument = context
        .getJdtClasspathContainerArgument("platformConfigurationId");

    // get the platform configuration
    PlatformConfiguration configuration = null;

    if (platformConfigurationIdContainerArgument != null) {
      String platformConfigurationId = platformConfigurationIdContainerArgument.getValue();

      if (Utilities.hasText(platformConfigurationId)) {
        configuration = registry.getPlatformConfiguration(platformConfigurationId);
        if (configuration == null) {
          throw new Ant4EclipseException(PdeExceptionCode.UKNOWN_PLATFORM_CONFIGURATION, platformConfigurationId);
        }
      }
    }

    if (configuration == null) {
      configuration = new PlatformConfiguration();
    }

    if (targetPlatformContainerArgument == null) {

      // get the one and only target platform
      if (registry.getTargetPlatformDefinitionIds().size() == 1) {
        String id = registry.getTargetPlatformDefinitionIds().get(0);
        return registry.getInstance(context.getWorkspace(), id, configuration);
      }

      // throw new Ant4EclipseException
      throw new Ant4EclipseException(PdeExceptionCode.NO_TARGET_PLATFORM_SET);
    } else {

      // get the TargetPlatform
      return registry.getInstance(context.getWorkspace(), targetPlatformContainerArgument.getValue(), configuration);
    }
  }
}