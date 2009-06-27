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
package org.ant4eclipse.jdt.model.jre;

import org.ant4eclipse.core.service.*;

import java.io.*;

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
   * @param id
   * @param location
   * @param isDefault
   * @return
   */
  JavaRuntime registerJavaRuntime(final String id, final File location, final boolean isDefault);

  /**
   * @param id
   * @param location
   * @return
   */
  JavaRuntime registerJavaRuntime(final String id, final File location);

  /**
   * @param id
   */
  void setDefaultJavaRuntime(final String id);

  /**
   * <p>
   * Returns <code>true</code> if a java runtime is registered with the given path.
   * </p>
   * 
   * @param id
   *          the id
   * @return <code>true</code> if the java runtime with the given id is known.
   */
  boolean hasJavaRuntime(final String id);

  /**
   * <p>
   * Returns the runtime with the given path.
   * </p>
   * 
   * @param id
   *          the id
   * @return the java runtime with the given path or <code>null</code> if no such java runtime is registered.
   */
  JavaRuntime getJavaRuntime(final String id);

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
   * @param id
   * @return
   */
  boolean hasJavaProfile(final String id);

  /**
   * @param id
   * @return
   */
  JavaProfile getJavaProfile(final String id);

  /**
   * <p>
   * Clears the java runtime cache. The java profile cache will <b>not</b> be cleared.
   * </p>
   */
  void clear();

  /**
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
