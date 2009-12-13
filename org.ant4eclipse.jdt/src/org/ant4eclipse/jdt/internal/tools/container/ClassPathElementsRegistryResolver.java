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
package org.ant4eclipse.jdt.internal.tools.container;

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.classpathelements.ClassPathContainer;
import org.ant4eclipse.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

import org.ant4eclipse.lib.core.service.ServiceRegistry;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassPathElementsRegistryResolver implements ClasspathContainerResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canResolveContainer(ClasspathEntry classpathEntry) {
    return getClassPathElementsRegistry().hasClassPathContainer(classpathEntry.getPath());
  }

  /**
   * {@inheritDoc}
   */
  public void resolveContainer(ClasspathEntry classpathEntry, ClasspathResolverContext context) {
    ClassPathContainer container = getClassPathElementsRegistry().getClassPathContainer(classpathEntry.getPath());
    context.addClasspathEntry(new ResolvedClasspathEntry(container.getPathEntries()));
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private ClassPathElementsRegistry getClassPathElementsRegistry() {
    return ServiceRegistry.instance().getService(ClassPathElementsRegistry.class);
  }
}
