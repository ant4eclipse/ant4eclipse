package org.ant4eclipse.platform.ant.core.delegate;

import java.io.File;

import org.ant4eclipse.platform.ant.core.GetPathComponent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with project pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetPathDelegate extends PathDelegate implements GetPathComponent {

  /** the id of the path */
  private String  _pathId   = null;

  /** the property */
  private String  _property = null;

  /** indicates whether the class path should be resolved relative or not */
  private boolean _relative = false;

  /** the resolved path entries */
  private File[]  _resolvedPath;

  /**
   * @param component
   */
  public GetPathDelegate(final ProjectComponent component) {
    super(component);
  }

  /**
   * {@inheritDoc}
   */
  public final void setPathId(final String id) {
    if (this._pathId == null) {
      this._pathId = id;
    }
  }

  /**
   * {@inheritDoc}
   */
  public final String getPathId() {
    return this._pathId;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPathIdSet() {
    return this._pathId != null;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isRelative() {
    return this._relative;
  }

  /**
   * {@inheritDoc}
   */
  public final void setRelative(final boolean relative) {
    this._relative = relative;
  }

  /**
   * {@inheritDoc}
   */
  public final void setProperty(final String property) {
    this._property = property;
  }

  /**
   * {@inheritDoc}
   */
  public final String getProperty() {
    return this._property;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPropertySet() {
    return this._property != null;
  }

  /**
   * {@inheritDoc}
   */
  public final void requirePathIdOrPropertySet() {
    if (!isPathIdSet() && !isPropertySet()) {
      throw new BuildException("At least one of 'pathId' or 'property' has to be set!");
    }
  }

  /**
   * {@inheritDoc}
   */
  public final File[] getResolvedPath() {
    return this._resolvedPath;
  }

  /**
   * {@inheritDoc}
   */
  public final void setResolvedPath(final File[] resolvedPath) {
    this._resolvedPath = resolvedPath;
  }

  /**
   * {@inheritDoc}
   */
  public final void populateProperty() {
    if (isPropertySet()) {
      final String resolvedpath = convertToString(getResolvedPath());
      getAntProject().setProperty(getProperty(), resolvedpath);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void populatePathId() {
    if (isPathIdSet()) {
      final Path resolvedPath = convertToPath(getResolvedPath());
      getAntProject().addReference(getPathId(), resolvedPath);
    }
  }
}
