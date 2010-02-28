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
package org.ant4eclipse.lib.jdt;

import org.ant4eclipse.lib.core.exception.ExceptionCode;
import org.ant4eclipse.lib.core.nls.NLS;
import org.ant4eclipse.lib.core.nls.NLSMessage;

/**
 * <p>
 * ExceptionCodes for JDT tools.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtExceptionCode extends ExceptionCode {

  @NLSMessage("\n\nNo 'jdtClassPathLibrary' defined for library entry '%1$s'.\n"
      + "To resolve this problem, please define a 'jdtClassPathLibrary' element inside your ant build file:\n\n"
      + "<ant4eclipse:jdtClassPathLibrary name=\"%1$s\">\n" + "  <fileset dir=\"...\"/>\n"
      + "</ant4eclipse:jdtClassPathLibrary >\n" + "\n"
      + "See http://www.ant4eclipse.org/node/54 for further information.")
  public static JdtExceptionCode CP_CONTAINER_NOT_HANDLED = null;

  @NLSMessage("The specified directory '%s' doesn't point to a valid java runtime environment.")
  public static JdtExceptionCode INVALID_JRE_DIRECTORY;

  @NLSMessage("Project '%s' must have a java project role.")
  public static JdtExceptionCode NO_JAVA_PROJECT_ROLE;

  @NLSMessage("Default java runtime could not be resolved!")
  public static JdtExceptionCode NO_DEFAULT_JAVA_RUNTIME_EXCEPTION;

  @NLSMessage("Exception while executing java launcher ('%s')")
  public static JdtExceptionCode JAVA_LAUNCHER_EXECUTION_EXCEPTION;

  /** BUILD_ORDER_EXCEPTION */
  @NLSMessage("The specified directory '%s' doesn't point to a valid java runtime environment.")
  public static JdtExceptionCode BUILD_ORDER_EXCEPTION;

  /** REFERENCE_TO_UNKNOWN_PROJECT */
  @NLSMessage("Project '%s'  references the unknown project '%s'")
  public static JdtExceptionCode REFERENCE_TO_UNKNOWN_PROJECT_EXCEPTION;

  @NLSMessage("Set of projects contains cyclic dependencies!")
  public static JdtExceptionCode CYCLIC_DEPENDENCIES_EXCEPTION;

  @NLSMessage("Project '%s'  references the unknown project '%s'")
  public static JdtExceptionCode REFERENCE_TO_UNKNOWN_BUNDLE_EXCEPTION;

  @NLSMessage("The class path of project '%s' contains a class path variable '%s' that is not bound.")
  public static JdtExceptionCode UNBOUND_CLASS_PATH_VARIABLE;

  @NLSMessage("Exception whilst resolving the classpath entry '%s' of project '%s': '%s'")
  public static JdtExceptionCode EXCEPTION_WHILE_RESOLVING_CLASSPATH_ENTRY;

  static {
    NLS.initialize(JdtExceptionCode.class);
  }

  /**
   * <p>
   * Creates a new instance of type JdtExceptionCode.
   * </p>
   * 
   * @param message
   */
  private JdtExceptionCode(String message) {
    super(message);
  }
}
