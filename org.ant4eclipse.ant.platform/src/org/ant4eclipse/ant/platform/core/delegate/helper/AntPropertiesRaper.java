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
package org.ant4eclipse.ant.platform.core.delegate.helper;

import java.util.Hashtable;
import java.util.Properties;

import org.ant4eclipse.ant.core.ThreadDispatchingPropertyHelper;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionDelegate;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;

/**
 * <p>
 * Helper class that provides access to the ant project's property field. During macro execution with the
 * {@link MacroExecutionDelegate} several properties are (temporarily) set. Once the execution has finished, all
 * properties have to be reset.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AntPropertiesRaper extends AbstractAntProjectRaper<String> {

  /**
   * <p>
   * Creates a new instance of type {@link AntPropertiesRaper}.
   * </p>
   * 
   * @param antProject
   *          the ant project
   */
  public AntPropertiesRaper( Project antProject, Thread thread ) {
    super( antProject, thread );

    registerThread();

    // set the value accessor
    setValueAccessor( new AntProjectValueAccessor<String>() {

      /**
       * <p>
       * Returns the ant project property with the given key
       * </p>
       * 
       * @param key
       *          the key
       */
      @Override
      public String getValue( String key ) {
        return getAntProject().getProperty( key );
      }

      /**
       * <p>
       * Sets the given value as a ant project property with the given key.
       * </p>
       * 
       * @param key
       *          the key
       * @param value
       *          the value to set
       */
      @Override
      public void setValue( String key, String value ) {
        getAntProject().setProperty( key, value );
      }

      /**
       * <p>
       * Removes the given value from the ant project properties.
       * </p>
       * 
       * @param key
       *          the key
       */
      @Override
      public void unsetValue( String key ) {
        removeProperty( key );
      }
    } );
  }

  /**
   * <p>
   * </p>
   */
  private void registerThread() {
    PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper( getAntProject() ).getNext();
    if( propertyHelper instanceof ThreadDispatchingPropertyHelper ) {
      ((ThreadDispatchingPropertyHelper) propertyHelper).registerThread( Thread.currentThread() );
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   */
  private void removeProperty( String name ) {
    PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper( getAntProject() ).getNext();
    if( propertyHelper instanceof ThreadDispatchingPropertyHelper ) {
      // System.out.println(String.format(" - - - removeProperty(%s)", name));
      ThreadDispatchingPropertyHelper threadDispatchingPropertyHelper = (ThreadDispatchingPropertyHelper) propertyHelper;
      Properties threadProperties = threadDispatchingPropertyHelper.getThreadProperties();
      if( threadProperties != null ) {
        threadProperties.remove( name );
      } else {
        _removeProperty( name );
      }
    }
  }

  /**
   * <p>
   * Removes the given value from the ant project properties.
   * </p>
   * 
   * @param key
   *          the key
   */
  @SuppressWarnings( "rawtypes" )
  private void _removeProperty( String name ) {
    Hashtable properties = null;
    // Ant 1.5 stores properties in Project
    try {
      properties = (Hashtable) AbstractAntProjectRaper.getValue( getAntProject(), "properties" );
      if( properties != null ) {
        properties.remove( name );
      }
    } catch( Exception e ) {
      // ignore, could be Ant 1.6
    }
    try {
      properties = (Hashtable) AbstractAntProjectRaper.getValue( getAntProject(), "userProperties" );
      if( properties != null ) {
        properties.remove( name );
      }
    } catch( Exception e ) {
      // ignore, could be Ant 1.6
    }

    // Ant 1.6 uses a PropertyHelper, can check for it by checking for a
    // reference to "ant.PropertyHelper"
    try {

      // MULTITHREADING!!
      Object property_helper = getAntProject().getReference( "ant.PropertyHelper" );
      if( property_helper != null ) {
        try {

          // MULTITHREADING!!
          properties = (Hashtable) AbstractAntProjectRaper.getValue( property_helper, "properties" );
          if( properties != null ) {
            properties.remove( name );
          }
        } catch( Exception e ) {
          // ignore
        }
        try {

          // MULTITHREADING!!
          properties = (Hashtable) AbstractAntProjectRaper.getValue( property_helper, "userProperties" );
          if( properties != null ) {
            properties.remove( name );
          }
        } catch( Exception e ) {
          // ignore
        }
      }
    } catch( Exception e ) {
      // ignore, could be Ant 1.5
    }
  }
  
} /* ENDCLASS */
