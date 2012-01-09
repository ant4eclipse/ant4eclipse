package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.lib.pde.tools.TargetPlatform;
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
  // Assure.notNull( "targetPlatform", targetPlatform );
  public UnresolvedBundlesAnalyzer( TargetPlatform targetPlatform ) {
    _targetPlatform = targetPlatform;
  }

  /**
   * <p>
   * </p>
   * 
   * @param bundleDescription
   *          the unresolved bundle
   * @return the root cause of the bundle description
   */
  public BundleDescription getRootCause( BundleDescription bundleDescription ) {
    ResolverError[] errors = bundleDescription.getContainingState().getResolverErrors( bundleDescription );
    for( ResolverError error : errors ) {
      switch( error.getType() ) {
      case ResolverError.MISSING_IMPORT_PACKAGE:
        return resolveMissingImport( bundleDescription, error );
      case ResolverError.MISSING_REQUIRE_BUNDLE:
        return resolveMissingRequiredBundle( bundleDescription, error );
      default:
        return bundleDescription;
      }
    }
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
  private BundleDescription resolveMissingRequiredBundle( BundleDescription bundleDescription, ResolverError error ) {
    VersionConstraint versionConstraint = error.getUnsatisfiedConstraint();
    for( BundleDescription erronousBundleDescription : _targetPlatform.getBundlesWithResolverErrors() ) {
      if( versionConstraint.isSatisfiedBy( erronousBundleDescription ) ) {
        return getRootCause( erronousBundleDescription );
      }
    }
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
  private BundleDescription resolveMissingImport( BundleDescription bundleDescription, ResolverError error ) {
    VersionConstraint versionConstraint = error.getUnsatisfiedConstraint();
    for( BundleDescription erronousBundleDescription : _targetPlatform.getBundlesWithResolverErrors() ) {
      for( ExportPackageDescription exportPackageDescription : erronousBundleDescription.getExportPackages() ) {
        if( versionConstraint.isSatisfiedBy( exportPackageDescription ) ) {
          return getRootCause( erronousBundleDescription );
        }
      }
    }
    return bundleDescription;
  }
  
} /* ENDCLASS */
