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
package org.ant4eclipse.pydt.test.builder;

import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

import org.ant4eclipse.pydt.model.project.PyDevProjectRole;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder which is used for the PyDev based python implementation.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PyDevProjectBuilder extends EclipseProjectBuilder implements PythonProjectBuilder {

  private static final String NAME_PYDEVPROJECT = ".pydevproject";

  private static final String ENC_UTF8          = "UTF-8";

  private String              _sourcepath;

  private List<String>        _sourcepathes;

  /**
   * Initialises this builder using the supplied project name.
   * 
   * @param projectname
   *          The name of the project used to be created. Neither <code>null</code> nor empty.
   */
  public PyDevProjectBuilder(final String projectname) {
    super(projectname);
    withNature(PyDevProjectRole.NATURE);
    withBuilder(PyDevProjectRole.BUILDCOMMAND);
    _sourcepath = "/" + projectname + "/src";
    _sourcepathes = new ArrayList<String>();
  }

  /**
   * {@inheritDoc}
   */
  public void useProject(final String projectname, final boolean export) {
    withProjectReference(projectname);
    if (!export) {
      /** @todo [14-Aug-2009:KASI] We need a message here, since each referred project is considered to be exported. */
    }
  }

  /**
   * {@inheritDoc}
   */
  protected void createArtefacts(final File projectdir) {
    super.createArtefacts(projectdir);
    writePyDevProject(new File(projectdir, NAME_PYDEVPROJECT));
  }

  /**
   * Generates a <code>.pydevproject</code> file representing the project configuration.
   * 
   * @param destination
   *          The destination where the file has to be written to.
   */
  private void writePyDevProject(final File destination) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"" + ENC_UTF8 + "\" standalone=\"no\"?>");
    buffer.append(NL);
    buffer.append("<?eclipse-pydev version=\"1.0\"?>");
    buffer.append(NL);
    buffer.append("<pydev_project>");
    buffer.append(NL);

    // write down the source pathes
    buffer.append("  <pydev_pathproperty name=\"org.python.pydev.PROJECT_SOURCE_PATH\">");
    buffer.append(NL);
    buffer.append("    <path>" + _sourcepath + "</path>");
    buffer.append(NL);
    for (int i = 0; i < _sourcepathes.size(); i++) {
      buffer.append("    <path>" + _sourcepathes.get(i) + "</path>");
      buffer.append(NL);
    }
    buffer.append("  </pydev_pathproperty>");
    buffer.append(NL);

    // write the runtime information
    buffer.append("<pydev_property name=\"org.python.pydev.PYTHON_PROJECT_VERSION\">python 2.6</pydev_property>");
    buffer.append(NL);
    buffer.append("<pydev_property name=\"org.python.pydev.PYTHON_PROJECT_INTERPRETER\">Default</pydev_property>");
    buffer.append(NL);

    buffer.append("</pydev_project>");
    Utilities.writeFile(destination, buffer.toString(), ENC_UTF8);
  }

  /**
   * {@inheritDoc}
   */
  public void setSourceFolder(final String sourcename) {
    _sourcepath = "/" + getProjectName() + "/" + sourcename;
  }

  /**
   * {@inheritDoc}
   */
  public void addSourceFolder(final String additionalfolder) {
    _sourcepathes.add("/" + getProjectName() + "/" + additionalfolder);
  }

} /* ENDCLASS */
