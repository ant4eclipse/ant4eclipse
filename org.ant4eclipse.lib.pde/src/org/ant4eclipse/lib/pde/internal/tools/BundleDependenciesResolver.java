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
package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.lib.core.osgi.ExplodedBundleLayoutResolver;
import org.ant4eclipse.lib.core.osgi.JaredBundleLayoutResolver;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.tools.PluginProjectLayoutResolver;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.eclipse.osgi.internal.resolver.StateHelperImpl;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * The {@link BundleDependenciesResolver} is a helper class that can be used to resolve the dependencies of a given
 * bundle.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class BundleDependenciesResolver {

  public List<BundleDependency> resolveBundleClasspath( BundleDescription description ) throws UnresolvedBundleException {
    return resolveBundleClasspath( description, null, null );
  }

  /**
   * <p>
   * </p>
   * 
   * @param description
   *          the bundle description
   * @param includeOptionalDependencies
   * @return
   * @throws UnresolvedBundleException
   */
  public List<BundleDependency> resolveBundleClasspath( final BundleDescription description, TargetPlatform targetPlatform, List<String> additionalBundles ) throws UnresolvedBundleException {

    Assure.notNull( "description", description );

    // step 1: throw exception if bundle description is not resolved
    if( !description.isResolved() ) {
      throw new UnresolvedBundleException( description );
    }

    // step 2: if the bundle is a fragment - get the host
    BundleDescription rootDescription = isFragment( description ) ? getHost( description ) : description;

    // step 3: get visible packages that are exported by other bundles
    List<ExportPackageDescription> allPackageDescriptions = new ArrayList<ExportPackageDescription>();

    // step 4: add the host visible packages
    allPackageDescriptions
        .addAll( Arrays.asList( StateHelperImpl.getInstance().getVisiblePackages( rootDescription ) ) );

    // step 5: add the fragment visible packages
    for( BundleDescription fragmentDescription : rootDescription.getFragments() ) {
      allPackageDescriptions.addAll( Arrays.asList( StateHelperImpl.getInstance().getVisiblePackages(
          fragmentDescription ) ) );
    }

    // step 6: Get exported packages from 'additional bundles'
    if( additionalBundles != null ) {
      allPackageDescriptions.addAll( addAdditionalPackages( targetPlatform, additionalBundles ) );
    }

    // step 7: create the result
    Map<BundleDescription,BundleDependency> map = new HashMap<BundleDescription,BundleDependency>();
    List<BundleDependency> result = new ArrayList<BundleDependency>();

    for( ExportPackageDescription exportPackageDescription : allPackageDescriptions ) {

      //
      BundleDescription bundleDescription = exportPackageDescription.getSupplier();
      if( isFragment( bundleDescription ) ) {
        bundleDescription = getHost( bundleDescription );
      }

      BundleDependency bundleDependency;
      if( map.containsKey( bundleDescription ) ) {
        bundleDependency = map.get( bundleDescription );
      } else {
        bundleDependency = new BundleDependency( bundleDescription );
        map.put( bundleDescription, bundleDependency );
        result.add( bundleDependency );
      }

      //
      bundleDependency.addExportedPackage( exportPackageDescription.getName() );
    }

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param packageDescriptions
   * @param targetPlatform
   * @param additionalBundles
   * @return
   */
  private List<ExportPackageDescription> addAdditionalPackages( TargetPlatform targetPlatform, List<String> additionalBundles ) {
    List<ExportPackageDescription> result = new ArrayList<ExportPackageDescription>();
    for( String additionalBundle : additionalBundles ) {
      A4ELogging.debug( "Adding additional bundle '%s'", additionalBundle );
      BundleDescription resolvedBundle = targetPlatform.getResolvedBundle( additionalBundle, null );
      addAdditionalPackages( result, targetPlatform, resolvedBundle );
    }
    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param exportedPackages
   * @param targetPlatform
   * @param resolvedBundle
   */
  private void addAdditionalPackages( List<ExportPackageDescription> exportedPackages, TargetPlatform targetPlatform,
      BundleDescription resolvedBundle ) {

    // Add exported package from resolvedBundle
    A4ELogging.debug( "Adding packages from '%s' to classpath", resolvedBundle );
    ExportPackageDescription[] exportPackages = resolvedBundle.getExportPackages();
    for( ExportPackageDescription exportPackageDescription : exportPackages ) {
      if( !exportedPackages.contains( exportPackageDescription ) ) {
        A4ELogging.debug( "Add additional exported package %s", exportPackageDescription );
        exportedPackages.add( exportPackageDescription );
      }
    }

    // Add packages from re-exported required bundle
    BundleSpecification[] requiredBundles = resolvedBundle.getRequiredBundles();
    for( BundleSpecification bundleSpecification : requiredBundles ) {
      if( bundleSpecification.isExported() ) {
        A4ELogging.debug( "Add re-exported bundle %s", bundleSpecification );
        addAdditionalPackages( exportedPackages, targetPlatform, bundleSpecification.getSupplier().getSupplier() );
      }
    }
  }

  /**
   * <p>
   * Returns <code>true</code> if the given <code>bundleDescription</code> is a fragment.
   * </p>
   * 
   * @param bundleDescription
   *          the {@link BundleDescription}
   * @return <code>true</code> if the given <code>bundleDescription</code> is a fragment.
   */
  public static boolean isFragment( BundleDescription bundleDescription ) {
    // bundle description describes a fragment if bundleDescription.getHost() != null
    return bundleDescription.getHost() != null;
  }

  /**
   * <p>
   * Returns the host for the given {@link BundleDescription}.
   * </p>
   * 
   * @param bundleDescription
   *          the {@link BundleDescription}
   * @return the host for the given {@link BundleDescription}.
   */
  public static BundleDescription getHost( BundleDescription bundleDescription ) {

    //
    if( !bundleDescription.isResolved() ) {
      throw new RuntimeException( String.format( "Bundle '%s' is not resolved.", bundleDescription.getSymbolicName() ) );
    }

    // bundle description describes a fragment ->
    // choose host as main bundle description
    if( isFragment( bundleDescription ) ) {
      return bundleDescription.getHost().getHosts()[0];
    }
    // bundle description describes a host -> return bundle description
    else {
      return bundleDescription;
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
  public static BundleLayoutResolver getBundleLayoutResolver( BundleDescription bundleDescription ) {

    // get the bundle source
    BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    File location = getLocation( bundleDescription );

    // eclipse project -> PluginProjectLayoutResolver
    if( bundleSource.isEclipseProject() ) {
      return new PluginProjectLayoutResolver( bundleSource.getAsEclipseProject() );
    }
    // directory -> ExplodedBundleLayoutResolver
    else if( location.isDirectory() ) {
      return new ExplodedBundleLayoutResolver( location );
    }
    // jar -> JaredBundleLayoutResolver
    else {
      return new JaredBundleLayoutResolver( location, ExpansionDirectory.getExpansionDir() );
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
  public static File getLocation( BundleDescription bundleDescription ) {

    // get the bundle source
    BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    File result = bundleSource.isEclipseProject() ? bundleSource.getAsEclipseProject().getFolder() : bundleSource.getAsFile();

    // return result
    return result;
  }

  /**
   * <p>
   * The class {@link BundleDependency} represents a dependency to another bundle. It also contains a set of all
   * packages that are imported by this dependency.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class BundleDependency {

    /** the host */
    private BundleDescription _bundleDescription;

    /** list of exported packages for this bundle dependency */
    private Set<String>       _exportedPackages;

    /**
     * <p>
     * Creates a new instance of type {@link BundleDependency}.
     * </p>
     * 
     * @param bundleDescription
     */
    public BundleDependency( BundleDescription bundleDescription ) {
      Assure.notNull( "bundleDescription", bundleDescription );
      _bundleDescription = bundleDescription;
      _exportedPackages = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Returns the {@link ResolvedClasspathEntry}.
     * </p>
     * 
     * @return the {@link ResolvedClasspathEntry}.
     */
    public ResolvedClasspathEntry getResolvedClasspathEntry() {

      // create the list of all contained files
      List<File> classfiles = new ArrayList<File>();
      List<File> sourcefiles = new ArrayList<File>();

      // resolve the host
      BundleLayoutResolver layoutResolver = getBundleLayoutResolver( _bundleDescription );
      classfiles.addAll( layoutResolver.resolveBundleClasspathEntries() );
      if( layoutResolver instanceof PluginProjectLayoutResolver ) {
        sourcefiles.addAll( ((PluginProjectLayoutResolver) layoutResolver).getPluginProjectSourceFolders() );
      }

      // resolve the fragments
      BundleDescription[] fragments = _bundleDescription.getFragments();
      for( BundleDescription fragmentDescription : fragments ) {
        layoutResolver = getBundleLayoutResolver( fragmentDescription );
        classfiles.addAll( layoutResolver.resolveBundleClasspathEntries() );
        if( layoutResolver instanceof PluginProjectLayoutResolver ) {
          sourcefiles.addAll( ((PluginProjectLayoutResolver) layoutResolver).getPluginProjectSourceFolders() );
        }
      }

      // create the access restrictions
      AccessRestrictions accessRestrictions = new AccessRestrictions();
      for( String exportedPackage : _exportedPackages ) {
        accessRestrictions.addPublicPackage( exportedPackage );
      }

      // return the result
      return new ResolvedClasspathEntry( classfiles, accessRestrictions, sourcefiles );
    }

    /**
     * <p>
     * Returns all referenced eclipse plug-in projects. This information is required to compute the build order between
     * eclipse plug-in projects.
     * </p>
     * 
     * @return a list with referenced eclipse plug-in projects
     */
    public List<EclipseProject> getReferencedPluginProjects() {

      // create the result list
      List<EclipseProject> result = new ArrayList<EclipseProject>();

      // add the host if it is an eclipse project
      BundleSource bundleSource = (BundleSource) _bundleDescription.getUserObject();

      if( bundleSource.isEclipseProject() ) {
        result.add( bundleSource.getAsEclipseProject() );
      }

      return result;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public BundleDescription getHost() {
      return _bundleDescription;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public BundleDescription[] getFragments() {
      return _bundleDescription.getFragments();
    }

    /**
     * <p>
     * Adds the package with the specified name as an imported packages.
     * </p>
     * 
     * @param packageName
     *          the package name
     */
    private void addExportedPackage( String packageName ) {
      _exportedPackages.add( packageName );
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((_bundleDescription == null) ? 0 : _bundleDescription.hashCode());
      result = prime * result + ((_exportedPackages == null) ? 0 : _exportedPackages.hashCode());
      return result;
    }

    @Override
    public boolean equals( Object obj ) {
      if( this == obj ) {
        return true;
      }
      if( obj == null ) {
        return false;
      }
      if( getClass() != obj.getClass() ) {
        return false;
      }
      BundleDependency other = (BundleDependency) obj;
      if( _bundleDescription == null ) {
        if( other._bundleDescription != null ) {
          return false;
        }
      } else if( !_bundleDescription.equals( other._bundleDescription ) ) {
        return false;
      }
      if( _exportedPackages == null ) {
        if( other._exportedPackages != null ) {
          return false;
        }
      } else if( !_exportedPackages.equals( other._exportedPackages ) ) {
        return false;
      }
      return true;
    }

  }

} /* ENDCLASS */
