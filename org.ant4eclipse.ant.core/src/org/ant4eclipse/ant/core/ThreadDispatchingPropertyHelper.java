package org.ant4eclipse.ant.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.ant4eclipse.lib.core.Assure;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ThreadDispatchingPropertyHelper extends PropertyHelper {

  private static final String MSG_SET_PROPERTY_HOOK = "[%s] setPropertyHook(%s ,%s ) - %s";
  private static final String PROP_NEWBUNDLEVERSION = "buildPlugin.newBundleVersion";
  
  private Map<Thread,Properties> propertiesmap;

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @return the {@link ThreadDispatchingPropertyHelper}
   */
  public static ThreadDispatchingPropertyHelper getInstance( Project project ) {
    Assure.notNull( "project", project );

    PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper( project ).getNext();

    if( propertyHelper instanceof ThreadDispatchingPropertyHelper ) {
      return (ThreadDispatchingPropertyHelper) propertyHelper;
    }

    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @return
   */
  public static boolean hasInstance( Project project ) {
    return getInstance( project ) != null;
  }

  /**
   * <p>
   * Creates a new instance of type {@link ThreadDispatchingPropertyHelper}.
   * </p>
   * 
   * @param project
   */
  public ThreadDispatchingPropertyHelper( Project project ) {
    Assure.notNull( "project", project );
    setProject( project );
    propertiesmap = new HashMap<Thread,Properties>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param thread
   * @return <code>true</code>, if the given thread is registered.
   */
  public boolean isThreadRegistered( Thread thread ) {
    return propertiesmap.containsKey( thread );
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   * @return true, if the property has been removed
   */
  public boolean removeProperty( String name ) {

    if( isThreadRegistered( Thread.currentThread() ) ) {
      return propertiesmap.get( Thread.currentThread() ).remove( name ) != null;
    }

    return false;
  }

  /**
   * <p>
   * </p>
   * 
   * @param thread
   */
  public void registerThread( Thread thread ) {
    Assure.notNull( "thread", thread );
    if( !isAnt4EclipseThread( thread ) ) {
      return;
    }

    if( ! propertiesmap.containsKey( thread ) ) {
      propertiesmap.put( thread, new Properties() );
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param thread
   * @return
   */
  private boolean isAnt4EclipseThread( Thread thread ) {
    return thread.getName().startsWith( "A4E-" );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean setPropertyHook( String ns, String name, Object value, boolean inherited, boolean user, boolean isNew ) {

    if( ! propertiesmap.containsKey( Thread.currentThread() ) ) {
      return false;
    }

    Properties properties = propertiesmap.get( Thread.currentThread() );
    properties.put( name, value );

    if( name.startsWith( PROP_NEWBUNDLEVERSION ) ) {
      System.out.println( String.format( MSG_SET_PROPERTY_HOOK, Thread.currentThread(), name, value, properties ) );
    }
    
    return true;
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getPropertyHook( String ns, String name, boolean user ) {

    if( ! propertiesmap.containsKey( Thread.currentThread() ) ) {
      return null;
    }

    Properties properties = propertiesmap.get( Thread.currentThread() );
    return  properties.get( name );
    
  }

  /**
   * <p>
   * </p>
   */
  public void dump() {
    Properties properties = propertiesmap.get( Thread.currentThread() );
    System.out.println( String.format( "[%s] %s", Thread.currentThread().hashCode(), properties ) );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Properties getThreadProperties() {
    return propertiesmap.get( Thread.currentThread() );
  }
  
} /* ENDCLASS */
