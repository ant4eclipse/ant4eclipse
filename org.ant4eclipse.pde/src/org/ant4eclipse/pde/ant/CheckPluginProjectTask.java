package org.ant4eclipse.pde.ant;

import java.util.List;

import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.pde.tools.PluginProjectChecker;
import org.ant4eclipse.pde.tools.PluginProjectChecker.Issue;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectBasedTask;
import org.apache.tools.ant.BuildException;

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
    return _failOnError;
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
    _failOnError = failOnError;
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
        if (_failOnError) {
          A4ELogging.error(issue.getMessage());
        } else {
          A4ELogging.warn(issue.getMessage());
        }
      }

      // fail if specified
      if (_failOnError) {
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
