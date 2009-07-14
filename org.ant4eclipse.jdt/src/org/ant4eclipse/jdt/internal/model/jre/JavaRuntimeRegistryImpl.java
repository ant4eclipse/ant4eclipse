/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.internal.model.jre;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.jdt.JdtModelExceptionCode;
import org.ant4eclipse.jdt.model.jre.JavaProfile;
import org.ant4eclipse.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.jdt.model.jre.JavaRuntimeRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaRuntimeRegistry --
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaRuntimeRegistryImpl implements JavaRuntimeRegistry {

  /** the default java runtime key * */
  private String                         _defaultJavaRuntimeKey = null;

  /** the java runtime cache */
  private final Map<String, JavaRuntime> _javaRuntimeCache;

  /** the java profile cache */
  private final Map<String, JavaProfile> _javaProfileCache;

  /**
   * <p>
   * Creates a new instance of type {@link JavaRuntimeRegistryImpl}.
   * </p>
   */
  public JavaRuntimeRegistryImpl() {

    // create hash maps
    this._javaRuntimeCache = new HashMap<String, JavaRuntime>();
    this._javaProfileCache = new HashMap<String, JavaProfile>();

    // read all known profiles
    final JavaProfile[] javaProfiles = JavaProfileReader.readAllProfiles();

    // add profiles to profile cache
    for (final JavaProfile javaProfile : javaProfiles) {
      this._javaProfileCache.put(javaProfile.getName(), javaProfile);
    }
  }

  /**
   * @return
   */
  public JavaRuntime registerJavaRuntime(final String id, final File location, final boolean isDefault) {
    Assert.nonEmpty(id);
    Assert.isDirectory(location);

    final JavaRuntime javaRuntime = JavaRuntimeLoader.loadJavaRuntime(id, location);

    // TODO: logging

    return registerJavaRuntime(javaRuntime, isDefault);
  }

  public JavaRuntime registerJavaRuntime(final String id, final File location) {
    return registerJavaRuntime(id, location, false);
  }

  public void setDefaultJavaRuntime(final String id) {
    Assert.notNull(id);
    Assert.assertTrue(hasJavaRuntime(id), "No JavaRuntime with id '" + id + "' registered!");

    this._defaultJavaRuntimeKey = id;
  }

  /**
   * <p>
   * Returns <code>true</code> if a java runtime is registered with the given path.
   * </p>
   * 
   * @param path
   *          the path under this java runtime is stored, e.g.
   *          <code>org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/jdk15</code>
   * @return <code>true</code> if the java runtime with the given id is known.
   */
  public boolean hasJavaRuntime(final String path) {
    Assert.nonEmpty(path);

    // return true if a java runtime exists
    if (this._javaRuntimeCache.containsKey(path)) {
      return true;
    }

    // return if a java profile exists
    if (this._javaProfileCache.containsKey(path)) {
      final JavaProfile javaProfile = this._javaProfileCache.get(path);
      if (getJavaRuntime(javaProfile) != null) {
        return true;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasJavaProfile(final String path) {
    Assert.nonEmpty(path);

    return this._javaProfileCache.containsKey(path);
  }

  public JavaRuntime getJavaRuntime(final String path) {
    Assert.nonEmpty(path);

    // return true if a java runtime exists
    if (this._javaRuntimeCache.containsKey(path)) {
      return this._javaRuntimeCache.get(path);
    }

    // return if a java profile exists
    if (this._javaProfileCache.containsKey(path)) {

      final JavaProfile javaProfile = this._javaProfileCache.get(path);
      return getJavaRuntime(javaProfile);
    }

    return null;
  }

  /**
   * @param path
   * @return
   */
  public JavaProfile getJavaProfile(final String path) {
    Assert.nonEmpty(path);

    return this._javaProfileCache.get(path);
  }

  /**
   * <p>
   * 
   * </p>
   * 
   * @return
   */
  public JavaRuntime getDefaultJavaRuntime() {

    // search for default key
    if ((this._defaultJavaRuntimeKey != null) && this._javaRuntimeCache.containsKey(this._defaultJavaRuntimeKey)) {
      return this._javaRuntimeCache.get(this._defaultJavaRuntimeKey);
    }

    // TODO:
    // A4ELogging
    // .warn(
    // "No java runtime could be found for eclipse project '%s'. Possible reasons are: either there is no JRE_CONTAINER
    // specified on the classpath or there is no JavaRuntime registered for the specified JRE_CONTAINER. Trying to use
    // JRE from java.home"
    // ,
    // this._eclipseProject.getName());

    // try to create java runtime from java.home
    final JavaRuntime javaRuntime = getJavaRuntimeFromJavaHome();
    if (javaRuntime != null) {
      return javaRuntime;
    }

    // no java runtime available - throw RuntimeException
    throw new Ant4EclipseException(JdtModelExceptionCode.NO_DEFAULT_JAVA_RUNTIME_EXCEPTION);
  }

  /**
   * <p>
   * Clears the java runtime cache. The java profile cache will <b>not</b> be cleared.
   * </p>
   */
  public void clear() {
    this._javaRuntimeCache.clear();
  }

  private JavaRuntime getJavaRuntime(final JavaProfile javaProfile) {

    // result
    JavaRuntime result = null;

    final String profileName = javaProfile.getName();

    // iterate over java runtime cache
    for (final Object element : this._javaRuntimeCache.values()) {

      // get the java runtime
      final JavaRuntime javaRuntime = (JavaRuntime) element;

      if (javaRuntime.getJavaProfile().getExecutionEnvironmentNames().contains(profileName)) {

        if (result == null) {
          result = javaRuntime;
        } else {
          if (getIndex(javaRuntime, profileName) < getIndex(result, profileName)) {
            result = javaRuntime;
          }
        }
      }
    }

    return result;
  }

  private int getIndex(final JavaRuntime javaRuntime, final String profileName) {
    int index = javaRuntime.getJavaProfile().getExecutionEnvironmentNames().indexOf(profileName);
    index = javaRuntime.getJavaProfile().getExecutionEnvironmentNames().size() - index;
    return index;
  }

  /**
   * <p>
   * Registers the specified {@link JavaRuntimeImpl}.
   * </p>
   * 
   * @param javaRuntime
   *          the java runtime to specify.
   * @param isDefault
   *          indicates if the java runtime is the default runtime or not.
   * 
   * @return the path under this java runtime is stored, e.g.
   *         <code>org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/jdk15</code>
   */
  private JavaRuntime registerJavaRuntime(final JavaRuntime javaRuntime, final boolean isDefault) {
    Assert.notNull(javaRuntime);

    // create path
    final String id = javaRuntime.getId();

    if (this._javaRuntimeCache.containsKey(id)) {
      // TODO
      // throw new RuntimeException("JavaRuntime with key '" + id + "' already registered.");
      return this._javaRuntimeCache.get(id);
    }

    // store java runtime
    this._javaRuntimeCache.put(id, javaRuntime);

    // store default if necessary
    if (isDefault || (this._defaultJavaRuntimeKey == null)) {
      setDefaultJavaRuntime(id);
    }

    // return java runtime
    return javaRuntime;
  }

  /**
   * <p>
   * Tries to create a java runtime for the JRE defined under system property 'java.home'. If the system property is not
   * properly set, <code>null</code> will be returned instead.
   * </p>
   * 
   * @return
   */
  private JavaRuntime getJavaRuntimeFromJavaHome() {

    // read system property 'java.home'
    final String javaHome = System.getProperty("java.home");

    // if system property 'java.home' is not set, return null
    if (javaHome == null) {
      A4ELogging.debug("System property 'java.home' not set.");
      return null;
    }

    // create file
    final File location = new File(javaHome);

    // if location is not a directory, return null
    if (!location.isDirectory()) {
      A4ELogging.debug("Location of 'java.home' (%s) is not a directory", javaHome);
      return null;
    }

    // create new java runtime
    A4ELogging.debug("Using JRE defined in system property 'java.home' (%s)", location.getAbsolutePath());
    return JavaRuntimeLoader.loadJavaRuntime("java.home", location);
  }
}
