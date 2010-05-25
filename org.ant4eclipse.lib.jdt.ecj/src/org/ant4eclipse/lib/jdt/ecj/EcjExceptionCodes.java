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
package org.ant4eclipse.lib.jdt.ecj;

import org.ant4eclipse.lib.core.exception.ExceptionCode;
import org.ant4eclipse.lib.core.nls.NLS;
import org.ant4eclipse.lib.core.nls.NLSMessage;

/**
 * <p>
 * Defines the exception codes for the eclipse java compiler subsystem.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EcjExceptionCodes extends ExceptionCode {

  @NLSMessage("Could not create jar file from file '%s'.")
  public static ExceptionCode     COULD_NOT_CREATE_JAR_FILE_FROM_FILE_EXCEPTION = null;

  @NLSMessage("Unknown target option '%s' in javac task.")
  public static ExceptionCode     UNKNOWN_JAVA_TARGET_OPTION_EXCEPTION;

  @NLSMessage("Unknown source option '%s' in javac task.")
  public static ExceptionCode     UNKNOWN_JAVA_SOURCE_OPTION_EXCEPTION;

  @NLSMessage("Unable to read binary type '%s' from jar file '%s'.")
  public static ExceptionCode     UNABLE_TO_READ_BINARY_TYPE_FROM_JAR_EXCEPTION;

  @NLSMessage("The ant reference id '%s' doesn't point to an EcjAdditionalCompilerArguments object.")
  public static EcjExceptionCodes NO_ECJ_ADDITIONAL_COMPILER_ARGUMENTS_OBJECT;

  @NLSMessage("The file '%s' with global compiler settings could not be found.")
  public static EcjExceptionCodes GLOBAL_COMPILER_SETTINGS_NOT_FOUND_EXCEPTION;

  @NLSMessage("Unable to read content of compilation unit '%s' in source folder '%s' with encoding '%s'.")
  public static EcjExceptionCodes UNABLE_TO_READ_COMPILATION_CONTENT_EXCEPTION;

  @NLSMessage("Compilation was not successful.")
  public static EcjExceptionCodes COMPILATION_WAS_NOT_SUCCESFUL;

  @NLSMessage("Source folder for source file '%s' does not exist.")
  public static ExceptionCode     SOURCE_FOLDER_FOR_SOURCE_FILE_DOES_NOT_EXIST;

  @NLSMessage("The javac source path attibute is not supported by the EcjCompilerAdapter.")
  public static ExceptionCode     JAVAC_SOURCE_PATH_NOT_SUPPORTED_EXCEPTION;

  @NLSMessage("No destination path has been set. You must either set a destination path using the"
      + "'destdir' attribute of the 'javac' task or you must reference a 'EcjAdditionalCompilerArguments' object"
      + "(e.g. from executeJdtProject)")
  public static ExceptionCode     NO_DEST_PATH_SET;

  static {
    NLS.initialize(EcjExceptionCodes.class);
  }

  /**
   * <p>
   * Creates a new instance of type {@link EcjExceptionCodes}.
   * </p>
   * 
   * @param message
   *          the message
   */
  private EcjExceptionCodes(String message) {
    super(message);
  }
}
