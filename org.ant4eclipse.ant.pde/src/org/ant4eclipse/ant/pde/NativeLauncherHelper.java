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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.internal.tools.ExpansionDirectory;
import org.ant4eclipse.lib.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.FileList.FileName;

import java.io.File;

/**
 * <p>
 * Helper class to create an ant {@link FileList} that contains all files that belongs to a native launcher for a
 * specific platform (e.g. eclipse.exe and eclipsec.exe).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class NativeLauncherHelper {

  /** the constant EXECUTABLE_ROOT */
  private static final String EXECUTABLE_ROOT                        = "bin";

  /** the constant FEATURE_ORG_ECLIPSE_EQUINOX_EXECUTABLE */
  private static final String FEATURE_ORG_ECLIPSE_EQUINOX_EXECUTABLE = "org.eclipse.equinox.executable";

  /** the constant MSG_FAILED_TO_LOOKUP_EXECUTABLES */
  private static final String MSG_FAILED_TO_LOOKUP_EXECUTABLES       = "The lookup of eclipse executables within the target platform locations failed !";

  /**
   * <p>
   * </p>
   * 
   * @param targetPlatform
   *          the target platform
   * 
   * @return the file list that contains all files that belongs to a native launcher for a specific platform (e.g.
   *         eclipse.exe and eclipsec.exe).
   */
  public static FileList getNativeLauncher(TargetPlatform targetPlatform) {

    // 1. step: try to get native launchers from the executable feature (feature 'org.eclipse.equinox.executable')
    FileList fileList = getNativeLauncherFromExecutableFeature(targetPlatform);

    if (fileList != null) {
      return fileList;
    }

    // 2. step: try to load the native launcher form the underlying eclipse installation
    return getNativeLauncherFromEclipseInstallation(targetPlatform);
  }

  /**
   * <p>
   * Return the launcher.
   * </p>
   * 
   * @param targetPlatform
   * @return the file list that contains all files that belongs to a native launcher for a specific platform (e.g.
   *         eclipse.exe and eclipsec.exe).
   */
  private static FileList getNativeLauncherFromExecutableFeature(TargetPlatform targetPlatform) {

    // get the executable feature from the target platform
    FeatureDescription featureDescription = targetPlatform
        .getFeatureDescription(FEATURE_ORG_ECLIPSE_EQUINOX_EXECUTABLE);

    // return null if feature doesn't exist
    if (featureDescription == null) {
      return null;
    }

    // get the platform configuration
    String ws = targetPlatform.getTargetPlatformConfiguration().getWindowingSystem();
    String os = targetPlatform.getTargetPlatformConfiguration().getOperatingSystem();
    String arch = targetPlatform.getTargetPlatformConfiguration().getArchitecture();

    // the feature 'org.eclipse.equinox.executable' must be a jar or a directory
    if (featureDescription.isFeatureProject()) {
      // TODO
      throw new RuntimeException(
          "The feature 'org.eclipse.equinox.executable' has to be a exported feature, not a feature project.");
    }

    // case 1: feature 'org.eclipse.equinox.executable' is a directory
    if (featureDescription.isDirectory()) {

      // get the platform specific launcher directory
      File rootDir = new File((File) featureDescription.getSource(), EXECUTABLE_ROOT + File.separatorChar + ws
          + File.separatorChar + os + File.separatorChar + arch);

      // return the file list
      return getAllChildren(rootDir);

    }

    // case 2: feature 'org.eclipse.equinox.executable' is a jar file
    else if (featureDescription.isJarFile()) {

      File expansionDir = ExpansionDirectory.getExpansionDir();

      File jaredFeature = (File) featureDescription.getSource();

      Expand expand = new Expand();
      expand.setProject(new Project());
      expand.setSrc(jaredFeature);

      File tempDir = new File(expansionDir, "org.eclipse.equinox.executable"
          + featureDescription.getFeatureManifest().getVersion());
      expand.setDest(tempDir);

      PatternSet patternset = new PatternSet();
      patternset.createInclude().setName(
          EXECUTABLE_ROOT + File.separatorChar + ws + File.separatorChar + os + File.separatorChar + arch
              + File.separatorChar + "**");

      expand.addPatternset(patternset);

      expand.execute();

      // get the platform specific launcher directory
      File rootDir = new File(tempDir, EXECUTABLE_ROOT + File.separatorChar + ws + File.separatorChar + os
          + File.separatorChar + arch);

      // return the file list
      return getAllChildren(rootDir);
    }

    // could not happen
    throw new RuntimeException("Unknown deployment format for feature 'org.eclipse.equinox.executable'.");
  }

  /**
   * <p>
   * </p>
   * 
   * @param targetPlatform
   * @return the file list that contains all files that belongs to a native launcher for a specific platform (e.g.
   *         eclipse.exe and eclipsec.exe).
   */
  private static FileList getNativeLauncherFromEclipseInstallation(TargetPlatform targetPlatform) {

    // Properties launcherProperties = null;
    //
    // try {
    // launcherProperties = new Properties();
    // launcherProperties.load(ExecutableFinder.class.getClassLoader().getResourceAsStream(
    // "org/ant4eclipse/ant/pde/nativelauncher.properties"));
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // return null;
    // }
    //
    // System.err.println(launcherProperties.get(ws + "." + os + "." + arch));

    // START
    // TODO: USE mativeeclipse.properties
    String guiexe = "eclipse";
    String cmdexe = "eclipse";

    if ("win32".equals(targetPlatform.getTargetPlatformConfiguration().getOperatingSystem())) {

      // for windows we've got two different executables
      guiexe = "eclipse.exe";
      cmdexe = "eclipsec.exe";
    }

    File targetLocation = null;

    File[] targetlocations = targetPlatform.getLocations();
    for (File loc : targetlocations) {
      File eclipseExe = new File(loc, guiexe);
      File eclipsecExe = new File(loc, cmdexe);
      if (eclipseExe.isFile() || eclipsecExe.isFile()) {
        targetLocation = loc;
        break;
      }
    }

    //
    if (targetLocation == null) {
      throw new BuildException(MSG_FAILED_TO_LOOKUP_EXECUTABLES);
    }

    FileList fileList = new FileList();
    fileList.setDir(targetLocation);

    FileName fileName = new FileList.FileName();
    fileName.setName(guiexe);
    fileList.addConfiguredFile(fileName);

    fileName = new FileList.FileName();
    fileName.setName(cmdexe);
    fileList.addConfiguredFile(fileName);

    return fileList;
  }

  /**
   * <p>
   * Helper method that returns all files contained in the given directory as a file list.
   * </p>
   * 
   * @param directory
   *          the root directory
   * @return all files contained in the given directory as a file list.
   */
  private static FileList getAllChildren(File directory) {

    // assert that directory is a directory
    Assure.isDirectory(directory);

    // create the result file list
    FileList fileList = new FileList();
    fileList.setDir(directory);

    // add all children to the file list
    for (File child : Utilities.getAllChildren(directory)) {
      FileName fileName = new FileList.FileName();
      fileName.setName(child.getAbsolutePath().substring(directory.getAbsolutePath().length() + 1));
      fileList.addConfiguredFile(fileName);
    }

    // return the result
    return fileList;
  }

}
