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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

/**
 * ContainerClasspathEntryResolver --
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ContainerClasspathEntryResolver extends AbstractClasspathEntryResolver implements Lifecycle {

  public final static String               CONTAINER_CLASSPATH_ENTRY_RESOLVER_PREFIX = "containerResolver";

  /** the static container resolver list */
  private List<ClasspathContainerResolver> _containerresolver;

  /** indicates if the resolver is initialized or not */
  private boolean                          _isInitialized                            = false;

  /**
   * <p>
   * Creates a new instance of type {@link ContainerClasspathEntryResolver}.
   * </p>
   */
  public ContainerClasspathEntryResolver() {
    initialize();
  }

  /**
   * {@inheritDoc}
   */
  public boolean canResolve(final ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_CONTAINER)
    /* || isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_CONTAINER) */;
  }

  /**
   * {@inheritDoc}
   */
  public void resolve(final ClasspathEntry entry, final ClasspathResolverContext context) {
    Assert.notNull(entry);

    // do not resolve if the class path entry is not visible
    if (!isClasspathEntryVisible(entry, context)) {
      return;
    }

    // log
    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("ContainerClasspathEntryResolver.resolve(" + entry + ", " + context + ")");
    }

    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("_containerresolver: " + this._containerresolver);
    }

    // set 'handled' to false
    boolean handled = false;

    // iterate over all registered container resolvers
    final Iterator iterator = this._containerresolver.iterator();
    while (iterator.hasNext()) {

      final ClasspathContainerResolver classpathContainerResolver = (ClasspathContainerResolver) iterator.next();

      if (A4ELogging.isDebuggingEnabled()) {
        A4ELogging.debug("ContainerClasspathEntryResolver.resolve: Try " + classpathContainerResolver);
      }

      if (classpathContainerResolver.canResolveContainer(entry)) {

        if (A4ELogging.isDebuggingEnabled()) {
          A4ELogging.debug("ContainerClasspathEntryResolver.resolve: Use " + classpathContainerResolver);
        }

        handled = true;
        classpathContainerResolver.resolveContainer(entry, context);
        break;
      }
    }

    // throw exception if not handled
    if (!handled) {
      // TODO
      throw new RuntimeException("Container '" + entry + "' not handled!");
    }
  }

  /**
   * {@inheritDoc}
   */
  public void initialize() {
    this._containerresolver = new LinkedList<ClasspathContainerResolver>();

    final Iterable<String[]> containerResolverEntries = Ant4EclipseConfiguration.Helper.getAnt4EclipseConfiguration()
        .getAllProperties(CONTAINER_CLASSPATH_ENTRY_RESOLVER_PREFIX);

    final List<ClasspathContainerResolver> containerResolvers = new LinkedList<ClasspathContainerResolver>();

// Instantiate all ProjectRoleIdentifiers
    for (final String[] containerResolverEntry : containerResolverEntries) {
      // we're not interested in the key of a roleidentifier. only the classname (value of the entry) is relevant
      final String containerResolverClassName = containerResolverEntry[1];
      final ClasspathContainerResolver resolver = Utilities.newInstance(containerResolverClassName);
      A4ELogging.trace("Register ClasspathContainerResolver '%s'", new Object[] { resolver });
      containerResolvers.add(resolver);
    }

    this._containerresolver = containerResolvers;

    // initialize all registered container resolvers
    final Iterator iterator = this._containerresolver.iterator();
    while (iterator.hasNext()) {
      final ClasspathContainerResolver classpathContainerResolver = (ClasspathContainerResolver) iterator.next();
      if (classpathContainerResolver instanceof Lifecycle) {
        ((Lifecycle) classpathContainerResolver).initialize();
      }
    }
    // set initialized to true
    this._isInitialized = true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return this._isInitialized;
  }

  /**
   * {@inheritDoc}
   */
  public void dispose() {
    // initialize all registered container resolvers
    final Iterator iterator = this._containerresolver.iterator();
    while (iterator.hasNext()) {
      final ClasspathContainerResolver classpathContainerResolver = (ClasspathContainerResolver) iterator.next();
      if (classpathContainerResolver instanceof Lifecycle) {
        ((Lifecycle) classpathContainerResolver).dispose();
      }
    }
    // set initialized to true
    this._isInitialized = false;
  }
}
