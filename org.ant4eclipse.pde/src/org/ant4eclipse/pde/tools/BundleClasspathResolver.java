package org.ant4eclipse.pde.tools;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.osgi.BundleLayoutResolver;
import org.ant4eclipse.pde.osgi.ExplodedBundleLayoutResolver;
import org.ant4eclipse.pde.osgi.JaredBundleLayoutResolver;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;

/**
 * Tools for resolving bundle classpathes
 *
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class BundleClasspathResolver {

  /** */
  private List<BundleDescription> _resolvedBundles;

  /**
   * <p>
   * </p>
   */
  public BundleClasspathResolver() {
    _resolvedBundles = new LinkedList<BundleDescription>();
  }

  /**
   * <p>
   * </p>
   *
   * @param context
   * @param resolvedBundleDescription
   */
  public void resolveBundleClasspath(final ClasspathResolverContext context, final BundleDescription bundleDescription) {

    Assert.notNull(context);
    Assert.notNull(bundleDescription);

    if (!bundleDescription.isResolved()) {
      throw new RuntimeException("bundle not resolved");
    }

    // add all packages that are imported...
    for (ExportPackageDescription exportPackageDescription : bundleDescription.getResolvedImports()) {
      // TODO: Access Restrictions
      addBundleToClasspath(context, exportPackageDescription.getSupplier());
    }

    // add all packages that come from required bundles...
    for (BundleDescription requiredBundle : bundleDescription.getResolvedRequires()) {

      // TODO: Access Restrictions
      addBundleToClasspath(context, requiredBundle);

      // add all reexported bundles also...
      for (BundleDescription reexportedBundle : getReexportedBundles(requiredBundle)) {

        // TODO: Access Restrictions
        addBundleToClasspath(context, reexportedBundle);
      }
    }

    // add the bundle itself to the classpath
    addBundleToClasspath(context, bundleDescription);
  }

  private BundleDescription[] getReexportedBundles(final BundleDescription bundleDescription) {
    Assert.notNull(bundleDescription);
    Assert.assertTrue(bundleDescription.isResolved(), "Bundle must be resolved!");

    final List<BundleDescription> result = new LinkedList<BundleDescription>();

    final BundleSpecification[] requiredBundles = bundleDescription.getRequiredBundles();
    for (int i = 0; i < requiredBundles.length; i++) {
      final BundleSpecification specification = requiredBundles[i];
      if (specification.isExported()) {
        final BundleDescription reexportedBundle = (BundleDescription) specification.getSupplier();
        if (!result.contains(reexportedBundle)) {
          result.add(reexportedBundle);
        }
        final BundleDescription reexportedBundles[] = getReexportedBundles(reexportedBundle);
        for (int j = 0; j < reexportedBundles.length; j++) {
          final BundleDescription description = reexportedBundles[j];
          if (!result.contains(description)) {
            result.add(description);
          }
        }
      }
    }

    final BundleDescription[] fragments = bundleDescription.getFragments();
    for (int i = 0; i < fragments.length; i++) {
      final BundleDescription fragment = fragments[i];
      if (!result.contains(fragment)) {
        result.add(fragment);
      }
      final BundleDescription reexportedBundles[] = getReexportedBundles(fragment);
      for (int j = 0; j < reexportedBundles.length; j++) {
        final BundleDescription reexportedBundle = reexportedBundles[j];
        if (!result.contains(reexportedBundle)) {
          result.add(reexportedBundle);
        }
      }
    }

    return (BundleDescription[]) result.toArray(new BundleDescription[result.size()]);
  }

  /**
   * @param context
   * @param bundleDescription
   */
  private void addBundleToClasspath(final ClasspathResolverContext context, final BundleDescription bundleDescription) {

    if (_resolvedBundles.contains(bundleDescription)) {
      return;
    }

    _resolvedBundles.add(bundleDescription);

    // handle fragments
    BundleDescription host = null;
    BundleDescription[] fragments = null;

    // bundle description describes a fragment ->
    // choose host as main bundle description and add fragments
    if (bundleDescription.getHost() != null) {
      host = bundleDescription.getHost().getHosts()[0];
      fragments = host.getFragments();
    }
    // bundle description describes a host ->
    // just add fragments
    else {
      host = bundleDescription;
      fragments = bundleDescription.getFragments();
    }

    addBundleToClasspath(context, host, fragments);

    // TODO!!
    // JarUtilities.expandBundle(bundleDescription);
    // final BundleSource bundleSource = BundleSource.getBundleSource(bundleDescription);
    // if (bundleSource.isEclipseProject()) {
    // final EclipseProject project = bundleSource.getAsEclipseProject();
    // if (JavaProjectRole.Helper.hasJavaProjectRole(project)) {
    // context.resolveProjectClasspath(project);
    // }
    // } else {
    // final String[] classpathEntries = bundleSource.getBundleClasspath();
    // for (int j = 0; j < classpathEntries.length; j++) {
    // final String entryName = classpathEntries[j];
    // File entry;
    // if (".".equals(entryName)) {
    // entry = bundleSource.getClasspathRoot();
    // } else {
    // entry = new File(bundleSource.getClasspathRoot(), entryName);
    // }
    // System.err.println(entry);
    // if (entry.exists()) {
    // // TODO: ACCESS RESTRICTIONS
    // context.addClasspathEntry(new ResolvedClasspathEntry(entry));
    // } else {
    // A4ELogging.debug("Not adding non-existant entry '%s'", entry);
    // }
    // }
    // }
  }

  /**
   * @param host
   * @param fragments
   * @param filtered
   * @return
   */
  private void addBundleToClasspath(final ClasspathResolverContext context, final BundleDescription host,
      final BundleDescription[] fragments) {

    BundleLayoutResolver layoutResolver = getBundleLayoutResolver(host);

    context.addClasspathEntry(new ResolvedClasspathEntry(layoutResolver.resolveBundleClasspathEntries()));

    for (BundleDescription fragment : fragments) {
      layoutResolver = getBundleLayoutResolver(fragment);
      context.addClasspathEntry(new ResolvedClasspathEntry(layoutResolver.resolveBundleClasspathEntries()));
    }
  }

  /**
   * <p>
   * Returns a {@link BundleLayoutResolver} for the given {@link BundleDescription}.
   * </p>
   *
   * @param bundleDescription
   *          the given {@link BundleDescription}.
   * @return a {@link BundleLayoutResolver} for the given {@link BundleDescription}.
   */
  private BundleLayoutResolver getBundleLayoutResolver(final BundleDescription bundleDescription) {

    // get the bundle source
    final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    final File location = getLocation(bundleDescription);

    // eclipse project -> PluginProjectLayoutResolver
    if (bundleSource.isEclipseProject()) {
      return new PluginProjectLayoutResolver(bundleSource.getAsEclipseProject());
    }
    // directory -> ExplodedBundleLayoutResolver
    else if (location.isDirectory()) {
      return new ExplodedBundleLayoutResolver(location);
    }
    // jar -> JaredBundleLayoutResolver
    else {
      // TODO: EXPANSION DIR!!
      return new JaredBundleLayoutResolver(location, new File("c:\\temp"));
    }
  }

  /**
   * @param bundleDescription
   * @return
   */
  private File getLocation(final BundleDescription bundleDescription) {

    // get the bundle source
    final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    final File result = bundleSource.isEclipseProject() ? bundleSource.getAsEclipseProject().getFolder() : bundleSource
        .getAsFile();

    // return result
    return result;
  }
}
