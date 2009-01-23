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

import java.io.File;

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.platform.model.resource.EclipseProject;


/**
 * <p>
 * Implements a {@link ClasspathEntryResolver} to resolve class path entries of type
 * {@link RawClasspathEntry#CPE_SOURCE}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SourceClasspathEntryResolver extends AbstractClasspathEntryResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canResolve(final ClasspathEntry entry) {
    // only suitable for raw class path entries of kind CPE_SOURCE
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_SOURCE);
  }

  /**
   * {@inheritDoc}
   */
  public void resolve(final ClasspathEntry pathEntry, final ClasspathResolverContext context) {

    // always exported, there is no need to check for visibility
    final RawClasspathEntry entry = (RawClasspathEntry) pathEntry;

    final String path = getCurrentJavaProjectRole(context).getOutputFolderForSourceFolder(entry.getPath());

    final File outputFolder = context.isWorkspaceRelative() ? context.getCurrentProject().getChild(path,
        EclipseProject.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME) : context.getCurrentProject().getChild(path,
        EclipseProject.ABSOLUTE);

    // TODO: ACCESS RESTRICTIONS
    context.addClasspathEntry(new ResolvedClasspathEntry(outputFolder));
  }
}
