package org.ant4eclipse.platform.ant.core;

import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface PathComponent {

  /**
   * Sets the path separator for this task.
   * 
   * @param newpathseparator
   *          The new path separator.
   */
  void setPathSeparator(String newpathseparator);

  /**
   * Returns the currently used path separator.
   * 
   * @return The currently used path separator.
   */
  String getPathSeparator();

  /**
   * <p>
   * Returns <code>true</code> if the path separator has been set.
   * </p>
   * 
   * @return <code>true</code> if the path separator has been set.
   */
  boolean isPathSeparatorSet();

  /**
   * <p>
   * Sets the current directory separator.
   * </p>
   * 
   * @param newdirseparator
   *          The new directory separator.
   */
  void setDirSeparator(String newdirseparator);

  /**
   * <p>
   * Returns the currently used directory separator.
   * </p>
   * 
   * @return the currently used directory separator.
   */
  String getDirSeparator();

  /**
   * <p>
   * Returns <code>true</code> if the directory separator has been set.
   * </p>
   * 
   * @return <code>true</code> if the directory separator has been set.
   */
  boolean isDirSeparatorSet();

  /**
   * <p>
   * Converts a {@link File} to a String.
   * </p>
   * <p>
   * This method can be used to convert an eclipse class path to a String, which could be used in an ant property.
   * </p>
   * 
   * @param entry
   *          the file
   * @return a string
   */
  String convertToString(File entry);

  /**
   * <p>
   * Converts an array of {@link File Files} to a String.
   * </p>
   * <p>
   * This method can be used to convert an eclipse class path to a String, which could be used in an ant property.
   * </p>
   * 
   * @param entries
   *          the file array
   * @return a string
   */
  String convertToString(File[] entries);

  /**
   * <p>
   * Converts a{@link File} to an ant path.
   * </p>
   * 
   * @param entry
   *          the file
   * @return a ant path
   */
  Path convertToPath(File entry);

  /**
   * <p>
   * Converts an array of {@link File Files} to an ant path.
   * </p>
   * 
   * @param entries
   *          the file array
   * @return a ant path
   */
  Path convertToPath(File[] entries);
}
