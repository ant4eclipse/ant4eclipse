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

import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.osgi.framework.Version;

import java.util.ArrayList;
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
    _plugins = new ArrayList<Plugin>();
    _includes = new ArrayList<Includes>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getApplication() {
    return _application;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getColocationAffinity() {
    return _colocationAffinity;
  }

  /**
   * @return flag that, if "true", indicates that the feature cannot be installed in a group with other features.
   */
  @Override
  public boolean isExclusive() {
    return _exclusive;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return _id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getImage() {
    return _image;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLocale() {
    return _locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMachineArchitecture() {
    return _machineArchitecture;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getOperatingSystem() {
    return _operatingSystem;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlugin() {
    return _plugin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPrimary() {
    return _primary;
  }

  /**
   * @return display label identifying the organization providing this component.
   */
  @Override
  public String getProviderName() {
    return _providerName;
  }

  /**
   * @return component version (eg. 1.0.3), required
   */
  @Override
  public Version getVersion() {
    return _version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWindowingSystem() {
    return _windowingSystem;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLabel() {
    return label;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Plugin> getPlugins() {
    return _plugins;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Includes> getIncludes() {
    return _includes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
      return String.format(
          "[Feature: _id: %s _version: %s label: %s _providerName: %s _image: %s _operatingSystem: %s _machineArchitecture: %s _windowingSystem: %s _locale: %s _colocationAffinity: %s _primary: %s _exclusive: %s _application: %s _plugin: %s _plugins: %s]",
         _id, _version, label, _providerName, _image, _operatingSystem, _machineArchitecture, _windowingSystem, _locale, _colocationAffinity, _primary, _exclusive, _application, _plugin, _plugins );
  }

  public void setApplication( String application ) {
    _application = application;
  }

  public void setColocationAffinity( String colocationAffinity ) {
    _colocationAffinity = colocationAffinity;
  }

  public void setExclusive( boolean exclusive ) {
    _exclusive = exclusive;
  }

  public void setId( String id ) {
    _id = id;
  }

  public void setImage( String image ) {
    _image = image;
  }

  public void setLocale( String locale ) {
    _locale = locale;
  }

  public void setMachineArchitecture( String machineArchitecture ) {
    _machineArchitecture = machineArchitecture;
  }

  public void setOperatingSystem( String operatingSystem ) {
    _operatingSystem = operatingSystem;
  }

  public void setPlugin( String plugin ) {
    _plugin = plugin;
  }

  public void setPrimary( boolean primary ) {
    _primary = primary;
  }

  public void setProviderName( String providerName ) {
    _providerName = providerName;
  }

  public void setVersion( Version version ) {
    _version = version;
  }

  public void setWindowingSystem( String windowingSystem ) {
    _windowingSystem = windowingSystem;
  }

  public void setLabel( String newlabel ) {
    label = newlabel;
  }

  /**
   * @param plugin
   */
  // Assure.notNull( "plugin", plugin );
  public void addPlugin( Plugin plugin ) {
    _plugins.add( plugin );
  }

  // Assure.notNull( "includes", includes );
  public void addIncludes( Includes includes ) {
    _includes.add( includes );
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
    @Override
    public String getDownloadSize() {
      return _downloadSize;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    @Override
    public boolean isFragment() {
      return _fragment;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    @Override
    public String getId() {
      return _id;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    @Override
    public String getInstallSize() {
      return _installSize;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    @Override
    public String getLocale() {
      return _locale;
    }

    @Override
    public String getMachineArchitecture() {
      return _machineArchitecture;
    }

    @Override
    public String getOperatingSystem() {
      return _operatingSystem;
    }

    @Override
    public boolean isUnpack() {
      return _unpack;
    }

    @Override
    public Version getVersion() {
      return _version;
    }

    @Override
    public String getWindowingSystem() {
      return _windowingSystem;
    }

    @Override
    public boolean hasDownloadSize() {
      return _downloadSize != null;
    }

    @Override
    public boolean hasId() {
      return _id != null;
    }

    @Override
    public boolean hasInstallSize() {
      return _installSize != null;
    }

    @Override
    public boolean hasLocale() {
      return _locale != null;
    }

    @Override
    public boolean hasMachineArchitecture() {
      return _machineArchitecture != null;
    }

    @Override
    public boolean hasOperatingSystem() {
      return _operatingSystem != null;
    }

    @Override
    public boolean hasVersion() {
      return _version != null;
    }

    @Override
    public boolean hasWindowingSystem() {
      return _windowingSystem != null;
    }

    @Override
    public String getEffectiveVersion() {
      return _effectiveVersion;
    }

    @Override
    public void setEffectiveVersion( String effectiveVersion ) {
      _effectiveVersion = effectiveVersion;
    }

    @Override
    public String toString() {
        return
            String.format( "[Plugin: id: %s version: %s fragment: %s operatingSystem: %s machineArchitecture: %s windowingSystem: %s locale: %s downloadSize: %s installSize: %s unpack: %s]",
            _id, _version, _fragment, _operatingSystem, _machineArchitecture, _windowingSystem, _locale,
            _downloadSize, _installSize, _unpack);
    }

    @Override
    public int hashCode() {
      int PRIME = 31;
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

    @Override
    public boolean equals( Object obj ) {
      if( this == obj ) {
        return true;
      }
      if( obj == null ) {
        return false;
      }
      if( getClass() != obj.getClass() ) {
        return false;
      }
      PluginImpl other = (PluginImpl) obj;
      if( _id == null ) {
        if( other._id != null ) {
          return false;
        }
      } else if( !_id.equals( other._id ) ) {
        return false;
      }
      if( _version == null ) {
        if( other._version != null ) {
          return false;
        }
      } else if( !_version.equals( other._version ) ) {
        return false;
      }
      if( _fragment != other._fragment ) {
        return false;
      }
      if( _operatingSystem == null ) {
        if( other._operatingSystem != null ) {
          return false;
        }
      } else if( !_operatingSystem.equals( other._operatingSystem ) ) {
        return false;
      }
      if( _machineArchitecture == null ) {
        if( other._machineArchitecture != null ) {
          return false;
        }
      } else if( !_machineArchitecture.equals( other._machineArchitecture ) ) {
        return false;
      }
      if( _windowingSystem == null ) {
        if( other._windowingSystem != null ) {
          return false;
        }
      } else if( !_windowingSystem.equals( other._windowingSystem ) ) {
        return false;
      }
      if( _locale == null ) {
        if( other._locale != null ) {
          return false;
        }
      } else if( !_locale.equals( other._locale ) ) {
        return false;
      }
      if( _downloadSize == null ) {
        if( other._downloadSize != null ) {
          return false;
        }
      } else if( !_downloadSize.equals( other._downloadSize ) ) {
        return false;
      }
      if( _installSize == null ) {
        if( other._installSize != null ) {
          return false;
        }
      } else if( !_installSize.equals( other._installSize ) ) {
        return false;
      }
      if( _unpack != other._unpack ) {
        return false;
      }
      return true;
    }

    public void setDownloadSize( String downloadSize ) {
      _downloadSize = downloadSize;
    }

    public void setFragment( boolean fragment ) {
      _fragment = fragment;
    }

    public void setId( String id ) {
      _id = id;
    }

    public void setInstallSize( String installSize ) {
      _installSize = installSize;
    }

    public void setLocale( String locale ) {
      _locale = locale;
    }

    public void setMachineArchitecture( String machineArchitecture ) {
      _machineArchitecture = machineArchitecture;
    }

    public void setOperatingSystem( String operatingSystem ) {
      _operatingSystem = operatingSystem;
    }

    public void setUnpack( boolean unpack ) {
      _unpack = unpack;
    }

    public void setVersion( Version version ) {
      _version = version;
    }

    public void setWindowingSystem( String windowingSystem ) {
      _windowingSystem = windowingSystem;
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
    @Override
    public String getId() {
      return _id;
    }

    /**
     * <p>
     * </p>
     * 
     * @param id
     *          the id to set
     */
    public void setId( String id ) {
      _id = id;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the version
     */
    @Override
    public Version getVersion() {
      return _version;
    }

    /**
     * <p>
     * </p>
     * 
     * @param version
     *          the version to set
     */
    public void setVersion( Version version ) {
      _version = version;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the name
     */
    @Override
    public String getName() {
      return _name;
    }

    /**
     * <p>
     * </p>
     * 
     * @param name
     *          the name to set
     */
    public void setName( String name ) {
      _name = name;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the optional
     */
    @Override
    public boolean isOptional() {
      return _optional;
    }

    /**
     * <p>
     * </p>
     * 
     * @param optional
     *          the optional to set
     */
    public void setOptional( boolean optional ) {
      _optional = optional;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the searchLocation
     */
    @Override
    public String getSearchLocation() {
      return _searchLocation;
    }

    /**
     * <p>
     * </p>
     * 
     * @param searchLocation
     *          the searchLocation to set
     */
    public void setSearchLocation( String searchLocation ) {
      _searchLocation = searchLocation;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the operatingSystem
     */
    @Override
    public String getOperatingSystem() {
      return _operatingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @param operatingSystem
     *          the operatingSystem to set
     */
    public void setOperatingSystem( String operatingSystem ) {
      _operatingSystem = operatingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the machineArchitecture
     */
    @Override
    public String getMachineArchitecture() {
      return _machineArchitecture;
    }

    /**
     * <p>
     * </p>
     * 
     * @param machineArchitecture
     *          the machineArchitecture to set
     */
    public void setMachineArchitecture( String machineArchitecture ) {
      _machineArchitecture = machineArchitecture;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the windowingSystem
     */
    @Override
    public String getWindowingSystem() {
      return _windowingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @param windowingSystem
     *          the windowingSystem to set
     */
    public void setWindowingSystem( String windowingSystem ) {
      _windowingSystem = windowingSystem;
    }

    /**
     * <p>
     * </p>
     * 
     * @return the locale
     */
    @Override
    public String getLocale() {
      return _locale;
    }

    /**
     * <p>
     * </p>
     * 
     * @param locale
     *          the locale to set
     */
    public void setLocale( String locale ) {
      _locale = locale;
    }
  }
  
} /* ENDCLASS */
