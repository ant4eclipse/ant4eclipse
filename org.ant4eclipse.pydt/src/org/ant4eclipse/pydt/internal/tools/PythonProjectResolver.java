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

  private PathEntryRegistry _registry;

  private Workspace         _workspace;

  private boolean           _export;

  public PythonProjectResolver(final Workspace workspace) {
    _registry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
    _workspace = workspace;
    _export = true;
  }

  public void setExport(boolean newexport) {
    _export = newexport;
  }

  private List<RawPathEntry> loadEntries(final EclipseProject project) {
    final List<RawPathEntry> result = new ArrayList<RawPathEntry>();
    final PythonProjectRole role = (PythonProjectRole) project.getRole(PythonProjectRole.class);
    final RawPathEntry[] entries = role.getRawPathEntries(ReferenceKind.Project);
    for (RawPathEntry entry : entries) {
      result.add(entry);
    }
    return result;
  }

  public List<EclipseProject> resolve(final EclipseProject project) {
    List<EclipseProject> result = new ArrayList<EclipseProject>();
    resolve(result, project);
    return result;
  }

  private void resolve(final List<EclipseProject> receiver, final EclipseProject project) {
    final List<RawPathEntry> entries = loadEntries(project);
    resolve(receiver, entries);
  }

  private void resolve(final List<EclipseProject> receiver, final List<RawPathEntry> entries) {
    final Set<RawPathEntry> exported = new HashSet<RawPathEntry>();
    while (!entries.isEmpty()) {
      final RawPathEntry entry = entries.remove(0);
      if (!_registry.isResolved(entry)) {
        _registry.registerResolvedPathEntry(entry, resolveRawPathEntry(entry));
      }
      final ResolvedProjectEntry resolved = (ResolvedProjectEntry) _registry.getResolvedPathEntry(entry);
      final EclipseProject refproject = _workspace.getProject(resolved.getProjectname());
      if (!receiver.contains(refproject)) {
        // only add the project if that hasn't been done yet
        receiver.add(refproject);
      }
      if (entry.isExported() && (!exported.contains(entry))) {
        // extend the list with entries provided by the dependent project
        exported.add(entry);
        if (_export) {
          entries.addAll(loadEntries(refproject));
        }
      }
    }
  }

  private ResolvedProjectEntry resolveRawPathEntry(RawPathEntry entry) {
    String value = entry.getValue();
    if ((value.charAt(0) != '/') || (value.length() == 1)) {
      /** @todo [31-Jul-2009:KASI] invalid syntax, generate an error here */
      return null;
    }
    return new ResolvedProjectEntry(value.substring(1));
  }

} /* ENDCLASS */
