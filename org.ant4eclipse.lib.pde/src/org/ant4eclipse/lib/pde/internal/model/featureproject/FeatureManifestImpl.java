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
package org.ant4eclipse.lib.pde.internal.model.featureproject;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.osgi.framework.Version;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * A feature is a way of grouping and describing different functionality that makes up a product. Features do not
 * contain any code. They merely describe a set of plug-ins that provide the function for the feature and information
 * about how to update it. Features are packaged in a feature archive file and described using a feature manifest.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FeatureManifestImpl implements FeatureManifest {

  /** required feature identifier (eg. com.xyz.myfeature) */
  private String         _id;

  /** required component version (eg. 1.0.3) */
  private Version        _version;

  /** optional displayable label (name) */
  private String         label;

  /** optional display label identifying the organization providing this component */
  private String         _providerName;

  /** optional image to use when displaying information about the feature */
  private String         _image;

  /** optional operating system specification */
  private String         _operatingSystem;

  /** optional machine architecture specification */
  private String         _machineArchitecture;

  /** optional windowing system specification */
  private String         _windowingSystem;

  /** optional locale specification */
  private String         _locale;

  /** optional reference to another feature identifier used to select the default installation location for this feature */
  private String         _colocationAffinity;

  /** optional indication specifying whether this feature can be used as a primary feature */
  private boolean        _primary;

  /** optional flag that, if "true", indicates that the feature cannot be installed in a group with other features */
  private boolean        _exclusive;

  /**
   * optional identifier that represents the id of the plug-in listed in the feature that is used to carry branding
   * information for the feature
   */
  private String         _plugin;

  /**
   * optional identifier of the Eclipse application that is to be used during startup when the declaring feature is the
   * primary feature
   */
  private String         _application;

  // install-handler?

  // description?

  // copyright?

  // license?

  // url?

  private List<Includes> _includes;

  // requires?

  /** list of referenced plug-ins */
  private List<Plugin>   _plugins;

  // data*

  /**
   * <p>
   * Creates a new instance of type FeatureManifest.
   * </p>
   */
  public FeatureManifestImpl() {

    // initialize plug-in list
    this._plugins = new LinkedList<Plugin>();

    this._includes = new LinkedList<Includes>();
  }

  /**
   * {@inheritDoc}
   */
  public String getApplication() {
    return this._application;
  }

  /**
   * {@inheritDoc}
   */
  public String getColocationAffinity() {
    return this._colocationAffinity;
  }

  /**
   * @return flag that, if "true", indicates that the feature cannot be installed in a group with other features.
   */
  public boolean isExclusive() {
    return this._exclusive;
  }

  /**
   * {@inheritDoc}
   */
  public String getId() {
    return this._id;
  }

  /**
   * {@inheritDoc}
   */
  public String getImage() {
    return this._image;
  }

  /**
   * {@inheritDoc}
   */
  public String getLocale() {
    return this._locale;
  }

  /**
   * {@inheritDoc}
   */
  public String getMachineArchitecture() {
    return this._machineArchitecture;
  }

  /**
   * {@inheritDoc}
   */
  public String getOperatingSystem() {
    return this._operatingSystem;
  }

  /**
   * {@inheritDoc}
   */
  public String getPlugin() {
    return this._plugin;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPrimary() {
    return this._primary;
  }

  /**
   * @return display label identifying the organization providing this component.
   */
  public String getProviderName() {
    return this._providerName;
  }

  /**
   * @return component version (eg. 1.0.3), required
   */
  public Version getVersion() {
    return this._version;
  }

  /**
   * {@inheritDoc}
   */
  public String getWindowingSystem() {
    return this._windowingSystem;
  }

  /**
   * {@inheritDoc}
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * {@inheritDoc}
   */
  public List<Plugin> getPlugins() {
    return this._plugins;
  }

  /**
   * {@inheritDoc}
   */
  public List<Includes> getIncludes() {
    return this._includes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[Feature:");
    buffer.append(" _id: ");
    buffer.append(this._id);
    buffer.append(" _version: ");
    buffer.append(this._version);
    buffer.append(" label: ");
    buffer.append(this.label);
    buffer.append(" _providerName: ");
    buffer.append(this._providerName);
    buffer.append(" _image: ");
    buffer.append(this._image);
    buffer.append(" _operatingSystem: ");
    buffer.append(this._operatingSystem);
    buffer.append(" _machineArchitecture: ");
    buffer.append(this._machineArchitecture);
    buffer.append(" _windowingSystem: ");
    buffer.append(this._windowingSystem);
    buffer.append(" _locale: ");
    buffer.append(this._locale);
    buffer.append(" _colocationAffinity: ");
    buffer.append(this._colocationAffinity);
    buffer.append(" _primary: ");
    buffer.append(this._primary);
    buffer.append(" _exclusive: ");
    buffer.append(this._exclusive);
    buffer.append(" _application: ");
    buffer.append(this._application);
    buffer.append(" _plugin: ");
    buffer.append(this._plugin);
    buffer.append(" _plugins: ");
    buffer.append(this._plugins);
    buffer.append("]");
    return buffer.toString();
  }

  public void setApplication(String application) {
    this._application = application;
  }

  public void setColocationAffinity(String colocationAffinity) {
    this._colocationAffinity = colocationAffinity;
  }

  public void setExclusive(boolean exclusive) {
    this._exclusive = exclusive;
  }

  public void setId(String id) {
    this._id = id;
  }

  public void setImage(String image) {
    this._image = image;
  }

  public void setLocale(String locale) {
    this._locale = locale;
  }

  public void setMachineArchitecture(String machineArchitecture) {
    this._machineArchitecture = machineArchitecture;
  }

  public void setOperatingSystem(String operatingSystem) {
    this._operatingSystem = operatingSystem;
  }

  public void setPlugin(String plugin) {
    this._plugin = plugin;
  }

  public void setPrimary(boolean primary) {
    this._primary = primary;
  }

  public void setProviderName(String providerName) {
    this._providerName = providerName;
  }

  public void setVersion(Version version) {
    this._version = version;
  }

  public void setWindowingSystem(String windowingSystem) {
    this._windowingSystem = windowingSystem;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @param plugin
   */
  public void addPlugin(Plugin plugin) {
    Assure.paramNotNull("plugin", plugin);
    this._plugins.add(plugin);
  }

  public void addIncludes(Includes includes) {
    Assure.paramNotNull("includes", includes);
    this._includes.add(includes);
  }

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class PluginImpl implements Plugin {

    /** required plug-in identifier */
    private String  _id;

    /** required plug-in version */
    private Version _version;

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

    /** effective version (that is version with replaced 'qualifier') **/
    private String  _effectiveVersion;

    /**
     * <p>
     * Creates a new instance of type {@link PluginImpl}.
     * </p>
     */
    public PluginImpl() {
      super();
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getDownloadSize() {
      return this._downloadSize;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public boolean isFragment() {
      return this._fragment;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getId() {
      return this._id;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getInstallSize() {
      return this._installSize;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getLocale() {
      return this._locale;
    }

    public String getMachineArchitecture() {
      return this._machineArchitecture;
    }

    public String getOperatingSystem() {
      return this._operatingSystem;
    }

    public boolean isUnpack() {
      return this._unpack;
    }

    public Version getVersion() {
      return this._version;
    }

    public String getWindowingSystem() {
      return this._windowingSystem;
    }

    public boolean hasDownloadSize() {
      return this._downloadSize != null;
    }

    public boolean hasId() {
      return this._id != null;
    }

    public boolean hasInstallSize() {
      return this._installSize != null;
    }

    public boolean hasLocale() {
      return this._locale != null;
    }

    public boolean hasMachineArchitecture() {
      return this._machineArchitecture != null;
    }

    public boolean hasOperatingSystem() {
      return this._operatingSystem != null;
    }

    public boolean hasVersion() {
      return this._version != null;
    }

    public boolean hasWindowingSystem() {
      return this._windowingSystem != null;
    }

    public String getEffectiveVersion() {
      return this._effectiveVersion;
    }

    public void setEffectiveVersion(String effectiveVersion) {
      this._effectiveVersion = effectiveVersion;
    }

    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Plugin:");
      buffer.append(" id: ");
      buffer.append(this._id);
      buffer.append(" version: ");
      buffer.append(this._version);
      buffer.append(" fragment: ");
      buffer.append(this._fragment);
      buffer.append(" operatingSystem: ");
      buffer.append(this._operatingSystem);
      buffer.append(" machineArchitecture: ");
      buffer.append(this._machineArchitecture);
      buffer.append(" windowingSystem: ");
      buffer.append(this._windowingSystem);
      buffer.append(" locale: ");
      buffer.append(this._locale);
      buffer.append(" downloadSize: ");
      buffer.append(this._downloadSize);
      buffer.append(" installSize: ");
      buffer.append(this._installSize);
      buffer.append(" unpack: ");
      buffer.append(this._unpack);
      buffer.append("]");
      return buffer.toString();
    }

    public int hashCode() {
      int PRIME = 31;
      int result = 1;
      result = PRIME * result + ((this._id == null) ? 0 : this._id.hashCode());
      result = PRIME * result + ((this._version == null) ? 0 : this._version.hashCode());
      result = PRIME * result + (this._fragment ? 1231 : 1237);
      result = PRIME * result + ((this._operatingSystem == null) ? 0 : this._operatingSystem.hashCode());
      result = PRIME * result + ((this._machineArchitecture == null) ? 0 : this._machineArchitecture.hashCode());
      result = PRIME * result + ((this._windowingSystem == null) ? 0 : this._windowingSystem.hashCode());
      result = PRIME * result + ((this._locale == null) ? 0 : this._locale.hashCode());
      result = PRIME * result + ((this._downloadSize == null) ? 0 : this._downloadSize.hashCode());
      result = PRIME * result + ((this._installSize == null) ? 0 : this._installSize.hashCode());
      result = PRIME * result + (this._unpack ? 1231 : 1237);
      return result;
    }

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      PluginImpl other = (PluginImpl) obj;
      if (this._id == null) {
        if (other._id != null) {
          return false;
        }
      } else if (!this._id.equals(other._id)) {
        return false;
      }
      if (this._version == null) {
        if (other._version != null) {
          return false;
        }
      } else if (!this._version.equals(other._version)) {
        return false;
      }
      if (this._fragment != other._fragment) {
        return false;
      }
      if (this._operatingSystem == null) {
        if (other._operatingSystem != null) {
          return false;
        }
      } else if (!this._operatingSystem.equals(other._operatingSystem)) {
        return false;
      }
      if (this._machineArchitecture == null) {
        if (other._machineArchitecture != null) {
          return false;
        }
      } else if (!this._machineArchitecture.equals(other._machineArchitecture)) {
        return false;
      }
      if (this._windowingSystem == null) {
        if (other._windowingSystem != null) {
          return false;
        }
      } else if (!this._windowingSystem.equals(other._windowingSystem)) {
        return false;
      }
      if (this._locale == null) {
        if (other._locale != null) {
          return false;
        }
      } else if (!this._locale.equals(other._locale)) {
        return false;
      }
      if (this._downloadSize == null) {
        if (other._downloadSize != null) {
          return false;
        }
      } else if (!this._downloadSize.equals(other._downloadSize)) {
        return false;
      }
      if (this._installSize == null) {
        if (other._installSize != null) {
          return false;
        }
      } else if (!this._installSize.equals(other._installSize)) {
        return false;
      }
      if (this._unpack != other._unpack) {
        return false;
      }
      return true;
    }

    public void setDownloadSize(String downloadSize) {
      this._downloadSize = downloadSize;
    }

    public void setFragment(boolean fragment) {
      this._fragment = fragment;
    }

    public void setId(String id) {
      this._id = id;
    }

    public void setInstallSize(String installSize) {
      this._installSize = installSize;
    }

    public void setLocale(String locale) {
      this._locale = locale;
    }

    public void setMachineArchitecture(String machineArchitecture) {
      this._machineArchitecture = machineArchitecture;
    }

    public void setOperatingSystem(String operatingSystem) {
      this._operatingSystem = operatingSystem;
    }

    public void setUnpack(boolean unpack) {
      this._unpack = unpack;
    }

    public void setVersion(Version version) {
      this._version = version;
    }

    public void setWindowingSystem(String windowingSystem) {
      this._windowingSystem = windowingSystem;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   * 
   */
  public static class IncludesImpl implements Includes {

    /** required feature identifier (eg. com.xyz.myfeature) */
    private String  _id;

    /** required component version (eg. 1.0.3) */
    private Version _version;

    /** optional name */
    private String  _name;

    private boolean _optional       = false;

    private String  _searchLocation = "root";

    /** optional operating system specification */
    private String  _operatingSystem;

    /** optional machine architecture specification */
    private String  _machineArchitecture;

    /** optional windowing system specification */
    private String  _windowingSystem;

    /** optional locale specification */
    private String  _locale;

    /**
     * <p>
     * </p>
     * 
     * @return the id
     */
    public String getId() {
      return this._id;
    }

    /**
     * <p>
     * </p>
     * 
     * @param id
     *          the id to set
     */
    public void setId(String id) {
      this._id = id;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the version
     */
    public Version getVersion() {
      return this._version;
    }

    /**
     * <p>
     * </p>
     * 
     * @param version
     *          the version to set
     */
    public void setVersion(Version version) {
      this._version = version;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the name
     */
    public String getName() {
      return this._name;
    }

    /**
     * <p>
     * </p>
     * 
     * @param name
     *          the name to set
     */
    public void setName(String name) {
      this._name = name;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the optional
     */
    public boolean isOptional() {
      return this._optional;
    }

    /**
     * <p>
     * </p>
     * 
     * @param optional
     *          the optional to set
     */
    public void setOptional(boolean optional) {
      this._optional = optional;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the searchLocation
     */
    public String getSearchLocation() {
      return this._searchLocation;
    }

    /**
     * <p>
     * </p>
     * 
     * @param searchLocation
     *          the searchLocation to set
     */
    public void setSearchLocation(String searchLocation) {
      this._searchLocation = searchLocation;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the operatingSystem
     */
    public String getOperatingSystem() {
      return this._operatingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @param operatingSystem
     *          the operatingSystem to set
     */
    public void setOperatingSystem(String operatingSystem) {
      this._operatingSystem = operatingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the machineArchitecture
     */
    public String getMachineArchitecture() {
      return this._machineArchitecture;
    }

    /**
     * <p>
     * </p>
     * 
     * @param machineArchitecture
     *          the machineArchitecture to set
     */
    public void setMachineArchitecture(String machineArchitecture) {
      this._machineArchitecture = machineArchitecture;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the windowingSystem
     */
    public String getWindowingSystem() {
      return this._windowingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @param windowingSystem
     *          the windowingSystem to set
     */
    public void setWindowingSystem(String windowingSystem) {
      this._windowingSystem = windowingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the locale
     */
    public String getLocale() {
      return this._locale;
    }

    /**
     * <p>
     * </p>
     * 
     * @param locale
     *          the locale to set
     */
    public void setLocale(String locale) {
      this._locale = locale;
    }
  }
}
