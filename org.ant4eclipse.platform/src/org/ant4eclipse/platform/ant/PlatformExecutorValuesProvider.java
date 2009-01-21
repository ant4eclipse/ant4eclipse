package org.ant4eclipse.platform.ant;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.PathComponent;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PlatformExecutorValuesProvider {

  /** the key for the project name property */
  public static final String  PROJECT_NAME           = "project.name";

  /** the key for the project directory property */
  public static final String  PROJECT_DIRECTORY      = "project.directory";

  /** the key for the project directory path */
  public static final String  PROJECT_DIRECTORY_PATH = "project.directory.path";

  /** the internally used path component */
  private final PathComponent _pathComponent;

  /**
   * <p>
   * The path delegate.
   * </p>
   * 
   * @param pathComponent
   */
  public PlatformExecutorValuesProvider(PathComponent pathComponent) {
    Assert.notNull(pathComponent);

    this._pathComponent = pathComponent;
  }

  /**
   * <p>
   * </p>
   * 
   * @param eclipseProject
   * @param executionValues
   */
  public void provideExecutorValues(EclipseProject eclipseProject, MacroExecutionValues executionValues) {
    Assert.notNull(eclipseProject);
    Assert.notNull(executionValues);

    // create scoped properties
    executionValues.getProperties().put(PlatformExecutorValuesProvider.PROJECT_NAME, eclipseProject.getSpecifiedName());
    executionValues.getProperties().put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY,
        this._pathComponent.convertToString(eclipseProject.getFolder()));

    // create scoped references
    executionValues.getReferences().put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY_PATH,
        this._pathComponent.convertToPath(eclipseProject.getFolder()));
  }

}
