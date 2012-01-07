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
package org.ant4eclipse.lib.pydt.internal.tools;

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolver;
import org.ant4eclipse.lib.pydt.model.ReferenceKind;
import org.ant4eclipse.lib.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedProjectEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Resolver implementation for the cdt. Currently the cdt doesn't support any kind of specific containers used to access
 * other projects, so the referenced projects are used in general.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonReferencedProjectResolverImpl implements ReferencedProjectsResolver {

  private Workspace                     _workspace;

  private UsedProjectsArgumentComponent _args;

  private PythonResolver                _resolver;

  /**
   * Initialises this resolver implementation.
   */
  public PythonReferencedProjectResolverImpl() {
    _resolver = null;
    _workspace = null;
    _args = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canHandle( EclipseProject project ) {
    return PythonUtilities.isPythonRelatedProject( project );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EclipseProject> resolveReferencedProjects( EclipseProject project, List<Object> additionalElements ) {
    _args = getArgs( additionalElements );
    if( _args.isExport() && PythonUtilities.isPyDevProject( project ) ) {
      // 'exported' and 'all' is equivalent for PyDev since there's no distinction
      // between these settings so each path is considered to be exported
      A4ELogging.warn( "The mode 'exported' is treated as 'all' for a PyDev project." );
    }
    _workspace = project.getWorkspace();
    _resolver = new PythonResolver( _workspace, getMode(), true );
    ResolvedPathEntry[] resolved = _resolver.resolve( project.getSpecifiedName() );
    List<EclipseProject> result = new ArrayList<EclipseProject>();
    for( ResolvedPathEntry entry : resolved ) {
      if( entry.getKind() == ReferenceKind.Project ) {
        result.add( _workspace.getProject( ((ResolvedProjectEntry) entry).getProjectname() ) );
      }
    }
    return result;
  }

  /**
   * Returns the resolving mode used for the PythonResolver.
   * 
   * @return The resolving mode used for the PythonResolver. Not <code>null</code>.
   */
  private PythonResolver.Mode getMode() {
    if( _args.isAll() ) {
      return PythonResolver.Mode.all;
    } else if( _args.isDirect() ) {
      return PythonResolver.Mode.direct;
    } else /* if (_args.isExport()) */{
      return PythonResolver.Mode.exported;
    }
  }

  /**
   * Returns the arguments used to control the resolving process.
   * 
   * @param additionalElements
   *          Additional elements provided by the ant subsystem.
   * 
   * @return An instance controlling the resolving process. Not <code>null</code>.
   */
  private UsedProjectsArgumentComponent getArgs( List<Object> additionalElements ) {
    if( additionalElements != null ) {
      List<Object> elements = Utilities.filter( additionalElements, UsedProjectsArgumentComponent.class );
      if( !elements.isEmpty() ) {
        UsedProjectsArgumentComponent args = (UsedProjectsArgumentComponent) elements.get( 0 );
        if( elements.size() > 1 ) {
          A4ELogging.warn( "Only one element '%s' is allowed ! Using the first one: '%s'.", "pydtReferencedProject",
              String.valueOf( args ) );
        }
        return args;
      }
    }
    return UsedProjectsArgumentComponent.DEFAULT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getReferenceType() {
    return "pydt";
  }

} /* ENDCLASS */
