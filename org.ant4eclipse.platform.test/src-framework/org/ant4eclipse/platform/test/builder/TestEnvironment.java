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
package org.ant4eclipse.platform.test.builder;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.ant4eclipse.core.logging.A4ELogging;

/**
 * The Test Environment contains a set of folder that are created before and removed after a test case.
 * 
 * @TODO this is work in progress...
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TestEnvironment {

  /**
   * should {@link #dispose()} remove the created directories?
   * 
   * can be set to <tt>false</tt> to avoid removing the directories (can be useful for debug purposes)
   */
  private final static boolean REMOVE_ON_DISPOSE = false;

  /**
   * The root directory of the test environment
   * 
   * <p>
   * <b>NOTE!</b> this directory will be deleted recursivley!
   */
  private File                 _rootDir;

  public TestEnvironment() throws Exception {
    init();
  }

  protected void init() throws Exception {
    _rootDir = new File(System.getProperty("java.io.tmpdir"), "a4etest");
    if (_rootDir.exists()) {
      removeDirectoryTree(_rootDir);
    }
    A4ELogging.debug("Create test dir: " + _rootDir);
    FileHelper.createDirectory(_rootDir);
  }

  public void dispose() throws Exception {
    if (_rootDir != null && REMOVE_ON_DISPOSE) {
      A4ELogging.debug("Remove test dir: " + _rootDir);
      removeDirectoryTree(_rootDir);
      _rootDir = null;
    }
  }

  public File createSubDirectory(String name) {
    assertNotNull(name);

    File subdir = new File(_rootDir, name);
    FileHelper.createDirectory(subdir);
    return subdir;
  }

  protected void removeDirectoryTree(File directory) throws Exception {
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
