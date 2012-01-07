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

import org.ant4eclipse.lib.core.data.Version;

import java.io.File;

/**
 * <p>
 * An object of type {@link JavaRuntime} represents a specific java runtime environment. You can create an instanceof
 * type {@link JavaRuntime} by registering at the {@link JavaRuntimeRegistry}, e.g. <code><pre>
 * &#47;&#47; retrieve the java runtime registry
 * JavaRuntimeRegistry registry = 
 *     (JavaRuntimeRegistry) ServiceRegistry.instance().getService(JavaRuntimeRegistry.class.getName());
 *     
 * &#47;&#47; register the java runtime
 * JavaRuntime javaRuntime = registry.registerJavaRuntime("jdk15", new File("R://software//jdk//jdk15"));
 * 
 * &#47;&#47; request the java runtime
 * javaRuntime = registry.getJavaRuntime("jdk15");
 * </pre></code>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface JavaRuntime {

  /** constant for the specification version '1.0' */
  Version JAVA_SPECIFICATION_VERSION_1_0 = Version.newBundleVersion( "1.0" );

  /** constant for the specification version '1.1' */
  Version JAVA_SPECIFICATION_VERSION_1_1 = Version.newBundleVersion( "1.1" );

  /** constant for the specification version '1.2' */
  Version JAVA_SPECIFICATION_VERSION_1_2 = Version.newBundleVersion( "1.2" );

  /** constant for the specification version '1.3' */
  Version JAVA_SPECIFICATION_VERSION_1_3 = Version.newBundleVersion( "1.3" );

  /** constant for the specification version '1.4' */
  Version JAVA_SPECIFICATION_VERSION_1_4 = Version.newBundleVersion( "1.4" );

  /** constant for the specification version '1.5' */
  Version JAVA_SPECIFICATION_VERSION_1_5 = Version.newBundleVersion( "1.5" );

  /** constant for the specification version '1.6' */
  Version JAVA_SPECIFICATION_VERSION_1_6 = Version.newBundleVersion( "1.6" );

  /** constant for the specification version '1.7' */
  Version JAVA_SPECIFICATION_VERSION_1_7 = Version.newBundleVersion( "1.7" );

  /**
   * <p>
   * Returns the id of the {@link JavaRuntime}.
   * <p>
   * 
   * @return The the id of the {@link JavaRuntime}.
   */
  String getId();

  /**
   * <p>
   * Returns the location of the {@link JavaRuntime}.
   * </p>
   * 
   * @return the location of the {@link JavaRuntime}.
   */
  File getLocation();

  /**
   * <p>
   * Returns all libraries that are available on the boot class path.
   * </p>
   * 
   * @return all libraries that are available on the boot class path.
   */
  File[] getLibraries();

  /**
   * <p>
   * Returns the version of the java runtime, e.g. <code>new Version("1.4.2_14b")</code>.
   * </p>
   * 
   * @return the version of this java runtime.
   */
  Version getJavaVersion();

  /**
   * <p>
   * Returns the specification version of the java runtime, e.g. <code>new Version("1.4")</code> or
   * {@link JavaRuntime#JAVA_SPECIFICATION_VERSION_1_4}.
   * </p>
   * 
   * @return the specification version of this java runtime.
   */
  Version getSpecificationVersion();

  /**
   * <p>
   * Returns the java profile that describes this java runtime. The returned profile always perfect matches this
   * runtime.
   * </p>
   * 
   * @return the java profile that describes this java runtime.
   */
  JavaProfile getJavaProfile();
  
} /* ENDINTERFACE */
