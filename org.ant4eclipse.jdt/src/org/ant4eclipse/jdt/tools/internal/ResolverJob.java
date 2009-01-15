package org.ant4eclipse.jdt.tools.internal;

import java.util.Properties;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

public class ResolverJob {

  /** the root eclipse project */
  private final EclipseProject _rootProject;

  /** the workspace that contains all projects */
  private final Workspace      _workspace;

  /** indicates whether the class path should be resolved relative or absolute */
  private final boolean        _relative;

  /** indicates whether the class path is a runtime class path or not */
  private final boolean        _runtimeClasspath;

  private final Properties     _properties;

  /**
   * @param rootProject
   * @param workspace
   * @param relative
   * @param runtime
   */
  public ResolverJob(final EclipseProject rootProject, final Workspace workspace, final boolean relative,
      final boolean runtime, final Properties properties) {

    this._rootProject = rootProject;
    this._workspace = workspace;
    this._relative = relative;
    this._runtimeClasspath = runtime;
    this._properties = properties;
  }

  /**
   * @return the rootProject
   */
  public final EclipseProject getRootProject() {
    return this._rootProject;
  }

  /**
   * @return the workspace
   */
  public final Workspace getWorkspace() {
    return this._workspace;
  }

  /**
   * @return the relative
   */
  public final boolean isRelative() {
    return this._relative;
  }

  /**
   * @return the runtimeClasspath
   */
  public final boolean isRuntimeClasspath() {
    return this._runtimeClasspath;
  }

  /**
   * @return the properties
   */
  public final Properties getProperties() {
    return this._properties;
  }
}
