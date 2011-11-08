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
    if (!this._propertiesMap.containsKey(Thread.currentThread())) {
      return false;
    }

    //
    Properties properties = this._propertiesMap.get(Thread.currentThread());
    properties.put(name, value);

    if (name.startsWith("buildPlugin.newBundleVersion")) {
      System.out.println(String.format("[%s] setPropertyHook(%s ,%s ) - %s", Thread.currentThread(), name, value,
          properties));
    }

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
