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
package org.ant4eclipse.lib.platform.internal.tools;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolver;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolverService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Helper class that allows you to resolve referenced projects.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class ReferencedProjectsResolverServiceImpl implements ReferencedProjectsResolverService {

  private Map<String, ReferencedProjectsResolver>   referencedProjectsResolvers;
  private String[]                                  reftypes;
  
  public ReferencedProjectsResolverServiceImpl() {
    List<ReferencedProjectsResolver> resolvers = A4ECore.instance().getServices( ReferencedProjectsResolver.class );
    referencedProjectsResolvers = new Hashtable<String,ReferencedProjectsResolver>();
    for( ReferencedProjectsResolver resolver : resolvers ) {
      referencedProjectsResolvers.put( resolver.getReferenceType(), resolver );
    }
    reftypes = new String[ referencedProjectsResolvers.size() ];
    referencedProjectsResolvers.keySet().toArray( reftypes );
    Arrays.sort( reftypes );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getReferenceTypes() {
    return reftypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, String[] referencetypes,
      List<Object> additionalElements) {

    referencetypes = Utilities.cleanup( referencetypes );
    if( referencetypes == null ) {
      A4ELogging.warn( "The resolving process didn't come with at least one reference type." );
      return Collections.emptyList();
    }
    
    Set<EclipseProject> result = new HashSet<EclipseProject>();
    for( String reftype : referencetypes ) {
      if( referencedProjectsResolvers.containsKey( reftype ) ) {
        ReferencedProjectsResolver resolver = referencedProjectsResolvers.get( reftype );
        if( resolver.canHandle( project ) ) {
          result.addAll( resolver.resolveReferencedProjects( project, additionalElements ) );
        } else {
          A4ELogging.debug(
            "The reference type '%s' can't handle project '%s'.", 
            reftype, 
            project.getSpecifiedName()
          );
        }
      } else {
        A4ELogging.warn( "The reference type '%s' is not supported.", reftype );
      }
    }
    
    return new ArrayList<EclipseProject>( result );
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EclipseProject> resolveReferencedProjects( EclipseProject project, List<Object> additionalElements ) {
    return resolveReferencedProjects( project, getReferenceTypes(), additionalElements );
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
