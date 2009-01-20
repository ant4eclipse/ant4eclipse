package org.ant4eclipse.platform.ant.core;

import java.io.File;

import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface PathComponent {

  /**
   * Changes the path separator for this task.
   * 
   * @param newpathseparator
   *          The new path separator.
   */
  public void setPathSeparator(final String newpathseparator);

  /**
   * Returns the currently used path separator.
   * 
   * @return The currently used path separator.
   */
  public String getPathSeparator();

  /**
   * <p>
   * Returns <code>true</code> if the path separator has been set.
   * </p>
   * 
   * @return <code>true</code> if the path separator has been set.
   */
  public boolean isPathSeparatorSet();

  /**
   * Changes the current directory separator.
   * 
   * @param newdirseparator
   *          The new directory separator.
   */
  public void setDirSeparator(String newdirseparator);

  /**
   * Returns the currently used directory separator.
   * 
   * @return The currently used directory separator.
   */
  public String getDirSeparator();

  /**
   * @return
   */
  public boolean isDirSeparatorSet();

  public String convertToString(File entry);

  /**
   * Converts an array of ResolvedClasspathEntry-objects to a String. The entries are separated by the separator char.
   * 
   * This method can be used to convert a complete eclipse classpath to a String, which could be used in an ant
   * property.
   * 
   * @param entries
   *          The resolved pathes that shall be converted.
   * @param pathseparator
   *          The separator which shall be used for each path element.
   * @param dirseparator
   *          The separator which shall be used for each directory element.
   * @param project
   *          The associated project.
   * 
   * @return A String containing the pathes.
   */
  public String convertToString(File[] entries);

  public Path convertToPath(File entry);

  /**
   * Converts the supplied resolved pathes to a Path instance.
   * 
   * @param entries
   *          The resolved pathes that shall be converted.
   * @param project
   *          The associated project.
   * 
   * @return The Path instance containing the supplied pathes.
   */
  public Path convertToPath(File[] entries);
}
