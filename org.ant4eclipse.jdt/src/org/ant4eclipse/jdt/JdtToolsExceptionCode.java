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
package org.ant4eclipse.jdt;

import org.ant4eclipse.core.exception.ExceptionCode;
import org.ant4eclipse.core.nls.NLS;
import org.ant4eclipse.core.nls.NLSMessage;

public class JdtToolsExceptionCode extends ExceptionCode {

  /** BUILD_ORDER_EXCEPTION */
  @NLSMessage("The specified directory '%s' doesn't point to a valid java runtime environment.")
  public static JdtToolsExceptionCode BUILD_ORDER_EXCEPTION;

  /** REFERENCE_TO_UNKNOWN_PROJECT */
  @NLSMessage("Project '%s'  references the unknown project '%s'")
  public static JdtToolsExceptionCode REFERENCE_TO_UNKNOWN_PROJECT_EXCEPTION;

  @NLSMessage("Set of projects contains cyclic dependencies!")
  public static JdtToolsExceptionCode CYCLIC_DEPENDENCIES_EXCEPTION;

  @NLSMessage("Project '%s'  references the unknown project '%s'")
  public static JdtToolsExceptionCode REFERENCE_TO_UNKNOWN_BUNDLE_EXCEPTION;

  @NLSMessage("The class path of project '%s' contains a class path variable '%s' that is not bound.")
  public static JdtToolsExceptionCode UNBOUND_CLASS_PATH_VARIABLE;

  static {
    NLS.initialize(JdtToolsExceptionCode.class);
  }

  /**
   * @param message
   */
  private JdtToolsExceptionCode(final String message) {
    super(message);
  }
}
