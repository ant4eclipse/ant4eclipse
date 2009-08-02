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

import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

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
 * Helper class which is used to resolve projects.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonProjectResolver {

  private PathEntryRegistry             _registry;

  private Workspace                     _workspace;

  private UsedProjectsArgumentComponent _args;

  /**
   * Initialises this resolved to make use of the supplied workspace.
   * 
   * @param workspace
   *          The workspace which is currently used. Not <code>null</code>.
   */
  public PythonProjectResolver(final Workspace workspace) {
    _registry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
    _workspace = workspace;
    _args = UsedProjectsArgumentComponent.DEFAULT;
  }

  /**
   * Changes the argument to control the resolving process.
   * 
   * @param newargs
   *          The new argument to control the resolving process.
   */
  public void setArgs(UsedProjectsArgumentComponent newargs) {
    _args = newargs;
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
   * Resolves the referenced projects according to the current resolving configuration (see
   * {@link #setArgs(UsedProjectsArgumentComponent)}.
   * 
   * @param project
   *          The project which referenced projects will be resolved. Not <code>null</code>.
   * 
   * @return A list of resolved projects. Not <code>null</code>.
   */
  public List<EclipseProject> resolve(final EclipseProject project) {
    List<EclipseProject> result = new ArrayList<EclipseProject>();
    resolve(result, loadEntries(project));
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
        _registry.registerResolvedPathEntry(entry, resolveRawPathEntry(entry));
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

  /**
   * Creates a resolved project entry from the supplied raw entry.
   * 
   * @param entry
   *          A raw entry used to refer to a project. Not <code>null</code>.
   * 
   * @return The resolved project entry equivalent. Not <code>null</code>.
   */
  private ResolvedProjectEntry resolveRawPathEntry(RawPathEntry entry) {
    String value = entry.getValue();
    if ((value.charAt(0) != '/') || (value.length() == 1)) {
      return null;
    }
    return new ResolvedProjectEntry(value.substring(1));
  }

} /* ENDCLASS */
