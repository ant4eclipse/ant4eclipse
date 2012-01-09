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
package org.ant4eclipse.lib.platform.internal.model.resource;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.internal.model.resource.role.ProjectRoleIdentifierRegistry;
import org.ant4eclipse.lib.platform.model.resource.BuildCommand;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRole;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsultes a project. A project contains a workspace and is represented by a directory in this workspace. A project
 * can have multiple natures and multiple roles.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EclipseProjectImpl implements EclipseProject {

  /** The name of the projects <tt>.settings</tt> folder */
  public static final String       SETTINGS_FOLDER_NAME = ".settings";

  /** the workspace that contains this project */
  private Workspace                _workspace;

  /** the file that represents this project */
  private File                     _projectDirectory;

  /** the project name specified in the project description */
  private String                   _specifiedName;

  /** the <tt>.settings</tt> folder of the project or <tt>null<tt> if there is no <tt>.settings</tt> folder */
  private File                     _settingsFolder;

  /** the project comment */
  private String                   _comment;

  /** the list of project natures */
  private List<ProjectNature>      _natures;

  /** the list of project roles */
  private List<ProjectRole>        _roles;

  /** the list of buildCommands */
  private List<BuildCommand>       _buildCommands;

  /** the referenced project specified in the project description */
  private List<String>             _referencedProjects;

  /** the linked resources specified in the project description */
  private List<LinkedResourceImpl> _linkedResources;

  /** the names of the linked resource. used for the mapping */
  private List<String>             _linkedResourceNames;

  /**
   * Creates a new instance of type project.
   * 
   * @param workspace
   *          the workspace
   * @param projectName
   *          the name of the project
   */
  public EclipseProjectImpl( Workspace workspace, File projectDirectory ) {
    Assure.isDirectory( "projectDirectory", projectDirectory );

    _workspace = workspace;
    _projectDirectory = Utilities.getCanonicalFile( projectDirectory );
    _natures = new ArrayList<ProjectNature>();
    _roles = new ArrayList<ProjectRole>();
    _buildCommands = new ArrayList<BuildCommand>();
    _referencedProjects = new ArrayList<String>();
    _linkedResources = new ArrayList<LinkedResourceImpl>();
    _linkedResourceNames = new ArrayList<String>();

    File settingsFolder = getChild( SETTINGS_FOLDER_NAME );
    _settingsFolder = (settingsFolder.isDirectory() ? settingsFolder : null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSpecifiedName() {
    return _specifiedName;
  }

  /**
   * @param specifiedName
   *          The specifiedName to set.
   */
  public void setSpecifiedName( String specifiedName ) {
    _specifiedName = specifiedName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getComment() {
    return _comment;
  }

  /**
   * @param comment
   *          The comment to set.
   */
  public void setComment( String comment ) {
    _comment = comment;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFolderName() {
    return _projectDirectory.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getFolder() {
    return _projectDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getFolder( PathStyle pathstyle ) {
    Assure.notNull( "pathstyle", pathstyle );
    if( pathstyle == PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME ) {
      return new File( "." );
    } else if( pathstyle == PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME ) {
      return new File( _projectDirectory.getName() );
    } else /* if (pathstyle == PathStyle.ABSOLUTE) */{
      return _projectDirectory.getAbsoluteFile();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exists() {
    return _projectDirectory.isDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasChild( String path ) {
    Assure.notNull( "path", path );
    File child = getChild( path );
    return child.exists();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getChild( String path ) {
    return getChild( path, PathStyle.ABSOLUTE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getChildren( List<String> pathes ) {
    return getChildren( pathes, PathStyle.ABSOLUTE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getChildren( List<String> path, PathStyle relative ) {
    Assure.notNull( "path", path );
    List<File> result = new ArrayList<File>();
    for( int i = 0; i < path.size(); i++ ) {
      result.add( getChild( path.get(i), relative ) );
    }
    return result;
  }

  protected boolean hasSettingsFolder() {
    return _settingsFolder != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSettingsFile( String settingsFileName ) {
    // check if settings folder exists
    if( !hasSettingsFolder() ) {
      return false;
    }
    File settingsFile = new File( _settingsFolder, settingsFileName );
    // is it an existing file?
    return settingsFile.isFile();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getSettingsFile( String settingsFileName ) throws RuntimeException {
    Assure.notNull( "settingsFileName", settingsFileName );
    Assure.assertTrue( hasSettingsFolder(), String.format( "The project '%s' must have a .settings folder", getFolderName() ) );
    File settingsFile = new File( _settingsFolder, settingsFileName );
    if( !settingsFile.exists() ) {
      throw new RuntimeException( String.format( "Settings File '%s' not found in project '%s'", settingsFileName, getFolderName() ) );
    }
    if( !settingsFile.isFile() ) {
      throw new RuntimeException( String.format( "Settings File '%s' in project '%s' is not a file", settingsFile, getFolderName() ) );
    }
    return settingsFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getChild( String path, PathStyle pathstyle ) {
    Assure.notNull( "path", path );

    String name = path;
    String rest = null;
    int idx = firstFileSeparator( path );
    if( idx != -1 ) {
      name = path.substring( 0, idx );
      rest = path.substring( idx + 1 );
    }

    // handle linked resource
    if( isLinkedResource( name ) ) {
      LinkedResourceImpl resource = getLinkedResource( name );
      if( (pathstyle != PathStyle.ABSOLUTE) && (resource.getRelativeLocation() == null) ) {
        return(new File( resource.getLocation() ));
      }

      File result = pathstyle != PathStyle.ABSOLUTE ? new File( getFolder(), resource.getRelativeLocation() )
          : new File( resource.getLocation() );

      if( rest != null ) {
        result = new File( result, rest );
      }
      if( (pathstyle == PathStyle.ABSOLUTE) && (!result.isAbsolute()) ) {
        result = new File( _projectDirectory, result.getPath() );
      }

      // TODO: remove
      if( !result.exists() ) {
        throw new RuntimeException( result.getAbsolutePath() );
      }

      //
      return result;
    }

    //
    if( pathstyle == PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME ) {
      if( path.length() == 0 ) {
        path = ".";
      }
      return new File( path );
    } else if( pathstyle == PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME ) {
      return new File( _projectDirectory.getName(), path );
    }

    // handle ABSOLUTE
    return new File( _projectDirectory, path );
  }

  /**
   * Returns the index of the first file separator.
   * 
   * @param str
   *          The string that shall be tested.
   * 
   * @return The index of the first file separator or -1.
   */
  private int firstFileSeparator( String str ) {
    int idx1 = str.indexOf( '/' );
    int idx2 = str.indexOf( '\\' );
    if( (idx1 == -1) && (idx2 == -1) ) {
      return -1;
    }
    if( (idx1 != -1) && (idx2 != -1) ) {
      return Math.min( idx1, idx2 );
    }
    return Math.max( idx1, idx2 );
  }

  /**
   * Adds the specified nature to the project.
   * 
   * @param nature
   *          the nature to add.
   */
  public void addNature( ProjectNature nature ) {
    Assure.notNull( "nature", nature );
    if( !_natures.contains( nature ) ) {
      _natures.add( nature );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNature( String natureName ) {
    Assure.notNull( "natureName", natureName );
    return hasNature( ProjectNature.createNature( natureName ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNature( ProjectNature nature ) {
    Assure.notNull( "nature", nature );

    // nature unknown:
    if( !_natures.contains( nature ) ) {

      ProjectRoleIdentifierRegistry registry = A4ECore.instance().getRequiredService(
          ProjectRoleIdentifierRegistry.class );

      // try if the user supplied an abbreviation
      String abbreviation = nature.getName().toLowerCase();
      ProjectNature[] natures = registry.getNaturesForAbbreviation( abbreviation );
      if( natures != null ) {
        // check the natures with the full id now
        for( ProjectNature projectnature : natures ) {
          if( _natures.contains( projectnature ) ) {
            return true;
          }
        }
      }

      // there's no mapping so we don't have an abbreviation here
      return false;
    }

    // nature known:
    else {
      return true;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectNature> getNatures() {
    return _natures;
  }

  /**
   * @param referencedProject
   */
  public void addReferencedProject( String referencedProject ) {
    if( (referencedProject != null) && !_referencedProjects.contains( referencedProject ) ) {
      _referencedProjects.add( referencedProject );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getReferencedProjects() {
    return _referencedProjects;
  }

  /**
   * Adds the specified role to the EclipseProject.
   * 
   * @param role
   *          Adds the specified role to the EclipseProject.
   */
  public void addRole( ProjectRole role ) {
    Assure.notNull( "role", role );
    if( hasRole( role.getClass() ) ) {
      throw new RuntimeException( String.format( "ProjectRole %s is already set!", role.getClass() ) );
    }
    _roles.add( role );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRole( Class<? extends ProjectRole> projectRoleClass ) {
    Assure.notNull( "projectRoleClass", projectRoleClass );
    Iterator<ProjectRole> iterator = _roles.iterator();
    while( iterator.hasNext() ) {
      AbstractProjectRole role = (AbstractProjectRole) iterator.next();
      if( projectRoleClass.isAssignableFrom( role.getClass() ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings( "unchecked" )
  public <T extends ProjectRole> T getRole( Class<T> projectRoleClass ) {
    Assure.notNull( "projectRoleClass", projectRoleClass );
    Assure.assertTrue( hasRole( projectRoleClass ), String.format( "hasRole(projectRoleClass) on project '%s' has to be true for role '%s'!", getFolderName(), projectRoleClass ) );

    Iterator<ProjectRole> iterator = _roles.iterator();

    ProjectRole role = null;
    while( iterator.hasNext() ) {
      role = iterator.next();
      if( projectRoleClass.isAssignableFrom( role.getClass() ) ) {
        break;
      }
    }
    return (T) role;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectRole> getRoles() {
    return _roles;
  }

  /**
   * Adds the specified build command to the project.
   * 
   * @param command
   *          the specified build command to the project.
   */
  public void addBuildCommand( BuildCommand command ) {
    Assure.notNull( "command", command );
    _buildCommands.add( command );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBuildCommand( String commandName ) {
    Assure.notNull( "commandName", commandName );
    BuildCommand command = new BuildCommandImpl( commandName );
    return hasBuildCommand( command );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBuildCommand( BuildCommand command ) {
    Assure.notNull( "command", command );
    return _buildCommands.contains( command );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BuildCommand> getBuildCommands() {
    return _buildCommands;
  }

  /**
   * Adds a new linked resource to the project.
   * 
   * @param linkedResource
   *          the linked resource to add.
   */
  public void addLinkedResource( LinkedResourceImpl linkedResource ) {
    Assure.notNull( "linkedResource", linkedResource );
    if( !_linkedResources.contains( linkedResource ) ) {
      _linkedResources.add( linkedResource );
      _linkedResourceNames.add( linkedResource.getName() );
    }
  }

  /**
   * Returns a specific LinkedResource instance.
   * 
   * @param name
   *          The name of the desired LinkedResource instance.
   * 
   * @return The desired LinkedResource instance.
   */
  public LinkedResourceImpl getLinkedResource( String name ) {
    Assure.assertTrue( isLinkedResource( name ), String.format( "Cannot retrieve linked resource '%s' !", name ) );
    int idx = _linkedResourceNames.indexOf( name );
    return _linkedResources.get( idx );
  }

  /**
   * Returns true if the supplied name refers to a linked resource.
   * 
   * @param name
   *          The name of a potential linked resource.
   * 
   * @return true <=> The name applies to a specific linked resource.
   */
  public boolean isLinkedResource( String name ) {
    Assure.notNull( "name", name );
    return _linkedResourceNames.contains( name );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace() {
    return _workspace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[EclipseProject: name: %s folder: %s]", getSpecifiedName(), getFolder() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null ) {
      return false;
    }
    if( o.getClass() != getClass() ) {
      return false;
    }
    EclipseProjectImpl castedObj = (EclipseProjectImpl) o;
    if( _workspace == null ) {
      if( castedObj._workspace != null ) {
        return false;
      }
    } else {
      if( !_workspace.equals( castedObj._workspace ) ) {
        return false;
      }
    }
    if( _projectDirectory == null ) {
      if( castedObj._projectDirectory != null ) {
        return false;
      }
    } else {
      if( !_projectDirectory.equals( castedObj._projectDirectory ) ) {
        return false;
      }
    }
    if( _specifiedName == null ) {
      if( castedObj._specifiedName != null ) {
        return false;
      }
    } else {
      if( !_specifiedName.equals( castedObj._specifiedName ) ) {
        return false;
      }
    }
    if( _comment == null ) {
      if( castedObj._comment != null ) {
        return false;
      }
    } else {
      if( !_comment.equals( castedObj._comment ) ) {
        return false;
      }
    }
    if( _natures == null ) {
      if( castedObj._natures != null ) {
        return false;
      }
    } else {
      if( !_natures.equals( castedObj._natures ) ) {
        return false;
      }
    }
    if( _roles == null ) {
      if( castedObj._roles != null ) {
        return false;
      }
    } else {
      if( !_roles.equals( castedObj._roles ) ) {
        return false;
      }
    }
    if( _buildCommands == null ) {
      if( castedObj._buildCommands != null ) {
        return false;
      }
    } else {
      if( !_buildCommands.equals( castedObj._buildCommands ) ) {
        return false;
      }
    }
    if( _referencedProjects == null ) {
      if( castedObj._referencedProjects != null ) {
        return false;
      }
    } else {
      if( !_referencedProjects.equals( castedObj._referencedProjects ) ) {
        return false;
      }
    }
    if( _linkedResources == null ) {
      return castedObj._linkedResources == null;
    } else {
      return _linkedResources.equals( castedObj._linkedResources );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_projectDirectory == null ? 0 : _projectDirectory.hashCode());
    hashCode = 31 * hashCode + (_specifiedName == null ? 0 : _specifiedName.hashCode());
    hashCode = 31 * hashCode + (_comment == null ? 0 : _comment.hashCode());
    hashCode = 31 * hashCode + (_natures == null ? 0 : _natures.hashCode());
    hashCode = 31 * hashCode + (_roles == null ? 0 : _roles.hashCode());
    hashCode = 31 * hashCode + (_buildCommands == null ? 0 : _buildCommands.hashCode());
    hashCode = 31 * hashCode + (_linkedResources == null ? 0 : _linkedResources.hashCode());
    return hashCode;
  }
  
} /* ENDCLASS */
