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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * The {@link ContainerClasspathEntryResolver} is responsible for resolving container class path entries (class path
 * entries of kind 'con', e.g. &lt;classpathentry kind="con" path="org.eclipse.jdt.junit.JUNIT_CONTAINER/3"/&gt;).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ContainerClasspathEntryResolver extends AbstractClasspathEntryResolver implements Lifecycle {

  /** CONTAINER_CLASSPATH_ENTRY_RESOLVER_PREFIX */
  public static final String               CONTAINER_CLASSPATH_ENTRY_RESOLVER_PREFIX = "containerResolver";

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
  public boolean canResolve(ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_CONTAINER)
    /* || isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_CONTAINER) */;
  }

  /**
   * {@inheritDoc}
   */
  public void resolve(ClasspathEntry entry, ClasspathResolverContext context) {
    Assure.notNull("entry", entry);

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
    Iterator<ClasspathContainerResolver> iterator = this._containerresolver.iterator();
    while (iterator.hasNext()) {

      ClasspathContainerResolver classpathContainerResolver = iterator.next();

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

    Iterable<Pair<String, String>> containerResolverEntries = Ant4EclipseConfiguration.Helper
        .getAnt4EclipseConfiguration().getAllProperties(CONTAINER_CLASSPATH_ENTRY_RESOLVER_PREFIX);

    List<ClasspathContainerResolver> containerResolvers = new LinkedList<ClasspathContainerResolver>();

    // Instantiate all Container Resolvers
    for (Pair<String, String> containerResolverEntry : containerResolverEntries) {

      // we're not interested in the key of a container resolver, only the class name (value of the entry) is relevant
      // to create new instance
      ClasspathContainerResolver resolver = Utilities.newInstance(containerResolverEntry.getSecond());

      // trace
      A4ELogging.trace("Register ClasspathContainerResolver '%s'", resolver);

      // add resolver
      containerResolvers.add(resolver);
    }

    this._containerresolver = containerResolvers;

    // initialize all registered container resolvers
    Iterator<ClasspathContainerResolver> iterator = this._containerresolver.iterator();
    while (iterator.hasNext()) {
      ClasspathContainerResolver classpathContainerResolver = iterator.next();
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
    Iterator<ClasspathContainerResolver> iterator = this._containerresolver.iterator();
    while (iterator.hasNext()) {
      ClasspathContainerResolver classpathContainerResolver = iterator.next();
      if (classpathContainerResolver instanceof Lifecycle) {
        ((Lifecycle) classpathContainerResolver).dispose();
      }
    }
    // set initialized to true
    this._isInitialized = false;
  }
}
