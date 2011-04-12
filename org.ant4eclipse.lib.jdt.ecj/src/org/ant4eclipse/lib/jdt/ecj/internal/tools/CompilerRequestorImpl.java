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

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.ecj.ReferableSourceFile;
import org.ant4eclipse.lib.jdt.ecj.SourceFile;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Implements the call-back interface {@link ICompilerRequestor} for receiving compilation results. The
 * {@link CompilerRequestorImpl} writes the compiled class files to disc or reports the errors in case the compilation
 * was not successful.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class CompilerRequestorImpl implements ICompilerRequestor {

  /** indicates whether the compilation was successful or not */
  protected boolean                  _compilationSuccessful;

  /** the list of categorized problems */
  protected List<CategorizedProblem> _categorizedProblems;

  /** collection of class files which have been compiled */
  private Map<String, File>          _compiledClassFiles;

  /**
   * <p>
   * Creates a new instance of type {@link CompilerRequestorImpl}.
   * </p>
   */
  public CompilerRequestorImpl() {
    this._compilationSuccessful = true;
    this._categorizedProblems = new LinkedList<CategorizedProblem>();
    this._compiledClassFiles = new Hashtable<String, File>();
  }

  /**
   * Returns a map for the compiled class files.
   * 
   * @return A map for the compiled class files. Not <code>null</code>.
   */
  public Map<String, File> getCompiledClassFiles() {
    return Collections.unmodifiableMap(this._compiledClassFiles);
  }

  /**
   * {@inheritDoc}
   */
  public void acceptResult(CompilationResult result) {

    // get the compilation unit...
    CompilationUnitImpl compilationUnitImpl = (CompilationUnitImpl) result.getCompilationUnit();

    // ...and the source file
    SourceFile sourceFile = compilationUnitImpl.getSourceFile();

    // return immediately if the source file is a ReferableSourceFile
    if (sourceFile instanceof ReferableSourceFile) {

      // add the problems...
      if (result.getAllProblems() != null) {
        if (A4ELogging.isTraceingEnabled()) {
          A4ELogging.trace("Could not compile referenced class '%s'. Reason: %s", sourceFile.getSourceFileName(),
              Arrays.asList(result.getAllProblems()));
        }
      }

      return;
    }

    // get the destination directory
    File destinationDirectory = sourceFile.getDestinationFolder();

    if (!result.hasErrors()) {
      ClassFile[] classFiles = result.getClassFiles();
      for (ClassFile classFile2 : classFiles) {
        char[][] compoundName = classFile2.getCompoundName();
        StringBuffer classFileName = new StringBuffer();
        for (int j = 0; j < compoundName.length; j++) {
          classFileName.append(compoundName[j]);
          if (j < compoundName.length - 1) {
            classFileName.append('/');
          }
        }
        classFileName.append(".class");
        File classFile = new File(destinationDirectory, classFileName.toString());
        File classDir = classFile.getParentFile();
        if (!classDir.exists()) {
          classDir.mkdirs();
        }
        try {
          A4ELogging.debug("writing class file: '%s'", classFile);
          Utilities.writeFile(classFile, classFile2.getBytes());
          this._compiledClassFiles.put(classFileName.toString(), classFile);
        } catch (Ant4EclipseException ioe) {
          A4ELogging.error("Could not write classfile '%s': %s", classFileName.toString(), ioe.toString());
          ioe.printStackTrace();
          this._compilationSuccessful = false;
        }
      }
    } else {
      this._compilationSuccessful = false;
    }

    // add the problems...
    if (result.getAllProblems() != null) {
      this._categorizedProblems.addAll(Arrays.asList(result.getAllProblems()));
    }
  }

  /**
   * <p>
   * Returns <code>true</code> if the compilation was successful, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if the compilation was successful, <code>false</code> otherwise.
   */
  public boolean isCompilationSuccessful() {
    return this._compilationSuccessful;
  }

  /**
   * <p>
   * Returns the categorized problems.
   * </p>
   * 
   * @return the categorized problems.
   */
  public CategorizedProblem[] getCategorizedProblems() {
    return this._categorizedProblems.toArray(new CategorizedProblem[0]);
  }
}
