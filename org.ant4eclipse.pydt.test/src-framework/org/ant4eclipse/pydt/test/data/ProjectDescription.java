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

  private List<String> _sourcefolders;

  /**
   * Initialises this datastructure.
   */
  public ProjectDescription() {
    _primaryprojectname = null;
    _secondaryprojectname = null;
    _internallibs = new ArrayList<String>();
    _sourcefolders = new ArrayList<String>();
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
    final String[] result = new String[_sourcefolders.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = Utilities.replace(Utilities.replace(_sourcefolders.get(i), "\\", "/"), "/", dirseparator);
    }
    return result;
  }

  /**
   * Adds a source folder path to this list. The path is workspace relative.
   * 
   * @param sourcefolder
   *          A workspace relative path of the source. Neither <code>null</code> nor empty.
   */
  public void addSourceFolder(final String sourcefolder) {
    _sourcefolders.add(sourcefolder);
  }

  /**
   * Returns a list of all internal libraries. The pathes are workspace relative.
   * 
   * @return A list of all internal libararies. Not <code>null</code>.
   */
  public String[] getInternalLibs() {
    return getInternalLibs(null);
  }

  /**
   * Returns a list of all internal libraries. The pathes are workspace relative.
   * 
   * @param dirseparator
   *          The dirseparator to be used. If <code>null</code> the default {@link File#separator} will be used.
   * 
   * @return A list of all internal libararies. Not <code>null</code>.
   */
  public String[] getInternalLibs(String dirseparator) {
    if (dirseparator == null) {
      dirseparator = File.separator;
    }
    final String[] result = new String[_internallibs.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = Utilities.replace(Utilities.replace(_internallibs.get(i), "\\", "/"), "/", dirseparator);
    }
    return result;
  }

  /**
   * Adds an internal library declared as a workspace relative path.
   * 
   * @param internallib
   *          An internal library declared as a workspace relative path. Neither <code>null</code> nor empty.
   */
  public void addInternalLibrary(final String internallib) {
    _internallibs.add(internallib);
  }

  /**
   * Changes the primary (main) project name.
   * 
   * @param primaryprojectname
   *          The primary project name. Neither <code>null</code> nor empty.
   */
  public void setPrimaryProjectname(final String primaryprojectname) {
    _primaryprojectname = primaryprojectname;
  }

  /**
   * Returns the primary (main) project name.
   * 
   * @return The primary (main) project name. Neither <code>null</code> nor empty.
   */
  public String getPrimaryProjectname() {
    return _primaryprojectname;
  }

  /**
   * Changes the secondary project name.
   * 
   * @param secondaryprojectname
   *          The secondary project name. Neither <code>null</code> nor empty.
   */
  public void setSecondaryProjectname(final String secondaryprojectname) {
    _secondaryprojectname = secondaryprojectname;
  }

  /**
   * Returns the secondary project name.
   * 
   * @return The secondary project name. Neither <code>null</code> nor empty.
   */
  public String getSecondaryProjectname() {
    return _secondaryprojectname;
  }

} /* ENDCLASS */
