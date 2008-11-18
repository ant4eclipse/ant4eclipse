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
package org.ant4eclipse.jdt.tools.internal.classpathentry;

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.platform.model.resource.EclipseProject;


/**
 * AbstractRawClasspathEntryResolver --
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractClasspathEntryResolver implements ClasspathEntryResolver {

  // /**
  // * Modifies the resolved class path for the resource with the specified path. The resource has to reside in the
  // * workspace.
  // *
  // * @param path
  // * the path of the resource.
  // * @deprecated use resolveProjectResource instead
  // */
  // protected final void resolveWorkspaceResource(final String path, final ClasspathResolverContext context) {
  // Assert.notNull(path);
  //
  // final File resource = context.getWorkspace().getChild(path, context.isRelative());
  // final ResolvedClasspathEntry entry = new ResolvedClasspathEntryImpl(context.getCurrentProject(), resource);
  // context.addResolvedClasspathEntry(entry);
  // }

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

  protected JavaProjectRole getCurrentJavaProjectRole(final ClasspathResolverContext context) {
    return JavaProjectRole.Helper.getJavaProjectRole(context.getCurrentProject());
  }
}
