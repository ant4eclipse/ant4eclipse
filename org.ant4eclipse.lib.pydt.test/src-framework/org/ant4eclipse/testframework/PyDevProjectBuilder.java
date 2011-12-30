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

import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pydt.model.project.PyDevProjectRole;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Builder which is used for the PyDev based python implementation.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PyDevProjectBuilder extends AbstractPythonProjectBuilder {

  private static final String NAME_PYDEVPROJECT = ".pydevproject";

  private static final String ENC_UTF8          = "UTF-8";

  private String              _sourcepath;

  private List<String>        _sourcepathes;

  private Map<String, URL>    _internallibs;

  /**
   * Initialises this builder using the supplied project name.
   * 
   * @param projectname
   *          The name of the project used to be created. Neither <code>null</code> nor empty.
   */
  public PyDevProjectBuilder(String projectname) {
    super(projectname);
    withNature(PyDevProjectRole.NATURE);
    withBuilder(PyDevProjectRole.BUILDCOMMAND);
    /**
     * @note [17-Aug-2009:KASI] By default the PyDev uses 'src' as a source folder. We don't imitate this here as this
     *       allows to simplify the tests.
     */
    this._sourcepath = "/" + projectname;
    this._sourcepathes = new ArrayList<String>();
    this._internallibs = new Hashtable<String, URL>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void useProject(String projectname, boolean export) {
    withProjectReference(projectname);
    if (!export) {
      /** @todo [14-Aug-2009:KASI] We need a message here, since each referred project is considered to be exported. */
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createArtefacts(File projectdir) {
    super.createArtefacts(projectdir);
    writePyDevProject(new File(projectdir, NAME_PYDEVPROJECT));
    writeInternalLibraries(projectdir);
  }

  /**
   * Generates a <code>.pydevproject</code> file representing the project configuration.
   * 
   * @param destination
   *          The destination where the file has to be written to.
   */
  private void writePyDevProject(File destination) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"" + ENC_UTF8 + "\" standalone=\"no\"?>");
    buffer.append(Utilities.NL);
    buffer.append("<?eclipse-pydev version=\"1.0\"?>");
    buffer.append(Utilities.NL);
    buffer.append("<pydev_project>");
    buffer.append(Utilities.NL);

    // write down the source pathes
    buffer.append("  <pydev_pathproperty name=\"org.python.pydev.PROJECT_SOURCE_PATH\">");
    buffer.append(Utilities.NL);
    buffer.append("    <path>" + this._sourcepath + "</path>");
    buffer.append(Utilities.NL);
    for (int i = 0; i < this._sourcepathes.size(); i++) {
      buffer.append("    <path>" + this._sourcepathes.get(i) + "</path>");
      buffer.append(Utilities.NL);
    }
    for (Map.Entry<String, URL> entry : this._internallibs.entrySet()) {
      buffer.append("    <path>/" + getProjectName() + "/" + entry.getKey() + "</path>");
      buffer.append(Utilities.NL);
    }
    buffer.append("  </pydev_pathproperty>");
    buffer.append(Utilities.NL);

    // write the runtime information
    buffer.append("<pydev_property name=\"org.python.pydev.PYTHON_PROJECT_VERSION\">python 2.6</pydev_property>");
    buffer.append(Utilities.NL);
    buffer.append("<pydev_property name=\"org.python.pydev.PYTHON_PROJECT_INTERPRETER\">Default</pydev_property>");
    buffer.append(Utilities.NL);

    buffer.append("</pydev_project>");
    Utilities.writeFile(destination, buffer.toString(), ENC_UTF8);
  }

  /**
   * Exports the internal libraries into the project folder.
   * 
   * @param destination
   *          The destination directory of the project. Not <code>null</code> and must be a valid directory.
   */
  private void writeInternalLibraries(File destination) {
    for (Map.Entry<String, URL> entry : this._internallibs.entrySet()) {
      File destfile = new File(destination, entry.getKey());
      Utilities.mkdirs(destfile.getParentFile());
      Utilities.copy(entry.getValue(), destfile);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSourceFolder(String sourcename) {
    this._sourcepath = "/" + getProjectName() + "/" + sourcename;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSourceFolder(String additionalfolder) {
    this._sourcepathes.add("/" + getProjectName() + "/" + additionalfolder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String importInternalLibrary(URL location) {
    String file = location.getFile();
    int lidx = file.lastIndexOf('/');
    String relative = "lib/" + (lidx != -1 ? file.substring(lidx + 1) : file);
    this._internallibs.put(relative, location);
    return relative;
  }

} /* ENDCLASS */
