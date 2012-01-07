package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.lib.core.Assure;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * An {@link UnresolvedBundleException} will be thrown if ant4eclipse tries to resolve a bundle that is not in the
 * RESOLVED state.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class UnresolvedBundleException extends Exception {

  /** serialVersionUID */
  private static final long serialVersionUID = -808588529247400291L;

  /** the bundle description of the unresolved bundle */
  private BundleDescription _bundleDescription;

  /**
   * <p>
   * Creates a new instance of type {@link UnresolvedBundleException}.
   * </p>
   * 
   * @param bundleDescription
   *          the bundle description
   */
  public UnresolvedBundleException( BundleDescription bundleDescription ) {
    Assure.notNull( "bundleDescription", bundleDescription );

    // the bundle description
    _bundleDescription = bundleDescription;
  }

  /**
   * <p>
   * Returns the bundle description.
   * </p>
   * 
   * @return the bundleDescription
   */
  public BundleDescription getBundleDescription() {
    return _bundleDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage() {
    return String.format( "Bundle '%s' is not resolved. Reason:\n%s",
        TargetPlatformImpl.getBundleInfo( _bundleDescription ),
        TargetPlatformImpl.dumpResolverErrors( _bundleDescription, false ) );
  }

} /* ENDCLASS */
