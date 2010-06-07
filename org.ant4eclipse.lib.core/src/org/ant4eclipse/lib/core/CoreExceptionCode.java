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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.exception.ExceptionCode;
import org.ant4eclipse.lib.core.nls.NLS;
import org.ant4eclipse.lib.core.nls.NLSMessage;

/**
 * <p>
 * The {@link ExceptionCode ExceptionCodes} for the core layer.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CoreExceptionCode extends ExceptionCode {

  /** - */
  @NLSMessage("The manifest header '%s' does not exist.")
  public static ExceptionCode     MANIFEST_HEADER_DOES_NOT_EXIST = null;

  /** - */
  @NLSMessage("Could not parse document.")
  public static CoreExceptionCode X_QUERY_PARSE_EXCEPTION;

  /** - */
  @NLSMessage("Invalid x-query '%s': %s")
  public static CoreExceptionCode X_QUERY_INVALID_QUERY_EXCEPTION;

  /** - */
  @NLSMessage("Duplicate entry '%s' in file '%s'")
  public static CoreExceptionCode X_QUERY_DUCPLICATE_ENTRY_EXCEPTION;

  /** - */
  @NLSMessage("The specified graph contains cyclic dependencies (e.g. '%s').")
  public static CoreExceptionCode CYCLIC_DEPENDENCIES_EXCEPTION;

  /** - */
  @NLSMessage("Path '%s' must not be a file")
  public static CoreExceptionCode PATH_MUST_NOT_BE_A_FILE;

  /** - */
  @NLSMessage("Directory '%s' could not be created for an unkown reason")
  public static CoreExceptionCode DIRECTORY_COULD_NOT_BE_CREATED;

  /** - */
  @NLSMessage("The class '%s' could not be loaded: '%s'")
  public static CoreExceptionCode COULD_NOT_LOAD_CLASS;

  /** - */
  @NLSMessage("The class '%s' could not be instantiated using constructor '%s'")
  public static CoreExceptionCode COULD_NOT_INSTANTIATE_CLASS;

  /** - */
  @NLSMessage("Could not access method '%s' on type '%s'")
  public static CoreExceptionCode COULD_NOT_ACCESS_METHOD;

  /** - */
  @NLSMessage("Parameter '%s' must be set on task '%s'")
  public static CoreExceptionCode PARAMETER_MUST_BE_SET_ON_TASK;

  /** - */
  @NLSMessage("Parameter '%s' must be set on type '%s'")
  public static CoreExceptionCode PARAMETER_MUST_BE_SET_ON_TYPE;

  /** service registry related exception codes * */
  /** - */
  @NLSMessage("Service registry has to be initialized.")
  public static CoreExceptionCode SERVICE_REGISTRY_HAS_TO_BE_INITIALIZED;

  /** - */
  @NLSMessage("Service registry has to be disposed.")
  public static CoreExceptionCode SERVICE_REGISTRY_HAS_TO_BE_DISPOSED;

  /** - */
  @NLSMessage("Service identifier '%s' is not unique.")
  public static CoreExceptionCode SERVICE_IDENTIFIER_IS_NOT_UNIQUE;

  /** - */
  @NLSMessage("Service '%s' could not be initialized.")
  public static CoreExceptionCode SERVICE_COULD_NOT_BE_INITIALIZED;

  /** - */
  @NLSMessage("Service '%s' could not be diposed.")
  public static CoreExceptionCode SERVICE_COULD_NOT_BE_DISPOSED;

  /** - */
  @NLSMessage("Service '%s' is not available.")
  public static CoreExceptionCode SERVICE_NOT_AVAILABLE;

  /** assert related exception codes * */
  /** - */
  @NLSMessage("Precondition violated: Object has to be set.")
  public static CoreExceptionCode ASSERT_NOT_NULL_FAILED;

  /** utility function related exception codes. */
  /** - */
  @NLSMessage("Could not export resource '%s' into file '%s'.")
  public static CoreExceptionCode COULD_NOT_EXPORT_RESOURCE;

  /** - */
  @NLSMessage("Launching the executable '%s' failed.")
  public static CoreExceptionCode EXECUTION_FAILURE;

  /** - */
  @NLSMessage("IO failed.")
  public static CoreExceptionCode IO_FAILURE;

  /** - */
  @NLSMessage("The resource '%s' is not available on the classpath.")
  public static CoreExceptionCode RESOURCE_NOT_ON_THE_CLASSPATH;

  /** - */
  @NLSMessage("Unpacking the archive '%s' failed.")
  public static CoreExceptionCode UNPACKING_FAILED;

  /** - */
  @NLSMessage("Failed to launch executable '%s' (returncode %d).\nOutput:\n%sError:\n%s")
  public static CoreExceptionCode LAUNCHING_FAILURE;

  /** - */
  @NLSMessage("An IO operation on the file '%s' failed.")
  public static CoreExceptionCode FILEIO_FAILURE;

  /** - */
  @NLSMessage("An IO operation on the resource '%s' failed.")
  public static CoreExceptionCode RESOURCEIO_FAILURE;

  /** - */
  @NLSMessage("A precondition has been violated: %s")
  public static CoreExceptionCode PRECONDITION_VIOLATION;

  /** - */
  @NLSMessage("An invalid format has been used: %s")
  public static CoreExceptionCode ILLEGAL_FORMAT;

  /** - */
  @NLSMessage("Failed to access canonical file for path '%s'.")
  public static CoreExceptionCode CANONICAL_FILE;

  static {
    NLS.initialize(CoreExceptionCode.class);
  }

  /**
   * <p>
   * Creates a new instance of type CoreExceptionCode.
   * </p>
   * 
   * @param message
   */
  private CoreExceptionCode(String message) {
    super(message);
  }

} /* ENDCLASS */
