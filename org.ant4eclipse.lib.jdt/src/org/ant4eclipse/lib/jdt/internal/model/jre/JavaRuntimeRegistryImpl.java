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
package org.ant4eclipse.lib.jdt.internal.model.jre;

import static org.ant4eclipse.lib.core.logging.A4ELogging.*;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.JdtExceptionCode;
import org.ant4eclipse.lib.jdt.model.ContainerTypes;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry;

/**
 * <p>
 * Implementation of the {@link JavaRuntimeRegistry}.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaRuntimeRegistryImpl implements JavaRuntimeRegistry {

  /** the default java runtime key * */
  private String                         _defaultJavaRuntimeKey = null;

  /** the default java runtime (lazy initialized) */
  private JavaRuntime                    _defaultJavaRuntime    = null;

  /** the java runtime cache */
  private final Map<String, JavaRuntime> _javaRuntimeCache      = new HashMap<String, JavaRuntime>();

  /**
   * <p>
   * Creates a new instance of type {@link JavaRuntimeRegistryImpl}.
   * </p>
   */
  public JavaRuntimeRegistryImpl() {
  }

  /**
   * {@inheritDoc}
   */
  public JavaRuntime registerJavaRuntime(String id, File location) {
    return registerJavaRuntime(id, location, null, null, null, null);

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry#registerJavaRuntime(java.lang.String, java.io.File,
   * java.util.List, java.lang.String)
   */
  public JavaRuntime registerJavaRuntime(String id, File location, String extDirs, String endorsedDirs,
      List<File> jreFiles, String profile) {
    Assure.nonEmpty("id", id);
    Assure.isDirectory("location", location);
    A4ELogging.info(
        "registerJavaRuntime: id = %s, location = %s, extDirs = %s, endorsedDirs = %s, jreFiles = %s, profile = %s", id,
        location, extDirs, endorsedDirs, jreFiles, profile);

    JavaRuntime javaRuntime = JavaRuntimeLoader.loadJavaRuntime(id, location, extDirs, endorsedDirs, jreFiles, profile);

    return registerJavaRuntime(javaRuntime);
  }

  /**
   * {@inheritDoc}
   */
  public void setDefaultJavaRuntime(String id) {
    Assure.notNull("id", id);
    Assure.assertTrue(hasJavaRuntime(id), "No JavaRuntime with id '" + id + "' registered!");

    this._defaultJavaRuntimeKey = id;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasJavaRuntime(String path) {
    Assure.nonEmpty("path", path);

    // return true if a java runtime exists
    if (this._javaRuntimeCache.containsKey(path)) {
      return true;
    }

    // return if a java profile exists
    JavaProfile javaProfile = JavaProfileReader.getInstance().getJavaProfile(path);
    if (javaProfile != null && getJavaRuntime(javaProfile) != null) {
      return true;
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasJavaProfile(String path) {
    Assure.nonEmpty("path", path);
    return JavaProfileReader.getInstance().hasJavaProfile(path);
  }

  public String getAllJavaProfileNames() {
    return JavaProfileReader.getInstance().getAllProfileNames();
  }

  /**
   * {@inheritDoc}
   */
  public JavaRuntime getJavaRuntime(String path) {
    Assure.nonEmpty("path", path);

    // return true if a java runtime exists
    if (this._javaRuntimeCache.containsKey(path)) {
      return this._javaRuntimeCache.get(path);
    }

    // return if a java profile exists
    JavaProfile javaProfile = JavaProfileReader.getInstance().getJavaProfile(path);
    if (javaProfile != null) {

      if (((JavaProfileImpl) javaProfile).getAssociatedJavaRuntimeId() != null) {
        return getJavaRuntime(((JavaProfileImpl) javaProfile).getAssociatedJavaRuntimeId());
      } else {
        return getJavaRuntime(javaProfile);
      }
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry#getJavaRuntimeForPath(java.lang.String)
   */
  public JavaRuntime getJavaRuntimeForPath(String path) {
    Assure.notNull("path", path);

    trace("Determining JRE for path '%s'", path);

    if (ContainerTypes.JRE_CONTAINER.equals(path)) {
      return getDefaultJavaRuntime();
    }

    JavaRuntime javaRuntime = null;

    if (path.startsWith(ContainerTypes.VMTYPE_PREFIX)) {
      javaRuntime = getJavaRuntime(path.substring(ContainerTypes.VMTYPE_PREFIX.length()));
    }

    if (javaRuntime != null) {
      return javaRuntime;
    }

    A4ELogging.warn("No java runtime '%s' defined - using default runtime.", path);

    return getDefaultJavaRuntime();
  }

  /**
   * {@inheritDoc}
   */
  public JavaProfile getJavaProfile(String path) {
    Assure.nonEmpty("path", path);
    return JavaProfileReader.getInstance().getJavaProfile(path);
  }

  /**
   * {@inheritDoc}
   */
  public JavaRuntime getDefaultJavaRuntime() {

    if (this._defaultJavaRuntime != null) {
      return this._defaultJavaRuntime;
    }

    if (this._defaultJavaRuntimeKey != null) {
      if (this._javaRuntimeCache.containsKey(this._defaultJavaRuntimeKey)) {
        this._defaultJavaRuntime = this._javaRuntimeCache.get(this._defaultJavaRuntimeKey);
        return this._defaultJavaRuntime;
      }

      // default jre was specified but does not exists. This shouldn't happen as
      // we make sure that there is a JRE with the default id in the moment
      // the default jre is set (setDefaultJavaRuntime).
      // For backward compatibility we only print a warning here.
      A4ELogging.error(
          "No default JRE configured with id '%s'. (missing or wrong <installedJREs> in your build file?). Trying to use JRE from java.home. ",
          this._defaultJavaRuntimeKey);
    }

    JavaRuntime highest = null;
    Collection<JavaRuntime> values = this._javaRuntimeCache.values();
    for (JavaRuntime javaRuntime : values) {
      if (highest == null || highest.getJavaVersion().compareTo(javaRuntime.getJavaVersion()) < 0) {
        highest = javaRuntime;
      }
    }
    if (highest != null) {
      A4ELogging.warn(
          "No default JRE has been set (using the 'default' attribute on <installedJREs> data type). Use highest JRE %s",
          highest.getJavaVersion());
      this._defaultJavaRuntime = highest;
      return highest;
    } else {
      A4ELogging.warn("No JRE has been set (using <installedJREs> data type). Trying to use JRE from java.home: %s",
          System.getProperty("java.home"));
      // try to create java runtime from java.home
      this._defaultJavaRuntime = getJavaRuntimeFromJavaHome();
      return this._defaultJavaRuntime;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param javaProfile
   * @return
   */
  private JavaRuntime getJavaRuntime(JavaProfile javaProfile) {

    // result
    JavaRuntime result = null;

    String profileName = javaProfile.getName();

    // iterate over java runtime cache
    for (Object element : this._javaRuntimeCache.values()) {

      // get the java runtime
      JavaRuntime javaRuntime = (JavaRuntime) element;

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

  /**
   * <p>
   * </p>
   * 
   * @param javaRuntime
   * @param profileName
   * @return
   */
  private int getIndex(JavaRuntime javaRuntime, String profileName) {
    int index = javaRuntime.getJavaProfile().getExecutionEnvironmentNames().indexOf(profileName);
    index = javaRuntime.getJavaProfile().getExecutionEnvironmentNames().size() - index;
    return index;
  }

  /**
   * <p>
   * Registers the specified {@link JavaRuntime}.
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
  private JavaRuntime registerJavaRuntime(JavaRuntime javaRuntime) {
    Assure.notNull("javaRuntime", javaRuntime);

    // create path
    String id = javaRuntime.getId();

    if (this._javaRuntimeCache.containsKey(id)) {
      JavaRuntime runtime = this._javaRuntimeCache.get(id);

      if (!runtime.equals(javaRuntime)) {

        // throw an exception
        // TODO
        throw new RuntimeException("Duplicate definition of JavaRuntime with key '" + id + "'.");
      }
      // return previous instance
      return this._javaRuntimeCache.get(id);
    }

    // store java runtime
    this._javaRuntimeCache.put(id, javaRuntime);

    // return java runtime
    return javaRuntime;
  }

  /**
   * <p>
   * Tries to create a java runtime for the JRE defined under system property 'java.home'. If the system property is not
   * properly set, <code>null</code> will be returned instead.
   * </p>
   * 
   * @return the {@link JavaRuntime} or <code>null</code> if no such {@link JavaRuntime} exists.
   */
  private JavaRuntime getJavaRuntimeFromJavaHome() {

    // read system property 'java.home'
    String javaHome = System.getProperty("java.home");

    // if system property 'java.home' is not set, return null
    if (javaHome == null) {
      throw new Ant4EclipseException(JdtExceptionCode.NO_DEFAULT_JAVA_RUNTIME_EXCEPTION,
          "Tried to read JRE from 'java.home' but system property 'java.home' is not set.");
    }

    // create file
    File location = new File(javaHome);

    // if location is not a directory, return null
    if (!location.isDirectory()) {
      throw new Ant4EclipseException(JdtExceptionCode.NO_DEFAULT_JAVA_RUNTIME_EXCEPTION,
          String.format("Location of 'java.home' (%s) is not a directory", javaHome));
    }

    // create new java runtime
    A4ELogging.info("Using default JRE defined in system property 'java.home' (%s)", location.getAbsolutePath());
    return JavaRuntimeLoader.loadJavaRuntime("java.home", location, null, null, null, null);
  }
}
