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

import org.ant4eclipse.core.ant.ExtendedBuildException;

import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.pydt.internal.tools.PydtSourcepathResolver;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedSourceEntry;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * Basic task used to access the source path of a python project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetPydtSourcePathTask extends AbstractPydtGetProjectPathTask {

  private static final String MSG_MULTIPLEFOLDERS   = "The Project '%s' contains multiple source folders ! If you want to allow this,"
                                                        + " you have to set allowMultipleFolders='true'!";

  private boolean             _allowMultipleFolders = false;

  /**
   * Specifies if multiple folders are supported or not.
   * 
   * @param allowMultipleFolders
   *          <code>true</code> <=> Multiple folders are supported, otherwise they're not.
   */
  public void setAllowMultipleFolders(final boolean allowMultipleFolders) {
    _allowMultipleFolders = allowMultipleFolders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (!_allowMultipleFolders) {
      final PythonProjectRole role = (PythonProjectRole) getEclipseProject().getRole(PythonProjectRole.class);
      final RawPathEntry[] entries = role.getRawPathEntries(ReferenceKind.Source);
      if (entries.length > 1) {
        throw new ExtendedBuildException(MSG_MULTIPLEFOLDERS, getEclipseProject().getSpecifiedName());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  protected File[] resolvePath() {
    final PythonProjectRole role = (PythonProjectRole) getEclipseProject().getRole(PythonProjectRole.class);
    final PydtSourcepathResolver resolver = new PydtSourcepathResolver();
    final RawPathEntry[] entries = role.getRawPathEntries(ReferenceKind.Source);
    final ResolvedSourceEntry[] resolved = resolver.resolve(entries);
    final File[] result = new File[resolved.length];
    for (int i = 0; i < resolved.length; i++) {
      result[i] = getEclipseProject().getChild(resolved[i].getFolder(), getPathStyle());
    }
    return result;
  }

} /* ENDCLASS */
