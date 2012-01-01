package org.ant4eclipse.lib.jdt.internal.tools.container;

import java.util.Hashtable;
import java.util.Map;

import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtResolverCache {

  /** the class path cache */
  private static final Map<String, ResolvedClasspath> _classpathCache = new Hashtable<String, ResolvedClasspath>();

  /**
   * <p>
   * </p>
   * 
   * @param key
   * @return
   */
  public ResolvedClasspath getResolvedClasspath(String key) {
    return _classpathCache.get(key);
  }

  /**
   * <p>
   * </p>
   * 
   * @param key
   * @param classpath
   */
  public void storeResolvedClasspath(String key, ResolvedClasspath classpath) {
    _classpathCache.put(key, classpath);
  }

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @param resolveRelative
   * @param runtimeClasspath
   * @return
   */
  public static String getCacheKey(EclipseProject project, boolean resolveRelative, boolean runtimeClasspath) {
    // TODO include classpathContainerArguments in key
    return project.getSpecifiedName() + "." + resolveRelative + "." + runtimeClasspath;
  }

}
