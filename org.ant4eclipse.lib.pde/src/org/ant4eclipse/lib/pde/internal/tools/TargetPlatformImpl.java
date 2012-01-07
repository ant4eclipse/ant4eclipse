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
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest.Includes;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest.Plugin;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.tools.PlatformConfiguration;
import org.ant4eclipse.lib.pde.tools.ResolvedFeature;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ResolverError;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.osgi.service.resolver.StateObjectFactory;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * A target platform contains different plug-in sets. It defines the target against which plug-ins will be compiled and
 * tested.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TargetPlatformImpl implements TargetPlatform {

  /** the bundle set that contains the plug-in projects */
  private BundleAndFeatureSet       _pluginProjectSet;

  /** contains a list of all the binary bundle sets that belong to this target location */
  private List<BundleAndFeatureSet> _binaryBundleSets;

  /** the target platform configuration */
  private PlatformConfiguration     _configuration;

  /** the state object */
  private State                     _state;

  /** - */
  private File[]                    _targetplatformLocations;

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformImpl}.
   * </p>
   * 
   * @param pluginProjectSet
   *          the set of all plug-in projects contained in the workspace, may <code>null</code>.
   * @param binaryBundleSets
   *          an array of bundle sets that contain the binary bundles, may <code>null</code>.
   * @param configuration
   *          the {@link PlatformConfiguration} of this target platform
   * @param targetlocations
   *          a list of all targetplatform locations providing the bundles. Neither <code>null</code> nor empty.
   */
  public TargetPlatformImpl( BundleAndFeatureSet pluginProjectSet, BundleAndFeatureSet[] binaryBundleSets,
      PlatformConfiguration configuration, File[] targetlocations ) {
    Assure.notNull( "configuration", configuration );

    // set the plug-in project set
    _pluginProjectSet = pluginProjectSet;

    // set the binary bundle sets
    if( binaryBundleSets != null ) {
      _binaryBundleSets = Arrays.asList( binaryBundleSets );
    } else {
      _binaryBundleSets = new ArrayList<BundleAndFeatureSet>();
    }

    // set the configuration
    _configuration = configuration;

    _targetplatformLocations = targetlocations;

    // initialize
    initialize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File[] getLocations() {
    return _targetplatformLocations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlatformConfiguration getTargetPlatformConfiguration() {
    return _configuration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BundleDescription getResolvedBundle( String symbolicName, Version version ) {
    return _state.getBundle( symbolicName, version );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BundleDescription> getBundlesWithResolverErrors() {

    // create result
    List<BundleDescription> bundleDescriptions = new ArrayList<BundleDescription>();

    // iterate over all descriptions
    for( BundleDescription description : _state.getBundles() ) {
      ResolverError[] errors = _state.getResolverErrors( description );
      if( errors != null && errors.length > 0 ) {
        bundleDescriptions.add( description );
      }
    }

    return bundleDescriptions;
    
  }

  /**
   * {@inheritDoc}
   */
  private void initialize() {
    if( _state == null ) {

      if( _pluginProjectSet != null ) {
        _pluginProjectSet.initialize();
      }

      for( BundleAndFeatureSet bundleSet : _binaryBundleSets ) {
        bundleSet.initialize();
      }

      _state = resolve();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refresh() {

    if( _pluginProjectSet != null ) {
      _pluginProjectSet.refresh();
    }

    for( BundleAndFeatureSet bundleSet : _binaryBundleSets ) {
      bundleSet.refresh();
    }

    _state = resolve();
  }

  /**
   * <p>
   * Returns a list with a {@link BundleDescription BundleDescriptions} of each bundle that is contained in the plug-in
   * project set or the binary bundle sets.
   * </p>
   * 
   * @param preferProjects
   *          indicates of plug-in projects should be preferred over binary bundles or not.
   * @return a list with a {@link BundleDescription BundleDescriptions} of each bundle that is contained in the plug-in
   *         project set or the binary bundle sets.
   */
  private List<BundleDescription> getAllBundleDescriptions( boolean preferProjects ) {

    // step 1: create the result list
    List<BundleDescription> result = new ArrayList<BundleDescription>();

    // step 2: add plug-in projects from the plug-in projects list to the result
    if( _pluginProjectSet != null ) {
      result.addAll( _pluginProjectSet.getAllBundleDescriptions() );
    }

    // step 3: add bundles from binary bundle sets to the result
    for( BundleAndFeatureSet binaryBundleSet : _binaryBundleSets ) {

      for( BundleDescription bundleDescription : binaryBundleSet.getAllBundleDescriptions() ) {
        if( (_pluginProjectSet != null) && preferProjects
            && _pluginProjectSet.containsBundle( bundleDescription.getSymbolicName() ) ) {
          // TODO: WARNING AUSGEBEN?
        } else {
          result.add( bundleDescription );
        }
      }
    }

    // step 4: return the result
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FeatureDescription getFeatureDescription( String id, Version version ) {
    Assure.nonEmpty( "id", id );

    if( version == null ) {
      return getFeatureDescription( id );
    }

    if( version.equals( Version.emptyVersion ) ) {
      return getFeatureDescription( id );
    }

    //
    FeatureDescription featureDescription = _pluginProjectSet.getFeatureDescription( id, version );

    //
    if( featureDescription != null ) {
      return featureDescription;
    }

    for( BundleAndFeatureSet bundleSet : _binaryBundleSets ) {
      featureDescription = bundleSet.getFeatureDescription( id, version );
      if( featureDescription != null ) {
        return featureDescription;
      }
    }
    // TODO
    A4ELogging.error( "Could not resolve feature '%s' in version '%s'.", id, version );
    //
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasFeatureDescription( String id, Version version ) {
    return getFeatureDescription( id, version ) != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FeatureDescription getFeatureDescription( String id ) {
    Assure.nonEmpty( "id", id );

    //
    FeatureDescription featureDescription = _pluginProjectSet.getFeatureDescription( id );

    //
    if( featureDescription != null ) {
      return featureDescription;
    }

    // result
    FeatureDescription result = null;

    // iterate over feature descriptions
    for( BundleAndFeatureSet bundleSet : _binaryBundleSets ) {

      // get the feature manifest
      featureDescription = bundleSet.getFeatureDescription( id );

      // if match -> set as result
      if( featureDescription != null && featureDescription.getFeatureManifest().getId().equals( id ) ) {
        if( result == null ) {
          result = featureDescription;
        } else {
          // the current feature description has a higher version, so use this one
          if( result.getFeatureManifest().getVersion().compareTo( featureDescription.getFeatureManifest().getVersion() ) < 0 ) {
            result = featureDescription;
          }
        }
      }
    }

    // return result
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasFeatureDescription( String id ) {
    return getFeatureDescription( id ) != null;
  }

  @Override
  public boolean matchesPlatformFilter( String id ) {
    try {

      //
      BundleDescription bundleDescription = getBundleDescription( id );
      if( bundleDescription == null ) {
        return true;
      }

      //
      String platformFilter = bundleDescription.getPlatformFilter();
      if( platformFilter == null ) {
        return true;
      }

      String arch = getTargetPlatformConfiguration().getArchitecture();
      String os = getTargetPlatformConfiguration().getOperatingSystem();
      String ws = getTargetPlatformConfiguration().getWindowingSystem();

      Properties dictionary = new Properties();
      dictionary.put( "osgi.ws", ws );
      dictionary.put( "osgi.os", os );
      dictionary.put( "osgi.arch", arch );

      Filter filter = FrameworkUtil.createFilter( platformFilter );
      return filter.match( dictionary );
    } catch( InvalidSyntaxException e ) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BundleDescription getBundleDescription( String id ) {
    Assure.nonEmpty( "id", id );

    //
    BundleDescription bundleDescription = _pluginProjectSet.getBundleDescription( id );

    //
    if( bundleDescription != null ) {
      return bundleDescription;
    }

    // result
    BundleDescription result = null;

    // iterate over feature descriptions
    for( BundleAndFeatureSet bundleSet : _binaryBundleSets ) {

      // get the feature manifest
      bundleDescription = bundleSet.getBundleDescription( id );

      // if match -> set as result
      if( bundleDescription != null ) {
        if( result == null ) {
          result = bundleDescription;
        } else {
          // the current bundle description has a higher version, so use this one
          if( result.getVersion().compareTo( bundleDescription.getVersion() ) < 0 ) {
            result = bundleDescription;
          }
        }
      }
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BundleDescription getBundleDescriptionFromWorkspace( String symbolicName ) {
    Assure.nonEmpty( "symbolicName", symbolicName );

    //
    return _pluginProjectSet.getBundleDescription( symbolicName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BundleDescription getBundleDescriptionFromBinaryBundles( String symbolicName ) {
    Assure.nonEmpty( "symbolicName", symbolicName );

    //
    BundleDescription bundleDescription = null;
    BundleDescription result = null;

    // iterate over feature descriptions
    for( BundleAndFeatureSet bundleSet : _binaryBundleSets ) {

      // get the feature manifest
      bundleDescription = bundleSet.getBundleDescription( symbolicName );

      // if match -> set as result
      if( bundleDescription != null ) {
        if( result == null ) {
          result = bundleDescription;
        } else {
          // the current bundle description has a higher version, so use this one
          if( result.getVersion().compareTo( bundleDescription.getVersion() ) < 0 ) {
            result = bundleDescription;
          }
        }
      }
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBundleDescription( String id ) {
    return getBundleDescription( id ) != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResolvedFeature resolveFeature( Object source, FeatureManifest manifest ) {
    Assure.notNull( "source", source );
    Assure.notNull( "manifest", manifest );

    ResolvedFeature resolvedFeature = new ResolvedFeature( source, manifest );

    resolvePlugins( manifest, resolvedFeature );

    resolveIncludes( manifest, resolvedFeature );

    // 6.3 return result
    return resolvedFeature;
  }

  /**
   * <p>
   * </p>
   * 
   * @param manifest
   * @param resolvedFeature
   */
  private void resolveIncludes( FeatureManifest manifest, ResolvedFeature resolvedFeature ) {

    // TODO: DependencyGraph!!
    List<Pair<Includes,FeatureDescription>> result = new ArrayList<Pair<Includes,FeatureDescription>>();

    for( Includes includes : manifest.getIncludes() ) {

      if( matches( includes.getOperatingSystem(), includes.getMachineArchitecture(), includes.getWindowingSystem(),
          includes.getLocale() ) ) {

        FeatureDescription featureDescription = null;
        if( includes.getVersion().equals( Version.emptyVersion ) ) {
          featureDescription = getFeatureDescription( includes.getId() );
        } else {
          featureDescription = getFeatureDescription( includes.getId(), includes.getVersion() );
        }
        if( featureDescription == null ) {
          // TODO: NLS
          throw new RuntimeException( "No Feature found for included feature '" + includes.getId() + "_"
              + includes.getVersion() + "'." );
        } else {
          result.add( new Pair<Includes,FeatureDescription>( includes, featureDescription ) );
        }
      }
    }

    resolvedFeature.setIncludesToFeatureDescriptionList( result );
  }

  /**
   * <p>
   * </p>
   * 
   * @param manifest
   * @param resolvedFeature
   * @throws BuildException
   */
  private void resolvePlugins( FeatureManifest manifest, ResolvedFeature resolvedFeature ) {

    // 4. Retrieve BundlesDescriptions for feature plug-ins
    Map<BundleDescription,Plugin> map = new HashMap<BundleDescription,Plugin>();
    List<BundleDescription> bundleDescriptions = new ArrayList<BundleDescription>();

    for( Plugin plugin : manifest.getPlugins() ) {

      if( matches( plugin.getOperatingSystem(), plugin.getMachineArchitecture(), plugin.getWindowingSystem(),
          plugin.getLocale() ) ) {

        // if a plug-in reference uses a version, the exact version must be found in the workspace
        // if a plug-in reference specifies "0.0.0" as version, the newest plug-in found will be used
        BundleDescription bundleDescription = _state.getBundle( plugin.getId(),
            plugin.getVersion().equals( Version.emptyVersion ) ? null : plugin.getVersion() );
        // TODO: NLS
        if( bundleDescription == null ) {
          throw new Ant4EclipseException( PdeExceptionCode.SPECIFIED_BUNDLE_NOT_FOUND, plugin.getId(),
              plugin.getVersion() );
        }
        // TODO: NLS
        if( !bundleDescription.isResolved() ) {
          if( !TargetPlatformImpl.platformFilterDoesNotMatch( bundleDescription ) ) {
            String resolverErrors = TargetPlatformImpl.dumpResolverErrors( bundleDescription, true );
            String bundleInfo = TargetPlatformImpl.getBundleInfo( bundleDescription );
            throw new RuntimeException( String.format( "Bundle '%s' is not resolved. Reason:\n%s", bundleInfo,
                resolverErrors ) );
          }
        } else {
          bundleDescriptions.add( bundleDescription );
          map.put( bundleDescription, plugin );
        }
      }
    }

    // 5. Sort the bundles
    BundleDescription[] sortedbundleDescriptions = bundleDescriptions.toArray( new BundleDescription[0] );
    Object[][] cycles = _state.getStateHelper().sortBundles( sortedbundleDescriptions );
    // warn on circular dependencies
    if( (cycles != null) && (cycles.length > 0) ) {
      // TODO: better error messages
      A4ELogging.warn( "Detected circular dependencies:" );
      for( Object[] cycle : cycles ) {
        A4ELogging.warn( Arrays.asList( cycle ).toString() );
      }
    }

    // 6.1 create result
    List<Pair<Plugin,BundleDescription>> result = new ArrayList<Pair<Plugin,BundleDescription>>();
    for( BundleDescription bundleDescription : sortedbundleDescriptions ) {
      Pair<Plugin,BundleDescription> pair = new Pair<Plugin,BundleDescription>( map.get( bundleDescription ),
          bundleDescription );
      result.add( pair );
    }

    resolvedFeature.setPluginToBundleDescptionList( result );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private State resolve() {

    // TODO
    FrameworkProperties.setProperty( "osgi.resolver.usesMode", "ignore" );

    // step 1: create new state
    State state = StateObjectFactory.defaultFactory.createState( true );

    for( BundleDescription bundleDescription : getAllBundleDescriptions( _configuration.isPreferProjects() ) ) {
      BundleDescription copy = StateObjectFactory.defaultFactory.createBundleDescription( bundleDescription );
      copy.setUserObject( bundleDescription.getUserObject() );
      if( !state.addBundle( copy ) ) {
        // TODO: NLS
        throw new RuntimeException( "Could not add bundle '" + bundleDescription + "' to state!" );
      }
      if( A4ELogging.isTraceingEnabled() ) {
        A4ELogging.trace( "Copied bundle to state: '%s'", getBundleInfo( bundleDescription ) );
      }
    }

    // set the platform properties
    Properties platformProperties = _configuration.getConfigurationProperties();
    if( A4ELogging.isDebuggingEnabled() ) {
      A4ELogging.debug( Utilities.toString( "Initializing TargetPlatform with properties: ", platformProperties ) );
    }
    state.setPlatformProperties( platformProperties );

    // resolve the state
    state.resolve();

    // log errors if any
    BundleDescription[] bundleDescriptions = state.getBundles();
    // boolean allStatesResolved = true;

    if( A4ELogging.isDebuggingEnabled() ) {
      for( BundleDescription description : bundleDescriptions ) {
        String resolverErrors = dumpResolverErrors( description, true );
        if( resolverErrors != null && !resolverErrors.trim().equals( "" ) ) {
          A4ELogging.debug( resolverErrors );
        }
      }
    }
    // return the state
    return state;
  }

  /**
   * Returns <code>true</code> if the current target configuration matches a given system specification.
   * 
   * @param os
   *          The os to be matched.
   * @param arch
   *          The architecture to be matched.
   * @param ws
   *          The windowing system to be matched.
   * @param nl
   *          The language to be matched.
   * 
   * @return <code>true</code> <=> The configuration matches the given system specification.
   */
  private boolean matches( String os, String arch, String ws, String nl ) {
    return contains( _configuration.getOperatingSystem(), os )
        && contains( _configuration.getArchitecture(), arch )
        && contains( _configuration.getWindowingSystem(), ws )
        && contains( _configuration.getLanguageSetting(), nl );
  }

  /**
   * <p>
   * </p>
   * 
   * @param element
   * @param commaSeparatedList
   * @return
   */
  private static boolean contains( String element, String commaSeparatedList ) {

    //
    if( element == null || element.trim().equals( "" ) ) {
      return true;
    }

    //
    if( commaSeparatedList == null || commaSeparatedList.trim().equals( "" ) ) {
      return true;
    }

    // split the elements
    String[] elements = commaSeparatedList.split( "," );

    // iterate over all the list elements
    for( String listElement : elements ) {

      //
      if( element.trim().equalsIgnoreCase( listElement ) ) {
        return true;
      }
    }

    // finally return false
    return false;
  }

  public static boolean platformFilterDoesNotMatch( BundleDescription description ) {
    Assure.notNull( "description", description );

    State state = description.getContainingState();
    ResolverError[] errors = state.getResolverErrors( description );

    return errors != null && errors.length == 1 && errors[0].getType() == ResolverError.PLATFORM_FILTER;
  }

  /**
   * <p>
   * Returns the resolver errors as a string.
   * </p>
   * 
   * @param description
   *          the bundle description
   * @param dumpHeader
   *          indicates if the header should be dumped or not
   * @return the resolver errors as a string.
   */
  public static String dumpResolverErrors( BundleDescription description, boolean dumpHeader ) {
    Assure.notNull( "description", description );

    StringBuffer stringBuffer = new StringBuffer();
    State state = description.getContainingState();
    ResolverError[] errors = state.getResolverErrors( description );
    if( !description.isResolved() || ((errors != null) && (errors.length != 0)) ) {
      if( (errors != null) && (errors.length == 1) && (errors[0].getType() == ResolverError.SINGLETON_SELECTION) ) {
        stringBuffer.append( "Not using '" );
        stringBuffer.append( getBundleInfo( description ) );
        stringBuffer.append( "' -- another version resolved\n" );
      } else {
        if( dumpHeader ) {
          stringBuffer.append( "Could not resolve '" );
          // stringBuffer.append(getBundleInfo(description));
          stringBuffer.append( description.getSymbolicName() );
          stringBuffer.append( "_" );
          stringBuffer.append( description.getVersion() );
          stringBuffer.append( "' (Location: " );
          stringBuffer.append( description.getLocation() );
          stringBuffer.append( "):\n" );
        }
        if( errors != null ) {
          if( errors.length > 0 ) {
            for( int i = 0; i < errors.length; i++ ) {
              stringBuffer.append( "  " );
              stringBuffer.append( errors[i] );
              if( i + 1 < errors.length ) {
                stringBuffer.append( "\n" );
              }
            }
          }
        }
      }
    }
    return stringBuffer.toString();
  }

  /**
   * <p>
   * Returns the bundle info of the given bundle description.
   * </p>
   * 
   * @param description
   *          the bundle description.
   * @return the bundle info of the given bundle description.
   */
  static String getBundleInfo( BundleDescription description ) {
    Assure.notNull( "description", description );

    BundleSource bundleSource = BundleSource.getBundleSource( description );

    StringBuffer buffer = new StringBuffer();
    buffer.append( description.getSymbolicName() ).append( "_" ).append( description.getVersion().toString() )
        .append( "@" );
    if( bundleSource.isEclipseProject() ) {
      buffer.append( "<P>" ).append( bundleSource.getAsEclipseProject().getFolder() );
    } else {
      buffer.append( bundleSource.getAsFile().getAbsolutePath() );
    }
    return buffer.toString();
  }

} /* ENDCLASS */
