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
package org.ant4eclipse.ant.jdt.base;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;

import org.ant4eclipse.ant.core.AntConfigurator;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractJdtTest extends AbstractWorkspaceBasedBuildFileTest {

  /** TEST_PATH_SEPARATOR */
  public static final String TEST_PATH_SEPARATOR = File.pathSeparator;

  /** TEST_DIR_SEPARATOR */
  public static final String TEST_DIR_SEPARATOR  = File.separator;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setupBuildFile(String unqualifiedBuildFileName) throws Exception {
    super.setupBuildFile(unqualifiedBuildFileName);
    getProject().setProperty("pathSeparator", TEST_PATH_SEPARATOR);
    getProject().setProperty("dirSeparator", TEST_DIR_SEPARATOR);
    /* KASI */ AntConfigurator.configureAnt4Eclipse(getProject());
  }

  protected void assertClasspath(String classpath, File... expectedEntries) {
    assertClasspath(classpath, TEST_PATH_SEPARATOR, TEST_DIR_SEPARATOR, expectedEntries);
  }

  protected String normalize(String path) {
    path = path.replace("/", TEST_PATH_SEPARATOR);
    path = path.replace("\\", TEST_PATH_SEPARATOR);
    return path;
  }

  /**
   * <p>
   * Makes sure that the given class path contains the expected entries
   * </p>
   * 
   * @param classpath
   * @param expectedEntries
   */
  protected void assertClasspath(String classpath, String pathSeparator, String dirSeparator, File... expectedEntries) {

    assertNotNull(classpath);

    String[] classpathItems = classpath.split(pathSeparator);
    assertEquals(expectedEntries.length, classpathItems.length);

    for (int i = 0; i < expectedEntries.length; i++) {
      try {
        File expectedDir = expectedEntries[i].getCanonicalFile();
        File classpathItem = new File(classpathItems[i]).getCanonicalFile();
        assertEquals(String.format("Classpath-Item '%d' does not match. Expected: '%s' Actual: '%s'",
            Integer.valueOf(i), expectedDir, classpathItem), expectedDir, classpathItem);
      } catch (IOException ex) {
        Assert.fail(ex.getMessage());
      }
    }
  }
}
