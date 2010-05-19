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
package org.ant4eclipse.ant.jdt.containerargs;

import org.ant4eclipse.ant.core.delegate.AbstractAntDelegate;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnsupportedAttributeException;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClasspathContainerArgumentDelegate extends AbstractAntDelegate implements
    JdtClasspathContainerArgumentComponent {

  /** - */
  public static final String                  CLASSPATH_ATTRIBUTE_PREFIX = "classpathAttributeContributor";

  /** - */
  private boolean                             _initialized               = false;

  /** - */
  private List<String>                        _knownAttributesList;

  /** the container argument list */
  private List<JdtClasspathContainerArgument> _containerArguments        = null;

  /**
   * <p>
   * </p>
   * 
   * @param component
   */
  public JdtClasspathContainerArgumentDelegate(ProjectComponent component) {
    super(component);

    this._containerArguments = new LinkedList<JdtClasspathContainerArgument>();
  }

  /**
   * {@inheritDoc}
   */
  public void setDynamicAttribute(String attributeName, String value) throws BuildException {
    init();

    if (!canHandleSubAttribute(attributeName)) {

      String msg = ((Task) getProjectComponent()).getTaskName() + " doesn't support the \"" + attributeName
          + "\" attribute.";
      throw new UnsupportedAttributeException(msg, attributeName);
      // ant4eclipse:executeJdtProject doesn't support the "targetPlatformId" attribute
    }

    // create argument
    JdtClasspathContainerArgument argument = new JdtClasspathContainerArgument();

    // set key and value
    argument.setKey(attributeName);
    argument.setValue(value);

    // add argument to argument list
    this._containerArguments.add(argument);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("deprecation")
  @Deprecated
  public JdtClasspathContainerArgument createJdtClasspathContainerArgument() {
    init();

    // create argument
    JdtClasspathContainerArgument argument = new JdtClasspathContainerArgument();

    // add argument to argument list
    this._containerArguments.add(argument);

    // return result
    return argument;
  }

  /**
   * {@inheritDoc}
   */
  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    init();

    // return result
    return this._containerArguments;
  }

  /**
   * <p>
   * Loads the configured subElementContributors.
   * </p>
   */
  protected void init() {

    // Return if already initialized
    if (this._initialized) {
      return;
    }

    // create the lists of dynamic attributes
    this._knownAttributesList = new LinkedList<String>();

    // get all properties that defines a SubElementContributor
    Ant4EclipseConfiguration config = ServiceRegistryAccess.instance().getService(Ant4EclipseConfiguration.class);
    Iterable<Pair<String, String>> subElementContributionEntries = config.getAllProperties(CLASSPATH_ATTRIBUTE_PREFIX);

    for (Pair<String, String> pair : subElementContributionEntries) {
      this._knownAttributesList.add(pair.getSecond());
    }

    // set initialized
    this._initialized = true;
  }

  /**
   * @param name
   * @param component
   * @return
   */
  protected boolean canHandleSubAttribute(String name) {
    init();

    // 
    for (String attribute : this._knownAttributesList) {
      if (attribute.equalsIgnoreCase(name)) {
        return true;
      }
    }

    // 
    return false;
  }

}
