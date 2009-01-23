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

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolvedClasspathEntry {

  /** the file that make */
  private final File[]             _entries;

  private final AccessRestrictions _accessRestrictions;

  public ResolvedClasspathEntry(final File[] entries, final AccessRestrictions accessRestrictions) {
    Assert.notNull(entries);

    this._entries = entries;
    this._accessRestrictions = accessRestrictions;
  }

  public ResolvedClasspathEntry(final File entry, final AccessRestrictions accessRestrictions) {
    this(new File[] { entry }, accessRestrictions);
  }

  public ResolvedClasspathEntry(final File[] entries) {
    this(entries, null);
  }

  public ResolvedClasspathEntry(final File entry) {
    this(new File[] { entry }, null);
  }

  public File[] getEntries() {
    return this._entries;
  }

  public AccessRestrictions getAccessRestrictions() {
    return this._accessRestrictions;
  }

  public boolean hasAccessRestrictions() {
    return this._accessRestrictions != null;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._accessRestrictions == null) ? 0 : this._accessRestrictions.hashCode());
    result = prime * result + ResolvedClasspathEntry.hashCode(this._entries);
    return result;
  }

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
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class AccessRestrictions {

    /** - */
    private boolean            _excludeAll = true;

    /** - */
    private final List<String> _publicPackages;

    /** - */
    private final List<String> _privatePackages;

    public AccessRestrictions() {
      this._publicPackages = new LinkedList<String>();
      this._privatePackages = new LinkedList<String>();
    }

    public AccessRestrictions(final List<String> publicPackages, final List<String> privatePackages,
        final boolean excludeAll) {

      // TODO TYPEN !!

      this._publicPackages = publicPackages;
      this._privatePackages = privatePackages;
      this._excludeAll = excludeAll;
    }

    public boolean isExcludeAll() {
      return this._excludeAll;
    }

    public List<String> getPublicPackages() {
      return this._publicPackages;
    }

    public List<String> getPrivatePackages() {
      return this._privatePackages;
    }

    public String asFormattedString() {
      final StringBuffer result = new StringBuffer();

      for (final String publicPackage : this._publicPackages) {
        result.append("+");
        result.append(publicPackage.replace('.', '/'));
        result.append("/*;");
      }

      for (final String privatePackage : this._privatePackages) {
        result.append("-");
        result.append(privatePackage.replace('.', '/'));
        result.append("/*;");
      }

      if (this._excludeAll) {
        result.append("-**/*");
      } else {
        result.append("+**/*");
      }

      return result.toString();
    }

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

  /**
   * @generated by CodeSugar http://sourceforge.net/projects/codesugar
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
}
