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
package org.ant4eclipse.lib.pde.model.buildproperties;

import org.ant4eclipse.core.Assert;

/**
 * <p>
 * {@link AbstractBuildProperties} represent the "common properties" that are used within plug-in projects and features
 * as described in the "Plug-in Development Environment Guide"-Help.
 * <p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 * @spec Eclipse Help - PDE Guide - "Feature and Plug-in Build Configuration Properties"
 */
public class AbstractBuildProperties {

  /** SELF */
  public static String  SELF              = ".";

  /** CONTEXT_QUALIFIER */
  private static String CONTEXT_QUALIFIER = "context";

  /** NONE_QUALIFIER */
  private static String NONE_QUALIFIER    = "none";

  /** indicates that the build script is hand-crafted as opposed to automatically generated */
  protected boolean     _custom;

  /** lists files that will included in the binary version of the plug-in being built */
  protected String[]    _binIncludes      = new String[0];

  /** lists files to exclude from the binary build */
  protected String[]    _binExcludes      = new String[0];

  /** lists files to include in the source build */
  private String[]      _srcIncludes      = new String[0];

  /** lists files to exclude from the source build */
  private String[]      _srcExcludes      = new String[0];

  /**
   * When the element version number ends with .qualifier this indicates by which value ".qualifier" must be replaced.
   * The value of the property can either be context, &lt;value&gt; or none. Context will generate a date according to
   * the system date, or use the CVS tags when the built is automated. Value is an actual value. None will remove
   * ".qualifier". If the property is omitted, context is used.
   */
  private String        _qualifier        = CONTEXT_QUALIFIER;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractBuildProperties}.
   * </p>
   */
  public AbstractBuildProperties() {
    // nothing to do here...
  }

  /**
   * <p>
   * Returns <code>true</code> if the <code>custom</code> property is set.
   * </p>
   * 
   * @return <code>true</code> if the <code>custom</code> property is set.
   */
  public boolean isCustom() {
    return this._custom;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has source excludes.
   * </p>
   * 
   * @return <code>true</code> if the build has source excludes.
   */
  public boolean hasSourceExcludes() {
    return this._srcExcludes != null && this._srcExcludes.length > 0;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has source includes.
   * </p>
   * 
   * @return <code>true</code> if the build has source includes.
   */
  public boolean hasSourceIncludes() {
    return this._srcIncludes != null && this._srcIncludes.length > 0;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has binary excludes.
   * </p>
   * 
   * @return <code>true</code> if the build has binary excludes.
   */
  public boolean hasBinaryExcludes() {
    return this._binExcludes != null && this._binExcludes.length > 0;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has binary includes.
   * </p>
   * 
   * @return <code>true</code> if the build has binary includes.
   */
  public boolean hasBinaryIncludes() {
    return this._binIncludes != null && this._binIncludes.length > 0;
  }

  /**
   * <p>
   * Returns the binary excludes.
   * </p>
   * 
   * @return the binary excludes.
   */
  public String[] getBinaryExcludes() {
    return this._binExcludes;
  }

  /**
   * <p>
   * Returns the binary includes.
   * </p>
   * 
   * @return the binary includes.
   */
  public String[] getBinaryIncludes() {
    return this._binIncludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the srcIncludes
   */
  public String[] getSourceIncludes() {
    return this._srcIncludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the srcExcludes
   */
  public String[] getSourceExcludes() {
    return this._srcExcludes;
  }

  /**
   * <p>
   * Returns the qualifier.
   * </p>
   * 
   * @return the qualifier.
   */
  public String getQualifier() {
    return this._qualifier;
  }

  /**
   * <p>
   * Returns <code>true</code> if the qualifier is the context qualifier.
   * </p>
   * 
   * @return <code>true</code> if the qualifier is the context qualifier.
   */
  public boolean isContextQualifier() {
    return isContextQualifer(this._qualifier);
  }

  /**
   * <p>
   * Returns <code>true</code> if the qualifier is the context qualifier.
   * </p>
   * 
   * @param qualifier
   *          the qualifier
   * @return <code>true</code> if the qualifier is the context qualifier.
   */
  public static boolean isContextQualifer(String qualifier) {
    return CONTEXT_QUALIFIER.equals(qualifier);
  }

  /**
   * <p>
   * Returns <code>true</code> if the qualifier is the none qualifier.
   * </p>
   * 
   * @return <code>true</code> if the qualifier is the none qualifier.
   */
  public boolean isNoneQualifier() {
    return isNoneQualifier(this._qualifier);
  }

  /**
   * <p>
   * Returns <code>true</code> if the qualifier is the none qualifier.
   * </p>
   * 
   * @param qualifier
   *          the qualifier
   * @return <code>true</code> if the qualifier is the none qualifier.
   */
  public static boolean isNoneQualifier(String qualifier) {
    return NONE_QUALIFIER.equals(qualifier);
  }

  /**
   * <p>
   * Sets the custom property.
   * </p>
   * 
   * @param custom
   */
  void setCustom(boolean custom) {
    this._custom = custom;
  }

  /**
   * <p>
   * Sets the binary excludes list.
   * </p>
   * 
   * @param excludes
   */
  void setBinaryExcludes(String[] excludes) {
    Assert.notNull(excludes);

    this._binExcludes = excludes;
  }

  /**
   * <p>
   * Sets the binary excludes list.
   * </p>
   * 
   * @param includes
   */
  void setBinaryIncludes(String[] includes) {
    Assert.notNull(includes);

    this._binIncludes = includes;
  }

  /**
   * <p>
   * </p>
   * 
   * @param srcIncludes
   *          the srcIncludes to set
   */
  void setSourceIncludes(String[] srcIncludes) {
    Assert.notNull(srcIncludes);

    this._srcIncludes = srcIncludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @param srcExcludes
   *          the srcExcludes to set
   */
  void setSourceExcludes(String[] srcExcludes) {
    Assert.notNull(srcExcludes);

    this._srcExcludes = srcExcludes;
  }

  /**
   * <p>
   * Sets the qualifier.
   * </p>
   * 
   * @param qualifier
   *          the qualifier to set.
   */
  void setQualifier(String qualifier) {
    if (qualifier == null) {
      return;
    }

    this._qualifier = qualifier;
  }
}
