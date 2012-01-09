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
package org.ant4eclipse.lib.jdt.tools.container;

/**
 * <p>
 * Represents a jdt class path container argument.
 * </p>
 * <p>
 * Some class path containes require additional information to be resolved. E.g. the PDE container (RequiredPlugins)
 * needs a reference to a valid target platform against a plug-in project will be resolved.
 * JdtClasspathContainerArguments allow to provides such values.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClasspathContainerArgument {

  /** the key */
  private String key;

  /** the value */
  private String value;

  /**
   * <p>
   * Returns the key of the {@link JdtClasspathContainerArgument}.
   * </p>
   * 
   * @return the key.
   */
  public final String getKey() {
    return key;
  }

  /**
   * <p>
   * Sets the key of the {@link JdtClasspathContainerArgument}.
   * </p>
   * 
   * @param newkey
   */
  public final void setKey( String newkey ) {
    key = newkey;
  }

  /**
   * <p>
   * Returns the value of the {@link JdtClasspathContainerArgument}.
   * </p>
   * 
   * @return the value of the {@link JdtClasspathContainerArgument}.
   */
  public final String getValue() {
    return value;
  }

  /**
   * <p>
   * Sets the value of the {@link JdtClasspathContainerArgument}.
   * </p>
   * 
   * @param newvalue
   *          the value of the {@link JdtClasspathContainerArgument}.
   */
  public final void setValue( String newvalue ) {
    value = newvalue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[JdtClasspathContainerArgument: key: %s value: %s]", key, value );
  }
  
} /* ENDCLASS */
