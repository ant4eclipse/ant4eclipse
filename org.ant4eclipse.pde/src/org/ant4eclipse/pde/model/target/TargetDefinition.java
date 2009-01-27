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
package org.ant4eclipse.pde.model.target;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Represents an eclipse target definition (<code>*.target</code>).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetDefinition {

  /** name of the target definition */
  private String      _name;

  /** location */
  private Location    _location;

  /** the environment definition */
  private Environment _environment = null;

  /** the target JRE */
  private TargetJRE   _targetJRE   = null;

  /** the content definition */
  private Content     _content     = null;

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getName() {
    return _name;
  }

  /**
   * @return
   */
  public boolean isLocationSet() {
    return _location != null;
  }

  /**
   * <p>
   * Returns the location of the target definition, that may is null.
   * </p>
   * 
   * @return the location of the target definition.
   */
  public Location getLocation() {
    return _location;
  }

  /**
   * @return
   */
  public boolean isEnvironmentSet() {
    return _environment != null;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Environment getEnvironment() {
    return _environment;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean isTargetJRESet() {
    return _targetJRE != null;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public TargetJRE getTargetJRE() {
    return _targetJRE;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean isContentSet() {
    return _content != null;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Content getContent() {
    return _content;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[TargetDefinition:");
    buffer.append(" _name: ");
    buffer.append(_name);
    buffer.append(" _location: ");
    buffer.append(_location);
    buffer.append(" _environment: ");
    buffer.append(_environment);
    buffer.append(" _targetJRE: ");
    buffer.append(_targetJRE);
    buffer.append(" _content: ");
    buffer.append(_content);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_content == null) ? 0 : _content.hashCode());
    result = prime * result + ((_environment == null) ? 0 : _environment.hashCode());
    result = prime * result + ((_location == null) ? 0 : _location.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_targetJRE == null) ? 0 : _targetJRE.hashCode());
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final TargetDefinition other = (TargetDefinition) obj;
    if (_content == null) {
      if (other._content != null)
        return false;
    } else if (!_content.equals(other._content))
      return false;
    if (_environment == null) {
      if (other._environment != null)
        return false;
    } else if (!_environment.equals(other._environment))
      return false;
    if (_location == null) {
      if (other._location != null)
        return false;
    } else if (!_location.equals(other._location))
      return false;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_targetJRE == null) {
      if (other._targetJRE != null)
        return false;
    } else if (!_targetJRE.equals(other._targetJRE))
      return false;
    return true;
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   */
  void setName(String name) {
    _name = name;
  }

  /**
   * <p>
   * </p>
   * 
   * @param location
   */
  void setLocation(Location location) {
    _location = location;
  }

  /**
   * <p>
   * </p>
   * 
   * @param environment
   */
  void setEnvironment(Environment environment) {
    _environment = environment;
  }

  /**
   * <p>
   * </p>
   * 
   * @param targetJRE
   */
  void setTargetJRE(TargetJRE targetJRE) {
    _targetJRE = targetJRE;
  }

  /**
   * <p>
   * </p>
   * 
   * @param content
   */
  void setContent(Content content) {
    _content = content;
  }

  /**
   * <p>
   * </p>
   */
  public static class Location {

    /** weather to use the default location or not */
    private boolean _useDefault = false;

    /** the path to the target location */
    private String  _path;

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public boolean useDefault() {
      return _useDefault;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public boolean isPathSet() {
      return _path != null;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getPath() {
      return _path;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((_path == null) ? 0 : _path.hashCode());
      result = prime * result + (_useDefault ? 1231 : 1237);
      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Location other = (Location) obj;
      if (_path == null) {
        if (other._path != null)
          return false;
      } else if (!_path.equals(other._path))
        return false;
      if (_useDefault != other._useDefault)
        return false;
      return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Location:");
      buffer.append(" _useDefault: ");
      buffer.append(_useDefault);
      buffer.append(" _path: ");
      buffer.append(_path);
      buffer.append("]");
      return buffer.toString();
    }

    /**
     * <p>
     * </p>
     * 
     * @param useDefault
     */
    void setUseDefault(boolean useDefault) {
      _useDefault = useDefault;
    }

    /**
     * <p>
     * </p>
     * 
     * @param path
     */
    void setPath(String path) {
      _path = path;
    }
  }

  /**
   * Environment - optional
   */
  public static class Environment {

    /** operating system - optional */
    private String _os;

    /** window system - optional */
    private String _ws;

    /** architecture - optional */
    private String _arch;

    /** locale - optional */
    private String _nl;

    public String getOs() {
      return _os;
    }

    public String getWs() {
      return _ws;
    }

    public String getArch() {
      return _arch;
    }

    public String getNl() {
      return _nl;
    }

    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((_arch == null) ? 0 : _arch.hashCode());
      result = prime * result + ((_nl == null) ? 0 : _nl.hashCode());
      result = prime * result + ((_os == null) ? 0 : _os.hashCode());
      result = prime * result + ((_ws == null) ? 0 : _ws.hashCode());
      return result;
    }

    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Environment other = (Environment) obj;
      if (_arch == null) {
        if (other._arch != null)
          return false;
      } else if (!_arch.equals(other._arch))
        return false;
      if (_nl == null) {
        if (other._nl != null)
          return false;
      } else if (!_nl.equals(other._nl))
        return false;
      if (_os == null) {
        if (other._os != null)
          return false;
      } else if (!_os.equals(other._os))
        return false;
      if (_ws == null) {
        if (other._ws != null)
          return false;
      } else if (!_ws.equals(other._ws))
        return false;
      return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Environment:");
      buffer.append(" _os: ");
      buffer.append(_os);
      buffer.append(" _ws: ");
      buffer.append(_ws);
      buffer.append(" _arch: ");
      buffer.append(_arch);
      buffer.append(" _nl: ");
      buffer.append(_nl);
      buffer.append("]");
      return buffer.toString();
    }

    void setOs(String os) {
      _os = os;
    }

    void setWs(String ws) {
      _ws = ws;
    }

    void setArch(String arch) {
      _arch = arch;
    }

    void setNl(String nl) {
      _nl = nl;
    }
  }

  /**
   * TargetJRE --
   * 
   * @author admin
   */
  public static class TargetJRE {

    public static final int DEFAULT_JRE           = 0;

    public static final int JRE                   = 1;

    public static final int EXECUTION_ENVIRONMENT = 2;

    /** - */
    private String          _jreName              = null;

    /** - */
    private String          _executionEnvironment = null;

    /**
     * @return
     */
    public String getJreName() {
      return _jreName;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getExecutionEnvironment() {
      return _executionEnvironment;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public int getType() {
      if (_jreName != null) {
        return JRE;
      } else if (_executionEnvironment != null) {
        return EXECUTION_ENVIRONMENT;
      } else {
        return DEFAULT_JRE;
      }
    }

    /**
     * @param jreName
     */
    void setJreName(String jreName) {
      _jreName = jreName;
    }

    /**
     * @param execEnv
     */
    void setExecutionEnvironment(String executionEnvironment) {
      _executionEnvironment = executionEnvironment;
    }

    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((_executionEnvironment == null) ? 0 : _executionEnvironment.hashCode());
      result = prime * result + ((_jreName == null) ? 0 : _jreName.hashCode());
      return result;
    }

    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final TargetJRE other = (TargetJRE) obj;
      if (_executionEnvironment == null) {
        if (other._executionEnvironment != null)
          return false;
      } else if (!_executionEnvironment.equals(other._executionEnvironment))
        return false;
      if (_jreName == null) {
        if (other._jreName != null)
          return false;
      } else if (!_jreName.equals(other._jreName))
        return false;
      return true;
    }

  }

  /**
   * 
   */
  public static class Content {

    /** wheather all plugins should be used or not */
    private boolean      _useAllPlugins;

    /**  */
    private List<String> _plugins;

    /** */
    private List<String> _features;

    /** */
    private List<String> _extraLocations;

    /**
     * 
     */
    public Content() {
      this._plugins = new LinkedList<String>();
      this._features = new LinkedList<String>();
      this._extraLocations = new LinkedList<String>();
    }

    /**
     * @return
     */
    public boolean useAllPlugins() {
      return this._useAllPlugins;
    }

    /**
     * @return
     */
    public String[] getPlugins() {
      return (String[]) this._plugins.toArray(new String[0]);
    }

    /**
     * @return
     */
    public String[] getFeatures() {
      return (String[]) this._features.toArray(new String[0]);
    }

    /**
     * @return
     */
    public String[] getExtraLocations() {
      return (String[]) this._extraLocations.toArray(new String[0]);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((_extraLocations == null) ? 0 : _extraLocations.hashCode());
      result = prime * result + ((_features == null) ? 0 : _features.hashCode());
      result = prime * result + ((_plugins == null) ? 0 : _plugins.hashCode());
      result = prime * result + (_useAllPlugins ? 1231 : 1237);
      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Content other = (Content) obj;
      if (_extraLocations == null) {
        if (other._extraLocations != null)
          return false;
      } else if (!_extraLocations.equals(other._extraLocations))
        return false;
      if (_features == null) {
        if (other._features != null)
          return false;
      } else if (!_features.equals(other._features))
        return false;
      if (_plugins == null) {
        if (other._plugins != null)
          return false;
      } else if (!_plugins.equals(other._plugins))
        return false;
      if (_useAllPlugins != other._useAllPlugins)
        return false;
      return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Content:");
      buffer.append(" _useAllPlugins: ");
      buffer.append(_useAllPlugins);
      buffer.append(" _plugins: ");
      buffer.append(_plugins);
      buffer.append(" _features: ");
      buffer.append(_features);
      buffer.append(" _extraLocations: ");
      buffer.append(_extraLocations);
      buffer.append("]");
      return buffer.toString();
    }

    /**
     * @param useAllPlugins
     */
    void setUseAllPlugins(boolean useAllPlugins) {
      this._useAllPlugins = useAllPlugins;
    }

    /**
     * @param plugin
     */
    void addPlugin(String plugin) {
      this._plugins.add(plugin);
    }

    /**
     * @param feature
     */
    void addFeature(String feature) {
      this._features.add(feature);
    }

    /**
     * @param extraLocation
     */
    void addExtraLocation(String extraLocation) {
      this._extraLocations.add(extraLocation);
    }

  }
}
