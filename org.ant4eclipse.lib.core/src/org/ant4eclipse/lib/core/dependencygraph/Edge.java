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
package org.ant4eclipse.lib.core.dependencygraph;

import org.ant4eclipse.lib.core.Assure;

/**
 * <p>
 * Internal representation of an edge.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <T>
 *          the type of the vertices
 */
public class Edge<T> {

  /** parent */
  private T parent;

  /** child */
  private T child;

  /**
   * <p>
   * Creates a new instance of type Edge.
   * </p>
   * 
   * @param aParent
   *          the parent object
   * @param aChild
   *          the child object
   */
  public Edge(T aParent, T aChild) {
    Assure.notNull(aParent);
    Assure.notNull(aChild);

    this.parent = aParent;
    this.child = aChild;
  }

  /**
   * <p>
   * Returns the child object.
   * </p>
   * 
   * @return the child object.
   */
  public T getChild() {
    return this.child;
  }

  /**
   * <p>
   * Returns the parent object.
   * </p>
   * 
   * @return the parent object.
   */
  public T getParent() {
    return this.parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int PRIME = 31;
    int result = 1;
    result = PRIME * result + this.parent.hashCode();
    result = PRIME * result + this.child.hashCode();
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    @SuppressWarnings("unchecked")
    Edge<T> other = (Edge<T>) obj;
    if (!this.parent.equals(other.parent)) {
      return false;
    }
    if (!this.child.equals(other.child)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("[Edge");
    result.append(" parent:");
    result.append(this.parent);
    result.append(" child:");
    result.append(this.child);
    result.append("]");
    return result.toString();
  }

} /* ENDCLASS */
