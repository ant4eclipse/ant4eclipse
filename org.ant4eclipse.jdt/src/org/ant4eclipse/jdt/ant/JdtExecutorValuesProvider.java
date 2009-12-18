/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.platform.ant.PlatformExecutorValuesProvider;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.PathComponent;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.jdt.tools.JdtResolver;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;

import java.io.File;
import java.util.List;

public class JdtExecutorValuesProvider implements JdtExecutorValues {

  /** the internally used path component */
  private PathComponent                  _pathComponent;

  /** the platform executor values provider */
  private PlatformExecutorValuesProvider _platformExecutorValuesProvider;

  /**
   * <p>
   * The path delegate.
   * </p>
   * 
   * @param pathComponent
   */
  public JdtExecutorValuesProvider(PathComponent pathComponent) {
    Assure.paramNotNull("pathComponent", pathComponent);
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
  public EcjAdditionalCompilerArguments provideExecutorValues(JavaProjectRole javaProjectRole,
      List<JdtClasspathContainerArgument> jdtClasspathContainerArguments, MacroExecutionValues executionValues) {

    // provide the executor values from the platform component
    this._platformExecutorValuesProvider.provideExecutorValues(javaProjectRole.getEclipseProject(), executionValues);

    // create compiler arguments
    EcjAdditionalCompilerArguments compilerArguments = new EcjAdditionalCompilerArguments();
    executionValues.getReferences().put(COMPILER_ARGS, compilerArguments);

    // resolve (boot) class path
    ResolvedClasspath cpAbsoluteCompiletime = JdtResolver.resolveProjectClasspath(javaProjectRole.getEclipseProject(),
        false, false, jdtClasspathContainerArguments);
    ResolvedClasspath cpRelativeCompiletime = JdtResolver.resolveProjectClasspath(javaProjectRole.getEclipseProject(),
        true, false, jdtClasspathContainerArguments);
    ResolvedClasspath cpAbsoluteRuntime = JdtResolver.resolveProjectClasspath(javaProjectRole.getEclipseProject(),
        false, true, jdtClasspathContainerArguments);
    ResolvedClasspath cpRelativeRuntime = JdtResolver.resolveProjectClasspath(javaProjectRole.getEclipseProject(),
        true, true, jdtClasspathContainerArguments);

    if (cpAbsoluteCompiletime.getBootClasspath().hasAccessRestrictions()) {
      // TODO
      compilerArguments.setBootClassPathAccessRestrictions(cpAbsoluteCompiletime.getBootClasspath()
          .getAccessRestrictions().asFormattedString());
    }

    ResolvedClasspathEntry[] classpathEntries = cpAbsoluteCompiletime.getClasspath();

    for (ResolvedClasspathEntry resolvedClasspathEntry : classpathEntries) {

      // set source folder for output folder
      if (resolvedClasspathEntry.hasSourcePathEntries()) {
        File[] sourcePathEntries = resolvedClasspathEntry.getSourcePathEntries();
        for (File file : resolvedClasspathEntry.getClassPathEntries()) {
          compilerArguments.addSourceFolderForOutputFolder(file, sourcePathEntries);
        }
      }

      // set access restrictions
      if (resolvedClasspathEntry.hasAccessRestrictions()) {
        AccessRestrictions accessRestrictions = resolvedClasspathEntry.getAccessRestrictions();
        for (File file : resolvedClasspathEntry.getClassPathEntries()) {
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
    String defaultOutputFolderName = javaProjectRole.getDefaultOutputFolder();
    File defaultOutputFolder = javaProjectRole.getEclipseProject().getChild(defaultOutputFolderName);
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

      for (String sourceFolderName : javaProjectRole.getSourceFolders()) {
        String outputFolderName = javaProjectRole.getOutputFolderForSourceFolder(sourceFolderName);
        File sourceFolder = javaProjectRole.getEclipseProject().getChild(sourceFolderName);
        File outputFolder = javaProjectRole.getEclipseProject().getChild(outputFolderName);
        compilerArguments.addOutputFolderForSourceFolder(sourceFolder, outputFolder);
      }
    }

    // return compilerArguments
    return compilerArguments;
  }
}
