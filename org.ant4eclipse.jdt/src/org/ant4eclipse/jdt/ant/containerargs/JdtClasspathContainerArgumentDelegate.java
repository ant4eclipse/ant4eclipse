package org.ant4eclipse.jdt.ant.containerargs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JdtClasspathContainerArgumentDelegate implements JdtClasspathContainerArgumentComponent {

  /** the container argument list */
  private final List<JdtClasspathContainerArgument> _containerArguments = new LinkedList<JdtClasspathContainerArgument>();

  /**
   * @see org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent#createContainerArg()
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
   * @see org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent#getContainerArguments()
   */
  public Map<String, Object> getJdtClasspathContainerArguments() {
    // create result
    final Map<String, Object> containerArguments = new HashMap<String, Object>();

    // put all arguments in the result
    for (final JdtClasspathContainerArgument classpathContainerArgument : this._containerArguments) {
      containerArguments.put(classpathContainerArgument.getKey(), classpathContainerArgument.getValue());
    }

    // return result
    return containerArguments;
  }
}
