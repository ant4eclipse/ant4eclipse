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
package org.ant4eclipse.pydt.ant;

import org.ant4eclipse.pydt.internal.tools.PathExpander;
import org.ant4eclipse.pydt.internal.tools.PythonResolver;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;

import java.io.File;

/**
 * Basic task used to access the source path of a python project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetPythonPathTask extends AbstractPydtGetProjectPathTask {

  private boolean _ignoreruntime = false;

  /**
   * Enables/disables the support for the runtime.
   * 
   * @param ignoreruntime
   *          <code>true</code> <=> The python runtime shall be ignored.
   */
  public void setIgnoreruntime(boolean ignoreruntime) {
    this._ignoreruntime = ignoreruntime;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {
    PythonResolver resolver = new PythonResolver(getWorkspace(), PythonResolver.Mode.all, this._ignoreruntime);
    PathExpander expander = new PathExpander(getEclipseProject());
    ResolvedPathEntry[] resolved = resolver.resolve(getEclipseProject().getSpecifiedName());
    File[] result = expander.expand(resolved, getPathStyle());
    return result;
  }

} /* ENDCLASS */
