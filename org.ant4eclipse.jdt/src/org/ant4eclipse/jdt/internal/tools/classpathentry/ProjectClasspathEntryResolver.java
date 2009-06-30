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

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * <p>
 * Implements a {@link ClasspathEntryResolver} that resolves class path entries to eclipse projects.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectClasspathEntryResolver extends AbstractClasspathEntryResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canResolve(final ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_PROJECT)
    /* || isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_PROJECT) */;
  }

  /**
   * @see org.ant4eclipse.jdt.internal.tools.classpathentry.ClasspathEntryResolver#resolve(org.ant4eclipse.jdt.model.ClasspathEntry,
   *      org.ant4eclipse.jdt.tools.container.ClasspathResolverContext)
   */
  public void resolve(final ClasspathEntry entry, final ClasspathResolverContext context) {

    // return immediately if the class path entry is not visible
    if (!isClasspathEntryVisible(entry, context)) {
      return;
    }

    // get the project name
    String projectname = entry.getPath();

    // normalize
    if (projectname.startsWith("/")) {
      projectname = projectname.substring(1);
    }

    // get the project
    final EclipseProject referencedProject = context.getWorkspace().getProject(projectname);

    // resolve the class path for the referenced project.
    context.resolveProjectClasspath(referencedProject);
  }
}
