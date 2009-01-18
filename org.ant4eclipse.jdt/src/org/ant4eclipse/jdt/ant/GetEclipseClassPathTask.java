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

import java.io.File;
import java.util.Properties;

import org.ant4eclipse.jdt.ant.containerargs.ClasspathContainerArgument;
import org.ant4eclipse.jdt.ant.containerargs.ClasspathContainerArgumentDelegate;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.platform.ant.base.AbstractGetProjectPathTask;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetEclipseClassPathTask extends AbstractGetProjectPathTask {

  /** Indicates whether the class path should be resolved as a runtime class path or not */
  private boolean                            _runtime = false;

  /** - */
  private ClasspathContainerArgumentDelegate _classpathContainerArgumentDelegate;

  public GetEclipseClassPathTask() {
    super();
    this._classpathContainerArgumentDelegate = new ClasspathContainerArgumentDelegate();
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

  public ClasspathContainerArgument createContainerArg() {
    if (this._classpathContainerArgumentDelegate == null) {
      this._classpathContainerArgumentDelegate = new ClasspathContainerArgumentDelegate();
    }

    return this._classpathContainerArgumentDelegate.createContainerArg();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {

    final Properties properties = this._classpathContainerArgumentDelegate.getAsProperties();

    final ResolvedClasspath resolvedClasspath = JdtResolver.resolveProjectClasspath(getEclipseProject(), isRelative(),
        isRuntime(), properties);

    return resolvedClasspath.getClasspathFiles();
  }
}
