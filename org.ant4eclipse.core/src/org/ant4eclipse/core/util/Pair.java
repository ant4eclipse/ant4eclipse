package org.ant4eclipse.core.util;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <T>
 * @param <U>
 */
public class Pair<T, U> {

  /** - */
  private final T             _first;

  /** - */
  private final U             _second;

  /** - */
  private transient final int _hash;

  public Pair(T first, U second) {

    this._first = first;

    this._second = second;

    this._hash = (this._first == null ? 0 : this._first.hashCode() * 31)
        + (this._second == null ? 0 : this._second.hashCode());
  }

  public T getFirst() {
    return this._first;
  }

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