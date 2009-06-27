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
package org.ant4eclipse.pde.model.link;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;

/**
 * LinkFileFactory provides methods to find and parse link files
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class LinkFileFactory {

  /**
   * Returns LinkFile instances for each .link-file in the "links" subdirectory of the given directory. If either the
   * directory doesn't exist or it doesn't contain a "links" directory, an empty array is returned.
   * 
   * @param directory
   *          The directory that contains the "links" subdirectory
   * @return An array of LinkFile objects representing all .link-files found in the directory.
   */
  public static LinkFile[] getLinkFiles(final File directory) {
    Assert.notNull(directory);

    final File linksDir = new File(directory, "links");

    if (!linksDir.isDirectory()) {
      A4ELogging.debug("Links-directory '%s' does not exist", linksDir.getAbsolutePath());
      return new LinkFile[0];
    }
    A4ELogging.debug("Reading links-directory '%s'", linksDir.getAbsolutePath());
    final File[] links = linksDir.listFiles(new FilenameFilter() {
      public boolean accept(final File dir, final String name) {
        return (name.endsWith(".link"));
      }
    });

    final List<LinkFile> result = new LinkedList<LinkFile>();

    for (int i = 0; i < links.length; i++) {
      final File link = links[i];
      final LinkFile linkFile = parseLinkFile(link);
      if (linkFile != null) {
        result.add(linkFile);
      }
    }

    return result.toArray(new LinkFile[result.size()]);

  }

  /**
   * Parses the given linkFile and returns a LinkFile instance. If the link file doesn't contain a "path=" entry null is
   * returned.
   * 
   * <p>
   * Note that ths LinkFile returned might point to an "invalid" directory, for example if the directory the "path="
   * attribute points to, doesn't exists. Use {@link LinkFile#isValidDestination()} to check if the destination is
   * valid.
   * 
   * <p>
   * From the eclipse online help:
   * "The link file is a java.io.Properties format file which defines the path to the installed extension."
   * 
   * @return The LinkFile representing the target destination defined in the link file or null if the link file doesn't
   *         contain a path.
   * 
   */
  public static LinkFile parseLinkFile(final File linkFile) {
    Assert.isFile(linkFile);
    A4ELogging.debug("Parsing link file '%s'", linkFile.getAbsolutePath());

    final Properties p = new Properties();
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(linkFile);
      p.load(inputStream);
    } catch (final IOException ioe) {
      throw new RuntimeException("could not read " + linkFile.getAbsolutePath() + ": " + ioe, ioe);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (final IOException ioe) {
          // ignore
        }
      }
    }
    final String path = p.getProperty("path");
    if (path == null) {
      return null;
    }

    final File destination = new File(path);
    return new LinkFile(destination);
  }
}
