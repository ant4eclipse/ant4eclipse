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
package org.ant4eclipse.lib.jdt.model.project;

import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.util.List;

/**
 * <p>
 * Implements the java project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface JavaProjectRole extends ProjectRole {

  /** the java nature id */
  String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

  /**
   * <p>
   * Returns whether class path entries are set or not.
   * </p>
   * 
   * @return Returns whether class path entries are set or not.
   */
  boolean hasRawClasspathEntries();

  /**
   * <p>
   * Returns the eclipse class path entries.
   * </p>
   * 
   * @return returns the eclipse class path entries.
   */
  List<RawClasspathEntry> getRawClasspathEntries();

  /**
   * <p>
   * Returns a list of EclipseClasspathEntry of a specific kind.
   * </p>
   * 
   * @param entrykind
   *          The kind of the desired entries.
   * 
   * @return A list of entries providing entries of the desired type.
   */
  List<RawClasspathEntry> getRawClasspathEntries( int entrykind );

  /**
   * <p>
   * Returns the java runtime for this eclipse project or null if no runtime specified or if no such java runtime has
   * been registered.
   * </p>
   * 
   * @return the java runtime for this eclipse project or null if no runtime specified or if no such java runtime has
   *         been registered.
   */
  JavaRuntime getJavaRuntime();

  /**
   * <p>
   * Returns the {@link JavaProfile}.
   * </p>
   * 
   * @return the {@link JavaProfile}.
   */
  JavaProfile getJavaProfile();

  /**
   * <p>
   * Returns a map with the compiler options or <code>null</code> if no such options exist.
   * </p>
   * 
   * @return a map with the compiler options or <code>null</code> if no such options exist.
   */
  StringMap getCompilerOptions();

  /**
   * <p>
   * Returns the source folders for this project.
   * </p>
   * 
   * @return the source folders for this project.
   */
  List<String> getSourceFolders();

  /**
   * <p>
   * </p>
   * 
   * @param sourceFolder
   * @return
   */
  String getIncludePatternsForSourceFolder( String sourceFolder );

  /**
   * <p>
   * </p>
   * 
   * @param sourceFolder
   * @return
   */
  String getExcludePatternsForSourceFolder( String sourceFolder );

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  boolean hasExcludeOrIncludeFiltersForSourceFolders();

  /**
   * <p>
   * Returns the output folders for this project.
   * </p>
   * 
   * @return the output folders for this project.
   */
  List<String> getAllOutputFolders();

  /**
   * <p>
   * Returns the output folder for a specific source folder.
   * </p>
   * 
   * @param sourceFolder
   *          the source folder
   * @return the output folder for a specific source folder.
   */
  String getOutputFolderForSourceFolder( String sourceFolder );

  /**
   * <p>
   * Returns the default output folder.
   * </p>
   * 
   * @return the default output folder
   */
  String getDefaultOutputFolder();

} /* ENDINTERFACE */
