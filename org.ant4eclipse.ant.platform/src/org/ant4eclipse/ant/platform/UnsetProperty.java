package org.ant4eclipse.ant.platform;

import java.lang.reflect.Field;
import java.util.Hashtable;

import org.ant4eclipse.ant.core.ThreadDispatchingPropertyHelper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class UnsetProperty extends Task {

  private String name = null;

  /**
   * Set the name of the property. Required unless 'file' is used.
   * 
   * @param name
   *          the name of the property.
   */
  public void setName( String name ) {
    this.name = name;
  }

  /**
   * Execute this task.
   * 
   * @exception BuildException
   *              Description of the Exception
   */
  @Override
  public void execute() throws BuildException {

    // assert
    if( this.name == null || this.name.equals( "" ) ) {
      throw new BuildException( "The 'name' attribute is required with 'unset'." );
    }

    // the done variable
    boolean done = false;

    //
    if( ThreadDispatchingPropertyHelper.hasInstance( getProject() ) ) {
      done = ThreadDispatchingPropertyHelper.getInstance( getProject() ).removeProperty( this.name );
    }

    //
    if( !done ) {

      // remove property
      removeProperty( this.name );
    }
    //
    return;
  }

  /**
   * Remove a property from the project's property table and the userProperty table. Note that Ant 1.6 uses a helper for
   * this.
   */
  private void removeProperty( String name ) {

    Hashtable properties = null;

    // Ant 1.5 stores properties in Project
    try {
      properties = (Hashtable) getValue( getProject(), "properties" );
      if( properties != null ) {
        properties.remove( name );
      }
    } catch( Exception e ) {
      // ignore, could be Ant 1.6
    }
    try {
      properties = (Hashtable) getValue( getProject(), "userProperties" );
      if( properties != null ) {
        properties.remove( name );
      }
    } catch( Exception e ) {
      // ignore, could be Ant 1.6
    }

    // Ant 1.6 uses a PropertyHelper, can check for it by checking for a
    // reference to "ant.PropertyHelper"
    try {
      Object property_helper = getProject().getReference( "ant.PropertyHelper" );
      if( property_helper != null ) {
        try {
          properties = (Hashtable) getValue( property_helper, "properties" );
          if( properties != null ) {
            properties.remove( name );
          }
        } catch( Exception e ) {
          // ignore
        }
        try {
          properties = (Hashtable) getValue( property_helper, "userProperties" );
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

  /**
   * Object rape: fondle the private parts of an object without it's permission.
   * 
   * @param thisClass
   *          The class to rape.
   * @param fieldName
   *          The field to fondle
   * @return The field value
   * @exception NoSuchFieldException
   *              Darn, nothing to fondle.
   */
  private Field getField( Class thisClass, String fieldName ) throws NoSuchFieldException {
    if( thisClass == null ) {
      throw new NoSuchFieldException( "Invalid field : " + fieldName );
    }
    try {
      return thisClass.getDeclaredField( fieldName );
    } catch( NoSuchFieldException e ) {
      return getField( thisClass.getSuperclass(), fieldName );
    }
  }

  /**
   * Object rape: fondle the private parts of an object without it's permission.
   * 
   * @param instance
   *          the object instance
   * @param fieldName
   *          the name of the field
   * @return an object representing the value of the field
   * @exception IllegalAccessException
   *              foiled by the security manager
   * @exception NoSuchFieldException
   *              Darn, nothing to fondle
   */
  private Object getValue( Object instance, String fieldName ) throws IllegalAccessException, NoSuchFieldException {
    Field field = getField( instance.getClass(), fieldName );
    field.setAccessible( true );
    return field.get( instance );
  }
  
} /* ENDCLASS */
