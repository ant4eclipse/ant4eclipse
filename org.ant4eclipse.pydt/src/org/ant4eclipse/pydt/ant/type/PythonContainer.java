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
package org.ant4eclipse.pydt.ant.type;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pydt.model.pyre.PythonRuntimeRegistry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import java.io.File;

/**
 * Container for all python runtimes that might be used.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonContainer extends AbstractAnt4EclipseDataType {

  private String  _defaultid;

  private boolean _sitepackages;

  /**
   * Initialises this container using the supplied ant project.
   * 
   * @param project
   *          The ant project used for the initialisation. Not <code>null</code>.
   */
  public PythonContainer(Project project) {
    super(project);
    this._defaultid = null;
    this._sitepackages = true;
  }

  /**
   * Changes the id of the default runtime.
   * 
   * @param defaultid
   *          The new id of the default runtime. Neither <code>null</code> nor empty.
   */
  public void setDefault(String defaultid) {
    this._defaultid = defaultid;
  }

  /**
   * Enables/disables the support for site packages.
   * 
   * @param enable
   *          <code>true</code> <=> Site packages are enabled.
   */
  public void setSitePackages(boolean enable) {
    this._sitepackages = enable;
  }

  /**
   * Creates the entry for a python runtime environment.
   * 
   * @return An entry for a python runtime environment. Not <code>null</code>.
   */
  public Runtime createPyre() {
    return new Runtime();
  }

  /**
   * Adds the supplied python runtime environment to this type after it has been configured..
   * 
   * @param runtime
   *          The python runtime environment configuration that shall be added. Not <code>null</code>.
   */
  public void addConfiguredPyre(Runtime runtime) {

    if (runtime._location == null) {
      throw new BuildException("Missing parameter 'location' on jre!");
    }

    if (!Utilities.hasText(runtime._id)) {
      throw new BuildException("Missing parameter 'id' on jre!");
    }

    PythonRuntimeRegistry registry = (PythonRuntimeRegistry) ServiceRegistry.instance().getService(
        PythonRuntimeRegistry.class.getName());

    registry.registerRuntime(runtime._id, runtime._location, this._sitepackages);

    if (runtime._id.equals(this._defaultid)) {
      registry.setDefaultRuntime(this._defaultid);
    }

  }

  /**
   * Basic datastructure used to collect the necessary information for a python runtime.
   */
  public static final class Runtime {

    private String _id;

    private File   _location;

    /**
     * Sets the id of the python runtime.
     * 
     * @param id
     *          The id of the python runtime.
     */
    public void setId(String id) {
      this._id = id;
    }

    /**
     * Sets the location of the python installation.
     * 
     * @param location
     *          The location of the python installation.
     */
    public void setLocation(File location) {
      this._location = location;
    }

  } /* ENDCLASSS */

} /* ENDCLASS */
