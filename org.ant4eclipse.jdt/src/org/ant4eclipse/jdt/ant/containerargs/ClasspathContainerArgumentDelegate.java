package org.ant4eclipse.jdt.ant.containerargs;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ClasspathContainerArgumentDelegate {

  /** the container argument list */
  private final List<ClasspathContainerArgument> _containerArguments = new LinkedList<ClasspathContainerArgument>();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public ClasspathContainerArgument createContainerArg() {
    // create argument
    final ClasspathContainerArgument argument = new ClasspathContainerArgument();

    // add argument to argument list
    this._containerArguments.add(argument);

    // return result
    return argument;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Properties getAsProperties() {
    // create result
    final Properties result = new Properties();

    // put all arguments in the result
    for (ClasspathContainerArgument classpathContainerArgument : _containerArguments) {
      result.put(classpathContainerArgument.getKey(), classpathContainerArgument.getValue());
    }

    // return result
    return result;
  }
}
