package org.ant4eclipse.jdt.ant.containerargs;

import org.ant4eclipse.platform.ant.SubElementContribution;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public class JdtClasspathContainerArgumentContribution implements SubElementContribution {

  private final JdtClasspathContainerArgumentDelegate _classpathContainerArgumentDelegate;

  public JdtClasspathContainerArgumentContribution() {
    this._classpathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();
  }

  public boolean canHandleSubElement(final String name, final ProjectComponent component) {
    // TODO? Class? component?
    return "jdtClasspathContainerArgument".equalsIgnoreCase(name);
  }

  public Object createSubElement(final String name, final ProjectComponent component) throws BuildException {
    return this._classpathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }
}
