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
package org.ant4eclipse.jdt.tools.internal.ejc;

import org.ant4eclipse.jdt.tools.ejc.CompileJobResult;
import org.eclipse.jdt.core.compiler.CategorizedProblem;

public class CompileJobResultImpl implements CompileJobResult {

  private boolean              _succeeded;

  private CategorizedProblem[] _categorizedProblems;

  public boolean succeeded() {
    return this._succeeded;
  }

  /**
   * <p>
   * Dumps all given problems to the console.
   * </p>
   */
  public void dumpProblems() {
    if ((this._categorizedProblems != null) && (this._categorizedProblems.length > 0)) {
      for (final CategorizedProblem problem : this._categorizedProblems) {
        final StringBuffer line = new StringBuffer();
        line.append("[");
        if (problem.isWarning()) {
          line.append("WARN");
        } else if (problem.isError()) {
          line.append("ERROR");
        } else {
          line.append("???");
        }
        line.append("] ");
        line.append(problem.getOriginatingFileName()).append("#").append(problem.getSourceLineNumber()).append(": ")
            .append(problem.getMessage());
        System.out.println(line.toString());
      }
    }
  }

  void setSucceeded(final boolean succeeded) {
    this._succeeded = succeeded;
  }

  protected void setCategorizedProblems(final CategorizedProblem[] categorizedProblems) {
    this._categorizedProblems = categorizedProblems;
  }
}
