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
package org.ant4eclipse.lib.pde.model.buildproperties;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginBuildProperties extends AbstractBuildProperties {

  /** list of libraries */
  private Map<String, Library> _libraries;

  // /** (deprecated) same effect than extra.<library> except that the entries are applied to all libraries */
  // private List _jarsExtraClasspath;

  /** defines the order in which jars should be compiled (in case there are multiple libraries) */
  private String[]             _jarsCompileOrder;

  /** Returns the javac source level for this plugin (If not set, 1.3 is the default value) */
  private String               _javacSource = "1.3";

  /** Returns the class compatibility level. (if not set 1.2 is the default value) */
  private String               _javacTarget = "1.2";

  /**
   *
   */
  public PluginBuildProperties() {
    this._libraries = new HashMap<String, Library>();
  }

  /**
   * @return Returns the libraries.
   */
  public Library[] getLibraries() {
    return this._libraries.values().toArray(new Library[0]);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getLibrariesSourceRoots(String sourcePostfix) {

    StringBuffer buffer = new StringBuffer();

    for (Iterator<Library> iterator = this._libraries.values().iterator(); iterator.hasNext();) {
      Library library = iterator.next();
      buffer.append(library.getName());
      if (!library.isSelf() && sourcePostfix != null) {
        buffer.append(sourcePostfix);
      }
      if (iterator.hasNext()) {
        buffer.append(",");
      }
    }

    return buffer.toString();
  }

  /**
   * @param library
   *          add the specified library to the build properties.
   */
  public void addLibrary(Library library) {
    Assure.notNull("library", library);
    if (!this._libraries.containsKey(library.getName())) {
      this._libraries.put(library.getName(), library);
    }
  }

  /**
   * Returns the library with the given name or null if no such library exists.
   * 
   * @param libraryName
   *          (eq. "." or "library1.jar")
   * @return The Library object or null if no such library exists
   */
  public Library getLibrary(String libraryName) {
    Assure.notNull("libraryName", libraryName);
    return this._libraries.get(libraryName);
  }

  /**
   * Returns whether the given library exists in this build properties.
   * 
   * @param libraryName
   *          (eg. "." or "library1.jar")
   * @return true or false
   */
  public boolean hasLibrary(String libraryName) {
    Assure.notNull("libraryName", libraryName);
    return this._libraries.containsKey(libraryName);
  }

  /**
   * @return Returns the jarsCompileOrder.
   */
  public String[] getJarsCompileOrder() {
    return this._jarsCompileOrder;
  }

  /**
   * Returns the libraries specified in this build.properties as specified by jarsCompileOrder or unordered if that
   * property is not set
   * 
   * @return An ordered list of libraries specified in the 'build.properties' file.
   */
  public Library[] getOrderedLibraries() {
    // build libraries
    String[] jars = getJarsCompileOrder();
    Collection<Library> libraries = null;
    if (jars == null || jars.length < 1) {
      // no build order specified, hope we don't need one...
      libraries = this._libraries.values();
    } else {
      libraries = new LinkedList<Library>();
      for (String jar : jars) {
        Library library = getLibrary(jar);
        if (library != null) {
          libraries.add(library);
        } else {
          A4ELogging.warn("Library '%s' specified in 'jars.compile.order' is not in defined in build.properties!", jar);
        }
      }
    }
    return libraries.toArray(new Library[libraries.size()]);
  }

  /**
   * @param compileOrder
   *          The compileOrder to set.
   */
  void setJarsCompileOrder(String[] compileOrder) {
    Assure.notNull("compileOrder", compileOrder);
    this._jarsCompileOrder = compileOrder;
  }

  /**
   * <p>
   * Implements a plugin library.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Library {

    /**
     * The name of the library.
     */
    private String   _name;

    /**
     * Indicate the file that will be used as a manifest for the library. The file must be located in one of the source
     * folder being used as input of the jar.
     */
    private String   _manifest;

    /**
     * Array of source folders that will be compiled (e.g. source.xyz.jar=src/, src-ant/). If the library is specified
     * in your plugin.xml or manifest.mf, the value should match it.
     */
    private String[] _source;

    /** Array of output folders receiving the result of the compilation */
    private String[] _output;

    /** List the files that should not be copied into the library by the compiler */
    private String   _exclude;

    // /**
    // * extra classpaths used to perform automated build. Classpath can either be relative paths, or // platform urls
    // * referring to plug-ins and fragments of your development environment (e.g. ../someplugin/xyz.jar, //
    // * platform:/plugins/org.apache.ant/ant.jar). Platform urls are recommended over relative paths
    // */
    // private List _extra;

    /**
     * @param name
     */
    public Library(String name) {
      Assure.notNull("name", name);
      this._name = name;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
      return this._name;
    }

    /**
     * @return Returns the manifest.
     */
    public String getManifest() {
      return this._manifest;
    }

    /**
     * <p>
     * </p>
     * 
     * 
     * @return Returns the output.
     */
    public String[] getOutput() {
      return this._output;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public boolean hasOutput() {
      return this._output != null && this._output.length > 0;
    }

    /**
     * @return Returns the source.
     */
    public String[] getSource() {
      return this._source;
    }

    /**
     * @return
     */
    public boolean hasSource() {
      return this._source != null && this._source.length > 0;
    }

    /**
     * @return Returns the exclude.
     */
    public String getExclude() {
      return this._exclude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[PluginBuildProperties.Libary");
      buffer.append(" name:");
      buffer.append(this._name);
      buffer.append(", manifest:");
      buffer.append(this._manifest);
      buffer.append(", source:");
      buffer.append(Arrays.asList(this._source));
      buffer.append(", output:");
      buffer.append(Arrays.asList(this._output));
      buffer.append(", exclude:");
      buffer.append(this._exclude);
      buffer.append("]");
      return buffer.toString();
    }

    @Override
    public int hashCode() {
      int PRIME = 31;
      int result = 1;
      result = PRIME * result + ((this._exclude == null) ? 0 : this._exclude.hashCode());
      result = PRIME * result + ((this._manifest == null) ? 0 : this._manifest.hashCode());
      result = PRIME * result + ((this._name == null) ? 0 : this._name.hashCode());
      result = PRIME * result + Library.hashCode(this._output);
      result = PRIME * result + Library.hashCode(this._source);
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
      Library other = (Library) obj;
      if (this._exclude == null) {
        if (other._exclude != null) {
          return false;
        }
      } else if (!this._exclude.equals(other._exclude)) {
        return false;
      }
      if (this._manifest == null) {
        if (other._manifest != null) {
          return false;
        }
      } else if (!this._manifest.equals(other._manifest)) {
        return false;
      }
      if (this._name == null) {
        if (other._name != null) {
          return false;
        }
      } else if (!this._name.equals(other._name)) {
        return false;
      }
      if (!Arrays.equals(this._output, other._output)) {
        return false;
      }
      if (!Arrays.equals(this._source, other._source)) {
        return false;
      }
      return true;
    }

    private static int hashCode(Object[] array) {
      int PRIME = 31;
      if (array == null) {
        return 0;
      }
      int result = 1;
      for (Object element : array) {
        result = PRIME * result + (element == null ? 0 : element.hashCode());
      }
      return result;
    }

    /**
     * <p>
     * Sets the file that will be used as a manifest for the library.
     * </p>
     * 
     * @param manifest
     *          the manifest to set.
     */
    void setManifest(String manifest) {
      this._manifest = manifest;
    }

    /**
     * @param output
     *          The output to set.
     */
    void setOutput(String[] output) {
      this._output = output;
    }

    /**
     * @param source
     *          The source to set.
     */
    void setSource(String[] source) {
      this._source = source;
    }

    /**
     * @param exclude
     *          The exclude to set.
     */
    void setExclude(String exclude) {
      this._exclude = exclude;
    }

    /**
     * @return whether this library points to the "root" library
     * 
     */
    public boolean isSelf() {
      return ".".equals(getName());
    }
  }

  /**
   * 
   * @return the source compatibility level or 1.3 (default value); never null
   */
  public String getJavacSource() {
    return this._javacSource == null ? "1.3" : this._javacSource;
  }

  public void setJavacSource(String javacSource) {
    this._javacSource = javacSource;
  }

  /**
   * @return the class compatibility level or 1.2 (default value); never null
   */
  public String getJavacTarget() {
    return this._javacTarget == null ? "1.2" : this._javacTarget;
  }

  public void setJavacTarget(String javacTarget) {
    this._javacTarget = javacTarget;
  }

}
