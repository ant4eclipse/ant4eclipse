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
package org.ant4eclipse.ant.platform.team;

import org.ant4eclipse.ant.platform.internal.team.SvnAdapter;
import org.ant4eclipse.ant.platform.internal.team.VcsAdapter;

/**
 * SvnGetProjectSetTask
 * 
 * <p>
 * Note: svnant 1.1.0 is required to work with this task !
 * 
 * @author nils (nils@nilshartmann.net)
 */
public class SvnGetProjectSetTask extends AbstractGetProjectSetTask {

  /**
   * Set to false to use command line client interface instead of JNI JavaHL binding. Defaults to true
   */
  private boolean _javahl        = true;

  /**
   * Set to false to use command line client interface instead of JavaSVN binding. Defaults to false
   */
  private boolean _javasvn       = true;

  /**
   * formatter definition used to format/parse dates (e.g. when revision is specified as date).
   */
  private String  _dateFormatter = null;

  /**
   * time zone used to format/parse dates (e.g. when revision is specified as date).
   */
  private String  _dateTimeZone  = null;

  public String getDateFormatter() {
    return this._dateFormatter;
  }

  public void setDateFormatter( String dateFormatter ) {
    this._dateFormatter = dateFormatter;
  }

  public String getDateTimeZone() {
    return this._dateTimeZone;
  }

  public void setDateTimeZone( String dateTimeZone ) {
    this._dateTimeZone = dateTimeZone;
  }

  public boolean isJavahl() {
    return this._javahl;
  }

  public void setJavahl( boolean javahl ) {
    this._javahl = javahl;
  }

  public boolean isJavasvn() {
    return this._javasvn;
  }

  public void setJavasvn( boolean javasvn ) {
    this._javasvn = javasvn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected VcsAdapter createVcsAdapter() {
    return new SvnAdapter( getProject(), isJavahl(), isJavasvn(), getDateFormatter(), getDateTimeZone() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void checkPrereqs() {
    // no additional pre-reqs
  }
  
} /* ENDCLASS */

