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

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Taken from Framework!
 * </p>
 */
public class JavaProfileReader implements A4EService {

  /** the java profile cache */
  private Map<String,JavaProfile> _javaProfileCache;

  public JavaProfileReader() {

    _javaProfileCache = new HashMap<String,JavaProfile>();

    // read all known profiles
    JavaProfile[] javaProfiles = readAllProfiles();

    // add profiles to profile cache
    for( JavaProfile javaProfile : javaProfiles ) {
      _javaProfileCache.put( javaProfile.getName(), javaProfile );
    }
  }

  /**
   * @return
   */
  public JavaProfile readDefaultProfile() {
    return _javaProfileCache.get( "JavaSE-1.6" );
  }

  /**
   * {@inheritDoc}
   */
  // Assure.nonEmpty( "path", path );
  public JavaProfile getJavaProfile( String path ) {
    return _javaProfileCache.get( path );
  }

  // Assure.nonEmpty( "path", path );
  public boolean hasJavaProfile( String path ) {
    return _javaProfileCache.containsKey( path );
  }

  /**
   * <p>
   * </p>
   * 
   * @param profileFile
   * @return
   */
  // Assure.exists( "profileFile", profileFile );
  // Assure.nonEmpty( "jreId", jreId );
  public void registerProfile( File profileFile, String jreId ) {
    StringMap props = new StringMap( profileFile );
    JavaProfileImpl javaProfile = new JavaProfileImpl( props );
    javaProfile.setAssociatedJavaRuntimeId( jreId );
    _javaProfileCache.put( javaProfile.getName(), javaProfile );
  }

  /**
   * @return
   */
  private JavaProfile[] readAllProfiles() {

    // load the profile listing first
    StringMap properties = new StringMap( "/profiles/profile.list" );

    String javaProfiles = properties.get( "java.profiles" );

    String[] profiles = javaProfiles.split( "," );

    List<JavaProfileImpl> result = new ArrayList<JavaProfileImpl>();

    for( String profile2 : profiles ) {
      String profile = profile2.trim();
      if( (profile != null) && !"".equals( profile ) ) {
        StringMap props = new StringMap( "/profiles/" + profile );
        result.add( new JavaProfileImpl( props ) );
      }
    }

    return result.toArray( new JavaProfile[result.size()] );
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
