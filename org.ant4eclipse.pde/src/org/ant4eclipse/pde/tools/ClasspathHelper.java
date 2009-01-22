/* net.sf.ant4eclipse.tools.pde.ClasspathHelper, created 10.11.2006 */
package org.ant4eclipse.pde.tools;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.HostSpecification;
import org.osgi.framework.Version;

/**
 * Tools for resolving bundle classpathes
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ClasspathHelper {

  /**
   * Returns all bundles that are conntected to the given bundleDescription by "require-bundle" headers. This works
   * recursivley for "reexport"ed bundles
   * <p>
   * The bundleDescription must be resolved
   * </p>
   */
  public static BundleDescription[] getAllRequiredBundles(final BundleDescription bundleDescription) {
    Assert.notNull(bundleDescription);
    Assert.assertTrue(bundleDescription.isResolved(), "Bundle must be resolved!");

    final List result = new LinkedList();

    final BundleDescription[] resolvedRequires = bundleDescription.getResolvedRequires();
    for (int i = 0; i < resolvedRequires.length; i++) {
      final BundleDescription description = resolvedRequires[i];
      if (!result.contains(description)) {
        result.add(description);
      }

      final BundleDescription reexportedBundles[] = getReexportedBundles(description);
      for (int j = 0; j < reexportedBundles.length; j++) {
        final BundleDescription reexportedBundle = reexportedBundles[j];
        if (!result.contains(reexportedBundle)) {
          result.add(reexportedBundle);
        }
      }
    }

    final BundleDescription[] fragments = bundleDescription.getFragments();
    // System.err.println("FRAGMENTS: " + Arrays.asList(fragments));
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

  private static BundleDescription[] getReexportedBundles(final BundleDescription bundleDescription) {
    Assert.notNull(bundleDescription);
    Assert.assertTrue(bundleDescription.isResolved(), "Bundle must be resolved!");

    final List result = new LinkedList();

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

    }

    return (BundleDescription[]) result.toArray(new BundleDescription[result.size()]);
  }

  public static void resolveBundleClasspath(final ClasspathResolverContext context,
      final BundleDescription resolvedBundleDescription) {
    resolveBundleClasspath(context, resolvedBundleDescription, false);
  }

  public static void resolveBundleClasspath(final ClasspathResolverContext context,
      final BundleDescription resolvedBundleDescription, final boolean addSelf) {

    Assert.notNull(context);
    Assert.notNull(resolvedBundleDescription);

    if (!resolvedBundleDescription.isResolved()) {
      throw new RuntimeException("bundle not resolved");
    }

    final List bundlesAdded = new LinkedList();

    if (addSelf) {
      // add the bundle itself to the classpath
      addBundleToClasspath(context, resolvedBundleDescription);
    }
    final ExportPackageDescription[] resolvedImports = resolvedBundleDescription.getResolvedImports();

    for (int i = 0; i < resolvedImports.length; i++) {
      final ExportPackageDescription description = resolvedImports[i];
      final BundleDescription importerDescription = description.getSupplier();
      if (!bundlesAdded.contains(importerDescription)) {
        addBundleToClasspath(context, importerDescription);
        bundlesAdded.add(importerDescription);
      }
    }

    final BundleDescription[] resolvedRequires = resolvedBundleDescription.getResolvedRequires();
    for (int i = 0; i < resolvedRequires.length; i++) {
      final BundleDescription requiredDescription = resolvedRequires[i];
      if (!bundlesAdded.contains(requiredDescription)) {
        addBundleToClasspath(context, requiredDescription);
        bundlesAdded.add(requiredDescription);
      }

      final BundleDescription[] reexportedBundles = getReexportedBundles(requiredDescription);
      for (int j = 0; j < reexportedBundles.length; j++) {
        final BundleDescription reexportedBundle = reexportedBundles[j];
        if (!bundlesAdded.contains(reexportedBundle)) {
          addBundleToClasspath(context, reexportedBundle);
          bundlesAdded.add(reexportedBundle);
        }
      }

    }
    //
    final BundleDescription[] fragments = resolvedBundleDescription.getFragments();
    for (int i = 0; i < fragments.length; i++) {
      final BundleDescription fragmentDescription = fragments[i];
      if (!bundlesAdded.contains(fragmentDescription)) {
        addBundleToClasspath(context, fragmentDescription);
        bundlesAdded.add(fragmentDescription);
      }
    }

  }

  private static void addBundleToClasspath(final ClasspathResolverContext context,
      final BundleDescription bundleDescription) {

    // TODO!!
    // JarUtilities.expandBundle(bundleDescription);
    final BundleSource bundleSource = BundleSource.getBundleSource(bundleDescription);
    if (bundleSource.isEclipseProject()) {
      final EclipseProject project = bundleSource.getAsEclipseProject();
      if (JavaProjectRole.Helper.hasJavaProjectRole(project)) {
        context.resolveProjectClasspath(project);
      }
    } else {
      final String[] classpathEntries = bundleSource.getBundleClasspath();
      for (int j = 0; j < classpathEntries.length; j++) {
        final String entryName = classpathEntries[j];
        File entry;
        if (".".equals(entryName)) {
          entry = bundleSource.getClasspathRoot();
        } else {
          entry = new File(bundleSource.getClasspathRoot(), entryName);
        }
        if (entry.exists()) {
          context.addClasspathEntry(entry);
        } else {
          A4ELogging.debug("Not adding non-existant entry '%s'", entry);
        }
      }
    }
  }

  /**
   * Returns for each package of the host an ExportPackageDescription that points to the <b>Fragment</b>
   * 
   * @param host
   * @return
   */
  public static ExportPackageDescription[] getPackagesFromHost(final HostSpecification host) {
    Assert.notNull(host);
    Assert.assertTrue(host.isResolved(), "Host must be resolved!");

    final BundleDescription fragmentBundle = host.getBundle();
    final BundleDescription hostBundle = (BundleDescription) host.getSupplier();

    final List result = new LinkedList();

    final ExportPackageDescription[] exportPackages = hostBundle.getExportPackages();
    for (int i = 0; i < exportPackages.length; i++) {
      final ExportPackageDescription description = exportPackages[i];
      // make sure, fragment doesn't have an appropriate export-package yet
      if (!hasExportPackage(fragmentBundle, description)) {
        result.add(new ExportPackageDescription() {

          public Version getVersion() {
            return null;
          }

          public BundleDescription getSupplier() {
            return fragmentBundle;
          }

          public String getName() {
            return description.getName();
          }

          public boolean isRoot() {
            return false;
          }

          public BundleDescription getExporter() {
            return fragmentBundle;
          }

          public Map getDirectives() {
            return new HashMap();
          }

          public Object getDirective(final String key) {
            return null;
          }

          public Map getAttributes() {
            return new HashMap();
          }

        });
      }
    }
    return (ExportPackageDescription[]) result.toArray(new ExportPackageDescription[result.size()]);
  }

  protected static boolean hasExportPackage(final BundleDescription description,
      final ExportPackageDescription exportPackageDescription) {
    final ExportPackageDescription[] exportPackages = description.getExportPackages();
    for (int i = 0; i < exportPackages.length; i++) {
      final ExportPackageDescription description2 = exportPackages[i];
      if (exportPackageDescription.getName().equals(description2.getName())
          && exportPackageDescription.getVersion().equals(description2.getVersion())) {
        return true;
      }
    }
    return false;
  }
}
