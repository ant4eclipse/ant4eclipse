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
package org.ant4eclipse.ant.jdt;

import org.ant4eclipse.ant.platform.PlatformExecutorValuesProvider;
import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.PathComponent;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.jdt.tools.JdtResolver;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.List;
import java.util.Set;

public class JdtExecutorValuesProvider implements JdtExecutorValues {

  /** the internally used path component */
  private PathComponent                  _pathComponent;

  /** the platform executor values provider */
  private PlatformExecutorValuesProvider _platformExecutorValuesProvider;

  /** the project component */
  private ProjectComponent               _projectComponent;

  /**
   * <p>
   * The path delegate.
   * </p>
   * 
   * @param pathComponent
   */
  public JdtExecutorValuesProvider( PathComponent pathComponent, ProjectComponent projectComponent ) {
    Assure.notNull( "pathComponent", pathComponent );
    _platformExecutorValuesProvider = new PlatformExecutorValuesProvider( pathComponent );
    _pathComponent = pathComponent;
    _projectComponent = projectComponent;
  }

  /**
   * <p>
   * </p>
   * 
   * @param eclipseProject
   * @param jdtClasspathContainerArguments
   * @param executionValues
   */
  public EcjAdditionalCompilerArguments provideExecutorValues( JavaProjectRole javaProjectRole,
      List<JdtClasspathContainerArgument> jdtClasspathContainerArguments, MacroExecutionValues executionValues,
      Set<String> requestedPaths ) {

    // provide the executor values from the platform component
    _platformExecutorValuesProvider.provideExecutorValues( javaProjectRole.getEclipseProject(), executionValues );

    // create compiler arguments
    EcjAdditionalCompilerArguments compilerArguments = new EcjAdditionalCompilerArguments();
    executionValues.getReferences().put( COMPILER_ARGS, compilerArguments );

    // resolve (boot) class path
    ResolvedClasspath cpAbsoluteCompiletime = null;
    if( requestedPaths.contains( ExecuteJdtProjectTask.CLASSPATH_ABSOLUTE_COMPILETIME ) ) {
      cpAbsoluteCompiletime = JdtResolver.resolveProjectClasspath( javaProjectRole.getEclipseProject(), false, false,
          jdtClasspathContainerArguments );
    }

    ResolvedClasspath cpRelativeCompiletime = null;
    if( requestedPaths.contains( ExecuteJdtProjectTask.CLASSPATH_RELATIVE_COMPILETIME ) ) {
      cpRelativeCompiletime = JdtResolver.resolveProjectClasspath( javaProjectRole.getEclipseProject(), true, false,
          jdtClasspathContainerArguments );
    }

    ResolvedClasspath cpAbsoluteRuntime = null;
    if( requestedPaths.contains( ExecuteJdtProjectTask.CLASSPATH_ABSOLUTE_RUNTIME ) ) {
      cpAbsoluteRuntime = JdtResolver.resolveProjectClasspath( javaProjectRole.getEclipseProject(), false, true,
          jdtClasspathContainerArguments );
    }

    ResolvedClasspath cpRelativeRuntime = null;
    if( requestedPaths.contains( ExecuteJdtProjectTask.CLASSPATH_RELATIVE_RUNTIME ) ) {
      cpRelativeRuntime = JdtResolver.resolveProjectClasspath( javaProjectRole.getEclipseProject(), true, true,
          jdtClasspathContainerArguments );
    }

    if( cpAbsoluteCompiletime != null ) {

      if( cpAbsoluteCompiletime.hasBootClasspath() ) {
        if( cpAbsoluteCompiletime.getBootClasspath().hasAccessRestrictions() ) {
          // TODO
          compilerArguments.setBootClassPathAccessRestrictions( cpAbsoluteCompiletime.getBootClasspath()
              .getAccessRestrictions().asFormattedString() );
        }
      }

      ResolvedClasspathEntry[] classpathEntries = cpAbsoluteCompiletime.getClasspath();
      for( ResolvedClasspathEntry resolvedClasspathEntry : classpathEntries ) {

        // set source folder for output folder
        if( resolvedClasspathEntry.hasSourcePathEntries() ) {
          File[] sourcePathEntries = resolvedClasspathEntry.getSourcePathEntries();
          for( File file : resolvedClasspathEntry.getClassPathEntries() ) {
            compilerArguments.addSourceFolderForOutputFolder( file, sourcePathEntries );
          }
        }

        if( A4ELogging.isDebuggingEnabled() ) {
          A4ELogging.debug( "Resolved cp entry: %s", resolvedClasspathEntry.toString() );
        }

        // set access restrictions
        if( resolvedClasspathEntry.hasAccessRestrictions() ) {
          AccessRestrictions accessRestrictions = resolvedClasspathEntry.getAccessRestrictions();
          for( File file : resolvedClasspathEntry.getClassPathEntries() ) {
            compilerArguments.addAccessRestrictions( file, accessRestrictions.asFormattedString() );
          }
        }
      }

      if( cpAbsoluteCompiletime.hasBootClasspath() ) {
        executionValues.getProperties().put( BOOT_CLASSPATH,
            _pathComponent.convertToString( cpAbsoluteCompiletime.getBootClasspathFiles() ) );
      }

      executionValues.getProperties().put( CLASSPATH_ABSOLUTE_COMPILETIME,
          _pathComponent.convertToString( cpAbsoluteCompiletime.getClasspathFiles() ) );

      if( cpAbsoluteCompiletime.hasBootClasspath() ) {
        executionValues.getReferences().put( BOOT_CLASSPATH_PATH,
            _pathComponent.convertToPath( cpAbsoluteCompiletime.getBootClasspathFiles() ) );
      }
      executionValues.getReferences().put( CLASSPATH_ABSOLUTE_COMPILETIME_PATH,
          _pathComponent.convertToPath( cpAbsoluteCompiletime.getClasspathFiles() ) );
    }

    if( cpRelativeCompiletime != null ) {
      executionValues.getProperties().put( CLASSPATH_RELATIVE_COMPILETIME,
          _pathComponent.convertToString( cpRelativeCompiletime.getClasspathFiles() ) );
      executionValues.getReferences().put( CLASSPATH_RELATIVE_COMPILETIME_PATH,
          _pathComponent.convertToPath( cpRelativeCompiletime.getClasspathFiles() ) );
    }

    if( cpAbsoluteRuntime != null ) {
      executionValues.getProperties().put( CLASSPATH_ABSOLUTE_RUNTIME,
          _pathComponent.convertToString( cpAbsoluteRuntime.getClasspathFiles() ) );
      executionValues.getReferences().put( CLASSPATH_ABSOLUTE_RUNTIME_PATH,
          _pathComponent.convertToPath( cpAbsoluteRuntime.getClasspathFiles() ) );
    }

    if( cpRelativeRuntime != null ) {
      executionValues.getProperties().put( CLASSPATH_RELATIVE_RUNTIME,
          _pathComponent.convertToString( cpRelativeRuntime.getClasspathFiles() ) );
      executionValues.getReferences().put( CLASSPATH_RELATIVE_RUNTIME_PATH,
          _pathComponent.convertToPath( cpRelativeRuntime.getClasspathFiles() ) );
    }

    // resolve default output folder
    String defaultOutputFolderName = javaProjectRole.getDefaultOutputFolder();
    if( defaultOutputFolderName == null ) {
      A4ELogging.info( "Project '%s' has no output folder", javaProjectRole.getEclipseProject().getSpecifiedName() );
    } else {
      File defaultOutputFolder = javaProjectRole.getEclipseProject().getChild( defaultOutputFolderName );
      executionValues.getProperties().put( DEFAULT_OUTPUT_DIRECTORY_NAME, defaultOutputFolderName );
      executionValues.getProperties().put( DEFAULT_OUTPUT_DIRECTORY,
          _pathComponent.convertToString( defaultOutputFolder ) );
      executionValues.getReferences().put( DEFAULT_OUTPUT_DIRECTORY_PATH,
          _pathComponent.convertToPath( defaultOutputFolder ) );
    }

    if( javaProjectRole.getSourceFolders().length > 0 ) {

      executionValues.getProperties().put(
          SOURCE_DIRECTORIES,
          _pathComponent.convertToString( javaProjectRole.getEclipseProject().getChildren(
              javaProjectRole.getSourceFolders() ) ) );

      executionValues.getReferences().put(
          SOURCE_DIRECTORIES_PATH,
          _pathComponent.convertToPath( javaProjectRole.getEclipseProject().getChildren(
              javaProjectRole.getSourceFolders() ) ) );

      // Support for filtered java source directory:
      // in this case we create a path that contains a file set with
      // all included java source files...
      if( javaProjectRole.hasExcludeOrIncludeFiltersForSourceFolders() ) {
        Path sourceFilteredFileSetPath = createFilteredSourceFilePath( javaProjectRole );
        compilerArguments.setSourceFilteredFilesetPath( sourceFilteredFileSetPath );
        executionValues.getReferences().put( SOURCE_FILTERED_FILESET_PATH, sourceFilteredFileSetPath );
      }

      executionValues.getProperties().put(
          OUTPUT_DIRECTORIES,
          _pathComponent.convertToString( javaProjectRole.getEclipseProject().getChildren(
              javaProjectRole.getAllOutputFolders() ) ) );

      executionValues.getReferences().put(
          OUTPUT_DIRECTORIES_PATH,
          _pathComponent.convertToPath( javaProjectRole.getEclipseProject().getChildren(
              javaProjectRole.getAllOutputFolders() ) ) );

      for( String sourceFolderName : javaProjectRole.getSourceFolders() ) {
        String outputFolderName = javaProjectRole.getOutputFolderForSourceFolder( sourceFolderName );
        File sourceFolder = javaProjectRole.getEclipseProject().getChild( sourceFolderName );
        File outputFolder = javaProjectRole.getEclipseProject().getChild( outputFolderName );
        compilerArguments.addOutputFolderForSourceFolder( sourceFolder, outputFolder );
      }
    }

    // return compilerArguments
    return compilerArguments;
  }

  /**
   * <p>
   * Returns an ant {@link Path} that contains a file set with all included source files.
   * </p>
   * 
   * @param javaProjectRole
   *          the java project role
   * @return the ant {@link Path}
   */
  public final Path createFilteredSourceFilePath( JavaProjectRole javaProjectRole ) {

    // the ant path
    Path antPath = new Path( _projectComponent.getProject() );

    // the source folder
    for( String sourceFolder : javaProjectRole.getSourceFolders() ) {

      File folder = javaProjectRole.getEclipseProject().getChild( sourceFolder );
      String includePattern = javaProjectRole.getIncludePatternsForSourceFolder( sourceFolder );
      String excludePattern = javaProjectRole.getExcludePatternsForSourceFolder( sourceFolder );

      FileSet fileSet = new FileSet();
      fileSet.setDir( folder );
      fileSet.setIncludes( includePattern );
      fileSet.setExcludes( excludePattern );

      antPath.addFileset( fileSet );
    }

    // return the ant path
    return antPath;
  }
  
} /* ENDCLASS */
