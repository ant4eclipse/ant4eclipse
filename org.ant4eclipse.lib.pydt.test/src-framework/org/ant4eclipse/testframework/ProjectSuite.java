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

import org.junit.Assert;

import java.net.URL;
import java.text.DecimalFormat;

/**
 * Helper class used to setup several python related projects.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProjectSuite implements ProjectSuiteApi {

  private static final String NAME_PREFIX          = "a4ePython%s";

  // the name to use for source folders with generated source
  private static final String NAME_GENERATEDSOURCE = "generated-source";

  private WorkspaceBuilder    _workspacebuilder;

  private boolean             _dltk;

  private int                 _count;

  private DecimalFormat       _formatter;

  private URL                 _sampleegg;

  private URL                 _samplejar;

  private URL                 _samplezip;

  /**
   * Initialises this project suite
   * 
   * @param wsbuilder
   *          The builder used for the workspace. Not <code>null</code>.
   * @param dltk
   *          <code>true</code> <=> Use a DLTK based python nature, PyDev otherwise.
   */
  public ProjectSuite(WorkspaceBuilder wsbuilder, boolean dltk) {
    Assert.assertNotNull(wsbuilder);
    this._workspacebuilder = wsbuilder;
    this._dltk = dltk;
    this._count = 1;
    this._formatter = new DecimalFormat("000");
    this._sampleegg = getResource("/org/ant4eclipse/pydt/test/data/sample.egg");
    this._samplejar = getResource("/org/ant4eclipse/pydt/test/data/sample.jar");
    this._samplezip = getResource("/org/ant4eclipse/pydt/test/data/sample.zip");
  }

  /**
   * Returns the location of a specific resource. This function causes an exception in case the resource could not be
   * located.
   * 
   * @param path
   *          The path of the resource (must be root based). Neither <code>null</code> nor empty.
   * 
   * @return The URL pointing to that resource. Not <code>null</code>.
   */
  private URL getResource(String path) {
    Assert.assertNotNull(path);
    URL result = getClass().getResource(path);
    if (result == null) {
      Assert.fail(String.format("The resource '%s' is not located on the classpath !", path));
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectDescription createEmptyProject(URL script, int projectsettings) {
    ProjectDescription result = new ProjectDescription();
    result.setPrimaryProjectname(newName());
    PythonProjectBuilder primarybuilder = newProjectBuilder(result.getPrimaryProjectname());
    primarybuilder.setBuildScript(script);
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSPRIMARY) != 0) {
      primarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
      result.addSourceFolder("/" + primarybuilder.getProjectName() + "/" + NAME_GENERATEDSOURCE);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYPRIMARY) != 0) {
      String prefix = "/" + result.getPrimaryProjectname() + "/";
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._samplezip), true);
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._sampleegg), true);
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._samplejar), true);
    }
    primarybuilder.populate(this._workspacebuilder);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectDescription createComplexProject(URL script, int projectsettings) {
    ProjectDescription result = new ProjectDescription();
    result.setPrimaryProjectname(newName());
    result.setSecondaryProjectname(newName());
    PythonProjectBuilder primarybuilder = newProjectBuilder(result.getPrimaryProjectname());
    primarybuilder.setBuildScript(script);
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSPRIMARY) != 0) {
      primarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
      result.addSourceFolder("/" + primarybuilder.getProjectName() + "/" + NAME_GENERATEDSOURCE);
    }
    PythonProjectBuilder secondarybuilder = newProjectBuilder(result.getSecondaryProjectname());
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSSECONDARY) != 0) {
      secondarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
      result.addSourceFolder("/" + secondarybuilder.getProjectName() + "/" + NAME_GENERATEDSOURCE);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYPRIMARY) != 0) {
      String prefix = "/" + result.getPrimaryProjectname() + "/";
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._samplezip), true);
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._sampleegg), true);
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._samplejar), true);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYSECONDARY) != 0) {
      String prefix = "/" + result.getSecondaryProjectname() + "/";
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(this._samplezip), false);
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(this._sampleegg), false);
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(this._samplejar), false);
    }
    primarybuilder.useProject(result.getSecondaryProjectname(), true);
    primarybuilder.populate(this._workspacebuilder);
    secondarybuilder.populate(this._workspacebuilder);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectDescription createCyclicProject(URL script, int projectsettings) {
    ProjectDescription result = new ProjectDescription();
    result.setPrimaryProjectname(newName());
    result.setSecondaryProjectname(newName());
    PythonProjectBuilder primarybuilder = newProjectBuilder(result.getPrimaryProjectname());
    primarybuilder.setBuildScript(script);
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSPRIMARY) != 0) {
      primarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
      result.addSourceFolder("/" + primarybuilder.getProjectName() + "/" + NAME_GENERATEDSOURCE);
    }
    PythonProjectBuilder secondarybuilder = newProjectBuilder(result.getSecondaryProjectname());
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSSECONDARY) != 0) {
      secondarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
      result.addSourceFolder("/" + secondarybuilder.getProjectName() + "/" + NAME_GENERATEDSOURCE);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYPRIMARY) != 0) {
      String prefix = "/" + result.getPrimaryProjectname() + "/";
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._samplezip), true);
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._sampleegg), true);
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(this._samplejar), true);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYSECONDARY) != 0) {
      String prefix = "/" + result.getSecondaryProjectname() + "/";
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(this._samplezip), false);
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(this._sampleegg), false);
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(this._samplejar), false);
    }
    primarybuilder.useProject(result.getSecondaryProjectname(), true);
    secondarybuilder.useProject(result.getPrimaryProjectname(), true);
    primarybuilder.populate(this._workspacebuilder);
    secondarybuilder.populate(this._workspacebuilder);
    return result;
  }

  /**
   * Creates a new instance of a PythonProjectBuilder with the supplied project name.
   * 
   * @param projectname
   *          The name of the project. Neither <code>null</code> nor empty.
   * 
   * @return The builder used to create the projects. Not <code>null</code>.
   */
  private PythonProjectBuilder newProjectBuilder(String projectname) {
    if (this._dltk) {
      return new DLTKProjectBuilder(projectname);
    } else {
      return new PyDevProjectBuilder(projectname);
    }
  }

  /**
   * Just a generative function to produce new names.
   * 
   * @return A newly generated name. Neither <code>null</code> nor empty.
   */
  private String newName() {
    return String.format(NAME_PREFIX, this._formatter.format(this._count++));
  }

} /* ENDCLASS */
