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

import org.ant4eclipse.lib.jdt.EclipsePathUtil;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * <p>
 * The {@link ContainerClasspathEntryResolver} is responsible for resolving library class path entries (class path
 * entries of kind 'lib' where path doesn't point to an eclipse project, e.g. &lt;classpathentry kind="lib"
 * path="/org.ant4eclipse.external/libs/ant/apache-ant-1.7.1.jar"
 * sourcepath="/org.ant4eclipse.external/libs/ant/apache-ant-1.7.1-src.zip"/&gt;).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class LibraryClasspathEntryResolver extends AbstractClasspathEntryResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canResolve(ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_LIBRARY)
    /* || isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_ARCHIVE) */;
  }

  /**
   * {@inheritDoc}
   */
  public void resolve(ClasspathEntry entry, ClasspathResolverContext context) {

    // do not resolve if the class path entry is not visible
    if (!isClasspathEntryVisible(entry, context)) {
      return;
    }

    // PROJECT_RELATIVE_PATH
    if (getPathType(entry, context) == EclipsePathUtil.PROJECT_RELATIVE_PATH) {
      resolveProjectRelativeResource(context.getCurrentProject(), entry.getPath(), context);
    }

    // WORKSPACE_RELATIVE_PATH
    else if (getPathType(entry, context) == EclipsePathUtil.WORKSPACE_RELATIVE_PATH) {
      String[] splitted = EclipsePathUtil.splitHeadAndTail(entry.getPath());

      // AE-225: Classpath resolution fails if a project is added as a library
      if (splitted.length == 1) {
        EclipseProject referencedProject = context.getWorkspace().getProject(splitted[0]);
        context.addClasspathEntry(new ResolvedClasspathEntry(referencedProject.getFolder()));
      } else {
        resolveProjectRelativeResource(context.getWorkspace().getProject(splitted[0]), splitted[1], context);
      }

    }

    // ABSOULTE_PATH
    else if (getPathType(entry, context) == EclipsePathUtil.ABSOLUTE_PATH) {
      resolveAbsoluteResource(entry.getPath(), context);
    }
  }

  /**
   * @param entry
   * @return
   */
  private int getPathType(ClasspathEntry entry, ClasspathResolverContext context) {
    return EclipsePathUtil.getPathType(entry.getPath(), context.getWorkspace());
    }

}
