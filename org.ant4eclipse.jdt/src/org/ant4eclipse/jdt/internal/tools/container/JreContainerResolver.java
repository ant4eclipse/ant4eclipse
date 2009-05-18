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
package org.ant4eclipse.jdt.internal.tools.container;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.ContainerTypes;
import org.ant4eclipse.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.jdt.model.jre.JavaRuntimeRegistry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

public class JreContainerResolver implements ClasspathContainerResolver {

  public boolean canResolveContainer(final ClasspathEntry classpathEntry) {
    return classpathEntry.getPath().startsWith(ContainerTypes.JRE_CONTAINER);
  }

  /**
   * @param resolver
   */
  public void resolveContainer(final ClasspathEntry classpathEntry, final ClasspathResolverContext context) {
    //

    if (!context.isCurrentProjectRoot()) {
      return;
    }

    final JavaRuntimeRegistry javaRuntimeRegistry = JavaRuntimeRegistry.Helper.getRegistry();

    final String path = classpathEntry.getPath();

    JavaRuntime javaRuntime = null;
    if (path.equals(ContainerTypes.JRE_CONTAINER)) {
      javaRuntime = javaRuntimeRegistry.getDefaultJavaRuntime();
    } else if (path.startsWith(ContainerTypes.VMTYPE_PREFIX)) {
      final String key = path.substring(ContainerTypes.VMTYPE_PREFIX.length());
      javaRuntime = javaRuntimeRegistry.getJavaRuntime(key);
      if (javaRuntime == null) {
        // TODO
        A4ELogging.warn("Could not find JRE for " + path + ". Using default JRE!");
        javaRuntime = javaRuntimeRegistry.getDefaultJavaRuntime();
      }
    } else {
      javaRuntime = javaRuntimeRegistry.getDefaultJavaRuntime();
    }

    if (javaRuntime == null) {
      // TODO
      throw new RuntimeException("Could not find JRE for " + path + ".");
    }

    // TODO
    AccessRestrictions accessRestrictions = null;
    final File[] libraries = javaRuntime.getLibraries();

    if (!path.equals(ContainerTypes.JRE_CONTAINER)) {
      final String profileKey = path.substring(ContainerTypes.VMTYPE_PREFIX.length());
      if (javaRuntimeRegistry.hasJavaProfile(profileKey)) {
        final Set<String> publicPackages = new LinkedHashSet<String>();
        publicPackages.add("java");
        publicPackages.addAll(javaRuntimeRegistry.getJavaProfile(profileKey).getSystemPackages());
        accessRestrictions = new AccessRestrictions(publicPackages, new LinkedHashSet<String>(), true);
      }
    }

    context.setBootClasspathEntry(new ResolvedClasspathEntry(libraries, accessRestrictions));
  }
}
