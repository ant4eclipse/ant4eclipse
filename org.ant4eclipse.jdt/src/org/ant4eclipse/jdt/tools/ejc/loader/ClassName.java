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
package org.ant4eclipse.jdt.tools.ejc.loader;

import org.ant4eclipse.core.Assert;

/**
 * <p>
 * Represents a <b>qualified</b> class name.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public final class ClassName {

  /** the package name */
  private final String _packageName;

  /** the class name */
  private final String _className;

  /**
   * <p>
   * Returns a new instance of type {@link ClassName} representing the given qualified class name.
   * </p>
   * 
   * @param qualifiedClassName
   *          The qualified class name
   * @return a ClassName instance representing this qualified class name
   */
  public static ClassName fromQualifiedClassName(final String qualifiedClassName) {
    Assert.nonEmpty(qualifiedClassName);

    // split the qualified class name
    final String[] splittedClassName = splitQualifiedClassName(qualifiedClassName);

    // create new instance
    return new ClassName(splittedClassName[0], splittedClassName[1]);
  }

  /**
   * <p>
   * Returns the qualified name of this class as a java type identifier (e.g. <code>foo.bar.Bazz</code>).
   * </p>
   * 
   * @return the qualified class name. Never null.
   */
  public String getQualifiedClassName() {
    return this._packageName + "." + this._className;
  }

  /**
   * <p>
   * Returns the name of this class without package (e.g. <code>Bazz</code>).
   * </p>
   * 
   * @return Name of this class. Never null.
   */
  public String getClassName() {
    return this._className;
  }

  /**
   * <p>
   * Returns the package name of this class (e.g. <code>foo.bar</code>).
   * </p>
   * 
   * @return Package name of this class. Never null.
   */
  public String getPackageName() {
    return this._packageName;
  }

  /**
   * <p>
   * Returns this package as a directory name (e.g. <code>foo/bar</code>).
   * </p>
   * 
   * @return this package as a directory name. Never null.
   */
  public String getPackageAsDirectoryName() {
    return getPackageName().replace('.', '/');
  }

  /**
   * <p>
   * Returns this class name as a classname including the package directory structure and the ".class" postfix. (e.g.
   * <code>foo/bar/Bazz.class</code>.
   * </p>
   * 
   * @return this class name as a file name
   */
  public String asClassFileName() {
    final String fileName = getQualifiedClassName().replace('.', '/');
    return fileName + ".class";
  }

  /**
   * <p>
   * Returns this class name as a classname including the package directory structure and the ".java" ending (e.g.
   * <code>foo/bar/Bazz.java</code>
   * </p>
   * 
   * @return this class name as a file name
   */
  public String asSourceFileName() {
    final String fileName = getQualifiedClassName().replace('.', '/');
    return fileName + ".java";
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getQualifiedClassName();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((this._className == null) ? 0 : this._className.hashCode());
    result = PRIME * result + ((this._packageName == null) ? 0 : this._packageName.hashCode());
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ClassName other = (ClassName) obj;
    if (this._className == null) {
      if (other._className != null) {
        return false;
      }
    } else if (!this._className.equals(other._className)) {
      return false;
    }
    if (this._packageName == null) {
      if (other._packageName != null) {
        return false;
      }
    } else if (!this._packageName.equals(other._packageName)) {
      return false;
    }
    return true;
  }

  /**
   * <p>
   * Splits the qualified class name in package name and class name
   * </p>
   * 
   * @return an array with exactly two items: {package name, class name}
   */
  private static String[] splitQualifiedClassName(final String qualifiedClassName) {
    Assert.nonEmpty(qualifiedClassName);

    final int v = qualifiedClassName.lastIndexOf('.');
    String packageName = null;
    if (v != -1) {
      packageName = qualifiedClassName.substring(0, v);
    }
    if ((packageName == null) || (packageName.length() < 1)) {
      throw new IllegalStateException("Default packages not supported! Classname was:'" + qualifiedClassName + "'");
    }

    final String className = qualifiedClassName.substring(v + 1);
    return new String[] { packageName, className };
  }

  /**
   * <p>
   * Creates a new instance of type {@link ClassName}.
   * </p>
   * 
   * @param packageName
   *          the package name.
   * @param className
   *          the class name.
   */
  private ClassName(final String packageName, final String className) {
    Assert.nonEmpty(packageName);
    Assert.nonEmpty(className);

    this._packageName = packageName;
    this._className = className;
  }
}
