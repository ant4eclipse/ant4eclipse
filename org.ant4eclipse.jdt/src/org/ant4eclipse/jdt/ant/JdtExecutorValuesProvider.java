package org.ant4eclipse.jdt.ant;

import java.util.Map;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.platform.ant.PlatformExecutorValuesProvider;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.PathComponent;
import org.ant4eclipse.platform.model.resource.EclipseProject;

public class JdtExecutorValuesProvider {

  /** the internally used path component */
  private final PathComponent                  _pathComponent;

  private final PlatformExecutorValuesProvider _platformExecutorValuesProvider;

  /**
   * <p>
   * The path delegate.
   * </p>
   * 
   * @param pathComponent
   */
  public JdtExecutorValuesProvider(final PathComponent pathComponent) {
    Assert.notNull(pathComponent);

    this._platformExecutorValuesProvider = new PlatformExecutorValuesProvider(pathComponent);

    this._pathComponent = pathComponent;
  }

  public void provideExecutorValues(final EclipseProject eclipseProject,
      final Map<String, Object> jdtClasspathContainerArguments, final MacroExecutionValues executionValues) {

    // provide the executor values form the platform component
    this._platformExecutorValuesProvider.provideExecutorValues(eclipseProject, executionValues);

    // jdt specific properties
    final ResolvedClasspath cpAbsoluteCompiletime = resolveClasspath(eclipseProject, false, false,
        jdtClasspathContainerArguments);
    final ResolvedClasspath cpRelativeCompiletime = resolveClasspath(eclipseProject, false, true,
        jdtClasspathContainerArguments);
    final ResolvedClasspath cpAbsoluteRuntime = resolveClasspath(eclipseProject, true, false,
        jdtClasspathContainerArguments);
    final ResolvedClasspath cpRelativeRuntime = resolveClasspath(eclipseProject, true, true,
        jdtClasspathContainerArguments);

    executionValues.getProperties().put("classpath.absolute.compiletime",
        this._pathComponent.convertToString(cpAbsoluteCompiletime.getClasspathFiles()));
    executionValues.getProperties().put("classpath.relative.compiletime",
        this._pathComponent.convertToString(cpRelativeCompiletime.getClasspathFiles()));
    executionValues.getProperties().put("classpath.absolute.runtime",
        this._pathComponent.convertToString(cpAbsoluteRuntime.getClasspathFiles()));
    executionValues.getProperties().put("classpath.relative.runtime",
        this._pathComponent.convertToString(cpRelativeRuntime.getClasspathFiles()));

    // executionValues.getProperties().put("default.output.directory", getJavaProjectRole().getDefaultOutputFolder());

    executionValues.getReferences().put("classpath.absolute.compiletime.path",
        this._pathComponent.convertToPath(cpAbsoluteCompiletime.getClasspathFiles()));
    executionValues.getReferences().put("classpath.relative.compiletime.path",
        this._pathComponent.convertToPath(cpRelativeCompiletime.getClasspathFiles()));
    executionValues.getReferences().put("classpath.absolute.runtime.path",
        this._pathComponent.convertToPath(cpAbsoluteRuntime.getClasspathFiles()));
    executionValues.getReferences().put("classpath.relative.runtime.path",
        this._pathComponent.convertToPath(cpRelativeRuntime.getClasspathFiles()));

    // executionValues.getProperties().put("default.output.directory", getJavaProjectRole().getDefaultOutputFolder());
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param eclipseProject
  // * @param executionValues
  // */
  // public void provideExecutorValues(final EclipseProject eclipseProject, final MacroExecutionValues executionValues)
  // {
  // Assert.notNull(eclipseProject);
  // Assert.notNull(executionValues);
  //
  // // create scoped properties
  // executionValues.getProperties().put(PlatformExecutorValuesProvider.PROJECT_NAME,
  // eclipseProject.getSpecifiedName());
  // executionValues.getProperties().put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY,
  // this._pathComponent.convertToString(eclipseProject.getFolder()));
  //
  // // create scoped references
  // executionValues.getReferences().put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY_PATH,
  // this._pathComponent.convertToPath(eclipseProject.getFolder()));
  // }

  private ResolvedClasspath resolveClasspath(final EclipseProject eclipseProject, final boolean resolveRelative,
      final boolean isRuntimeClasspath, final Map<String, Object> jdtClasspathContainerArguments) {

    // TODO
    final Properties properties = new Properties();
    properties.putAll(jdtClasspathContainerArguments);

    final ResolvedClasspath resolvedClasspath = JdtResolver.resolveProjectClasspath(eclipseProject, resolveRelative,
        isRuntimeClasspath, properties);

    return resolvedClasspath;
  }
}
