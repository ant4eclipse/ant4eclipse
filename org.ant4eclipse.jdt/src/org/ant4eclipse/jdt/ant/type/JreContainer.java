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
package org.ant4eclipse.jdt.ant.type;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.jdt.model.ContainerTypes;
import org.ant4eclipse.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.jdt.model.jre.JavaRuntimeRegistry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * A datatype used as a container for classpathes.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class JreContainer extends AbstractAnt4EclipseDataType {

  private String _defaultJre;

  /**
   * Simply initialises this new type.
   * 
   * @param project
   *          The project this type applies to.
   */
  public JreContainer(final Project project) {
    super(project);
    this._defaultJre = null;
  }

  /**
   * Creates the entry for a java runtime environment.
   * 
   * @return An entry for a java runtime environment.
   */
  public Runtime createJre() {
    return (new Runtime());
  }

  public void setDefault(final String defaultJre) {
    this._defaultJre = defaultJre;
  }

  /**
   * Adds the supplied java runtime environment to this type after it has been configured..
   * 
   * @param runtime
   *          The java runtime environment configuration that shall be added.
   */
  public void addConfiguredJre(final Runtime runtime) {
    final File location = runtime.getLocation();
    if (location == null) {
      throw new BuildException("Missing parameter 'location' on jre!");
    }

    if (!Utilities.hasText(runtime.getId())) {
      throw new BuildException("Missing parameter 'id' on jre!");
    }

    final boolean isDefault = runtime.getId().equals(this._defaultJre);

    final JavaRuntimeRegistry javaRuntimeRegistry = (JavaRuntimeRegistry) ServiceRegistry.instance().getService(
        JavaRuntimeRegistry.class.getName());

    final JavaRuntime javaRuntime = javaRuntimeRegistry.registerJavaRuntime(runtime.getId(), runtime.getLocation());

    Assert.notNull(javaRuntime);

    if (isDefault) {
      javaRuntimeRegistry.setDefaultJavaRuntime(runtime.getId());
    }

    final Path path = new Path(getProject());
    final File[] libraries = javaRuntime.getLibraries();
    for (final File librarie : libraries) {
      path.createPathElement().setLocation(librarie);
    }

    getProject().addReference(ContainerTypes.VMTYPE_PREFIX + runtime.getId(), path);

    // register default JRE as JRE_CONTAINER too
    if (isDefault) {
      A4ELogging.debug("Registered default JRE with id '%s'", ContainerTypes.JRE_CONTAINER);
      getProject().addReference(ContainerTypes.JRE_CONTAINER, path);
    }
  }

  public static class Runtime {

    private String _id;

    private File   _location;

    public String getId() {
      return this._id;
    }

    public void setId(final String id) {
      this._id = id;
    }

    public File getLocation() {
      return this._location;
    }

    public void setLocation(final File location) {
      this._location = location;
    }
  }
} /* ENDCLASS */
