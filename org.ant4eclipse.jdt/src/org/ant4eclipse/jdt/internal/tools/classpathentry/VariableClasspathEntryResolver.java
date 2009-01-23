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

public class VariableClasspathEntryResolver extends AbstractClasspathEntryResolver {

  public boolean canResolve(final ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_VARIABLE)
        /*|| isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_VARIABLE)*/;
  }

  public void resolve(final ClasspathEntry entry, final ClasspathResolverContext context) {

    // ResolvedClasspathEntry classpathEntry = null;
    //
    // if (isRawClasspathEntry(entry)) {
    // classpathEntry = new ResolvedClasspathEntryImpl(context.getCurrentProject(), (RawClasspathEntry) entry);
    // } else if (isRuntimeClasspathEntry(entry)) {
    // classpathEntry = new ResolvedClasspathEntryImpl(context.getCurrentProject(), (RuntimeClasspathEntry) entry);
    // }
    //
    // context.addResolvedClasspathEntry(classpathEntry);
  }
}
