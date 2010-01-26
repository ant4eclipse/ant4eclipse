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
package org.ant4eclipse.lib.jdt.model.jre;

import org.ant4eclipse.lib.core.service.ServiceRegistry;

import java.io.File;

/**
 * <p>
 * Defines a service to register and retrieve java runtime environments.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface JavaRuntimeRegistry {

  /**
   * <p>
   * Registers the java runtime that is specified using the given location with the {@link JavaRuntimeRegistry}.
   * </p>
   * 
   * @param id
   *          the id of java runtime
   * @param location
   *          the location
   * @param isDefault
   *          indicates is the {@link JavaRuntime} should be the default one.
   * @return the {@link JavaRuntime}
   */
  JavaRuntime registerJavaRuntime(String id, File location, boolean isDefault);

  /**
   * <p>
   * Registers the java runtime that is specified using the given location with the {@link JavaRuntimeRegistry}.
   * </p>
   * 
   * @param id
   *          the id of java runtime
   * @param location
   *          the location
   * @return the {@link JavaRuntime}
   */
  JavaRuntime registerJavaRuntime(String id, File location);

  /**
   * <p>
   * Sets the default java runtime.
   * </p>
   * 
   * @param id
   *          the id of the default java runtime.
   */
  void setDefaultJavaRuntime(String id);

  /**
   * <p>
   * Returns <code>true</code> if a java runtime is registered with the given id.
   * </p>
   * 
   * @param id
   *          the id under this java runtime is stored (e.g. 'jdk15' or 'jdk16')
   * @return <code>true</code> if the java runtime with the given id is known.
   */
  boolean hasJavaRuntime(String id);

  /**
   * <p>
   * Returns the runtime with the given id.
   * </p>
   * 
   * @param id
   *          the id under this java runtime is stored (e.g. 'jdk15' or 'jdk16')
   * @return the java runtime with the given path or <code>null</code> if no such java runtime is registered.
   */
  JavaRuntime getJavaRuntime(String id);

  /**
   * <p>
   * Returns the default Java Runtime.
   * </p>
   * 
   * <p>
   * If no default java runtime is set, an exception will be thrown.
   * </p>
   * 
   * @return the default java runtime. Never null.
   */
  JavaRuntime getDefaultJavaRuntime();

  /**
   * <p>
   * Returns <code>true</code> if a java profile is registered with the given id.
   * </p>
   * 
   * @param id
   *          the id of the profile
   * @return <code>true</code> if a java profile is registered with the given id.
   */
  boolean hasJavaProfile(String id);

  /**
   * <p>
   * Returns the {@link JavaProfile} with the given id.
   * </p>
   * 
   * @param id
   *          the id of the java profile.
   * @return the {@link JavaProfile} with the given id.
   */
  JavaProfile getJavaProfile(String id);

  /**
   * <p>
   * Helper class to fetch the {@link JavaRuntimeRegistry} instance from the {@link ServiceRegistry}.
   * </p>
   */
  static class Helper {

    /**
     * <p>
     * Fetches the {@link JavaRuntimeRegistry} instance from the {@link ServiceRegistry}.
     * </p>
     * 
     * @return the registered {@link JavaRuntimeRegistry}
     */
    public static JavaRuntimeRegistry getRegistry() {
      return (JavaRuntimeRegistry) ServiceRegistry.instance().getService(JavaRuntimeRegistry.class.getName());
    }
  }
}
