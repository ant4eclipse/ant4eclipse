/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.pde.internal.tools;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * A plug-in set implementation that represent plug-ins stored as eclipse projects in the workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public final class PluginProjectSet extends AbstractBundleSet {

  /** the workspace which contains the projects to build */
  private final Workspace _workspace;

  /**
   * <p>
   * Creates a new instance of type TargetPlatform for a specific workspace.
   * </p>
   * 
   * @param workspace
   *          the workspace that will be used.
   */
  public PluginProjectSet(final Workspace workspace) {
    super(workspace);
    A4ELogging.trace("PluginProjectSet<init>(%s)", workspace);
    Assert.notNull(workspace);

    this._workspace = workspace;
  }

  /**
   * @see net.sf.ant4eclipse.tools.pde.internal.target.AbstractBundleSet#readBundles()
   */
  protected void readBundles() {

    // read all projects from workspace...
    final EclipseProject[] eclipseProjects = this._workspace.getAllProjects();

    // add all plugin projects to plugin list and exported package list
    for (int i = 0; i < eclipseProjects.length; i++) {
      final EclipseProject project = eclipseProjects[i];
      if (PluginProjectRole.Helper.hasPluginProjectRole(project)) {
        final BundleDescription bundleDescription = PluginProjectRole.Helper.getPluginProjectRole(project)
            .getBundleDescription();
        addBundleDescription(bundleDescription);
      }
    }
  }
}
