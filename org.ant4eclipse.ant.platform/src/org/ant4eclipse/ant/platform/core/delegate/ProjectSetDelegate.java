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
package org.ant4eclipse.ant.platform.core.delegate;

import org.ant4eclipse.ant.platform.core.ProjectSetComponent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectSetDelegate extends TeamProjectSetDelegate implements ProjectSetComponent {

  private List<String>   _projectNames;

  /**
   * Creates a new instance of type {@link ProjectSetDelegate}.
   * 
   * @param component
   *          the project component
   */
  public ProjectSetDelegate( ProjectComponent component ) {
    super( component );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setProjectNames( String projectNames ) {
    if( projectNames == null ) {
      _projectNames = new ArrayList<String>();
    } else {
      String[] names = projectNames.split( "," );
      _projectNames = new ArrayList<String>();
      for( int i = 0; i < names.length; i++ ) {
        _projectNames.add( names[i].trim() );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getProjectNames() {
    if( isTeamProjectSetSet() ) {
      return getTeamProjectSet().getProjectNames();
    } else {
      return _projectNames;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isProjectNamesSet() {
    return _projectNames != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireProjectNamesSet() {
    if( !isProjectNamesSet() ) {
      // TODO
      throw new BuildException( "projectNames has to be set!" );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireTeamProjectSetOrProjectNamesSet() {
    if( !isProjectNamesSet() && !isTeamProjectSetSet() ) {
      // TODO
      throw new BuildException( "projectNames or teamProjectSet has to be set!" );
    }
  }
  
} /* ENDCLASS */
