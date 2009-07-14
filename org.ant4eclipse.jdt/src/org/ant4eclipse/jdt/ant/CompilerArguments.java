package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.core.Assert;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Internal helper class to hand over some additional information to ant4eclipse's JDTCompilerAdapter. Some information
 * - e.g. access restrictions or output folders for specific source folders - can not be defined using ant's standard
 * <code>javac</code>. To allow the JDTCompilerAdapter to access and use these information, an instance of this class is
 * used.
 * </p>
 * <p>
 * Whenever ant4eclipse computes a class path for an eclipse java project, an instance of type {@link CompilerArguments}
 * is created and stored as a reference in the current ant project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CompilerArguments {

  /** maps source folders to output folders */
  private final Map<File, File>   _outputFolderMap;

  /** maps class path entries to access restrictions */
  private final Map<File, String> _accessRestrictions;

  /** the compiler options */
  private Map<String, String>     _compilerOptions;

  /** the boot class path access restrictions */
  private String                  _bootClassPathAccessRestrictions;

  /**
   * <p>
   * Creates a new instance of type CompilerArguments.
   * </p>
   */
  public CompilerArguments() {
    // create the maps
    this._accessRestrictions = new HashMap<File, String>();
    this._outputFolderMap = new HashMap<File, File>();
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
  public boolean hasAccessRestrictions(final File classpathentry) {
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
  public String getAccessRestrictions(final File classpathentry) {
    return this._accessRestrictions.get(classpathentry);
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
  public File getOutputFolder(final File sourceFolder) {
    Assert.isDirectory(sourceFolder);

    return this._outputFolderMap.get(sourceFolder);
  }

  /**
   * <p>
   * Returns the compiler options.
   * </p>
   * 
   * @return the compilerOptions
   */
  public Map<String, String> getCompilerOptions() {
    return this._compilerOptions;
  }

  /**
   * <p>
   * Returns <code>true</code> if the arguments contain compiler options.
   * </p>
   * 
   * @return <code>true</code> if the arguments contain compiler options.
   */
  public boolean hasCompilerOptions() {
    return this._compilerOptions != null;
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
  public void addAccessRestrictions(final File classpathentry, final String accessRestrictions) {
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
  public void setBootClassPathAccessRestrictions(final String bootClassPathAccessRestrictions) {
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
  public void addSourceFolder(final File sourceFolder, final File outputFolder) {
    this._outputFolderMap.put(sourceFolder, outputFolder);
  }

  /**
   * <p>
   * Adds the compiler options to the {@link CompilerArguments}.
   * </p>
   * 
   * @param compilerOptions
   *          the compiler options.
   */
  public void addCompilerOptions(final Map<String, String> compilerOptions) {
    this._compilerOptions = compilerOptions;
  }
}
