package org.ant4eclipse.pde.internal.tools;

import org.ant4eclipse.pde.tools.TargetPlatform;

import org.ant4eclipse.lib.core.Assure;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.ResolverError;
import org.eclipse.osgi.service.resolver.VersionConstraint;

/**
 * <p>
 * Helper class that analyzes an unresolved bundle and tries to find the root cause for the unresolved bundle.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class UnresolvedBundlesAnalyzer {

  /** the target platform */
  private TargetPlatform _targetPlatform;

  /**
   * <p>
   * Creates a new instance of type {@link UnresolvedBundlesAnalyzer}.
   * </p>
   * 
   * @param targetPlatform
   *          the target platform
   */
  public UnresolvedBundlesAnalyzer(TargetPlatform targetPlatform) {
    Assure.notNull(targetPlatform);

    // set the target platform
    this._targetPlatform = targetPlatform;
  }

  /**
   * <p>
   * </p>
   * 
   * @param bundleDescription
   *          the unresolved bundle
   * @return the root cause of the bundle description
   */
  public BundleDescription getRootCause(BundleDescription bundleDescription) {

    // get the resolver errors
    ResolverError[] errors = bundleDescription.getContainingState().getResolverErrors(bundleDescription);

    // iterate over all the errors
    for (ResolverError error : errors) {
      switch (error.getType()) {
      case ResolverError.MISSING_IMPORT_PACKAGE:
        return resolveMissingImport(bundleDescription, error);
      case ResolverError.MISSING_REQUIRE_BUNDLE:
        return resolveMissingRequiredBundle(bundleDescription, error);
      default:
        return bundleDescription;
      }
    }

    // return the 'original' bundle description
    return bundleDescription;
  }

  /**
   * <p>
   * Tries to find the root cause for a missing required bundle.
   * </p>
   * 
   * @param bundleDescription
   *          the bundle description
   * @param error
   *          the error
   * @return the root cause for a missing required bundle.
   */
  private BundleDescription resolveMissingRequiredBundle(BundleDescription bundleDescription, ResolverError error) {

    // get the constraint
    VersionConstraint versionConstraint = error.getUnsatisfiedConstraint();

    // iterate over all bundles with errors
    for (BundleDescription erronousBundleDescription : this._targetPlatform.getBundlesWithResolverErrors()) {
      if (versionConstraint.isSatisfiedBy(erronousBundleDescription)) {
        return getRootCause(erronousBundleDescription);
      }
    }

    // return the 'original' bundle description
    return bundleDescription;
  }

  /**
   * <p>
   * Tries to find the root cause for a missing import.
   * </p>
   * 
   * @param bundleDescription
   *          the bundle description
   * @param error
   *          the error
   * @return the root cause for a missing required bundle.
   */
  private BundleDescription resolveMissingImport(BundleDescription bundleDescription, ResolverError error) {

    // get the constraint
    VersionConstraint versionConstraint = error.getUnsatisfiedConstraint();

    // iterate over all bundles with errors
    for (BundleDescription erronousBundleDescription : this._targetPlatform.getBundlesWithResolverErrors()) {
      for (ExportPackageDescription exportPackageDescription : erronousBundleDescription.getExportPackages()) {
        if (versionConstraint.isSatisfiedBy(exportPackageDescription)) {
          return getRootCause(erronousBundleDescription);
        }
      }
    }

    // return the 'original' bundle description
    return bundleDescription;
  }
}
