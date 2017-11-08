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

  private String _qualifiedName;

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
  private ClassName(String qualifiedClassName) {

    this._qualifiedName = qualifiedClassName;
    this._packageName = "";
    this._className = qualifiedClassName;

    int v = qualifiedClassName.lastIndexOf('.');
    if (v != -1) {
      this._packageName = qualifiedClassName.substring(0, v);
      this._className = qualifiedClassName.substring(v + 1);
    }

  }

  /**
   * <p>
   * Returns the qualified name of this class as a java type identifier (e.g. <code>foo.bar.Bazz</code>).
   * </p>
   * 
   * @return the qualified class name. Neither <code>null</code> nor empty.
   */
  public String getQualifiedClassName() {
    return this._qualifiedName;
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
   * Returns the Name of the outermost class. If this is not an inner class, the actual classname is returned
   *
   * @return
   */
  public String getOuterClassName() {
    int v = this._className.indexOf('$');
    final String outerClassName = v == -1 ? this._className : this._className.substring(0, v);
    return outerClassName;

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
    String directoryName = getPackageAsDirectoryName();
    if (!directoryName.isEmpty()) {
      directoryName = directoryName + "/";
    }
    String fileName = directoryName + getOuterClassName() + ".java";
    return fileName;
  }

  // TODO
  public String asSimpleSourceFileName() {
    return getOuterClassName() + ".java";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this._qualifiedName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return this._qualifiedName.hashCode();
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
    return this._qualifiedName.equals(other._qualifiedName);
  }

  /**
   * <p>
   * Returns a new instance of type {@link ClassName} representing the given qualified class name.
   * </p>
   * 
   * @todo [10-Dec-2009:KASI] Personally I would say that the constructor should be preferred instead of this static
   *       method in order to get rid of this method.
   * 
   * @param qualifiedClassName
   *          The qualified class name
   * @return a ClassName instance representing this qualified class name
   */
  public static final ClassName fromQualifiedClassName(String qualifiedClassName) {
    Assure.nonEmpty("qualifiedClassName", qualifiedClassName);
    return new ClassName(qualifiedClassName);
  }

} /* ENDCLASS */
