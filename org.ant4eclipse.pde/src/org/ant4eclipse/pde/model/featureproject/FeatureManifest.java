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
package org.ant4eclipse.pde.model.featureproject;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.osgi.framework.Version;

/**
 * <p>
 * A feature is a way of grouping and describing different functionality that makes up a product. Features do not
 * contain any code. They merely describe a set of plug-ins that provide the function for the feature and information
 * about how to update it. Features are packaged in a feature archive file and described using a feature manifest.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FeatureManifest {

  /** required feature identifier (eg. com.xyz.myfeature) */
  private String  _id;

  /** required component version (eg. 1.0.3) */
  private Version _version;

  /** optional displayable label (name) */
  private String  label;

  /**
   * optional display label identifying the organization providing this component
   */
  private String  _providerName;

  /** optional image to use when displaying information about the feature */
  private String  _image;

  /** optional operating system specification */
  private String  _operatingSystem;

  /** optional machine architecture specification */
  private String  _machineArchitecture;

  /** optional windowing system specification */
  private String  _windowingSystem;

  /** optional locale specification */
  private String  _locale;

  /**
   * optional reference to another feature identifier used to select the default installation location for this feature
   */
  private String  _colocationAffinity;

  /**
   * optional indication specifying whether this feature can be used as a primary feature
   */
  private boolean _primary;

  /**
   * optional flag that, if "true", indicates that the feature cannot be installed in a group with other features
   */
  private boolean _exclusive;

  /**
   * optional identifier of the Eclipse application that is to be used during startup when the declaring feature is the
   * primary feature
   */
  private String  _application;

  /**
   * optional identifier that represents the id of the plug-in listed in the feature that is used to carry branding
   * information for the feature
   */
  private String  _plugin;

  /** list of referenced plugins */
  private List    _plugins;

  /**
   * Creates a new instance of type Feature.
   */
  public FeatureManifest() {
    _plugins = new LinkedList();
  }

  /**
   * Returns the name of the application.
   *
   * @return The name of the application.
   */
  public String getApplication() {
    return _application;
  }

  /**
   * Returns a reference to another feature.
   *
   * @return A reference to another feature.
   */
  public String getColocationAffinity() {
    return _colocationAffinity;
  }

  /**
   * @return flag that, if "true", indicates that the feature cannot be installed in a group with other features.
   */
  public boolean isExclusive() {
    return _exclusive;
  }

  /**
   * @return feature identifier (eg. com.xyz.myfeature), required
   */
  public String getId() {
    return _id;
  }

  /**
   * Returns the location of an associated image.
   *
   * @return The location of an associated image.
   */
  public String getImage() {
    return _image;
  }

  /**
   * @return locale specification.
   */
  public String getLocale() {
    return _locale;
  }

  /**
   * @return optional machine architecture specification.
   */
  public String getMachineArchitecture() {
    return _machineArchitecture;
  }

  /**
   * @return operating system specification.
   */
  public String getOperatingSystem() {
    return _operatingSystem;
  }

  /**
   * Returns the plugin used for this feature.
   *
   * @return The plugin used for this feature.
   */
  public String getPlugin() {
    return _plugin;
  }

  /**
   * Returns true if this feature is a primary one.
   *
   * @return true <=> This feature is a primary one.
   */
  public boolean isPrimary() {
    return _primary;
  }

  /**
   * @return display label identifying the organization providing this component.
   */
  public String getProviderName() {
    return _providerName;
  }

  /**
   * @return component version (eg. 1.0.3), required
   */
  public Version getVersion() {
    return _version;
  }

  /**
   * Returns the name of the windowing system.
   *
   * @return The name of the windowing system.
   */
  public String getWindowingSystem() {
    return _windowingSystem;
  }

  /**
   * @return displayable label (name).
   */
  public String getLabel() {
    return label;
  }

  /**
   * @return all the referenced plugins.
   */
  public Plugin[] getPlugins() {
    return (Plugin[]) _plugins.toArray(new Plugin[0]);
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[Feature:");
    buffer.append(" _id: ");
    buffer.append(_id);
    buffer.append(" _version: ");
    buffer.append(_version);
    buffer.append(" label: ");
    buffer.append(label);
    buffer.append(" _providerName: ");
    buffer.append(_providerName);
    buffer.append(" _image: ");
    buffer.append(_image);
    buffer.append(" _operatingSystem: ");
    buffer.append(_operatingSystem);
    buffer.append(" _machineArchitecture: ");
    buffer.append(_machineArchitecture);
    buffer.append(" _windowingSystem: ");
    buffer.append(_windowingSystem);
    buffer.append(" _locale: ");
    buffer.append(_locale);
    buffer.append(" _colocationAffinity: ");
    buffer.append(_colocationAffinity);
    buffer.append(" _primary: ");
    buffer.append(_primary);
    buffer.append(" _exclusive: ");
    buffer.append(_exclusive);
    buffer.append(" _application: ");
    buffer.append(_application);
    buffer.append(" _plugin: ");
    buffer.append(_plugin);
    buffer.append(" _plugins: ");
    buffer.append(_plugins);
    buffer.append("]");
    return buffer.toString();
  }

  void setApplication(String application) {
    _application = application;
  }

  void setColocationAffinity(String colocationAffinity) {
    _colocationAffinity = colocationAffinity;
  }

  void setExclusive(boolean exclusive) {
    _exclusive = exclusive;
  }

  void setId(String id) {
    _id = id;
  }

  void setImage(String image) {
    _image = image;
  }

  void setLocale(String locale) {
    _locale = locale;
  }

  void setMachineArchitecture(String machineArchitecture) {
    _machineArchitecture = machineArchitecture;
  }

  void setOperatingSystem(String operatingSystem) {
    _operatingSystem = operatingSystem;
  }

  void setPlugin(String plugin) {
    _plugin = plugin;
  }

  void setPrimary(boolean primary) {
    _primary = primary;
  }

  void setProviderName(String providerName) {
    _providerName = providerName;
  }

  void setVersion(Version version) {
    _version = version;
  }

  void setWindowingSystem(String windowingSystem) {
    _windowingSystem = windowingSystem;
  }

  void setLabel(String label) {
    this.label = label;
  }

  /**
   * @param plugin
   */
  void addPlugin(Plugin plugin) {
    Assert.notNull(plugin);

    _plugins.add(plugin);
  }

  public static class Plugin {
    /** required plug-in identifier */
    private String  _id;

    /** required plug-in version */
    private Version _version;
    
    /** effective version (that is version with replaced 'qualifier') **/
    private String _effectiveVersion;

    /** optional specification indicating if this entry is a plug-in fragment */
    private boolean _fragment = false;

    /** optional operating system specification */
    private String  _operatingSystem;

    /** optional machine architecture specification */
    private String  _machineArchitecture;

    /** optional windowing system specification */
    private String  _windowingSystem;

    /** optional locale specification */
    private String  _locale;

    /**
     * optional hint supplied by the feature packager, indicating the download size in KBytes of the referenced plug-in
     * archive
     */
    private String  _downloadSize;

    /**
     * optional hint supplied by the feature packager, indicating the install size in KBytes of the referenced plug-in
     * archive
     */
    private String  _installSize;

    /**
     * optional specification supplied by the feature packager, indicating that plugin is capable of running from a jar,
     * and contents of plug-in jar should not be unpacked into a directory
     */
    private boolean _unpack   = true;

    /**
     * Creates a new instance of type Plugin.
     */
    public Plugin() {
      super();
    }

    public String getDownloadSize() {
      return _downloadSize;
    }

    public boolean isFragment() {
      return _fragment;
    }

    public String getId() {
      return _id;
    }

    public String getInstallSize() {
      return _installSize;
    }

    public String getLocale() {
      return _locale;
    }

    public String getMachineArchitecture() {
      return _machineArchitecture;
    }

    public String getOperatingSystem() {
      return _operatingSystem;
    }

    public boolean isUnpack() {
      return _unpack;
    }

    public Version getVersion() {
      return _version;
    }

    public String getWindowingSystem() {
      return _windowingSystem;
    }

    public boolean hasDownloadSize() {
      return _downloadSize != null;
    }

    public boolean hasId() {
      return _id != null;
    }

    public boolean hasInstallSize() {
      return _installSize != null;
    }

    public boolean hasLocale() {
      return _locale != null;
    }

    public boolean hasMachineArchitecture() {
      return _machineArchitecture != null;
    }

    public boolean hasOperatingSystem() {
      return _operatingSystem != null;
    }

    public boolean hasVersion() {
      return _version != null;
    }

    public boolean hasWindowingSystem() {
      return _windowingSystem != null;
    }
    
    public String getEffectiveVersion() {
      return _effectiveVersion;
    }

    public void setEffectiveVersion(String effectiveVersion) {
      _effectiveVersion = effectiveVersion;
    }

    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Plugin:");
      buffer.append(" id: ");
      buffer.append(_id);
      buffer.append(" version: ");
      buffer.append(_version);
      buffer.append(" fragment: ");
      buffer.append(_fragment);
      buffer.append(" operatingSystem: ");
      buffer.append(_operatingSystem);
      buffer.append(" machineArchitecture: ");
      buffer.append(_machineArchitecture);
      buffer.append(" windowingSystem: ");
      buffer.append(_windowingSystem);
      buffer.append(" locale: ");
      buffer.append(_locale);
      buffer.append(" downloadSize: ");
      buffer.append(_downloadSize);
      buffer.append(" installSize: ");
      buffer.append(_installSize);
      buffer.append(" unpack: ");
      buffer.append(_unpack);
      buffer.append("]");
      return buffer.toString();
    }

    public int hashCode() {
      final int PRIME = 31;
      int result = 1;
      result = PRIME * result + ((_id == null) ? 0 : _id.hashCode());
      result = PRIME * result + ((_version == null) ? 0 : _version.hashCode());
      result = PRIME * result + (_fragment ? 1231 : 1237);
      result = PRIME * result + ((_operatingSystem == null) ? 0 : _operatingSystem.hashCode());
      result = PRIME * result + ((_machineArchitecture == null) ? 0 : _machineArchitecture.hashCode());
      result = PRIME * result + ((_windowingSystem == null) ? 0 : _windowingSystem.hashCode());
      result = PRIME * result + ((_locale == null) ? 0 : _locale.hashCode());
      result = PRIME * result + ((_downloadSize == null) ? 0 : _downloadSize.hashCode());
      result = PRIME * result + ((_installSize == null) ? 0 : _installSize.hashCode());
      result = PRIME * result + (_unpack ? 1231 : 1237);
      return result;
    }

    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Plugin other = (Plugin) obj;
      if (_id == null) {
        if (other._id != null)
          return false;
      } else if (!_id.equals(other._id))
        return false;
      if (_version == null) {
        if (other._version != null)
          return false;
      } else if (!_version.equals(other._version))
        return false;
      if (_fragment != other._fragment)
        return false;
      if (_operatingSystem == null) {
        if (other._operatingSystem != null)
          return false;
      } else if (!_operatingSystem.equals(other._operatingSystem))
        return false;
      if (_machineArchitecture == null) {
        if (other._machineArchitecture != null)
          return false;
      } else if (!_machineArchitecture.equals(other._machineArchitecture))
        return false;
      if (_windowingSystem == null) {
        if (other._windowingSystem != null)
          return false;
      } else if (!_windowingSystem.equals(other._windowingSystem))
        return false;
      if (_locale == null) {
        if (other._locale != null)
          return false;
      } else if (!_locale.equals(other._locale))
        return false;
      if (_downloadSize == null) {
        if (other._downloadSize != null)
          return false;
      } else if (!_downloadSize.equals(other._downloadSize))
        return false;
      if (_installSize == null) {
        if (other._installSize != null)
          return false;
      } else if (!_installSize.equals(other._installSize))
        return false;
      if (_unpack != other._unpack)
        return false;
      return true;
    }

    void setDownloadSize(String downloadSize) {
      _downloadSize = downloadSize;
    }

    void setFragment(boolean fragment) {
      _fragment = fragment;
    }

    void setId(String id) {
      _id = id;
    }

    void setInstallSize(String installSize) {
      _installSize = installSize;
    }

    void setLocale(String locale) {
      _locale = locale;
    }

    void setMachineArchitecture(String machineArchitecture) {
      _machineArchitecture = machineArchitecture;
    }

    void setOperatingSystem(String operatingSystem) {
      _operatingSystem = operatingSystem;
    }

    void setUnpack(boolean unpack) {
      _unpack = unpack;
    }

    void setVersion(Version version) {
      _version = version;
    }

    void setWindowingSystem(String windowingSystem) {
      _windowingSystem = windowingSystem;
    }
  }
}
