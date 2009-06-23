package org.ant4eclipse.platform.ant.core.delegate;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.ant.core.PathComponent;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PathDelegate extends AbstractAntDelegate implements PathComponent {

  /** the path separator (e.g. ':' or ';') */
  private String _pathSeparator;

  /** the directory separator (e.g. '/' or '\' */
  private String _dirSeparator;

  /**
   * <p>
   * Creates a new instance of type {@link PathDelegate}.
   * </p>
   * 
   * @param component
   *          the ProjectComponent
   */
  public PathDelegate(final ProjectComponent component) {
    super(component);

    // set default separators
    this._pathSeparator = File.pathSeparator;
    this._dirSeparator = File.separator;
  }

  /**
   * {@inheritDoc}
   */
  public final void setPathSeparator(final String newpathseparator) {
    Assert.nonEmpty(newpathseparator);

    this._pathSeparator = newpathseparator;
  }

  /**
   * {@inheritDoc}
   */
  public final String getPathSeparator() {
    return (this._pathSeparator);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPathSeparatorSet() {
    return this._pathSeparator != null;
  }

  /**
   * {@inheritDoc}
   */
  public final void setDirSeparator(final String newdirseparator) {
    Assert.nonEmpty(newdirseparator);

    this._dirSeparator = newdirseparator;
  }

  /**
   * {@inheritDoc}
   */
  public final String getDirSeparator() {
    return (this._dirSeparator);
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isDirSeparatorSet() {
    return (this._dirSeparator != null);
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(final File entry) {
    return convertToString(new File[] { entry });
  }

  /**
   * {@inheritDoc}
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

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(final File entry) {
    return convertToPath(new File[] { entry });
  }

  /**
   * {@inheritDoc}
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
