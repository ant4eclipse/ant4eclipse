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
package org.ant4eclipse.platform.model.team.cvssupport.project;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.PlatformExceptionCode;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.team.cvssupport.CvsRoot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>
 * Helper class for parsing cvs information.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CvsParser {

  /**
   * Returns whether the specified project
   * 
   * @param project
   *          The EclipseProject instance where to lookup the CVS root file.
   * 
   * @return true <=> The CVS project has been set.
   */
  public static boolean isCvsProject(EclipseProject project) {
    Assert.notNull(project);
    return project.hasChild("CVS" + File.separator + "Root");
  }

  /**
   * Reads the name of the CVS repository.
   * 
   * @param project
   *          The EclipseProject instance where to read the repository name from.
   * 
   * @return The name of the CVS repository.
   * 
   * @throws Ant4EclipseException
   *           Loading the content failed for some reason.
   */
  public static String readCvsRepositoryName(EclipseProject project) throws Ant4EclipseException {
    Assert.notNull(project);

    File cvsRepositoryFile = project.getChild("CVS" + File.separator + "Repository");

    return readFile(cvsRepositoryFile);
  }

  /**
   * Reads the content of the CVS root file.
   * 
   * @param project
   *          The EclipseProject instance where to look for a CVS root file.
   * 
   * @return A CVSRoot instance associated with the supplied project.
   * 
   * @throws Ant4EclipseException
   *           Loading the root file failed.
   */
  public static CvsRoot readCvsRoot(EclipseProject project) throws Ant4EclipseException {
    Assert.notNull(project);

    File cvsRootFile = project.getChild("CVS" + File.separator + "Root");

    String cvsRoot = readFile(cvsRootFile);
    return new CvsRoot(cvsRoot);
  }

  public static String readTag(EclipseProject project) throws Ant4EclipseException {
    Assert.notNull(project);

    if (!project.hasChild("CVS" + File.separator + "Tag")) {
      return null;
    }

    File tagFile = project.getChild("CVS" + File.separator + "Tag");

    String tag = readFile(tagFile);
    if (tag.length() <= 1) {
      return null;
    }
    return tag.substring(1);
  }

  /**
   * Reads the given file and returns its content as a String.
   * 
   * @param file
   *          The file to read
   * @return The file content
   * @throws Ant4EclipseException
   *           When reading the file fails for some reason
   */
  private static String readFile(File file) throws Ant4EclipseException {
    StringBuffer buffy = new StringBuffer();

    try {
      BufferedReader in = new BufferedReader(new FileReader(file));
      String str;

      while ((str = in.readLine()) != null) {
        buffy.append(str);
      }
      Utilities.close(in);
    } catch (IOException e) {
      throw new Ant4EclipseException(e, PlatformExceptionCode.ERROR_WHILE_READING_CVS_FILE, file, e.toString());
    }

    return buffy.toString();
  }

  /**
   * Private Constructor: no need to create an instance of type CvsParser.
   */
  private CvsParser() {
    // avoid creating instances
  }
}