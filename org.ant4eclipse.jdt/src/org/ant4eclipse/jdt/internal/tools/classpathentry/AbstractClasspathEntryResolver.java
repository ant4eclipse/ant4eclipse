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
package org.ant4eclipse.jdt.internal.tools.classpathentry;

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.lib.core.Assure;

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
  protected final void resolveProjectRelativeResource(EclipseProject project, String path,
      ClasspathResolverContext context) {
    Assure.notNull(path);

    EclipseProject.PathStyle relative = context.isWorkspaceRelative() ? EclipseProject.PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME
        : EclipseProject.PathStyle.ABSOLUTE;
    File child = project.getChild(path, relative);
    context.addClasspathEntry(new ResolvedClasspathEntry(child));
  }

  /**
   * Resolves an absolute resource.
   * 
   * @param file
   *          The absolute resource.
   */
  protected final void resolveAbsoluteResource(String path, ClasspathResolverContext context) {
    Assure.nonEmpty(path);

    context.addClasspathEntry(new ResolvedClasspathEntry(new File(path)));
  }

  /**
   * @param entry
   * @param entryKind
   * @return
   */
  protected final boolean isRawClasspathEntryOfKind(ClasspathEntry entry, int entryKind) {
    return (entry instanceof RawClasspathEntry) && (entry.getEntryKind() == entryKind);
  }

  // /**
  // * @param entry
  // * @param entryKind
  // * @return
  // */
  // protected final boolean isRuntimeClasspathEntryOfKind(ClasspathEntry entry, int entryKind) {
  // return (entry instanceof RuntimeClasspathEntry) && (entry.getEntryKind() == entryKind);
  // }

  /**
   * <p>
   * Returns <code>true</code>, if the given entry is instance of type {@link ClasspathEntry}.
   * </p>
   * 
   * @param entry
   *          the class path entry
   * @return <code>true</code>, if the given entry is instance of type {@link ClasspathEntry}.
   */
  protected final boolean isRawClasspathEntry(ClasspathEntry entry) {
    return (entry instanceof RawClasspathEntry);
  }

  // /**
  // * @param entry
  // * @return
  // */
  // protected final boolean isRuntimeClasspathEntry(ClasspathEntry entry) {
  // return (entry instanceof RuntimeClasspathEntry);
  // }

  /**
   * <p>
   * Returns <code>true</code>, if the class path entry is visible.
   * </p>
   * 
   * @param entry
   *          the entry
   * @param context
   *          the resolver context
   * @return <code>true</code>, if the class path entry is visible.
   */
  protected final boolean isClasspathEntryVisible(ClasspathEntry entry, ClasspathResolverContext context) {
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
  protected JavaProjectRole getCurrentJavaProjectRole(ClasspathResolverContext context) {
    return JavaProjectRole.Helper.getJavaProjectRole(context.getCurrentProject());
  }
}
