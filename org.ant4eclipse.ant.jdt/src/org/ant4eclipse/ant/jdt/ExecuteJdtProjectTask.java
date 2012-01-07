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

import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.ScopedMacroDefinition;
import org.ant4eclipse.ant.platform.core.delegate.ConditionalMacroDef;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.jdt.tools.JdtResolver;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteJdtProjectTask extends AbstractExecuteJdtProjectTask implements JdtExecutorValues {

  /** the constant for SCOPE_PROJECT_ELEMENT_NAME */
  private static final String      SCOPE_PROJECT_ELEMENT_NAME                    = "ForProject";

  /** the constant for SCOPE_TARGET_DIRECTORY_ELEMENT_NAME */
  private static final String      SCOPE_TARGET_DIRECTORY_ELEMENT_NAME           = "ForEachOutputDirectory";

  /** the constant for SCOPE_SOURCE_DIRECTORY_ELEMENT_NAME */
  private static final String      SCOPE_SOURCE_DIRECTORY_ELEMENT_NAME           = "ForEachSourceDirectory";

  private static final String      SCOPE_FOR_EACH_RUNTIME_CLASSPATH_ELEMENT_NAME = "ForEachRuntimeClasspathEntry";

  /** the constant for SCOPE_SOURCE_DIRECTORY */
  public static final String       SCOPE_SOURCE_DIRECTORY                        = "SCOPE_SOURCE_DIRECTORY";

  /** the constant for SCOPE_TARGET_DIRECTORY */
  public static final String       SCOPE_TARGET_DIRECTORY                        = "SCOPE_TARGET_DIRECTORY";

  /** the constant for SCOPE_PROJECT */
  public static final String       SCOPE_PROJECT                                 = "SCOPE_PROJECT";

  public static final String       SCOPE_FOR_EACH_RUNTIME_CLASSPATH              = "SCOPE_FOR_EACH_RUNTIME_CLASSPATH";

  /** - */
  public static final String       CLASSPATH_ABSOLUTE_COMPILETIME                = "absolute.compiletime";

  /** - */
  public static final String       CLASSPATH_ABSOLUTE_RUNTIME                    = "absolute.runtime";

  /** - */
  public static final String       CLASSPATH_RELATIVE_COMPILETIME                = "relative.compiletime";

  /** - */
  public static final String       CLASSPATH_RELATIVE_RUNTIME                    = "relative.runtime";

  /** - */
  private static final Set<String> CLASSPATH_POSSIBLE_VALUES                     = Collections
                                                                                     .unmodifiableSet( new HashSet<String>(
                                                                                         Arrays
                                                                                             .asList(
                                                                                                 CLASSPATH_ABSOLUTE_COMPILETIME,
                                                                                                 CLASSPATH_ABSOLUTE_RUNTIME,
                                                                                                 CLASSPATH_RELATIVE_COMPILETIME,
                                                                                                 CLASSPATH_RELATIVE_RUNTIME ) ) );

  /** - */
  private Set<String>              _resolvedClassPaths                           = CLASSPATH_POSSIBLE_VALUES;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteJdtProjectTask}.
   * </p>
   */
  public ExecuteJdtProjectTask() {
    super( "executeJdtProject" );
  }

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteJdtProjectTask}.
   * </p>
   * 
   * @param prefix
   *          the prefix
   */
  protected ExecuteJdtProjectTask( String prefix ) {
    super( prefix );
  }

  /**
   * @param resolvedClassPaths
   */
  public void setResolvedClassPaths( String resolvedClassPaths ) {

    _resolvedClassPaths = new HashSet<String>();
    
    if( resolvedClassPaths == null ) {
      return;
    }

    String[] paths = resolvedClassPaths.split( "," );
    for( String path : paths ) {

      String trimmedPath = path.trim().toLowerCase();

      // Make sure argument is valid
      if( !CLASSPATH_POSSIBLE_VALUES.contains( trimmedPath ) ) {
        throw new BuildException( String.format(
            "Invalid value for Parameter 'resolvedClassPaths' specified: '%s'. Allowed values are: '%s'", path,
            CLASSPATH_POSSIBLE_VALUES ) );
      }

      // add value
      _resolvedClassPaths.add( trimmedPath );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Object createDynamicElement( String name ) throws BuildException {

    // handle SCOPE_SOURCE_DIRECTORY
    if( SCOPE_SOURCE_DIRECTORY_ELEMENT_NAME.equalsIgnoreCase( name ) ) {
      return createScopedMacroDefinition( SCOPE_SOURCE_DIRECTORY );
    }
    // handle SCOPE_TARGET_DIRECTORY
    else if( SCOPE_TARGET_DIRECTORY_ELEMENT_NAME.equalsIgnoreCase( name ) ) {
      return createScopedMacroDefinition( SCOPE_TARGET_DIRECTORY );
    }
    // handle SCOPE_PROJECT
    else if( SCOPE_PROJECT_ELEMENT_NAME.equalsIgnoreCase( name ) ) {
      return createScopedMacroDefinition( SCOPE_PROJECT );
    }
    // handle SCOPE_FOR_EACH_RUNTIME_CLASSPATH
    else if( SCOPE_FOR_EACH_RUNTIME_CLASSPATH_ELEMENT_NAME.equalsIgnoreCase( name ) ) {
      return createScopedMacroDefinition( SCOPE_FOR_EACH_RUNTIME_CLASSPATH );
    }

    // delegate to template method
    return onCreateDynamicElement( name );
  }

  /**
   * <p>
   * Override this method to provide support for additional sub-elements defined in an ant build file.
   * </p>
   * 
   * @param name
   *          the name of the sub element
   * @return
   */
  protected Object onCreateDynamicElement( String name ) {
    // default implementation returns null
    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param scopedMacroDefinition
   * @return
   */
  protected boolean onExecuteScopeMacroDefintion( ScopedMacroDefinition<String> scopedMacroDefinition ) {
    // default implementation returns false
    return false;
  }

  /**
   * <p>
   * </p>
   * 
   * @param executionValues
   */
  protected void addAdditionalExecutionValues( MacroExecutionValues executionValues ) {
    // adds additional execution values
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // execute scoped macro definitions
    for( ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions() ) {

      MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      // execute SCOPE_SOURCE_DIRECTORY
      if( SCOPE_SOURCE_DIRECTORY.equals( scopedMacroDefinition.getScope() ) ) {
        executeSourceDirectoryScopedMacroDef( macroDef );
      }
      // execute SCOPE_TARGET_DIRECTORY
      else if( SCOPE_TARGET_DIRECTORY.equals( scopedMacroDefinition.getScope() ) ) {
        executeOutputDirectoryScopedMacroDef( macroDef );
      }
      // execute SCOPE_PROJECT
      else if( SCOPE_PROJECT.equals( scopedMacroDefinition.getScope() ) ) {
        executeProjectScopedMacroDef( macroDef );
      }
      // execute SCOPE_PROJECT
      else if( SCOPE_FOR_EACH_RUNTIME_CLASSPATH.equals( scopedMacroDefinition.getScope() ) ) {
        executeForEachRuntimeClasspathScopedMacroDef( macroDef );
      }
      // delegate to template method
      else {
        if( !onExecuteScopeMacroDefintion( scopedMacroDefinition ) ) {
          // TODO: NLS
          throw new RuntimeException( "Unknown Scope '" + scopedMacroDefinition.getScope() + "'" );
        }
      }
    }
  }

  /**
   * @param macroDef
   * @param javaProjectRole
   * @param classpathes
   */
  private void executeSourceDirectoryScopedMacroDef( MacroDef macroDef ) {

    for( final String sourceFolder : getJavaProjectRole().getSourceFolders() ) {

      // execute macro
      executeMacroInstance( macroDef, new MacroExecutionValuesProvider() {

        @Override
        public MacroExecutionValues provideMacroExecutionValues( MacroExecutionValues values ) {

          getExecutorValuesProvider().provideExecutorValues( getJavaProjectRole(), getJdtClasspathContainerArguments(),
              values, ExecuteJdtProjectTask.this._resolvedClassPaths );

          // add source and output directory
          values.getProperties().put( SOURCE_DIRECTORY_NAME, sourceFolder );
          values.getProperties()
              .put( SOURCE_DIRECTORY, convertToString( getEclipseProject().getChild( sourceFolder ) ) );
          values.getProperties().put( SOURCE_DIRECTORY_INCLUDES,
              getJavaProjectRole().getIncludePatternsForSourceFolder( sourceFolder ) );
          values.getProperties().put( SOURCE_DIRECTORY_EXCLUDES,
              getJavaProjectRole().getExcludePatternsForSourceFolder( sourceFolder ) );
          values.getProperties().put( OUTPUT_DIRECTORY_NAME, sourceFolder );
          values.getProperties().put(
              OUTPUT_DIRECTORY,
              convertToString( getEclipseProject().getChild(
                  getJavaProjectRole().getOutputFolderForSourceFolder( sourceFolder ) ) ) );

          // refs
          values.getReferences().put( SOURCE_DIRECTORY_PATH,
              convertToPath( getEclipseProject().getChild( sourceFolder ) ) );
          values.getReferences().put(
              OUTPUT_DIRECTORY_PATH,
              convertToPath( getEclipseProject().getChild(
                  getJavaProjectRole().getOutputFolderForSourceFolder( sourceFolder ) ) ) );

          // add additional execution values if necessary
          addAdditionalExecutionValues( values );

          // return the values
          return values;
        }
      } );
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeOutputDirectoryScopedMacroDef( MacroDef macroDef ) {

    // iterate over all output folders
    for( final String outFolder : getJavaProjectRole().getAllOutputFolders() ) {

      // execute macro
      executeMacroInstance( macroDef, new MacroExecutionValuesProvider() {

        @Override
        public MacroExecutionValues provideMacroExecutionValues( MacroExecutionValues values ) {

          // get the default jdt executor values
          getExecutorValuesProvider().provideExecutorValues( getJavaProjectRole(), getJdtClasspathContainerArguments(),
              values, ExecuteJdtProjectTask.this._resolvedClassPaths );

          // add output directory
          values.getProperties().put( OUTPUT_DIRECTORY_NAME, outFolder );
          values.getProperties().put( OUTPUT_DIRECTORY, convertToString( getEclipseProject().getChild( outFolder ) ) );
          values.getReferences()
              .put( OUTPUT_DIRECTORY_PATH, convertToPath( getEclipseProject().getChild( outFolder ) ) );

          // call template method
          addAdditionalExecutionValues( values );

          // return the values
          return values;
        }
      } );
    }
  }

  /**
   * <p>
   * Executed the given MacroDef for each (resolved) runtime classpath entry of the Eclipse project
   * </p>
   * 
   * @param macroDef
   */
  private void executeForEachRuntimeClasspathScopedMacroDef( MacroDef macroDef ) {
    Assure.instanceOf( "macroDef", macroDef, ConditionalMacroDef.class );

    // Get the ConditionalMacroDef to access the macros attributes
    ConditionalMacroDef conditionalMacroDef = (ConditionalMacroDef) macroDef;

    // Read the 'reverse' attribute
    boolean reverse = Boolean.parseBoolean( conditionalMacroDef.getAttribute( "reverse", "false" ) );

    JavaProjectRole javaProjectRole = getJavaProjectRole();

    final EclipseProject project = javaProjectRole.getEclipseProject();

    // Resolve the absolute and relative classpaths
    ResolvedClasspath cpAbsoluteRuntime = JdtResolver.resolveProjectClasspath( project, false, true, getJdtClasspathContainerArguments() );
    
    // get the entries
    final List<File> absoluteClasspathFiles = cpAbsoluteRuntime.getClasspathFiles();

    // reverse the classpath order if requested
    if( reverse ) {
      Collections.reverse( absoluteClasspathFiles );
    }

    // invoke callback template for each classpath entry
    for( int i = 0; i < absoluteClasspathFiles.size(); i++ ) {
      final int index = i;
      executeMacroInstance( macroDef, new MacroExecutionValuesProvider() {

        @Override
        public MacroExecutionValues provideMacroExecutionValues( MacroExecutionValues values ) {

          getExecutorValuesProvider().provideExecutorValues( getJavaProjectRole(), getJdtClasspathContainerArguments(),
              values, ExecuteJdtProjectTask.this._resolvedClassPaths );

          final StringMap properties = values.getProperties();
          // add absolute path
          properties.put( "classpathEntry.absolute", absoluteClasspathFiles.get( index ).getAbsolutePath() );

          // // add relative path
          // properties.put("classpathEntry.relative", relativeClasspathFiles[index].getPath());

          // add name (last part of the path)
          properties.put( "classpathEntry.name", absoluteClasspathFiles.get( index ).getName() );

          // add informations about file system resource
          properties.put( "classpathEntry.isExisting", Boolean.toString( absoluteClasspathFiles.get( index ).exists() ) );
          properties.put( "classpathEntry.isFile", Boolean.toString( absoluteClasspathFiles.get( index ).isFile() ) );
          properties.put( "classpathEntry.isFolder", Boolean.toString( absoluteClasspathFiles.get( index ).isDirectory() ) );

          String relative = Utilities.calcRelative( project.getFolder(), absoluteClasspathFiles.get( index ) );
          if( (relative == null) || (relative.indexOf( ".." ) != -1) ) {
            // the calculation of a diff path failed or the relative path "moves" outside of the
            // projects directory
            properties.put( "classpathEntry.isProjectRelative", "false" );
          } else {
            // we've got a project relative classpath entry
            properties.put( "classpathEntry.isProjectRelative", "true" );
          }

          // create a FileSet for the entry describing it's content
          FileSet fileSet = new FileSet();
          fileSet.setProject( getProject() );
          if( absoluteClasspathFiles.get( index ).isFile() ) {
            fileSet.setFile( absoluteClasspathFiles.get( index ) );
          } else if( absoluteClasspathFiles.get( index ).isDirectory() ) {
            fileSet.setDir( absoluteClasspathFiles.get( index ) );
          }

          // add the FileSet as reference
          values.getReferences().put( "classpathEntry.fileSet", fileSet );

          return values;
        }
      } );
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeProjectScopedMacroDef( MacroDef macroDef ) {

    // execute macro
    executeMacroInstance( macroDef, new MacroExecutionValuesProvider() {

      @Override
      public MacroExecutionValues provideMacroExecutionValues( final MacroExecutionValues values ) {

        // get the default jdt executor values
        getExecutorValuesProvider().provideExecutorValues( getJavaProjectRole(), getJdtClasspathContainerArguments(),
            values, ExecuteJdtProjectTask.this._resolvedClassPaths );

        // add additional execution values if necessary
        addAdditionalExecutionValues( values );

        // return the values
        return values;
      }
    } );
  }
  
} /* ENDCLASS */
