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
package org.ant4eclipse.lib.pde.model.target;

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
    return this._name;
  }

  /**
   * @return
   */
  public boolean isLocationSet() {
    return this._location != null;
  }

  /**
   * <p>
   * Returns the location of the target definition, that may is null.
   * </p>
   * 
   * @return the location of the target definition.
   */
  public Location getLocation() {
    return this._location;
  }

  /**
   * @return
   */
  public boolean isEnvironmentSet() {
    return this._environment != null;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Environment getEnvironment() {
    return this._environment;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean isTargetJRESet() {
    return this._targetJRE != null;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public TargetJRE getTargetJRE() {
    return this._targetJRE;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean isContentSet() {
    return this._content != null;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Content getContent() {
    return this._content;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[TargetDefinition:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(" _location: ");
    buffer.append(this._location);
    buffer.append(" _environment: ");
    buffer.append(this._environment);
    buffer.append(" _targetJRE: ");
    buffer.append(this._targetJRE);
    buffer.append(" _content: ");
    buffer.append(this._content);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + ((this._content == null) ? 0 : this._content.hashCode());
    result = prime * result + ((this._environment == null) ? 0 : this._environment.hashCode());
    result = prime * result + ((this._location == null) ? 0 : this._location.hashCode());
    result = prime * result + ((this._name == null) ? 0 : this._name.hashCode());
    result = prime * result + ((this._targetJRE == null) ? 0 : this._targetJRE.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
    TargetDefinition other = (TargetDefinition) obj;
    if (this._content == null) {
      if (other._content != null) {
        return false;
      }
    } else if (!this._content.equals(other._content)) {
      return false;
    }
    if (this._environment == null) {
      if (other._environment != null) {
        return false;
      }
    } else if (!this._environment.equals(other._environment)) {
      return false;
    }
    if (this._location == null) {
      if (other._location != null) {
        return false;
      }
    } else if (!this._location.equals(other._location)) {
      return false;
    }
    if (this._name == null) {
      if (other._name != null) {
        return false;
      }
    } else if (!this._name.equals(other._name)) {
      return false;
    }
    if (this._targetJRE == null) {
      if (other._targetJRE != null) {
        return false;
      }
    } else if (!this._targetJRE.equals(other._targetJRE)) {
      return false;
    }
    return true;
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   */
  void setName(String name) {
    this._name = name;
  }

  /**
   * <p>
   * </p>
   * 
   * @param location
   */
  void setLocation(Location location) {
    this._location = location;
  }

  /**
   * <p>
   * </p>
   * 
   * @param environment
   */
  void setEnvironment(Environment environment) {
    this._environment = environment;
  }

  /**
   * <p>
   * </p>
   * 
   * @param targetJRE
   */
  void setTargetJRE(TargetJRE targetJRE) {
    this._targetJRE = targetJRE;
  }

  /**
   * <p>
   * </p>
   * 
   * @param content
   */
  void setContent(Content content) {
    this._content = content;
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
      return this._useDefault;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public boolean isPathSet() {
      return this._path != null;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getPath() {
      return this._path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      int prime = 31;
      int result = 1;
      result = prime * result + ((this._path == null) ? 0 : this._path.hashCode());
      result = prime * result + (this._useDefault ? 1231 : 1237);
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
      Location other = (Location) obj;
      if (this._path == null) {
        if (other._path != null) {
          return false;
        }
      } else if (!this._path.equals(other._path)) {
        return false;
      }
      if (this._useDefault != other._useDefault) {
        return false;
      }
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Location:");
      buffer.append(" _useDefault: ");
      buffer.append(this._useDefault);
      buffer.append(" _path: ");
      buffer.append(this._path);
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
      this._useDefault = useDefault;
    }

    /**
     * <p>
     * </p>
     * 
     * @param path
     */
    void setPath(String path) {
      this._path = path;
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
      return this._os;
    }

    public String getWs() {
      return this._ws;
    }

    public String getArch() {
      return this._arch;
    }

    public String getNl() {
      return this._nl;
    }

    @Override
    public int hashCode() {
      int prime = 31;
      int result = 1;
      result = prime * result + ((this._arch == null) ? 0 : this._arch.hashCode());
      result = prime * result + ((this._nl == null) ? 0 : this._nl.hashCode());
      result = prime * result + ((this._os == null) ? 0 : this._os.hashCode());
      result = prime * result + ((this._ws == null) ? 0 : this._ws.hashCode());
      return result;
    }

    @Override
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
      Environment other = (Environment) obj;
      if (this._arch == null) {
        if (other._arch != null) {
          return false;
        }
      } else if (!this._arch.equals(other._arch)) {
        return false;
      }
      if (this._nl == null) {
        if (other._nl != null) {
          return false;
        }
      } else if (!this._nl.equals(other._nl)) {
        return false;
      }
      if (this._os == null) {
        if (other._os != null) {
          return false;
        }
      } else if (!this._os.equals(other._os)) {
        return false;
      }
      if (this._ws == null) {
        if (other._ws != null) {
          return false;
        }
      } else if (!this._ws.equals(other._ws)) {
        return false;
      }
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Environment:");
      buffer.append(" _os: ");
      buffer.append(this._os);
      buffer.append(" _ws: ");
      buffer.append(this._ws);
      buffer.append(" _arch: ");
      buffer.append(this._arch);
      buffer.append(" _nl: ");
      buffer.append(this._nl);
      buffer.append("]");
      return buffer.toString();
    }

    void setOs(String os) {
      this._os = os;
    }

    void setWs(String ws) {
      this._ws = ws;
    }

    void setArch(String arch) {
      this._arch = arch;
    }

    void setNl(String nl) {
      this._nl = nl;
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
      return this._jreName;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getExecutionEnvironment() {
      return this._executionEnvironment;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public int getType() {
      if (this._jreName != null) {
        return JRE;
      } else if (this._executionEnvironment != null) {
        return EXECUTION_ENVIRONMENT;
      } else {
        return DEFAULT_JRE;
      }
    }

    /**
     * @param jreName
     */
    void setJreName(String jreName) {
      this._jreName = jreName;
    }

    /**
     * @param execEnv
     */
    void setExecutionEnvironment(String executionEnvironment) {
      this._executionEnvironment = executionEnvironment;
    }

    @Override
    public int hashCode() {
      int prime = 31;
      int result = 1;
      result = prime * result + ((this._executionEnvironment == null) ? 0 : this._executionEnvironment.hashCode());
      result = prime * result + ((this._jreName == null) ? 0 : this._jreName.hashCode());
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
      TargetJRE other = (TargetJRE) obj;
      if (this._executionEnvironment == null) {
        if (other._executionEnvironment != null) {
          return false;
        }
      } else if (!this._executionEnvironment.equals(other._executionEnvironment)) {
        return false;
      }
      if (this._jreName == null) {
        if (other._jreName != null) {
          return false;
        }
      } else if (!this._jreName.equals(other._jreName)) {
        return false;
      }
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
      return this._plugins.toArray(new String[0]);
    }

    /**
     * @return
     */
    public String[] getFeatures() {
      return this._features.toArray(new String[0]);
    }

    /**
     * @return
     */
    public String[] getExtraLocations() {
      return this._extraLocations.toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
      int prime = 31;
      int result = 1;
      result = prime * result + ((this._extraLocations == null) ? 0 : this._extraLocations.hashCode());
      result = prime * result + ((this._features == null) ? 0 : this._features.hashCode());
      result = prime * result + ((this._plugins == null) ? 0 : this._plugins.hashCode());
      result = prime * result + (this._useAllPlugins ? 1231 : 1237);
      return result;
    }

    /**
     * {@inheritDoc}
     */
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
      Content other = (Content) obj;
      if (this._extraLocations == null) {
        if (other._extraLocations != null) {
          return false;
        }
      } else if (!this._extraLocations.equals(other._extraLocations)) {
        return false;
      }
      if (this._features == null) {
        if (other._features != null) {
          return false;
        }
      } else if (!this._features.equals(other._features)) {
        return false;
      }
      if (this._plugins == null) {
        if (other._plugins != null) {
          return false;
        }
      } else if (!this._plugins.equals(other._plugins)) {
        return false;
      }
      if (this._useAllPlugins != other._useAllPlugins) {
        return false;
      }
      return true;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[Content:");
      buffer.append(" _useAllPlugins: ");
      buffer.append(this._useAllPlugins);
      buffer.append(" _plugins: ");
      buffer.append(this._plugins);
      buffer.append(" _features: ");
      buffer.append(this._features);
      buffer.append(" _extraLocations: ");
      buffer.append(this._extraLocations);
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
