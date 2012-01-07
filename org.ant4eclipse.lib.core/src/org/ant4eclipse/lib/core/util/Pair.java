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
package org.ant4eclipse.lib.core.util;

/**
 * <p>
 * Implements a pair.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Pair<T,U> {

  /** the first thing of this pair */
  private T _first;

  /** the second thing of this pair */
  private U _second;

  /**
   * <p>
   * Creates a new instance of type {@link Pair}.
   * </p>
   * 
   * @param first
   *          the first thing of this pair
   * @param second
   *          the second thing of this pair
   */
  public Pair( T first, U second ) {
    _first = first;
    _second = second;
  }

  /**
   * <p>
   * Returns the first thing of this pair.
   * </p>
   * 
   * @return the first thing of this pair.
   */
  public T getFirst() {
    return _first;
  }

  /**
   * <p>
   * Returns the second thing of this pair.
   * </p>
   * 
   * @return the second thing of this pair.
   */
  public U getSecond() {
    return _second;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    // compute the hash code
    int result = _first == null ? 0 : _first.hashCode();
    result = 31 * result + (_second == null ? 0 : _second.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public boolean equals( Object other ) {
    if( this == other ) {
      return true;
    }
    if( other == null || !(getClass().isInstance( other )) ) {
      return false;
    }
    Pair<T,U> otherPair = getClass().cast( other );
    return Utilities.equals( _first, otherPair._first ) && Utilities.equals( _second, otherPair._second );
  }

} /* ENDCLASS */
