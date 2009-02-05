package org.ant4eclipse.pde.tools;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.osgi.BundleLayoutResolver;
import org.ant4eclipse.pde.osgi.ExplodedBundleLayoutResolver;
import org.ant4eclipse.pde.osgi.JaredBundleLayoutResolver;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;

/**
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class BundleClasspathResolver {

  /** */
  private Map<BundleDescription, BundleDependency> _resolvedBundles;

  /**
   * <p>
   * </p>
   */
  public BundleClasspathResolver() {
    _resolvedBundles = new HashMap<BundleDescription, BundleDependency>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param context
   * @param resolvedBundleDescription
   */
  public List<BundleDependency> resolveBundleClasspath(final BundleDescription bundleDescription) {

    Assert.notNull(bundleDescription);

    if (!bundleDescription.isResolved()) {
      throw new RuntimeException("bundle not resolved");
    }

    // Step 1: add all packages that are imported...
    for (ExportPackageDescription exportPackageDescription : bundleDescription.getResolvedImports()) {

      addExportedPackage(exportPackageDescription);
    }

    // Step 2: add all packages that come from required bundles...
    for (BundleDescription requiredBundle : bundleDescription.getResolvedRequires()) {

      addAllExportedPackages(requiredBundle);

      // add all re-exported bundles also...
      for (BundleDescription reexportedBundle : getReexportedBundles(requiredBundle)) {

        addAllExportedPackages(reexportedBundle);
      }
    }

    // Step 3: add the bundle itself to the class path
    // addBundleToClasspath(context, bundleDescription);

    return null;
  }

  private BundleDescription[] getReexportedBundles(final BundleDescription bundleDescription) {
    Assert.notNull(bundleDescription);
    Assert.assertTrue(bundleDescription.isResolved(), "Bundle must be resolved!");

    // define the result set
    final Set<BundleDescription> resultSet = new LinkedHashSet<BundleDescription>();

    // iterate over all required bundles
    for (BundleSpecification specification : bundleDescription.getRequiredBundles()) {

      // only add exported entries
      if (specification.isExported()) {

        // get the bundle description
        final BundleDescription reexportedBundle = (BundleDescription) specification.getSupplier();

        // add the bundle description
        resultSet.add(reexportedBundle);

        // add all re-exported bundle description recursively
        for (BundleDescription rereexportedBundle : getReexportedBundles(reexportedBundle)) {
          resultSet.add(rereexportedBundle);
        }
      }
    }

    // iterate over all attached fragments
    for (BundleDescription fragment : bundleDescription.getFragments()) {

      // add the fragment
      // TODO should we really add the fragment here ?
      // -> fragments are added 'automatically' in BundleDependency.getResolvedClasspathEntry()
      resultSet.add(fragment);

      // add all re-exported bundles
      for (BundleDescription reexportedBundle : getReexportedBundles(fragment)) {
        resultSet.add(reexportedBundle);
      }
    }

    // return the result
    return (BundleDescription[]) resultSet.toArray(new BundleDescription[resultSet.size()]);
  }

  /**
   * @param context
   * @param bundleDescription
   */
  private void addAllExportedPackages(final BundleDescription bundleDescription) {

    System.err.println(bundleDescription);
    
    BundleDescription host = getHost(bundleDescription);

    if (_resolvedBundles.containsKey(host)) {
      return;
    }

    //_resolvedBundles.add(bundleDescription);
    
    // bundleDescription.getExportPackages()

    // BundleLayoutResolver layoutResolver = getBundleLayoutResolver(host);
    //
    // // context.addClasspathEntry(new ResolvedClasspathEntry(layoutResolver.resolveBundleClasspathEntries()));
    //
    // for (BundleDescription fragment : fragments) {
    // layoutResolver = getBundleLayoutResolver(fragment);
    // // context.addClasspathEntry(new ResolvedClasspathEntry(layoutResolver.resolveBundleClasspathEntries()));
    // }
  }

  private void addExportedPackage(ExportPackageDescription exportPackageDescription) {
    // TODO Auto-generated method stub
    
    System.err.println(exportPackageDescription);
  }

  /**
   * <p>
   * Returns the host for the given {@link BundleDescription}.
   * </p>
   * 
   * @param bundleDescription
   * @return
   */
  private BundleDescription getHost(BundleDescription bundleDescription) {

    // bundle description describes a fragment ->
    // choose host as main bundle description
    if (bundleDescription.getHost() != null) {
      return bundleDescription.getHost().getHosts()[0];
    }
    // bundle description describes a host ->
    // return bundle description
    else {
      return bundleDescription;
    }
  }

  /**
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class BundleDependency {

    /** the host */
    private BundleDescription _host;

    /** list of all imported packages */
    private Set<String>       _importedPackages;

    /** list of all packages that are imported through 'RequireBundle' */
    private Set<String>       _packagesImportedThroughRequireBundle;

    /**
     * 
     */
    public BundleDependency() {

      _importedPackages = new LinkedHashSet<String>();
      _packagesImportedThroughRequireBundle = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Returns the {@link ResolvedClasspathEntry}.
     * </p>
     * 
     * @return
     */
    public ResolvedClasspathEntry getResolvedClasspathEntry() {

      List<File> files = new LinkedList<File>();
      BundleLayoutResolver layoutResolver = getBundleLayoutResolver(_host);
      files.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));

      for (BundleDescription fragment : _host.getFragments()) {
        layoutResolver = getBundleLayoutResolver(fragment);
        files.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));
      }

      // TODO
      AccessRestrictions accessRestrictions = new AccessRestrictions();

      ResolvedClasspathEntry resolvedClasspathEntry = new ResolvedClasspathEntry(files.toArray(new File[0]),
          accessRestrictions);
      return resolvedClasspathEntry;
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
     * <p>
     * Returns the location for the given {@link BundleDescription}.
     * </p>
     * 
     * @param bundleDescription
     *          the {@link BundleDescription}
     * @return the location for the given {@link BundleDescription}.
     */
    private File getLocation(final BundleDescription bundleDescription) {

      // get the bundle source
      final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

      // get the location
      final File result = bundleSource.isEclipseProject() ? bundleSource.getAsEclipseProject().getFolder()
          : bundleSource.getAsFile();

      // return result
      return result;
    }
  }
}
