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

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.io.File;

/**
 * <p>
 * Abstract base class for all {@link ClasspathEntryResolver ClasspathEntryResolvers}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractClasspathEntryResolver implements ClasspathEntryResolver {

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @param path
   * @param context
   */
  protected final void resolveProjectRelativeResource(final EclipseProject project, final String path,
      final ClasspathResolverContext context) {
    Assert.notNull(path);

    final int relative = context.isWorkspaceRelative() ? EclipseProject.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME
        : EclipseProject.ABSOLUTE;
    final File child = project.getChild(path, relative);
    context.addClasspathEntry(new ResolvedClasspathEntry(child));
  }

  /**
   * Resolves an absolute resource.
   * 
   * @param file
   *          The absolute resource.
   */
  protected final void resolveAbsoluteResource(final String path, final ClasspathResolverContext context) {
    Assert.nonEmpty(path);

    context.addClasspathEntry(new ResolvedClasspathEntry(new File(path)));
  }

  /**
   * @param entry
   * @param entryKind
   * @return
   */
  protected final boolean isRawClasspathEntryOfKind(final ClasspathEntry entry, final int entryKind) {
    return (entry instanceof RawClasspathEntry) && (entry.getEntryKind() == entryKind);
  }

  // /**
  // * @param entry
  // * @param entryKind
  // * @return
  // */
  // protected final boolean isRuntimeClasspathEntryOfKind(final ClasspathEntry entry, final int entryKind) {
  // return (entry instanceof RuntimeClasspathEntry) && (entry.getEntryKind() == entryKind);
  // }

  /**
   * @param entry
   * @return
   */
  protected final boolean isRawClasspathEntry(final ClasspathEntry entry) {
    return (entry instanceof RawClasspathEntry);
  }

  // /**
  // * @param entry
  // * @return
  // */
  // protected final boolean isRuntimeClasspathEntry(final ClasspathEntry entry) {
  // return (entry instanceof RuntimeClasspathEntry);
  // }

  protected final boolean isClasspathEntryVisible(final ClasspathEntry entry, final ClasspathResolverContext context) {
    return context.isRuntime() || context.isCurrentProjectRoot() /* || isRuntimeClasspathEntry(entry) */
        || (isRawClasspathEntry(entry) && ((RawClasspathEntry) entry).isExported());
  }

  /**
   * <p>
   * Helper method. Returns the {@link JavaProjectRole} of the current project.
   * </p>
   * 
   * @param context
   *          the {@link ClasspathResolverContext}
   * @return the {@link JavaProjectRole} of the current project.
   */
  protected JavaProjectRole getCurrentJavaProjectRole(final ClasspathResolverContext context) {
    return JavaProjectRole.Helper.getJavaProjectRole(context.getCurrentProject());
  }
}
