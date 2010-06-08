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

import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.ecj.CompileJobDescription;
import org.ant4eclipse.lib.jdt.ecj.CompileJobResult;
import org.ant4eclipse.lib.jdt.ecj.SourceFile;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.CompileJobResultImpl;
import org.apache.tools.ant.types.Path;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>
 * Implements a javac compiler adapter for sun's javac commandline. This adapter is capable to understand eclipse
 * project settings.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class JavacCompilerAdapter extends A4ECompilerAdapter {

  private static final String   SUFFIX_JAVA    = ".java";

  private static final String   PATH_SEPARATOR = System.getProperty("path.separator");

  private Object                _javac;

  private Method                _compile;

  private StringBuffer          _buffer;

  private IProblemFactory       _problemfactory;

  private PrintStream           _stdout;

  private PrintStream           _stderr;

  private ByteArrayOutputStream _byteout;

  /**
   * Initialises this compiler adapter instance.
   */
  public JavacCompilerAdapter() {

    this._buffer = new StringBuffer();
    this._byteout = new ByteArrayOutputStream();

    // use reflection to access the compiler
    try {
      this._javac = Utilities.newInstance("com.sun.tools.javac.Main");
      this._compile = this._javac.getClass().getMethod("compile", new Class[] { String[].class });
    } catch (SecurityException ex) {
      failedCompile(ex);
    } catch (NoSuchMethodException ex) {
      failedCompile(ex);
    }

    // create the problem factory
    this._problemfactory = new DefaultProblemFactory(Locale.getDefault());
  }

  /**
   * Provides a classpath from the supplied compile job description.
   * 
   * @param description
   *          The description providing all necessary information. Not <code>null</code>.
   * 
   * @return The classpath which has been setup for the compile job. Not <code>null</code>.
   */
  private String getClasspath(CompileJobDescription description) {
    this._buffer.setLength(0);
    File[] classpath = description.getClassFileLoader().getClasspath();
    if (classpath.length > 0) {
      this._buffer.append(classpath[0].getAbsolutePath());
      for (int i = 1; i < classpath.length; i++) {
        this._buffer.append(PATH_SEPARATOR);
        this._buffer.append(classpath[i].getAbsolutePath());
      }
    }
    return this._buffer.toString();
  }

  /**
   * Concatenates the supplied list of path entries.
   * 
   * @param list
   *          The list of path entries. Not <code>null</code>.
   * 
   * @return The concatenated path list. Not <code>null</code>.
   */
  private String getConcatenatedPath(String[] list) {
    this._buffer.setLength(0);
    if (list.length > 0) {
      this._buffer.append(list[0]);
      for (int i = 1; i < list.length; i++) {
        this._buffer.append(PATH_SEPARATOR);
        this._buffer.append(list[i]);
      }
    }
    return this._buffer.toString();
  }

  /**
   * Calculates the debugging options according to the supplied description.
   * 
   * @param description
   *          The descriptional instance providing all information to run a compile job. Not <code>null</code>.
   * 
   * @return The compile options used for the generation of byte code. Not <code>null</code>.
   */
  private String getDebugOptions(CompileJobDescription description) {
    this._buffer.setLength(0);
    if (description.getCompilerOptions().containsKey(CompilerOptions.OPTION_LineNumberAttribute)) {
      this._buffer.append("lines");
    }
    if (description.getCompilerOptions().containsKey(CompilerOptions.OPTION_LocalVariableAttribute)) {
      if (this._buffer.length() > 0) {
        this._buffer.append(",");
      }
      this._buffer.append("vars");
    }
    if (description.getCompilerOptions().containsKey(CompilerOptions.OPTION_SourceFileAttribute)) {
      if (this._buffer.length() > 0) {
        this._buffer.append(",");
      }
      this._buffer.append("source");
    }
    if (this._buffer.length() == 0) {
      // nothing has been specified
      this._buffer.append("none");
    }
    return this._buffer.toString();
  }

  /**
   * Evaluates some arguments to setup output options for the compilation process.
   * 
   * @param description
   *          The descriptional instance providing all information to run a compile job. Not <code>null</code>.
   * 
   * @return The options used to control the compilation process. Not <code>null</code>.
   */
  private String getCompileOptions(CompileJobDescription description) {
    this._buffer.setLength(0);
    if (description.getCompilerOptions().containsKey(CompilerOptions.OPTION_ReportMissingSerialVersion)) {
      this._buffer.append("serial");
    } else {
      this._buffer.append("-serial");
    }
    this._buffer.append(",");
    if (description.getCompilerOptions().containsKey(CompilerOptions.OPTION_ReportFallthroughCase)) {
      this._buffer.append("fallthrough");
    } else {
      this._buffer.append("-fallthrough");
    }
    this._buffer.append(",");
    if (description.getCompilerOptions().containsKey(CompilerOptions.OPTION_ReportUncheckedTypeOperation)) {
      this._buffer.append("unchecked");
    } else {
      this._buffer.append("-unchecked");
    }
    this._buffer.append(",");
    if (description.getCompilerOptions().containsKey(CompilerOptions.OPTION_ReportFinallyBlockNotCompletingNormally)) {
      this._buffer.append("finally");
    } else {
      this._buffer.append("-finally");
    }
    if (this._buffer.length() == 0) {
      // the recommended options
      return "-Xlint";
    } else {
      // the specified options
      return String.format("-Xlint:%s", this._buffer);
    }
  }

  /**
   * Creates a list with commandline arguments shared among all source files.
   * 
   * @param description
   *          The description used for the compilation process. Not <code>null</code>.
   * 
   * @return The list with commandline arguments. Not <code>null</code>.
   */
  private List<String> createCommonArgs(CompileJobDescription description) {

    Map<String, String> options = description.getCompilerOptions();

    List<String> result = new ArrayList<String>();

    result.add(getCompileOptions(description));

    Path bootclasspath = getJavac().getBootclasspath();
    if (bootclasspath != null) {
      result.add("-bootclasspath");
      result.add(getConcatenatedPath(bootclasspath.list()));
    }

    Path extdirs = getJavac().getExtdirs();
    if (extdirs != null) {
      result.add("-extdirs");
      result.add(getConcatenatedPath(extdirs.list()));
    }

    result.add("-classpath");
    result.add(getClasspath(description));

    result.add(String.format("-g:%s", getDebugOptions(description)));

    if (A4ELogging.isDebuggingEnabled()) {
      result.add("-verbose");
    }

    if (options.containsKey(CompilerOptions.OPTION_Source)) {
      result.add("-source");
      result.add(options.get(CompilerOptions.OPTION_Source));
    }

    if (options.containsKey(CompilerOptions.OPTION_Compliance)) {
      result.add("-target");
      result.add(options.get(CompilerOptions.OPTION_Compliance));
    }

    if (options.containsKey(CompilerOptions.OPTION_Encoding)) {
      result.add("-encoding");
      result.add(options.get(CompilerOptions.OPTION_Encoding));
    }

    if (options.containsKey(CompilerOptions.OPTION_ReportDeprecation)
        || options.containsKey(CompilerOptions.OPTION_ReportDeprecationInDeprecatedCode)
        || options.containsKey(CompilerOptions.OPTION_ReportDeprecationWhenOverridingDeprecatedMethod)) {
      result.add("-deprecation");
    }

    return result;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected CompileJobResult compile(CompileJobDescription description) {

    try {

      List<CategorizedProblem> problems = new ArrayList<CategorizedProblem>();
      List<String> preparedargs = createCommonArgs(description);
      List<String> arguments = new ArrayList<String>();
      boolean succeeded = true;
      for (SourceFile sourcefile : description.getSourceFiles()) {

        // setup the commandline arguments for the javac executable
        arguments.clear();
        arguments.addAll(preparedargs);

        arguments.add("-d");
        arguments.add(sourcefile.getDestinationFolder().getAbsolutePath());
        arguments.add(sourcefile.getSourceFile().getAbsolutePath());

        boolean singlesuccess = true;
        String[] arglist = arguments.toArray(new String[arguments.size()]);
        startCapturing();
        try {
          Integer returncode = (Integer) this._compile.invoke(this._javac, new Object[] { arglist });
          if (returncode.intValue() != 0) {
            singlesuccess = false;
          }
        } finally {
          stopCapturing();
        }

        if (!singlesuccess) {
          succeeded = false;
          // we're using the platform encoding as this is the encoding used for the out/err stream
          // from the console
          createProblems(problems, sourcefile.getSourceFileName(), new String(this._byteout.toByteArray()));
        }

      }

      CompileJobResultImpl result = new CompileJobResultImpl();
      result.setSucceeded(succeeded);
      result.setCategorizedProblems(problems.toArray(new CategorizedProblem[problems.size()]));
      return result;

    } catch (IllegalArgumentException ex) {
      throw failedCompile(ex);
    } catch (IllegalAccessException ex) {
      throw failedCompile(ex);
    } catch (InvocationTargetException ex) {
      throw failedCompile(ex);
    }

  }

  /**
   * Creates the problem instances which can be used to report some issues.
   * 
   * @param problems
   *          The receiving list for the problems. Not <code>null</code>.
   * @param sourcefilename
   *          The filename these problems are related to. Not <code>null</code>.
   * @param text
   *          The content of the javac outcome. Not <code>null</code>.
   */
  private void createProblems(List<CategorizedProblem> problems, String sourcefilename, String text) {
    List<String> lines = Utilities.splitText(text);
    Collections.reverse(lines);
    for (int i = 0; i < lines.size() - 2; i++) {
      int col = lines.get(i).indexOf('^');
      if (col != -1) {
        // we've got a marker for the issue which usually looks like in the following example
        //
        // mysource.java:12 : there's an error
        // public class mysource- {
        // ^
        //
        String line = lines.get(i + 2);
        int suffix = line.indexOf(SUFFIX_JAVA);
        if (suffix != -1) {
          line = line.substring(suffix + SUFFIX_JAVA.length() + 1); // +1 to skip a ':' before the line number
          int colon = line.indexOf(':');
          if (colon != -1) {
            int lineno = Integer.parseInt(line.substring(0, colon));
            String error = line.substring(colon + 1).trim();
            problems.add(this._problemfactory.createProblem(sourcefilename.toCharArray(), IProblem.Unclassified,
                new String[] { error }, new String[] { error }, ProblemSeverities.Error, -1, -1, lineno, problems
                    .size()));
          }
        }
      }
    }
  }

  /**
   * Enables the capturing so we can analyze the outcome of the java compiler.
   */
  private void startCapturing() {
    this._stdout = System.out;
    this._stderr = System.err;
    this._byteout.reset();
    System.setErr(new PrintStream(this._byteout));
    System.setOut(new PrintStream(this._byteout));
  }

  /**
   * Disables the capturing so the collected outcome can be processed.
   */
  private void stopCapturing() {
    System.setOut(this._stdout);
    System.setErr(this._stderr);
  }

  private Ant4EclipseException failedCompile(Exception ex) {
    return new Ant4EclipseException(CoreExceptionCode.COULD_NOT_ACCESS_METHOD, "compile", "com.sun.tools.javac.Main");
  }

} /* ENDCALSS */
