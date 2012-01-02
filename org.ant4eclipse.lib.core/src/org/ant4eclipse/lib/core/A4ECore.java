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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.util.Utilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Initializes this base class. 
 */
public class A4ECore {

  private static final A4ECore INSTANCE = new A4ECore();

  private Map<Class<?>,List<?>>   servicecache;
  
  /**
   * Initializes this management class for A4E related services and data.
   */
  private A4ECore() {
    servicecache = new Hashtable<Class<?>,List<?>>();
  }
  
  /**
   * Returns a required service type. 
   * 
   * @param servicetype   The required service type. Not <code>null</code>.
   * 
   * @return   An instance of the supplied type. Not <code>null</code>.
   */
  public <T> T getRequiredService( Class<T> servicetype ) {
    List<T> services = loadServices( servicetype );
    if( services.isEmpty() ) {
      /* KASI */
      throw new RuntimeException( String.format( "Missing required service: '%s'", servicetype.getName() ) );
    }
    if( services.size() != 1 ) {
      /* KASI */
      Class<?>[] clazzes = Utilities.getClasses( services.toArray() );
      throw new RuntimeException( String.format( "Too many services for type '%s' (%s)", servicetype.getName(), Utilities.toString( null, false, clazzes ) ) );
    }
    return null;
  }

  /**
   * Returns an optional service type. 
   * 
   * @param servicetype   The optional service type. Not <code>null</code>.
   * 
   * @return   An instance of the supplied type. Not <code>null</code>.
   */
  public <T> T getOptionalService( Class<T> servicetype ) {
    List<T> services = loadServices( servicetype );
    if( services.size() > 1 ) {
      /* KASI */
      Class<?>[] clazzes = Utilities.getClasses( services.toArray() );
      throw new RuntimeException( String.format( "Too many services for type '%s' (%s)", servicetype.getName(), Utilities.toString( null, false, clazzes ) ) );
    }
    if( services.isEmpty() ) {
      return null;
    } else {
      return services.get(0);
    }
  }

  /**
   * Returns a list of service implementations for the supplied service type.
   * 
   * @param servicetype   The service type used to identify the implementations. Not <code>null</code>.
   * 
   * @return   A list of service implementations for the supplied service type. Not <code>null</code>.
   */
  public <T> List<T> getServices( Class<T> servicetype ) {
    return loadServices( servicetype );
  }

  /**
   * Loads all service implementations for the supplied interface unless they're already cached.  
   * 
   * @param clazz   The service type used to identify the implementations. Not <code>null</code>.
   * 
   * @return   A list of  service implementations associated with the supplied type. Not <code>null</code>.
   */
  private <T> List<T> loadServices( Class<T> clazz ) {
    List<T> result = (List<T>) servicecache.get( clazz );
    if( result == null ) {
      result = new ArrayList<T>();
      Iterator<T> iterator = ServiceLoader.load( clazz ).iterator();
      while( iterator.hasNext() ) {
        result.add( iterator.next() );
      }
      servicecache.put( clazz, result );
    }
    return result;
  }
  
  /**
   * Delivers the basic functionality used to manage A4E related services and data.
   * 
   * @return   The basic functionality used to manage A4E related services and data. 
   *           Not <code>null</code>.
   */
  public static final A4ECore instance() {
    return INSTANCE;
  }
  
} /* ENDCLASS */
