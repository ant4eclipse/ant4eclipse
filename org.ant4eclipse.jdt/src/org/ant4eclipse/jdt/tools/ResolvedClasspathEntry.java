/**********************************************************************
 * Copyright (c) 2005-2007 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.tools;

import org.ant4eclipse.core.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * Represents a resolved class path entry. A resolved class path entry contains an array of files that belong to this
 * entry. It also contains an (optional) {@link AccessRestrictions} object that specifies the visibility rules for this
 * entry.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolvedClasspathEntry {

  /** the file array */
  private final File[]             _entries;

  /** the visibility rules */
  private final AccessRestrictions _accessRestrictions;

  /**
   * <p>
   * Creates a new instance of type ResolvedClasspathEntry.
   * </p>
   * 
   * @param entries
   * @param accessRestrictions
   */
  public ResolvedClasspathEntry(final File[] entries, final AccessRestrictions accessRestrictions) {
    Assert.notNull(entries);

    this._entries = entries;
    this._accessRestrictions = accessRestrictions;
  }

  /**
   * <p>
   * Creates a new instance of type ResolvedClasspathEntry.
   * </p>
   * 
   * @param entry
   * @param accessRestrictions
   */
  public ResolvedClasspathEntry(final File entry, final AccessRestrictions accessRestrictions) {
    this(new File[] { entry }, accessRestrictions);
  }

  /**
   * <p>
   * Creates a new instance of type ResolvedClasspathEntry.
   * </p>
   * 
   * @param entries
   *          the file entries
   */
  public ResolvedClasspathEntry(final File[] entries) {
    this(entries, null);
  }

  /**
   * <p>
   * Creates a new instance of type ResolvedClasspathEntry.
   * </p>
   * 
   * @param entry
   *          the file entry
   */
  public ResolvedClasspathEntry(final File entry) {
    this(new File[] { entry }, null);
  }

  /**
   * <p>
   * Returns all file entries for this class path entry.
   * </p>
   * 
   * @return all file entries for this class path entry.
   */
  public File[] getEntries() {
    return this._entries;
  }

  /**
   * <p>
   * Returns the {@link AccessRestrictions}.
   * </p>
   * 
   * @return the {@link AccessRestrictions}.
   */
  public AccessRestrictions getAccessRestrictions() {
    return this._accessRestrictions;
  }

  /**
   * <p>
   * Returns <code>true</code>, if the class path entry contains {@link AccessRestrictions}.
   * </p>
   * 
   * @return <code>true</code>, if the class path entry contains {@link AccessRestrictions}.
   */
  public boolean hasAccessRestrictions() {
    return this._accessRestrictions != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._accessRestrictions == null) ? 0 : this._accessRestrictions.hashCode());
    result = prime * result + ResolvedClasspathEntry.hashCode(this._entries);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ResolvedClasspathEntry other = (ResolvedClasspathEntry) obj;
    if (this._accessRestrictions == null) {
      if (other._accessRestrictions != null) {
        return false;
      }
    } else if (!this._accessRestrictions.equals(other._accessRestrictions)) {
      return false;
    }
    if (!Arrays.equals(this._entries, other._entries)) {
      return false;
    }
    return true;
  }

  /**
   * <p>
   * Generated helper method that returns the hash code of an array of objects.
   * </p>
   * 
   * @param array
   *          the array.
   * @return the hash code of an array of objects.
   */
  private static int hashCode(final Object[] array) {
    final int prime = 31;
    if (array == null) {
      return 0;
    }
    int result = 1;
    for (final Object element : array) {
      result = prime * result + (element == null ? 0 : element.hashCode());
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedClasspathEntry:");
    buffer.append(" { ");
    for (int i0 = 0; (this._entries != null) && (i0 < this._entries.length); i0++) {
      buffer.append(" _entries[" + i0 + "]: ");
      buffer.append(this._entries[i0]);
    }
    buffer.append(" } ");
    buffer.append(" _accessRestrictions: ");
    buffer.append(this._accessRestrictions);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * <p>
   * Encapsulates the visibility rules of a resolved class path entry.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class AccessRestrictions {

    /** indicates if everything should be excluded by default */
    private boolean           _excludeAll = true;

    /** the set of public packages */
    private final Set<String> _publicPackages;

    /** the set of private packages */
    private final Set<String> _privatePackages;

    /**
     * <p>
     * Creates a new instance of type AccessRestrictions.
     * </p>
     * 
     */
    public AccessRestrictions() {
      this._publicPackages = new LinkedHashSet<String>();
      this._privatePackages = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Creates a new instance of type AccessRestrictions.
     * </p>
     * 
     * @param publicPackages
     *          the set of public packages
     * @param privatePackages
     *          the set of private packages
     * @param excludeAll
     *          indicates if everything should be excluded by default
     */
    public AccessRestrictions(final Set<String> publicPackages, final Set<String> privatePackages,
        final boolean excludeAll) {

      // TODO
      // AE-67: Support for types (exclusion/inclusion of classes/interfaces)

      this._publicPackages = publicPackages;
      this._privatePackages = privatePackages;
      this._excludeAll = excludeAll;
    }

    /**
     * <p>
     * Returns <code>true</code> if every packages is excluded by default.
     * </p>
     * 
     * @return <code>true</code> if every packages is excluded by default.
     */
    public boolean isExcludeAll() {
      return this._excludeAll;
    }

    /**
     * <p>
     * Adds a public package.
     * </p>
     * 
     * @param name
     *          the name of the public package.
     */
    public void addPublicPackage(final String name) {
      this._publicPackages.add(name);
    }

    /**
     * <p>
     * Adds a private package.
     * </p>
     * 
     * @param name
     *          the name of the private package.
     */
    public void addPrivatePackage(final String name) {
      this._privatePackages.add(name);
    }

    /**
     * <p>
     * Returns all public packages.
     * </p>
     * 
     * @return all public packages.
     */
    public Set<String> getPublicPackages() {
      return this._publicPackages;
    }

    /**
     * <p>
     * Returns all private packages.
     * </p>
     * 
     * @return all private packages.
     */
    public Set<String> getPrivatePackages() {
      return this._privatePackages;
    }

    /**
     * <p>
     * Returns the access restrictions as formatted string.
     * </p>
     * 
     * @return the access restrictions as formatted string.
     */
    public String asFormattedString() {
      final StringBuffer result = new StringBuffer();

      // format public packages
      for (final String publicPackage : this._publicPackages) {
        result.append("+");
        result.append(publicPackage.replace('.', '/'));
        result.append("/*;");
      }

      // format private packages
      for (final String privatePackage : this._privatePackages) {
        result.append("-");
        result.append(privatePackage.replace('.', '/'));
        result.append("/*;");
      }

      // exclude/include all
      if (this._excludeAll) {
        result.append("-**/*");
      } else {
        result.append("+**/*");
      }

      // return result
      return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("[AccessRestrictions:");
      buffer.append(" _excludeAll: ");
      buffer.append(this._excludeAll);
      buffer.append(" _publicPackages: ");
      buffer.append(this._publicPackages);
      buffer.append(" _privatePackages: ");
      buffer.append(this._privatePackages);
      buffer.append("]");
      return buffer.toString();
    }

  }
}
