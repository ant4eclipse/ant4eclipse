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
import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnsupportedAttributeException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClasspathContainerArgumentDelegate extends AbstractAntDelegate implements
    JdtClasspathContainerArgumentComponent {

  private boolean                             _initialized        = false;

  /** the container argument list */
  private List<JdtClasspathContainerArgument> _containerArguments = null;

  /**
   * <p>
   * </p>
   * 
   * @param component
   */
  public JdtClasspathContainerArgumentDelegate( ProjectComponent component ) {
    super( component );
    _containerArguments = new ArrayList<JdtClasspathContainerArgument>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDynamicAttribute( String attributeName, String value ) throws BuildException {
    init();

    if( !canHandleSubAttribute( attributeName ) ) {
      String msg = String.format("%s doesn't support the \"%s\" attribute.", ((Task) getProjectComponent()).getTaskName(), attributeName);
      throw new UnsupportedAttributeException( msg, attributeName );
    }

    // create argument
    JdtClasspathContainerArgument argument = new JdtClasspathContainerArgument();

    // set key and value
    argument.setKey( attributeName );
    argument.setValue( value );

    // add argument to argument list
    _containerArguments.add( argument );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    init();

    // return result
    return _containerArguments;
  }

  /**
   * <p>
   * Loads the configured subElementContributors.
   * </p>
   */
  protected void init() {

    // Return if already initialized
    if( _initialized ) {
      return;
    }

    // set initialized
    _initialized = true;
  }

  /**
   * @param name
   * @param component
   * @return
   */
  protected boolean canHandleSubAttribute( String name ) {
    init();
    return false;
  }

} /* ENDCLASS */
