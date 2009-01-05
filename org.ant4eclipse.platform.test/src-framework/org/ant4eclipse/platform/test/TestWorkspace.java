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
package org.ant4eclipse.platform.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.ant4eclipse.platform.test.builder.FileHelper;

/**
 * The Test Environment contains a set of folder that are created before and removed after a test case.
 * 
 * @TODO this is work in progress...
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TestWorkspace {

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

  public TestWorkspace() throws Exception {
    init();
  }

  public TestWorkspace(boolean removeOnDispose) throws Exception {
    this._removeOnDispose = removeOnDispose;
    init();
  }

  protected void init() throws Exception {
    this._rootDir = new File(System.getProperty("java.io.tmpdir"), "a4etest");
    if (this._rootDir.exists()) {
      removeDirectoryTree(this._rootDir);
    }
    System.out.println("Create test dir: " + this._rootDir);
    FileHelper.createDirectory(this._rootDir);
  }

  public void dispose() throws Exception {
    if (this._rootDir != null && this._removeOnDispose) {
      System.out.println("Remove test dir: " + this._rootDir);
      removeDirectoryTree(this._rootDir);
      this._rootDir = null;
    }
  }

  /**
   * Creates the file fileName with the given content in the root folder of the test environment
   * 
   * @param fileName
   * @param content
   * @throws IOException
   */
  public File createFile(String fileName, String content) throws IOException {
    File outFile = new File(this._rootDir, fileName);
    FileHelper.createFile(outFile, content);
    return outFile;
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

  private void removeDirectoryTree(File directory) throws Exception {
    if (directory != null && directory.exists()) {
      System.gc();
      FileHelper.removeDirectoryTree(directory.getAbsolutePath());
      if (directory.exists()) {
        System.err.println("Warn! Could not remove directory '" + directory + "' trying again...");
        System.gc();
        Thread.sleep(1000);
        FileHelper.removeDirectoryTree(directory.getAbsolutePath());
        if (directory.exists()) {
          System.err.println("WARN! UNABLE TO DELETE TEST DIRECTORY '" + directory + "'");
        }
      }
    }
  }
}
