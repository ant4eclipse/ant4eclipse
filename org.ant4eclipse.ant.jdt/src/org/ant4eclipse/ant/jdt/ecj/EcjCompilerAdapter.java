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

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.PerformanceLogging;
import org.ant4eclipse.lib.jdt.ecj.CompileJobDescription;
import org.ant4eclipse.lib.jdt.ecj.CompileJobResult;
import org.ant4eclipse.lib.jdt.ecj.EcjAdapter;

/**
 * <p>
 * Implements a javac compiler adapter for the eclipse compiler for java (ecj). The ant4eclipse javac compiler
 * implements several enhancements The usage of the ecj has several advantages, e.g. support of access restrictions,
 * multiple source folders.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class EcjCompilerAdapter extends A4ECompilerAdapter {

  /**
   * {@inheritDoc}
   */
  @Override
  protected CompileJobResult compile(CompileJobDescription description) {

    PerformanceLogging.start(EcjCompilerAdapter.class, "compile");

    CompileJobResult result = null;

    try {
      result = EcjAdapter.Factory.create().compile(description);
    } finally {
      long duration = PerformanceLogging.stop(EcjCompilerAdapter.class, "compile");
      if (duration > 0) {
        int compiledClasses = (result == null ? -1 : result.getCompiledClassFiles().size());
        A4ELogging.info("ECJ Compilation took %d ms for %d class files (avg: %f ms/class)", //
            duration, compiledClasses, (double) duration / compiledClasses);
      }
    }

    return result;
  }

} /* ENDCALSS */
