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
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetJdtClassPathTask extends AbstractGetProjectPathTask implements JdtClasspathContainerArgumentComponent {

  /** Indicates whether the class path should be resolved as a runtime class path or not */
  private boolean                                     _runtime = false;

  /** - */
  private final JdtClasspathContainerArgumentDelegate _classpathContainerArgumentDelegate;

  public GetJdtClassPathTask() {
    super();
    this._classpathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();
  }

  /**
   * @param id
   */
  public void setClasspathId(final String id) {
    super.setPathId(id);
  }

  /**
   * @return Returns the runtime.
   */
  public boolean isRuntime() {
    return this._runtime;
  }

  /**
   * @param runtime
   *          The runtime to set.
   */
  public void setRuntime(final boolean runtime) {
    this._runtime = runtime;
  }

  /**
   * @see org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent#createJdtClasspathContainerArgument()
   */
  public JdtClasspathContainerArgument createJdtClasspathContainerArgument() {
    return this._classpathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }

  /**
   * @see org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent#getJdtClasspathContainerArguments()
   */
  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return this._classpathContainerArgumentDelegate.getJdtClasspathContainerArguments();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {

    final ResolvedClasspath resolvedClasspath = JdtResolver.resolveProjectClasspath(getEclipseProject(), isRelative(),
        isRuntime(), this._classpathContainerArgumentDelegate.getJdtClasspathContainerArguments());

    return resolvedClasspath.getClasspathFiles();
  }
}
