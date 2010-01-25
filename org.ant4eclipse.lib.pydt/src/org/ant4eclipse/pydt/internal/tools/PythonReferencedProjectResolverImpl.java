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
package org.ant4eclipse.pydt.internal.tools;

import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;

import org.ant4eclipse.pydt.ant.usedargs.UsedProjectsArgumentComponent;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.pydt.model.ResolvedProjectEntry;

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
    this._resolver = null;
    this._workspace = null;
    this._args = null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean canHandle(EclipseProject project) {
    return PythonUtilities.isPythonRelatedProject(project);
  }

  /**
   * {@inheritDoc}
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements) {
    this._args = getArgs(additionalElements);
    if (this._args.isExport() && PythonUtilities.isPyDevProject(project)) {
      // 'exported' and 'all' is equivalent for PyDev since there's no distinction
      // between these settings so each path is considered to be exported
      A4ELogging.warn("The mode 'exported' is treated as 'all' for a PyDev project.");
    }
    this._workspace = project.getWorkspace();
    this._resolver = new PythonResolver(this._workspace, getMode(), true);
    ResolvedPathEntry[] resolved = this._resolver.resolve(project.getSpecifiedName());
    List<EclipseProject> result = new ArrayList<EclipseProject>();
    for (ResolvedPathEntry entry : resolved) {
      if (entry.getKind() == ReferenceKind.Project) {
        result.add(this._workspace.getProject(((ResolvedProjectEntry) entry).getProjectname()));
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
    if (this._args.isAll()) {
      return PythonResolver.Mode.all;
    } else if (this._args.isDirect()) {
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
  private UsedProjectsArgumentComponent getArgs(List<Object> additionalElements) {
    if (additionalElements != null) {
      List<Object> elements = Utilities.filter(additionalElements, UsedProjectsArgumentComponent.class);
      if (!elements.isEmpty()) {
        UsedProjectsArgumentComponent args = (UsedProjectsArgumentComponent) elements.get(0);
        if (elements.size() > 1) {
          A4ELogging.warn("Only one element '%s' is allowed ! Using the first one: '%s'.",
              UsedProjectsArgumentComponent.ELEMENTNAME, String.valueOf(args));
        }
        return args;
      }
    }
    return UsedProjectsArgumentComponent.DEFAULT;
  }

} /* ENDCLASS */
