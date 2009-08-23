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
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.pydt.internal.tools.PythonResolver;
import org.ant4eclipse.pydt.internal.tools.PythonUtilities;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedOutputEntry;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * Basic task used to access the source path of a python project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetPythonOutputPathTask extends AbstractPydtGetProjectPathTask {

  private static final String MSG_MULTIPLEFOLDERS   = "The Project '%s' contains multiple source folders ! If you want to allow this,"
                                                        + " you have to set allowMultipleFolders='true'!";

  private static final String MSG_PYDLTK            = "The python DLTK framework doesn't provide information for output folders."
                                                        + "Therefore the source folders are used for the output pathes as well.";

  private static final String MSG_PYDEV             = "The PyDev framework uses source folders as output folders. They can't be "
                                                        + "set explicitly.";

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
    if (PythonUtilities.isPyDLTKProject(getEclipseProject())) {
      // should be warning because the dltk doesn't support output folders
      A4ELogging.warn(MSG_PYDLTK);
    } else {
      // only debug because PyDev uses the source folder as an output folder but doesn't
      // declare this explicitly (maybe in the future)
      A4ELogging.debug(MSG_PYDEV);
    }
    final PythonProjectRole role = (PythonProjectRole) getEclipseProject().getRole(PythonProjectRole.class);
    final PythonResolver resolver = new PythonResolver();
    final RawPathEntry[] entries = role.getRawPathEntries(ReferenceKind.Output);
    final ResolvedPathEntry[] resolved = resolver.resolve(entries);
    final File[] result = new File[resolved.length];
    for (int i = 0; i < resolved.length; i++) {
      final ResolvedOutputEntry entry = (ResolvedOutputEntry) resolved[i];
      result[i] = getEclipseProject().getChild(entry.getFolder(), getPathStyle());
    }
    return result;
  }

} /* ENDCLASS */
