package org.ant4eclipse.jdt.ant;

import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.platform.ant.PlatformExecutorValuesProvider;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.PathComponent;
import org.ant4eclipse.platform.model.resource.EclipseProject;

public class JdtExecutorValuesProvider {

  public static final String                   CLASSPATH_RELATIVE_RUNTIME_PATH     = "classpath.relative.runtime.path";

  public static final String                   CLASSPATH_ABSOLUTE_RUNTIME_PATH     = "classpath.absolute.runtime.path";

  public static final String                   CLASSPATH_RELATIVE_COMPILETIME_PATH = "classpath.relative.compiletime.path";

  public static final String                   CLASSPATH_ABSOLUTE_COMPILETIME_PATH = "classpath.absolute.compiletime.path";

  public static final String                   BOOT_CLASSPATH_PATH                 = "boot.classpath.path";

  public static final String                   CLASSPATH_RELATIVE_RUNTIME          = "classpath.relative.runtime";

  public static final String                   CLASSPATH_ABSOLUTE_RUNTIME          = "classpath.absolute.runtime";

  public static final String                   CLASSPATH_RELATIVE_COMPILETIME      = "classpath.relative.compiletime";

  public static final String                   CLASSPATH_ABSOLUTE_COMPILETIME      = "classpath.absolute.compiletime";

  public static final String                   BOOT_CLASSPATH                      = "boot.classpath";

  /** the internally used path component */
  private final PathComponent                  _pathComponent;

  /** the platform executor values provider */
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

  /**
   * <p>
   * </p>
   * 
   * @param eclipseProject
   * @param jdtClasspathContainerArguments
   * @param executionValues
   */
  public void provideExecutorValues(final EclipseProject eclipseProject,
      final List<JdtClasspathContainerArgument> jdtClasspathContainerArguments,
      final MacroExecutionValues executionValues) {

    // provide the executor values form the platform component
    this._platformExecutorValuesProvider.provideExecutorValues(eclipseProject, executionValues);

    // jdt specific properties
    final ResolvedClasspath cpAbsoluteCompiletime = JdtResolver.resolveProjectClasspath(eclipseProject, false, false,
        jdtClasspathContainerArguments);
    final ResolvedClasspath cpRelativeCompiletime = JdtResolver.resolveProjectClasspath(eclipseProject, false, true,
        jdtClasspathContainerArguments);
    final ResolvedClasspath cpAbsoluteRuntime = JdtResolver.resolveProjectClasspath(eclipseProject, true, false,
        jdtClasspathContainerArguments);
    final ResolvedClasspath cpRelativeRuntime = JdtResolver.resolveProjectClasspath(eclipseProject, true, true,
        jdtClasspathContainerArguments);

    executionValues.getProperties().put(BOOT_CLASSPATH,
        this._pathComponent.convertToString(cpAbsoluteCompiletime.getBootClasspathFiles()));

    executionValues.getProperties().put(CLASSPATH_ABSOLUTE_COMPILETIME,
        this._pathComponent.convertToString(cpAbsoluteCompiletime.getClasspathFiles()));
    executionValues.getProperties().put(CLASSPATH_RELATIVE_COMPILETIME,
        this._pathComponent.convertToString(cpRelativeCompiletime.getClasspathFiles()));
    executionValues.getProperties().put(CLASSPATH_ABSOLUTE_RUNTIME,
        this._pathComponent.convertToString(cpAbsoluteRuntime.getClasspathFiles()));
    executionValues.getProperties().put(CLASSPATH_RELATIVE_RUNTIME,
        this._pathComponent.convertToString(cpRelativeRuntime.getClasspathFiles()));

    // executionValues.getProperties().put("default.output.directory", getJavaProjectRole().getDefaultOutputFolder());

    executionValues.getReferences().put(BOOT_CLASSPATH_PATH,
        this._pathComponent.convertToPath(cpAbsoluteCompiletime.getBootClasspathFiles()));

    executionValues.getReferences().put(CLASSPATH_ABSOLUTE_COMPILETIME_PATH,
        this._pathComponent.convertToPath(cpAbsoluteCompiletime.getClasspathFiles()));
    executionValues.getReferences().put(CLASSPATH_RELATIVE_COMPILETIME_PATH,
        this._pathComponent.convertToPath(cpRelativeCompiletime.getClasspathFiles()));
    executionValues.getReferences().put(CLASSPATH_ABSOLUTE_RUNTIME_PATH,
        this._pathComponent.convertToPath(cpAbsoluteRuntime.getClasspathFiles()));
    executionValues.getReferences().put(CLASSPATH_RELATIVE_RUNTIME_PATH,
        this._pathComponent.convertToPath(cpRelativeRuntime.getClasspathFiles()));

    // executionValues.getProperties().put("default.output.directory", getJavaProjectRole().getDefaultOutputFolder());
  }
}
