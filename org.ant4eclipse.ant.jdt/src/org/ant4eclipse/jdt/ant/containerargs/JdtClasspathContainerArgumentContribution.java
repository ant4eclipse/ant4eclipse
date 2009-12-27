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
package org.ant4eclipse.jdt.ant.containerargs;


import org.ant4eclipse.platform.ant.SubElementContribution;

import org.ant4eclipse.lib.jdt.tools.container.ClasspathContainerResolver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

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
public class JdtClasspathContainerArgumentContribution implements SubElementContribution {

  /** - */
  private JdtClasspathContainerArgumentDelegate _classpathContainerArgumentDelegate;

  /**
   * <p>
   * Creates a new instance of type JdtClasspathContainerArgumentContribution.
   * </p>
   * 
   */
  public JdtClasspathContainerArgumentContribution() {
    this._classpathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();
  }

  /**
   * {@inheritDoc}
   */
  public boolean canHandleSubElement(String name, ProjectComponent component) {
    // TODO? Class? component?
    return "jdtClasspathContainerArgument".equalsIgnoreCase(name);
  }

  /**
   * {@inheritDoc}
   */
  public Object createSubElement(String name, ProjectComponent component) throws BuildException {
    return this._classpathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }
}
