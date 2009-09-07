package org.ant4eclipse.jdt.ecj;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Utilities;
import org.apache.tools.ant.taskdefs.Javac;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 *
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
   * @return
   */
  @SuppressWarnings("unchecked")
  public static Map getCompilerOptions(Javac javac, String projectCompilerOptionsFile, String defaultCompilerOptionsFile) {
    Assert.notNull(javac);

    Map<String, String> projectOptions = getFileCompilerOptions(projectCompilerOptionsFile);
    Map<String, String> defaultOptions = getFileCompilerOptions(defaultCompilerOptionsFile);
    Map<String, String> javacOptions = getJavacCompilerOptions(javac);

    Map<String, String> mergedMap = mergeCompilerOptions(projectOptions, javacOptions, defaultOptions);

    // Step 1: create result
    CompilerOptions compilerOptions = new CompilerOptions(mergedMap);

    // 

    // // create default
    // if (compilerOptions == null) {
    //
    // // create compiler options
    // compilerOptions = new CompilerOptions();
    //
    // // debug
    // if (javac.getDebug()) {
    // compilerOptions.produceDebugAttributes = ClassFileConstants.ATTR_SOURCE | ClassFileConstants.ATTR_LINES
    // | ClassFileConstants.ATTR_VARS;
    // } else {
    // compilerOptions.produceDebugAttributes = 0x0;
    // }
    // // TODO
    // // see: http://help.eclipse.org/galileo/topic/org.eclipse.jdt.doc.isv/guide/jdt_api_options.htm#compatibility
    //
    // // get the source option
    // compilerOptions.sourceLevel = CompilerOptions.versionToJdkLevel(javac.getSource());
    //
    // // get the target option
    // long targetLevel = CompilerOptions.versionToJdkLevel(javac.getTarget());
    // compilerOptions.complianceLevel = targetLevel;
    // compilerOptions.targetJDK = targetLevel;
    // }

    // TODO:
    // A4ELogging.info("Using the following compile options:\n %s", compilerOptions.toString());

    // return the compiler options
    return compilerOptions.getMap();
  }

  /**
   * @param javac
   * @return
   */
  private static Map<String, String> getJavacCompilerOptions(Javac javac) {

    Map<String, String> result = new HashMap<String, String>();

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
      Object[] entries = result.entrySet().toArray();
      for (int i = 0, max = entries.length; i < max; i++) {
        Map.Entry entry = (Map.Entry) entries[i];
        if (!(entry.getKey() instanceof String))
          continue;
        if (!(entry.getValue() instanceof String))
          continue;
        if (((String) entry.getValue()).equals(CompilerOptions.WARNING)) {
          result.put((String) entry.getKey(), CompilerOptions.IGNORE);
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

    // /*
    // * verbose option
    // */
    // if (this.verbose) {
    // cmd.createArgument().setValue("-verbose");
    // }

    // /*
    // * failnoerror option
    // */
    // if (!this.attributes.getFailonerror()) {
    // cmd.createArgument().setValue("-proceedOnError");
    // }

    // /*
    // * target option.
    // */
    // if (this.target != null) {
    // result.put(CompilerOptions.OPTION_TargetPlatform, this.target);
    // }

    // /*
    // * source option
    // */
    // String source = this.attributes.getSource();
    // if (source != null) {
    // result.put(CompilerOptions.OPTION_Source, source);
    // }

    // /*
    // * encoding option
    // */
    // if (javac.getEncoding() != null) {
    // cmd.createArgument().setValue("-encoding");
    // cmd.createArgument().setValue(this.encoding);
    // }

    return result;
  }

  /**
   * @param fileName
   * @return
   */
  private static Map<String, String> getFileCompilerOptions(String fileName) {
    if (fileName != null) {
      File compilerOptionsFile = new File(fileName);
      if (compilerOptionsFile.exists() && compilerOptionsFile.isFile()) {
        Map<String, String> compilerOptionsMap = Utilities.readProperties(compilerOptionsFile);
        return compilerOptionsMap;
      }
    }
    return null;
  }

  /**
   * @param options_1
   * @param options_2
   * @param options_3
   * @return
   */
  private static Map<String, String> mergeCompilerOptions(Map<String, String> options_1, Map<String, String> options_2,
      Map<String, String> options_3) {

    Map<String, String> result = new HashMap<String, String>();
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

  /**
   * <p>
   * </p>
   * 
   * @param args
   */
  public static void main(String[] args) {

    //
    Map<String, String> map1 = new HashMap<String, String>();
    map1.put("a", "1");
    map1.put("b", "1");
    map1.put("c", "1");

    //
    Map<String, String> map2 = new HashMap<String, String>();
    map2.put("b", "2");
    map2.put("d", "2");

    //
    Map<String, String> map3 = new HashMap<String, String>();
    map3.put("b", "3");
    map3.put("c", "3");
    map3.put("d", "3");
    map3.put("e", "3");

    // 
    Map<String, String> map = mergeCompilerOptions(map1, map2, map3);

    System.err.println(map);
  }
}
