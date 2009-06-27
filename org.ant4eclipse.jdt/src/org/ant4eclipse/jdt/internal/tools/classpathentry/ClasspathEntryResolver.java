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
package org.ant4eclipse.jdt.internal.tools.classpathentry;

import org.ant4eclipse.jdt.model.*;
import org.ant4eclipse.jdt.model.project.*;
import org.ant4eclipse.jdt.tools.container.*;

/**
 * <p>
 * Defines the common interface for all {@link ClasspathEntryResolver ClasspathEntryResolvers}. A
 * {@link ClasspathEntryResolver} is used internally to resolve {@link RawClasspathEntry RawClasspathEntries} from the
 * project's <code>.classpath</code> file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClasspathEntryResolver {

  /**
   * <p>
   * Must return <code>true</code>, if this {@link ClasspathEntryResolver} can resolve the given {@link ClasspathEntry}.
   * </p>
   * 
   * @param classpathEntry
   *          the class path entry to resolve.
   * @return <code>true</code>, if this {@link ClasspathEntryResolver} can resolve the given {@link ClasspathEntry}.
   */
  boolean canResolve(ClasspathEntry classpathEntry);

  /**
   * <p>
   * Resolves the given {@link ClasspathEntry}.
   * </p>
   * 
   * @param classpathEntry
   *          the {@link ClasspathEntry}.
   * @param context
   *          the {@link ClasspathResolverContext}
   */
  void resolve(ClasspathEntry classpathEntry, final ClasspathResolverContext context);
}
