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
import org.ant4eclipse.pydt.internal.tools.PythonResolver;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
  public void setIgnoreruntime(final boolean ignoreruntime) {
    _ignoreruntime = ignoreruntime;
  }

  /**
   * {@inheritDoc}
   */
  protected File[] resolvePath() {
    final PythonProjectRole role = (PythonProjectRole) getEclipseProject().getRole(PythonProjectRole.class);
    final PythonResolver resolver = new PythonResolver();
    RawPathEntry[] entries = role.getRawPathEntries();
    if (_ignoreruntime) {
      // the runtimes shall be ignored, so don't resolve them
      entries = removeRuntimeEntries(entries);
    }
    final ResolvedPathEntry[] resolved = resolver.resolve(getEclipseProject().getSpecifiedName(), entries);
    final File[] result = resolver.expand(resolved, getEclipseProject(), getPathStyle());
    return result;
  }

  /**
   * Removes the runtime entries so they will not be used for the resolving process.
   * 
   * @param entries
   *          A list of entries needed to be filtered. Not <code>null</code>.
   * 
   * @return A filtered list of entries not containing the {@link ReferenceKind#Runtime}.
   */
  private RawPathEntry[] removeRuntimeEntries(final RawPathEntry[] entries) {
    List<RawPathEntry> list = new ArrayList<RawPathEntry>();
    for (int i = 0; i < entries.length; i++) {
      if (entries[i].getKind() != ReferenceKind.Runtime) {
        list.add(entries[i]);
      }
    }
    return list.toArray(new RawPathEntry[list.size()]);
  }

} /* ENDCLASS */
