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
package org.ant4eclipse.testframework;


import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.testframework.EclipseProjectBuilder;

import java.io.File;
import java.net.URL;

/**
 * Base implementation for python related project builders.
 */
abstract class AbstractPythonProjectBuilder extends EclipseProjectBuilder implements PythonProjectBuilder {

  private static final String NAME_BUILDXML = "build.xml";

  private URL                 _buildscript;

  AbstractPythonProjectBuilder(String projectname) {
    super(projectname);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createArtefacts(File projectdir) {
    super.createArtefacts(projectdir);
    writeAntScript(new File(projectdir, NAME_BUILDXML));
  }

  /**
   * {@inheritDoc}
   */
  public File populate(WorkspaceBuilder workspacebuilder) {
    Assure.notNull(workspacebuilder);
    return workspacebuilder.addProject(this);
  }

  /**
   * {@inheritDoc}
   */
  public void setBuildScript(URL location) {
    Assure.notNull(location);
    this._buildscript = location;
  }

  /**
   * Writes the ANT script if one has been set.
   * 
   * @param destination
   *          The destination where the ANT script has been saved to. Not <code>null</code>.
   */
  private void writeAntScript(File destination) {
    if (this._buildscript != null) {
      Utilities.copy(this._buildscript, destination);
    }
  }

} /* ENDCLASS */
