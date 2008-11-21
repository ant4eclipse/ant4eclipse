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
package org.ant4eclipse.core;

import org.ant4eclipse.core.exception.ExceptionCode;

public class CoreExceptionCode extends ExceptionCode {

  @NLSMessage("Could not parse document.")
  public static CoreExceptionCode X_QUERY_PARSE_EXCEPTION;

  @NLSMessage("Invalid x-query '%s': %s")
  public static CoreExceptionCode X_QUERY_INVALID_QUERY_EXCEPTION;

  @NLSMessage("Duplicate entry '%s' in file '%s'")
  public static CoreExceptionCode X_QUERY_DUCPLICATE_ENTRY_EXCEPTION;

  @NLSMessage("The specified graph contains cyclic dependencies (f.e. '%s').")
  public static CoreExceptionCode CYCLIC_DEPENDENCIES_EXCEPTION;

  @NLSMessage("Path '%s' must not be a file")
  public static CoreExceptionCode PATH_MUST_NOT_BE_A_FILE;

  @NLSMessage("Directory '%s' could not be created for an unkown reason")
  public static CoreExceptionCode DIRECTORY_COULD_NOT_BE_CREATED;

  @NLSMessage("The class '%s' could not be loaded: '%s'")
  public static CoreExceptionCode COULD_NOT_LOAD_CLASS;

  @NLSMessage("The class '%s' could not be instantiated using its default constructor: '%s'")
  public static CoreExceptionCode COULD_NOT_INSTANTIATE_CLASS;

  static {
    NLS.initialize(CoreExceptionCode.class);
  }

  private CoreExceptionCode(final String message) {
    super(message);
  }
}
