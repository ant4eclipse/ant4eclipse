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

  /** the property helper */
  private Map<Thread, Properties> _propertiesMap;

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @return the {@link ThreadDispatchingPropertyHelper}
   */
  public static ThreadDispatchingPropertyHelper getInstance(Project project) {
    Assure.notNull("project", project);

    //
    PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(project).getNext();

    //
    if (propertyHelper instanceof ThreadDispatchingPropertyHelper) {
      return (ThreadDispatchingPropertyHelper) propertyHelper;
    }

    //
    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @return
   */
  public static boolean hasInstance(Project project) {
    return getInstance(project) != null;
  }

  /**
   * <p>
   * Creates a new instance of type {@link ThreadDispatchingPropertyHelper}.
   * </p>
   * 
   * @param project
   */
  public ThreadDispatchingPropertyHelper(Project project) {
    super();

    Assure.notNull("project", project);
    setProject(project);

    //
    this._propertiesMap = new HashMap<Thread, Properties>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param thread
   * @return <code>true</code>, if the given thread is registered.
   */
  public boolean isThreadRegistered(Thread thread) {

    //
    return this._propertiesMap.containsKey(thread);
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   * @return true, if the property has been removed
   */
  public boolean removeProperty(String name) {

    //
    if (isThreadRegistered(Thread.currentThread())) {
      return this._propertiesMap.get(Thread.currentThread()).remove(name) != null;
    }

    // return false
    return false;
  }

  /**
   * <p>
   * </p>
   * 
   * @param thread
   */
  public void registerThread(Thread thread) {
    Assure.notNull("thread", thread);

    //
    if (!isAnt4EclipseThread(thread)) {
      return;
    }

    //
    if (!this._propertiesMap.containsKey(thread)) {
      this._propertiesMap.put(thread, new Properties());
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param thread
   * @return
   */
  private boolean isAnt4EclipseThread(Thread thread) {
    return thread.getName().startsWith("A4E-");
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param thread
  // */
  // public void unregisterThread(Thread thread) {
  //
  // //
  // this._propertiesMap.remove(thread);
  // }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean setPropertyHook(String ns, String name, Object value, boolean inherited, boolean user, boolean isNew) {

    // if (name.startsWith("buildPlugin.newBundleVersion")) {
    // System.out.println(String.format("~~~ [%s] 1 - setPropertyHook(%s ,%s )", Thread.currentThread(), name, value));
    // }

    //
    final Thread currentThread = Thread.currentThread();

    if (!this._propertiesMap.containsKey(currentThread)) {
      return false;
    }

    //
    Properties properties = this._propertiesMap.get(currentThread);

    if (properties == null) {
      throw new IllegalStateException("_propertiesMap.containsKey(" + currentThread.getId()
          + ") returned 'true', but _propertiesMap.get() returned null");
    }

    properties.put(name, value);

    //
    // if (!inherited && user && !isNew) {
    // System.out.println(String.format("[%s] 1a - setPropertyHook(%s ,%s )", Thread.currentThread(), name, value));
    // propertyHelper.setUserProperty(ns, name, value);
    // } else if (!inherited && !user && isNew) {
    // System.out.println(String.format("[%s] 1b - setPropertyHook(%s ,%s )", Thread.currentThread(), name, value));
    // propertyHelper.setNewProperty(ns, name, value);
    // } else if (inherited && !user && !isNew) {
    // System.out.println(String.format("[%s] 1c - setPropertyHook(%s ,%s )", Thread.currentThread(), name, value));
    // propertyHelper.setInheritedProperty(ns, name, value);
    // } else if (!inherited && !user && !isNew) {
    // System.out.println(String.format("[%s] 1d - setPropertyHook(%s ,%s )", Thread.currentThread(), name, value));
    // propertyHelper.setProperty(ns, name, value, false);
    // }

    //
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getPropertyHook(String ns, String name, boolean user) {

    //
    if (!this._propertiesMap.containsKey(Thread.currentThread())) {
      return null;
    }

    //
    Properties properties = this._propertiesMap.get(Thread.currentThread());
    Object value = properties.get(name);

    // if (name.startsWith("executeProjectSet")) {
    // System.out.println(String.format("[%s] %s getPropertyHook(%s)", Thread.currentThread().hashCode(), value, name));
    // System.out.println(String.format("[%s] %s", Thread.currentThread().hashCode(), properties));
    // }

    //
    return value;
  }

  /**
   * <p>
   * </p>
   */
  public void dump() {
    Properties properties = this._propertiesMap.get(Thread.currentThread());
    System.out.println(String.format("[%s] %s", Thread.currentThread().hashCode(), properties));
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Properties getThreadProperties() {
    // System.out.println(hashCode() + " :::: " + this._propertiesMap);
    return this._propertiesMap.get(Thread.currentThread());
  }
}
