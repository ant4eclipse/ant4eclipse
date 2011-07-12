package org.ant4eclipse.lib.jdt.ecj.internal.tools.loader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.jdt.ecj.ClassFileLoader;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann
 */
public class ClassFileLoaderCache {

  /**
   * System-Property that allows disabling the ClassFileLoaderCache
   */
  private static final boolean         DISABLE_CACHE = Boolean.getBoolean("ant4eclipse.disableClassFileLoaderCache");

  /**
   * System-Property that enables tracing of the cache. <b>This is very verbose!</b>
   */
  private static final boolean         TRACE_CACHE   = Boolean.getBoolean("ant4eclipse.traceClassFileLoaderCache");

  /** the class file loader map */
  private Map<Object, ClassFileLoader> _classFileLoaderMap;

  /**
   * Hit counter
   */
  private int                          _hits         = 0;

  /**
   * Miss counter
   */
  private int                          _missed       = 0;

  /**
   * <p>
   * Creates a new instance of type ClassFileLoaderCache.
   * </p>
   */
  public ClassFileLoaderCache() {
    this._classFileLoaderMap = new HashMap<Object, ClassFileLoader>();
    System.out.println("ClassFileLoaderCache is " + (DISABLE_CACHE ? "" : "not ") + "disabled");
  }

  public void storeClassFileLoader(Object key, ClassFileLoader classFileLoader) {
    if (!DISABLE_CACHE) {
      if (TRACE_CACHE) {
        A4ELogging.debug("Store ClassFileLoader in cache for: '" + key + "' -> " + classFileLoader);
        A4ELogging.debug("  Packages: " + Arrays.asList(classFileLoader.getAllPackages()));
      }
    this._classFileLoaderMap.put(key, classFileLoader);
  }
  }

  public ClassFileLoader getClassFileLoader(Object key) {
    ClassFileLoader classFileLoader = this._classFileLoaderMap.get(key);
    if (classFileLoader != null) {
      this._hits++;
      if (!DISABLE_CACHE && TRACE_CACHE) {
        A4ELogging.debug("Got ClassFileLoader from cache for: " + key);
      }
    } else {
      this._missed++;
      if (!DISABLE_CACHE && TRACE_CACHE) {
        A4ELogging.debug("Missed ClassFileLoader in cache for: " + key);
      }
    }
    return classFileLoader;
  }

  public boolean hasClassFileLoader(Object key) {
    return this._classFileLoaderMap.containsKey(key);
  }

  public static ClassFileLoaderCache getInstance() {
    return ServiceRegistryAccess.instance().getService(ClassFileLoaderCache.class);
  }

  /**
   * Dumps the current content and hit statistics of the ClassFileLoaderCache via A4ELogging
   */
  public void dump() {
    if (DISABLE_CACHE) {
      A4ELogging.info("ClassFileLoaderCache has been disabled. Anyway there have been " + (this._missed + this._hits)
          + " calls to the cache.");
    } else {
      A4ELogging.info("ClassFileLoaderCache contains " + this._classFileLoaderMap.size() + " entries.");
      A4ELogging.info("There has been " + this._hits + " hits and " + this._missed + " misses");
      for (Map.Entry<Object, ClassFileLoader> entry : this._classFileLoaderMap.entrySet()) {
        A4ELogging.info("  " + entry.getKey() + " -> " + entry.getValue());
        A4ELogging.info("  Packages: " + Arrays.asList(entry.getValue().getAllPackages()));
      }
    }
  }
}
