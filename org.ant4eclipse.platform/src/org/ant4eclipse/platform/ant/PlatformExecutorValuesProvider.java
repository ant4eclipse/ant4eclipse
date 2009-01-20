package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.delegate.PathDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;

public class PlatformExecutorValuesProvider {

  public static final String PROJECT_NAME           = "project.name";

  public static final String PROJECT_DIRECTORY      = "project.directory";

  public static final String PROJECT_DIRECTORY_PATH = "project.directory.path";

  private final PathDelegate _pathDelegate;

  public PlatformExecutorValuesProvider(PathDelegate pathDelegate) {

    this._pathDelegate = pathDelegate;
  }

  public MacroExecutionValues getExecutorValues(EclipseProject eclipseProject) {

    MacroExecutionValues executorValues = new MacroExecutionValues();

    // create scoped properties
    executorValues.getProperties().put(PlatformExecutorValuesProvider.PROJECT_NAME, eclipseProject.getSpecifiedName());
    executorValues.getProperties().put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY,
        this._pathDelegate.convertToString(eclipseProject.getFolder()));

    // create scoped references
    executorValues.getReferences().put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY_PATH,
        this._pathDelegate.convertToPath(eclipseProject.getFolder()));

    return executorValues;
  }

}
