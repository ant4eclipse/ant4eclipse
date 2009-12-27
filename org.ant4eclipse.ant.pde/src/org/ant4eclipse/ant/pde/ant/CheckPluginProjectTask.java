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
package org.ant4eclipse.ant.pde.ant;



import org.ant4eclipse.ant.platform.ant.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.pde.tools.PluginProjectChecker;
import org.ant4eclipse.lib.pde.tools.PluginProjectChecker.Issue;
import org.apache.tools.ant.BuildException;

import java.util.List;

/**
 * <p>
 * The {@link CheckPluginProjectTask} can be used to check if a plug-in project contains inconsistent or erroneous build
 * property entries.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CheckPluginProjectTask extends AbstractProjectBasedTask {

  /** indicates if the task should fail on error */
  private boolean _failOnError = true;

  /**
   * <p>
   * Returns <code>true</code>, if the task should fail on error.
   * </p>
   * 
   * @return <code>true</code>, if the task should fail on error.
   */
  public boolean isFailOnError() {
    return this._failOnError;
  }

  /**
   * <p>
   * Sets if the task should fail on error.
   * </p>
   * 
   * @param failOnError
   *          the value
   */
  public void setFailOnError(boolean failOnError) {
    this._failOnError = failOnError;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // create new PluginProjectChecker
    PluginProjectChecker pluginProjectChecker = new PluginProjectChecker(getEclipseProject());

    // check project
    List<Issue> issues = pluginProjectChecker.checkPluginProject();

    // if there are any issues, log them
    if (!issues.isEmpty()) {

      // iterate over the issue list
      for (Issue issue : issues) {
        if (this._failOnError) {
          A4ELogging.error(issue.getMessage());
        } else {
          A4ELogging.warn(issue.getMessage());
        }
      }

      // fail if specified
      if (this._failOnError) {
        throw new BuildException(String.format(
            "Inconsistent or erroneous plug-in project definition for project '%s'. See above for details.",
            getEclipseProject().getSpecifiedName()));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    requireWorkspaceAndProjectNameSet();
  }
}
