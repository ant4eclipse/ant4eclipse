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

import org.ant4eclipse.ant.platform.core.SubElementAndAttributesComponent;
import org.ant4eclipse.ant.platform.core.delegate.SubElementAndAttributesDelegate;
import org.ant4eclipse.ant.platform.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolverService;
import org.apache.tools.ant.BuildException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Returns all referenced projects of a specified project
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class GetUsedProjectsTask extends AbstractProjectBasedTask implements SubElementAndAttributesComponent {

  /** the default seperator */
  private final static String             DEFAULT_SEPARATOR = ",";

  /**
   * The name of an ant property that will hold the list of referenced projects
   */
  private String                          _property;

  /**
   * An (optional) specified separator that is used to separate the project names (defaults to <b>
   * {@link #DEFAULT_SEPARATOR}</b>)
   */
  private String                          _separator;

  /**
   * Allows to enable self inclusion of the used project if set to <code>true</code>.
   */
  private boolean                         _selfinclude;

  /**
   * The reference type that is used for the resolving process of projects. A value of <code>null</code> means that all
   * reference types are being tried.
   */
  private String[]                        _referencetypes;

  /**
   *
   */
  private SubElementAndAttributesDelegate _subElementAndAttributesDelegate;

  public GetUsedProjectsTask() {
    this._subElementAndAttributesDelegate = new SubElementAndAttributesDelegate(this);
    this._referencetypes = null;
    this._selfinclude = false;
  }

  /**
   * Changes the reference type used to influence the project resolving process.
   * 
   * @param newreferencetype
   *          A reference type which depends on the current configuration of a4e.
   */
  public void setReferencetypes(String newreferencetypes) {
    String[] elements = null;
    if (newreferencetypes != null) {
      elements = newreferencetypes.split(",");
    }
    this._referencetypes = Utilities.cleanup(elements);
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
  public void setSeparator(String separator) {
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
  public void setSelfinclude(boolean selfinclude) {
    this._selfinclude = selfinclude;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object createDynamicElement(String name) throws BuildException {
    return this._subElementAndAttributesDelegate.createDynamicElement(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Object> getSubElements() {
    return this._subElementAndAttributesDelegate.getSubElements();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getSubAttributes() {
    return this._subElementAndAttributesDelegate.getSubAttributes();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDynamicAttribute(String name, String value) throws BuildException {
    this._subElementAndAttributesDelegate.setDynamicAttribute(name, value);
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
    if (this._referencetypes != null) {
      // check if we can use the provided reference type
      String[] allowed = getResolver().getReferenceTypes();
      for (String reftype : this._referencetypes) {
        if (!Utilities.contains(reftype, allowed)) {
          throw new BuildException("The 'referencetypes' value '" + reftype + "' is not supported.");
        }
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
    if (this._referencetypes != null) {
      // there's a restriction provided by the user
      types = this._referencetypes;
    }

    EclipseProject project = getEclipseProject();
    List<EclipseProject> referenced = new ArrayList<EclipseProject>();

    // load the directly referenced projects
    referenced.addAll(getResolver().resolveReferencedProjects(project, types, getSubElements()));

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
   * Returns the currently registered resolver service.
   * 
   * @return The currently registered resolver service. Not <code>null</code>.
   */
  private ReferencedProjectsResolverService getResolver() {
    return A4ECore.instance().getRequiredService( ReferencedProjectsResolverService.class );
  }

} /* ENDCLASS */
