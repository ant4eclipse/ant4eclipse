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
package org.ant4eclipse.pydt.test;

import org.ant4eclipse.core.Ant4EclipseConfigurator;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pydt.ant.GetPydtOutputPathTask;
import org.ant4eclipse.pydt.ant.GetPydtPythonPathTask;
import org.ant4eclipse.pydt.ant.GetPydtSourcePathTask;
import org.ant4eclipse.pydt.ant.PydtDocumentationTask;
import org.ant4eclipse.pydt.ant.type.PythonContainer;
import org.ant4eclipse.pydt.test.builder.WorkspaceBuilder;
import org.ant4eclipse.pydt.test.data.ProjectSuite;
import org.ant4eclipse.pydt.test.data.ProjectSuiteApi;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.net.URL;

/**
 * Abstract implementation of a test used in conjunction with a dynamically created workspace.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public abstract class AbstractWorkspaceBasedTest extends WorkspaceBuilder implements ProjectSuiteApi {

  /** @todo [17-Aug-2009:KASI] Maybe we should declare supported properties globally. */
  private static final String PROP_DISPOSEONEXIT = "ant4eclipse.disposeonexit";

  private static final String NAME_BUILDXML      = "build.xml";

  private boolean             _dltk;

  private ProjectSuite        _projectsuite;

  private boolean             _disposeonexit;

  /**
   * Intialises this workspace based test.
   * 
   * @param dltk
   *          <code>true</code> <=> Use the python dltk implementation.
   */
  public AbstractWorkspaceBasedTest(final boolean dltk) {
    _dltk = dltk;
    _disposeonexit = true;
    final String val = Utilities.cleanup(System.getProperty(PROP_DISPOSEONEXIT));
    if (val != null) {
      _disposeonexit = !"false".equals(val);
    }
  }

  /**
   * Returns the location of the buildfile within a specific project.
   * 
   * @param projectname
   *          The name of the project. Neither <code>null</code> nor empty.
   * 
   * @return The location of the ant buildfile. Not <code>null</code>.
   */
  private File getBuildFile(final String projectname) {
    final File projectdir = getProjectFolder(projectname);
    return new File(projectdir, NAME_BUILDXML);
  }

  /**
   * Executes the ant build script within a specific project.
   * 
   * @param projectname
   *          The name of the project which script shall be executed. Neither <code>null</code> nor empty.
   * @param target
   *          The name of the target which has to be executed. Neither <code>null</code> nor empty.
   * 
   * @return A result containing the generated information from the target. Not <code>null</code>.
   */
  public BuildResult execute(final String projectname, final String target) {
    return execute(projectname, target, null);
  }

  /**
   * Executes the ant build script within a specific project.
   * 
   * @param projectname
   *          The name of the project which script shall be executed. Neither <code>null</code> nor empty.
   * @param target
   *          The name of the target which has to be executed. Neither <code>null</code> nor empty.
   * @param dirsep
   *          A directory separator currently used for the replacement. If <code>null</code> the default
   *          {@link File#separator} will be used.
   * 
   * @return A result containing the generated information from the target. Not <code>null</code>.
   */
  public BuildResult execute(final String projectname, final String target, String dirsep) {

    Assert.assertNotNull(projectname);
    Assert.assertNotNull(target);

    if (dirsep == null) {
      dirsep = File.separator;
    }

    final File buildfile = getBuildFile(projectname);

    final Project project = new Project();
    project.init();

    project.setUserProperty(AntProperties.PROP_ANTFILE, buildfile.getAbsolutePath());
    setupProperties(project, projectname);
    extendDefinitions(project);

    final BuildResult result = new BuildResult(getWorkspaceFolder(), dirsep);
    result.assign(project);

    ProjectHelper.configureProject(project, buildfile);

    project.executeTarget(target);

    return result;

  }

  /**
   * Sets up the properties which are made part of the environment.
   * 
   * @param project
   *          The ant project used for the execution. Not <code>null</code>.
   * @param projectname
   *          The name of the eclipse project used for the testing. Not <code>null</code>.
   */
  protected void setupProperties(final Project project, final String projectname) {
    project.setUserProperty(AntProperties.PROP_PROJECTNAME, projectname);
    project.setUserProperty(AntProperties.PROP_WORKSPACEDIR, getWorkspaceFolder().getAbsolutePath());
  }

  /**
   * Allows to extend task and type definitions for the supplied project.
   * 
   * @param project
   *          The project used to configure. Not <code>null</code>.
   */
  protected void extendDefinitions(final Project project) {
    project.addTaskDefinition("getPythonSourcePath", GetPydtSourcePathTask.class);
    project.addTaskDefinition("getPythonOutputPath", GetPydtOutputPathTask.class);
    project.addTaskDefinition("getPythonPath", GetPydtPythonPathTask.class);
    project.addTaskDefinition("pythonDoc", PydtDocumentationTask.class);
    project.addDataTypeDefinition("pythonContainer", PythonContainer.class);
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
  protected URL getResource(final String path) {
    Assert.assertNotNull(path);
    final URL result = getClass().getResource(path);
    if (result == null) {
      Assert.fail(String.format("The resource '%s' is not located on the classpath !", path));
    }
    return result;
  }

  /**
   * Prepares the invocation of ant tasks.
   */
  @Before
  public void setup() {
    Ant4EclipseConfigurator.configureAnt4Eclipse();
    _projectsuite = new ProjectSuite(this, _dltk);
  }

  /**
   * Disposes all previously aquired resources.
   */
  @After
  public void teardown() {
    if (_disposeonexit) {
      dispose();
    }
    ServiceRegistry.reset();
    _dltk = false;
    _projectsuite = null;
  }

  /**
   * {@inheritDoc}
   */
  public String createEmptyProject(final URL script, final boolean multiplefolders) {
    return _projectsuite.createEmptyProject(script, multiplefolders);
  }

  /**
   * {@inheritDoc}
   */
  public String createComplexProject(final URL script, final boolean mainmultiple, final boolean secondarymultiple) {
    return _projectsuite.createComplexProject(script, mainmultiple, secondarymultiple);
  }

} /* ENDCLASS */
