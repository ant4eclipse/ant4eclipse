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
package org.ant4eclipse.pde.model.buildproperties;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginBuildProperties extends AbstractBuildProperties {

  // /** lists files to include in the source build */
  // private String _srcIncludes;

  // /** lists files to exclude from the source build */
  // private String _srcExcludes;

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
    _libraries = new HashMap<String, Library>();
  }

  /**
   * @return Returns the libraries.
   */
  public Library[] getLibraries() {
    return _libraries.values().toArray(new Library[0]);
  }

  /**
   * @param library
   *          add the specified library to the build properties.
   */
  public void addLibrary(Library library) {
    Assert.notNull(library);

    if (!_libraries.containsKey(library.getName())) {
      _libraries.put(library.getName(), library);
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
    Assert.notNull(libraryName);

    return _libraries.get(libraryName);
  }

  /**
   * Returns whether the given library exists in this build properties.
   * 
   * @param libraryName
   *          (eg. "." or "library1.jar")
   * @return true or false
   */
  public boolean hasLibrary(String libraryName) {
    Assert.notNull(libraryName);

    return _libraries.containsKey(libraryName);
  }

  /**
   * @return Returns the jarsCompileOrder.
   */
  public String[] getJarsCompileOrder() {
    return _jarsCompileOrder;
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
      libraries = _libraries.values();
    } else {
      libraries = new LinkedList<Library>();
      for (int i = 0; i < jars.length; i++) {
        Library library = getLibrary(jars[i]);
        if (library != null) {
          libraries.add(library);
        } else {
          A4ELogging.warn("Library '%s' specified in 'jars.compile.order' is not in defined in build.properties!",
              jars[i]);
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
    Assert.notNull(compileOrder);

    _jarsCompileOrder = compileOrder;
  }

  /**
   * <p>
   * Implements a plugin library.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Library {

    private static int hashCode(Object[] array) {
      final int PRIME = 31;
      if (array == null)
        return 0;
      int result = 1;
      for (int index = 0; index < array.length; index++) {
        result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
      }
      return result;
    }

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
      Assert.notNull(name);

      _name = name;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
      return _name;
    }

    /**
     * @return Returns the manifest.
     */
    public String getManifest() {
      return _manifest;
    }

    /**
     * @return Returns the output.
     */
    public String[] getOutput() {
      return _output;
    }

    /**
     * @return Returns the source.
     */
    public String[] getSource() {
      return _source;
    }

    /**
     * @return Returns the exclude.
     */
    public String getExclude() {
      return _exclude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[PluginBuildProperties.Libary");
      buffer.append(" name:");
      buffer.append(_name);
      buffer.append(", manifest:");
      buffer.append(_manifest);
      buffer.append(", source:");
      buffer.append(Arrays.asList(_source));
      buffer.append(", output:");
      buffer.append(Arrays.asList(_output));
      buffer.append(", exclude:");
      buffer.append(_exclude);
      buffer.append("]");
      return buffer.toString();
    }

    @Override
    public int hashCode() {
      final int PRIME = 31;
      int result = 1;
      result = PRIME * result + ((_exclude == null) ? 0 : _exclude.hashCode());
      result = PRIME * result + ((_manifest == null) ? 0 : _manifest.hashCode());
      result = PRIME * result + ((_name == null) ? 0 : _name.hashCode());
      result = PRIME * result + Library.hashCode(_output);
      result = PRIME * result + Library.hashCode(_source);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Library other = (Library) obj;
      if (_exclude == null) {
        if (other._exclude != null)
          return false;
      } else if (!_exclude.equals(other._exclude))
        return false;
      if (_manifest == null) {
        if (other._manifest != null)
          return false;
      } else if (!_manifest.equals(other._manifest))
        return false;
      if (_name == null) {
        if (other._name != null)
          return false;
      } else if (!_name.equals(other._name))
        return false;
      if (!Arrays.equals(_output, other._output))
        return false;
      if (!Arrays.equals(_source, other._source))
        return false;
      return true;
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
      _manifest = manifest;
    }

    /**
     * @param output
     *          The output to set.
     */
    void setOutput(String[] output) {
      _output = output;
    }

    /**
     * @param source
     *          The source to set.
     */
    void setSource(String[] source) {
      _source = source;
    }

    /**
     * @param exclude
     *          The exclude to set.
     */
    void setExclude(String exclude) {
      _exclude = exclude;
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
    return (_javacSource == null ? "1.3" : _javacSource);
  }

  public void setJavacSource(String javacSource) {
    _javacSource = javacSource;
  }

  /**
   * @return the class compatibility level or 1.2 (default value); never null
   */
  public String getJavacTarget() {
    return (_javacTarget == null ? "1.2" : _javacTarget);
  }

  public void setJavacTarget(String javacTarget) {
    _javacTarget = javacTarget;
  }

}
