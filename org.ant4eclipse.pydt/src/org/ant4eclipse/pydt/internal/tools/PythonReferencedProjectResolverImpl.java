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
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;

import org.ant4eclipse.pydt.ant.usedargs.UsedProjectsArgumentComponent;
import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedProjectEntry;
import org.ant4eclipse.pydt.tools.PathEntryRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Resolver implementation for the cdt. Currently the cdt doesn't support any kind of specific containers used to access
 * other projects, so the referenced projects are used in general.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonReferencedProjectResolverImpl implements ReferencedProjectsResolver {

  private PathEntryRegistry             _registry;

  private Workspace                     _workspace;

  private UsedProjectsArgumentComponent _args;

  private PythonResolver                  _resolver;

  /**
   * Initialises this resolver implementation.
   */
  public PythonReferencedProjectResolverImpl() {
    _registry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
    _resolver = new PythonResolver();
    _workspace = null;
    _args = null;
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
    _args = getArgs(additionalElements);
    if (_args.isExport() && PythonUtilities.isPyDevProject(project)) {
      A4ELogging.warn("The mode 'exported' is treated as 'all' for a PyDev project.");
    }
    _workspace = project.getWorkspace();
    List<EclipseProject> result = new ArrayList<EclipseProject>();
    resolve(result, loadEntries(project));
    return result;
  }

  /**
   * Returns the arguments used to control the resolving process.
   * 
   * @param additionalElements
   *          Additional elements provided by the ant subsystem.
   * 
   * @return An instance controlling the resolving process. Not <code>null</code>.
   */
  private UsedProjectsArgumentComponent getArgs(final List<Object> additionalElements) {
    if (additionalElements != null) {
      final List<Object> elements = Utilities.filter(additionalElements, UsedProjectsArgumentComponent.class);
      if (!elements.isEmpty()) {
        final UsedProjectsArgumentComponent args = (UsedProjectsArgumentComponent) elements.get(0);
        if (elements.size() > 1) {
          A4ELogging.warn("Only one element '%s' is allowed ! Using the first one: '%s'.",
              UsedProjectsArgumentComponent.ELEMENTNAME, String.valueOf(args));
        }
        return args;
      }
    }
    return UsedProjectsArgumentComponent.DEFAULT;
  }

  /**
   * Returns a list of raw path entries from the supplied project.
   * 
   * @param project
   *          The project which raw path entries have to be returned. Not <code>null</code>.
   * 
   * @return A list of raw path entries. Not <code>null</code>.
   */
  private List<RawPathEntry> loadEntries(final EclipseProject project) {
    final List<RawPathEntry> result = new ArrayList<RawPathEntry>();
    final PythonProjectRole role = (PythonProjectRole) project.getRole(PythonProjectRole.class);
    final RawPathEntry[] entries = role.getRawPathEntries(ReferenceKind.Project);
    for (RawPathEntry entry : entries) {
      result.add(entry);
    }
    return result;
  }

  /**
   * Resolving process which tries to lookup projects depending on the raw path entries needed to be processed.
   * 
   * @param receiver
   *          A list used to collect the resolved process. Not <code>null</code>.
   * @param entries
   *          The raw path entries needed to be processed. Not <code>null</code>.
   */
  private void resolve(final List<EclipseProject> receiver, final List<RawPathEntry> entries) {

    final Set<RawPathEntry> followed = new HashSet<RawPathEntry>();
    while (!entries.isEmpty()) {

      // get the first entry
      final RawPathEntry entry = entries.remove(0);
      if (!_registry.isResolved(entry)) {
        // until now it has not been resolved, so resolve and register it
        _resolver.resolve(entry);
      }
      // access the resolved path entry
      final ResolvedProjectEntry resolved = (ResolvedProjectEntry) _registry.getResolvedPathEntry(entry);
      // fetch the EclipseProject instance from the workspace
      final EclipseProject refproject = _workspace.getProject(resolved.getProjectname());
      if (!receiver.contains(refproject)) {
        // only add the project if that hasn't been done yet
        receiver.add(refproject);
      }

      // indirection handling: only enter if it has not been followed yet
      if ((!followed.contains(entry)) && (!_args.isDirect())) {

        if (_args.isAll()) {
          // this mode overrides any path setting
          followed.add(entry);
          entries.addAll(loadEntries(refproject));
        } else if (_args.isExport() && entry.isExported()) {
          // just follow exported entries
          followed.add(entry);
          entries.addAll(loadEntries(refproject));
        }

      }

    }

  }

} /* ENDCLASS */
