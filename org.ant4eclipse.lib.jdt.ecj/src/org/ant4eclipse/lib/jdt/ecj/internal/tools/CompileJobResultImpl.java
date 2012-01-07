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

import org.ant4eclipse.lib.jdt.ecj.CompileJobResult;
import org.eclipse.jdt.core.compiler.CategorizedProblem;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class CompileJobResultImpl implements CompileJobResult {

  private boolean              _succeeded;

  private CategorizedProblem[] _categorizedProblems;

  private Map<String,File>     _compiledclassfiles;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean succeeded() {
    return _succeeded;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CategorizedProblem[] getCategorizedProblems() {
    return _categorizedProblems == null ? new CategorizedProblem[0] : _categorizedProblems;
  }

  public void setSucceeded( boolean succeeded ) {
    _succeeded = succeeded;
  }

  public void setCategorizedProblems( CategorizedProblem[] categorizedProblems ) {
    _categorizedProblems = categorizedProblems;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String,File> getCompiledClassFiles() {
    if( _compiledclassfiles == null ) {
      return Collections.EMPTY_MAP;
    } else {
      return _compiledclassfiles;
    }
  }

  /**
   * Changes the map which contains the compiled class files.
   * 
   * @param compiledclasses
   *          A map for the class files. Maybe <code>null</code>.
   */
  public void setCompiledClassFiles( Map<String,File> compiledclasses ) {
    _compiledclassfiles = compiledclasses;
  }

} /* ENDCLASS */
