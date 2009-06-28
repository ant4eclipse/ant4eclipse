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
package org.ant4eclipse.core.util;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;

/**
 * <p>
 * Collection of utility functions that aren't specific to A4E.
 * <p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class Utilities {

  /**
   * Prevent instantiation of this class.
   */
  private Utilities() {
    // Prevent instantiation of this class.
  }

  /**
   * Closes the supplied stream if it's available.
   * 
   * @param stream
   *          The stream that has to be closed. Maybe <code>null</code>.
   */
  public static final void close(final InputStream stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (final IOException ex) {
        // generally not interesting so a warning is apropriate here
        A4ELogging.warn(ex.getMessage());
      }
    }
  }

  /**
   * Closes the supplied stream if it's available.
   * 
   * @param stream
   *          The stream that has to be closed. Maybe <code>null</code>.
   */
  public static final void close(final OutputStream stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (final IOException ex) {
        // generally not interesting so a warning is apropriate here
        A4ELogging.warn(ex.getMessage());
      }
    }
  }

  public static final URL toURL(final File file) {
    Assert.notNull(file);
    final URI uri = file.toURI();
    try {
      return uri.toURL();
    } catch (final MalformedURLException e) {
      // TODO
      throw new RuntimeException(e);
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
  public static final boolean delete(final File file) {
    Assert.notNull(file);
    boolean result = true;
    if (file.isDirectory()) {
      // delete the children
      final File[] children = file.listFiles();
      if (children != null) {
        for (File element : children) {
          result = result && delete(element);
        }
      }
    }
    // try to delete the file multiple times,
    // since the deletion query may fail (f.e.
    // if another process locked the file)
    int tries = 5;
    while ((!file.delete()) && (tries > 0)) {
      // allow the garbage collector to cleanup potential references
      System.gc();
      try {
        Thread.sleep(10);
      } catch (final InterruptedException ex) {
        // do nothing here
      }
      tries--;
    }
    if (file.exists()) {
      A4ELogging.warn("Failed to delete '%s' !", file.getPath());
      result = false;
    }
    return (result);
  }

  public static final boolean hasChild(File directory, final String childName) {
    String[] children = directory.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.equals(childName);
      }
    });

    return (children.length > 0);
  }

  public static final File getChild(File directory, final String childName) {
    File[] children = directory.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.equals(childName);
      }
    });
    if (children.length < 1) {
      return null;
    }

    return children[0];
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
  public static final String calcRelative(final File fromfile, final File tofile) {
    String frompath = null;
    String topath = null;
    try {
      frompath = fromfile.getCanonicalPath();
    } catch (final IOException ex) {
      return (null);
    }
    try {
      topath = tofile.getCanonicalPath();
    } catch (final IOException ex) {
      return (null);
    }
    final String[] fromstr = frompath.replace('\\', '/').split("/");
    final String[] tostr = topath.replace('\\', '/').split("/");

    if (!fromstr[0].equals(tostr[0])) {
      // we're not working on the same device
      /**
       * @todo [26-Feb-2006:KASI] Can this be omitted under UNIX ?
       */
      return (null);
    }
    int same = 1;
    for (; same < Math.min(fromstr.length, tostr.length); same++) {
      if (!fromstr[same].equals(tostr[same])) {
        break;
      }
    }
    final StringBuffer buffer = new StringBuffer();
    for (int i = same; i < fromstr.length; i++) {
      buffer.append(File.separator + "..");
    }
    for (int i = same; i < tostr.length; i++) {
      buffer.append(File.separator + tostr[i]);
    }
    if (buffer.length() > 0) {
      buffer.delete(0, File.separator.length());
    }
    return (buffer.toString());
  }

  /**
   * removes trailing / or \\ from the given path
   * 
   * @param path
   * @return the path without a trailing path separator
   */
  public static final String removeTrailingPathSeparator(final String path) {
    if ((path == null) || (path.length() < 2)) {
      return path;
    }
    if (path.endsWith("/") || path.endsWith("\\")) {
      return path.substring(0, path.length() - 1);
    }
    return path;

  }

  /**
   * <p>
   * Check if a String has text. More specifically, returns <code>true</code> if the string not <code>null<code>, it's <code>length is > 0</code>,
   * and it has at least one non-whitespace character.
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
  public static final boolean hasText(final String str) {
    int strLen;
    if ((str == null) || ((strLen = str.length()) == 0)) {
      return false;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Replaces a character with a specified string.
   * 
   * @param input
   *          The string which will be modified.
   * @param ch
   *          The character that shall be replaced.
   * @param replacement
   *          The replacing string.
   * 
   * @return A string with replaced characters.
   */
  public static final String replace(final String input, final char ch, final String replacement) {
    final StringBuffer buffer = new StringBuffer();
    final String searchstr = String.valueOf(ch);
    final StringTokenizer tokenizer = new StringTokenizer(input, searchstr, true);
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();
      if (token.equals(searchstr)) {
        buffer.append(replacement);
      } else {
        buffer.append(token);
      }
    }
    return (buffer.toString());
  }

  /**
   * Generates a textual representation of the supplied Property map.
   * 
   * @param properties
   *          The map which shall be translated into a text.
   * 
   * @return The text representing the content of the supplied map.
   */
  public static final String toString(final Properties properties) {
    return (toString(null, properties));
  }

  /**
   * Generates a textual representation of the supplied Property map.
   * 
   * @param title
   *          A textual information printed above the property map.
   * @param properties
   *          The map which shall be translated into a text.
   * 
   * @return The text representing the content of the supplied map.
   */
  public static final String toString(final String title, final Properties properties) {
    final StringBuilder buffer = new StringBuilder();
    final String linesep = System.getProperty("line.separator");
    if (title != null) {
      buffer.append(title);
      buffer.append(linesep);
    }
    Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
    while (it.hasNext()) {
      Entry<Object, Object> entry = it.next();
      buffer.append("'").append(entry.getKey());
      buffer.append("' -> '").append(entry.getValue()).append("'");
      buffer.append(linesep);
    }
    return (buffer.toString());
  }

  public static final Map<String, String> readProperties(final File propertiesFile) {
    FileInputStream fis = null;
    Properties result = null;
    try {
      fis = new FileInputStream(propertiesFile);
      final Properties pref = new Properties();
      pref.load(fis);
      result = pref;
      fis.close();
      A4ELogging.debug("Read compiler settings from '%s'", propertiesFile.getAbsolutePath());
    } catch (final IOException ex) {
      A4ELogging.warn("Could not load settings file '%s': '%s", propertiesFile.getAbsolutePath(), ex.toString());
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (final IOException ioe) {
          // don't care...
        }
      }
    }
    Map<String, String> nresult = new Hashtable<String, String>();
    for (Map.Entry<Object, Object> entry : result.entrySet()) {
      nresult.put((String) entry.getKey(), (String) entry.getValue());
    }
    return nresult;
  }

  public static final Properties readPropertiesFromClasspath(final String name) {

    final ClassLoader classLoader = Utilities.class.getClassLoader();

    final InputStream inputStream = classLoader.getResourceAsStream(name);

    if (inputStream == null) {
      return null;
    }

    final Properties profileProperties = new Properties();

    try {
      profileProperties.load(inputStream);
      inputStream.close();
      return profileProperties;
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Creates the given directory (including all of its missing parent directories) if it does not exists yet.
   * 
   * @param directory
   */
  public static final void mkdirs(final File directory) {
    Assert.notNull("The parameter 'directory' must not be null", directory);
    if (directory.isDirectory()) {
      return; // already there
    }

    if (directory.isFile()) {
      throw new Ant4EclipseException(CoreExceptionCode.PATH_MUST_NOT_BE_A_FILE, directory);
    }

    if (!directory.mkdirs()) {
      throw new Ant4EclipseException(CoreExceptionCode.DIRECTORY_COULD_NOT_BE_CREATED);
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
  @SuppressWarnings("unchecked")
  public static final <T> T newInstance(String className) {
    Assert.notNull("The parameter 'className' must not be null", className);

    Class<?> clazz = null;

    // Try to load class...
    try {
      clazz = Class.forName(className);
    } catch (Exception ex) {
      throw new Ant4EclipseException(CoreExceptionCode.COULD_NOT_LOAD_CLASS, ex, new Object[] { className,
          ex.toString() });
    }

    // try to instantiate using default cstr...
    T object = null;

    try {
      object = (T) clazz.newInstance();
    } catch (Exception ex) {
      throw new Ant4EclipseException(CoreExceptionCode.COULD_NOT_INSTANTIATE_CLASS, ex, new Object[] { className,
          ex.toString() });
    }

    // return the constructed object
    return object;
  }
}
