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

import org.ant4eclipse.lib.core.Assure;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Internal helper class to hand over some additional information to ant4eclipse's EcjCompilerAdapter. Some information
 * - e.g. access restrictions or output folders for specific source folders - can not be defined using ant's standard
 * <code>javac</code>. To allow the EcjCompilerAdapter to access and use these information, an instance of this class is
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

  private Path                _sourceFilteredFilesetPath;

  /** maps source folders to output folders */
  private Map<File,File>      _outputFolderMap;

  /** maps output folders to source folders */
  private Map<File,Set<File>> _sourceFolderMap;

  /** maps class path entries to access restrictions */
  private Map<File,String>    _accessRestrictions;

  /** the boot class path access restrictions */
  private String              _bootClassPathAccessRestrictions;

  /**
   * <p>
   * Creates a new instance of type CompilerArguments.
   * </p>
   */
  public EcjAdditionalCompilerArguments() {
    // create the maps
    _accessRestrictions = new HashMap<File,String>();
    _outputFolderMap = new HashMap<File,File>();
    _sourceFolderMap = new HashMap<File,Set<File>>();
  }

  /**
   * <p>
   * Returns <code>true</code>, if boot class path access restrictions are set.
   * </p>
   * 
   * @return <code>true</code>, if boot class path access restrictions are set.
   */
  public boolean hasBootClassPathAccessRestrictions() {
    return _bootClassPathAccessRestrictions != null;
  }

  /**
   * <p>
   * Returns the boot class path access restrictions.
   * </p>
   * 
   * @return the boot class path access restrictions.
   */
  public String getBootClassPathAccessRestrictions() {
    return _bootClassPathAccessRestrictions;
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
  public boolean hasAccessRestrictions( File classpathentry ) {
    return _accessRestrictions.containsKey( classpathentry );
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
  public String getAccessRestrictions( File classpathentry ) {
    return _accessRestrictions.get( classpathentry );
  }

  /**
   * <p>
   * </p>
   * 
   * @param outputFolder
   * @return
   */
  public boolean hasSourceFoldersForOutputFolder( File outputFolder ) {
    return _sourceFolderMap.containsKey( outputFolder );
  }

  /**
   * <p>
   * </p>
   * 
   * @param outputFolder
   * @return
   */
  public List<File> getSourceFoldersForOutputFolder( File outputFolder ) {
    if( hasSourceFoldersForOutputFolder( outputFolder ) ) {
      return new ArrayList<File>( _sourceFolderMap.get( outputFolder ) );
    } else {
      return new ArrayList<File>();
    }
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
  public File getOutputFolder( File sourceFolder ) {
    Assure.isDirectory( "sourceFolder", sourceFolder );
    return _outputFolderMap.get( sourceFolder );
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
  public void addAccessRestrictions( File classpathentry, String accessRestrictions ) {
    _accessRestrictions.put( classpathentry, accessRestrictions );
  }

  /**
   * <p>
   * Adds a boot access restriction for the given class path entry.
   * </p>
   * 
   * @param bootClassPathAccessRestrictions
   *          the boot access restriction.
   */
  public void setBootClassPathAccessRestrictions( String bootClassPathAccessRestrictions ) {
    Assure.nonEmpty( "bootClassPathAccessRestrictions", bootClassPathAccessRestrictions );
    _bootClassPathAccessRestrictions = bootClassPathAccessRestrictions;
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
  public void addOutputFolderForSourceFolder( File sourceFolder, File outputFolder ) {
    _outputFolderMap.put( sourceFolder, outputFolder );
  }

  /**
   * <p>
   * </p>
   * 
   * @param sourceFolders
   * @param outputFolder
   */
  public void addSourceFolderForOutputFolder( File outputFolder, List<File> sourceFolders ) {
    Assure.notNull( "outputFolder", outputFolder );
    Assure.notNull( "sourceFolders", sourceFolders );

    // get source folder map
    Set<File> sourceFolderSet = _sourceFolderMap.get( outputFolder );

    // if no source folder map exists, create a new one
    if( sourceFolderSet == null ) {
      sourceFolderSet = new HashSet<File>();
      _sourceFolderMap.put( outputFolder, sourceFolderSet );
    }

    // add the source folder
    sourceFolderSet.addAll( sourceFolders );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean hasSourceFilteredFilesetPath() {
    return _sourceFilteredFilesetPath != null;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Path getSourceFilteredFilesetPath() {
    return _sourceFilteredFilesetPath;
  }

  /**
   * <p>
   * </p>
   * 
   * @param sourceFilteredFilesetPath
   */
  public void setSourceFilteredFilesetPath( Path sourceFilteredFilesetPath ) {
    _sourceFilteredFilesetPath = sourceFilteredFilesetPath;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "EcjAdditionalCompilerArguments [_sourceFolderMap=" + _sourceFolderMap + ", _outputFolderMap="
        + _outputFolderMap + ", _bootClassPathAccessRestrictions=" + _bootClassPathAccessRestrictions
        + ", _accessRestrictions=" + _accessRestrictions + ", _sourceFilteredFilesetPath="
        + _sourceFilteredFilesetPath + "]";
  }
  
} /* ENDCLASS */
