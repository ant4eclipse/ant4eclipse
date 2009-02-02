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
package org.ant4eclipse.pde.internal.tools.target;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * Abstract base implementation for all {@link BundleSet BundleSets}.
 * </p>
 *
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractBundleSet implements BundleSet {

  /** the list that contains all the bundle descriptions */
  private final List   _bundleDescriptonList;

  /** indicates whether or not the platform is initialized */
  private boolean      _isInitialised = false;

  /** the id of the BundleSet */
  private final Object _id;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractBundleSet}.
   * </p>
   *
   * @param id
   *          the identifier of this {@link BundleSet}
   */
  protected AbstractBundleSet(final Object id) {
    A4ELogging.trace("AbstractBundleSet<init>()");
    Assert.notNull(id);

    this._id = id;
    this._bundleDescriptonList = new LinkedList();
  }

  /**
   * <p>
   * Abstract method. Must be implemented to read bundles from an underlying location (e.g. a directory that contains
   * binary bundles or a workspace that contains bundle projects).
   * </p>
   */
  protected abstract void readBundles();

  /**
   * {@inheritDoc}
   */
  public final Object getId() {
    return this._id;
  }

  /**
   * {@inheritDoc}
   */
  public final void initialize() {
    if (!isInitialised()) {

      final long start = System.currentTimeMillis();

      refresh();

      final long stop = System.currentTimeMillis();
      A4ELogging.debug("Needed %s ms to read %s bundles from bundle set.", new Object[] { new Long(stop - start),
          "" + this._bundleDescriptonList.size() });
    }

    this._isInitialised = true;
  }

  /**
   * {@inheritDoc}
   */
  public final void invalidate() {
    this._isInitialised = false;
  }

  /**
   * {@inheritDoc}
   */
  public final void refresh() {
    // clear list of bundles...
    this._bundleDescriptonList.clear();

    readBundles();

    this._isInitialised = true;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isInitialised() {
    return this._isInitialised;
  }

  /**
   * {@inheritDoc}
   */
  public final BundleDescription[] getAllBundleDescriptions() {
    initialize();
    return (BundleDescription[]) this._bundleDescriptonList.toArray(new BundleDescription[0]);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean contains(final String symbolicName) {
    Assert.notNull(symbolicName);

    initialize();

    final Iterator iterator = this._bundleDescriptonList.iterator();

    while (iterator.hasNext()) {
      final BundleDescription description = (BundleDescription) iterator.next();
      if (symbolicName.equals(description.getSymbolicName())
      /*
       * TODO || "system.bundle".equals(symbolicName) && description.isSystemBundle()
       */) {
        return true;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final BundleDescription[] getBundleDescription(final String symbolicName) {
    Assert.notNull(symbolicName);

    initialize();

    final List result = new LinkedList();

    final Iterator iterator = this._bundleDescriptonList.iterator();

    while (iterator.hasNext()) {
      final BundleDescription description = (BundleDescription) iterator.next();
      if (symbolicName.equals(description.getSymbolicName())
      /*
       * TODO || "system.bundle".equals(symbolicName) && description.isSystemBundle()
       */) {
        result.add(description);
      }
    }

    return (BundleDescription[]) result.toArray(new BundleDescription[0]);
  }

  /**
   * <p>
   * Adds the given {@link BundleDescription} to the {@link BundleSet}.
   * </p>
   *
   * @param bundleDescription
   *          the {@link BundleDescription} to add.
   */
  protected final void addBundleDescription(final BundleDescription bundleDescription) {
    Assert.notNull(bundleDescription);

    // TODO
    System.err.println(bundleDescription.getUserObject());

    this._bundleDescriptonList.add(bundleDescription);
  }
}
