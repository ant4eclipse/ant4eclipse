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
package org.ant4eclipse.ant.jdt.ecj;


import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.ecj.EcjExceptionCodes;
import org.apache.tools.ant.taskdefs.Javac;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.util.Util;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * The {@link CompilerOptionsProvider} is a utility class that computes compiler options based on ant's javac task as
 * well as an (optional) project specific and an (optional) global compiler options file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CompilerOptionsProvider {

  /**
   * <p>
   * Creates the compiler options for the JDT compiler.
   * </p>
   * <p>
   * The compiler options are defined here:
   * <ul>
   * <li><a href="http://help.eclipse.org/galileo/topic/org.eclipse.jdt.doc.isv/guide/jdt_api_options.htm">JDT Core
   * options</a></li>
   * <li><a href=
   * "http://help.eclipse.org/galileo/topic/org.eclipse.jdt.doc.user/reference/preferences/java/ref-preferences-compiler.htm"
   * >Java Compiler Preferences </a></li>
   * <li><ahref="http://help.eclipse.org/galileo/topic/org.eclipse.jdt.doc.user/reference/preferences/java/compiler/ref-preferences-errors-warnings.htm"
   * >Java Compiler Errors/Warnings Preferences</a></li>
   * </ul>
   * </p>
   * 
   * @param javac
   *          the javac task
   * @param projectCompilerOptionsFile
   *          the project specific compiler options file.
   * @param globalCompilerOptionsFile
   *          the global compiler options file.
   * 
   * @return the map with the merged compiler options.
   */
  @SuppressWarnings("unchecked")
  public static StringMap getCompilerOptions(Javac javac, String projectCompilerOptionsFile,
      String globalCompilerOptionsFile) {
    Assure.notNull(javac);

    // get the project options
    StringMap projectOptions = getFileCompilerOptions(projectCompilerOptionsFile);
    if (A4ELogging.isTraceingEnabled()) {
      A4ELogging.trace("projectOptions: '%s'.", projectOptions);
    }
    // get the default options
    StringMap defaultOptions = getFileCompilerOptions(globalCompilerOptionsFile);
    if (A4ELogging.isTraceingEnabled()) {
      A4ELogging.trace("defaultOptions: '%s'.", defaultOptions);
    }
    // get the javac options
    StringMap javacOptions = getJavacCompilerOptions(javac);
    if (A4ELogging.isTraceingEnabled()) {
      A4ELogging.trace("javacOptions: '%s'.", javacOptions);
    }
    // merge the map
    StringMap mergedMap = mergeCompilerOptions(projectOptions, defaultOptions, javacOptions);
    if (A4ELogging.isTraceingEnabled()) {
      A4ELogging.trace("mergedMap: '%s'.", mergedMap);
    }
    // create result
    CompilerOptions compilerOptions = new CompilerOptions(mergedMap);

    // verbose option
    compilerOptions.verbose = javac.getVerbose();

    // debug the compiler options
    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("Using the following compile options:\n %s", compilerOptions.toString());
    }

    // return the compiler options
    StringMap result = new StringMap();
    result.putAll(compilerOptions.getMap());
    return result;
  }

  /**
   * <p>
   * Returns the compiler options specified in the javac task.
   * </p>
   * 
   * @param javac
   *          the javac task
   * @return the compiler options specified in the javac task.
   */
  @SuppressWarnings("unchecked")
  private static StringMap getJavacCompilerOptions(Javac javac) {

    StringMap result = new StringMap();

    /*
     * set the source option
     */
    if (Utilities.hasText(javac.getSource())) {

      // get the source
      String source = javac.getSource();
      // set the source
      if (source.equals("1.3")) {
        result.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_3);
      } else if (source.equals("1.4")) {
        result.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_4);
      } else if (source.equals("1.5") || source.equals("5") || source.equals("5.0")) {
        result.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
      } else if (source.equals("1.6") || source.equals("6") || source.equals("6.0")) {
        result.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);
      } else if (source.equals("1.7") || source.equals("7") || source.equals("7.0")) {
        result.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_7);
      } else {
        throw new Ant4EclipseException(EcjExceptionCodes.UNKNOWN_JAVA_SOURCE_OPTION_EXCEPTION, source);
      }
    }

    /*
     * set the target option
     */
    if (Utilities.hasText(javac.getTarget())) {

      // get the target
      String target = javac.getSource();

      // set the target
      if (target.equals("1.3")) {
        result.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_3);
        result.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_3);
      } else if (target.equals("1.4")) {
        result.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_4);
        result.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_4);
      } else if (target.equals("1.5") || target.equals("5") || target.equals("5.0")) {
        result.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
        result.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
      } else if (target.equals("1.6") || target.equals("6") || target.equals("6.0")) {
        result.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);
        result.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);
      } else if (target.equals("1.7") || target.equals("7") || target.equals("7.0")) {
        result.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_7);
        result.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_7);
      } else {
        throw new Ant4EclipseException(EcjExceptionCodes.UNKNOWN_JAVA_TARGET_OPTION_EXCEPTION, target);
      }
    }

    /*
     * set the debug options
     */
    if (javac.getDebug()) {

      String debugLevel = javac.getDebugLevel();

      if (debugLevel != null) {
        result.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.DO_NOT_GENERATE);
        result.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.DO_NOT_GENERATE);
        result.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.DO_NOT_GENERATE);
        if (debugLevel.length() != 0) {
          if (debugLevel.indexOf("vars") != -1) {
            result.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
          }
          if (debugLevel.indexOf("lines") != -1) {
            result.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
          }
          if (debugLevel.indexOf("source") != -1) {
            result.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
          }
        }
      } else {
        result.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
        result.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
        result.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
      }
    } else {
      result.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.DO_NOT_GENERATE);
      result.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.DO_NOT_GENERATE);
      result.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.DO_NOT_GENERATE);
    }

    /*
     * Handle the nowarn option. If none, then we generate all warnings.
     */
    if (javac.getNowarn()) {
      // disable all warnings
      Map.Entry<String, String>[] entries = result.entrySet().toArray(new Map.Entry[result.size()]);
      for (Entry<String, String> entrie : entries) {
        Map.Entry<String, String> entry = entrie;
        if (entry.getValue().equals(CompilerOptions.WARNING)) {
          result.put(entry.getKey(), CompilerOptions.IGNORE);
        }
      }
      result.put(CompilerOptions.OPTION_TaskTags, Util.EMPTY_STRING);
      if (javac.getDeprecation()) {
        result.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.WARNING);
        result.put(CompilerOptions.OPTION_ReportDeprecationInDeprecatedCode, CompilerOptions.ENABLED);
        result.put(CompilerOptions.OPTION_ReportDeprecationWhenOverridingDeprecatedMethod, CompilerOptions.ENABLED);
      }
    } else if (javac.getDeprecation()) {
      result.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.WARNING);
      result.put(CompilerOptions.OPTION_ReportDeprecationInDeprecatedCode, CompilerOptions.ENABLED);
      result.put(CompilerOptions.OPTION_ReportDeprecationWhenOverridingDeprecatedMethod, CompilerOptions.ENABLED);
    } else {
      result.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
      result.put(CompilerOptions.OPTION_ReportDeprecationInDeprecatedCode, CompilerOptions.DISABLED);
      result.put(CompilerOptions.OPTION_ReportDeprecationWhenOverridingDeprecatedMethod, CompilerOptions.DISABLED);
    }

    /*
     * set the encoding option
     */
    if (javac.getEncoding() != null) {
      result.put(CompilerOptions.OPTION_Encoding, javac.getEncoding());
    }

    // return result
    return result;
  }

  /**
   * <p>
   * Returns the compiler options for the given compiler options file.
   * </p>
   * 
   * @param fileName
   *          the compiler options file.
   * @return the map with the compiler options.
   */
  private static StringMap getFileCompilerOptions(String fileName) {
    if (Utilities.hasText(fileName)) {
      try {
        File compilerOptionsFile = new File(fileName);
        if (compilerOptionsFile.exists() && compilerOptionsFile.isFile()) {
          StringMap compilerOptionsMap = new StringMap(compilerOptionsFile);
          return compilerOptionsMap;
        }
      } catch (Exception e) {
        A4ELogging.warn("Could not read compiler options file '%s'.\nReason: '%s'", fileName, e.getMessage());
        return null;
      }
    }
    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param options_1
   * @param options_2
   * @param options_3
   * @return
   */
  private static StringMap mergeCompilerOptions(StringMap options_1, StringMap options_2, StringMap options_3) {

    StringMap result = new StringMap();
    if (options_3 != null) {
      result.putAll(options_3);
    }
    if (options_2 != null) {
      result.putAll(options_2);
    }
    if (options_1 != null) {
      result.putAll(options_1);
    }
    return result;
  }
}
