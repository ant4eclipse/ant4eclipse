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
package org.ant4eclipse.lib.jdt.ecj.internal.tools.loader;

import org.ant4eclipse.lib.core.ClassName;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.jdt.ecj.ClassFile;
import org.ant4eclipse.lib.jdt.ecj.ClassFileLoader;
import org.ant4eclipse.lib.jdt.ecj.EcjExceptionCodes;
import org.ant4eclipse.lib.jdt.ecj.ReferableSourceFile;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.ReferableSourceFileImpl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>
 * Implementation of a class path based {@link ClassFileLoader}. An instance of this class contains an array of files
 * (jar files or directories) where the class file loader searches for classes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathClassFileLoaderImpl implements ClassFileLoader {

  private List<File>                    _classpathEntries;
  private List<File>                    _sourcepathEntries;
  private File                          _location;
  private byte                          _type;
  private Map<String,PackageProvider>   _allPackages;

  /**
   * <p>
   * Creates a new instance of type ClasspathClassFileLoaderImpl.
   * </p>
   * 
   * @param entry
   *          the file entry
   * @param type
   *          type
   */
  // Assure.notNull( "entry", entry );
  public ClasspathClassFileLoaderImpl( File entry, byte type ) {
    _location = entry;
    _type = type;
    initialize( Arrays.asList( entry ), new ArrayList<File>() );
  }

  // Assure.notNull( "classPathEntry", classPathEntry );
  // Assure.notNull( "sourcePathEntry", sourcePathEntry );
  public ClasspathClassFileLoaderImpl( File classPathEntry, byte type, File sourcePathEntry ) {
    _location = classPathEntry;
    _type = type;
    initialize( Arrays.asList( classPathEntry ), Arrays.asList( sourcePathEntry ) );
  }

  /**
   * <p>
   * Creates a new instance of type {@link FilteredClasspathClassFileLoader}.
   * </p>
   * 
   * @param location
   * @param type
   * @param classpathEntries
   */
  // Assure.notNull( "location", location );
  public ClasspathClassFileLoaderImpl( File location, byte type, List<File> classpathEntries ) {
    _location = location;
    _type = type;
    initialize( classpathEntries, new ArrayList<File>() );
  }

  // Assure.notNull( "location", location );
  public ClasspathClassFileLoaderImpl( File location, byte type, List<File> classpathEntries, List<File> sourcePathEntries ) {
    _location = location;
    _type = type;
    initialize( classpathEntries, sourcePathEntries );
  }

  /**
   * <p>
   * Creates a new instance of type {@link FilteredClasspathClassFileLoader}.
   * </p>
   */
  protected ClasspathClassFileLoaderImpl() {
    // nothing to do here...
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPackage( String packageName ) {
    return _allPackages.containsKey( packageName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getAllPackages() {
    List<String> keys = new ArrayList<String>( _allPackages.keySet() );
    Collections.sort( keys );
    return keys;
  }

  /**
   * <p>
   * Sets the location.
   * </p>
   * 
   * @param location
   */
  protected void setLocation( File location ) {
    _location = location;
  }

  /**
   * <p>
   * Sets the type.
   * </p>
   * 
   * @param type
   */
  protected void setType( byte type ) {
    _type = type;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected File getLocation() {
    return _location;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected byte getType() {
    return _type;
  }

  /**
   * <p>
   * Returns all class path entries of this {@link ClassFileLoader}.
   * </p>
   * 
   * @return all class path entries of this {@link ClassFileLoader}.
   */
  protected List<File> getClasspathEntries() {
    return _classpathEntries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getClasspath() {
    return getClasspathEntries();
  }

  /**
   * <p>
   * Initializes this class file loader.
   * </p>
   * 
   * @param classpathEntries
   *          the class path entries.
   */
  // Assure.notNull( "classpathEntries", classpathEntries );
  // Assure.notNull( "sourcepathEntries", sourcepathEntries );
  // for( File classpathEntrie : classpathEntries ) {
  //   Assure.notNull( "classpathEntrie", classpathEntrie );
  // }
  // for( File sourcepathEntry : sourcepathEntries ) {
  //   Assure.notNull( "sourcepathEntry", sourcepathEntry );
  // }
  protected void initialize( List<File> classpathEntries, List<File> sourcepathEntries ) {

    // assign path entries
    _classpathEntries = classpathEntries;
    _sourcepathEntries = sourcepathEntries;

    // create allPackages hash map
    _allPackages = new HashMap<String,PackageProvider>();

    // add all existing packages to the hash map
    for( File file : _classpathEntries ) {
      if( file.isDirectory() ) {
        List<String> allPackages = getAllPackagesFromDirectory( file );
        addAllPackagesFromClassPathEntry( allPackages, file );
      } else if( file.isFile() ) {
        List<String> allPackages = getAllPackagesFromJar( file );
        addAllPackagesFromClassPathEntry( allPackages, file );
      }
    }

    // add all existing packages to the hash map
    for( File file : _sourcepathEntries ) {
      if( file.isDirectory() ) {
        List<String> allPackages = getAllPackagesFromDirectory( file );
        addAllPackagesFromSourcePathEntry( allPackages, file );
      }
    }
  }

  /**
   * <p>
   * Returns a new {@link PackageProvider}. This method returns a {@link PackageProvider} of type
   * {@link PackageProvider}.
   * </p>
   * <p>
   * You can override this method to provide your own {@link PackageProvider} implementation.
   * </p>
   * 
   * @param classpathEntry
   * @return
   */
  protected PackageProvider newPackageProvider() {
    return new PackageProvider();
  }

  /**
   * <p>
   * </p>
   * 
   * @param packageName
   * @return
   */
  protected final PackageProvider getPackageProvider( String packageName ) {
    return _allPackages.get( packageName );
  }

  /**
   * @param allPackages
   * @param classPathEntry
   */
  private void addAllPackagesFromClassPathEntry( List<String> allPackages, File classPathEntry ) {
    for( String aPackage : allPackages ) {
      if( _allPackages.containsKey( aPackage ) ) {
        PackageProvider provider = _allPackages.get( aPackage );
        provider.addClasspathEntry( classPathEntry );
      } else {
        PackageProvider provider = newPackageProvider();
        provider.addClasspathEntry( classPathEntry );
        _allPackages.put( aPackage, provider );
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param allPackages
   * @param sourcePathEntry
   */
  private void addAllPackagesFromSourcePathEntry( List<String> allPackages, File sourcePathEntry ) {
    for( String aPackage : allPackages ) {
      if( _allPackages.containsKey( aPackage ) ) {
        PackageProvider provider = _allPackages.get( aPackage );
        provider.addSourcepathEntry( sourcePathEntry );
      } else {
        PackageProvider provider = newPackageProvider();
        provider.addSourcepathEntry( sourcePathEntry );
        _allPackages.put( aPackage, provider );
      }
    }
  }

  /**
   * <p>
   * Returns all the names of the packages that are contained in the specified jar file. The package list contains the
   * packages that contain classes as well as all parent packages of those.
   * </p>
   * 
   * @param jar
   * @return
   * @throws IOException
   */
  // Assure.isFile( "jar", jar );
  private List<String> getAllPackagesFromJar( File jar ) {

    // prepare result...
    List<String> result = new ArrayList<String>();

    // create the jarFile wrapper...
    JarFile jarFile = null;

    try {
      jarFile = new JarFile( jar );
    } catch( IOException e ) {
      throw new Ant4EclipseException( EcjExceptionCodes.COULD_NOT_CREATE_JAR_FILE_FROM_FILE_EXCEPTION, jar.getAbsolutePath() );
    }

    // Iterate over entries...
    Enumeration<?> enumeration = jarFile.entries();
    while( enumeration.hasMoreElements() ) {
      JarEntry jarEntry = (JarEntry) enumeration.nextElement();

      // add package for each found directory...
      String directoryName = null;

      // if the jar entry is a directory, the directory name is the name of the jar entry...
      if( jarEntry.isDirectory() ) {
        directoryName = jarEntry.getName();
      }
      // otherwise the directory name has to be computed
      else {
        int splitIndex = jarEntry.getName().lastIndexOf( '/' );
        if( splitIndex != -1 ) {
          directoryName = jarEntry.getName().substring( 0, splitIndex );
        }
      }

      // directoryName can be null if a top level entry is processed
      if( directoryName != null ) {
        // convert path to package name
        String packageName = directoryName.replace( '/', '.' );
        packageName = packageName.endsWith( "." ) ? packageName.substring( 0, packageName.length() - 1 ) : packageName;

        // at package with all the parent packages (!) to the result list
        List<String> packages = allPackages( packageName );
        for( int i = 0; i < packages.size(); i++ ) {
          if( !result.contains( packages.get(i) ) ) {
            result.add( packages.get(i) );
          }
        }
      }
    }

    return result;

  }

  /**
   * <p>
   * Returns all package names (including parent package names) for the specified package.
   * </p>
   * <p>
   * <b>Example:</b><br/>
   * Given the package name <code>net.sf.ant4eclipse.tools</code> this method will return {"net", "net.sf",
   * "net.sf.ant4eclipse", "net.sf.ant4eclipse.tools"}.
   * </p>
   * 
   * @param packageName
   *          the name of the package.
   * @return all package names (including parent package names) for the specified package.
   */
  private List<String> allPackages( String packageName ) {
    StringTokenizer tokenizer = new StringTokenizer( packageName, "." );
    List<String> result = new ArrayList<String>();
    if( tokenizer.hasMoreTokens() ) {
      result.add( tokenizer.nextToken() );
      int i = 0;
      while( tokenizer.hasMoreTokens() ) {
        result.add( String.format( "%s.%s", result.get(i), tokenizer.nextToken() ) );
        i++;
      }
    }
    return result;
  }

  /**
   * @param directory
   * @return
   */
  private List<String> getAllPackagesFromDirectory( File directory ) {
    List<String> result = new ArrayList<String>();
    File[] children = directory.listFiles( new FileFilter() {
      @Override
      public boolean accept( File pathname ) {
        return pathname.isDirectory();
      }
    } );
    for( File element : children ) {
      getAllPackagesFromDirectory( null, element, result );
    }
    return result;
  }

  /**
   * @param prefix
   * @param directory
   * @param result
   */
  private void getAllPackagesFromDirectory( String prefix, File directory, List<String> result ) {

    String newPrefix = prefix == null ? "" : prefix + ".";

    result.add( newPrefix + directory.getName() );

    File[] children = directory.listFiles( new FileFilter() {
      @Override
      public boolean accept( File pathname ) {
        return pathname.isDirectory();
      }
    } );

    for( File element : children ) {
      getAllPackagesFromDirectory( newPrefix + directory.getName(), element, result );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClassFile loadClass( ClassName className ) {

    if( !hasPackage( className.getPackageName() ) ) {
      return null;
    }

    return getPackageProvider( className.getPackageName() ).loadClassFile( className );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReferableSourceFile loadSource( ClassName className ) {
    if( !hasPackage( className.getPackageName() ) ) {
      return null;
    }

    return getPackageProvider( className.getPackageName() ).loadSourceFile( className );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[ClasspathClassFileLoader:" );
    buffer.append( " { " );
    for( int i0 = 0; (_classpathEntries != null) && (i0 < _classpathEntries.size()); i0++ ) {
      buffer.append( " _classpathEntries[" + i0 + "]: " );
      buffer.append( _classpathEntries.get(i0) );
    }
    buffer.append( " } " );
    buffer.append( " _location: " );
    buffer.append( _location );
    buffer.append( "]" );
    return buffer.toString();
  }

  /**
   * <p>
   * Encapsulates all class and source path entries that provide a specific package.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class PackageProvider {

    /** the class path entries */
    private List<File> _classpathEntries;

    /** the source path entries */
    private List<File> _sourcepathEntries;

    /**
     * <p>
     * Creates a new instance of type {@link PackageProvider}.
     * </p>
     */
    public PackageProvider() {
      _classpathEntries = new ArrayList<File>();
      _sourcepathEntries = new ArrayList<File>();
    }

    /**
     * <p>
     * Adds the specified file to the class path.
     * </p>
     * 
     * @param classpathEntry
     *          the class path entry to add.
     */
    // Assure.exists( "classpathEntry", classpathEntry ); 
    public void addClasspathEntry( File classpathEntry ) {
      _classpathEntries.add( classpathEntry );
    }

    /**
     * <p>
     * Adds the specified file to the source path.
     * </p>
     * 
     * @param sourcepathEntry
     *          the source path entry to add.
     */
    // Assure.isDirectory( "sourcepathEntry", sourcepathEntry );
    public void addSourcepathEntry( File sourcepathEntry ) {
      _sourcepathEntries.add( sourcepathEntry );
    }

    /**
     * <p>
     * </p>
     * 
     * @param className
     * @return
     */
    public ClassFile loadClassFile( ClassName className ) {

      for( File file : _classpathEntries ) {
        File classpathEntry = file;

        if( classpathEntry.isDirectory() ) {
          File result = new File( classpathEntry, className.asClassFileName() );

          if( result.exists() ) {

            try {
              if( result.getName().equals( result.getCanonicalFile().getName() ) ) {
                return new FileClassFileImpl( result, classpathEntry.getAbsolutePath(),
                    ClasspathClassFileLoaderImpl.this._type );
              }
            } catch( IOException e ) {
              // do nothing
            }
          }
        } else {
          try {
            JarFile jarFile = new JarFile( classpathEntry );

            JarEntry entry = jarFile.getJarEntry( className.asClassFileName() );

            if( (entry != null) ) {
              return new JarClassFileImpl( className.asClassFileName(), jarFile, classpathEntry.getAbsolutePath(),
                  ClasspathClassFileLoaderImpl.this._type );
            }
          } catch( IOException e ) {
            // nothing to do here...
          }
        }
      }
      return null;
    }

    /**
     * <p>
     * </p>
     * 
     * @param className
     * @return
     */
    public ReferableSourceFile loadSourceFile( ClassName className ) {

      String javaFileName = className.getClassName() + ".java";

      for( File classpathEntry : _sourcepathEntries ) {

        if( classpathEntry.isDirectory() ) {
          File packageDir = new File( classpathEntry, className.getPackageAsDirectoryName() );
          if( packageDir.isDirectory() ) {
            for( String name : packageDir.list() ) {
              if( javaFileName.equals( name )
                  && new File( classpathEntry, className.asSourceFileName().replace( '/', File.separatorChar )
                      .replace( '\\', File.separatorChar ) ).exists() ) {
                return new ReferableSourceFileImpl( classpathEntry, className.asSourceFileName()
                    .replace( '/', File.separatorChar ).replace( '\\', File.separatorChar ),
                    classpathEntry.getAbsolutePath(), ClasspathClassFileLoaderImpl.this._type );
              }
            }
          }
        }

      }
      return null;
    }
  }
  
} /* ENDCLASS */
