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

import org.ant4eclipse.core.dependencygraph.DependencyGraph;
import org.ant4eclipse.core.dependencygraph.DependencyGraph.VertexRenderer;

import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

import java.util.Arrays;
import java.util.LinkedList;
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
  public static final List<EclipseProject> resolveBuildOrder(Workspace workspace, String[] projectNames,
      String[] referenceTypes, List<Object> additionalElements) {

    // retrieve all eclipse projects from the workspace
    EclipseProject[] eclipseProjects = workspace.getProjects(projectNames, true);

    // create a dependency graph
    DependencyGraph<EclipseProject> dependencyGraph = new DependencyGraph<EclipseProject>(
        new VertexRenderer<EclipseProject>() {
          public String renderVertex(EclipseProject eclipseProject) {
            return eclipseProject.getSpecifiedName();
          }
        });

    // iterate over the eclipse projects
    for (EclipseProject eclipseProject : eclipseProjects) {

      // System.err.println("TEST: Project " + eclipseProject.getSpecifiedName());

      if (!dependencyGraph.containsVertex(eclipseProject)) {
        dependencyGraph.addVertex(eclipseProject);
      }

      // resolve referenced projects
      List<EclipseProject> referencedProjects = referenceTypes != null ? ReferencedProjectsResolverService.Helper
          .getService().resolveReferencedProjects(eclipseProject, referenceTypes, additionalElements)
          : ReferencedProjectsResolverService.Helper.getService().resolveReferencedProjects(eclipseProject,
              additionalElements);

      // add referenced projects to the dependency graph
      for (EclipseProject referencedProject : referencedProjects) {

        if (!dependencyGraph.containsVertex(referencedProject)) {
          dependencyGraph.addVertex(referencedProject);
        }
        if (!eclipseProject.equals(referencedProject)) {
          dependencyGraph.addEdge(eclipseProject, referencedProject);
        }
      }
    }

    // calculate the order
    List<EclipseProject> orderProjects = dependencyGraph.calculateOrder();

    // filter result - only the requested projects should be listed
    List<EclipseProject> result = new LinkedList<EclipseProject>();
    List<String> names = Arrays.asList(projectNames);
    for (EclipseProject eclipseProject : orderProjects) {
      if (names.contains(eclipseProject.getSpecifiedName())) {
        result.add(eclipseProject);
      }
    }

    // return the result
    return result;
  }
}
