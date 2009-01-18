package org.ant4eclipse.platform.ant.delegate;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Utilities;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with project pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectPathDelegate extends ProjectDelegate {

  /** the path separator (e.g. ':' or ';') */
  private String _pathSeparator;

  /** the directory separator (e.g. '/' or '\' */
  private String _dirSeparator;

  /**
   * <p>
   * </p>
   * 
   * @param component
   */
  public ProjectPathDelegate(final ProjectComponent component) {
    super(component);

    this._pathSeparator = File.pathSeparator;
    this._dirSeparator = File.separator;
  }

  /**
   * Changes the path separator for this task.
   * 
   * @param newpathseparator
   *          The new path separator.
   */
  public final void setPathSeparator(final String newpathseparator) {
    Assert.nonEmpty(newpathseparator);

    this._pathSeparator = newpathseparator;
  }

  /**
   * Returns the currently used path separator.
   * 
   * @return The currently used path separator.
   */
  public final String getPathSeparator() {
    return (this._pathSeparator);
  }

  /**
   * <p>
   * Returns <code>true</code> if the path separator has been set.
   * </p>
   * 
   * @return <code>true</code> if the path separator has been set.
   */
  public final boolean isPathSeparatorSet() {
    return this._pathSeparator != null;
  }

  /**
   * Changes the current directory separator.
   * 
   * @param newdirseparator
   *          The new directory separator.
   */
  public final void setDirSeparator(final String newdirseparator) {
    Assert.nonEmpty(newdirseparator);

    this._dirSeparator = newdirseparator;
  }

  /**
   * Returns the currently used directory separator.
   * 
   * @return The currently used directory separator.
   */
  public final String getDirSeparator() {
    return (this._dirSeparator);
  }

  public final boolean isDirSeparatorset() {
    return (this._dirSeparator != null);
  }

  public final String convertToString(final File entry) {
    return convertToString(new File[] { entry });
  }

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
  public final String convertToString(final File[] entries) {
    Assert.notNull(entries);

    // convert Files to String
    final List<String> entriesAsString = new LinkedList<String>();
    for (final File entry : entries) {
      final String path = entry.getPath();
      if (!entriesAsString.contains(path)) {
        entriesAsString.add(path);
      }
    }

    // replace path and directory separator
    final StringBuilder buffer = new StringBuilder();
    final Iterator<String> iterator = entriesAsString.iterator();
    while (iterator.hasNext()) {
      String path = iterator.next().replace('\\', '/');
      path = Utilities.replace(path, '/', this._dirSeparator);
      buffer.append(path);
      if (iterator.hasNext()) {
        buffer.append(this._pathSeparator);
      }
    }

    // return result
    return buffer.toString();
  }

  public final Path convertToPath(final File entry) {
    return convertToPath(new File[] { entry });
  }

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
  public final Path convertToPath(final File[] entries) {
    Assert.notNull(entries);
    final Path antPath = new Path(getAntProject());
    for (final File entry : entries) {
      // TODO getPath() vs. getAbsolutePath()
      antPath.append(new Path(getAntProject(), entry.getPath()));
    }
    return antPath;
  }
}
