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
package org.ant4eclipse.pydt.test.data;

import org.ant4eclipse.pydt.test.builder.DLTKProjectBuilder;
import org.ant4eclipse.pydt.test.builder.PyDevProjectBuilder;
import org.ant4eclipse.pydt.test.builder.PythonProjectBuilder;
import org.ant4eclipse.pydt.test.builder.WorkspaceBuilder;
import org.junit.Assert;

import java.net.URL;
import java.text.DecimalFormat;

/**
 * Helper class used to setup several python related projects.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProjectSuite implements ProjectSuiteApi {

  private static final String NAME_PREFIX = "a4ePython%s";

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
  public ProjectSuite(final WorkspaceBuilder wsbuilder, final boolean dltk) {
    Assert.assertNotNull(wsbuilder);
    _workspacebuilder = wsbuilder;
    _dltk = dltk;
    _count = 1;
    _formatter = new DecimalFormat("000");
    _sampleegg = getResource("/org/ant4eclipse/pydt/test/data/sample.egg");
    _samplejar = getResource("/org/ant4eclipse/pydt/test/data/sample.jar");
    _samplezip = getResource("/org/ant4eclipse/pydt/test/data/sample.zip");
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
  private URL getResource(final String path) {
    Assert.assertNotNull(path);
    final URL result = getClass().getResource(path);
    if (result == null) {
      Assert.fail(String.format("The resource '%s' is not located on the classpath !", path));
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectDescription createEmptyProject(final URL script, final int projectsettings) {
    final ProjectDescription result = new ProjectDescription();
    result.setPrimaryProjectname(newName());
    final PythonProjectBuilder primarybuilder = newProjectBuilder(result.getPrimaryProjectname());
    primarybuilder.setBuildScript(script);
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSPRIMARY) != 0) {
      primarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYPRIMARY) != 0) {
      final String prefix = "/" + result.getPrimaryProjectname() + "/";
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_samplezip));
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_sampleegg));
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_samplejar));
    }
    primarybuilder.populate(_workspacebuilder);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectDescription createComplexProject(final URL script, final int projectsettings) {
    final ProjectDescription result = new ProjectDescription();
    result.setPrimaryProjectname(newName());
    result.setSecondaryProjectname(newName());
    final PythonProjectBuilder primarybuilder = newProjectBuilder(result.getPrimaryProjectname());
    primarybuilder.setBuildScript(script);
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSPRIMARY) != 0) {
      primarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
    }
    final PythonProjectBuilder secondarybuilder = newProjectBuilder(result.getSecondaryProjectname());
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSSECONDARY) != 0) {
      secondarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYPRIMARY) != 0) {
      final String prefix = "/" + result.getPrimaryProjectname() + "/";
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_samplezip));
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_sampleegg));
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_samplejar));
    }
    if ((projectsettings & KIND_INTERNALLIBRARYSECONDARY) != 0) {
      final String prefix = "/" + result.getSecondaryProjectname() + "/";
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(_samplezip));
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(_sampleegg));
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(_samplejar));
    }
    primarybuilder.useProject(result.getSecondaryProjectname(), true);
    primarybuilder.populate(_workspacebuilder);
    secondarybuilder.populate(_workspacebuilder);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectDescription createCyclicProject(final URL script, final int projectsettings) {
    final ProjectDescription result = new ProjectDescription();
    result.setPrimaryProjectname(newName());
    result.setSecondaryProjectname(newName());
    final PythonProjectBuilder primarybuilder = newProjectBuilder(result.getPrimaryProjectname());
    primarybuilder.setBuildScript(script);
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSPRIMARY) != 0) {
      primarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
    }
    final PythonProjectBuilder secondarybuilder = newProjectBuilder(result.getSecondaryProjectname());
    if ((projectsettings & KIND_MULTIPLESOURCEFOLDERSSECONDARY) != 0) {
      secondarybuilder.addSourceFolder(NAME_GENERATEDSOURCE);
    }
    if ((projectsettings & KIND_INTERNALLIBRARYPRIMARY) != 0) {
      final String prefix = "/" + result.getPrimaryProjectname() + "/";
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_samplezip));
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_sampleegg));
      result.addInternalLibrary(prefix + primarybuilder.importInternalLibrary(_samplejar));
    }
    if ((projectsettings & KIND_INTERNALLIBRARYSECONDARY) != 0) {
      final String prefix = "/" + result.getSecondaryProjectname() + "/";
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(_samplezip));
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(_sampleegg));
      result.addInternalLibrary(prefix + secondarybuilder.importInternalLibrary(_samplejar));
    }
    primarybuilder.useProject(result.getSecondaryProjectname(), true);
    secondarybuilder.useProject(result.getPrimaryProjectname(), true);
    primarybuilder.populate(_workspacebuilder);
    secondarybuilder.populate(_workspacebuilder);
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
  private PythonProjectBuilder newProjectBuilder(final String projectname) {
    if (_dltk) {
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
    return String.format(NAME_PREFIX, _formatter.format(_count++));
  }

} /* ENDCLASS */
