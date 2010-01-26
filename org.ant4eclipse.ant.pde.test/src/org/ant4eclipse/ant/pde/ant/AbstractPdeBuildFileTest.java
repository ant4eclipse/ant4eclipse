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
package org.ant4eclipse.ant.pde.ant;

import java.io.File;

import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.pde.test.EchoLogfile;

import org.ant4eclipse.ant.jdt.base.AbstractJdtTest;

/**
 * <p>
 * Abstract base class for all PDE build file tests.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AbstractPdeBuildFileTest extends AbstractJdtTest {

  /** ECLIPSE_SDK_35_WIN32 */
  public static String        ECLIPSE_SDK_35_WIN32         = "ECLIPSE_SDK_35_WIN32";

  /** ECLIPSE_SDK_35_LINUX_GTK */
  public static String        ECLIPSE_SDK_35_LINUX_GTK     = "ECLIPSE_SDK_35_LINUX_GTK";

  /** - */
  private final static String EXPECTED_TARGET_PLATFORM_LOG = "Trying to read bundles and feature from 'workspace'\\.Needed \\d* ms to read 1 bundles and 0 features from bundle set\\.Trying to read bundles and feature from 'target platform location '.*''\\.Needed \\d* ms to read 368 bundles and 15 features from bundle set\\.Trying to read bundles and feature from 'target platform location '.*''\\.Needed \\d* ms to read \\d* bundles and \\d* features from bundle set\\.";

  /** the additional target platform directory */
  private File                _additionalTargetPlatformDirectory;

  /**
   * The name of the file that contains the output of the echo-tasks in the buildfile
   */
  private String              _echoLogfileName;

  /**
   * The echo log file that was used by ant. Might be null
   */
  private EchoLogfile         _echoLogfile;

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupDefaultBuildFile();
    setupDefaultProperties();
    createAdditionalTargetPlatformDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void tearDown() throws Exception {
    if (this._echoLogfile != null) {
      this._echoLogfile.dispose();
      this._echoLogfile = null;
    }
    disposeAdditionalTargetPlatformDirectory();
    super.tearDown();
  }

  /**
   * <p>
   * Sets up the default build file.
   * </p>
   * 
   * @throws Exception
   *           the exception
   */
  protected void setupDefaultBuildFile() throws Exception {
    // set up the build file
    setupBuildFile("buildWorkspace.xml");
  }

  protected EchoLogfile getEchoLogfile() throws Exception {
    // echo log points to a file that is used to echo all messages into that are compared
    // after test execution with expected values (instead of parsing the *whole* log output)
    if (this._echoLogfile == null) {
      this._echoLogfile = new EchoLogfile(this._echoLogfileName);
    }
    return this._echoLogfile;
  }

  /**
   * <p>
   * </p>
   * 
   * @throws Exception
   */
  protected void setupDefaultProperties() throws Exception {
    this._echoLogfileName = new File(getTestWorkspaceDirectory(), "echo.log").getAbsolutePath();
    getProject().setProperty("echolog", this._echoLogfileName);
    getProject().setProperty("workspace", getTestWorkspaceDirectory().getAbsolutePath());
    getProject().setProperty("targetplatform.1", getTargetPlatformDirectory().getAbsolutePath());
    getProject().setProperty("targetplatform.2", getAdditionalTargetPlatformPath());
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected String getTargetPlatformPath(String environmentVariable) {

    // get the environment variable
    String targetPlatformPath = System.getenv(environmentVariable);

    // get the system variable
    if (!Utilities.hasText(targetPlatformPath)) {
      targetPlatformPath = System.getProperty(environmentVariable);
    }

    // throw exception
    if (!Utilities.hasText(targetPlatformPath)) {
      throw new RuntimeException(
          String
              .format(
                  "To execute a PDE build file test, you have to specify an environment or a system variable '%s' that points to an Eclipse IDE for Java Developers 3.5.0 Windows (http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/galileo/R/eclipse-java-galileo-win32.zip).",
                  environmentVariable));
    }

    // return the path
    return targetPlatformPath;
  }

  protected File getTargetPlatformDirectory() {
    return new File(getTargetPlatformPath(ECLIPSE_SDK_35_WIN32));
  }

  protected String getAdditionalTargetPlatformPath() {
    String tmpDir = System.getProperty("java.io.tmpdir");
    return tmpDir + File.separator + "ant4eclipse.second.targetplatform";
  }

  protected File getAdditionalTargetPlatformDirectory() {
    return this._additionalTargetPlatformDirectory;
  }

  protected String getExpectedTargetPlatformLog() {
    return EXPECTED_TARGET_PLATFORM_LOG;
  }

  protected void createAdditionalTargetPlatformDirectory() {
    this._additionalTargetPlatformDirectory = new File(getAdditionalTargetPlatformPath());
    if (!this._additionalTargetPlatformDirectory.exists()) {
      this._additionalTargetPlatformDirectory.mkdirs();
    }
  }

  protected void disposeAdditionalTargetPlatformDirectory() {
    File dir = getAdditionalTargetPlatformDirectory();
    try {
      if (dir != null) {
        System.out.println("Remove second.targetplatform directory: " + dir);

        if (!Utilities.delete(dir)) {
          throw new RuntimeException(String.format("Failed to delete directory '%s'.", dir));
        }
        dir = null;
      }
    } catch (Exception ex) {
      System.err.println("WARN! Could not remove second.targetplatform directory " + dir + ": " + ex);
      ex.printStackTrace();
    }
  }
}
