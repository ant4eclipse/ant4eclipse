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

import org.ant4eclipse.core.exception.AbstractExceptionCode;

public class CoreExceptionCode extends AbstractExceptionCode {

  private static final String           X_QUERY_PARSE_EXCEPTION_MSG            = "Could not parse document.";

  private static final String           X_QUERY_INVALID_QUERY_EXCEPTION_MSG    = "Invalid x-query '%s': %s";

  private static final String           X_QUERY_DUCPLICATE_ENTRY_EXCEPTION_MSG = "Duplicate entry '%s' in file '%s'";

  private static final String           CYCLIC_DEPENDENCIES_EXCEPTION_MSG      = "The specified graph contains cyclic dependencies (f.e. '%s').";

  public static final CoreExceptionCode X_QUERY_PARSE_EXCEPTION                = new CoreExceptionCode(
                                                                                   X_QUERY_PARSE_EXCEPTION_MSG);

  public static final CoreExceptionCode X_QUERY_INVALID_QUERY_EXCEPTION        = new CoreExceptionCode(
                                                                                   X_QUERY_INVALID_QUERY_EXCEPTION_MSG);

  public static final CoreExceptionCode X_QUERY_DUCPLICATE_ENTRY_EXCEPTION     = new CoreExceptionCode(
                                                                                   X_QUERY_DUCPLICATE_ENTRY_EXCEPTION_MSG);

  public static final CoreExceptionCode CYCLIC_DEPENDENCIES_EXCEPTION          = new CoreExceptionCode(
                                                                                   CYCLIC_DEPENDENCIES_EXCEPTION_MSG);

  public static final CoreExceptionCode PATH_MUST_NOT_BE_A_FILE                = new CoreExceptionCode(
                                                                                   "Path '%s' must not be a file");

  public static final CoreExceptionCode DIRECTORY_COULD_NOT_BE_CREATED         = new CoreExceptionCode(
                                                                                   "Directory '%s' could not be created for an unkown reason");

  public static final CoreExceptionCode COULD_NOT_LOAD_CLASS                   = new CoreExceptionCode(
                                                                                   "The class '%s' could not be loaded: '%s'");

  public static final CoreExceptionCode COULD_NOT_INSTANTIATE_CLASS            = new CoreExceptionCode(
                                                                                   "The class '%s' could not be instantiated using its default constructor: '%s'");

  private CoreExceptionCode(final String message) {
    super(message);
  }
}
