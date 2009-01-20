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
   * <p>
   * Sets the path ID. The resolved path can be references via this ID in the ant build file.
   * </p>
   * 
   * @param id
   *          the path ID
   */
  public final void setPathId(final String id) {
    if (this._pathId == null) {
      this._pathId = id;
    }
  }

  /**
   * <p>
   * Returns the path ID for this task.
   * </p>
   * 
   * @return The path ID for this task.
   */
  public final String getPathId() {
    return this._pathId;
  }

  /**
   * <p>
   * Returns true if the path ID has been set.
   * </p>
   * 
   * @return true <=> The path ID has been set.
   */
  public final boolean isPathIdSet() {
    return this._pathId != null;
  }

  /**
   * <p>
   * Returns whether or not the path should be resolved relative to the workspace.
   * </p>
   * 
   * @return <code>true</code> if the path should be resolved relative to the workspace.
   */
  public final boolean isRelative() {
    return this._relative;
  }

  /**
   * <p>
   * Sets whether the path should be resolved relative to the workspace.
   * </p>
   * 
   * @param relative
   *          whether the path should be resolved relative to the workspace.
   */
  public final void setRelative(final boolean relative) {
    this._relative = relative;
  }

  /**
   * <p>
   * Sets the name of the property that should hold the resolved path.
   * </p>
   * 
   * @param property
   *          the name of the property that should hold the resolved path.
   */
  public final void setProperty(final String property) {
    this._property = property;
  }

  /**
   * <p>
   * Returns the name of the property that should hold the resolved path.
   * <p>
   * 
   * @return the name of the property that should hold the resolved path.
   */
  public final String getProperty() {
    return this._property;
  }

  /**
   * <p>
   * Returns true if the name of the property has been set.
   * </p>
   * 
   * @return <code>true</code> if the name of the property has been set.
   */
  public final boolean isPropertySet() {
    return this._property != null;
  }

  /**
   * <p>
   * Ensures that either a path id or a property (or both) is set. If none of both is set, a BuildException will be
   * thrown.
   * </p>
   */
  public final void requirePathIdOrPropertySet() {
    if (!isPathIdSet() && !isPropertySet()) {
      throw new BuildException("At least one of 'pathId' or 'property' has to be set!");
    }
  }

  /**
   * <p>
   * Returns the a list of resolved pathes.
   * </p>
   * 
   * @return A list of resolved pathes.
   */
  public final File[] getResolvedPath() {
    return this._resolvedPath;
  }

  /**
   * <p>
   * Sets the resolved path entries.
   * </p>
   * 
   * @param resolvedPath
   *          the resolved path entries.
   */
  public final void setResolvedPath(final File[] resolvedPath) {
    this._resolvedPath = resolvedPath;
  }

  /**
   * <p>
   * Populates the property if specified.
   * </p>
   */
  public final void populateProperty() {
    if (isPropertySet()) {
      final String resolvedpath = convertToString(getResolvedPath());
      getAntProject().setProperty(getProperty(), resolvedpath);
    }
  }

  /**
   * <p>
   * Populates the path id if specified.
   * </p>
   */
  public final void populatePathId() {
    if (isPathIdSet()) {
      final Path resolvedPath = convertToPath(getResolvedPath());
      getAntProject().addReference(getPathId(), resolvedPath);
    }
  }
}
