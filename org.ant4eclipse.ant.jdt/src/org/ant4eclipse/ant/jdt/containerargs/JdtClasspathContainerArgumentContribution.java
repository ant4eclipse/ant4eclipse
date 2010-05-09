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

import org.ant4eclipse.ant.platform.SubAttributeContribution;
import org.ant4eclipse.ant.platform.SubElementContribution;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathContainerResolver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Implementation of a {@link SubElementContribution} that can handle <code>&lt;jdtClasspathContainerArgument&gt;</code>
 * elements.
 * </p>
 * <p>
 * Several jdt class path containers require specific information for the resolving process. E.g. it is necessary a // *
 * valid target platform to resolve a PDE <code>org.eclipse.pde.core.requiredPlugins</code> container. Such information
 * can be provided through <code>&lt;jdtClasspathContainerArgument&gt;</code> elements. The key for such an additional
 * information is provided by the specific {@link ClasspathContainerResolver}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClasspathContainerArgumentContribution implements SubElementContribution, SubAttributeContribution {

  /** - */
  public static final String                    CLASSPATH_ATTRIBUTE_PREFIX = "classpathAttributeContributor";

  /** - */
  private JdtClasspathContainerArgumentDelegate _classpathContainerArgumentDelegate;

  /** - */
  private boolean                               _initialized               = false;

  /** - */
  private List<String>                          _attributeList;

  /**
   * <p>
   * Creates a new instance of type JdtClasspathContainerArgumentContribution.
   * </p>
   * 
   */
  public JdtClasspathContainerArgumentContribution(ProjectComponent projectComponent) {
    this._classpathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate(projectComponent);
  }

  /**
   * {@inheritDoc}
   */
  public boolean canHandleSubElement(String name, ProjectComponent component) {
    init();
    // TODO? Class? component?
    return "jdtClasspathContainerArgument".equalsIgnoreCase(name);
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public Object createSubElement(String name, ProjectComponent component) throws BuildException {
    init();
    return this._classpathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }

  /**
   * @see org.ant4eclipse.ant.platform.SubAttributeContribution#canHandleSubAttribute(java.lang.String,
   *      org.apache.tools.ant.ProjectComponent)
   */
  public boolean canHandleSubAttribute(String name, ProjectComponent component) {
    init();

    // 
    for (String attribute : this._attributeList) {
      if (attribute.equalsIgnoreCase(name)) {
        return true;
      }
    }

    // 
    return false;
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
    this._attributeList = new LinkedList<String>();

    // get all properties that defines a SubElementContributor
    Ant4EclipseConfiguration config = ServiceRegistry.instance().getService(Ant4EclipseConfiguration.class);
    Iterable<Pair<String, String>> subElementContributionEntries = config.getAllProperties(CLASSPATH_ATTRIBUTE_PREFIX);

    for (Pair<String, String> pair : subElementContributionEntries) {
      this._attributeList.add(pair.getSecond());
    }

    // set initialized
    this._initialized = true;
  }
}
