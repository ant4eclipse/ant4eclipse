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
package org.ant4eclipse.jdt.internal.model.jre;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * <p>
 * Implements a {@link Version}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Version {

  /** the major version */
  private final Integer _major;

  /** the minor version */
  private final Integer _minor;

  /** the micro version */
  private final Integer _micro;

  /** the qualifier version */
  private final String  _qualifier;

  /**
   * @param version
   */
  public Version(final String version) {
    int major = -1;
    int minor = -1;
    int micro = -1;
    String qualifier = null;

    try {
      final StringTokenizer st = new StringTokenizer(version, ".", true);
      major = Integer.parseInt(st.nextToken());

      if (st.hasMoreTokens()) {
        st.nextToken(); // consume delimiter
        minor = Integer.parseInt(st.nextToken());

        if (st.hasMoreTokens()) {
          st.nextToken(); // consume delimiter

          final String microWithQualifier = st.nextToken();
          final String[] splitted = microWithQualifier.split("_");

          micro = Integer.parseInt(splitted[0]);
          if (splitted.length > 1) {
            qualifier = splitted[1];
          }
          if (st.hasMoreTokens()) {
            throw new IllegalArgumentException("invalid format"); //$NON-NLS-1$
          }
        }
      }
    } catch (final NoSuchElementException e) {
      throw new IllegalArgumentException("invalid version format '" + version + "'"); //$NON-NLS-1$
    }

    this._major = major != -1 ? Integer.valueOf(major) : null;
    this._minor = minor != -1 ? Integer.valueOf(minor) : null;
    this._micro = micro != -1 ? Integer.valueOf(micro) : null;
    this._qualifier = qualifier;
  }

  public int getMajor() {
    return this._major != null ? this._major.intValue() : 0;
  }

  public int getMinor() {
    return this._minor != null ? this._minor.intValue() : 0;
  }

  public int getMicro() {
    return this._micro != null ? this._micro.intValue() : 0;
  }

  public String getQualifier() {
    return this._qualifier;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._major == null) ? 0 : this._major.hashCode());
    result = prime * result + ((this._micro == null) ? 0 : this._micro.hashCode());
    result = prime * result + ((this._minor == null) ? 0 : this._minor.hashCode());
    result = prime * result + ((this._qualifier == null) ? 0 : this._qualifier.hashCode());
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
    final Version other = (Version) obj;
    if (this._major == null) {
      if (other._major != null) {
        return false;
      }
    } else if (!this._major.equals(other._major)) {
      return false;
    }
    if (this._micro == null) {
      if (other._micro != null) {
        return false;
      }
    } else if (!this._micro.equals(other._micro)) {
      return false;
    }
    if (this._minor == null) {
      if (other._minor != null) {
        return false;
      }
    } else if (!this._minor.equals(other._minor)) {
      return false;
    }
    if (this._qualifier == null) {
      if (other._qualifier != null) {
        return false;
      }
    } else if (!this._qualifier.equals(other._qualifier)) {
      return false;
    }
    return true;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(this._major);
    if (this._minor != null) {
      buffer.append(".");
      buffer.append(this._minor);
    }
    if (this._micro != null) {
      buffer.append(".");
      buffer.append(this._micro);
    }
    if (this._qualifier != null) {
      buffer.append("_");
      buffer.append(this._qualifier);
    }
    return buffer.toString();
  }
}
