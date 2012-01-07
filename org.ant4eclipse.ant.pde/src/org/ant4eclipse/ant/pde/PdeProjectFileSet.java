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
package org.ant4eclipse.ant.pde;

import java.io.File;
import java.util.List;

import org.ant4eclipse.ant.platform.core.task.AbstractAnt4EclipseFileSet;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.pde.model.buildproperties.AbstractBuildProperties;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.selectors.SelectorUtils;

/**
 * <p>
 * The {@link PdeProjectFileSet} type can be used to define plug-in project relative file sets.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PdeProjectFileSet extends AbstractAnt4EclipseFileSet {

  /** the bundle root (self) */
  private static final String     SELF                   = ".";

  /** the name of the (default) self directory */
  private static final String     DEFAULT_SELF_DIRECTORY = "@dot";

  /** the ant attribute 'excludeLibraries' */
  private boolean                 _excludeLibraries      = false;

  /** - */
  private boolean                 _sourceBundle          = false;

  /** - */
  private AbstractBuildProperties _buildProperties;

  /**
   * <p>
   * Creates a new instance of type {@link PdeProjectFileSet}.
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public PdeProjectFileSet( Project project ) {
    super( project );

  }

  /**
   * <p>
   * </p>
   * 
   * @return the sourceBundle
   */
  public boolean isSourceBundle() {
    return _sourceBundle;
  }

  /**
   * <p>
   * </p>
   * 
   * @param sourceBundle
   *          the sourceBundle to set
   */
  public void setSourceBundle( boolean sourceBundle ) {
    _sourceBundle = sourceBundle;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the excludeLibraries
   */
  public boolean isExcludeLibraries() {
    return _excludeLibraries;
  }

  /**
   * <p>
   * </p>
   * 
   * @param excludeLibraries
   *          the excludeLibraries to set
   */
  public void setExcludeLibraries( boolean excludeLibraries ) {
    _excludeLibraries = excludeLibraries;
  }

  /**
   * <p>
   * Computes the file set.
   * </p>
   */
  @Override
  protected void doComputeFileSet( List<Resource> resourceList ) {

    // require workspace and project name set
    requireWorkspaceAndProjectNameSet();

    if( !(getEclipseProject().hasRole( PluginProjectRole.class ) || getEclipseProject().hasRole(
        FeatureProjectRole.class )) ) {
      throw new BuildException( String.format(
          "Project '%s' must have role 'PluginProjectRole' or 'FeatureProjectRole'.", getEclipseProject()
              .getSpecifiedName() ) );
    }

    _buildProperties = getEclipseProject().hasRole( PluginProjectRole.class ) ? getEclipseProject().getRole(
        PluginProjectRole.class ).getBuildProperties() : getEclipseProject().getRole( FeatureProjectRole.class )
        .getBuildProperties();

    // nothing to do if no inclusion pattern is defined
    if( _sourceBundle && (!_buildProperties.hasSourceIncludes()) ) {
      return;
    } else if( (!_sourceBundle) && (!_buildProperties.hasBinaryIncludes()) ) {
      return;
    }

    // clear the resource list
    resourceList.clear();

    // iterate over the included pattern set
    List<String> includes = _sourceBundle ? _buildProperties.getSourceIncludes() : _buildProperties.getBinaryIncludes();
    for( String token : includes ) {
      processEntry( resourceList, token );
    }

    // debug the resolved entries
    if( A4ELogging.isDebuggingEnabled() ) {
      A4ELogging.debug( "Resolved pde project file set for project '%s'. Entries are:", getEclipseProject()
          .getSpecifiedName() );
      for( Resource resource : resourceList ) {
        A4ELogging.debug( "- '%s'", resource );
      }
    }

  }

  /**
   * <p>
   * 
   * </p>
   * 
   * @param resourceList
   *          The list the resources should be added to
   * @param token
   *          The token that should be processed
   */
  private void processEntry( List<Resource> resourceList, String token ) {

    // if token is a library name and _excludeLibraries
    if( _excludeLibraries && _buildProperties instanceof PluginBuildProperties
        && ((PluginBuildProperties) _buildProperties).hasLibrary( token ) ) {
      return;
    }

    // 'patch' the dot
    if( token.equals( SELF ) ) {
      token = DEFAULT_SELF_DIRECTORY;
    }

    // 'process' the token
    if( getEclipseProject().hasChild( token ) ) {

      // get the project child with the given name
      File file = getEclipseProject().getChild( token );

      if( file.isFile() ) {
        // if the child is a file, just add it to the list
        resourceList.add( new FileResource( getEclipseProject().getFolder(), token ) );
      } else {

        // if the child is a directory, scan the directory
        DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setBasedir( file );
        directoryScanner.setCaseSensitive( isCaseSensitive() );
        directoryScanner.setIncludes( null );
        if( getDefaultexcludes() ) {
          directoryScanner.addDefaultExcludes();
        }

        // do the job
        directoryScanner.scan();

        // get the included files and add it to the resource list
        String[] files = directoryScanner.getIncludedFiles();

        for( String fileName : files ) {
          if( token.equals( DEFAULT_SELF_DIRECTORY ) ) {
            if( !matchExcludePattern( fileName ) ) {
              resourceList.add( new FileResource( file, fileName ) );
            }
          } else {
            if( !matchExcludePattern( token + File.separatorChar + fileName ) ) {
              String filePath = normalize( file.getPath() );
              String rootPath = normalize( filePath ).substring( 0, filePath.lastIndexOf( normalize( token ) ) );
              resourceList.add( new FileResource( new File( rootPath ), token + File.separatorChar + fileName ) );
            }
          }
        }
      }
    }
  }

  /**
   * <p>
   * Helper method. Normalizes the given path.
   * </p>
   * 
   * @param path
   *          the path to normalize
   * @return the normalized path
   */
  private String normalize( String path ) {

    // replace '/' and '\' with File.separatorChar
    String result = path.replace( '/', File.separatorChar ).replace( '\\', File.separatorChar );

    // remove trailing '/' and '\'
    if( result.endsWith( "/" ) || result.endsWith( "\\" ) ) {
      result = result.substring( 0, result.length() - 1 );
    }

    // return result
    return result;
  }

  /**
   * <p>
   * Helper method. Checks if the given string (which is a path) matches one of the exclude patterns.
   * </p>
   * 
   * @param path
   *          the path
   * @return <code>true</code> if the given path matches an exclusion pattern.
   */
  private boolean matchExcludePattern( String path ) {
    List<String> excludes = _sourceBundle ? _buildProperties.getSourceExcludes() : _buildProperties.getBinaryExcludes();
    for( String pattern : excludes ) {
      // if the given path matches an exclusion pattern, return true
      if( SelectorUtils.matchPath( normalize( pattern ), normalize( path ), isCaseSensitive() ) ) {
        return true;
      }
    }
    return false;
  }
  
} /* ENDCLASS */
