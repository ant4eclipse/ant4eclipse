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
package net.sf.ant4eclipse.model.platform.team.cvssupport.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.model.platform.resource.EclipseProject;
import net.sf.ant4eclipse.model.platform.resource.internal.factory.FileParserException;
import net.sf.ant4eclipse.model.platform.team.cvssupport.CvsRoot;

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
  public static boolean isCvsProject(final EclipseProject project) {
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
   * @throws FileParserException
   *           Loading the content failed for some reason.
   */
  public static String readCvsRepositoryName(final EclipseProject project) throws FileParserException {
    Assert.notNull(project);

    final File cvsRepositoryFile = project.getChild("CVS" + File.separator + "Repository");

    try {
      return readFile(cvsRepositoryFile);
    } catch (final IllegalArgumentException exception) {
      throw new FileParserException(exception.getMessage());
    } catch (final IOException exception) {
      throw new FileParserException(exception.getMessage());
    }
  }

  /**
   * Reads the content of the CVS root file.
   * 
   * @param project
   *          The EclipseProject instance where to look for a CVS root file.
   * 
   * @return A CVSRoot instance associated with the supplied project.
   * 
   * @throws FileParserException
   *           Loading the root file failed.
   */
  public static CvsRoot readCvsRoot(final EclipseProject project) throws FileParserException {
    Assert.notNull(project);

    final File cvsRootFile = project.getChild("CVS" + File.separator + "Root");

    try {
      final String cvsRoot = readFile(cvsRootFile);
      return new CvsRoot(cvsRoot);
    } catch (final IllegalArgumentException exception) {
      throw new FileParserException(exception.getMessage());
    } catch (final IOException exception) {
      throw new FileParserException(exception.getMessage());
    }
  }

  public static String readTag(final EclipseProject project) throws FileParserException {
    Assert.notNull(project);

    if (!project.hasChild("CVS" + File.separator + "Tag")) {
      return null;
    }

    final File tagFile = project.getChild("CVS" + File.separator + "Tag");

    try {
      final String tag = readFile(tagFile);
      if (tag.length() <= 1) {
        return null;
      }
      return tag.substring(1);
    } catch (final IllegalArgumentException exception) {
      throw new FileParserException(exception.getMessage());
    } catch (final IOException exception) {
      throw new FileParserException(exception.getMessage());
    }
  }

  private static String readFile(final File file) throws IOException {
    final StringBuffer buffy = new StringBuffer();

    try {
      final BufferedReader in = new BufferedReader(new FileReader(file));
      String str;

      while ((str = in.readLine()) != null) {
        buffy.append(str);
      }

      in.close();
    } catch (final IOException e) {
      // ignore - cannot do anything about it
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