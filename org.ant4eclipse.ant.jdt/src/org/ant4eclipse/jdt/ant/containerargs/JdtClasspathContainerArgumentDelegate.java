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

import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;

import java.util.LinkedList;
import java.util.List;

public class JdtClasspathContainerArgumentDelegate implements JdtClasspathContainerArgumentComponent {

  /** the container argument list */
  private List<JdtClasspathContainerArgument> _containerArguments = new LinkedList<JdtClasspathContainerArgument>();

  /**
   * {@inheritDoc}
   */
  public JdtClasspathContainerArgument createJdtClasspathContainerArgument() {

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

    // return result
    return this._containerArguments;
  }
}
