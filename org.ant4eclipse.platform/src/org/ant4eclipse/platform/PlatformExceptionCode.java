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
package org.ant4eclipse.platform;

import org.ant4eclipse.core.exception.ExceptionCode;
import org.ant4eclipse.core.nls.NLS;
import org.ant4eclipse.core.nls.NLSMessage;

import org.ant4eclipse.platform.ant.team.TeamExceptionCode;

public class PlatformExceptionCode extends ExceptionCode {

  public static String                MISSING_CONNECTION_TYPE = "Missing connection type";

  public static String                MISSING_REPOSITORY      = "Missing repository";

  @NLSMessage("The usage of the 'project' attribute is not supported anymore. Please use the 'workspaceDirectory' and 'projectName' attributes instead.")
  public static PlatformExceptionCode DEPRECATED_USAGE_OF_SET_PROJECT;

  @NLSMessage("There are two projects with the same specified name: '%s', '%s' ")
  public static PlatformExceptionCode PROJECT_WITH_SAME_SPECIFIED_NAME_ALREADY_EXISTS;

  @NLSMessage("Unknown execution scope '%s'")
  public static PlatformExceptionCode UNKNOWN_EXECUTION_SCOPE;

  @NLSMessage("Error while executing CVS '%s' command: '%s' ")
  public static TeamExceptionCode     ERROR_WHILE_EXECUTING_CVS_COMMAND;

  @NLSMessage("Error while executing SVN '%s' command: '%s' ")
  public static TeamExceptionCode     ERROR_WHILE_EXECUTING_SVN_COMMAND;

  @NLSMessage("Could not create an SVNUrl from URL '%s' of team project description '%s': '%s'")
  public static TeamExceptionCode     COULD_NOT_BUILD_SVNURL_FOR_PROJECT;

  @NLSMessage("The team project set provider with id '%s' is unkown")
  public static TeamExceptionCode     UNKNOWN_TEAM_PROJECT_SET_PROVIDER;

  @NLSMessage("Error while trying to read CVS file '%s': '%s'")
  public static TeamExceptionCode     ERROR_WHILE_READING_CVS_FILE;

  @NLSMessage("Invalid PSF-reference. Expected to have '%s' tokens, but have %s tokens in reference '%s'")
  public static TeamExceptionCode     INVALID_PSF_REFERENCE;

  @NLSMessage("The cvsroot '%s' is invalid: %s")
  public static TeamExceptionCode     INVALID_CVS_ROOT;

  static {
    NLS.initialize(PlatformExceptionCode.class);
  }

  private PlatformExceptionCode(String message) {
    super(message);
  }
}
