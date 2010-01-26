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
package org.ant4eclipse.lib.jdt.ecj.internal.tools;



import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.jdt.ecj.CompileJobDescription;
import org.ant4eclipse.lib.jdt.ecj.CompileJobResult;
import org.ant4eclipse.lib.jdt.ecj.EcjAdapter;
import org.ant4eclipse.lib.jdt.ecj.SourceFile;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The {@link EcjAdapterImpl} can be used to compile eclipse projects with the eclipse java compiler (ejc). It provides
 * support for
 * <ul>
 * <li>setting the compiler options as specified in the eclipse project or in the global settings</li>
 * <li>setting the java runtime environment as specified in the eclipse project</li>
 * </ul>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class EcjAdapterImpl implements EcjAdapter {

  /**
   * {@inheritDoc}
   */
  public CompileJobResult compile(CompileJobDescription description) {
    Assert.notNull(description);

    // create the name environment
    INameEnvironment nameEnvironment = new NameEnvironmentImpl(description.getClassFileLoader());

    // get the compiler options
    Map<String, String> compilerOptions = description.getCompilerOptions();

    // retrieve the compilation units
    ICompilationUnit[] sources = getCompilationUnits(description.getSourceFiles());

    // create the error handling policy
    IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();

    // create the problem factory
    IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());

    // create the compiler requestor
    CompilerRequestorImpl requestor = new CompilerRequestorImpl();

    // in any case disallow forbidden references
    compilerOptions.put("org.eclipse.jdt.core.compiler.problem.forbiddenReference", "error");

    // create the compiler
    Compiler compiler = new Compiler(nameEnvironment, policy, new CompilerOptions(compilerOptions), requestor,
        problemFactory);

    // compile
    compiler.compile(sources);

    // create the compile job result
    CompileJobResultImpl result = new CompileJobResultImpl();
    result.setSucceeded(requestor.isCompilationSuccessful());
    result.setCategorizedProblems(requestor.getCategorizedProblems());

    // return the result
    return result;
  }

  /**
   * <p>
   * Returns the compilation units for the given source files.
   * </p>
   * 
   * @param sourceFiles
   *          the source files
   * @return the compilation units for the given source files.
   */
  private ICompilationUnit[] getCompilationUnits(SourceFile[] sourceFiles) {

    // create result list
    List<ICompilationUnit> result = new LinkedList<ICompilationUnit>();

    // iterate over source folders
    for (SourceFile sourceFile : sourceFiles) {

      CompilationUnitImpl compilationUnitImpl = new CompilationUnitImpl(sourceFile);

      if (!result.contains(compilationUnitImpl)) {
        result.add(compilationUnitImpl);
      }
    }

    // return the result
    return result.toArray(new ICompilationUnit[result.size()]);
  }
}