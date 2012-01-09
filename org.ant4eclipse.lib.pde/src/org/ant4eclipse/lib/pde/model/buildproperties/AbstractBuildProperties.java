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

import org.ant4eclipse.lib.core.Assure;

import java.util.ArrayList;
import java.util.List;

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
  public static String  SELF                  = ".";

  /** CONTEXT_QUALIFIER */
  private static String CONTEXT_QUALIFIER     = "context";

  /** NONE_QUALIFIER */
  private static String NONE_QUALIFIER        = "none";

  /** indicates that the build script is hand-crafted as opposed to automatically generated */
  protected boolean     _custom;

  /** lists files that will included in the binary version of the plug-in being built */
  protected List<String>    _binIncludes      = new ArrayList<String>();

  /** lists files to exclude from the binary build */
  protected List<String>    _binExcludes      = new ArrayList<String>();

  /** lists files to include in the source build */
  private List<String>      _srcIncludes      = new ArrayList<String>();

  /** lists files to exclude from the source build */
  private List<String>      _srcExcludes      = new ArrayList<String>();

  /**
   * When the element version number ends with .qualifier this indicates by which value ".qualifier" must be replaced.
   * The value of the property can either be context, &lt;value&gt; or none. Context will generate a date according to
   * the system date, or use the CVS tags when the built is automated. Value is an actual value. None will remove
   * ".qualifier". If the property is omitted, context is used.
   */
  private String        _qualifier        = CONTEXT_QUALIFIER;

  /**
   * <p>
   * Returns <code>true</code> if the <code>custom</code> property is set.
   * </p>
   * 
   * @return <code>true</code> if the <code>custom</code> property is set.
   */
  public boolean isCustom() {
    return _custom;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has source excludes.
   * </p>
   * 
   * @return <code>true</code> if the build has source excludes.
   */
  public boolean hasSourceExcludes() {
    return (_srcExcludes != null) && (_srcExcludes.size() > 0);
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has source includes.
   * </p>
   * 
   * @return <code>true</code> if the build has source includes.
   */
  public boolean hasSourceIncludes() {
    return (_srcIncludes != null) && (_srcIncludes.size() > 0);
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has binary excludes.
   * </p>
   * 
   * @return <code>true</code> if the build has binary excludes.
   */
  public boolean hasBinaryExcludes() {
    return (_binExcludes != null) && (_binExcludes.size() > 0);
  }

  /**
   * <p>
   * Returns <code>true</code> if the build has binary includes.
   * </p>
   * 
   * @return <code>true</code> if the build has binary includes.
   */
  public boolean hasBinaryIncludes() {
    return (_binIncludes != null) && (_binIncludes.size() > 0);
  }

  /**
   * <p>
   * Returns the binary excludes.
   * </p>
   * 
   * @return the binary excludes.
   */
  public List<String> getBinaryExcludes() {
    return _binExcludes;
  }

  /**
   * <p>
   * Returns the binary includes.
   * </p>
   * 
   * @return the binary includes.
   */
  public List<String> getBinaryIncludes() {
    return _binIncludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the srcIncludes
   */
  public List<String> getSourceIncludes() {
    return _srcIncludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the srcExcludes
   */
  public List<String> getSourceExcludes() {
    return _srcExcludes;
  }

  /**
   * <p>
   * Returns the qualifier.
   * </p>
   * 
   * @return the qualifier.
   */
  public String getQualifier() {
    return _qualifier;
  }

  /**
   * <p>
   * Returns <code>true</code> if the qualifier is the context qualifier.
   * </p>
   * 
   * @return <code>true</code> if the qualifier is the context qualifier.
   */
  public boolean isContextQualifier() {
    return isContextQualifer( _qualifier );
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
  public static boolean isContextQualifer( String qualifier ) {
    return CONTEXT_QUALIFIER.equals( qualifier );
  }

  /**
   * <p>
   * Returns <code>true</code> if the qualifier is the none qualifier.
   * </p>
   * 
   * @return <code>true</code> if the qualifier is the none qualifier.
   */
  public boolean isNoneQualifier() {
    return isNoneQualifier( _qualifier );
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
  public static boolean isNoneQualifier( String qualifier ) {
    return NONE_QUALIFIER.equals( qualifier );
  }

  /**
   * <p>
   * Sets the custom property.
   * </p>
   * 
   * @param custom
   */
  void setCustom( boolean custom ) {
    _custom = custom;
  }

  /**
   * <p>
   * Sets the binary excludes list.
   * </p>
   * 
   * @param excludes
   */
  void setBinaryExcludes( List<String> excludes ) {
    Assure.notNull( "excludes", excludes );
    _binExcludes = excludes;
  }

  /**
   * <p>
   * Sets the binary excludes list.
   * </p>
   * 
   * @param includes
   */
  void setBinaryIncludes( List<String> includes ) {
    Assure.notNull( "includes", includes );
    _binIncludes = includes;
  }

  /**
   * <p>
   * </p>
   * 
   * @param srcIncludes
   *          the srcIncludes to set
   */
  void setSourceIncludes( List<String> srcIncludes ) {
    Assure.notNull( "srcIncludes", srcIncludes );
    _srcIncludes = srcIncludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @param srcExcludes
   *          the srcExcludes to set
   */
  void setSourceExcludes( List<String> srcExcludes ) {
    Assure.notNull( "srcExcludes", srcExcludes );
    _srcExcludes = srcExcludes;
  }

  /**
   * <p>
   * Sets the qualifier.
   * </p>
   * 
   * @param qualifier
   *          the qualifier to set.
   */
  void setQualifier( String qualifier ) {
    if( qualifier == null ) {
      return;
    }
    _qualifier = qualifier;
  }
  
} /* ENDCLASS */
