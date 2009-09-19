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
package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent;
import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentDelegate;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import org.ant4eclipse.platform.ant.core.task.AbstractGetProjectPathTask;

import java.io.File;
import java.util.List;

/**
 * <p>
 * Resolves a class path from a underlying jdt project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetJdtClassPathTask extends AbstractGetProjectPathTask implements JdtClasspathContainerArgumentComponent {

  /** Indicates whether the class path should be resolved as a runtime class path or not */
  private boolean                               _runtime = false;

  /** the {@link JdtClasspathContainerArgumentDelegate} */
  private JdtClasspathContainerArgumentDelegate _classpathContainerArgumentDelegate;

  /**
   * <p>
   * Creates a new instance of type {@link GetJdtClassPathTask}.
   * </p>
   */
  public GetJdtClassPathTask() {
    super();

    // create the JdtClasspathContainerArgumentDelegate
    this._classpathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();
  }

  /**
   * <p>
   * Sets the class path id.
   * </p>
   * 
   * @param id
   *          the class path id
   */
  public void setClasspathId(String id) {
    super.setPathId(id);
  }

  /**
   * <p>
   * Returns if a runtime path should be resolved or not.
   * </p>
   * 
   * @return if a runtime path should be resolved or not.
   */
  public boolean isRuntime() {
    return this._runtime;
  }

  /**
   * <p>
   * Returns if a runtime path should be resolved or not.
   * </p>
   * 
   * @param runtime
   *          if a runtime path should be resolved or not.
   */
  public void setRuntime(boolean runtime) {
    this._runtime = runtime;
  }

  /**
   * {@inheritDoc}
   */
  public JdtClasspathContainerArgument createJdtClasspathContainerArgument() {
    return this._classpathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }

  /**
   * {@inheritDoc}
   */
  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return this._classpathContainerArgumentDelegate.getJdtClasspathContainerArguments();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {

    // resolve the path
    ResolvedClasspath resolvedClasspath = JdtResolver.resolveProjectClasspath(getEclipseProject(), isRelative(),
        isRuntime(), this._classpathContainerArgumentDelegate.getJdtClasspathContainerArguments());

    // return the path files
    return resolvedClasspath.getClasspathFiles();
  }
}
