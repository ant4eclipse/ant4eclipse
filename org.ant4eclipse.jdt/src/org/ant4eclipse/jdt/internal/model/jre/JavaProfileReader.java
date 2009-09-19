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

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.StringMap;

import org.ant4eclipse.jdt.model.jre.JavaProfile;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Taken from Framework!
 * </p>
 */
public class JavaProfileReader {

  /**
   * @return
   */
  public static JavaProfile readDefaultProfile() {
    return readProfile("JavaSE-1.6");
  }

  /**
   * @return
   */
  public static JavaProfile[] readAllProfiles() {

    // load the profile listing first
    StringMap properties = new StringMap("/profiles/profile.list");

    String javaProfiles = properties.get("java.profiles");

    String[] profiles = javaProfiles.split(",");

    List<JavaProfileImpl> result = new LinkedList<JavaProfileImpl>();

    for (String profile2 : profiles) {
      String profile = profile2.trim();
      if ((profile != null) && !"".equals(profile)) {
        StringMap props = new StringMap("/profiles/" + profile);
        result.add(new JavaProfileImpl(props));
      }
    }

    return result.toArray(new JavaProfile[result.size()]);
  }

  /**
   * @param profile
   * @return
   */
  public static JavaProfile readProfile(String profile) {
    Assert.notNull(profile);
    String profileName = profile + ".profile";
    A4ELogging.debug("trying to read profile '%s' from classpath", profileName);
    StringMap profileProperties = new StringMap("/profiles/" + profileName);
    return new JavaProfileImpl(profileProperties);
  }

  // public static String getVMProfile(File jreLocation) {
  //
  // JavaLauncher javaLauncher = JavaLauncher.createWithA4eClasspath(jreLocation);
  //
  // javaLauncher.setMainClass("net.sf.ant4eclipse.model.jdt.jre.internal.support.LegacySystemProperties");
  // javaLauncher.setArgs(new String[] { JAVA_SPECIFICATION_VERSION, "java.vendor" });
  // javaLauncher.execute();
  //
  // String[] sysout = javaLauncher.getSystemOut();
  // // for (int i = 0; i < sysout.length; i++) {
  // // System.err.println(i + " : " + sysout[i]);
  // // }
  //
  // // String[] syserr = javaLauncher.getSystemErr();
  // // for (int i = 0; i < syserr.length; i++) {
  // // System.err.println(syserr[i]);
  // // }
  //
  // Properties properties = new Properties();
  // for (int i = 0; i < sysout.length; i++) {
  // String[] prop = sysout[i].split("=");
  // properties.put(prop[0], prop[1]);
  // }
  //
  // return getVmProfile(properties);
  // }
}