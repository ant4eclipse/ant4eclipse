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
package org.ant4eclipse.platform.ant;

import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.ant.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolverService;

import org.apache.tools.ant.BuildException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Returns all referenced projects of a specified project
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetUsedProjectsTask extends AbstractProjectBasedTask {

  /** the default seperator */
  private final static String DEFAULT_SEPARATOR = ",";

  /**
   * The name of an ant property that will hold the list of referenced projects
   */
  private String              _property;

  /**
   * An (optional) specified separator that is used to separate the project names (defaults to <b>
   * {@link #DEFAULT_SEPARATOR}</b>)
   */
  private String              _separator;

  /**
   * (Optional - only with <code>source=project</code>) specifies if required projects should be resolve recursive.
   * Defaults to true.
   */
  private boolean             _recursive        = true;

  /**
   * Allows to enable self inclusion of the used project if set to <code>true</code>.
   */
  private boolean             _selfinclude      = false;

  /**
   * The reference type that is used for the resolving process of projects. A value of <code>null</code> means that all
   * reference types are being tried.
   */
  private String              _referencetype;

  /**
   * Changes the reference type used to influence the project resolving process.
   * 
   * @param newreferencetype
   *          A reference type which depends on the current configuration of a4e.
   */
  public void setReferencetype(final String newreferencetype) {
    this._referencetype = Utilities.cleanup(newreferencetype);
  }

  /**
   * <p>
   * Sets the name of an ant property that will hold the list of referenced projects.
   * </p>
   * 
   * @param property
   *          the name of an ant property that will hold the list of referenced projects.
   */
  public void setProperty(final String property) {
    this._property = Utilities.cleanup(property);
  }

  /**
   * <p>
   * Sets an (optional) separator that is used to separate the project names.
   * </p>
   * 
   * @param separator
   *          an (optional) separator that is used to separate the project names.
   */
  public void setSeparator(final String separator) {
    this._separator = Utilities.cleanup(separator);
  }

  /**
   * <p>
   * Enables/disables self inclusion of the current project.
   * </p>
   * 
   * @param selfinclude
   *          <code>true</code> <=> Enable self inclusion.
   */
  public void setSelfinclude(final boolean selfinclude) {
    this._selfinclude = selfinclude;
  }

  /**
   * <p>
   * Specifies if required projects should be resolve recursive. This attibute is optional and has to be specified only
   * when <code>source=project</code> . Defaults to true.
   * </p>
   * 
   * @param recursive
   *          <code>true</code> if required projects should be resolve recursive.
   */
  public void setRecursive(final boolean recursive) {
    this._recursive = recursive;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (this._property == null) {
      throw new BuildException("The attribute 'property' has not been set or is empty.");
    }
    /**
     * @todo [09-Jul-2009:KASI] Checking of parental classes should not be splitted up into multiple methods. This
     *       should happen within the parental {@link #preconditions()} method. The use of these splitted checks is no
     *       longer necessary since we've introduced the delegates providing the splitted checks, so the tasks
     *       themselves don't need to behave the same way.
     */
    requireWorkspaceAndProjectNameSet();
    if (this._referencetype != null) {
      // check if we can use the provided reference type
      if (!Utilities.contains(this._referencetype, getResolver().getReferenceTypes())) {
        throw new BuildException("The 'referencetype' value '" + this._referencetype + "' is not supported.");
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    if (this._separator == null) {
      this._separator = DEFAULT_SEPARATOR;
    }

    String[] types = getResolver().getReferenceTypes();
    if (this._referencetype != null) {
      // there's a restriction provided by the user
      types = new String[] { this._referencetype };
    }

    EclipseProject project = getEclipseProject();
    List<EclipseProject> referenced = new ArrayList<EclipseProject>();

    // load the directly referenced projects
    referenced.addAll(getResolver().resolveReferencedProjects(project, types, null));
    if (this._recursive) {
      // collect indirectly referenced projects if necessary
      collectReferencedProjects(types, referenced, 0);
    }

    if (this._selfinclude) {
      // include ourselves as requested
      referenced.add(0, project);
    }

    // generate the result value
    StringBuffer buffer = new StringBuffer();
    if (!referenced.isEmpty()) {
      buffer.append(referenced.get(0).getSpecifiedName());
      for (int i = 1; i < referenced.size(); i++) {
        buffer.append(this._separator);
        buffer.append(referenced.get(i).getSpecifiedName());
      }
    }

    getProject().setProperty(this._property, buffer.toString());

  }

  /**
   * Recursively collects all referenced projects.
   * 
   * @param referencetypes
   *          The reference types used to access their resolvers.
   * @param candidates
   *          The list with the projects that already have been processed. Not <code>null</code>.
   * @param pos
   *          The index within the list pointing to unresolved projects.
   */
  private void collectReferencedProjects(final String[] referencetypes, final List<EclipseProject> candidates,
      final int pos) {
    int newpos = candidates.size();
    for (int i = pos; i < candidates.size(); i++) {
      EclipseProject current = candidates.get(i);
      List<EclipseProject> referenced = getResolver().resolveReferencedProjects(current, referencetypes, null);
      for (int j = 0; j < referenced.size(); j++) {
        if (!candidates.contains(referenced.get(j))) {
          // don't add projects twice
          candidates.add(referenced.get(j));
        }
      }
    }
    if (newpos < candidates.size()) {
      // there are added projects, that needs to be resolved, too
      collectReferencedProjects(referencetypes, candidates, newpos);
    }
  }

  /**
   * Returns the currently registered resolver service.
   * 
   * @return The currently registered resolver service. Not <code>null</code>.
   */
  private ReferencedProjectsResolverService getResolver() {
    /**
     * @todo [09-Jul-2009:KASI] The inner convenience classes located in service interfaces should be removed. I'm just
     *       using this shortcut here in order to support refactoring in future.
     */
    return ReferencedProjectsResolverService.Helper.getService();
  }

} /* ENDCLASS */
