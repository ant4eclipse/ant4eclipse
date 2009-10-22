package org.ant4eclipse.jdt.ecj.internal.tools.loader;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.jdt.ecj.ClassFileLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassFileLoaderCache {

  /** the class file loader map */
  private Map<Object, ClassFileLoader> _classFileLoaderMap;

  /**
   * <p>
   * Creates a new instance of type ClassFileLoaderCache.
   * </p>
   */
  public ClassFileLoaderCache() {
    this._classFileLoaderMap = new HashMap<Object, ClassFileLoader>();
  }

  public void storeClassFileLoader(Object key, ClassFileLoader classFileLoader) {
    this._classFileLoaderMap.put(key, classFileLoader);
  }

  public ClassFileLoader getClassFileLoader(Object key) {
    return this._classFileLoaderMap.get(key);
  }

  public boolean hasClassFileLoader(Object key) {
    return this._classFileLoaderMap.containsKey(key);
  }

  public static ClassFileLoaderCache getInstance() {
    return (ClassFileLoaderCache) ServiceRegistry.instance().getService(ClassFileLoaderCache.class.getName());
  }
}
