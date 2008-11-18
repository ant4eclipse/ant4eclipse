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
package org.ant4eclipse.jdt.tools.internal.classpathentry;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.ManifestHelper;
import org.ant4eclipse.core.util.ManifestHelper.ManifestHeaderElement;
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

  /** the static container resolver list */
  private static List _containerresolver = new LinkedList();

  /** indicates if the resolver is initialized or not */
  private boolean     _isInitialized     = false;

  static {
    try {
      final Properties properties = new Properties();
      properties.load(ContainerClasspathEntryResolver.class
          .getResourceAsStream("/net/sf/ant4eclipse/containerresolver.properties"));

      // TODO
      Assert
          .assertTrue(properties.containsKey("containerresolver"),
              "Property 'containerresolver' has to be defined in property file '/net/sf/ant4eclipse/containerresolver.properties'!");

      final ManifestHeaderElement[] elements = ManifestHelper.getManifestHeaderElements(properties
          .getProperty("containerresolver"));

      for (int i = 0; i < elements.length; i++) {
        final ManifestHeaderElement manifestHeaderElement = elements[i];
        final String[] classNames = manifestHeaderElement.getValues();
        for (int j = 0; j < classNames.length; j++) {
          final String className = classNames[j];
          final Class clazz = ContainerClasspathEntryResolver.class.getClassLoader().loadClass(className);
          final Object instance = clazz.newInstance();
          // TODO ASSERT
          addContainerResolver((ClasspathContainerResolver) instance);
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param containerClasspathEntryResolver
   */
  public static void addContainerResolver(final ClasspathContainerResolver classpathContainerResolver) {
    if (!_containerresolver.contains(classpathContainerResolver)) {
      _containerresolver.add(classpathContainerResolver);
    }
  }

  /**
   * <p>
   * Creates a new instance of type {@link ContainerClasspathEntryResolver}.
   * </p>
   */
  public ContainerClasspathEntryResolver() {
    // emtpy constructor
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
      A4ELogging.debug("_containerresolver: " + _containerresolver);
    }

    // set 'handled' to false
    boolean handled = false;

    // iterate over all registered container resolvers
    final Iterator iterator = _containerresolver.iterator();
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
    // initialize all registered container resolvers
    final Iterator iterator = _containerresolver.iterator();
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
    final Iterator iterator = _containerresolver.iterator();
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
