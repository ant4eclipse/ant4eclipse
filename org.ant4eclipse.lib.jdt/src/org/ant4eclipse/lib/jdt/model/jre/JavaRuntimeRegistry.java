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

import org.ant4eclipse.lib.core.A4EService;

import java.io.File;
import java.util.List;

/**
 * <p>
 * Defines a service to register and retrieve java runtime environments.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface JavaRuntimeRegistry extends A4EService {

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
  JavaRuntime registerJavaRuntime( String id, File location, boolean isDefault );

  /**
   * <p>
   * Registers the java runtime that is specified using the given location and the specified files with the
   * {@link JavaRuntimeRegistry}.
   * </p>
   * <p>
   * If <b>jreFiles</b> is <tt>null</tt>, the files for the jre are automatically determined
   * 
   * @param id
   * @param location
   * @param jreFiles
   *          the jreFiles that define the jre or null
   * @return
   */
  JavaRuntime registerJavaRuntime( String id, File location, List<File> jreFiles );

  /**
   * <p>
   * Registers the java runtime that is specified using the given location with the {@link JavaRuntimeRegistry}.
   * </p>
   * 
   * <p>
   * Same as {@link #registerJavaRuntime(String, File, List<File>) registerJavaRuntime(String, File, null)}
   * 
   * @param id
   *          the id of java runtime
   * @param location
   *          the location
   * @return the {@link JavaRuntime}
   * 
   */
  JavaRuntime registerJavaRuntime( String id, File location );

  /**
   * <p>
   * Sets the default java runtime.
   * </p>
   * 
   * @param id
   *          the id of the default java runtime.
   */
  void setDefaultJavaRuntime( String id );

  /**
   * <p>
   * Returns <code>true</code> if a java runtime is registered with the given id.
   * </p>
   * 
   * @param id
   *          the id under this java runtime is stored (e.g. 'jdk15' or 'jdk16')
   * @return <code>true</code> if the java runtime with the given id is known.
   */
  boolean hasJavaRuntime( String id );

  /**
   * <p>
   * Returns the runtime with the given id.
   * </p>
   * 
   * @param id
   *          the id under this java runtime is stored (e.g. 'jdk15' or 'jdk16')
   * @return the java runtime with the given path or <code>null</code> if no such java runtime is registered.
   */
  JavaRuntime getJavaRuntime( String id );

  /**
   * Returns the java runtime for the given path (i.e.
   * 'org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5')
   * 
   * @param path
   *          The path
   * @return The matching runtime. In case no runtime can be discovered, the default runtime is returned. If no default
   *         runtime is set, an Exception is thrown
   */
  JavaRuntime getJavaRuntimeForPath( String path );

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
  boolean hasJavaProfile( String id );

  /**
   * <p>
   * Returns the {@link JavaProfile} with the given id.
   * </p>
   * 
   * @param id
   *          the id of the java profile.
   * @return the {@link JavaProfile} with the given id.
   */
  JavaProfile getJavaProfile( String id );

} /* ENDINTERFACE */
