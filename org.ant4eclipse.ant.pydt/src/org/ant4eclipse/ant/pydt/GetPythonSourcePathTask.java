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
package org.ant4eclipse.ant.pydt;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.pydt.PydtExceptionCode;
import org.ant4eclipse.lib.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.lib.pydt.internal.tools.PathExpander;
import org.ant4eclipse.lib.pydt.internal.tools.PythonResolver;
import org.ant4eclipse.lib.pydt.model.RawPathEntry;
import org.ant4eclipse.lib.pydt.model.ReferenceKind;
import org.ant4eclipse.lib.pydt.model.ResolvedPathEntry;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.util.List;

/**
 * Basic task used to access the source path of a python project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetPythonSourcePathTask extends AbstractPydtGetProjectPathTask {

  private boolean allowmuliplefolders = false;

  /**
   * Specifies if multiple folders are supported or not.
   * 
   * @param newallowmultiplefolders
   *          <code>true</code> <=> Multiple folders are supported, otherwise they're not.
   */
  public void setAllowMultipleFolders( boolean newallowmultiplefolders ) {
    allowmuliplefolders = newallowmultiplefolders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if( !allowmuliplefolders ) {
      PythonProjectRole  role    = getEclipseProject().getRole( PythonProjectRole.class );
      List<RawPathEntry> entries = role.getRawPathEntries( ReferenceKind.Source );
      if( entries.size() > 1 ) {
        throw new Ant4EclipseException( PydtExceptionCode.MULTIPLEFOLDERS, getEclipseProject().getSpecifiedName() );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {
    PythonProjectRole   role      = getEclipseProject().getRole( PythonProjectRole.class );
    PythonResolver      resolver  = new PythonResolver( getWorkspace(), PythonResolver.Mode.all, true );
    PathExpander        expander  = new PathExpander( getEclipseProject() );
    List<RawPathEntry>  entries   = role.getRawPathEntries( ReferenceKind.Source );
    ResolvedPathEntry[] resolved  = resolver.resolve( entries );
    return expander.expand( resolved, getPathStyle() );
  }

} /* ENDCLASS */
