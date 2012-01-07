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
package org.ant4eclipse.lib.platform.tools;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.dependencygraph.DependencyGraph;
import org.ant4eclipse.lib.core.dependencygraph.VertexRenderer;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Resolves the build order of the projects with the given names.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BuildOrderResolver {

  /**
   * <p>
   * Resolves the build order of the projects with the given names.
   * </p>
   * 
   * @param workspace
   *          the workspace
   * @param projectNames
   *          an array with the names of all projects that should be sorted.
   * @param referenceTypes
   * @param additionalElements
   * @return
   */
  public static final List<EclipseProject> resolveBuildOrder( Workspace workspace, List<String> projectNames, List<String> referenceTypes, List<Object> additionalElements ) {

    // retrieve all eclipse projects from the workspace
    List<EclipseProject> eclipseProjects = workspace.getProjects( projectNames, true );

    // create a dependency graph
    DependencyGraph<EclipseProject> dependencyGraph = new DependencyGraph<EclipseProject>(
        new VertexRenderer<EclipseProject>() {
          @Override
          public String renderVertex( EclipseProject eclipseProject ) {
            return eclipseProject.getSpecifiedName();
          }
        } );

    // iterate over the eclipse projects
    for( EclipseProject eclipseProject : eclipseProjects ) {

      if( !dependencyGraph.containsVertex( eclipseProject ) ) {
        dependencyGraph.addVertex( eclipseProject );
      }

      // resolve referenced projects
      ReferencedProjectsResolverService resolverservice = A4ECore.instance().getRequiredService( ReferencedProjectsResolverService.class );
      if( referenceTypes == null ) {
        referenceTypes = resolverservice.getReferenceTypes();
      }

      List<EclipseProject> referencedProjects = resolverservice.resolveReferencedProjects( eclipseProject, referenceTypes, additionalElements );

      // add referenced projects to the dependency graph
      for( EclipseProject referencedProject : referencedProjects ) {

        if( !dependencyGraph.containsVertex( referencedProject ) ) {
          dependencyGraph.addVertex( referencedProject );
        }
        if( !eclipseProject.equals( referencedProject ) ) {
          dependencyGraph.addEdge( eclipseProject, referencedProject );
        }
      }
    }

    // calculate the order
    List<EclipseProject> orderProjects = dependencyGraph.calculateOrder();

    // filter result - only the requested projects should be listed
    List<EclipseProject> result = new ArrayList<EclipseProject>();
    for( EclipseProject eclipseProject : orderProjects ) {
      if( projectNames.contains( eclipseProject.getSpecifiedName() ) ) {
        result.add( eclipseProject );
      }
    }
    return result;
    
  }
  
} /* ENDCLASS */
