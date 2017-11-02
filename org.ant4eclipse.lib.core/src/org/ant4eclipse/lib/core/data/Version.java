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
package org.ant4eclipse.lib.core.data;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.nls.NLS;
import org.ant4eclipse.lib.core.nls.NLSMessage;

/**
 * <p>
 * Implements a {@link Version}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class Version implements Comparable<Version> {

  @NLSMessage("The given version '%s' is invalid. Must match: <major> [ '.' <minor> [ '.' <micro> [ '_' <qualifier> ] ] ]")
  public static String MSG_FORMATERROR;

  /** the major version */
  private Integer      _major;

  /** the minor version */
  private Integer      _minor;

  /** the micro version */
  private Integer      _micro;

  /** the qualifier version */
  private String       _qualifier;

  private String       _str;

  static {
    NLS.initialize(Version.class);
  }

  /**
   * Sets up this version information from the supplied formatted string. The format is as followed:<br/>
   * 
   * &lt;major&gt;['.'&lt;minor&gt;['.'&lt;micro&gt;['_'&lt;qualifier&gt;]]]
   * 
   * The meaning of the qualifier depends on the context where the version information is used.
   * 
   * @param version
   *          A formatted version string. Neither <code>null</code> nor empty.
   */
  public static final Version newBundleVersion(String version) {
    Assure.nonEmpty("version", version);
    return new Version(version, true);
  }

  /**
   * Sets up this version information from the supplied formatted string. The format is as followed:<br/>
   * 
   * &lt;major&gt;['.'&lt;minor&gt;['.'&lt;micro&gt;[&lt;qualifier&gt;]]]
   * 
   * The meaning of the qualifier depends on the context where the version information is used.
   * 
   * @param version
   *          A formatted version string. Neither <code>null</code> nor empty.
   */
  public static final Version newStandardVersion(String version) {
    Assure.nonEmpty("version", version);
    return new Version(version, false);
  }

  /**
   * Sets up this version information from the supplied formatted string. The format is as followed:<br/>
   * 
   * &lt;major&gt;['.'&lt;minor&gt;['.'&lt;micro&gt;['_'&lt;qualifier&gt;]]]
   * 
   * The meaning of the qualifier depends on the context where the version information is used.
   * 
   * @param version
   *          A formatted version string. Neither <code>null</code> nor empty.
   * @param bundleversion
   *          <code>true</code> <=> The format of the version denotes a bundle version.
   */
  private Version(String version, boolean bundleversion) {
    Assure.nonEmpty("version", version);

    this._major = Integer.valueOf(0);
    this._minor = Integer.valueOf(0);
    this._micro = Integer.valueOf(0);
    this._qualifier = null;

    try {
      StringTokenizer st = new StringTokenizer(version, ".", false);
      this._major = Integer.valueOf(st.nextToken());
      if (st.hasMoreTokens()) {
        this._minor = Integer.valueOf(st.nextToken());
        if (st.hasMoreTokens()) {

          String microWithQualifier = st.nextToken();
          int firstpos = indexOf(microWithQualifier, bundleversion ? '_' : '_', '-');
          if (firstpos == -1) {
            // no delimiter
            this._micro = Integer.valueOf(microWithQualifier);
          } else {
            // with delimiter separating the qualifier
            this._micro = Integer.valueOf(microWithQualifier.substring(0, firstpos));
            if (firstpos < microWithQualifier.length() - 1) {
              this._qualifier = microWithQualifier.substring(firstpos + 1);
            }
          }

          if (st.hasMoreTokens()) {
            throw new Ant4EclipseException(CoreExceptionCode.ILLEGAL_FORMAT, String.format(MSG_FORMATERROR, version));
          }
        }
      }
    } catch (NumberFormatException ex) {
      throw new Ant4EclipseException(CoreExceptionCode.ILLEGAL_FORMAT, String.format(MSG_FORMATERROR, version));
    } catch (NoSuchElementException e) {
      throw new Ant4EclipseException(CoreExceptionCode.ILLEGAL_FORMAT, String.format(MSG_FORMATERROR, version));
    }

    // create a textual representation
    StringBuffer buffer = new StringBuffer();
    buffer.append(this._major);
    buffer.append(".");
    buffer.append(this._minor);
    buffer.append(".");
    buffer.append(this._micro);
    if (this._qualifier != null) {
      buffer.append("_");
      buffer.append(this._qualifier);
    }
    this._str = buffer.toString();

  }

  /**
   * Determines the leftmost character position of a list of candidates.
   * 
   * @param str
   *          The String where to look for. Not <code>null</code>.
   * @param candidates
   *          The characters to look for.
   * 
   * @return The position of the leftmost found character or -1.
   */
  private int indexOf(String str, char... candidates) {
    int result = Integer.MAX_VALUE;
    for (char ch : candidates) {
      int pos = str.indexOf(ch);
      if (pos != -1) {
        result = Math.min(result, pos);
      }
    }
    if (result == Integer.MAX_VALUE) {
      result = -1;
    }
    return result;
  }

  /**
   * Returns the major version.
   * 
   * @return The major version.
   */
  public int getMajor() {
    return this._major.intValue();
  }

  /**
   * Returns the minor version.
   * 
   * @return The minor version.
   */
  public int getMinor() {
    return this._minor.intValue();
  }

  /**
   * Returns the micro version.
   * 
   * @return The micro version.
   */
  public int getMicro() {
    return this._micro.intValue();
  }

  /**
   * Returns the qualifier for this version.
   * 
   * @return The qualifier for this version. Maybe <code>null</code>.
   */
  public String getQualifier() {
    return this._qualifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return this._str.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Version other = (Version) obj;
    return this._str.equals(other._str);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this._str;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Version o) {
    int cmp = compare(this._major, o._major);
    if (cmp == 0) {
      cmp = compare(this._minor, o._minor);
      if (cmp == 0) {
        cmp = compare(this._micro, o._micro);
        if (cmp == 0) {
          cmp = compare(this._qualifier, o._qualifier);
        }
      }
    }
    return cmp;
  }

  /**
   * @param _major2
   * @param _major3
   * @return
   */
  private static <T extends Comparable<T>> int compare(T o1, T o2) {
    if (o1 == o2) {
      return 0;
    }
    if (o1 == null && o2 != null) {
      return -1;
    }
    if (o1 != null && o2 == null) {
      return 1;
    }
    return o1.compareTo(o2);
  }

} /* ENDCLASS */
