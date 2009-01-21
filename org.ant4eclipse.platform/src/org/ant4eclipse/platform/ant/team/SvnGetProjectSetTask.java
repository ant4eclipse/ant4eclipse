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
package org.ant4eclipse.platform.ant.team;

import org.ant4eclipse.platform.internal.ant.team.SvnAdapter;
import org.ant4eclipse.platform.internal.ant.team.VcsAdapter;

/**
 * SvnGetProjectSetTask 
 * 
 * <p>Note: svnant 1.1.0 is required to work with this task !
 * 
 * @author nils (nils@nilshartmann.net)
 */
public class SvnGetProjectSetTask extends AbstractGetProjectSetTask {
 
  /**
   * Set to false to use command line client interface instead of JNI JavaHL binding.  
   * Defaults to true
   */
  private boolean _javahl = true;
  
  /**
   * Set to false to use command line client interface instead of JavaSVN binding.
   * Defaults to false
   */
  private boolean _javasvn = true;
  
  /**
   * formatter definition used to format/parse dates (e.g. when revision is specified as date).
   */
  private String _dateFormatter = null;
  
  /**
   * time zone used to format/parse dates (e.g. when revision is specified as date).
   */
  private String _dateTimeZone = null;
  
  public String getDateFormatter() {
    return _dateFormatter;
  }

  public void setDateFormatter(String dateFormatter) {
    _dateFormatter = dateFormatter;
  }

  public String getDateTimeZone() {
    return _dateTimeZone;
  }

  public void setDateTimeZone(String dateTimeZone) {
    _dateTimeZone = dateTimeZone;
  }

  public boolean isJavahl() {
    return _javahl;
  }

  public void setJavahl(boolean javahl) {
    _javahl = javahl;
  }

  public boolean isJavasvn() {
    return _javasvn;
  }

  public void setJavasvn(boolean javasvn) {
    _javasvn = javasvn;
  }

  protected VcsAdapter createVcsAdapter() {
    return new SvnAdapter(getProject(), isJavahl(), isJavasvn(), getDateFormatter(), getDateTimeZone());
  }

  protected void checkPrereqs() {
    // no additional pre-reqs
  }
}