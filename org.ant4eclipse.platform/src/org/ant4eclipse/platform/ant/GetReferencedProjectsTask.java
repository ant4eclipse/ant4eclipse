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

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectBasedTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * <p>
 * Returns all referenced projects of a specified project
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class GetReferencedProjectsTask extends AbstractProjectBasedTask {

  /** the default seperator */
  public final static String DEFAULT_SEPARATOR = ",";

  /** read projects from classpath (default) */
  public final static String SOURCE_CLASSPATH  = "classpath";

  /** read projects from .project file */
  public final static String SOURCE_PROJECT    = "project";

  /**
   * The name of an ant property that will hold the list of referenced projects
   */
  private String             _property;

  /** Where to read the referenced projects from: .classpath or .project */
  private String             _source           = SOURCE_CLASSPATH;

  /**
   * An (optional) specified separator that is used to separate the project names (defaults to <b>
   * {@link #DEFAULT_SEPARATOR}</b>)
   */
  private String             _separator        = DEFAULT_SEPARATOR;

  /**
   * (Optional - only with <code>source=classpath</code>) specifies if only exported projects should be listed
   * (equivalent to runtime classpath). Defaults to <b>false</b>.
   */
  private boolean            _exportedOnly     = false;

  /**
   * (Optional - only with <code>source=project</code>) specifies if required projects should be resolve recursive.
   * Defaults to true.
   */
  private boolean            _recursive        = true;

  /** The property used to describe the unavailable projects. */
  private String             _unavailableProjects;

  /**
   * Use the specified name of an Eclipse project rather than it's filesystem name.
   */
  private boolean            _specifiedNames   = false;

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
  public void setProperty(final String property) {
    this._property = property;
  }

  /**
   * <p>
   * Sets the name of an ANT property that will hold the list of projects that are not available.
   * </p>
   * 
   * @param property
   *          the name of an ANT property that will hold the list of projects that are not available.
   */
  public void setUnavailableProjects(final String property) {
    this._unavailableProjects = property;
  }

  /**
   * <p>
   * Enables the use of specified names which might differ from the project name within the file system.
   * </p>
   * 
   * @param specifiednames
   *          <code>true</code> <=> Create the list using the specified names.
   */
  public void setSpecifiedNames(final boolean specifiednames) {
    this._specifiedNames = specifiednames;
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
  public void setSeparator(final String separator) {
    Assert.notNull(separator);
    this._separator = separator;
  }

  /**
   * <p>
   * Return where to read the referenced projects from. Allowed values are 'classpath' or 'project'.
   * </p>
   * 
   * @return where to read the referenced projects from.
   */
  public String getSource() {
    return this._source;
  }

  /**
   * <p>
   * Sets where to read the referenced projects from. Allowed values are 'classpath' or 'project'.
   * </p>
   * 
   * @param source
   *          where to read the referenced projects from.
   */
  public void setSource(final String source) {
    this._source = source;
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
  public void setRecursive(final boolean recursive) {
    this._recursive = recursive;
  }

  public boolean isExportedOnly() {
    return this._exportedOnly;
  }

  public void setExportedOnly(final boolean exportedOnly) {
    this._exportedOnly = exportedOnly;
  }

  public boolean isPropertySet() {
    return Utilities.hasText(this._property);
  }

  public void requirePropertySet() {
    if (!isPropertySet()) {
      throw new BuildException("Missing parameter: 'property'");
    }
  }

  protected void requireSourceSet() {
    if (!Utilities.hasText(getSource())) {
      throw new BuildException("Missing parameter: 'source'");
    }
    if (!SOURCE_CLASSPATH.equals(getSource()) && !SOURCE_PROJECT.equals(getSource())) {
      throw new BuildException("Invalid 'source' parameter value. Must be '" + SOURCE_CLASSPATH + "' or '"
          + SOURCE_PROJECT + "'");
    }
  }

  public boolean isReadFromProject() {
    return SOURCE_PROJECT.equals(getSource());
  }

  public boolean isReadFromClasspath() {
    return SOURCE_CLASSPATH.equals(getSource());
  }

  @Override
  protected void doExecute() {
    requirePropertySet();
    requireSourceSet();
    requireWorkspaceAndProjectNameSet();

    throw new UnsupportedOperationException("Not implemented yet");

    // EclipseProject[] referencedProjects;
    // try {
    //
    // List rejected = null;
    // if (this._unavailableProjects != null) {
    // rejected = new LinkedList();
    // }
    // if (isReadFromClasspath()) {
    // referencedProjects = ReferencedProjectsResolver.getProjectsReferencedByClasspath(getEclipseProject(),
    // isExportedOnly(), rejected);
    // } else {
    // referencedProjects = ReferencedProjectsResolver.getReferencedProjects(getEclipseProject(), isRecursive(),
    // rejected);
    // }
    //
    // final StringBuffer result = new StringBuffer();
    // if (this._specifiedNames) {
    // result.append(referencedProjects[0].getSpecifiedName());
    // } else {
    // result.append(referencedProjects[0].getFolderName());
    // }
    // for (int i = 1; i < referencedProjects.length; i++) {
    // result.append(getSeparator());
    // if (this._specifiedNames) {
    // result.append(referencedProjects[i].getSpecifiedName());
    // } else {
    // result.append(referencedProjects[i].getFolderName());
    // }
    // }
    // A4ELogging.debug("Setting '%s' to list of referenced projects '%s'", new Object[] { getProperty(), result });
    // getProjectDelegate().setStringProperty(getProperty(), result.toString());
    //
    // if ((rejected != null) && (!rejected.isEmpty())) {
    // result.setLength(0);
    // result.append(rejected.get(0));
    // for (int i = 1; i < rejected.size(); i++) {
    // result.append(getSeparator());
    // result.append(rejected.get(i));
    // }
    // A4ELogging.debug("Setting '%s' to list of rejected projects '%s'", new Object[] { this._unavailableProjects,
    // result });
    // getProjectDelegate().setStringProperty(this._unavailableProjects, result.toString());
    // }
    //
    // } catch (final BuildException ex) {
    // throw ex;
    // } catch (final Exception ex) {
    // A4ELogging.debug(ex.getMessage());
    // throw new BuildException(ex.getMessage(), ex);
    // }
  }

  /**
   * <p>
   * </p>
   * 
   * @author Nils Hartmann (nils@nilshartmann.net)
   */
  public static class NonJavaProjectHandling extends EnumeratedAttribute {

    /**
     * Creates a new instance of type NonJavaProjectHandling.
     */
    public NonJavaProjectHandling() {
      // default cstr
    }

    /**
     * Creates a new instance of type NonJavaProjectHandling.
     * 
     * @param value
     *          the yalue to be set.
     */
    public NonJavaProjectHandling(final String value) {
      super();
      setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getValues() {
      return new String[] { "fail", "ignore", "prepend", "append" };
    }

    /**
     * @return
     */
    public int asBuildOrderResolverConstant() {
      return getIndex() + 1;
    }
  } /* ENDCLASS */
}
