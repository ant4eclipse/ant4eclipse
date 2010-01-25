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
package org.ant4eclipse.ant.jdt;

import org.ant4eclipse.core.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Internal helper class to hand over some additional information to ant4eclipse's JDTCompilerAdapter. Some information
 * - e.g. access restrictions or output folders for specific source folders - can not be defined using ant's standard
 * <code>javac</code>. To allow the JDTCompilerAdapter to access and use these information, an instance of this class is
 * used.
 * </p>
 * <p>
 * Whenever ant4eclipse computes a class path for an eclipse java project, an instance of type
 * {@link EcjAdditionalCompilerArguments} is created and stored as a reference in the current ant project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EcjAdditionalCompilerArguments {

  /** maps source folders to output folders */
  private Map<File, File>      _outputFolderMap;

  /** maps output folders to source folders */
  private Map<File, Set<File>> _sourceFolderMap;

  /** maps class path entries to access restrictions */
  private Map<File, String>    _accessRestrictions;

  /** the boot class path access restrictions */
  private String               _bootClassPathAccessRestrictions;

  /**
   * <p>
   * Creates a new instance of type CompilerArguments.
   * </p>
   */
  public EcjAdditionalCompilerArguments() {
    // create the maps
    this._accessRestrictions = new HashMap<File, String>();
    this._outputFolderMap = new HashMap<File, File>();
    this._sourceFolderMap = new HashMap<File, Set<File>>();
  }

  /**
   * <p>
   * Returns <code>true</code>, if boot class path access restrictions are set.
   * </p>
   * 
   * @return <code>true</code>, if boot class path access restrictions are set.
   */
  public boolean hasBootClassPathAccessRestrictions() {
    return this._bootClassPathAccessRestrictions != null;
  }

  /**
   * <p>
   * Returns the boot class path access restrictions.
   * </p>
   * 
   * @return the boot class path access restrictions.
   */
  public String getBootClassPathAccessRestrictions() {
    return this._bootClassPathAccessRestrictions;
  }

  /**
   * <p>
   * Returns <code>true</code>, if an access restriction for the given class path entry is specified.
   * </p>
   * 
   * @param classpathentry
   *          the class path entry
   * @return <code>true</code>, if an access restriction for the given class path entry is specified.
   */
  public boolean hasAccessRestrictions(File classpathentry) {
    return this._accessRestrictions.containsKey(classpathentry);
  }

  /**
   * <p>
   * Returns the access restrictions for the given class path entry or <code>null</code> if no access restriction for
   * the given class path entry is specified.
   * </p>
   * 
   * @param classpathentry
   *          the class path entry
   * @return the access restrictions for the given class path entry or <code>null</code> if no access restriction for
   *         the given class path entry is specified.
   */
  public String getAccessRestrictions(File classpathentry) {
    return this._accessRestrictions.get(classpathentry);
  }

  /**
   * <p>
   * </p>
   * 
   * @param outputFolder
   * @return
   */
  public boolean hasSourceFoldersForOutputFolder(File outputFolder) {
    return this._sourceFolderMap.containsKey(outputFolder);
  }

  /**
   * <p>
   * </p>
   * 
   * @param outputFolder
   * @return
   */
  public File[] getSourceFoldersForOutputFolder(File outputFolder) {
    return hasSourceFoldersForOutputFolder(outputFolder) ? this._sourceFolderMap.get(outputFolder).toArray(new File[0])
        : new File[0];
  }

  /**
   * <p>
   * Returns the output folder for a specific source folder.
   * </p>
   * 
   * @param sourceFolder
   *          the source folder.
   * @return the output folder for a specific source folder.
   */
  public File getOutputFolder(File sourceFolder) {
    Assert.isDirectory(sourceFolder);

    return this._outputFolderMap.get(sourceFolder);
  }

  /**
   * <p>
   * Adds an access restriction for the given class path entry.
   * </p>
   * 
   * @param classpathentry
   *          the class path entry.
   * @param accessRestrictions
   *          an access restriction for the given class path entry.
   */
  public void addAccessRestrictions(File classpathentry, String accessRestrictions) {
    this._accessRestrictions.put(classpathentry, accessRestrictions);
  }

  /**
   * <p>
   * Adds a boot access restriction for the given class path entry.
   * </p>
   * 
   * @param bootClassPathAccessRestrictions
   *          the boot access restriction.
   */
  public void setBootClassPathAccessRestrictions(String bootClassPathAccessRestrictions) {
    Assert.nonEmpty(bootClassPathAccessRestrictions);

    this._bootClassPathAccessRestrictions = bootClassPathAccessRestrictions;
  }

  /**
   * <p>
   * Adds a source folder to the source-output-folder map.
   * </p>
   * 
   * @param sourceFolder
   *          the source folder
   * @param outputFolder
   *          the output folder
   */
  public void addOutputFolderForSourceFolder(File sourceFolder, File outputFolder) {
    this._outputFolderMap.put(sourceFolder, outputFolder);
  }

  /**
   * <p>
   * </p>
   * 
   * @param sourceFolders
   * @param outputFolder
   */
  public void addSourceFolderForOutputFolder(File outputFolder, File[] sourceFolders) {
    Assert.notNull(outputFolder);
    Assert.notNull(sourceFolders);

    // get source folder map
    Set<File> sourceFolderSet = this._sourceFolderMap.get(sourceFolders);

    // if no source folder map exists, create a new one
    if (sourceFolderSet == null) {
      sourceFolderSet = new HashSet<File>();
      this._sourceFolderMap.put(outputFolder, sourceFolderSet);
    }

    // add the source folder
    sourceFolderSet.addAll(Arrays.asList(sourceFolders));
  }
}
