/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.tools;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.jdt.model.JdtModelExceptionCode;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.model.pde.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;
import org.ant4eclipse.tools.pde.RequiredPluginsResolver;
import org.ant4eclipse.tools.pde.target.TargetPlatform;
import org.ant4eclipse.tools.pde.target.TargetPlatformRegistry;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BuildOrderResolver {

  // TODO: REFACTORING: USE TREESERIALISER AS BASECLASS!!!

  /**
   * Indicates that the resovler should fail if there is a non-java project in the team project set.
   */
  public final static int  FAIL_ON_NONJAVA_PROJECTS = 1;

  /**
   * Indicates the resolver should ignore all non-java projects in the team project set.
   */
  public final static int  IGNORE_NONJAVA_PROJECTS  = 2;

  /**
   * Indicates that non-java projectes found in the team project set should be inserted after the java projects
   */
  public final static int  PREPEND_NONJAVA_PROJECTS = 3;

  /**
   * Indicates that non-java projectes found in the team project set should be inserted before the java projects
   */
  public final static int  APPEND_NONJAVA_PROJECTS  = 4;

  /** the workspace */
  private final Workspace  _workspace;

  /**
   * location of platform used to access plugins
   */
  private File             _targetPlatformLocation;

  /** the team project names to resolve */
  private String[]         _projectNames;

  /** how to handle non java projects */
  private final int        _nonJavaProjectHandling;

  /** the projects to resolve */
  private EclipseProject[] _projects;

  /**
   * Resolves the build order for all projects in the teamProjectSet.
   * 
   * @param targetPlatformLocation
   *          Location of the target platform if a plugin must be resolved. If null, the workspace location is used as
   *          target location.
   * @throws BuildOrderException
   *           when a project referenced from a project contained in the teamProjectSet is not included in the
   *           teamProjectSet
   */
  public static EclipseProject[] resolveBuildOrder(final Workspace workspace, final File targetPlatformLocation,
      final TeamProjectSet teamProjectSet, final int nonJavaProjectHandling) {
    final BuildOrderResolver resolver = new BuildOrderResolver(workspace, targetPlatformLocation, teamProjectSet,
        nonJavaProjectHandling);
    return resolver.resolveBuildOrder();
  }

  /**
   * Resolves the build order for all projects in the projectNames.
   * 
   * @param targetPlatformLocation
   *          Location of the target platform if a plugin must be resolved. If null, the workspace location is used as
   *          target location.
   * @throws BuildOrderException
   *           when a project referenced from a project in projectNames is not included in projectNames
   */
  public static EclipseProject[] resolveBuildOrder(final Workspace workspace, final File targetPlatformLocation,
      final String[] projectNames, final int nonJavaProjectHandling) {
    final BuildOrderResolver resolver = new BuildOrderResolver(workspace, targetPlatformLocation, projectNames,
        nonJavaProjectHandling);
    return resolver.resolveBuildOrder();
  }

  /**
   * Resolves the build order for all projects
   * 
   * @param targetPlatformLocation
   *          Location of the target platform if a plugin must be resolved. If null, the workspace location is used as
   *          target location.
   * @throws BuildOrderException
   *           when a project referenced from a project in projectNames is not included in projectNames
   */
  public static EclipseProject[] resolveBuildOrder(final Workspace workspace, final File targetPlatformLocation,
      final EclipseProject[] projects, final int nonJavaProjectHandling) {
    final BuildOrderResolver resolver = new BuildOrderResolver(workspace, targetPlatformLocation, projects,
        nonJavaProjectHandling);
    return resolver.resolveBuildOrder();
  }

  /**
   * @param workspace
   * @param targetPlatformLocation
   *          Location of the target platform if a plugin must be resolved. If null, the workspace location is used as
   *          target location.
   * @param teamProjectSet
   *          Contains the projects that should be ordered
   * @param nonJavaProjectHandling
   */
  private BuildOrderResolver(final Workspace workspace, final File targetPlatformLocation,
      final TeamProjectSet teamProjectSet, final int nonJavaProjectHandling) {
    this(workspace, targetPlatformLocation, teamProjectSet.getProjectNames(), nonJavaProjectHandling);
  }

  /**
   * @param workspace
   * @param targetPlatformLocation
   *          Location of the target platform if a plugin must be resolved. May be null. If null, the workspace location
   *          is used as target location.
   * @param projectNames
   *          An array with project names in the workspace that should be ordered
   * @param nonJavaProjectHandling
   */
  private BuildOrderResolver(final Workspace workspace, final File targetPlatformLocation, final String[] projectNames,
      final int nonJavaProjectHandling) {
    Assert.notNull(workspace);
    Assert.notNull(projectNames);
    Assert.assertTrue((nonJavaProjectHandling >= FAIL_ON_NONJAVA_PROJECTS)
        && (nonJavaProjectHandling <= APPEND_NONJAVA_PROJECTS), "Invalid value for nonjavaProjectHandling");

    this._workspace = workspace;

    if (targetPlatformLocation != null) {
      Assert.isDirectory(targetPlatformLocation);
      this._targetPlatformLocation = targetPlatformLocation;
    }

    this._projectNames = projectNames;
    this._nonJavaProjectHandling = nonJavaProjectHandling;
  }

  private BuildOrderResolver(final Workspace workspace, final File targetPlatformLocation,
      final EclipseProject[] eclipseProjects, final int nonJavaProjectHandling) {
    Assert.notNull(workspace);
    Assert.notNull(eclipseProjects);
    Assert.assertTrue((nonJavaProjectHandling >= FAIL_ON_NONJAVA_PROJECTS)
        && (nonJavaProjectHandling <= APPEND_NONJAVA_PROJECTS), "Invalid value for nonjavaProjectHandling");

    this._workspace = workspace;

    if (targetPlatformLocation != null) {
      Assert.isDirectory(targetPlatformLocation);
      this._targetPlatformLocation = targetPlatformLocation;
    }
    this._projects = eclipseProjects;
    this._nonJavaProjectHandling = nonJavaProjectHandling;
  }

  /**
   * Computes the build order for the projects specified in projectNames
   * 
   * <p>
   * The projects must exist in the specified workspace.
   * </p>
   */
  protected EclipseProject[] resolveBuildOrder() {

    // nonJavaProjects will be set to null if the resolving should fail if
    // it detects non java-projects
    final List nonJavaProjects = (this._nonJavaProjectHandling != FAIL_ON_NONJAVA_PROJECTS ? new LinkedList() : null);

    if (this._projects == null) {
      this._projects = this._workspace.getProjects(this._projectNames, true);
    }

    final List unorderedProjects = createDependencyGraph(nonJavaProjects);

    final List orderedProjects = resolveBuildOrder(unorderedProjects);

    final int nonJavaProjectCount = ((this._nonJavaProjectHandling == APPEND_NONJAVA_PROJECTS) || (this._nonJavaProjectHandling == PREPEND_NONJAVA_PROJECTS)) ? nonJavaProjects
        .size()
        : 0;

    final EclipseProject[] result = new EclipseProject[orderedProjects.size() + nonJavaProjectCount];

    Iterator iterator = null;
    int count = 0;

    if (this._nonJavaProjectHandling == PREPEND_NONJAVA_PROJECTS) {
      iterator = nonJavaProjects.iterator();
      while (iterator.hasNext()) {
        final EclipseProject nonjavaProject = (EclipseProject) iterator.next();
        result[count] = nonjavaProject;
        count++;
      }
    }

    iterator = orderedProjects.iterator();
    while (iterator.hasNext()) {
      final DependencyNode wrapper = (DependencyNode) iterator.next();
      result[count] = wrapper.getProject();
      count++;
    }

    if (this._nonJavaProjectHandling == APPEND_NONJAVA_PROJECTS) {
      iterator = nonJavaProjects.iterator();
      while (iterator.hasNext()) {
        final EclipseProject nonjavaProject = (EclipseProject) iterator.next();
        result[count] = nonjavaProject;
        count++;
      }
    }

    return result;
  }

  /**
   * Creates an dependency graph for the given projects.
   * 
   * @param nonJavaProjects
   *          List non java projects will be added to or null if the resolver should fail on non java projects
   * 
   * @return A list of DependencyNode instances.
   * 
   * @throws BuildOrderException
   */
  private List createDependencyGraph(final List nonJavaProjects) {

    final Map projectWrapperMap = new Hashtable();

    // create dependency node...
    for (int i = 0; i < this._projects.length; i++) {
      final EclipseProject eclipseProject = this._projects[i];

      if (!JavaProjectRole.Helper.hasJavaProjectRole(eclipseProject)) {
        if (nonJavaProjects == null) {
          throw new Ant4EclipseException(JdtModelExceptionCode.NO_JAVA_PROJECT_ROLE, this._projects[i].getFolderName());
        }
        nonJavaProjects.add(eclipseProject);

      } else {

        final DependencyNode projectWrapper = new DependencyNode(this._projects[i]);
        projectWrapperMap.put(projectWrapper.getProjectName(), projectWrapper);

      }
    }

    // step 2: resolve the required projects...
    final Iterator iterator = projectWrapperMap.values().iterator();

    while (iterator.hasNext()) {
      final DependencyNode projectWrapper = (DependencyNode) iterator.next();

      final DependencyNode[] requiredProjects = resolveRequiredProjects(projectWrapper, projectWrapperMap);
      projectWrapper.addRequiredProjects(requiredProjects);
    }

    return new LinkedList(projectWrapperMap.values());
  }

  /**
   * @param project
   * @throws BuildOrderException
   */
  private DependencyNode[] resolveRequiredProjects(final DependencyNode project, final Map projectNodes) {
    Assert.notNull(projectNodes);
    Assert.notNull(project);

    final List requiredProjects = new LinkedList();

    final RawClasspathEntry[] classpathEntries = JavaProjectRole.Helper.getJavaProjectRole(project.getProject())
        .getRawClasspathEntries();
    for (int i = 0; i < classpathEntries.length; i++) {
      final RawClasspathEntry classpathEntry = classpathEntries[i];

      if (classpathEntry.getEntryKind() == RawClasspathEntry.CPE_PROJECT) {
        final String path = classpathEntry.getPath();
        final String projectName = path.substring(1);
        final DependencyNode requiredProject = (DependencyNode) projectNodes.get(projectName);

        if (requiredProject == null) {
          throw new Ant4EclipseException(JdtToolsExceptionCode.REFERENCE_TO_UNKNOWN_PROJECT_EXCEPTION, new Object[] {
              project.getProjectName(), projectName });
        }

        requiredProjects.add(requiredProject);

      } else if (classpathEntry.getEntryKind() == RawClasspathEntry.CPE_CONTAINER) {
        /**
         * @todo [19-Feb-2006:KASI] Requires some discussion ?
         */
        if (RequiredPluginsResolver.CONTAINER_TYPE_PDE_REQUIRED_PLUGINS.equals(classpathEntry.getPath())) {
          final BundleDescription bundle = PluginProjectRole.getPluginProjectRole(project.getProject())
              .getBundleDescription();

          if (bundle == null) {
            // final StringBuffer errMsg = new
            // StringBuffer("Required plugin '").append(project.getProjectName()).append(
            // "' missing in workspace at '").append(this._workspace.getAbsolutePath()).append("'");
            // if (this._targetPlatformLocation != null) {
            // errMsg.append(" and in target platform at '").append(this._targetPlatformLocation.getAbsolutePath())
            // .append("'");
            // }
            // throw new BuildOrderException(errMsg.toString());#
            // TODO
            throw new Ant4EclipseException(JdtToolsExceptionCode.REFERENCE_TO_UNKNOWN_BUNDLE_EXCEPTION);
          }

          final State state = getTargetPlatform().resolve(true);
          final BundleDescription resolvedBundle = state.getBundle(bundle.getSymbolicName(), bundle.getVersion());
          final BundleDescription[] preRequisits = resolvedBundle.getContainingState().getStateHelper()
              .getPrerequisites(new BundleDescription[] { resolvedBundle });
          for (int j = 0; j < preRequisits.length; j++) {
            final BundleDescription description = preRequisits[j];
            if (!description.getSymbolicName().equals(bundle.getSymbolicName())) {
              final DependencyNode node = (DependencyNode) projectNodes.get(description.getSymbolicName());
              if (node != null) {
                requiredProjects.add(node);
              }
            }
          }
        }
      }
    }

    return (DependencyNode[]) requiredProjects.toArray(new DependencyNode[0]);
  }

  protected TargetPlatform getTargetPlatform() {
    return TargetPlatformRegistry.getInstance(this._workspace, this._targetPlatformLocation);
  }

  /**
   * @param unorderedDependencyNodes
   * 
   * @return A sorted list.
   * 
   * @throws BuildOrderException
   */
  private List resolveBuildOrder(List unorderedDependencyNodes) {
    Assert.notNull(unorderedDependencyNodes);

    // the result list...
    final List result = new LinkedList();

    // iterate over the dependency nodes. All leafs are added to the result
    // list and removed from the list of unordered dependency nodes.
    final Iterator iterator = unorderedDependencyNodes.iterator();
    while (iterator.hasNext()) {
      final DependencyNode dependencyNode = (DependencyNode) iterator.next();
      if (dependencyNode.isLeaf()) {
        result.add(dependencyNode);
        iterator.remove();
      }
    }

    // we have a cyclic dependency if no leafs exists and the
    // list of unordered nodes is not empty
    if (result.isEmpty() && !unorderedDependencyNodes.isEmpty()) {
      throw new Ant4EclipseException(JdtToolsExceptionCode.CYCLIC_DEPENDENCIES_EXCEPTION);
    }

    // remove all leafs from the the list of unordered dependency nodes
    unorderedDependencyNodes = removeLeafs(unorderedDependencyNodes);
    if (!unorderedDependencyNodes.isEmpty()) {
      result.addAll(resolveBuildOrder(unorderedDependencyNodes));
    }

    return result;
  }

  /**
   * @param unorderedDependencyNodes
   * 
   * @return A modified list without leafs.
   */
  private List removeLeafs(final List unorderedDependencyNodes) {
    Assert.notNull(unorderedDependencyNodes);

    final Iterator unorderedDependencyNodesIterator = unorderedDependencyNodes.iterator();

    while (unorderedDependencyNodesIterator.hasNext()) {
      final DependencyNode projectNode = (DependencyNode) unorderedDependencyNodesIterator.next();

      final List requiredProjects = projectNode.getRequiredProjects();
      final Iterator requiredProjectsIterator = requiredProjects.iterator();

      while (requiredProjectsIterator.hasNext()) {
        final DependencyNode requiredProject = (DependencyNode) requiredProjectsIterator.next();

        if (!unorderedDependencyNodes.contains(requiredProject)) {
          requiredProjectsIterator.remove();
        }
      }
    }
    return unorderedDependencyNodes;
  }

  /**
   * A dependency node contains an eclipse project (that has to be a java project!) and a list of all required projects.
   * 
   * @created 13.04.2005
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  static private class DependencyNode {

    /** the project */
    private final EclipseProject _project;

    /** list of required projects */
    private final List           _requiredProjects;

    /**
     * @param project
     */
    public DependencyNode(final EclipseProject project) {
      Assert.notNull(project);

      this._project = project;
      this._requiredProjects = new LinkedList();
    }

    /**
     * Returns the project name.
     * 
     * @return The project name.
     */
    public String getProjectName() {
      return this._project.getFolderName();
    }

    /**
     * Returns the EclipseProject instance.
     * 
     * @return The EclipseProject instance.
     */
    public EclipseProject getProject() {
      return this._project;
    }

    /**
     * Returns a list of required EclipseProject instances.
     * 
     * @return A list of required EclipseProject instances.
     */
    public List getRequiredProjects() {
      return this._requiredProjects;
    }

    /**
     * Returns true if this node is a leaf.
     * 
     * @return true <=> This node is a leaf.
     */
    public boolean isLeaf() {
      return this._requiredProjects.isEmpty();
    }

    /**
     * @param projects
     */
    void addRequiredProjects(final DependencyNode[] projects) {
      Assert.notNull(projects);

      for (int i = 0; i < projects.length; i++) {
        final DependencyNode project = projects[i];
        this._requiredProjects.add(project);
      }
    }
  }
}