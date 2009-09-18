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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
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

  public static final String PROP_A4ETEMPDIR = "ant4eclipse.temp";

  public static final String NL              = System.getProperty("line.separator");

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
  public static final void close(InputStream stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException ex) {
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
  public static final void close(OutputStream stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException ex) {
        // generally not interesting so a warning is apropriate here
        A4ELogging.warn(ex.getMessage());
      }
    }
  }

  /**
   * Closes the supplied writer if it's available.
   * 
   * @param writer
   *          The writer which has to be closed. Maybe <code>null</code>.
   */
  public static final void close(Writer writer) {
    if (writer != null) {
      try {
        writer.close();
      } catch (IOException ex) {
        // generally not interesting so a warning is apropriate here
        A4ELogging.warn(ex.getMessage());
      }
    }
  }

  public static final URL toURL(File file) {
    Assert.notNull(file);
    URI uri = file.toURI();
    try {
      return uri.toURL();
    } catch (MalformedURLException e) {
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
  public static final boolean delete(File file) {
    Assert.notNull(file);
    if (!file.exists()) {
      return true;
    }
    boolean result = true;
    if (file.isDirectory()) {
      // delete the children
      File[] children = file.listFiles();
      if (children != null) {
        for (File element : children) {
          result = delete(element) && result;
        }
      }
    }
    // try to delete the file multiple times,
    // since the deletion query may fail (f.e.
    // if another process locked the file)
    int tries = 5;
    while ((!file.delete()) && (tries > 0)) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException ex) {
        // do nothing here
      }
      tries--;
    }
    if (file.exists()) {
      A4ELogging.warn("Failed to delete '%s' !", file.getPath());
      result = false;
    }
    return result;
  }

  public static final List<File> getAllChildren(File file) {
    Assert.notNull(file);

    List<File> result = new LinkedList<File>();

    if (file.isDirectory()) {

      // add the children
      File[] children = file.listFiles();
      if (children != null) {
        for (File element : children) {
          if (element.isFile()) {
            result.add(element);
          } else {
            result.addAll(getAllChildren(element));
          }
        }
      }
    } else {
      result.add(file);
    }
    return result;
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
  public static final String replace(String input, String search, String replacement) {
    Assert.notNull(input);
    Assert.notNull(search);
    Assert.notNull(replacement);
    int idx = input.indexOf(search);
    if (idx == -1) {
      return input;
    }
    String before = "";
    String after = "";
    if (idx > 0) {
      before = input.substring(0, idx);
    }
    if (idx + search.length() < input.length()) {
      after = input.substring(idx + search.length());
    }
    return before + replacement + replace(after, search, replacement);
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
  public static final String calcRelative(File fromfile, File tofile) {
    String frompath = null;
    String topath = null;
    try {
      frompath = fromfile.getCanonicalPath();
    } catch (IOException ex) {
      return (null);
    }
    try {
      topath = tofile.getCanonicalPath();
    } catch (IOException ex) {
      return (null);
    }
    String[] fromstr = frompath.replace('\\', '/').split("/");
    String[] tostr = topath.replace('\\', '/').split("/");

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
    StringBuffer buffer = new StringBuffer();
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
  public static final String removeTrailingPathSeparator(String path) {
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
  public static final boolean hasText(String str) {
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
   * Alters the supplied String to make sure that it has a value.
   * 
   * @param input
   *          The String that might be altered. Maybe <code>null</code>.
   * 
   * @return <code>null</code> in case there's no value or a String which contains at least one valuable character.
   */
  public static final String cleanup(String input) {
    String result = input;
    if (result != null) {
      result = result.trim();
      if (result.length() == 0) {
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
  public static final String[] cleanup(String[] input) {
    List<String> result = new ArrayList<String>();
    if (input != null) {
      for (String element2 : input) {
        String element = cleanup(element2);
        if (element != null) {
          result.add(element);
        }
      }
      if (result.isEmpty()) {
        return null;
      }
    }
    return result.toArray(new String[result.size()]);
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
  public static final String replace(String input, char ch, String replacement) {
    StringBuffer buffer = new StringBuffer();
    String searchstr = String.valueOf(ch);
    StringTokenizer tokenizer = new StringTokenizer(input, searchstr, true);
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
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
  public static final String toString(Properties properties) {
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
  public static final String toString(String title, Properties properties) {
    StringBuilder buffer = new StringBuilder();
    if (title != null) {
      buffer.append(title);
      buffer.append(NL);
    }
    Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
    while (it.hasNext()) {
      Entry<Object, Object> entry = it.next();
      buffer.append("'").append(entry.getKey());
      buffer.append("' -> '").append(entry.getValue()).append("'");
      buffer.append(NL);
    }
    return (buffer.toString());
  }

  /**
   * Reads the properties from the supplied resource.
   * 
   * @param resource
   *          The resource providing the content. Not <code>null</code>.
   * 
   * @return A map of properties providing the settings. Not <code>null</code>.
   */
  public static final Map<String, String> readProperties(URL resource) throws IOException {
    InputStream instream = null;
    Map<String, String> result = null;
    try {
      instream = resource.openStream();
      result = readProperties(instream);
    } finally {
      close(instream);
    }
    return result;
  }

  /**
   * Reads the properties from the supplied InputStream instance.
   * 
   * @param propertiesFile
   *          The File providing the content. Not <code>null</code>.
   * 
   * @return A map of properties providing the read content. If <code>null</code> the settings could not be loaded.
   */
  public static final Map<String, String> readProperties(File propertiesFile) {
    FileInputStream fis = null;
    Map<String, String> result = null;
    try {
      fis = new FileInputStream(propertiesFile);
      result = readProperties(fis);
      A4ELogging.debug("Read settings from '%s'", propertiesFile.getAbsolutePath());
    } catch (IOException ex) {
      /**
       * @todo [28-Jun-2009:KASI] The caller should handle this exception has he has the knowledge how this should be
       *       treated.
       */
      A4ELogging.warn("Could not load settings file '%s': '%s", propertiesFile.getAbsolutePath(), ex.toString());
    } finally {
      close(fis);
    }
    return result;
  }

  /**
   * Reads the properties from the supplied InputStream instance.
   * 
   * @param instream
   *          The InputStream providing the content. Maybe <code>null</code>.
   * 
   * @return A map of properties providing the read content. Not <code>null</code>.
   * 
   * @throws IOException
   *           Loading the properties from the InputStream failed.
   */
  public static final Map<String, String> readProperties(InputStream instream) throws IOException {
    Map<String, String> result = new Hashtable<String, String>();
    if (instream != null) {
      Properties properties = new Properties();
      properties.load(instream);
      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        result.put((String) entry.getKey(), (String) entry.getValue());
      }
    }
    return result;
  }

  /**
   * @todo [28-Jun-2009:KASI] The return type should be Map<String,String> in order to provide a datatype making use of
   *       generics. This would cause some changes within APIs which are uncritical but I don't intend to rewrite APIs
   *       without further discussion.
   */
  public static final Properties readPropertiesFromClasspath(String name) {

    ClassLoader classLoader = Utilities.class.getClassLoader();

    InputStream inputStream = classLoader.getResourceAsStream(name);

    if (inputStream == null) {
      return null;
    }

    Properties profileProperties = new Properties();

    try {
      profileProperties.load(inputStream);
      close(inputStream);
      return profileProperties;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Creates the given directory (including all of its missing parent directories) if it does not exists yet.
   * 
   * @param directory
   */
  public static final void mkdirs(File directory) {
    Assert.notNull("The parameter 'directory' must not be null", directory);
    if (directory.isDirectory()) {
      return; // already there
    }

    if (directory.isFile()) {
      throw new Ant4EclipseException(CoreExceptionCode.PATH_MUST_NOT_BE_A_FILE, directory);
    }

    if (!directory.mkdirs()) {
      throw new Ant4EclipseException(CoreExceptionCode.DIRECTORY_COULD_NOT_BE_CREATED, directory);
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
      throw new Ant4EclipseException(ex, CoreExceptionCode.COULD_NOT_LOAD_CLASS, className, ex.toString());
    }

    // try to instantiate using default cstr...
    T object = null;

    try {
      object = (T) clazz.newInstance();
    } catch (Exception ex) {
      throw new Ant4EclipseException(ex, CoreExceptionCode.COULD_NOT_INSTANTIATE_CLASS, className, ex.toString());
    }

    // return the constructed object
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
  public static final boolean contains(String candidate, String... allowed) {
    Assert.notNull("The parameter 'candidate' must not be null", candidate);
    Assert.notNull("The parameter 'allowed' must not be null", allowed);
    for (String part : allowed) {
      if (candidate.equals(part)) {
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
  public static final List<Object> filter(List<Object> input, Class<?> clazz) {
    List<Object> result = new ArrayList<Object>();
    for (int i = 0; i < input.size(); i++) {
      if (clazz.isAssignableFrom(input.get(i).getClass())) {
        result.add(input.get(i));
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
  public static final void copy(URL source, File dest) {
    InputStream instream = null;
    OutputStream outstream = null;
    try {
      instream = source.openStream();
      outstream = new FileOutputStream(dest);
      byte[] buffer = new byte[16384];
      copy(instream, outstream, buffer);
    } catch (IOException ex) {
      throw new Ant4EclipseException(ex, CoreExceptionCode.COULD_NOT_EXPORT_RESOURCE, source.toExternalForm(), dest);
    }
  }

  /**
   * Unpacks the content from the supplied zip file into the supplied destination directory.
   * 
   * @param zipfile
   *          The zip file which has to be unpacked. Not <code>null</code> and must be a file.
   * @param destdir
   *          The directory where the content shall be written to. Not <code>null</code>.
   */
  public static final void unpack(File zipfile, File destdir) {
    Assert.notNull(zipfile);
    Assert.notNull(destdir);
    byte[] buffer = new byte[16384];
    try {
      if (!destdir.isAbsolute()) {
        destdir = destdir.getAbsoluteFile();
      }
      ZipFile zip = new ZipFile(zipfile);
      Enumeration<? extends ZipEntry> entries = zip.entries();
      while (entries.hasMoreElements()) {
        ZipEntry zentry = entries.nextElement();
        if (zentry.isDirectory()) {
          mkdirs(new File(destdir, zentry.getName()));
        } else {
          File destfile = new File(destdir, zentry.getName());
          mkdirs(destfile.getParentFile());
          copy(zip.getInputStream(zentry), new FileOutputStream(destfile), buffer);
        }
      }
    } catch (IOException ex) {
      throw new Ant4EclipseException(ex, CoreExceptionCode.UNPACKING_FAILED, zipfile);
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
  public static final void copy(InputStream instream, OutputStream outstream, byte[] buffer) throws IOException {
    Assert.notNull(instream);
    Assert.notNull(outstream);
    Assert.notNull(buffer);
    try {
      int read = instream.read(buffer);
      while (read != -1) {
        if (read > 0) {
          outstream.write(buffer, 0, read);
        }
        read = instream.read(buffer);
      }
    } finally {
      close(outstream);
      close(instream);
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
  public static final File exportResource(String resource) {
    Assert.nonEmpty(resource);
    Assert.assertTrue(resource.startsWith("/"), "Exporting a resource is only supported for root based pathes !");
    String suffix = ".tmp";
    int lidx = resource.lastIndexOf('.');
    if (lidx != -1) {
      suffix = resource.substring(lidx);
    }
    return exportResource(resource, suffix);
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
  public static final File exportResource(String resource, String suffix) {
    Assert.nonEmpty(resource);
    Assert.assertTrue(resource.startsWith("/"), "Exporting a resource is only supported for root based pathes !");
    URL url = Utilities.class.getResource(resource);
    if (url == null) {
      throw new Ant4EclipseException(CoreExceptionCode.RESOURCE_NOT_ON_THE_CLASSPATH, resource);
    }
    try {
      File result = File.createTempFile("a4e", suffix);
      copy(url, result);
      return result.getCanonicalFile();
    } catch (IOException ex) {
      throw new Ant4EclipseException(CoreExceptionCode.IO_FAILURE);
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
      String tempdir = cleanup(System.getProperty(PROP_A4ETEMPDIR));
      if (tempdir != null) {
        File dir = new File(tempdir);
        mkdirs(dir);
        result = File.createTempFile("a4e", "dir", dir);
      } else {
        result = File.createTempFile("a4e", "dir");
      }
      if (!delete(result)) {
        throw new Ant4EclipseException(CoreExceptionCode.IO_FAILURE);
      }
      if (!result.mkdirs()) {
        throw new Ant4EclipseException(CoreExceptionCode.IO_FAILURE);
      }
      return result;
    } catch (IOException ex) {
      throw new Ant4EclipseException(CoreExceptionCode.IO_FAILURE);
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
  public static final File createFile(String content, String suffix, String encoding) {
    Assert.notNull(content);
    Assert.nonEmpty(encoding);
    try {
      File result = File.createTempFile("a4e", suffix);
      writeFile(result, content, encoding);
      return result.getCanonicalFile();
    } catch (IOException ex) {
      throw new Ant4EclipseException(CoreExceptionCode.IO_FAILURE);
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
  public static final void writeFile(File destination, String content, String encoding) {
    Assert.notNull(destination);
    Assert.notNull(content);
    Assert.nonEmpty(encoding);
    OutputStream output = null;
    Writer writer = null;
    try {
      // check if the file can be written
      if (destination.exists() && (!destination.canWrite())) {
        throw new RuntimeException("Could not create file: " + destination);
      }
      output = new FileOutputStream(destination);
      writer = new OutputStreamWriter(output, encoding);
      writer.write(content);
    } catch (IOException ex) {
      throw new Ant4EclipseException(CoreExceptionCode.IO_FAILURE);
    } finally {
      Utilities.close(writer);
      Utilities.close(output);
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
  public static final String stripSuffix(String name) {
    Assert.notNull(name);
    int lidx = name.lastIndexOf('.');
    if (lidx != -1) {
      return name.substring(0, lidx);
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
  public static final void execute(File exe, StringBuffer output, String... args) {
    execute(exe, output, null, args);
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
  public static final void execute(File exe, StringBuffer output, StringBuffer error, String... args) {

    try {

      if (output == null) {
        output = new StringBuffer();
      }

      if (error == null) {
        error = new StringBuffer();
      }

      String[] cmdarray = null;
      if (args == null) {
        cmdarray = new String[] { exe.getAbsolutePath() };
      } else {
        cmdarray = new String[args.length + 1];
        cmdarray[0] = exe.getAbsolutePath();
        System.arraycopy(args, 0, cmdarray, 1, args.length);
      }

      Process process = Runtime.getRuntime().exec(cmdarray);
      OutputCopier outcopier = new OutputCopier(process.getInputStream(), output);
      OutputCopier errcopier = new OutputCopier(process.getErrorStream(), error);
      outcopier.start();
      errcopier.start();
      int result = process.waitFor();
      if (result != 0) {
        A4ELogging.error(CoreExceptionCode.LAUNCHING_FAILURE.getMessage(), exe, Integer.valueOf(result), output, error);
        throw new Ant4EclipseException(CoreExceptionCode.LAUNCHING_FAILURE, exe, Integer.valueOf(result), output, error);
      }

    } catch (Exception ex) {
      throw new Ant4EclipseException(ex, CoreExceptionCode.EXECUTION_FAILURE, exe);
    }

  }

  /**
   * Simple Thread extension that copies content from an InputStream into StringBuffer.
   * 
   * @author Daniel Kasmeroglu
   */
  private static class OutputCopier extends Thread {

    private BufferedReader _source;

    private StringBuffer   _receiver;

    /**
     * Initalises this copiying process.
     * 
     * @param instream
     *          The stream which provides the content. Not <code>null</code>.
     * @param dest
     *          The destination buffer used to get the output. Not <code>null</code>.
     */
    public OutputCopier(InputStream instream, StringBuffer dest) {
      this._source = new BufferedReader(new InputStreamReader(instream));
      this._receiver = dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      try {
        String line = this._source.readLine();
        while (line != null) {
          this._receiver.append(line);
          this._receiver.append(NL);
          line = this._source.readLine();
        }
      } catch (IOException ex) {
        /** @todo [06-Aug-2009:KASI] We might need something more precise here. */
        throw new Ant4EclipseException(ex, CoreExceptionCode.IO_FAILURE);
      }
    }

  } /* ENDCLASS */

} /* ENDCLASS */
