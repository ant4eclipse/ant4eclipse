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
package org.ant4eclipse.pde.model.product;

import org.osgi.framework.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Represents an eclipse product definition (<code>*.product</code>).
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProductDefinition {

  /**
   * A constant allowing to identify operating system specific values.
   */
  public static enum Os {
    /** - */
    all,
    /** - */
    win32,
    /** - */
    solaris,
    /** - */
    linux,
    /** - */
    macosx
  }

  /*
   * <configIni use="default" path="config.ini"> <win32>/org.autosar.mmt2.product/config.ini</win32> </configIni>
   * 
   * <windowImages/>
   */

  /** - */
  private String               _name;

  /** - */
  private String               _id;

  /** - */
  private String               _application;

  /** - */
  private Version              _version;

  /** - */
  private boolean              _basedonfeatures;

  /** - */
  private List<String>         _pluginids;

  /** - */
  private List<String>         _fragmentids;

  /** - */
  private Map<Os, String>      _configini;

  /** - */
  private Map<Os, String>      _programargs;

  /** - */
  private Map<Os, String>      _vmargs;

  /** - */
  private String               _launchername;

  /** - */
  private Map<Os, String>      _vm;

  /** - */
  private String               _splashplugin;

  /** - */
  private Map<String, Version> _features;

  /**
   * Initialises this data structure.
   */
  public ProductDefinition() {
    this._pluginids = new ArrayList<String>();
    this._fragmentids = new ArrayList<String>();
    this._name = null;
    this._id = null;
    this._application = null;
    this._version = null;
    this._basedonfeatures = false;
    this._configini = new Hashtable<Os, String>();
    this._programargs = new Hashtable<Os, String>();
    this._vmargs = new Hashtable<Os, String>();
    this._launchername = null;
    this._vm = new Hashtable<Os, String>();
    this._splashplugin = null;
    this._features = new Hashtable<String, Version>();
  }

  /**
   * Adds a feature with a specified version to this product definition.
   * 
   * @param featureid
   *          The id of the feature. Neither <code>null</code> nor empty.
   * @param version
   *          The version of the associated feature. Not <code>null</code>.
   */
  void addFeature(String featureid, Version version) {
    this._features.put(featureid, version);
  }

  /**
   * Returns a list of all currently registered feature ids.
   * 
   * @return A list of all currently registered feature ids. Not <code>null</code>.
   */
  public String[] getFeatureIds() {
    String[] result = this._features.keySet().toArray(new String[this._features.size()]);
    Arrays.sort(result);
    return result;
  }

  /**
   * Returns the version associated with the supplied feature.
   * 
   * @param feature
   *          The id of the feature used to get the version from. Neither <code>null</code> nor empty.
   * 
   * @return The version of the supplied version. <code>null</code> if the feature is not valid.
   */
  public Version getFeatureVersion(String feature) {
    return this._features.get(feature);
  }

  /**
   * Changes the id of the plugin providing the splash screen.
   * 
   * @param pluginid
   *          The id of the plugin providing the splash screen.
   */
  void setSplashplugin(String pluginid) {
    this._splashplugin = pluginid;
  }

  /**
   * Returns the id of the plugin providing the splash screen.
   * 
   * @return The id of the plugin providing the splash screen. Maybe <code>null</code>.
   */
  public String getSplashplugin() {
    return this._splashplugin;
  }

  /**
   * Changes the name of the launcher.
   * 
   * @param newlaunchername
   *          The new name of the launcher. Neither <code>null</code> nor empty.
   */
  void setLaunchername(String newlaunchername) {
    this._launchername = newlaunchername;
  }

  /**
   * Returns the current name of the launcher.
   * 
   * @return The current name of the launcher.
   */
  public String getLaunchername() {
    return this._launchername;
  }

  /**
   * Registers the required vm for a specific os.
   * 
   * @param os
   *          The associated operating system. Not <code>null</code>.
   * @param vm
   *          The required vm for this operating system. If not <code>null</code> it must be non empty.
   */
  void addVm(Os os, String vm) {
    if (vm != null) {
      this._vm.put(os, vm.trim());
    }
  }

  /**
   * Returns the required vm for the supplied operating system.
   * 
   * @param os
   *          The operating system used to access the required vm. Not <code>null</code>.
   * 
   * @return The required vm or <code>null</code> if none has been specified.
   */
  public String getVm(Os os) {
    return this._vm.get(os);
  }

  /**
   * Registers a set of vm arguments for a specific os.
   * 
   * @param os
   *          The associated operating system. Not <code>null</code>.
   * @param args
   *          The vm arguments for this operating system. If not <code>null</code> it must be non empty.
   */
  void addVmArgs(Os os, String args) {
    if (args != null) {
      this._vmargs.put(os, args.replace('\n', ' ').trim());
    }
  }

  /**
   * Returns the vm arguments used for the supplied operating system.
   * 
   * @param os
   *          The operating system used to access the vm arguments. Not <code>null</code>.
   * 
   * @return The vm arguments or <code>null</code> if none has been specified.
   */
  public String getVmArgs(Os os) {
    return this._vmargs.get(os);
  }

  /**
   * Returns the vm arguments used for all operating systems.
   * 
   * @return The vm arguments or <code>null</code> if none has been specified.
   */
  public String getVmArgs() {
    return getVmArgs(Os.all);
  }

  /**
   * Registers a set of program arguments for a specific os.
   * 
   * @param os
   *          The associated operating system. Not <code>null</code>.
   * @param args
   *          The program arguments for this operating system. If not <code>null</code> it must be non empty.
   */
  void addProgramArgs(Os os, String args) {
    if (args != null) {
      this._programargs.put(os, args.replace('\n', ' ').trim());
    }
  }

  /**
   * Returns the program arguments used for the supplied operating system.
   * 
   * @param os
   *          The operating system used to access the program arguments. Not <code>null</code>.
   * 
   * @return The program arguments or <code>null</code> if none has been specified.
   */
  public String getProgramArgs(Os os) {
    return this._programargs.get(os);
  }

  /**
   * Returns the program arguments used for all operating systems.
   * 
   * @return The program arguments or <code>null</code> if none has been specified.
   */
  public String getProgramArgs() {
    return getProgramArgs(Os.all);
  }

  /**
   * Registers a config ini path for a specific os. The path is always workspace relative which means it begins with a
   * leading slash.
   * 
   * @param os
   *          The associated operating system. Not <code>null</code>.
   * @param path
   *          The path to be used for this operating system. If not <code>null</code> it must be non empty.
   */
  void addConfigIni(Os os, String path) {
    if (path != null) {
      this._configini.put(os, path.trim());
    }
  }

  /**
   * Returns a config.ini path used for the supplied operating system. The path is always workspace relative which means
   * it begins with a leading slash.
   * 
   * @param os
   *          The operating system used to access the path. Not <code>null</code>.
   * 
   * @return The config.ini path or <code>null</code> if none has been specified.
   */
  public String getConfigIni(Os os) {
    return this._configini.get(os);
  }

  /**
   * Registers the supplied plugin id.
   * 
   * @param pluginid
   *          The id of the included plugin. Neither <code>null</code> nor empty.
   * @param isfragment
   *          <code>true</code> <=> The plugin is a framgent.
   */
  void addPlugin(String pluginid, boolean isfragment) {
    if (isfragment) {
      this._fragmentids.add(pluginid);
    } else {
      this._pluginids.add(pluginid);
    }
  }

  /**
   * Returns a list of all currently registered plugin ids.
   * 
   * @return A list of all currently registered plugin ids. Not <code>null</code>.
   */
  public String[] getPluginIds() {
    return this._pluginids.toArray(new String[this._pluginids.size()]);
  }

  /**
   * Returns a list of all currently registered fragment ids.
   * 
   * @return A list of all currently registered fragment ids. Not <code>null</code>.
   */
  public String[] getFragmentIds() {
    return this._fragmentids.toArray(new String[this._fragmentids.size()]);
  }

  /**
   * Changes the name for this product.
   * 
   * @param newname
   *          The new name for this product. Neither <code>null</code> nor empty.
   */
  void setName(String newname) {
    this._name = newname;
  }

  /**
   * Returns the name for this product.
   * 
   * @return The name for this product. Neither <code>null</code> nor empty.
   */
  public String getName() {
    return this._name;
  }

  /**
   * Changes the id for this product.
   * 
   * @param newid
   *          The new id for this product. Neither <code>null</code> nor empty.
   */
  void setId(String newid) {
    this._id = newid;
  }

  /**
   * Returns the id for this product.
   * 
   * @return The id for this product. Neither <code>null</code> nor empty.
   */
  public String getId() {
    return this._id;
  }

  /**
   * Changes the application id associated with this product.
   * 
   * @param newapplication
   *          The new application id. Neither <code>null</code> nor empty.
   */
  void setApplication(String newapplication) {
    this._application = newapplication;
  }

  /**
   * Returns the id of the product application.
   * 
   * @return The id of the product application. Neither <code>null</code> nor empty.
   */
  public String getApplication() {
    return this._application;
  }

  /**
   * Changes the version for this product.
   * 
   * @param newversion
   *          The new version for this product. Not <code>null</code>.
   */
  void setVersion(Version newversion) {
    this._version = newversion;
  }

  /**
   * Returns the version of the product.
   * 
   * @return The version of the product. Not <code>null</code>.
   */
  public Version getVersion() {
    return this._version;
  }

  /**
   * (Un)Marks this product as based on features.
   * 
   * @param newbasedonfeatures
   *          <code>true</code> <=> This product is based on features.
   */
  void setBasedOnFeatures(boolean newbasedonfeatures) {
    this._basedonfeatures = newbasedonfeatures;
  }

  /**
   * Returns <code>true</code> if this product is based on features rather than plugins.
   * 
   * @return <code>true</code> <=> This product is based on features.
   */
  public boolean isBasedOnFeatures() {
    return this._basedonfeatures;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ProductDefinition:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(", _id: ");
    buffer.append(this._id);
    buffer.append(", _application: ");
    buffer.append(this._application);
    buffer.append(", _version: ");
    buffer.append(this._version);
    buffer.append(", _launchername: ");
    buffer.append(this._launchername);
    buffer.append(", _splashplugin: ");
    buffer.append(this._splashplugin);
    buffer.append(", _basedonfeatures: ");
    buffer.append(this._basedonfeatures);
    if (this._basedonfeatures) {
      String[] featureids = getFeatureIds();
      buffer.append(", _features: {");
      if (featureids.length > 0) {
        buffer.append(featureids[0]);
        buffer.append("=");
        buffer.append(getFeatureVersion(featureids[0]));
        for (int i = 1; i < featureids.length; i++) {
          buffer.append(",");
          buffer.append(featureids[i]);
          buffer.append("=");
          buffer.append(getFeatureVersion(featureids[i]));
        }
      }
      buffer.append("}");
    } else {
      buffer.append(", _pluginids: {");
      if (!this._pluginids.isEmpty()) {
        buffer.append(this._pluginids.get(0));
        for (int i = 1; i < this._pluginids.size(); i++) {
          buffer.append(",");
          buffer.append(this._pluginids.get(i));
        }
      }
      buffer.append("}");
      buffer.append(", _fragmentids: {");
      if (!this._fragmentids.isEmpty()) {
        buffer.append(this._fragmentids.get(0));
        for (int i = 1; i < this._fragmentids.size(); i++) {
          buffer.append(",");
          buffer.append(this._fragmentids.get(i));
        }
      }
      buffer.append("}");
    }
    buffer.append(", _configini: {");
    boolean first = true;
    for (Os os : Os.values()) {
      String configini = getConfigIni(os);
      if (configini != null) {
        if (!first) {
          buffer.append(", ");
        }
        buffer.append(os);
        buffer.append("=");
        buffer.append(configini);
        first = false;
      }
    }
    buffer.append("}");
    buffer.append(", _programargs: {");
    first = true;
    for (Os os : Os.values()) {
      String programargs = getProgramArgs(os);
      if (programargs != null) {
        if (!first) {
          buffer.append(", ");
        }
        buffer.append(os);
        buffer.append("=");
        buffer.append(programargs);
        first = false;
      }
    }
    buffer.append("}");
    buffer.append(", _vmargs: {");
    first = true;
    for (Os os : Os.values()) {
      String vmargs = getVmArgs(os);
      if (vmargs != null) {
        if (!first) {
          buffer.append(", ");
        }
        buffer.append(os);
        buffer.append("=");
        buffer.append(vmargs);
        first = false;
      }
    }
    buffer.append("}");
    buffer.append(", _vm: {");
    first = true;
    for (Os os : Os.values()) {
      String vm = getVm(os);
      if (vm != null) {
        if (!first) {
          buffer.append(", ");
        }
        buffer.append(os);
        buffer.append("=");
        buffer.append(vm);
        first = false;
      }
    }
    buffer.append("}");
    buffer.append("]");
    return buffer.toString();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((this._name == null) ? 0 : this._name.hashCode());
    result = 31 * result + ((this._id == null) ? 0 : this._id.hashCode());
    result = 31 * result + ((this._application == null) ? 0 : this._application.hashCode());
    result = 31 * result + ((this._version == null) ? 0 : this._version.hashCode());
    result = 31 * result + ((this._launchername == null) ? 0 : this._launchername.hashCode());
    result = 31 * result + ((this._splashplugin == null) ? 0 : this._splashplugin.hashCode());
    result = 31 * result + (this._basedonfeatures ? 1 : 0);
    String[] featureids = getFeatureIds();
    for (String featureid : featureids) {
      result = 31 * result + featureid.hashCode();
      Version version = getFeatureVersion(featureid);
      result = 31 * result + version.hashCode();
    }
    for (int i = 0; i < this._pluginids.size(); i++) {
      result = 31 * result + this._pluginids.get(i).hashCode();
    }
    for (int i = 0; i < this._fragmentids.size(); i++) {
      result = 31 * result + this._fragmentids.get(i).hashCode();
    }
    for (Os os : Os.values()) {
      String configini = getConfigIni(os);
      result = 31 * result + (configini == null ? 0 : configini.hashCode());
    }
    for (Os os : Os.values()) {
      String programargs = getProgramArgs(os);
      result = 31 * result + (programargs == null ? 0 : programargs.hashCode());
    }
    for (Os os : Os.values()) {
      String vmargs = getVmArgs(os);
      result = 31 * result + (vmargs == null ? 0 : vmargs.hashCode());
    }
    for (Os os : Os.values()) {
      String vm = getVm(os);
      result = 31 * result + (vm == null ? 0 : vm.hashCode());
    }
    return result;
  }

} /* ENDCLASS */
