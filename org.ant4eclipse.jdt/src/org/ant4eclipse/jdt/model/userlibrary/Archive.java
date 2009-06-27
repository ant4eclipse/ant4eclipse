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
package org.ant4eclipse.jdt.model.userlibrary;

import java.io.File;

import org.ant4eclipse.core.Assert;

/**
 * Simple library entry within the user library configuration file.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class Archive {

  /** the path */
  private File   _path;

  /** the javadoc */
  private String _javadoc;

  /** the source */
  private File   _source;

  /**
   * Creates an archive entry provding some infos regarding a classpath entry.
   * 
   * @param path
   *          The location of the classes. Maybe a directory or a Jar.
   */
  public Archive(File path) {
    this(path, null, null);
  }

  /**
   * Creates an archive entry provding some infos regarding a classpath entry.
   * 
   * @param path
   *          The location of the classes. Maybe a directory or a Jar.
   * @param source
   *          The location of corresponding sources.
   * @param javadoc
   *          The location of the javadocs as an url.
   */
  public Archive(File path, File source, String javadoc) {
    Assert.exists(path);
    _path = path;
    setSource(source);
    setJavaDoc(javadoc);
  }

  /**
   * Changes the source entry for this archive.
   * 
   * @param newsource
   *          The new source entry for this archive.
   */
  public void setSource(File newsource) {
    // TODO: Should we log this?
    // if (newsource != null && !newsource.exists()) {
    // LoggerFactory.instance().getLogger().warn(
    // "Source '" + newsource + "' of archive '" + _path
    // + "' does not exist!");
    // }

    _source = newsource;
  }

  /**
   * Changes the javadoc entry for this archive.
   * 
   * @param newjavadoc
   *          The new javadoc entry.
   */
  public void setJavaDoc(String newjavadoc) {
    // TODO: Should we throw a exceptiion here?
    // if (newjavadoc != null) {
    // Assert.assertTrue(newjavadoc.length() > 0,
    // "javadoc.length() > 0 has to be true");
    // }

    _javadoc = newjavadoc;
  }

  /**
   * Returns the location of the classes. Maybe a directory or a Jar.
   * 
   * @return The location of the classes. Maybe a directory or a Jar.
   */
  public File getPath() {
    return (_path);
  }

  /**
   * Returns the location of the sources.
   * 
   * @return The location of the sources.
   */
  public File getSource() {
    return (_source);
  }

  /**
   * Returns the location of the java docs.
   * 
   * @return The location of the java docs.
   */
  public String getJavaDoc() {
    return (_javadoc);
  }

} /* ENDCLASS */