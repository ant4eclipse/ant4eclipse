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
package org.ant4eclipse.ant.platform;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.ant.platform.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolverService;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * Returns all referenced projects of a specified project
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class GetReferencedProjectsTask extends AbstractProjectBasedTask {

  /** the default seperator */
  public static final String DEFAULT_SEPARATOR = ",";

  /**
   * The name of an ant property that will hold the list of referenced projects
   */
  private String             _property;

  /**
   * An (optional) specified separator that is used to separate the project names (defaults to <b>
   * {@link #DEFAULT_SEPARATOR}</b>)
   */
  private String             _separator        = DEFAULT_SEPARATOR;

  /**
   * (Optional) specifies if required projects should be resolve recursive. Defaults to true.
   */
  private boolean            _recursive        = true;

  /**
   * <p>
   * Returns the name of an ant property that will hold the list of referenced projects.
   * </p>
   * 
   * @return the name of an ant property that will hold the list of referenced projects.
   */
  public String getProperty() {
    return this._property;
  }

  /**
   * <p>
   * Sets the name of an ant property that will hold the list of referenced projects.
   * </p>
   * 
   * @param property
   *          the name of an ant property that will hold the list of referenced projects.
   */
  public void setProperty(String property) {
    this._property = property;
  }

  /**
   * <p>
   * Returns an (optional) separator that is used to separate the project names.
   * </p>
   * 
   * @return an (optional) separator that is used to separate the project names.
   */
  public String getSeparator() {
    return this._separator;
  }

  /**
   * <p>
   * Sets an (optional) separator that is used to separate the project names.
   * </p>
   * 
   * @param separator
   *          an (optional) separator that is used to separate the project names.
   */
  public void setSeparator(String separator) {
    Assure.notNull("separator", separator);
    this._separator = separator;
  }

  /**
   * <p>
   * Returns if required projects should be resolve recursive.
   * </p>
   * 
   * @return true <=> Required projects should be resolved recursive.
   */
  public boolean isRecursive() {
    return this._recursive;
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
  public void setRecursive(boolean recursive) {
    this._recursive = recursive;
  }

  public boolean isPropertySet() {
    return Utilities.hasText(this._property);
  }

  public void requirePropertySet() {
    if (!isPropertySet()) {
      throw new BuildException("Missing parameter: 'property'");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {
    requirePropertySet();
    requireWorkspaceAndProjectNameSet();

    final EclipseProject eclipseProject = getEclipseProject();
    final List<EclipseProject> result = new LinkedList<EclipseProject>();

    resolveDependencies(ServiceRegistryAccess.instance().getService(ReferencedProjectsResolverService.class), result,
        isRecursive(), eclipseProject);

    StringBuilder builder = new StringBuilder();
    Iterator<EclipseProject> it = result.iterator();

    while (it.hasNext()) {
      builder.append(it.next().getSpecifiedName());
      if (it.hasNext()) {
        builder.append(getSeparator());
      }
    }

    getProject().setProperty(this._property, builder.toString());
  }

  private void resolveDependencies(final ReferencedProjectsResolverService referencedProjectsResolverService,
      final List<EclipseProject> result, final boolean recursive, final EclipseProject project) {
    List<EclipseProject> referencedProjects = referencedProjectsResolverService
        .resolveReferencedProjects(project, null);

    for (EclipseProject eclipseProject : referencedProjects) {
      if (!result.contains(eclipseProject)) {
        result.add(eclipseProject);

        if (recursive) {
          resolveDependencies(referencedProjectsResolverService, result, recursive, eclipseProject);
        }
      }

    }

  }

}
