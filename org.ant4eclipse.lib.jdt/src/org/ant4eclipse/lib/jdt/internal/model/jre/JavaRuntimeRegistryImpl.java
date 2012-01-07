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
package org.ant4eclipse.lib.jdt.internal.model.jre;

import static org.ant4eclipse.lib.core.logging.A4ELogging.trace;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.JdtExceptionCode;
import org.ant4eclipse.lib.jdt.model.ContainerTypes;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Implementation of the {@link JavaRuntimeRegistry}.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaRuntimeRegistryImpl implements JavaRuntimeRegistry {

  /** the default java runtime key * */
  private String                  _defaultJavaRuntimeKey = null;

  /** the java runtime cache */
  private Map<String,JavaRuntime> _javaRuntimeCache;

  /**
   * <p>
   * Creates a new instance of type {@link JavaRuntimeRegistryImpl}.
   * </p>
   */
  public JavaRuntimeRegistryImpl() {

    // create hash maps
    _javaRuntimeCache = new HashMap<String,JavaRuntime>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JavaRuntime registerJavaRuntime( String id, File location, boolean isDefault ) {
    return registerJavaRuntime( id, location, null, isDefault );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JavaRuntime registerJavaRuntime( String id, File location ) {
    return registerJavaRuntime( id, location, null );

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry#registerJavaRuntime(java.lang.String, java.io.File,
   * java.util.List)
   */
  @Override
  public JavaRuntime registerJavaRuntime( String id, File location, List<File> jreFiles ) {
    return registerJavaRuntime( id, location, jreFiles, false );
  }

  private JavaRuntime registerJavaRuntime( String id, File location, List<File> jreFiles, boolean isDefault ) {
    Assure.nonEmpty( "id", id );
    Assure.isDirectory( "location", location );

    JavaRuntime javaRuntime = JavaRuntimeLoader.loadJavaRuntime( id, location, jreFiles );

    return registerJavaRuntime( javaRuntime, isDefault );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDefaultJavaRuntime( String id ) {
    Assure.notNull( "id", id );
    Assure.assertTrue( hasJavaRuntime( id ), "No JavaRuntime with id '" + id + "' registered!" );

    _defaultJavaRuntimeKey = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasJavaRuntime( String path ) {
    Assure.nonEmpty( "path", path );

    // return true if a java runtime exists
    if( _javaRuntimeCache.containsKey( path ) ) {
      return true;
    }

    // return if a java profile exists
    JavaProfile javaProfile = getProfileReader().getJavaProfile( path );
    if( javaProfile != null && getJavaRuntime( javaProfile ) != null ) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasJavaProfile( String path ) {
    Assure.nonEmpty( "path", path );
    return getProfileReader().hasJavaProfile( path );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JavaRuntime getJavaRuntime( String path ) {
    Assure.nonEmpty( "path", path );

    // return true if a java runtime exists
    if( _javaRuntimeCache.containsKey( path ) ) {
      return _javaRuntimeCache.get( path );
    }

    // return if a java profile exists
    JavaProfile javaProfile = getProfileReader().getJavaProfile( path );
    if( javaProfile != null ) {

      if( ((JavaProfileImpl) javaProfile).getAssociatedJavaRuntimeId() != null ) {
        return getJavaRuntime( ((JavaProfileImpl) javaProfile).getAssociatedJavaRuntimeId() );
      } else {
        return getJavaRuntime( javaProfile );
      }
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry#getJavaRuntimeForPath(java.lang.String)
   */
  @Override
  public JavaRuntime getJavaRuntimeForPath( String path ) {
    Assure.notNull( "path", path );

    trace( "Determining JRE for path '%s'", path );

    if( ContainerTypes.JRE_CONTAINER.equals( path ) ) {
      return getDefaultJavaRuntime();
    }

    JavaRuntime javaRuntime = null;

    if( path.startsWith( ContainerTypes.VMTYPE_PREFIX ) ) {
      javaRuntime = getJavaRuntime( path.substring( ContainerTypes.VMTYPE_PREFIX.length() ) );
    }

    if( javaRuntime != null ) {
      return javaRuntime;
    }

    A4ELogging.warn( "No java runtime '%s' defined - using default runtime.", path );

    return getDefaultJavaRuntime();
  }

  private JavaProfileReader getProfileReader() {
    return A4ECore.instance().getRequiredService( JavaProfileReader.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JavaProfile getJavaProfile( String path ) {
    Assure.nonEmpty( "path", path );
    return getProfileReader().getJavaProfile( path );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JavaRuntime getDefaultJavaRuntime() {

    // search for default key
    if( (_defaultJavaRuntimeKey != null) && _javaRuntimeCache.containsKey( _defaultJavaRuntimeKey ) ) {
      return _javaRuntimeCache.get( _defaultJavaRuntimeKey );
    }

    // TODO:
    // A4ELogging
    // .warn(
    // "No java runtime could be found for eclipse project '%s'. Possible reasons are: either there is no JRE_CONTAINER
    // specified on the classpath or there is no JavaRuntime registered for the specified JRE_CONTAINER. Trying to use
    // JRE from java.home"
    // ,
    // _eclipseProject.getName());

    // try to create java runtime from java.home
    JavaRuntime javaRuntime = getJavaRuntimeFromJavaHome();
    if( javaRuntime != null ) {
      return javaRuntime;
    }

    // no java runtime available - throw RuntimeException
    throw new Ant4EclipseException( JdtExceptionCode.NO_DEFAULT_JAVA_RUNTIME_EXCEPTION );
  }

  /**
   * <p>
   * </p>
   * 
   * @param javaProfile
   * @return
   */
  private JavaRuntime getJavaRuntime( JavaProfile javaProfile ) {

    // result
    JavaRuntime result = null;

    String profileName = javaProfile.getName();

    // iterate over java runtime cache
    for( Object element : _javaRuntimeCache.values() ) {

      // get the java runtime
      JavaRuntime javaRuntime = (JavaRuntime) element;

      if( javaRuntime.getJavaProfile().getExecutionEnvironmentNames().contains( profileName ) ) {

        if( result == null ) {
          result = javaRuntime;
        } else {
          if( getIndex( javaRuntime, profileName ) < getIndex( result, profileName ) ) {
            result = javaRuntime;
          }
        }
      }
    }

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param javaRuntime
   * @param profileName
   * @return
   */
  private int getIndex( JavaRuntime javaRuntime, String profileName ) {
    int index = javaRuntime.getJavaProfile().getExecutionEnvironmentNames().indexOf( profileName );
    index = javaRuntime.getJavaProfile().getExecutionEnvironmentNames().size() - index;
    return index;
  }

  /**
   * <p>
   * Registers the specified {@link JavaRuntime}.
   * </p>
   * 
   * @param javaRuntime
   *          the java runtime to specify.
   * @param isDefault
   *          indicates if the java runtime is the default runtime or not.
   * 
   * @return the path under this java runtime is stored, e.g.
   *         <code>org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/jdk15</code>
   */
  private JavaRuntime registerJavaRuntime( JavaRuntime javaRuntime, boolean isDefault ) {
    Assure.notNull( "javaRuntime", javaRuntime );

    // create path
    String id = javaRuntime.getId();

    if( _javaRuntimeCache.containsKey( id ) ) {
      JavaRuntime runtime = _javaRuntimeCache.get( id );

      if( !runtime.equals( javaRuntime ) ) {

        // throw an exception
        // TODO
        throw new RuntimeException( "Duplicate definition of JavaRuntime with key '" + id + "'." );
      }
      // return previous instance
      return _javaRuntimeCache.get( id );
    }

    // store java runtime
    _javaRuntimeCache.put( id, javaRuntime );

    // store default if necessary
    if( isDefault || (_defaultJavaRuntimeKey == null) ) {
      setDefaultJavaRuntime( id );
    }

    // return java runtime
    return javaRuntime;
  }

  /**
   * <p>
   * Tries to create a java runtime for the JRE defined under system property 'java.home'. If the system property is not
   * properly set, <code>null</code> will be returned instead.
   * </p>
   * 
   * @return the {@link JavaRuntime} or <code>null</code> if no such {@link JavaRuntime} exists.
   */
  private JavaRuntime getJavaRuntimeFromJavaHome() {

    // read system property 'java.home'
    String javaHome = System.getProperty( "java.home" );

    // if system property 'java.home' is not set, return null
    if( javaHome == null ) {
      A4ELogging.debug( "System property 'java.home' not set." );
      return null;
    }

    // create file
    File location = new File( javaHome );

    // if location is not a directory, return null
    if( !location.isDirectory() ) {
      A4ELogging.debug( "Location of 'java.home' (%s) is not a directory", javaHome );
      return null;
    }

    // create new java runtime
    A4ELogging.debug( "Using JRE defined in system property 'java.home' (%s)", location.getAbsolutePath() );
    return JavaRuntimeLoader.loadJavaRuntime( "java.home", location, null );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }

} /* ENDCLASS */
