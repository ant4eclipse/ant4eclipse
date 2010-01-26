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


import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.jdt.JdtExceptionCode;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathVariable;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;

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
  public boolean canResolve(ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_VARIABLE);
  }

  /**
   * {@inheritDoc}
   */
  public void resolve(ClasspathEntry entry, ClasspathResolverContext context) {

    // get the path
    String path = entry.getPath();

    // split variable and tail if necessary
    String variable = path;
    String tail = null;
    int separatorIndex = path.indexOf(this.SEPARATOR);
    if (separatorIndex != -1) {
      variable = path.substring(0, separatorIndex);
      tail = path.substring(separatorIndex);
    }

    // get variablesRegistry
    ClassPathElementsRegistry elementsRegistry = ServiceRegistry.instance().getService(ClassPathElementsRegistry.class);

    // resolve variable
    if (elementsRegistry.hasClassPathVariable(variable)) {
      ClassPathVariable classpathVariable = elementsRegistry.getClassPathVariable(variable);
      if (tail != null) {
        context.addClasspathEntry(new ResolvedClasspathEntry(new File(classpathVariable.getPath(), tail)));
      } else {
        context.addClasspathEntry(new ResolvedClasspathEntry(classpathVariable.getPath()));
      }
    }
    // throw exception if variable is unbound
    else {
      throw new Ant4EclipseException(JdtExceptionCode.UNBOUND_CLASS_PATH_VARIABLE, context.getCurrentProject()
          .getSpecifiedName(), variable);
    }
  }
}
