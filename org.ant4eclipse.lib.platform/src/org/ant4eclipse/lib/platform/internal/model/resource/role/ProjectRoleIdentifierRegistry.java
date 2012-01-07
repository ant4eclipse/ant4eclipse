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
package org.ant4eclipse.lib.platform.internal.model.resource.role;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.platform.internal.model.resource.EclipseProjectImpl;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRoleIdentifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The ProjectRoleIdentifierRegistry holds all known {@link ProjectRoleIdentifier}s. It can be used to apply roles to
 * {@link EclipseProjectImpl}s
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class ProjectRoleIdentifierRegistry implements A4EService {

  private List<ProjectRoleIdentifier> roleidentifiers;

  private Map<String,ProjectNature[]> naturesmap;

  public ProjectRoleIdentifierRegistry() {

    roleidentifiers = new ArrayList<ProjectRoleIdentifier>();
    naturesmap = new Hashtable<String,ProjectNature[]>();
    roleidentifiers.addAll( A4ECore.instance().getServices( ProjectRoleIdentifier.class ) );

    Map<String,Set<ProjectNature>> naturesets = new Hashtable<String,Set<ProjectNature>>();
    for( ProjectRoleIdentifier identifier : roleidentifiers ) {

      Set<ProjectNature> natures = identifier.getNatures();
      if( (natures == null) || natures.isEmpty() ) {
        // no associated nature so there's no possible mapping
        continue;
      }

      List<String> abbreviations = identifier.getNatureNicknames();
      if( abbreviations == null ) {
        // no abbreviations so there's no possible mapping
        continue;
      }

      // associate all nicks with the corresponding natures list
      for( String abbreviation : abbreviations ) {
        Set<ProjectNature> currentnatures = naturesets.get( abbreviation );
        if( currentnatures == null ) {
          currentnatures = new HashSet<ProjectNature>();
          naturesets.put( abbreviation, currentnatures );
        }
        currentnatures.addAll( natures );
      }

    }

    for( Map.Entry<String,Set<ProjectNature>> entry : naturesets.entrySet() ) {
      naturesmap.put( entry.getKey(), entry.getValue().toArray( new ProjectNature[entry.getValue().size()] ) );
    }

  }

  /**
   * Returns all full nature IDs associated with the supplied nick.
   * 
   * @param nick
   *          The nick which will be used to access the natures. Neither <code>null</code> nor empty.
   * 
   * @return A list of associated <code>ProjectNature</code> instances. Maybe <code>null</code>.
   */
  public ProjectNature[] getNaturesForAbbreviation( String nick ) {
    return naturesmap.get( nick );
  }

  /**
   * Modifies the supplied project according to all currently registered RoleIdentifier instances.
   * 
   * @param project
   *          The project that shall be modified. Not <code>null</code>.
   */
  public void applyRoles( EclipseProjectImpl project ) {
    for( ProjectRoleIdentifier roleidentifier : roleidentifiers ) {
      if( roleidentifier.isRoleSupported( project ) ) {
        project.addRole( roleidentifier.createRole( project ) );
      }
    }
  }

  /**
   * Performs a post processing step for the roles. This might be necessary in situations where the applied role does
   * not have all information needed (especially when the information must be provided by other projects which have not
   * been processed at role application time).
   * 
   * @param project
   *          The project used for the post processing step. Not <code>null</code>.
   */
  public void postProcessRoles( EclipseProject project ) {
    for( ProjectRoleIdentifier roleidentifier : roleidentifiers ) {
      if( roleidentifier.isRoleSupported( project ) ) {
        roleidentifier.postProcess( project );
      }
    }
  }

  /**
   * Provides an {@link Iterable} which can be used to run through all currently registered
   * {@link ProjectRoleIdentifier} instances.
   * 
   * @return An {@link Iterable} which can be used to run through all currently registered {@link ProjectRoleIdentifier}
   *         instances. Not <code>null</code>.
   */
  public Iterable<ProjectRoleIdentifier> getProjectRoleIdentifiers() {
    return roleidentifiers;
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
