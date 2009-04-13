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
package org.ant4eclipse.jdt.internal.model.jre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.jdt.internal.model.jre.support.LibraryDetector;
import org.ant4eclipse.jdt.model.jre.JavaProfile;
import org.ant4eclipse.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.jdt.model.jre.JavaRuntimeRegistry;

public class JavaRuntimeLoader {
  /**  */
  private static final String JAVA_SPECIFICATION_NAME         = "java.specification.name";

  /**  */
  private static final String JAVA_SPECIFICATION_VERSION      = "java.specification.version";

  /** J2ME configuration property name */
  private static final String J2ME_MICROEDITION_CONFIGURATION = "microedition.configuration"; //$NON-NLS-1$

  /** J2ME profile property name */
  private static final String J2ME_MICROEDITION_PROFILES      = "microedition.profiles";     //$NON-NLS-1$

  /**  */
  private static String       J2SE                            = "J2SE-";                     //$NON-NLS-1$

  /**  */
  private static String       JAVASE                          = "JavaSE-";                   //$NON-NLS-1$

  /**
   * @param id
   * @param location
   */
  public static JavaRuntime loadJavaRuntime(final String id, final File location) {
    Assert.nonEmpty(id);
    Assert.isDirectory(location);

    final String outfileName = System.getProperty("java.io.tmpdir") + "\\ant4eclipse_jdk_props_" +
        Math.round(Math.random() * 1000000000);
    // System.out.println(outfileName);

    final JavaExecuter javaLauncher = JavaExecuter.createWithA4eClasspath(location);
    javaLauncher.setMainClass(LibraryDetector.class.getName());
    javaLauncher.setArgs(new String[] { outfileName });
    javaLauncher.execute();

    // TODO
    final StringBuffer contents = new StringBuffer();
    try {
      final File file = new File(outfileName);
      final BufferedReader in = new BufferedReader(new FileReader(file));
      String str;
      while ((str = in.readLine()) != null) {
        contents.append(str);
      }
      in.close();
      file.deleteOnExit();
    } catch (final Throwable e) {
      e.printStackTrace();
    }

    final String result = contents.toString();
// System.out.println(result);
    final String[] values = result.split("\\|");
    final Version javaVersion = new Version(values[0]);
    final String sunbootclasspath = values[1];
    final String javaextdirs = values[2];
    // final String javaendorseddirs = values[3];
    final Version javaSpecificationVersion = new Version(values[4]);

    final List<File> files = new LinkedList<File>();
    addFiles(sunbootclasspath, false, files);
    addFiles(javaextdirs, true, files);

    // addFiles(javaendorseddirs);
    // TODO: ext-libs!!

    final File[] libraries = files.toArray(new File[0]);

    //
    final Properties properties = new Properties();
    properties.put(JAVA_SPECIFICATION_VERSION, values[4]);
    properties.put(JAVA_SPECIFICATION_NAME, values[5]);

    final String javaProfileName = getVmProfile(properties);

    final JavaRuntimeRegistry javaRuntimeRegistry = (JavaRuntimeRegistry) ServiceRegistry.instance().getService(
        JavaRuntimeRegistry.class.getName());
    final JavaProfile javaProfile = javaRuntimeRegistry.getJavaProfile(javaProfileName);

    final JavaRuntime javaRuntime = new JavaRuntimeImpl(id, location, libraries, javaVersion, javaSpecificationVersion,
        javaProfile);

    return javaRuntime;
  }

  private static void addFiles(final String path, final boolean addChildrenIfDirectory, final List<File> list) {

    final String[] fileNames = path.split(File.pathSeparator);

    for (final String fileName : fileNames) {
      final File file = new File(fileName);
      if (file.exists()) {

        if (file.isFile() && !list.contains(file)) {
          list.add(file);
        } else if (file.isDirectory() && addChildrenIfDirectory) {
          final File[] children = file.listFiles(new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
              return name.endsWith(".jar");
            }
          });
          for (final File child : children) {
            if (child.isFile() && !list.contains(child)) {
              list.add(child);
            }
          }
        }
      }
    }
  }

  private static String getVmProfile(final Properties systemProperties) {
    // final Properties result = new Properties();
    // Find the VM profile name using J2ME properties
    final String j2meConfig = systemProperties.getProperty(J2ME_MICROEDITION_CONFIGURATION);
    final String j2meProfiles = systemProperties.getProperty(J2ME_MICROEDITION_PROFILES);
    String vmProfile = null;
    String javaEdition = null;
    if ((j2meConfig != null) && (j2meConfig.length() > 0) && (j2meProfiles != null) && (j2meProfiles.length() > 0)) {
      // save the vmProfile based off of the config and profile
      // use the last profile; assuming that is the highest one
      final String[] j2meProfileList = getArrayFromList(j2meProfiles, " "); //$NON-NLS-1$
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
        final StringTokenizer st = new StringTokenizer(javaSpecVersion, " _-"); //$NON-NLS-1$
        javaSpecVersion = st.nextToken();
        final String javaSpecName = systemProperties.getProperty(JAVA_SPECIFICATION_NAME);
        if ("J2ME Foundation Specification".equals(javaSpecName)) {
          vmProfile = "CDC-" + javaSpecVersion + "_Foundation-" + javaSpecVersion; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
          javaEdition = J2SE;

          if (new Integer(javaSpecVersion.split("\\.")[1]).intValue() >= 6) {
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
  private static String[] getArrayFromList(final String stringList, final String separator) {
    if ((stringList == null) || (stringList.trim().length() == 0)) {
      return new String[0];
    }
    final ArrayList<String> list = new ArrayList<String>();
    final StringTokenizer tokens = new StringTokenizer(stringList, separator);
    while (tokens.hasMoreTokens()) {
      final String token = tokens.nextToken().trim();
      if (token.length() != 0) {
        list.add(token);
      }
    }
    return list.toArray(new String[list.size()]);
  }
}
