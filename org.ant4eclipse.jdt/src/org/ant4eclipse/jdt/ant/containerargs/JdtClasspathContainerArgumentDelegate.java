package org.ant4eclipse.jdt.ant.containerargs;

import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import java.util.LinkedList;
import java.util.List;

public class JdtClasspathContainerArgumentDelegate implements JdtClasspathContainerArgumentComponent {

  /** the container argument list */
  private final List<JdtClasspathContainerArgument> _containerArguments = new LinkedList<JdtClasspathContainerArgument>();

  /**
   * {@inheritDoc}
   */
  public JdtClasspathContainerArgument createJdtClasspathContainerArgument() {

    // create argument
    final JdtClasspathContainerArgument argument = new JdtClasspathContainerArgument();

    // add argument to argument list
    this._containerArguments.add(argument);

    // return result
    return argument;
  }

  /**
   * {@inheritDoc}
   */
  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {

    // return result
    return this._containerArguments;
  }
}
