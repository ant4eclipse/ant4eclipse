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

import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.nls.NLSMessage;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * <p>
 * Implements a {@link Version}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class Version {

  @NLSMessage("The given version '%s' is invalid. Must match: <major> [ '.' <minor> [ '.' <micro> [ '_' <qualifier> ] ] ]")
  private static String MSG_FORMATERROR;

  /** the major version */
  private Integer       _major;

  /** the minor version */
  private Integer       _minor;

  /** the micro version */
  private Integer       _micro;

  /** the qualifier version */
  private String        _qualifier;

  private String        _str;

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
  public Version(String version) {

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
          String[] splitted = microWithQualifier.split("_");

          this._micro = Integer.valueOf(splitted[0]);
          if (splitted.length > 1) {
            this._qualifier = splitted[1];
          }
          if (st.hasMoreTokens()) {
            throw new Ant4EclipseException(CoreExceptionCode.ILLEGAL_FORMAT, String.format(MSG_FORMATERROR, version));
          }
        }
      }
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

} /* ENDCLASS */
