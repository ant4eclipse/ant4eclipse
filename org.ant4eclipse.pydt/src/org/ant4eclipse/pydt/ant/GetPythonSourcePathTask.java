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

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.pydt.PydtExceptionCode;
import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.pydt.internal.tools.PathExpander;
import org.ant4eclipse.pydt.internal.tools.PythonResolver;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * Basic task used to access the source path of a python project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetPythonSourcePathTask extends AbstractPydtGetProjectPathTask {

  private boolean _allowMultipleFolders = false;

  /**
   * Specifies if multiple folders are supported or not.
   * 
   * @param allowMultipleFolders
   *          <code>true</code> <=> Multiple folders are supported, otherwise they're not.
   */
  public void setAllowMultipleFolders(boolean allowMultipleFolders) {
    this._allowMultipleFolders = allowMultipleFolders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (!this._allowMultipleFolders) {
      PythonProjectRole role = getEclipseProject().getRole(PythonProjectRole.class);
      RawPathEntry[] entries = role.getRawPathEntries(ReferenceKind.Source);
      if (entries.length > 1) {
        throw new Ant4EclipseException(PydtExceptionCode.MULTIPLEFOLDERS, getEclipseProject().getSpecifiedName());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {
    PythonProjectRole role = getEclipseProject().getRole(PythonProjectRole.class);
    PythonResolver resolver = new PythonResolver(getWorkspace(), PythonResolver.Mode.all, true);
    PathExpander expander = new PathExpander(getEclipseProject());
    RawPathEntry[] entries = role.getRawPathEntries(ReferenceKind.Source);
    ResolvedPathEntry[] resolved = resolver.resolve(entries);
    File[] result = expander.expand(resolved, getPathStyle());
    return result;
  }

} /* ENDCLASS */
