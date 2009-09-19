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

import org.ant4eclipse.core.util.ExtendedProperties;

import java.util.List;

/**
 * <p>
 * Represents a java profile. A java profile is a symbolic representations of a JRE. For example, rather than talking
 * about a specific JRE, with a specific name at a specific location on your disk, you can talk about the J2SE-1.4 java
 * profile. The system can then be configured to use a specific JRE to implement that java profile.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface JavaProfile {

  /** the constant for the java profile 'JavaSE-1.6' */
  String JAVA_PROFILE_JavaSE_1_6             = "JavaSE-1.6";

  /** the constant for the java profile 'J2SE-1.5' */
  String JAVA_PROFILE_J2SE_1_5               = "J2SE-1.5";

  /** the constant for the java profile 'J2SE-1.4' */
  String JAVA_PROFILE_J2SE_1_4               = "J2SE-1.4";

  /** the constant for the java profile 'J2SE-1.3' */
  String JAVA_PROFILE_J2SE_1_3               = "J2SE-1.3";

  /** the constant for the java profile 'J2SE-1.2' */
  String JAVA_PROFILE_J2SE_1_2               = "J2SE-1.2";

  /** the constant for the java profile 'JRE-1.1' */
  String JAVA_PROFILE_JRE_1_1                = "JRE-1.1";

  /** the constant for the java profile 'CDC-1.1_Foundation-1.1' */
  String JAVA_PROFILE_CDC_1_1_Foundation_1_1 = "CDC-1.1_Foundation-1.1";

  /** the constant for the java profile 'CDC-1.0_Foundation-1.0' */
  String JAVA_PROFILE_CDC_1_0_Foundation_1_0 = "CDC-1.0_Foundation-1.0";

  /** the constant for the java profile 'OSGi_Minimum-1.0' */
  String JAVA_PROFILE_OSGi_Minimum_1_0       = "OSGi_Minimum-1.0";

  /** the constant for the java profile 'OSGi_Minimum-1.1' */
  String JAVA_PROFILE_OSGi_Minimum_1_1       = "OSGi_Minimum-1.1";

  /**
   * <p>
   * Returns the name of the java profile, e.g. <code>J2SE-1.5</code> or {@link JavaProfile#JAVA_PROFILE_J2SE_1_5}.
   * </p>
   * 
   * @return the name of the java profile.
   */
  String getName();

  /**
   * <p>
   * Returns a list of execution environment names that this java profile is a super set of (e.g.
   * <code>[OSGi/Minimum-1.0, OSGi/Minimum-1.1, JRE-1.1, J2SE-1.2]</code> ).
   * </p>
   * 
   * @return a list of execution environment names that this java profile is a super set of.
   */
  List<String> getExecutionEnvironmentNames();

  /**
   * <p>
   * Returns all specified system packages.
   * </p>
   * 
   * @return
   */
  List<String> getSystemPackages();

  /**
   * <p>
   * Returns the java profile as a properties object. The following properties are defined:
   * <ul>
   * <li><code>org.osgi.framework.system.packages</code></li>
   * <li><code>org.osgi.framework.bootdelegation</code></li>
   * <li><code>org.osgi.framework.executionenvironment</code></li>
   * <li><code>osgi.java.profile.name</code></li>
   * </ul>
   * </p>
   * 
   * @return the java profile as properties.
   */
  ExtendedProperties getProperties();

  /**
   * <p>
   * Returns <code>true</code>, if the specified package name denotes a system package.
   * </p>
   * 
   * @param packageName
   *          the package name
   * 
   * @return <code>true</code>, if the specified package name denotes a system package, <code>false</code> otherwise.
   */
  boolean isSystemPackage(String packageName);

  /**
   * <p>
   * Returns <code>true</code>, if the specified package name is must be delegated to the boot class loader.
   * </p>
   * 
   * @param packageName
   *          the package name
   * 
   * @return <code>true</code>, if the specified package name is must be delegated to the boot class loader.
   */
  boolean isDelegatedToBootClassLoader(String packageName);
}