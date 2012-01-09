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

import org.ant4eclipse.lib.core.logging.A4ELogging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginBuildProperties extends AbstractBuildProperties {

  /** list of libraries */
  private Map<String,Library> _libraries;

  /** defines the order in which jars should be compiled (in case there are multiple libraries) */
  private List<String>        _jarsCompileOrder;

  /** Returns the javac source level for this plugin (If not set, 1.3 is the default value) */
  private String              _javacSource = "1.3";

  /** Returns the class compatibility level. (if not set 1.2 is the default value) */
  private String              _javacTarget = "1.2";

  /**
   * The list of additional bundles, that are added to the compile-time classpath only (<b>additional.bundles</b>).
   * 
   * <p>
   * This property can be set using the "Automated Management of Dependencies" section in the Manifest editor
   */
  private List<String>         _additionalBundles;

  /**
   *
   */
  public PluginBuildProperties() {
    _libraries = new HashMap<String,Library>();
  }

  /**
   * @return Returns the libraries.
   */
  public Library[] getLibraries() {
    return _libraries.values().toArray( new Library[0] );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getLibrariesSourceRoots( String sourcePostfix ) {
    StringBuffer buffer = new StringBuffer();
    for( Iterator<Library> iterator = _libraries.values().iterator(); iterator.hasNext(); ) {
      Library library = iterator.next();
      buffer.append( library.getName() );
      if( !library.isSelf() && sourcePostfix != null ) {
        buffer.append( sourcePostfix );
      }
      if( iterator.hasNext() ) {
        buffer.append( "," );
      }
    }
    return buffer.toString();
  }

  /**
   * @param library
   *          add the specified library to the build properties.
   */
  // Assure.notNull( "library", library );
  public void addLibrary( Library library ) {
    if( !_libraries.containsKey( library.getName() ) ) {
      _libraries.put( library.getName(), library );
    }
  }

  /**
   * Returns the library with the given name or null if no such library exists.
   * 
   * @param libraryName
   *          (eq. "." or "library1.jar")
   * @return The Library object or null if no such library exists
   */
  // Assure.notNull( "libraryName", libraryName );
  public Library getLibrary( String libraryName ) {
    return _libraries.get( libraryName );
  }

  /**
   * Returns whether the given library exists in this build properties.
   * 
   * @param libraryName
   *          (eg. "." or "library1.jar")
   * @return true or false
   */
  // Assure.notNull( "libraryName", libraryName );
  public boolean hasLibrary( String libraryName ) {
    return _libraries.containsKey( libraryName );
  }

  /**
   * @return Returns the jarsCompileOrder.
   */
  public List<String> getJarsCompileOrder() {
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
    List<String> jars = getJarsCompileOrder();
    Collection<Library> libraries = null;
    if( (jars == null) || jars.isEmpty() ) {
      // no build order specified, hope we don't need one...
      libraries = _libraries.values();
    } else {
      libraries = new ArrayList<Library>();
      for( String jar : jars ) {
        Library library = getLibrary( jar );
        if( library != null ) {
          libraries.add( library );
        } else {
          A4ELogging
              .warn( "Library '%s' specified in 'jars.compile.order' is not in defined in build.properties!", jar );
        }
      }
    }
    return libraries.toArray( new Library[libraries.size()] );
  }

  /**
   * @param compileOrder
   *          The compileOrder to set.
   */
  // Assure.notNull( "compileOrder", compileOrder );
  void setJarsCompileOrder( List<String> compileOrder ) {
    _jarsCompileOrder = compileOrder;
  }

  /**
   * @return the additionalBundles
   */
  public List<String> getAdditionalBundles() {
    return _additionalBundles;
  }

  /**
   * @param additionalBundles
   *          the additionalBundles to set
   */
  // Assure.notNull( "additionalBundles", additionalBundles );
  public void setAdditionalBundles( List<String> additionalBundles ) {
    _additionalBundles = additionalBundles;
  }

  /**
   * Returns true, if there additional bundles defined, that should be added to compile time classpath
   * 
   * @return
   */
  public boolean hasAdditionalBundles() {
    return(_additionalBundles != null);
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
    private String          _name;

    /**
     * Indicate the file that will be used as a manifest for the library. The file must be located in one of the source
     * folder being used as input of the jar.
     */
    private String          _manifest;

    /**
     * Array of source folders that will be compiled (e.g. source.xyz.jar=src/, src-ant/). If the library is specified
     * in your plugin.xml or manifest.mf, the value should match it.
     */
    private List<String>    _source;

    /** Array of output folders receiving the result of the compilation */
    private List<String>    _output;

    /** List the files that should not be copied into the library by the compiler */
    private String          _exclude;

    /**
     * @param name
     */
    // Assure.notNull( "name", name );
    public Library( String name ) {
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
     * <p>
     * </p>
     * 
     * @return Returns the output.
     */
    public List<String> getOutput() {
      return _output;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public boolean hasOutput() {
      return (_output != null) && (_output.size() > 0);
    }

    /**
     * @return Returns the source.
     */
    public List<String> getSource() {
      return _source;
    }

    /**
     * @return
     */
    public boolean hasSource() {
      return (_source != null) && (_source.size() > 0);
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
      return String.format(
        "[PluginBuildProperties.Libary name: %s, manifest: %s, source: %s, output: %s, exclude: %s]",
      _name, _manifest, Arrays.asList( _source ), Arrays.asList( _output ), _exclude );
    }

    @Override
    public int hashCode() {
      int PRIME = 31;
      int result = 1;
      result = PRIME * result + ((_exclude == null) ? 0 : _exclude.hashCode());
      result = PRIME * result + ((_manifest == null) ? 0 : _manifest.hashCode());
      result = PRIME * result + ((_name == null) ? 0 : _name.hashCode());
      result = PRIME * result + Library.hashCode( _output );
      result = PRIME * result + Library.hashCode( _source );
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
      Library other = (Library) obj;
      if( _exclude == null ) {
        if( other._exclude != null ) {
          return false;
        }
      } else if( !_exclude.equals( other._exclude ) ) {
        return false;
      }
      if( _manifest == null ) {
        if( other._manifest != null ) {
          return false;
        }
      } else if( !_manifest.equals( other._manifest ) ) {
        return false;
      }
      if( _name == null ) {
        if( other._name != null ) {
          return false;
        }
      } else if( !_name.equals( other._name ) ) {
        return false;
      }
      if( !Arrays.equals( _output.toArray(), other._output.toArray() ) ) {
        return false;
      }
      if( !Arrays.equals( _source.toArray(), other._source.toArray() ) ) {
        return false;
      }
      return true;
    }

    private static int hashCode( List<?> array ) {
      int PRIME = 31;
      if( array == null ) {
        return 0;
      }
      int result = 1;
      for( Object element : array ) {
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
    void setManifest( String manifest ) {
      _manifest = manifest;
    }

    /**
     * @param output
     *          The output to set.
     */
    void setOutput( List<String> output ) {
      _output = output;
    }

    /**
     * @param source
     *          The source to set.
     */
    void setSource( List<String> source ) {
      _source = source;
    }

    /**
     * @param exclude
     *          The exclude to set.
     */
    void setExclude( String exclude ) {
      _exclude = exclude;
    }

    /**
     * @return whether this library points to the "root" library
     * 
     */
    public boolean isSelf() {
      return ".".equals( getName() );
    }
  }

  /**
   * 
   * @return the source compatibility level or 1.3 (default value); never null
   */
  public String getJavacSource() {
    return _javacSource == null ? "1.3" : _javacSource;
  }

  public void setJavacSource( String javacSource ) {
    _javacSource = javacSource;
  }

  /**
   * @return the class compatibility level or 1.2 (default value); never null
   */
  public String getJavacTarget() {
    return _javacTarget == null ? "1.2" : _javacTarget;
  }

  public void setJavacTarget( String javacTarget ) {
    _javacTarget = javacTarget;
  }

} /* ENDCLASS */
