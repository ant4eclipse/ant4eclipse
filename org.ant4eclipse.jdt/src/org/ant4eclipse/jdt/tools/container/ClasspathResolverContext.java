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
package org.ant4eclipse.jdt.tools.container;

import java.util.List;

import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

/**
 * <p>
 * An instance of type {@link ClasspathResolverContext} is passed to a {@link ClasspathContainerResolver} to enable the
 * resolution of eclipse class path containers.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClasspathResolverContext {

  /**
   * <p>
   * Returns <code>true</code> if the the class path should be resolved relative to the workspace.
   * <p>
   * 
   * @return <code>true</code> if the the class path should be resolved relative to the workspace.
   */
  public boolean isWorkspaceRelative();

  /**
   * <p>
   * Returns <code>true</code> if the the class path is a runtime class path.
   * </p>
   * 
   * @return <code>true</code> if the the class path is a runtime class path.
   */
  public boolean isRuntime();

  /**
   * <p>
   * Returns the current {@link Workspace}.
   * </p>
   * 
   * @return the current {@link Workspace}.
   */
  public Workspace getWorkspace();

  /**
   * <p>
   * Returns <code>true</code>, if a current project is set.
   * </p>
   * 
   * @return <code>true</code>, if a current project is set.
   */
  public boolean hasCurrentProject();

  /**
   * <p>
   * Returns the current {@link EclipseProject}.
   * </p>
   * 
   * @return The current {@link EclipseProject}. Maybe null.
   */
  public EclipseProject getCurrentProject();

  /**
   * <p>
   * Returns <code>true</code> if the current project is the root project.
   * </p>
   * 
   * @return <code>true</code> if the current project is the root project.
   */
  public boolean isCurrentProjectRoot();

  /**
   * <p>
   * Resolves the class path for a (java-)project.
   * </p>
   * 
   * @param project
   *          the (java-)project which class path should be resolved.
   */
  public void resolveProjectClasspath(EclipseProject project);

  /**
   * 
   */
  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments();

  /**
   * <p>
   * Adds a resolved class path entry to the class path.
   * </p>
   * 
   * @param entry
   *          the entry to add.
   */
  public void addClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry);

  /**
   * <p>
   * Adds a resolved class path entry to the boot class path.
   * </p>
   * 
   * @param entry
   *          the entry to add.
   */
  public void setBootClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry);
}