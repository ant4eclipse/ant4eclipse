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

import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.pydt.internal.tools.PydtResolver;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;

import java.io.File;

/**
 * Basic task used to access the source path of a python project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetPydtPythonPathTask extends AbstractPydtGetProjectPathTask {

  /**
   * {@inheritDoc}
   */
  protected File[] resolvePath() {
    final PythonProjectRole role = (PythonProjectRole) getEclipseProject().getRole(PythonProjectRole.class);
    final PydtResolver resolver = new PydtResolver();
    final RawPathEntry[] entries = role.getRawPathEntries();
    final ResolvedPathEntry[] resolved = resolver.resolve(entries);
    final File[] result = resolver.expand(resolved, getEclipseProject());
    return result;
  }

} /* ENDCLASS */
