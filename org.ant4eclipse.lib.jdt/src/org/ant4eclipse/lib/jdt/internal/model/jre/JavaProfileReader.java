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

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.StringMap;

import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Taken from Framework!
 * </p>
 */
public class JavaProfileReader implements Lifecycle {

  /** the java profile cache */
  private Map<String, JavaProfile> _javaProfileCache;

  /**
   * {@inheritDoc}
   */
  public void initialize() {

    this._javaProfileCache = new HashMap<String, JavaProfile>();

    // read all known profiles
    JavaProfile[] javaProfiles = readAllProfiles();

    // add profiles to profile cache
    for (JavaProfile javaProfile : javaProfiles) {
      this._javaProfileCache.put(javaProfile.getName(), javaProfile);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void dispose() {

  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return this._javaProfileCache != null;
  }

  /**
   * @return
   */
  public JavaProfile readDefaultProfile() {
    return this._javaProfileCache.get("JavaSE-1.6");
  }

  /**
   * {@inheritDoc}
   */
  public JavaProfile getJavaProfile(String path) {
    Assert.nonEmpty(path);

    return this._javaProfileCache.get(path);
  }

  public boolean hasJavaProfile(String path) {
    Assert.nonEmpty(path);

    return this._javaProfileCache.containsKey(path);
  }

  /**
   * @return
   */
  private JavaProfile[] readAllProfiles() {

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

  // /**
  // * @param profile
  // * @return
  // */
  // public static JavaProfile readProfile(String profile) {
  // Assert.notNull(profile);
  // String profileName = profile + ".profile";
  // A4ELogging.debug("trying to read profile '%s' from classpath", profileName);
  // StringMap profileProperties = new StringMap("/profiles/" + profileName);
  // return new JavaProfileImpl(profileProperties);
  // }

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

  public static JavaProfileReader getInstance() {
    return (JavaProfileReader) ServiceRegistry.instance().getService(JavaProfileReader.class.getName());
  }
}