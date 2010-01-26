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
package org.ant4eclipse.platform.test.builder;

import static org.junit.Assert.assertNotNull;

import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.testframework.FileHelper;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates Eclipse projects for test purposes.
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class EclipseProjectBuilder {

  private String       _projectName;

  private List<String> _natures;

  private List<String> _builders;

  private List<String> _referencedProjects;

  public EclipseProjectBuilder(String projectName) {
    assertNotNull(projectName);
    this._projectName = projectName;
    this._natures = new LinkedList<String>();
    this._builders = new LinkedList<String>();
    this._referencedProjects = new LinkedList<String>();
  }

  /**
   * @return the projectName
   */
  public String getProjectName() {
    return this._projectName;
  }

  public EclipseProjectBuilder withNature(String natureId) {
    assertNotNull(natureId);
    this._natures.add(natureId);
    return this;
  }

  public EclipseProjectBuilder withProjectReference(String referencedProject) {
    assertNotNull(referencedProject);
    this._referencedProjects.add(referencedProject);
    return this;
  }

  public EclipseProjectBuilder withBuilder(String buildCmd) {
    assertNotNull(buildCmd);

    this._builders.add(buildCmd);
    return this;
  }

  /**
   * Creates this project
   * 
   * @param destinationDirectory
   *          the directory where this project(directory) should be created to
   * @return The project directory
   */
  public File createIn(File destinationDirectory) {
    Assert.isDirectory(destinationDirectory);
    File projectDir = new File(destinationDirectory, this._projectName);
    FileHelper.createDirectory(projectDir);

    createArtefacts(projectDir);
    return projectDir;
  }

  protected void createArtefacts(File projectDir) {
    createProjectFile(projectDir);
  }

  protected void createProjectFile(File projectDir) {
    StringBuffer dotProject = new StringBuffer();
    dotProject.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(Utilities.NL).append(
        "<projectDescription><name>").append(this._projectName).append("</name>").append("<comment/>").append(
        Utilities.NL);

    dotProject.append("<projects>");
    for (String referencedProject : this._referencedProjects) {
      dotProject.append("<project>").append(referencedProject).append("</project>").append(Utilities.NL);
    }

    dotProject.append("</projects>").append(Utilities.NL);

    dotProject.append(Utilities.NL).append("<buildSpec>").append(Utilities.NL);
    Iterator<String> it = this._builders.iterator();
    while (it.hasNext()) {
      String builder = it.next();
      dotProject.append("<buildCommand><name>").append(builder).append("</name><arguments/></buildCommand>").append(
          Utilities.NL);
    }
    dotProject.append("</buildSpec>").append(Utilities.NL);

    it = this._natures.iterator();
    dotProject.append("<natures>").append(Utilities.NL);
    while (it.hasNext()) {
      String builder = it.next();
      dotProject.append("<nature>").append(builder).append("</nature>").append(Utilities.NL);
    }
    dotProject.append("</natures>").append(Utilities.NL);
    dotProject.append("</projectDescription>").append(Utilities.NL);

    File dotProjectFile = new File(projectDir, ".project");
    Utilities.writeFile(dotProjectFile, dotProject.toString(), "UTF-8");
  }
}