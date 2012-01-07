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
package org.ant4eclipse.lib.platform.model.resource.role;

import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>Default implementation of <code>ProjectRoleIdentifier</code>.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public abstract class AbstractProjectRoleIdentifier implements ProjectRoleIdentifier {

  private List<String>          nicknames;
  private Set<ProjectNature>    projectnatures;

  public AbstractProjectRoleIdentifier( String nature, String ... nicks ) {
    nicknames = new ArrayList<String>();
    for( String nick : nicks ) {
      nicknames.add( nick );
    }
    projectnatures = null;
    if( nature != null ) {
      projectnatures = ProjectNature.createNatures( nature );
    }
  }

  public AbstractProjectRoleIdentifier( String[] natures, String ... nicks ) {
    nicknames = new ArrayList<String>();
    for( String nick : nicks ) {
      nicknames.add( nick );
    }
    projectnatures = ProjectNature.createNatures( natures );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRoleSupported( EclipseProject project ) {
    if( projectnatures != null ) {
      for( ProjectNature nature : projectnatures ) {
        if( project.hasNature( nature ) ) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postProcess( EclipseProject project ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ProjectNature> getNatures() {
    return projectnatures;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getNatureNicknames() {
    return nicknames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }

} /* ENDCLASS */
