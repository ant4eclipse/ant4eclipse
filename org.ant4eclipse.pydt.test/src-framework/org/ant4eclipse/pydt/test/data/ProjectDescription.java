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
package org.ant4eclipse.pydt.test.data;

import org.ant4eclipse.core.util.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Descriptional datastructure allowing to compare the results of the ant tasks.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProjectDescription {

  private String       _primaryprojectname;

  private String       _secondaryprojectname;

  private List<String> _internallibs;

  private List<String> _internallibsprimary;

  private List<String> _internallibssecondary;

  private List<String> _sourcefolders;

  /**
   * Initialises this datastructure.
   */
  public ProjectDescription() {
    this._primaryprojectname = null;
    this._secondaryprojectname = null;
    this._internallibs = new ArrayList<String>();
    this._sourcefolders = new ArrayList<String>();
    this._internallibsprimary = new ArrayList<String>();
    this._internallibssecondary = new ArrayList<String>();
  }

  /**
   * Returns a list of all added source folders. The pathes are workspace relative.
   * 
   * @return A list of all added source folders. Not <code>null</code>.
   */
  public String[] getSourceFolders() {
    return getSourceFolders(null);
  }

  /**
   * Returns a list of all added source folders. The pathes are workspace relative.
   * 
   * @param dirseparator
   *          The dirseparator to be used. If <code>null</code> the default {@link File#separator} will be used.
   * 
   * @return A list of all added source folders. Not <code>null</code>.
   */
  public String[] getSourceFolders(String dirseparator) {
    if (dirseparator == null) {
      dirseparator = File.separator;
    }
    String[] result = new String[this._sourcefolders.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = Utilities.replace(Utilities.replace(this._sourcefolders.get(i), "\\", "/"), "/", dirseparator);
    }
    return result;
  }

  /**
   * Adds a source folder path to this list. The path is workspace relative.
   * 
   * @param sourcefolder
   *          A workspace relative path of the source. Neither <code>null</code> nor empty.
   */
  public void addSourceFolder(String sourcefolder) {
    this._sourcefolders.add(sourcefolder);
  }

  /**
   * Returns a list of all internal libraries. The pathes are workspace relative.
   * 
   * @return A list of all internal libararies. Not <code>null</code>.
   */
  public String[] getInternalLibs() {
    return getInternalLibs(null, null);
  }

  /**
   * Returns a list of all internal libraries. The pathes are workspace relative.
   * 
   * @param dirseparator
   *          The dirseparator to be used. If <code>null</code> the default {@link File#separator} will be used.
   * @param primary
   *          <code>null</code> <=> All internal libraries. <code>Boolean.TRUE</code> <=> Only internal library for the
   *          primary project. <code>Boolean.FALSE</code> <=> Only internal library for the secondary project.
   * 
   * @return A list of all internal libararies. Not <code>null</code>.
   */
  public String[] getInternalLibs(String dirseparator, Boolean primary) {
    if (dirseparator == null) {
      dirseparator = File.separator;
    }
    List<String> libs = this._internallibs;
    if (primary != null) {
      if (primary.booleanValue()) {
        libs = this._internallibsprimary;
      } else {
        libs = this._internallibssecondary;
      }
    }
    String[] result = new String[libs.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = Utilities.replace(Utilities.replace(libs.get(i), "\\", "/"), "/", dirseparator);
    }
    return result;
  }

  /**
   * Adds an internal library declared as a workspace relative path.
   * 
   * @param internallib
   *          An internal library declared as a workspace relative path. Neither <code>null</code> nor empty.
   * @param primary
   *          <code>true</code> <=> Used for the primary project, secondary project otherwise.
   */
  public void addInternalLibrary(String internallib, boolean primary) {
    this._internallibs.add(internallib);
    if (primary) {
      this._internallibsprimary.add(internallib);
    } else {
      this._internallibssecondary.add(internallib);
    }
  }

  /**
   * Changes the primary (main) project name.
   * 
   * @param primaryprojectname
   *          The primary project name. Neither <code>null</code> nor empty.
   */
  public void setPrimaryProjectname(String primaryprojectname) {
    this._primaryprojectname = primaryprojectname;
  }

  /**
   * Returns the primary (main) project name.
   * 
   * @return The primary (main) project name. Neither <code>null</code> nor empty.
   */
  public String getPrimaryProjectname() {
    return this._primaryprojectname;
  }

  /**
   * Changes the secondary project name.
   * 
   * @param secondaryprojectname
   *          The secondary project name. Neither <code>null</code> nor empty.
   */
  public void setSecondaryProjectname(String secondaryprojectname) {
    this._secondaryprojectname = secondaryprojectname;
  }

  /**
   * Returns the secondary project name.
   * 
   * @return The secondary project name. Neither <code>null</code> nor empty.
   */
  public String getSecondaryProjectname() {
    return this._secondaryprojectname;
  }

} /* ENDCLASS */
