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
package org.ant4eclipse.lib.jdt.internal.tools.container;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathContainer;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kaaisoft.net)
 */
public class ClassPathElementsRegistryResolver implements ClasspathContainerResolver {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canResolveContainer( ClasspathEntry classpathEntry ) {
    return getClassPathElementsRegistry().hasClassPathContainer( classpathEntry.getPath() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolveContainer( ClasspathEntry classpathEntry, ClasspathResolverContext context ) {
    ClassPathContainer container = getClassPathElementsRegistry().getClassPathContainer( classpathEntry.getPath() );
    context.addClasspathEntry( new ResolvedClasspathEntry( container.getPathEntries() ) );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private ClassPathElementsRegistry getClassPathElementsRegistry() {
    return A4ECore.instance().getRequiredService( ClassPathElementsRegistry.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }

} /* ENDCLASS */
