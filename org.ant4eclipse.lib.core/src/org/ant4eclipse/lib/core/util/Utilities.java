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
package org.ant4eclipse.lib.core.util;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.nls.NLS;
import org.ant4eclipse.lib.core.nls.NLSMessage;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 * Collection of utility functions that aren't specific to A4E.
 * <p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class Utilities {

  private static final String OPEN            = "${";
  private static final String CLOSE           = "}";

  @NLSMessage( "Exporting a resource is only supported for root based pathes !" )
  public static String        MSG_INVALIDRESOURCEPATH;

  @NLSMessage( "Failed to delete '%s' !" )
  public static String        MSG_FAILEDTODELETE;

  public static final String  PROP_A4ETEMPDIR = "ant4eclipse.temp";

  public static final String  NL              = System.getProperty( "line.separator" );
  public static final String  ENCODING        = System.getProperty( "file.encoding" );
  private static final String OS              = System.getProperty( "os.name" );

  static {
    NLS.initialize( Utilities.class );
  }

  /**
   * Returns a canonical representation of the supplied file.
   * 
   * @param file
   *          The file which canonical representation is desired. Not <code>null</code>.
   * 
   * @return The canonical file. Not <code>null</code>.
   */
  public static final File getCanonicalFile( File file ) {
    Assure.notNull( "file", file );
    try {
      return file.getCanonicalFile();
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.CANONICAL_FILE, file );
    }
  }

  /**
   * Reads the complete content of a text into a StringBuffer. Newlines will be transformed into the system specific
   * newlines (@see {@link #NL} unless requested otherwise.
   * 
   * @param resource
   *          The resource providing the text content. Must be located on the classpath.
   * @param encoding
   *          The encoding to be used for the file. Neither <code>null</code> nor empty.
   * @param includenewlines
   *          <code>true</code> <=> Allow newlines or remove them otherwise.
   * 
   * @return The buffer containing the file content. Not <code>null</code>.
   */
  public static final StringBuffer readTextContent( String resource, String encoding, boolean includenewlines ) {
    URL url = Utilities.class.getResource( resource );
    if( url == null ) {
      throw new Ant4EclipseException( CoreExceptionCode.RESOURCE_NOT_ON_THE_CLASSPATH, resource );
    }
    return readTextContent( url, encoding, includenewlines );
  }

  /**
   * Reads the complete content of a text into a StringBuffer. Newlines will be transformed into the system specific
   * newlines (@see {@link #NL} unless requested otherwise.
   * 
   * @param input
   *          The file providing the text content. Must be a valid file.
   * @param encoding
   *          The encoding to be used for the file. Neither <code>null</code> nor empty.
   * @param includenewlines
   *          <code>true</code> <=> Allow newlines or remove them otherwise.
   * 
   * @return The buffer containing the file content. Not <code>null</code>.
   */
  public static final StringBuffer readTextContent( File input, String encoding, boolean includenewlines ) {
    InputStream instream = null;
    try {
      instream = new FileInputStream( input );
      return readTextContent( instream, encoding, includenewlines );
    } catch( Ant4EclipseException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.FILEIO_FAILURE, input );
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.FILEIO_FAILURE, input );
    } finally {
      close( instream );
    }
  }

  /**
   * Reads the complete content of a text into a StringBuffer. Newlines will be transformed into the system specific
   * newlines (@see {@link #NL} unless requested otherwise.
   * 
   * @param input
   *          The resource providing the text content. Must be a valid resource.
   * @param encoding
   *          The encoding to be used for the file. Maybe <code>null</code>.
   * @param includenewlines
   *          <code>true</code> <=> Allow newlines or remove them otherwise.
   * 
   * @return The buffer containing the file content. Not <code>null</code>.
   */
  public static final StringBuffer readTextContent( URL input, String encoding, boolean includenewlines ) {
    InputStream instream = null;
    try {
      instream = input.openStream();
      return readTextContent( instream, encoding, includenewlines );
    } catch( Ant4EclipseException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.RESOURCEIO_FAILURE, input.toExternalForm() );
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.RESOURCEIO_FAILURE, input.toExternalForm() );
    } finally {
      close( instream );
    }
  }

  /**
   * Reads the complete content of a text into a StringBuffer. Newlines will be transformed into the system specific
   * newlines (@see {@link #NL} unless requested otherwise.
   * 
   * @param input
   *          The stream providing the text content. Not <code>null</code>.
   * @param encoding
   *          The encoding to be used for the file. Neither <code>null</code> nor empty.
   * @param includenewlines
   *          <code>true</code> <=> Allow newlines or remove them otherwise.
   * 
   * @return The buffer containing the file content. Not <code>null</code>.
   */
  public static final StringBuffer readTextContent( InputStream input, String encoding, boolean includenewlines ) {
    try {
      StringBuffer result = new StringBuffer();
      OutputCopier copier = new OutputCopier( input, result, encoding );
      copier.start();
      copier.join();
      if( !includenewlines ) {
        int pos = result.indexOf( NL );
        while( pos != -1 ) {
          result.delete( pos, pos + NL.length() );
          pos = result.indexOf( NL );
        }
      }
      return result;
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.IO_FAILURE );
    } catch( InterruptedException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.FILEIO_FAILURE, input );
    }
  }

  /**
   * Closes the supplied Closeable if it's available.
   * 
   * @param closeable
   *          The closeable that has to be closed. Maybe <code>null</code>.
   */
  public static final void close( Closeable closeable ) {
    if( closeable != null ) {
      try {
        closeable.close();
      } catch( IOException ex ) {
        // generally not interesting so a warning is apropriate here
        A4ELogging.warn( ex.getMessage() );
      }
    }
  }

  /**
   * @see #close(Closeable)
   */
  @Deprecated
  public static final void close( InputStream instream ) {
    close( (Closeable) instream );
  }

  /**
   * @see #close(Closeable)
   */
  @Deprecated
  public static final void close( OutputStream outstream ) {
    close( (Closeable) outstream );
  }

  /**
   * @see #close(Closeable)
   */
  @Deprecated
  public static final void close( Reader reader ) {
    close( (Closeable) reader );
  }

  /**
   * @see #close(Closeable)
   */
  @Deprecated
  public static final void close( Writer writer ) {
    close( (Closeable) writer );
  }

  public static final URL toURL( File file ) {
    Assure.notNull( "file", file );
    URI uri = file.toURI();
    try {
      return uri.toURL();
    } catch( MalformedURLException ex ) {
      throw new RuntimeException( ex );
    }
  }

  /**
   * Returns true if the supplied path could be deleted completely. In case of a directory the complete tree will be
   * deleted.
   * 
   * @param file
   *          The path which shall be removed.
   * 
   * @return true <=> The path could be deleted with success.
   */
  public static final boolean delete( File file ) {
    Assure.notNull( "file", file );
    if( !file.exists() ) {
      return true;
    }
    boolean result = true;
    if( file.isDirectory() ) {
      // delete the children
      File[] children = file.listFiles();
      if( children != null ) {
        for( File element : children ) {
          result = delete( element ) && result;
        }
      }
    }
    // try to delete the file multiple times,
    // since the deletion query may fail (f.e.
    // if another process locked the file)
    int tries = 5;
    while( (!file.delete()) && (tries > 0) ) {
      try {
        System.gc();
        Thread.sleep( 10 );
        System.gc();
      } catch( InterruptedException ex ) {
        // do nothing here
      }
      tries--;
    }
    if( file.exists() ) {
      A4ELogging.warn( MSG_FAILEDTODELETE, file.getPath() );
      result = false;
    }
    return result;
  }

  /**
   * Returns a list of all files located within the supplied directory/file.
   * 
   * @param file
   *          The resource which files should be listed. If it's a file it will be the only child in the returned list.
   * 
   * @return The list containing all children. Not <code>null</code>.
   */
  public static final List<File> getAllChildren( File file ) {
    Assure.notNull( "file", file );
    List<File> result = new ArrayList<File>();
    if( file.isDirectory() ) {
      // add the children
      File[] children = file.listFiles();
      if( children != null ) {
        for( File element : children ) {
          if( element.isFile() ) {
            result.add( element );
          } else {
            result.addAll( getAllChildren( element ) );
          }
        }
      }
    } else {
      result.add( file );
    }
    return result;
  }

  public static final boolean hasChild( File directory, final String childName ) {
    return getChild( directory, childName ) != null;
  }

  public static final File getChild( File directory, final String childName ) {
    File[] children = directory.listFiles( new FilenameFilter() {
      @Override
      public boolean accept( File dir, String name ) {
        return name.equals( childName );
      }
    } );
    if( children.length < 1 ) {
      return null;
    }
    return children[0];
  }

  /**
   * Simple search replace method.
   * 
   * @param input
   *          The text which should be altered. Not <code>null</code>.
   * @param search
   *          The text that has to be removed. Not <code>null</code>.
   * @param replacement
   *          The text which has to be used as a replacement. Not <code>null</code>.
   * 
   * @return The modified text. Not <code>null</code>.
   */
  public static final String replace( String input, String search, String replacement ) {
    Assure.notNull( "input", input );
    Assure.notNull( "search", search );
    Assure.notNull( "replacement", replacement );
    int idx = input.indexOf( search );
    if( idx == -1 ) {
      return input;
    }
    StringBuffer buffer = new StringBuffer( input );
    while( idx != -1 ) {
      buffer.delete( idx, idx + search.length() );
      buffer.insert( idx, replacement );
      idx = buffer.indexOf( search, idx + replacement.length() );
    }
    return buffer.toString();
  }

  /**
   * Replaces a character with a specified string.
   * 
   * @param input
   *          The string which will be modified. Not <code>null</code>.
   * @param ch
   *          The character that shall be replaced.
   * @param replacement
   *          The replacing string. Not <code>null</code>.
   * 
   * @return A string with replaced characters. Not <code>null</code>.
   */
  public static final String replace( String input, char ch, String replacement ) {
    return replace( input, String.valueOf( ch ), replacement );
  }

  /**
   * Calculates a relative path for the supplied files.
   * 
   * @param fromfile
   *          Starting point within a file system.
   * @param tofile
   *          Ending point within a file system.
   * 
   * @return The file which indicates the relative path. null in case the relative path could not be calculated.
   */
  public static final String calcRelative( File fromfile, File tofile ) {
    Assure.notNull( "fromfile", fromfile );
    Assure.notNull( "tofile", tofile );
    String frompath = null;
    String topath = null;
    try {
      frompath = fromfile.getCanonicalPath().replace( '\\', '/' );
    } catch( IOException ex ) {
      return null;
    }
    try {
      topath = tofile.getCanonicalPath().replace( '\\', '/' );
    } catch( IOException ex ) {
      return null;
    }
    if( frompath.equals( "/" ) ) {
      // special treatment for unix filesystems since split would result in an empty list
      if( topath.startsWith( "/" ) ) {
        return replace( topath.substring( 1 ), "/", File.separator );
      } else {
        // the other path is invalid
        return null;
      }
    }
    String[] fromstr = frompath.split( "/" );
    String[] tostr = topath.split( "/" );
    if( !fromstr[0].equals( tostr[0] ) ) {
      // we're not working on the same device
      /**
       * @todo [26-Feb-2006:KASI] Can this be omitted under UNIX ?
       */
      return null;
    }
    int same = 1;
    for( ; same < Math.min( fromstr.length, tostr.length ); same++ ) {
      if( !fromstr[same].equals( tostr[same] ) ) {
        break;
      }
    }
    StringBuffer buffer = new StringBuffer();
    for( int i = same; i < fromstr.length; i++ ) {
      buffer.append( File.separator );
      buffer.append( ".." );
    }
    for( int i = same; i < tostr.length; i++ ) {
      buffer.append( File.separator );
      buffer.append( tostr[i] );
    }
    if( buffer.length() > 0 ) {
      buffer.delete( 0, File.separator.length() );
    }
    return buffer.toString();
  }

  /**
   * removes trailing / or \\ from the given path
   * 
   * @param path
   * @return the path without a trailing path separator
   */
  public static final String removeTrailingPathSeparator( String path ) {
    if( (path == null) || (path.length() < 2) ) {
      return path;
    }
    if( path.endsWith( "/" ) || path.endsWith( "\\" ) ) {
      return path.substring( 0, path.length() - 1 );
    }
    return path;
  }

  /**
   * Reverses the order of the elements in the specified array.
   * 
   * @param array
   *          the array whose elements are to be reversed.
   */
  public static <T> void reverse( T[] array ) {
    if( array != null ) {
      final List<T> list = Arrays.asList( array );
      Collections.reverse( list );
    }
  }

  /**
   * <p>
   * Check if a String has text. More specifically, returns <code>true</code> if the string not
   * <code>null<code>, it's <code>length is > 0</code>, and it has at least one non-whitespace character.
   * </p>
   * <p>
   * 
   * <pre>
   *      Utilities.hasText(null) = false
   *      Utilities.hasText(&quot;&quot;) = false
   *      Utilities.hasText(&quot; &quot;) = false
   *      Utilities.hasText(&quot;12345&quot;) = true
   *      Utilities.hasText(&quot; 12345 &quot;) = true
   * </pre>
   * 
   * </p>
   * 
   * @param str
   *          the String to check, may be <code>null</code>
   * @return <code>true</code> if the String is not null, length > 0, and not whitespace only
   * @see java.lang.Character#isWhitespace
   */
  public static final boolean hasText( String str ) {
    return cleanup( str ) != null;
  }

  /**
   * Alters the supplied String to make sure that it has a value.
   * 
   * @param input
   *          The String that might be altered. Maybe <code>null</code>.
   * 
   * @return <code>null</code> in case there's no value or a String which contains at least one valuable character.
   */
  public static final String cleanup( String input ) {
    String result = input;
    if( result != null ) {
      result = result.trim();
      if( result.length() == 0 ) {
        result = null;
      }
    }
    return result;
  }

  /**
   * Similar to {@link #cleanup(String)} the input is altered if necessary, so the returned list only consists of
   * non-empty Strings. There's at least one String or the result is <code>null</code>.
   * 
   * @param input
   *          The input array which might be altered. Maybe <code>null</code>.
   * 
   * @return A list of the input values which is either <code>null</code> or contains at least one non empty value.
   */
  public static final String[] cleanup( String[] input ) {
    List<String> result = new ArrayList<String>();
    if( input != null ) {
      for( String element2 : input ) {
        String element = cleanup( element2 );
        if( element != null ) {
          result.add( element );
        }
      }
      if( result.isEmpty() ) {
        return null;
      }
    }
    return result.toArray( new String[ result.size() ] );
  }

  /**
   * Similar to {@link #cleanup(String)} the input is altered if necessary, so the returned list only consists of
   * non-empty Strings. There's at least one String or the result is <code>null</code>.
   * 
   * @param input
   *          The input array which might be altered. Maybe <code>null</code>.
   * 
   * @return A list of the input values which is either <code>null</code> or contains at least one non empty value.
   */
  public static final List<String> cleanup( List<String> input ) {
    if( input != null ) {
      for( int i = input.size() - 1; i >= 0; i-- ) {
        String element = cleanup( input.get(i) );
        if( element == null ) {
          input.remove(i);
        }
      }
      if( input.isEmpty() ) {
        input = null;
      }
    }
    return input;
  }

  /**
   * Generates a textual representation for the supplied list of values.
   * 
   * @param objects
   *          A list of values. Not <code>null</code>.
   * @param delimiter
   *          The delimiter to be used. If <code>null</code> the default value <code>,</code> is used.
   * 
   * @return A textual representation for the supplied list of values. Not <code>null</code>.
   */
  public static final String listToString( Object[] objects, String delimiter ) {
    Assure.notNull( "objects", objects );
    if( delimiter == null ) {
      delimiter = ",";
    }
    StringBuffer buffer = new StringBuffer();
    if( objects.length > 0 ) {
      buffer.append( String.valueOf( objects[0] ) );
      for( int i = 1; i < objects.length; i++ ) {
        buffer.append( delimiter );
        buffer.append( String.valueOf( objects[i] ) );
      }
    }
    return buffer.toString();
  }

  /**
   * Returns a list of all classes for all supplied objects.
   * 
   * @param objects
   *          The objects used to get the class types for. Maybe <code>null</code>.
   * 
   * @return A list of all classes for all supplied objects. Not <code>null</code>.
   */
  public static final Class<?>[] getClasses( Object ... objects ) {
    if( objects != null ) {
      Class<?>[] result = new Class<?>[objects.length];
      for( int i = 0; i < objects.length; i++ ) {
        result[i] = objects[i].getClass();
      }
      return result;
    } else {
      return new Class<?>[0];
    }
  }

  /**
   * Delivers a <code>String</code> representation of the supplied list of classes.
   * 
   * @param delimiter
   *          The delimiter to be used. If <code>null</code> comma will be used.
   * @param simplename
   *          <code>true</code> <=> Use the class simplename rather than the fully qualified name.
   * @param classes
   *          The list of classes which shall be turned into a textual presentation. Mabye <code>null</code>.
   * 
   * @return A textual presentation of the supplied classes. Not <code>null</code>.
   */
  public static final String toString( String delimiter, boolean simplename, Class<?> ... classes ) {
    if( delimiter == null ) {
      delimiter = ",";
    }
    StringBuffer buffer = new StringBuffer();
    if( (classes != null) && (classes.length > 0) ) {
      buffer.append( simplename ? classes[0].getSimpleName() : classes[0].getName() );
      for( int i = 1; i < classes.length; i++ ) {
        buffer.append( delimiter );
        buffer.append( simplename ? classes[i].getSimpleName() : classes[i].getName() );
      }
    }
    return buffer.toString();
  }

  /**
   * Generates a textual representation of the supplied Property map.
   * 
   * @param properties
   *          The map which shall be translated into a text.
   * 
   * @return The text representing the content of the supplied map.
   */
  public static final String toString( Properties properties ) {
    return toString( null, properties );
  }

  /**
   * Generates a textual representation of the supplied Property map.
   * 
   * @param title
   *          A textual information printed above the property map.
   * @param properties
   *          The map which shall be translated into a text. Not <code>null</code>.
   * 
   * @return The text representing the content of the supplied map.
   */
  public static final String toString( String title, Properties properties ) {
    Assure.notNull( "properties", properties );
    StringBuilder buffer = new StringBuilder();
    if( title != null ) {
      buffer.append( title );
      buffer.append( NL );
    }
    Iterator<Entry<Object,Object>> it = properties.entrySet().iterator();
    while( it.hasNext() ) {
      Entry<Object,Object> entry = it.next();
      buffer.append( "'" ).append( entry.getKey() );
      buffer.append( "' -> '" );
      buffer.append( entry.getValue() );
      buffer.append( "'" );
      buffer.append( NL );
    }
    return buffer.toString();
  }

  /**
   * Creates the given directory (including all of its missing parent directories) if it does not exists yet.
   * 
   * @param directory
   */
  public static final void mkdirs( File directory ) {
    Assure.notNull( "directory", directory );
    if( directory.isDirectory() ) {
      return;
    }
    if( directory.isFile() ) {
      throw new Ant4EclipseException( CoreExceptionCode.PATH_MUST_NOT_BE_A_FILE, directory );
    }
    if( !directory.mkdirs() ) {
      throw new Ant4EclipseException( CoreExceptionCode.DIRECTORY_COULD_NOT_BE_CREATED, directory );
    }
  }

  /**
   * Creates a new instance of the class with the given name.
   * 
   * <p>
   * The class must be loadable via Class.forName() and must have a default constructor
   * 
   * @param className
   * @return
   */
  @SuppressWarnings( "unchecked" )
  public static final <T> T newInstance( String className ) {
    Assure.notNull( "className", className );
    Class<?> clazz = null;
    try {
      clazz = Class.forName( className );
    } catch( Exception ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_LOAD_CLASS, className, ex.toString() );
    }
    // try to instantiate using default cstr...
    T object = null;
    try {
      object = (T) clazz.newInstance();
    } catch( Exception ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_INSTANTIATE_CLASS, className, ex.toString() );
    }
    // return the constructed object
    return object;
  }

  @SuppressWarnings( "unchecked" )
  public static final <T> T newInstance( String className, Class<?>[] types, Object[] args ) {
    Assure.notNull( "className", className );
    Class<?> clazz = null;
    try {
      clazz = Class.forName( className );
    } catch( Exception ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_LOAD_CLASS, className, ex.toString() );
    }
    // try to instantiate...
    T object = null;
    try {
      Constructor<?> constructor = clazz.getDeclaredConstructor( types );
      constructor.setAccessible( true );
      constructor.newInstance( args );
      object = (T) constructor.newInstance( args );
    } catch( Exception ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_INSTANTIATE_CLASS, className, ex.toString() );
    }
    // return the constructed object
    return object;
  }

  /**
   * <p>
   * The class must be loadable via Class.forName() and must have a constructor with a single String parameter.
   * </p>
   * 
   * @param className
   *          The name of the class. Neither <code>null</code> nor empty.
   * @param arg
   *          The argument used to instantiate the class. Maybe <code>null</code>.
   * 
   * @return The newly instantiated type. Not <code>null</code>.
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public static final <T> T newInstance( String className, String arg ) {
    Assure.notNull( "className", className );
    Class<?> clazz = null;
    // Try to load class...
    try {
      clazz = Class.forName( className );
    } catch( Exception ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_LOAD_CLASS, className, ex.toString() );
    }
    Constructor constructor = null;
    try {
      constructor = clazz.getConstructor( String.class );
    } catch( NoSuchMethodException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_INSTANTIATE_CLASS, className, ex.toString() );
    }
    // try to instantiate using default cstr...
    T object = null;
    try {
      object = (T) constructor.newInstance( arg );
    } catch( Exception ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_INSTANTIATE_CLASS, className, ex.toString() );
    }
    return object;
  }

  /**
   * Checks whether a specific literal is part of a list of allowed values.
   * 
   * @param candidate
   *          The literal which has to be tested. Not <code>null</code>.
   * @param allowed
   *          A list of allowed values. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied literal is part of the allowed values.
   */
  public static final boolean contains( String candidate, String ... allowed ) {
    Assure.notNull( "candidate", candidate );
    Assure.notNull( "allowed", allowed );
    for( String part : allowed ) {
      if( candidate.equals( part ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Filters a list according to a specified type. The order is preserved.
   * 
   * @param input
   *          The list which will be filtered. Not <code>null</code>.
   * @param clazz
   *          The type which must be matched by the list elements. Not <code>null</code>.
   * 
   * @return A list with the input elements that do match the specified type. Not <code>null</code>.
   */
  public static final List<Object> filter( List<Object> input, Class<?> clazz ) {
    Assure.notNull( "input", input );
    Assure.notNull( "clazz", clazz );
    List<Object> result = new ArrayList<Object>();
    for( int i = 0; i < input.size(); i++ ) {
      if( clazz.isAssignableFrom( input.get( i ).getClass() ) ) {
        result.add( input.get( i ) );
      }
    }
    return result;
  }

  /**
   * This function copies the content of a resource into a file. This function will cause an exception in case of a
   * failure.
   * 
   * @param source
   *          The URL pointing to the resource. Not <code>null</code>.
   * @param dest
   *          The destination file where the copy shall be created. Not <code>null</code>.
   */
  public static final void copy( URL source, File dest ) {
    Assure.notNull( "source", source );
    Assure.notNull( "dest", dest );
    InputStream instream = null;
    OutputStream outstream = null;
    try {
      instream = source.openStream();
      outstream = new FileOutputStream( dest );
      byte[] buffer = new byte[16384];
      copy( instream, outstream, buffer );
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COULD_NOT_EXPORT_RESOURCE, source.toExternalForm(), dest );
    }
  }

  /**
   * Unpacks the content from the supplied zip file into the supplied destination directory.
   * 
   * @todo [11-Dec-2009:KASI] This should be merged with {@link #expandJarFile(JarFile, File)}
   * 
   * @param zipfile
   *          The zip file which has to be unpacked. Not <code>null</code> and must be a file.
   * @param destdir
   *          The directory where the content shall be written to. Not <code>null</code>.
   */
  public static final void unpack( File zipfile, File destdir ) {
    Assure.notNull( "zipfile", zipfile );
    Assure.notNull( "destdir", destdir );
    byte[] buffer = new byte[16384];
    try {
      if( !destdir.isAbsolute() ) {
        destdir = destdir.getAbsoluteFile();
      }
      ZipFile zip = new ZipFile( zipfile );
      Enumeration<? extends ZipEntry> entries = zip.entries();
      while( entries.hasMoreElements() ) {
        ZipEntry zentry = entries.nextElement();
        if( zentry.isDirectory() ) {
          mkdirs( new File( destdir, zentry.getName() ) );
        } else {
          File destfile = new File( destdir, zentry.getName() );
          mkdirs( destfile.getParentFile() );
          copy( zip.getInputStream( zentry ), new FileOutputStream( destfile ), buffer );
        }
      }
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.UNPACKING_FAILED, zipfile );
    }
  }

  /**
   * Copies one file to another location.
   * 
   * @param source
   *          The original File which has to be copied. Not <code>null</code>.
   * @param to
   *          The destination File which has to be written. Not <code>null</code>.
   */
  public static final void copy( File source, File to ) {
    Assure.isFile( "source", source );
    Assure.notNull( "to", to );
    FileInputStream instream = null;
    FileOutputStream outstream = null;
    FileChannel readchannel = null;
    FileChannel writechannel = null;
    try {
      instream = new FileInputStream( source );
      outstream = new FileOutputStream( to );
      readchannel = instream.getChannel();
      writechannel = outstream.getChannel();
      readchannel.transferTo( 0, readchannel.size(), writechannel );
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.COPY_FAILURE, source.getAbsolutePath(),
          to.getAbsolutePath() );
    } finally {
      close( readchannel );
      close( writechannel );
      close( instream );
      close( outstream );
    }
  }

  /**
   * Copies the complete content from an InputStream into an OutputStream using a specified buffer. Both streams will be
   * closed after completion or in case an exception comes up.
   * 
   * @param instream
   *          The InputStream providing the content. Not <code>null</code>.
   * @param outstream
   *          The OutputStream used to write the content to. Not <code>null</code>.
   * @param buffer
   *          The buffer used for the copying process. Not <code>null</code>.
   * 
   * @throws IOException
   *           Copying failed for some reason.
   */
  public static final void copy( InputStream instream, OutputStream outstream, byte[] buffer ) throws IOException {
    Assure.notNull( "instream", instream );
    Assure.notNull( "outstream", outstream );
    Assure.nonEmpty( "buffer", buffer );
    try {
      int read = instream.read( buffer );
      while( read != -1 ) {
        if( read > 0 ) {
          outstream.write( buffer, 0, read );
        }
        read = instream.read( buffer );
      }
    } finally {
      close( outstream );
      close( instream );
    }
  }

  /**
   * Exports a resource which resides on the classpath into a temporarily generated file. This file will be deleted when
   * the vm will be exited. Therefore the file is just used temporarily. The resource itself must be accessible through
   * the ClassLoader used to load this class. The suffix will be derived from the resource. If this resource doesn't
   * contain a suffix the default value <i>.tmp</i> is used.
   * 
   * @param resource
   *          The path for the resource. Must start with a <i>/</i> since each path is based on the root. Neither
   *          <code>null</code> nor empty.
   * 
   * @return The file keeping the exported content.
   */
  public static final File exportResource( String resource ) {
    Assure.nonEmpty( "resource", resource );
    Assure.assertTrue( resource.startsWith( "/" ), MSG_INVALIDRESOURCEPATH );
    String suffix = ".tmp";
    int lidx = resource.lastIndexOf( '.' );
    if( lidx != -1 ) {
      suffix = resource.substring( lidx );
    }
    return exportResource( resource, suffix );
  }

  /**
   * Exports a resource which resides on the classpath into a temporarily generated file. This file will be deleted when
   * the vm will be exited. Therefore the file is just used temporarily. The resource itself must be accessible through
   * the ClassLoader used to load this class.
   * 
   * @param resource
   *          The path for the resource. Must start with a <i>/</i> since each path is based on the root. Neither
   *          <code>null</code> nor empty.
   * @param suffix
   *          The suffix used for the exported file. Neither <code>null</code> nor empty.
   * 
   * @return The file keeping the exported content.
   */
  public static final File exportResource( String resource, String suffix ) {
    Assure.nonEmpty( "resource", resource );
    Assure.assertTrue( resource.startsWith( "/" ), MSG_INVALIDRESOURCEPATH );
    URL url = Utilities.class.getResource( resource );
    if( url == null ) {
      throw new Ant4EclipseException( CoreExceptionCode.RESOURCE_NOT_ON_THE_CLASSPATH, resource );
    }
    try {
      File result = createTempFile( "", suffix, ENCODING );
      copy( url, result );
      return result.getCanonicalFile();
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.IO_FAILURE );
    }
  }

  /**
   * Creates a directory which can be used for temporary data.
   * 
   * @return A directory which can be used for temporary data. Not <code>null</code> and is a directory.
   */
  public static final File createTempDir() {
    try {
      File result = null;
      String tempdir = cleanup( System.getProperty( PROP_A4ETEMPDIR ) );
      if( tempdir != null ) {
        File dir = new File( tempdir );
        mkdirs( dir );
        result = File.createTempFile( "a4e", "dir", dir );
      } else {
        result = File.createTempFile( "a4e", "dir" );
      }
      if( !delete( result ) ) {
        throw new Ant4EclipseException( CoreExceptionCode.IO_FAILURE );
      }
      if( !result.mkdirs() ) {
        throw new Ant4EclipseException( CoreExceptionCode.IO_FAILURE );
      }
      return result;
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.IO_FAILURE );
    }
  }

  /**
   * Writes some content into a temporary File and gives access to it.
   * 
   * @param content
   *          The content which has to be written. Neither <code>null</code> nor empty.
   * @param suffix
   *          The suffix used for the returned File. Neither <code>null</code> nor empty.
   * @param encoding
   *          The encoding that will be used to write the content. Neither <code>null</code> nor empty.
   * 
   * @return A temporary used file containing the supplied content. Not <code>null</code>.
   */
  public static final File createTempFile( String content, String suffix, String encoding ) {
    Assure.notNull( "content", content );
    Assure.nonEmpty( "encoding", encoding );
    try {
      File result = File.createTempFile( "a4e", suffix );
      writeFile( result, content, encoding );
      return result.getCanonicalFile();
    } catch( IOException ex ) {
      A4ELogging.debug( "Temp dir: %s", System.getProperty( "java.io.tmpdir" ) );
      A4ELogging.debug( "CanWrite: %s", new File( System.getProperty( "java.io.tmpdir" ) ).canWrite() );
      throw new Ant4EclipseException( ex, CoreExceptionCode.IO_FAILURE );
    }
  }

  /**
   * This function stores a file under a specified location using a chosen encoding.
   * 
   * @param destination
   *          The destination where the file has to be written to. Not <code>null</code>.
   * @param content
   *          The content that has to be written. Not <code>null</code>.
   * @param encoding
   *          The encoding that will be used to write the content. Neither <code>null</code> nor empty.
   */
  public static final void writeFile( File destination, String content, String encoding ) {
    Assure.notNull( "destination", destination );
    Assure.notNull( "content", content );
    Assure.nonEmpty( "encoding", encoding );
    OutputStream output = null;
    Writer writer = null;
    try {
      output = new FileOutputStream( destination );
      writer = new OutputStreamWriter( output, encoding );
      writer.write( content );
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.IO_FAILURE );
    } finally {
      close( writer );
      close( output );
    }
  }

  /**
   * This function stores a file under a specified location using the supplied data.
   * 
   * @param destination
   *          The destination where the file has to be written to. Not <code>null</code>.
   * @param content
   *          The content that has to be written. Not <code>null</code>.
   */
  public static final void writeFile( File destination, byte[] content ) {
    Assure.notNull( "destination", destination );
    Assure.notNull( "content", content );
    OutputStream output = null;
    try {
      output = new FileOutputStream( destination );
      output.write( content );
    } catch( IOException ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.FILEIO_FAILURE, destination );
    } finally {
      close( output );
    }
  }

  /**
   * Removes a suffix from a name if it has one.
   * 
   * @param name
   *          The name which suffix has to be removed. Not <code>null</code>.
   * 
   * @return The name without the suffix.
   */
  public static final String stripSuffix( String name ) {
    Assure.notNull( "name", name );
    int lidx = name.lastIndexOf( '.' );
    if( lidx != -1 ) {
      return name.substring( 0, lidx );
    } else {
      return name;
    }
  }

  /**
   * Executes a single command. The output stream can be captured within a buffer if desired. This method does
   * <b>NOT</b> support to provide some input. Failures will cause exceptions.
   * 
   * @param exe
   *          The location of the executable. Not <code>null</code>.
   * @param output
   *          A buffer for the output stream. Maybe <code>null</code>.
   * @param args
   *          The arguments for the execution. Maybe <code>null</code>.
   */
  public static final void execute( File exe, StringBuffer output, String ... args ) {
    execute( exe, output, null, args );
  }

  /**
   * <p>
   * Expands the specified jar file to the expansion directory.
   * </p>
   * 
   * @param jarFile
   *          the jar file to expand
   * @param expansionDirectory
   *          the expansion directory
   * @throws IOException
   */
  public static synchronized final void expandJarFile( JarFile jarFile, File expansionDirectory ) {

    Assure.notNull( "jarFile", jarFile );
    Assure.notNull( "expansionDirectory", expansionDirectory );

    if( expansionDirectory.exists() ) {
      A4ELogging.info( "%s|Already expanded '%s' to '%s'", Thread.currentThread().getId(), jarFile, expansionDirectory );
      return;
    }

    A4ELogging.info( "%s|Expanding '%s' to '%s'", Thread.currentThread().getId(), jarFile, expansionDirectory );

    mkdirs( expansionDirectory );

    // this way we make sure that calls to File#getParentFile always return non-null values
    expansionDirectory = expansionDirectory.getAbsoluteFile();

    Enumeration<JarEntry> entries = jarFile.entries();
    while( entries.hasMoreElements() ) {

      ZipEntry zipEntry = entries.nextElement();

      File destFile = new File( expansionDirectory, zipEntry.getName() );
      if( destFile.exists() ) {
        // a directory might already have been created
        continue;
      }

      if( zipEntry.isDirectory() ) {
        mkdirs( destFile );
      } else {
        mkdirs( destFile.getParentFile() );
        InputStream inputStream = null;
        try {
          inputStream = jarFile.getInputStream( zipEntry );
          writeFile( inputStream, destFile );
        } catch( IOException ex ) {
          throw new Ant4EclipseException( ex, CoreExceptionCode.IO_FAILURE );
        } finally {
          close( inputStream );
        }
      }

    }

  }

  private static void writeFile( InputStream inputStream, File file ) {
    Assure.notNull( "inputStream", inputStream );

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream( file );

      byte buffer[] = new byte[1024];
      int count;
      while( (count = inputStream.read( buffer, 0, buffer.length )) > 0 ) {
        fos.write( buffer, 0, count );
      }
    } catch( IOException e ) {
      /**
       * @todo [28-Jun-2009:KASI] The original code didn't take care of this, since it only closed the streams (now done
       *       within the finally sequence). Nevertheless the resource might not have been copied completely, so there's
       *       need to be a valid treatment.
       */
    } finally {
      // close open streams
      close( fos );
      close( inputStream );
    }
  }

  /**
   * Executes a single command. The output and the error stream can be captured within buffers if desired. This method
   * does <b>NOT</b> support to provide some input. Failures will cause exceptions.
   * 
   * @param exe
   *          The location of the executable. Not <code>null</code>.
   * @param output
   *          A buffer for the output stream. Maybe <code>null</code>.
   * @param error
   *          A buffer for the error stream. Maybe <code>null</code>.
   * @param args
   *          The arguments for the execution. Maybe <code>null</code>.
   */
  public static final void execute( File exe, StringBuffer output, StringBuffer error, String ... args ) {

    try {

      if( output == null ) {
        output = new StringBuffer();
      }

      if( error == null ) {
        error = new StringBuffer();
      }

      String[] cmdarray = null;
      if( args == null ) {
        cmdarray = new String[] { exe.getAbsolutePath() };
      } else {
        cmdarray = new String[args.length + 1];
        cmdarray[0] = exe.getAbsolutePath();
        System.arraycopy( args, 0, cmdarray, 1, args.length );
      }

      Process process = Runtime.getRuntime().exec( cmdarray );
      OutputCopier outcopier = new OutputCopier( process.getInputStream(), output, ENCODING );
      OutputCopier errcopier = new OutputCopier( process.getErrorStream(), error, ENCODING );
      outcopier.start();
      errcopier.start();
      int result = process.waitFor();
      if( result != 0 ) {
        A4ELogging.error( CoreExceptionCode.LAUNCHING_FAILURE.getMessage(), exe, Integer.valueOf( result ), output,
            error );
        throw new Ant4EclipseException( CoreExceptionCode.LAUNCHING_FAILURE, exe, Integer.valueOf( result ), output,
            error );
      }

    } catch( Exception ex ) {
      throw new Ant4EclipseException( ex, CoreExceptionCode.EXECUTION_FAILURE, exe );
    }

  }

  /**
   * Returns <code>true</code> if the supplied objects are equal. This function is capable to deal with
   * <code>null</code> values.
   * 
   * @param <T>
   *          The type of parameters that will be used.
   * @param o1
   *          One object. Maybe <code>null</code>.
   * @param o2
   *          Another object. Maybe <code>null</code>.
   * 
   * @return <code>true</code> <=> Both objects are equal.
   */
  public static final <T> boolean equals( T o1, T o2 ) {
    if( o1 == null ) {
      return o2 == null;
    } else if( o1 == o2 ) {
      // not necessary but efficient
      return true;
    } else {
      return o1.equals( o2 );
    }
  }

  /**
   * <p>
   * Performs a textual replacement for variables. Variables must be enclosed within {@link #OPEN} and {@link #CLOSE}.
   * </p>
   * 
   * @param template
   *          The template containing variable references. Not <code>null</code>.
   * @param replacements
   *          The replacement for the variables. Not <code>null</code>.
   * 
   * @return The evaluated result. Not <code>null</code>.
   */
  public static final String replaceTokens( String template, Map<String,String> replacements ) {
    return replaceTokens( template, replacements, OPEN, CLOSE );
  }

  /**
   * <p>
   * Performs a textual replacement for variables.
   * </p>
   * 
   * @param template
   *          The template containing variable references. Not <code>null</code>.
   * @param replacements
   *          The replacement for the variables. Not <code>null</code>.
   * @param openlit
   *          The opening literal for a variable reference. Neither <code>null</code> nor empty.
   * @param closelit
   *          The closing literal for a variable reference. Neither <code>null</code> nor empty.
   * 
   * @return The evaluated result. Not <code>null</code>.
   */
  public static final String replaceTokens( String template, Map<String,String> replacements, String openlit,
      String closelit ) {
    Assure.notNull( "template", template );
    Assure.notNull( "replacements", replacements );
    Assure.notNull( "openlit", openlit );
    Assure.notNull( "closelit", closelit );
    StringBuffer buffer = new StringBuffer( template );
    int index = buffer.indexOf( openlit );
    while( index != -1 ) {
      int next = buffer.indexOf( openlit, index + openlit.length() );
      int close = buffer.indexOf( closelit, index + openlit.length() );
      if( close == -1 ) {
        // no closing anymore available, so there's no replacement operation to be done anymore
        break;
      }
      if( (next != -1) && (next < close) ) {
        // no close for the current literal, so continue with the next candidate
        index = next;
        continue;
      }
      String key = buffer.substring( index + openlit.length(), close );
      String value = replacements.get( key );
      if( value != null ) {
        // remove the existing content
        buffer.delete( index, close + closelit.length() );
        buffer.insert( index, value );
        // this is advisable as the value might contain the key, so we're preventing a loop here
        index += value.length();
      } else {
        // no replacement value found, so go on with the next candidate
        index = close + closelit.length();
      }
      index = buffer.indexOf( openlit, index );
    }
    return buffer.toString();
  }

  /**
   * Returns <code>true</<code> if we're currently running under windows.
   * 
   * @return <code>true</code> <=> We're currently running under windows.
   */
  public static final boolean isWindows() {
    return OS.toLowerCase().startsWith( "windows" );
  }

  /**
   * Splits the supplied text into a list of lines. Only lines with content will be delivered. The returned list is
   * allowed to be altered.
   * 
   * @param text
   *          The text which has to be splitted. Not <code>null</code>.
   * 
   * @return The list of lines provided by the supplied text.
   */
  public static final List<String> splitText( String text ) {
    return splitText( text, false );
  }

  /**
   * Splits the supplied text into a list of lines. Only lines with content will be delivered. The returned list is
   * allowed to be altered.
   * 
   * @param text
   *          The text which has to be splitted. Not <code>null</code>.
   * @param incall
   *          <code>true</code> <=> Include all lines.
   * 
   * @return The list of lines provided by the supplied text.
   */
  public static final List<String> splitText( String text, boolean incall ) {
    List<String> result = new ArrayList<String>();
    BufferedReader reader = new BufferedReader( new StringReader( text ) );
    try {
      String line = reader.readLine();
      while( line != null ) {
        if( incall || (Utilities.cleanup( line ) != null) ) {
          result.add( line );
        }
        line = reader.readLine();
      }
    } catch( IOException ex ) {
      // as we're only working within memory this shouldn't happen
      throw new Ant4EclipseException( CoreExceptionCode.IO_FAILURE );
    }
    return result;
  }

  public static final String toString( List<?> objects ) {
    return toString( null, objects );
  }
  
  public static final String toString( String delimiter, List<?> objects ) {
    if( delimiter == null ) {
      delimiter = ",";
    }
    StringBuffer buffer = new StringBuffer();
    if( (objects != null) && (objects.size() > 0) ) {
      buffer.append( String.valueOf( objects.get(0) ) );
      for( int i = 1; i < objects.size(); i++ ) {
        buffer.append( delimiter );
        buffer.append( String.valueOf( objects.get(i) ) );
      }
    }
    return buffer.toString();
  }

  /**
   * Simple Thread extension that copies content from an InputStream into StringBuffer.
   * 
   * @author Daniel Kasmeroglu
   */
  private static final class OutputCopier extends Thread {

    private BufferedReader _source;

    private StringBuffer   _receiver;

    /**
     * Initalises this copiying process.
     * 
     * @param instream
     *          The stream which provides the content. Not <code>null</code>.
     * @param dest
     *          The destination buffer used to get the output. Maybe <code>null</code>.
     * @param encoding
     *          The encoding to be used while accessing the strem. Not <code>null</code>.
     */
    public OutputCopier( InputStream instream, StringBuffer dest, String encoding ) throws UnsupportedEncodingException {
      if( encoding == null ) {
        encoding = ENCODING;
      }
      _source = new BufferedReader( new InputStreamReader( instream, encoding ) );
      _receiver = dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      try {
        String line = _source.readLine();
        while( line != null ) {
          _receiver.append( line );
          _receiver.append( NL );
          line = _source.readLine();
        }
      } catch( IOException ex ) {
        /** @todo [06-Aug-2009:KASI] We might need something more precise here. */
        throw new Ant4EclipseException( ex, CoreExceptionCode.IO_FAILURE );
      }
    }

  } /* ENDCLASS */

} /* ENDCLASS */
