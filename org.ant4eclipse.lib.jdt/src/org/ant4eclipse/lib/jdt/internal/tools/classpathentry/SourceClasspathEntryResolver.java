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
package org.ant4eclipse.lib.jdt.internal.tools.classpathentry;


import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

import java.io.File;

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
  @Override
  public boolean canResolve( ClasspathEntry entry ) {
    // only suitable for raw class path entries of kind CPE_SOURCE
    return isRawClasspathEntryOfKind( entry, RawClasspathEntry.CPE_SOURCE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolve( ClasspathEntry pathEntry, ClasspathResolverContext context ) {

    // always exported, there is no need to check for visibility
    RawClasspathEntry entry = (RawClasspathEntry) pathEntry;

    // get the source path
    String sourcePath = entry.getPath();

    // get the source folder
    File sourceFolder = context.isWorkspaceRelative() ? context.getCurrentProject().getChild( sourcePath,
        EclipseProject.PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME ) : context.getCurrentProject().getChild(
        sourcePath, EclipseProject.PathStyle.ABSOLUTE );

    // get the output path
    String outputPath = getCurrentJavaProjectRole( context ).getOutputFolderForSourceFolder( sourcePath );

    // get the output folder
    File outputFolder = context.isWorkspaceRelative() ? context.getCurrentProject().getChild( outputPath,
        EclipseProject.PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME ) : context.getCurrentProject().getChild(
        outputPath, EclipseProject.PathStyle.ABSOLUTE );

    // TODO: ACCESS RESTRICTIONS
    context.addClasspathEntry( new ResolvedClasspathEntry( new File[] { outputFolder }, null,
        new File[] { sourceFolder } ) );
  }
  
} /* ENDCLASS */
