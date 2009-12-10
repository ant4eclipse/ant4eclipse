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
package org.ant4eclipse.platform.internal.model.resource;

import org.ant4eclipse.platform.model.resource.BuildCommand;

import org.ant4eclipse.lib.core.Assure;

/**
 * <p>
 * Encapsulates an build command of an eclipse java project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BuildCommandImpl implements BuildCommand {

  /** the name of the build command */
  private String _name;

  /**
   * the 'triggers'-element, or null if not present
   */
  private String _triggers;

  /**
   * @param name
   *          the name of the build command.
   */
  public BuildCommandImpl(String name) {
    this(name, null);
  }

  /**
   * Creates a new instance of type BuildCommand.
   * 
   * @param name
   *          the name of the build command.
   * @param the
   *          triggers that would cause this builder to run in eclipse (might be null)
   */
  public BuildCommandImpl(String name, String triggers) {
    Assure.notNull(name);
    this._name = name;
    if ((triggers != null) && triggers.endsWith(",")) {
      if (triggers.length() > 1) { // remove trailing ,
        triggers = triggers.substring(0, triggers.length() - 1);
      } else {
        triggers = null;
      }
    }
    this._triggers = triggers;
  }

  /**
   * {@inheritDoc}
   */
  public final String getName() {
    return this._name;
  }

  /**
   * {@inheritDoc}
   */
  public String getTriggers() {
    return this._triggers;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasTriggers() {
    return (this._triggers != null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    BuildCommandImpl castedObj = (BuildCommandImpl) o;
    return ((this._name == null ? castedObj._name == null : this._name.equals(castedObj._name)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[BuildCommand:");
    buffer.append(" name: ");
    buffer.append(this._name);
    buffer.append(" triggers: ");
    buffer.append(this._triggers);
    buffer.append("]");
    return buffer.toString();
  }
}
