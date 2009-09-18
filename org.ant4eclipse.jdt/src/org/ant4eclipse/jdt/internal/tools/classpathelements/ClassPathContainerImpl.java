package org.ant4eclipse.jdt.internal.tools.classpathelements;

import org.ant4eclipse.jdt.tools.classpathelements.ClassPathContainer;

import java.io.File;

/**
 * <p>
 * Implementation of {@link ClassPathContainer}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassPathContainerImpl implements ClassPathContainer {

  /** the name of the class path container */
  private String _name;

  /** the path entries of this class path container */
  private File[] _pathEntries;

  /**
   * <p>
   * Creates a new instance of type {@link ClassPathContainerImpl}.
   * </p>
   * 
   * @param name
   *          the name of the class path container
   * @param pathEntries
   *          the path entries
   */
  public ClassPathContainerImpl(String name, File[] pathEntries) {
    super();

    this._name = name;
    this._pathEntries = pathEntries;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return this._name;
  }

  /**
   * {@inheritDoc}
   */
  public File[] getPathEntries() {
    return this._pathEntries;
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
    ClassPathContainerImpl castedObj = (ClassPathContainerImpl) o;
    return ((this._name == null ? castedObj._name == null : this._name.equals(castedObj._name)) && java.util.Arrays
        .equals(this._pathEntries, castedObj._pathEntries));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (this._name == null ? 0 : this._name.hashCode());
    for (int i0 = 0; this._pathEntries != null && i0 < this._pathEntries.length; i0++) {
      hashCode = 31 * hashCode + (this._pathEntries == null ? 0 : this._pathEntries[i0].hashCode());
    }
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ClassPathContainerImpl:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(" { ");
    for (int i0 = 0; this._pathEntries != null && i0 < this._pathEntries.length; i0++) {
      buffer.append(" _pathEntries[" + i0 + "]: ");
      buffer.append(this._pathEntries[i0]);
    }
    buffer.append(" } ");
    buffer.append("]");
    return buffer.toString();
  }
}
