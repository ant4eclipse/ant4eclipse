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

import org.ant4eclipse.core.exception.Ant4EclipseException;

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.classpathvariables.ClasspathVariable;
import org.ant4eclipse.jdt.model.classpathvariables.ClasspathVariablesRegistry;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.JdtToolsExceptionCode;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

import java.io.File;

/**
 * <p>
 * A class path variable can be added to a project's class path. It can be used to define the location of a JAR file or
 * a directory that isn't part of the workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class VariableClasspathEntryResolver extends AbstractClasspathEntryResolver {

  /** the SEPARATOR */
  private final String SEPARATOR = "/";

  /**
   * <p>
   * Returns <code>true</code>, if the {@link ClasspathEntry} is of kind {@link RawClasspathEntry.CPE_VARIABLE}.
   * </p>
   * 
   * @param classpathEntry
   *          the class path entry to resolve.
   * @return <code>true</code>, if the {@link ClasspathEntry} is of kind {@link RawClasspathEntry.CPE_VARIABLE}.
   */
  public boolean canResolve(final ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_VARIABLE);
  }

  /**
   * {@inheritDoc}
   */
  public void resolve(final ClasspathEntry entry, final ClasspathResolverContext context) {

    // get the path
    final String path = entry.getPath();

    // split variable and tail if necessary
    String variable = path;
    String tail = null;
    final int separatorIndex = path.indexOf(this.SEPARATOR);
    if (separatorIndex != -1) {
      variable = path.substring(0, separatorIndex);
      tail = path.substring(separatorIndex);
    }

    // get variablesRegistry
    final ClasspathVariablesRegistry variablesRegistry = ClasspathVariablesRegistry.Helper.getRegistry();

    // resolve variable
    if (variablesRegistry.hasClassPathVariable(variable)) {
      final ClasspathVariable classpathVariable = variablesRegistry.getClassPathVariable(variable);
      if (tail != null) {
        context.addClasspathEntry(new ResolvedClasspathEntry(new File(classpathVariable.getPath(), tail)));
      } else {
        context.addClasspathEntry(new ResolvedClasspathEntry(classpathVariable.getPath()));
      }
    }
    // throw exception if variable is unbound
    else {
      throw new Ant4EclipseException(JdtToolsExceptionCode.UNBOUND_CLASS_PATH_VARIABLE, context.getCurrentProject()
          .getSpecifiedName(), variable);
    }
  }
}
