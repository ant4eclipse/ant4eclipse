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
package org.ant4eclipse.lib.jdt.internal.model.jre;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.data.Version;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.JdtExceptionCode;
import org.ant4eclipse.lib.jdt.internal.model.jre.support.LibraryDetector;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry;

public class JavaRuntimeLoader {
  /**  */
  private static final String JAVA_SPECIFICATION_NAME         = "java.specification.name";

  /**  */
  private static final String JAVA_SPECIFICATION_VERSION      = "java.specification.version";

  /** J2ME configuration property name */
  private static final String J2ME_MICROEDITION_CONFIGURATION = "microedition.configuration"; //$NON-NLS-1$

  /** J2ME profile property name */
  private static final String J2ME_MICROEDITION_PROFILES      = "microedition.profiles";      //$NON-NLS-1$

  /**  */
  private static String       J2SE                            = "J2SE-";                      //$NON-NLS-1$

  /**  */
  private static String       JAVASE                          = "JavaSE-";                    //$NON-NLS-1$

  /**
   * @param id
   * @param location
   * @param files
   *          the list of (jar-)files defining this java runtime or null if the file should be determined from the
   *          JavaRuntime's location
   */
  public static JavaRuntime loadJavaRuntime(String id, File location, String extDirs, String endorsedDirs,
      List<File> files) {
    Assure.nonEmpty("id", id);
    Assure.isDirectory("location", location);

    String outfileName = System.getProperty("java.io.tmpdir") + File.separatorChar + "ant4eclipse_jdk_props_"
        + Math.round(Math.random() * 1000000000);
    // System.out.println(outfileName);

    JavaExecuter javaLauncher = JavaExecuter.createWithA4eClasspath(location);
    javaLauncher.setMainClass(LibraryDetector.class.getName());
    javaLauncher.setArgs(new String[] { outfileName });

    javaLauncher.execute();

    // TODO
    StringBuffer contents = new StringBuffer();
    try {
      File file = new File(outfileName);
      contents = Utilities.readTextContent(file, Utilities.ENCODING, false);
      file.deleteOnExit();
    } catch (Throwable e) {
      e.printStackTrace();
    }

    String result = contents.toString();
    // System.out.println(result);
    String[] values = result.split("\\|");
    Version javaVersion = Version.newStandardVersion(values[0]);
    String sunbootclasspath = values[1];
    String javaextdirs = (extDirs != null ? extDirs : values[2]);
    String javaendorseddirs = (endorsedDirs != null ? endorsedDirs : values[3]);
    Version javaSpecificationVersion = Version.newBundleVersion(values[4]);

    if (files != null) {
      A4ELogging.debug("Using specified files for JRE '%s': '%s'", id, files);
    } else {
      files = new LinkedList<File>();
      A4ELogging.debug("Adding endorsed files from endorsed dirs for JRE '%s': '%s'", id, javaendorseddirs);
      addFiles(javaendorseddirs, false, files);
      addFiles(sunbootclasspath, false, files);
      A4ELogging.debug("Adding ext files from exts dirs for JRE '%s': '%s'", id, javaextdirs);
      addFiles(javaextdirs, true, files);
    }

    File[] libraries = files.toArray(new File[0]);

    //
    Properties properties = new Properties();
    properties.put(JAVA_SPECIFICATION_VERSION, values[4]);
    properties.put(JAVA_SPECIFICATION_NAME, values[5]);

    String javaProfileName = getVmProfile(properties);
    JavaRuntimeRegistry javaRuntimeRegistry = ServiceRegistryAccess.instance().getService(JavaRuntimeRegistry.class);
    if (!javaRuntimeRegistry.hasJavaProfile(javaProfileName)) {
      A4ELogging.error("No Java-Profile with name '%s' found for JRE '%s' located at '%s'. Known Profiles: '%s'",
          javaProfileName, id, location, javaRuntimeRegistry.getAllJavaProfileNames());
      throw new Ant4EclipseException(JdtExceptionCode.NO_JAVA_PROFILE_FOUND_FOR_JRE, id, location, javaProfileName);
    }
    JavaProfile javaProfile = javaRuntimeRegistry.getJavaProfile(javaProfileName);

    JavaRuntime javaRuntime = new JavaRuntimeImpl(id, location, libraries, javaVersion, javaSpecificationVersion,
        javaProfile);

    return javaRuntime;
  }

  private static void addFiles(String path, boolean addChildrenIfDirectory, List<File> list) {

    String[] fileNames = path.split(File.pathSeparator);

    for (String fileName : fileNames) {
      File file = new File(fileName);
      if (file.exists()) {

        if (file.isFile() && !list.contains(file)) {
          list.add(file);
        } else if (file.isDirectory() && addChildrenIfDirectory) {
          File[] children = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
              return name.endsWith(".jar");
            }
          });
          for (File child : children) {
            if (child.isFile() && !list.contains(child)) {
              list.add(child);
            }
          }
        }
      }
    }
  }

  private static String getVmProfile(Properties systemProperties) {
    // Properties result = new Properties();
    // Find the VM profile name using J2ME properties
    String j2meConfig = systemProperties.getProperty(J2ME_MICROEDITION_CONFIGURATION);
    String j2meProfiles = systemProperties.getProperty(J2ME_MICROEDITION_PROFILES);
    String vmProfile = null;
    String javaEdition = null;
    if ((j2meConfig != null) && (j2meConfig.length() > 0) && (j2meProfiles != null) && (j2meProfiles.length() > 0)) {
      // save the vmProfile based off of the config and profile
      // use the last profile; assuming that is the highest one
      String[] j2meProfileList = getArrayFromList(j2meProfiles, " "); //$NON-NLS-1$
      if ((j2meProfileList != null) && (j2meProfileList.length > 0)) {
        vmProfile = j2meConfig + '_' + j2meProfileList[j2meProfileList.length - 1];
      }
    } else {
      // No J2ME properties; use J2SE properties
      // Note that the CDC spec appears not to require VM implementations to set the
      // javax.microedition properties!! So we will try to fall back to the
      // java.specification.name property, but this is pretty ridiculous!!
      String javaSpecVersion = systemProperties.getProperty(JAVA_SPECIFICATION_VERSION);
      // set the profile and EE based off of the java.specification.version
      // TODO We assume J2ME Foundation and J2SE here. need to support other profiles J2EE ...
      if (javaSpecVersion != null) {
        StringTokenizer st = new StringTokenizer(javaSpecVersion, " _-"); //$NON-NLS-1$
        javaSpecVersion = st.nextToken();
        String javaSpecName = systemProperties.getProperty(JAVA_SPECIFICATION_NAME);
        if ("J2ME Foundation Specification".equals(javaSpecName)) {
          vmProfile = "CDC-" + javaSpecVersion + "_Foundation-" + javaSpecVersion; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
          javaEdition = J2SE;

          if (Integer.parseInt(javaSpecVersion.split("\\.")[1]) >= 6) {
            javaEdition = JAVASE;
          }

          vmProfile = javaEdition + javaSpecVersion;
        }
      }
    }

    return vmProfile;
  }

  /**
   * Returns the result of converting a list of tokens into an array. The tokens are split using the specified
   * separator.
   * 
   * @return the array of string tokens. If there are none then an empty array is returned.
   * @param stringList
   *          the initial string list
   * @param separator
   *          the separator to use to split the list into tokens.
   * @since 3.2
   */
  private static String[] getArrayFromList(String stringList, String separator) {
    if ((stringList == null) || (stringList.trim().length() == 0)) {
      return new String[0];
    }
    ArrayList<String> list = new ArrayList<String>();
    StringTokenizer tokens = new StringTokenizer(stringList, separator);
    while (tokens.hasMoreTokens()) {
      String token = tokens.nextToken().trim();
      if (token.length() != 0) {
        list.add(token);
      }
    }
    return list.toArray(new String[list.size()]);
  }
}
