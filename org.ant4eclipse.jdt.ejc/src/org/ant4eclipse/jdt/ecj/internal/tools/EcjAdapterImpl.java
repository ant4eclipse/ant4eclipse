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
package org.ant4eclipse.jdt.ecj.internal.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.ecj.ClassFileLoader;
import org.ant4eclipse.jdt.ecj.CompileJobDescription;
import org.ant4eclipse.jdt.ecj.CompileJobResult;
import org.ant4eclipse.jdt.ecj.EcjAdapter;
import org.ant4eclipse.jdt.ecj.CompileJobDescription.SourceFile;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

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

  /*
   * (non-Javadoc)
   * 
   * @see net.sf.ant4eclipse.tools.osgi.ejc.EjcAdapter#compile(net.sf.ant4eclipse.tools.osgi.ejc.CompileJobDescription)
   */
  public CompileJobResult compile(final CompileJobDescription description) {
    Assert.notNull(description);

    // final File[] sourceFolder = description.getSourceFolders();

    // A4ELogging.debug("source folder: " + Arrays.asList(sourceFolder));

    // final File outputFolder = description.getOutputFolder();
    //
    // A4ELogging.debug("output folder: " + outputFolder);

    final ClassFileLoader classFileLoader = description.getClassFileLoader();

    A4ELogging.debug("classFileLoader: " + classFileLoader);

    final Map<String, String> compilerOptions = description.getCompilerOptions();

    A4ELogging.debug("compiler options: " + compilerOptions);

    // retrieve the compilation units
    final ICompilationUnit[] sources = getCompilationUnits(description.getSourceFiles());

    final IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();

    final IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());

    final CompilerRequestorImpl requestor = new CompilerRequestorImpl();

    final INameEnvironment nameEnvironment = new NameEnvironmentImpl(classFileLoader);

    // in any case disallow forbidden references
    compilerOptions.put("org.eclipse.jdt.core.compiler.problem.forbiddenReference", "error");

    final Compiler compiler = new Compiler(nameEnvironment, policy, new CompilerOptions(compilerOptions), requestor,
        problemFactory);

    try {
      compiler.compile(sources);
    } catch (final Exception e) {
      e.printStackTrace();
    }

    final CompileJobResultImpl result = new CompileJobResultImpl();
    result.setSucceeded(requestor.isCompilationSuccessful());
    result.setCategorizedProblems(requestor.getCategorizedProblems());
    return result;
  }

  /**
   * @param sourceFolders
   * @return
   */
  private ICompilationUnit[] getCompilationUnits(final SourceFile[] sourceFiles) {

    // create result list
    final List<ICompilationUnit> result = new LinkedList<ICompilationUnit>();

    // iterate over source folders
    for (final SourceFile sourceFile : sourceFiles) {

      final CompilationUnitImpl compilationUnitImpl = new CompilationUnitImpl(sourceFile);

      if (!result.contains(compilationUnitImpl)) {
        result.add(compilationUnitImpl);
      }
    }

    // return the result
    return result.toArray(new ICompilationUnit[result.size()]);
  }
}