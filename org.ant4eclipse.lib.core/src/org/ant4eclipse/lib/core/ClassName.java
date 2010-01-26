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
package org.ant4eclipse.lib.core;

/**
 * <p>
 * Represents a <b>qualified</b> class name.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public final class ClassName {

  /** the package name */
  private String _packageName;

  /** the class name */
  private String _className;

  /**
   * <p>
   * Returns a new instance of type {@link ClassName} representing the given qualified class name.
   * </p>
   * 
   * @param qualifiedClassName
   *          The qualified class name
   * @return a ClassName instance representing this qualified class name
   */
  public static ClassName fromQualifiedClassName(String qualifiedClassName) {
    Assure.nonEmpty(qualifiedClassName);

    // split the qualified class name
    String[] splittedClassName = splitQualifiedClassName(qualifiedClassName);

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
    return this._packageName.equals("") ? this._className : this._packageName + "." + this._className;
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
    String fileName = getQualifiedClassName().replace('.', '/');
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
    String fileName = getQualifiedClassName().replace('.', '/');
    return fileName + ".java";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getQualifiedClassName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int PRIME = 31;
    int result = 1;
    result = PRIME * result + this._className.hashCode();
    result = PRIME * result + this._packageName.hashCode();
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
    ClassName other = (ClassName) obj;
    if (!this._className.equals(other._className)) {
      return false;
    }
    if (!this._packageName.equals(other._packageName)) {
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
  private static String[] splitQualifiedClassName(String qualifiedClassName) {
    Assure.nonEmpty(qualifiedClassName);

    int v = qualifiedClassName.lastIndexOf('.');
    String packageName = "";
    if (v != -1) {
      packageName = qualifiedClassName.substring(0, v);
    }
    // if ((packageName == null) || (packageName.length() < 1)) {
    // throw new IllegalStateException("Default packages not supported! Classname was:'" + qualifiedClassName + "'");
    // }

    String className = qualifiedClassName.substring(v + 1);
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
  private ClassName(String packageName, String className) {
    Assure.notNull(packageName);
    Assure.nonEmpty(className);

    this._packageName = packageName;
    this._className = className;
  }
}
