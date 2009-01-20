package org.ant4eclipse.platform.ant.core.task;

import java.io.File;

import org.ant4eclipse.platform.ant.core.PathComponent;
import org.ant4eclipse.platform.ant.core.delegate.PathDelegate;
import org.apache.tools.ant.types.Path;

public abstract class AbstractProjectSetPathBasedTask extends AbstractProjectSetBasedTask implements PathComponent {

  /** - */
  private final PathDelegate _pathDelegate;

  /**
   * 
   */
  public AbstractProjectSetPathBasedTask() {
    super();

    this._pathDelegate = new PathDelegate(this);
  }

  public final String getDirSeparator() {
    return this._pathDelegate.getDirSeparator();
  }

  public final String getPathSeparator() {
    return this._pathDelegate.getPathSeparator();
  }

  public final boolean isDirSeparatorSet() {
    return this._pathDelegate.isDirSeparatorSet();
  }

  public final boolean isPathSeparatorSet() {
    return this._pathDelegate.isPathSeparatorSet();
  }

  public final void setDirSeparator(String newdirseparator) {
    this._pathDelegate.setDirSeparator(newdirseparator);
  }

  public final void setPathSeparator(String newpathseparator) {
    this._pathDelegate.setPathSeparator(newpathseparator);
  }

  public final Path convertToPath(File[] entries) {
    return this._pathDelegate.convertToPath(entries);
  }

  public final String convertToString(File[] entries) {
    return this._pathDelegate.convertToString(entries);
  }

  public final Path convertToPath(File entry) {
    return this._pathDelegate.convertToPath(entry);
  }

  public final String convertToString(File entry) {
    return this._pathDelegate.convertToString(entry);
  }

  public PathDelegate getPathDelegate() {
    return this._pathDelegate;
  }
}
