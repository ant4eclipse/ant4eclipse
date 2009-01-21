package org.ant4eclipse.jdt.ant.containerargs;

import org.ant4eclipse.platform.ant.DynamicElementContributor;
import org.ant4eclipse.platform.ant.ExecuteProjectSetTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public class JdtClasspathContainerArgumentContributor implements DynamicElementContributor {

  private final JdtClasspathContainerArgumentDelegate _classpathContainerArgumentDelegate;

  public JdtClasspathContainerArgumentContributor() {
    this._classpathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();
  }

  public boolean canHandle(final String name, final ProjectComponent component) {

    System.err.println(name);
    System.err.println(component);

    return "jdtClasspathContainerArgument".equalsIgnoreCase(name) && component instanceof ExecuteProjectSetTask;
  }

  public Object createDynamicElement(final String name, final ProjectComponent component) throws BuildException {
    return this._classpathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }
}
