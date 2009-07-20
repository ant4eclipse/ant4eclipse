package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import org.ant4eclipse.platform.ant.PlatformExecutorValuesProvider;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.PathComponent;

import java.io.File;
import java.util.List;

public class JdtExecutorValuesProvider implements JdtExecutorValues {

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
  public EcjAdditionalCompilerArguments provideExecutorValues(final JavaProjectRole javaProjectRole,
      final List<JdtClasspathContainerArgument> jdtClasspathContainerArguments,
      final MacroExecutionValues executionValues) {

    // provide the executor values form the platform component
    this._platformExecutorValuesProvider.provideExecutorValues(javaProjectRole.getEclipseProject(), executionValues);

    // create compiler arguments
    final EcjAdditionalCompilerArguments compilerArguments = new EcjAdditionalCompilerArguments();
    executionValues.getReferences().put(COMPILER_ARGS, compilerArguments);

    // resolve (boot) class path
    final ResolvedClasspath cpAbsoluteCompiletime = JdtResolver.resolveProjectClasspath(javaProjectRole
        .getEclipseProject(), false, false, jdtClasspathContainerArguments);
    final ResolvedClasspath cpRelativeCompiletime = JdtResolver.resolveProjectClasspath(javaProjectRole
        .getEclipseProject(), true, false, jdtClasspathContainerArguments);
    final ResolvedClasspath cpAbsoluteRuntime = JdtResolver.resolveProjectClasspath(
        javaProjectRole.getEclipseProject(), false, true, jdtClasspathContainerArguments);
    final ResolvedClasspath cpRelativeRuntime = JdtResolver.resolveProjectClasspath(
        javaProjectRole.getEclipseProject(), true, true, jdtClasspathContainerArguments);

    if (cpAbsoluteCompiletime.getBootClasspath().hasAccessRestrictions()) {
      // TODO
      compilerArguments.setBootClassPathAccessRestrictions(cpAbsoluteCompiletime.getBootClasspath()
          .getAccessRestrictions().asFormattedString());
    }

    final ResolvedClasspathEntry[] classpathEntries = cpAbsoluteCompiletime.getClasspath();
    for (final ResolvedClasspathEntry resolvedClasspathEntry : classpathEntries) {
      if (resolvedClasspathEntry.hasAccessRestrictions()) {
        final AccessRestrictions accessRestrictions = resolvedClasspathEntry.getAccessRestrictions();
        for (final File file : resolvedClasspathEntry.getEntries()) {
          compilerArguments.addAccessRestrictions(file, accessRestrictions.asFormattedString());
        }
      }
    }

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

    // resolve default output folder
    final String defaultOutputFolderName = javaProjectRole.getDefaultOutputFolder();
    final File defaultOutputFolder = javaProjectRole.getEclipseProject().getChild(defaultOutputFolderName);
    executionValues.getProperties().put(DEFAULT_OUTPUT_DIRECTORY_NAME, defaultOutputFolderName);
    executionValues.getProperties().put(DEFAULT_OUTPUT_DIRECTORY,
        this._pathComponent.convertToString(defaultOutputFolder));
    executionValues.getReferences().put(DEFAULT_OUTPUT_DIRECTORY_PATH,
        this._pathComponent.convertToPath(defaultOutputFolder));

    if (javaProjectRole.getSourceFolders().length > 0) {

      executionValues.getProperties().put(
          SOURCE_DIRECTORIES,
          this._pathComponent.convertToString(javaProjectRole.getEclipseProject().getChildren(
              javaProjectRole.getSourceFolders())));

      executionValues.getReferences().put(
          SOURCE_DIRECTORIES_PATH,
          this._pathComponent.convertToPath(javaProjectRole.getEclipseProject().getChildren(
              javaProjectRole.getSourceFolders())));

      for (final String sourceFolderName : javaProjectRole.getSourceFolders()) {
        final String outputFolderName = javaProjectRole.getOutputFolderForSourceFolder(sourceFolderName);
        final File sourceFolder = javaProjectRole.getEclipseProject().getChild(sourceFolderName);
        final File outputFolder = javaProjectRole.getEclipseProject().getChild(outputFolderName);
        compilerArguments.addSourceFolder(sourceFolder, outputFolder);
      }
    }

    // return compilerArguments
    return compilerArguments;
  }
}
