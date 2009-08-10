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
package org.ant4eclipse.core.util;

import org.ant4eclipse.core.Assert;

import java.io.File;
import java.lang.reflect.Method;
import java.security.CodeSource;

/**
 * <p>
 * Helper class that allows you to load classes either from the ant class loader or the standard class loader.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassLoadingHelper {

  /** the method 'getClasspath' */
  private static final String METHOD_GET_CLASSPATH                      = "getClasspath";

  /** the class 'AntClassLoader' */
  private static final String CLASS_ORG_APACHE_TOOLS_ANT_ANTCLASSLOADER = "org.apache.tools.ant.AntClassLoader";

  /**
   * <p>
   * Returns the class path entries for the specified class.
   * </p>
   * 
   * @param clazz
   *          the class
   * @return the class path entries for the specified class.
   */
  public static String[] getClasspathEntriesFor(final Class<?> clazz) {
    Assert.notNull(clazz);

    // get class loader
    final ClassLoader classLoader = clazz.getClassLoader();
    final Class<? extends ClassLoader> classLoaderClass = classLoader.getClass();

    // AntClassLoader: we have to call 'getClasspath()', because the code source
    // always is the 'ant.jar'
    if (classLoaderClass.getName().equals(CLASS_ORG_APACHE_TOOLS_ANT_ANTCLASSLOADER)) {

      try {
        final Method method = classLoaderClass.getDeclaredMethod(METHOD_GET_CLASSPATH, new Class[0]);
        final Object result = method.invoke(classLoader, new Object[0]);
        final String[] fileNames = result.toString().split(File.pathSeparator);
        return fileNames;
      } catch (final Exception e) {
        e.printStackTrace();
        return new String[0];
      }
    }
    // 'normal' class loader: just retrieve the code source
    else {
      final CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
      return new String[] { codeSource.getLocation().getFile() };
    }
  }
}
