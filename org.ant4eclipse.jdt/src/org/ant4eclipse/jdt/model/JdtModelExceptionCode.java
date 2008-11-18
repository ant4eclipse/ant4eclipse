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
package org.ant4eclipse.jdt.model;

import org.ant4eclipse.core.exception.AbstractExceptionCode;

public class JdtModelExceptionCode extends AbstractExceptionCode {

  private static final String               INVALID_JRE_DIRECTORY_MSG             = "The specified directory '%s' doesn't point to a valid java runtime environment.";

  private static final String               NO_JAVA_PROJECT_ROLE_MSG              = "Project '%s' must have a java project role.";

  private static final String               NO_DEFAULT_JAVA_RUNTIME_EXCEPTION_MSG = "Default java runtime could not be resolved!";

  private static final String               JAVA_LAUNCHER_EXECUTION_EXCEPTION_MSG = "Exception while executing java launcher ('%s')";

  public static final JdtModelExceptionCode INVALID_JRE_DIRECTORY                 = new JdtModelExceptionCode(
                                                                                      INVALID_JRE_DIRECTORY_MSG);

  public static final JdtModelExceptionCode NO_JAVA_PROJECT_ROLE                  = new JdtModelExceptionCode(
                                                                                      NO_JAVA_PROJECT_ROLE_MSG);

  public static final JdtModelExceptionCode NO_DEFAULT_JAVA_RUNTIME_EXCEPTION     = new JdtModelExceptionCode(
                                                                                      NO_DEFAULT_JAVA_RUNTIME_EXCEPTION_MSG);

  public static final JdtModelExceptionCode JAVA_LAUNCHER_EXECUTION_EXCEPTION     = new JdtModelExceptionCode(
                                                                                      JAVA_LAUNCHER_EXECUTION_EXCEPTION_MSG);

  private JdtModelExceptionCode(final String message) {
    super(message);
  }
}
