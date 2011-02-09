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
import org.ant4eclipse.lib.jdt.ecj.CompileJobDescription;
import org.ant4eclipse.lib.jdt.ecj.CompileJobResult;

/**
 * Just used as a dummy, so users with old scripts to have the chance to recognize their outdated.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
@Deprecated
public class JDTCompilerAdapter extends JavacCompilerAdapter {

  private static final String MSG_DEPRECATED_TYPE = "The compiler adapter '%s' is deprecated. Please us '%s' instead.";

  /**
   * {@inheritDoc}
   */
  @Override
  protected CompileJobResult compile(CompileJobDescription description) {
    A4ELogging.warn(MSG_DEPRECATED_TYPE, getClass().getName(), JavacCompilerAdapter.class.getName());
    return super.compile(description);
  }

} /* ENDCLASS */
