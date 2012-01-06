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

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.JdtExceptionCode;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * The {@link ContainerClasspathEntryResolver} is responsible for resolving container class path entries (class path
 * entries of kind 'con', e.g. &lt;classpathentry kind="con" path="org.eclipse.jdt.junit.JUNIT_CONTAINER/3"/&gt;).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kaaisoft.net)
 */
public class ContainerClasspathEntryResolver extends AbstractClasspathEntryResolver {

  /** CONTAINER_CLASSPATH_ENTRY_RESOLVER_PREFIX */
  public static final String               CONTAINER_CLASSPATH_ENTRY_RESOLVER_PREFIX = "containerResolver";

  /** the static container resolver list */
  private List<ClasspathContainerResolver> _containerresolver;
  
  /**
   * <p>
   * Creates a new instance of type {@link ContainerClasspathEntryResolver}.
   * </p>
   */
  public ContainerClasspathEntryResolver() {
    _containerresolver = A4ECore.instance().getServices( ClasspathContainerResolver.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canResolve(ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_CONTAINER)
    /* || isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_CONTAINER) */;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolve(ClasspathEntry entry, ClasspathResolverContext context) {
    Assure.notNull("entry", entry);

    // do not resolve if the class path entry is not visible
    if (!isClasspathEntryVisible(entry, context)) {
      return;
    }

    // log
    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("ContainerClasspathEntryResolver.resolve(%s, %s)", entry, context);
    }

    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("_containerresolver: %s", this._containerresolver);
    }

    // set 'handled' to false
    boolean handled = false;

    // iterate over all registered container resolvers
    Iterator<ClasspathContainerResolver> iterator = this._containerresolver.iterator();
    while (iterator.hasNext()) {

      ClasspathContainerResolver classpathContainerResolver = iterator.next();

      if (A4ELogging.isDebuggingEnabled()) {
        A4ELogging.debug("ContainerClasspathEntryResolver.resolve: Try %s", classpathContainerResolver);
      }

      if (classpathContainerResolver.canResolveContainer(entry)) {

        if (A4ELogging.isDebuggingEnabled()) {
          A4ELogging.debug("ContainerClasspathEntryResolver.resolve: Use %s", classpathContainerResolver);
        }

        handled = true;
        classpathContainerResolver.resolveContainer(entry, context);
        break;
      }
    }

    // throw exception if not handled
    if (!handled) {
      throw new Ant4EclipseException(JdtExceptionCode.CP_CONTAINER_NOT_HANDLED, entry.getPath(), context
          .getCurrentProject().getSpecifiedName());
    }
  }

} /* ENDCLASS */
