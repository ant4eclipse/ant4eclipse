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
import org.ant4eclipse.core.util.ManifestHelper;
import org.ant4eclipse.core.util.ManifestHelper.ManifestHeaderElement;

import org.ant4eclipse.jdt.model.jre.JavaProfile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A JRE profile contains values for the properties <code>org.osgi.framework.system.packages</code>,
 * <code>org.osgi.framework.bootdelegation</code> and <code>org.osgi.framework.executionenvironment</code>.
 * 
 * <ul>
 * <li><b>org.osgi.framework.system.packages:</b> The system property <code>org.osgi.framework.system.packages</code>
 * contains the export packages descriptions for the system bundle. This property employs the standard Export-Package
 * manifest header syntax: <br/>
 * <br/>
 * 
 * <code>org.osgi.framework.system.packages ::= package-description ( ',' package-description )* </code><br/>
 * 
 * </li>
 * <li><b>org.osgi.framework.bootdelegation:</b></li>
 * <li><b>org.osgi.framework.executionenvironment:</b></li>
 * <li><b>osgi.java.profile.name:</b></li>
 * </ul>
 * </p>
 * 
 * @author admin
 */
public class JavaProfileImpl implements JavaProfile {

  /** - */
  private static final String PROPERTY_SYSTEM_PACKAGES        = "org.osgi.framework.system.packages";

  /** - */
  private static final String PROPERTY_BOOTDELEGATION         = "org.osgi.framework.bootdelegation";

  /** - */
  private static final String PROPERTY_EXECUTIONENVIRONMENT   = "org.osgi.framework.executionenvironment";

  /** - */
  private static final String PROPERTY_PROFILE_NAME           = "osgi.java.profile.name";

  /** the java profile properties */
  private Map<String, String> _properties;

  /** the list of system packages */
  private List<String>        _systemPackagesList             = new LinkedList<String>();

  /** the list of packages that are delegated to the boot class loader */
  private List<PackageFilter> _delegatedToBootClassLoaderList = new LinkedList<PackageFilter>();

  private List<String>        _executionEnvironments          = new LinkedList<String>();

  /**
   * @param properties
   */
  public JavaProfileImpl(Map<String, String> properties) {
    Assert.notNull(properties);

    this._properties = properties;

    initialise();
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return this._properties.get(JavaProfileImpl.PROPERTY_PROFILE_NAME);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isSystemPackage(String packageName) {
    return this._systemPackagesList.contains(packageName);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDelegatedToBootClassLoader(String packageName) {

    for (Object element : this._delegatedToBootClassLoaderList) {
      PackageFilter packageFilter = (PackageFilter) element;
      if (packageFilter.containsPackage(packageName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getExecutionEnvironmentNames() {
    return Collections.unmodifiableList(this._executionEnvironments);
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getSystemPackages() {
    return Collections.unmodifiableList(this._systemPackagesList);
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, String> getProperties() {
    return this._properties;
  }

  /**
   * 
   */
  private void initialise() {

    // set up system packages list...
    if (isNotEmpty(this._properties.get(PROPERTY_SYSTEM_PACKAGES))) {

      // get HeaderElements
      ManifestHelper.ManifestHeaderElement[] headerElements = ManifestHelper.getManifestHeaderElements(this._properties
          .get(PROPERTY_SYSTEM_PACKAGES));

      // iterate over result
      for (ManifestHeaderElement headerElement : headerElements) {
        // get values (package names)
        String[] packageNames = headerElement.getValues();

        // add package names to string
        for (String packageName : packageNames) {
          this._systemPackagesList.add(packageName);
        }
      }
    }

    if (isNotEmpty(this._properties.get(PROPERTY_BOOTDELEGATION))) {
      String[] packageDescriptions = this._properties.get(PROPERTY_BOOTDELEGATION).split(",");
      for (String packageDescription : packageDescriptions) {
        this._delegatedToBootClassLoaderList.add(new PackageFilter(packageDescription));
      }
    }

    if (isNotEmpty(this._properties.get(PROPERTY_EXECUTIONENVIRONMENT))) {
      String[] executionEnvironments = this._properties.get(PROPERTY_EXECUTIONENVIRONMENT).split(",");
      for (String executionEnvironment : executionEnvironments) {
        this._executionEnvironments.add(executionEnvironment);
      }
    }
  }

  private boolean isNotEmpty(String string) {
    return (string != null) && !"".equals(string.trim());
  }

  /**
   * PackageFilter --
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class PackageFilter {

    /** - */
    private String[] _includedPackages;

    /**
     * @param includedPackages
     */
    public PackageFilter(String includedPackages) {

      // set default value
      this._includedPackages = includedPackages == null ? new String[] {} : includedPackages.split(",");

      //
      for (int i = 0; i < this._includedPackages.length; i++) {

        // replace OSGi wild cards (*) with regular expressions (.+)
        // NOTE: we're interpreting a * as "one or more characters here"
        // otherwise we have to use the regex ".*" which means "zero or more
        // characters here"
        this._includedPackages[i] = this._includedPackages[i].replaceAll("\\*", ".+");
      }
    }

    /**
     * <p>
     * </p>
     * 
     * @param packageName
     * @return
     */
    public boolean containsPackage(String packageName) {
      Assert.notNull(packageName);

      //
      for (String package1 : this._includedPackages) {
        if (matches(package1.trim(), packageName)) {
          return true;
        }
      }

      // no match - return false
      return false;
    }

    /**
     * <p>
     * </p>
     * 
     * @param osgiPattern
     * @param string
     * 
     * @return
     */
    private boolean matches(String osgiPattern, String string) {
      Assert.notNull(osgiPattern);
      Assert.notNull(string);

      return string.matches(osgiPattern);
    }
  }

  /**
   * @generated by CodeSugar http://sourceforge.net/projects/codesugar
   */

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[JavaProfile:");
    buffer.append(" _properties: ");
    buffer.append(this._properties);
    buffer.append("]");
    return buffer.toString();
  }
}
