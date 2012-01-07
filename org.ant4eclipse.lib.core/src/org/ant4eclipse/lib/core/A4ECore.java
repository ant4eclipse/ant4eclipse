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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Initializes this base class. 
 */
public class A4ECore {

  private static final A4ECore                     INSTANCE   = new A4ECore();

  private static final Comparator<A4EService>      COMPARATOR = new Comparator<A4EService>() {

                                                                /**
                                                                 * {@inheritDoc}
                                                                 */
                                                                @Override
                                                                public int compare( A4EService s1, A4EService s2 ) {
                                                                  Integer o1 = s1.getPriority();
                                                                  Integer o2 = s2.getPriority();
                                                                  if( (o1 != null) && (o2 != null) ) {
                                                                    return o2.compareTo( o1 );
                                                                  } else if( (o1 == null) && (o2 == null) ) {
                                                                    return 0;
                                                                  } else if( o1 != null ) {
                                                                    return -1;
                                                                  } else {
                                                                    return 1;
                                                                  }
                                                                }

                                                              };

  private Map<Class<?>,List<? extends A4EService>> servicecache;

  private Map<Object,Object>                       runtimecache;

  /**
   * Initializes this management class for A4E related services and data.
   */
  private A4ECore() {
    servicecache = new Hashtable<Class<?>,List<? extends A4EService>>();
    runtimecache = new Hashtable<Object,Object>();
  }

  public void putRuntimeValue( Object key, Object val ) {
    runtimecache.put( key, val );
  }

  public <T> T getRuntimeValue( Object key ) {
    return (T) runtimecache.get( key );
  }

  /**
   * This function can be called to enforce the reset of all loaded service states.
   */
  public void reset() {
    for( List<? extends A4EService> services : servicecache.values() ) {
      for( A4EService service : services ) {
        service.reset();
      }
    }
    runtimecache.clear();
  }

  /**
   * Returns a required service type.
   * 
   * @param servicetype
   *          The required service type. Not <code>null</code>.
   * 
   * @return An instance of the supplied type. Not <code>null</code>.
   */
  public <T extends A4EService> T getRequiredService( Class<T> servicetype ) {
    List<T> services = loadServices( servicetype );
    if( services.isEmpty() ) {
      /* KASI */
      throw new RuntimeException( String.format( "Missing required service: '%s'", servicetype.getName() ) );
    }
    return services.get( 0 );
  }

  /**
   * Returns an optional service type.
   * 
   * @param servicetype
   *          The optional service type. Not <code>null</code>.
   * 
   * @return An instance of the supplied type. Not <code>null</code>.
   */
  public <T extends A4EService> T getOptionalService( Class<T> servicetype ) {
    List<T> services = loadServices( servicetype );
    if( services.isEmpty() ) {
      return null;
    } else {
      return services.get( 0 );
    }
  }

  /**
   * Returns a list of service implementations for the supplied service type.
   * 
   * @param servicetype
   *          The service type used to identify the implementations. Not <code>null</code>.
   * 
   * @return A list of service implementations for the supplied service type. Not <code>null</code>.
   */
  public <T extends A4EService> List<T> getServices( Class<T> servicetype ) {
    return loadServices( servicetype );
  }

  /**
   * Loads all service implementations for the supplied interface unless they're already cached.
   * 
   * @param clazz
   *          The service type used to identify the implementations. Not <code>null</code>.
   * 
   * @return A list of service implementations associated with the supplied type. Not <code>null</code>.
   */
  private <T extends A4EService> List<T> loadServices( Class<T> clazz ) {
    List<T> result = (List<T>) servicecache.get( clazz );
    if( result == null ) {
      result = new ArrayList<T>();
      Iterator<T> iterator = ServiceLoader.load( clazz ).iterator();
      while( iterator.hasNext() ) {
        result.add( iterator.next() );
      }
      Collections.sort( result, COMPARATOR );
      servicecache.put( clazz, result );
    }
    return result;
  }

  /**
   * Delivers the basic functionality used to manage A4E related services and data.
   * 
   * @return The basic functionality used to manage A4E related services and data. Not <code>null</code>.
   */
  public static final A4ECore instance() {
    return INSTANCE;
  }

} /* ENDCLASS */
