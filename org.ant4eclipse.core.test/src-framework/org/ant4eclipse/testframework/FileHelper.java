/**********************************************************************
 * Copyright (c) 2005-2006 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.testframework;

import static java.lang.String.format;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

public class FileHelper {

  private static final byte[] BEGIN_CHUNK = { 64, -79, -117, -127, 35, -68, 0, 20, 26, 37, -106, -25, -93, -109, -66,
      30                                 };

  private static final byte[] END_CHUNK   = { -64, 88, -5, -13, 35, -68, 0, 20, 26, 81, -13, -116, 123, -69, 119, -58 };

  /**
   * Delete a directory, his subdirectory and all files.
   * 
   * @param directoryName
   *          the name of the directory to remove.
   * @throws Exception
   *           if an error occurs.
   */
  public static void removeDirectoryTree(String directoryName) {

    File directory = new File(directoryName);

    if (!directory.exists()) {
      return;
    }

    if (!directory.isDirectory()) {
      throw new RuntimeException("'" + directory + "' is not a directory");
    }

    String[] fileList = directory.list();
    int numFile = fileList.length;
    File f = null;
    for (int i = 0; i < numFile; i++) {
      f = new File(directoryName + File.separator + fileList[i]);
      if (f.isDirectory()) {
        removeDirectoryTree(f.getPath());
      } else {
        f.delete();
      }
    }
    directory.delete();
  }

  public static void createDirectory(File directory) {
    Assert.notNull(directory);

    if (directory.isFile()) {
      throw new RuntimeException("Directory '" + directory + "' is a file");
    }
    if (directory.isDirectory()) {
      throw new RuntimeException("Directory '" + directory + "' already exists");
    }

    if (!directory.mkdirs()) {
      throw new RuntimeException("Directory '" + directory + "' could not be created for an unkown reason");
    }
  }

  public static void createFile(File file, String content) {
    Assert.notNull(file);
    Assert.notNull(content);

    try {
      if (!file.exists()) {
        if (!file.createNewFile()) {
          throw new RuntimeException("Could not create file: " + file);
        }
      }

      FileWriter fileWriter = new FileWriter(file);

      fileWriter.write(content);
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Create a new file in the specified directory using the given file name and content.
   * <p>
   * If the directory it does not exist it will be created.
   * </p>
   * <p>
   * If already exists a file with the same name, this has deleted before create the new file.
   * </p>
   * 
   * @param directoryName
   *          the directory in which the file must be created.
   * @param fileName
   *          the name of the file
   * @param content
   *          the content of the file.
   * @throws Exception
   *           if an error occurs during creation.
   */
  public static void createFile(String directoryName, String fileName, byte[] content) {

    File fileOut = new File(directoryName + File.separator + fileName);

    fileOut.mkdirs();

    if (fileOut.exists()) {
      fileOut.delete();
    }

    try {
      FileOutputStream fout = new FileOutputStream(fileOut);

      fout.write(content);
      fout.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    // System.out.println("Created file " + directoryName + "\\" + fileName);
  }

  /**
   * Create a new file in the specified directory using the given file name and byteStream.
   * <p>
   * If the directory it does not exist it will be created.
   * </p>
   * <p>
   * If already exists a file with the same name, this has deleted before create the new file.
   * </p>
   * 
   * @param directoryName
   *          the directory in which the file must be created.
   * @param fileName
   *          the name of the file
   * @param byteStream
   *          the content of the file.
   * @throws Exception
   *           if an error occurs during creation.
   */
  public static void createFile(String directoryName, String fileName, ByteArrayOutputStream byteStream) {

    File fileOut = new File(directoryName + File.separator + fileName);

    fileOut.mkdirs();

    if (fileOut.exists()) {
      fileOut.delete();
    }

    try {
      FileOutputStream fout = new FileOutputStream(fileOut);
      byteStream.writeTo(fout);
      fout.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

  }

  /**
   * Returns the content of the file specified.
   * 
   * @param fileName
   *          the name of the file.
   * @return the content of the file.
   * @throws IOException
   *           if the file not exits or if an I/O error occurs.
   */
  public static byte[] getFile(String fileName) {
    File file = new File(fileName);
    int dim = (int) file.length();
    byte[] content = new byte[dim];

    try {
      BufferedInputStream stream = new BufferedInputStream(new FileInputStream(fileName));
      stream.read(content);
      stream.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return content;
  }

  /**
   * Returns the content of the resource from the classpath
   * 
   * @param resourceName
   *          the name of the resource that should be loaded from the classpath
   * @return the content of the resource
   * @throws IOException
   *           if the file not exits or if an I/O error occurs.
   */
  public static String getResource(String resourceName) {

    InputStream inputStream = FileHelper.class.getResourceAsStream("/" + resourceName);
    if (inputStream == null) {
      throw new RuntimeException(format("Resource '%s' not found on classpath!", resourceName));
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      int b;
      while ((b = inputStream.read()) != -1) {
        out.write(b);
      }
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      Utilities.close(out);
      Utilities.close(inputStream);
    }
    return out.toString();
  }

  /**
   * Returns the content of the file specified.
   */
  public static byte[] getFileFiltered(String fileName, char tokenSep, Map<String, String> filter) {
    try {
      File file = new File(fileName);
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = null;
      StringBuffer buffer = new StringBuffer();
      while ((line = reader.readLine()) != null) {
        try {
          line = replaceTokens(line, tokenSep, filter);
        } catch (NoSuchElementException e) {
          A4ELogging.debug(e.getMessage());
          throw new RuntimeException("Could filter file " + fileName + ": " + e, e);
        }
        buffer.append(line);
        buffer.append(System.getProperty("line.separator"));
      }
      return buffer.toString().getBytes();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Returns the content of the file specified.
   */
  public static byte[] getBinaryFileFiltered(String fileName, Map<String, String> filter) {
    File file = new File(fileName);
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = reader.readLine();
      while (line != null) {
        String value = filter.get(line);
        if (value != null) {
          byteout.write(BEGIN_CHUNK);
          DataOutputStream dataout = new DataOutputStream(byteout);
          dataout.writeUTF(value);
          byteout.write(END_CHUNK);
        }
        line = reader.readLine();
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return byteout.toByteArray();
  }

  /**
   * Returns all files in the given directory filtering on the given extension. Pathnames are returned relative to
   * <i>directory</i>
   * 
   * @param directory
   *          the directory to list
   * @param root
   *          the path of the root which will be removed to create the pathes
   * 
   * @return String[] with the selected filenames. An zero-length array if no files are selected
   */
  public static String[] getAllFiles(String directory, String root) {

    Vector<String> fileList = new Vector<String>();

    File[] files = new File(directory).listFiles();

    if ((files == null) || (files.length == 0)) {
      return new String[0];
    }

    for (int i = 0; i < files.length; ++i) {
      File f = files[i];
      if (f.isDirectory()) {
        String[] children = getAllFiles(f.getPath(), root);
        for (String element : children) {
          fileList.addElement(element);
        }
      } else if (f.isFile()) {
        String fileName = f.getPath();

        if (fileName.startsWith(root)) {
          fileName = fileName.substring(root.length());
        }

        fileList.addElement(fileName);
      }
    }

    return fileList.toArray(new String[fileList.size()]);
  }

  public static String replaceTokens(String line, char tokenSep, Map<String, String> tokens) {
    boolean inToken = false;

    StringBuffer result = new StringBuffer();
    StringBuffer currentToken = new StringBuffer();

    int i = -1;
    while (++i < line.length()) {
      char c = line.charAt(i);
      if (c == tokenSep) {
        if (!inToken) {
          inToken = true;
          currentToken = new StringBuffer();
          continue;
        }
        inToken = false;
        String replaceValue = tokens.get(currentToken.toString());
        if (replaceValue == null) {
          // No value found for current token; include error message
          throw new NoSuchElementException("NO VALUE FOUND FOR TOKEN: " + currentToken + "!");
        }
        result.append(replaceValue);
        continue;
      }
      // not a token char
      if (inToken) {
        currentToken.append(c);
      } else {
        result.append(c);
      }
    }
    return result.toString();
  }

}
