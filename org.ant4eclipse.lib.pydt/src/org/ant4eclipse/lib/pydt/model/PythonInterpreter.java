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
package org.ant4eclipse.lib.pydt.model;

import org.ant4eclipse.lib.core.Assure;

import java.io.File;

/**
 * Datastructure used to simply describe the known Python interpreters.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonInterpreter implements Comparable<PythonInterpreter> {

  private static final String[] EXESUFFICES = new String[] { "", ".exe", ".bat", ".sh" };

  private String                _name;

  private String[]              _executables;

  /**
   * Sets up this datastructure used for python interpreters.
   * 
   * @param name
   *          The name of the python interpreter. Neither <code>null</code> nor empty.
   * @param executables
   *          The supported executable names. Not <code>null</code>. Must be sorted lexicographically and each element
   *          is supposed to be neither <code>null</code> nor empty.
   */
  public PythonInterpreter(String name, String[] executables) {
    Assure.paramNotNull("name", name);
    Assure.paramNotNull("executables", executables);
    this._name = name;
    this._executables = executables;
  }

  /**
   * Returns the name associated with this interpreter.
   * 
   * @return The name associated with this interpreter.
   */
  public String getName() {
    return this._name;
  }

  /**
   * Looks for the python interpreter executable within the supplied installation directory.
   * 
   * @param directory
   *          The python installation directory. Not <code>null</code>.
   * 
   * @return The location of the python executable or <code>null</code>.
   */
  public File lookup(File directory) {
    Assure.paramNotNull("directory", directory);
    for (String exename : this._executables) {
      for (String suffix : EXESUFFICES) {
        File candidate = new File(directory, exename + suffix);
        if (candidate.isFile()) {
          // found a match
          return candidate;
        }
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (object.getClass() != getClass()) {
      return false;
    }
    PythonInterpreter other = (PythonInterpreter) object;
    if (!this._name.equals(other._name)) {
      return false;
    }
    if (this._executables.length != other._executables.length) {
      return false;
    }
    for (int i = 0; i < this._executables.length; i++) {
      if (!this._executables[i].equals(other._executables)) {
        return false;
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = this._name.hashCode();
    for (String executable : this._executables) {
      result = result * 31 + executable.hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[PythonInterpreter:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(", _executables: {");
    if (this._executables.length > 0) {
      buffer.append(this._executables[0]);
      for (int i = 1; i < this._executables.length; i++) {
        buffer.append(", ");
        buffer.append(this._executables[i]);
      }
    }
    buffer.append("}]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  public int compareTo(PythonInterpreter other) {
    return this._name.compareTo(other._name);
  }

} /* ENDCLASS */
