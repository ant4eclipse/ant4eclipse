/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package net.sf.ant4eclipse.model.platform.resource.internal;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.model.platform.resource.ProjectNature;

/**
 * Encapsulates a nature of a project. A project can have multiple natures.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectNatureImpl implements ProjectNature {

  /** the name of the nature */
  private final String _name;

  /**
   * Creates a new instance of type ProjectNature
   * 
   * @param name
   */
  public ProjectNatureImpl(final String name) {
    Assert.notNull(name);

    this._name = name;
  }

  /**
   * @return Returns the name.
   */
  public final String getName() {
    return this._name;
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    final ProjectNatureImpl castedObj = (ProjectNatureImpl) o;
    return (this._name.equals(castedObj._name));
  }

  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + this._name.hashCode();
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ProjectNature:");
    buffer.append(" name: ");
    buffer.append(this._name);
    buffer.append("]");
    return buffer.toString();
  }
}