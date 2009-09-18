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
package org.ant4eclipse.testframework;

import static org.junit.Assert.assertNotNull;

import org.ant4eclipse.core.util.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Test Environment contains a set of folder that are created before and removed after a test case.
 * 
 * @TODO this is work in progress...
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TestDirectory {

  /**
   * should {@link #dispose()} remove the created directories?
   * 
   * can be set to <tt>false</tt> to avoid removing the directories (can be useful for debug purposes)
   */
  private boolean _removeOnDispose = true;

  /**
   * The root directory of the test environment
   * 
   * <p>
   * <b>NOTE!</b> this directory will be deleted recursivley!
   */
  private File    _rootDir;

  public TestDirectory() {
    init();
  }

  public TestDirectory(boolean removeOnDispose) {
    this._removeOnDispose = removeOnDispose;
    init();
  }

  protected void init() {
    this._rootDir = new File(System.getProperty("java.io.tmpdir"), "a4etest");
    if (this._rootDir.exists()) {
      if (!Utilities.delete(this._rootDir)) {
        throw new RuntimeException(String.format("Failed to delete directory '%s'.", this._rootDir));
      }
    }
    System.out.println("Create test dir: " + this._rootDir);
    FileHelper.createDirectory(this._rootDir);
  }

  public void dispose() {
    try {
      if (this._rootDir != null && this._removeOnDispose) {
        System.out.println("Remove test dir: " + this._rootDir);
        if (!Utilities.delete(this._rootDir)) {
          throw new RuntimeException(String.format("Failed to delete directory '%s'.", this._rootDir));
        }
        this._rootDir = null;
      }
    } catch (Exception ex) {
      System.err.println("WARN! Could not remove test directory " + this._rootDir + ": " + ex);
      ex.printStackTrace();
    }
  }

  /**
   * Creates the file fileName with the given content in the root folder of the test environment
   * 
   * @param fileName
   * @param content
   * @throws IOException
   */
  public File createFile(String fileName, String content) {
    File outFile = new File(this._rootDir, fileName);
    FileHelper.createFile(outFile, content);
    return outFile;
  }

  /**
   * Copies the content from the given input stream to the file.
   * 
   * <p>
   * This method closes the input stream after copying it
   * 
   * @param fileName
   *          The filename that is relative to the root of the test environment
   * @param inputStream
   *          The inputStream to read from
   * @return The file that has been createad
   * @throws IOException
   */
  public File createFile(String fileName, InputStream inputStream) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int bytesRead = -1;
    while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
      output.write(buffer, 0, bytesRead);
    }
    Utilities.close(inputStream);
    return createFile(fileName, output.toString());
  }

  public File createSubDirectory(String name) {
    assertNotNull(name);

    File subdir = new File(this._rootDir, name);
    FileHelper.createDirectory(subdir);
    return subdir;
  }

  public File getRootDir() {
    return this._rootDir;
  }

}
