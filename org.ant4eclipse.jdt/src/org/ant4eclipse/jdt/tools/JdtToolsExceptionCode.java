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
package org.ant4eclipse.jdt.tools;

import org.ant4eclipse.core.exception.AbstractExceptionCode;

public class JdtToolsExceptionCode extends AbstractExceptionCode {

  /** - */
  private static final String               BUILD_ORDER_EXCEPTION_MSG                  = "The specified directory '%s' doesn't point to a valid java runtime environment.";

  /** - */
  private static final String               REFERENCE_TO_UNKNOWN_PROJECT_EXCEPTION_MSG = "Project '%s'  references the unknown project '%s'";

  private static final String               CYCLIC_DEPENDENCIES_EXCEPTION_MSG          = "Set of projects contains cyclic dependencies!";

  private static final String               REFERENCE_TO_UNKNOWN_BUNDLE_EXCEPTION_MSG  = "Project '%s'  references the unknown project '%s'";

  /** BUILD_ORDER_EXCEPTION */
  public static final JdtToolsExceptionCode BUILD_ORDER_EXCEPTION                      = new JdtToolsExceptionCode(
                                                                                           BUILD_ORDER_EXCEPTION_MSG);

  /** REFERENCE_TO_UNKNOWN_PROJECT */
  public static final JdtToolsExceptionCode REFERENCE_TO_UNKNOWN_PROJECT_EXCEPTION     = new JdtToolsExceptionCode(
                                                                                           REFERENCE_TO_UNKNOWN_PROJECT_EXCEPTION_MSG);

  public static final JdtToolsExceptionCode CYCLIC_DEPENDENCIES_EXCEPTION              = new JdtToolsExceptionCode(
                                                                                           CYCLIC_DEPENDENCIES_EXCEPTION_MSG);

  public static final JdtToolsExceptionCode REFERENCE_TO_UNKNOWN_BUNDLE_EXCEPTION      = new JdtToolsExceptionCode(
                                                                                           REFERENCE_TO_UNKNOWN_BUNDLE_EXCEPTION_MSG);

  /**
   * @param message
   */
  private JdtToolsExceptionCode(final String message) {
    super(message);
  }
}
