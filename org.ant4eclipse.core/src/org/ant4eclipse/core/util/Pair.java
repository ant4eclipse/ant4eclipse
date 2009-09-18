package org.ant4eclipse.core.util;

/**
 * <p>
 * Implements a pair.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <T>
 * @param <U>
 */
public class Pair<T, U> {

  /** the first thing of this pair */
  private T             _first;

  /** the second thing of this pair */
  private U             _second;

  /** the hash code */
  private transient int _hash;

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
  public Pair(T first, U second) {

    // set the elements
    this._first = first;
    this._second = second;

    // compute the hash code
    this._hash = (this._first == null ? 0 : this._first.hashCode() * 31)
        + (this._second == null ? 0 : this._second.hashCode());
  }

  /**
   * <p>
   * Returns the first thing of this pair.
   * </p>
   * 
   * @return the first thing of this pair.
   */
  public T getFirst() {
    return this._first;
  }

  /**
   * <p>
   * Returns the second thing of this pair.
   * </p>
   * 
   * @return the second thing of this pair.
   */
  public U getSecond() {
    return this._second;
  }

  @Override
  public int hashCode() {
    return this._hash;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || !(getClass().isInstance(other))) {
      return false;
    }

    Pair<T, U> otherPair = getClass().cast(other);

    return (this._first == null ? otherPair._first == null : this._first.equals(otherPair._first))
        && (this._second == null ? otherPair._second == null : this._second.equals(otherPair._second));
  }
}