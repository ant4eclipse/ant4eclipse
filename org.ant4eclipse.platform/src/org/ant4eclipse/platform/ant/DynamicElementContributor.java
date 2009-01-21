package org.ant4eclipse.platform.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public interface DynamicElementContributor {

  public boolean canHandle(String name, ProjectComponent component);

  /**
   * Create an element with the given name
   * 
   * @param name
   *          the element nbame
   * @throws BuildException
   *           when any error occurs
   * @return the element created
   */
  public Object createDynamicElement(String name, ProjectComponent component) throws BuildException;
}
