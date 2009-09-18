package org.ant4eclipse.platform.ant.core.task;

import org.ant4eclipse.platform.ant.core.PathComponent;
import org.ant4eclipse.platform.ant.core.delegate.PathDelegate;

import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * <p>
 * Abstract base class for project set based tasks that are able to resolve pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectSetPathBasedTask extends AbstractProjectSetBasedTask implements PathComponent {

  /** the path delegate */
  private PathDelegate _pathDelegate;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractProjectSetPathBasedTask}.
   * </p>
   */
  public AbstractProjectSetPathBasedTask() {
    super();

    // create the path delegate
    this._pathDelegate = new PathDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  public final String getDirSeparator() {
    return this._pathDelegate.getDirSeparator();
  }

  /**
   * {@inheritDoc}
   */
  public final String getPathSeparator() {
    return this._pathDelegate.getPathSeparator();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isDirSeparatorSet() {
    return this._pathDelegate.isDirSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPathSeparatorSet() {
    return this._pathDelegate.isPathSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void setDirSeparator(String newdirseparator) {
    this._pathDelegate.setDirSeparator(newdirseparator);
  }

  /**
   * {@inheritDoc}
   */
  public final void setPathSeparator(String newpathseparator) {
    this._pathDelegate.setPathSeparator(newpathseparator);
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File[] entries) {
    return this._pathDelegate.convertToPath(entries);
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File[] entries) {
    return this._pathDelegate.convertToString(entries);
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File entry) {
    return this._pathDelegate.convertToPath(entry);
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File entry) {
    return this._pathDelegate.convertToString(entry);
  }

  /**
   * {@inheritDoc}
   */
  public PathDelegate getPathDelegate() {
    return this._pathDelegate;
  }
}
