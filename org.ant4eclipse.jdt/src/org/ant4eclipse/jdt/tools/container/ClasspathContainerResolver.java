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
package org.ant4eclipse.jdt.tools.container;

import org.ant4eclipse.jdt.model.ClasspathEntry;

/**
 * <p>
 * Defines the common interface for {@link ClasspathContainerResolver}. Classes that implement this interface must be
 * able to resolve a given eclipse class path container.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClasspathContainerResolver {

  /**
   * <p>
   * Returns <code>true</code>, if this {@link ClasspathContainerResolver} can resolve the given {@link ClasspathEntry}.
   * </p>
   * 
   * @param classpathEntry
   *          the {@link ClasspathEntry} that should be resolved
   * @return <code>true</code>, if this {@link ClasspathContainerResolver} can resolve the given {@link ClasspathEntry}.
   */
  public boolean canResolveContainer(ClasspathEntry classpathEntry);

  /**
   * <p>
   * Resolves the given {@link ClasspathEntry}. Implementors of this method can use the {@link ClasspathResolverContext}
   * to set the resolved class path entries.
   * </p>
   * 
   * @param classpathEntry
   *          the {@link ClasspathEntry} to resolve
   * @param context
   *          the {@link ClasspathResolverContext}
   */
  public void resolveContainer(ClasspathEntry classpathEntry, ClasspathResolverContext context);
}
