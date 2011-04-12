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
package org.ant4eclipse.lib.platform.internal.model.resource.workspaceregistry;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.internal.model.resource.ChunkyFile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Reads a ".location" file which provides the position of an external project from eclipse's .metadata directory.
 * 
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class LocationFileParser {

  /**
   * Prefix that indicates that the location of a file is not an os-path but a URI. (Eclipse >=3.2)
   */
  private static final String URI_PREFIX = "URI//";

  /**
   * <p>
   * Reads the given <code>.location</code> file and returns the <b>existing</b> path to the eclipse project directory
   * stored in the <code>.location</code> file or <code>null</code> if the path doesn't point to a valid eclipse project
   * directory.
   * </p>
   * <p>
   * In opposite of {@link #readLocation(File)} this method validates the path from the .location file and only returns
   * a File object if the location points to a valid Eclipse project directory.
   * </p>
   * 
   * @param locationFile
   *          the location file to be parsed
   * @return the project directory or <code>null</code> if the location doesn't point to a valid project directory
   */
  public static final File getProjectDirectory(File locationFile) {
    Assure.isFile("locationFile", locationFile);

    try {
      // read the location of the project directory
      File projectDir = readLocation(locationFile);

      // check if projectDir is valid
      if (projectDir != null) {
        if (projectDir.isDirectory()) {
          File projectfile = new File(projectDir, ".project");
          if (projectfile.isFile()) {
            return projectDir;
          } else {
            A4ELogging.debug(
                "LocationFileParser.getProjectDirectory(): the project '%s' doesn't provide an Eclipse configuration",
                projectDir.getAbsolutePath());
          }
        } else {
          A4ELogging.debug("LocationFileParser.getProjectDirectory(): the stored location '%s' is not a directory",
              projectDir);
        }
      }
    } catch (IOException e) {
      // TODO: Logging
      e.printStackTrace();
    }

    // return default
    return null;
  }

  /**
   * Reads the given location file and returns the location that is stored inside the location as a File.
   * 
   * <p>
   * The file returned by this method may not exist, no check is performed here, thus you have to check for the
   * existence of the file (and propably for the correct type too).
   * </p>
   * 
   * <p>
   * You might want to use {@link #getProjectDirectory(File)} to receive a pointer to a valid, existing Eclipse project
   * file
   * </p>
   * 
   * @param locationfile
   *          The file which contains the desired data.
   * @return The location stored in the .location file (typically the root-folder of an external project) or null if no
   *         location could be read from the .location file
   * @throws IOException
   */
  static final File readLocation(File locationfile) throws IOException {
    Assure.isFile("locationfile", locationfile);

    ChunkyFile cf = new ChunkyFile(locationfile);
    if (cf.getChunkCount() == 1) {
      byte[] data = cf.getChunk(0);
      DataInputStream datain = new DataInputStream(new ByteArrayInputStream(data));
      String location = datain.readUTF();
      File file = null;
      if (location.length() > 0) {
        /*
         * see {@link org.eclipse.core.internal.resources.LocalMetaArea#readPrivateDescription(IProject target,
         * IProjectDescription description)}
         */
        if (location.startsWith(URI_PREFIX)) {
          URI uri = URI.create(location.substring(URI_PREFIX.length()));
          if (!uri.getScheme().startsWith("file")) {
            A4ELogging.debug("LocationFileParser.readLocation(): the stored location uri '%s' is not a file-uri", uri);
          } else {
            file = new File(uri);
          }
        } else {
          try {
            // try to interprete the location as a URI
            file = new File(new URI(location));
          } catch (URISyntaxException ex) {
            // fallback mechanism which interprets the location as a simple path
            A4ELogging.debug("LocationFileParser.readLocation(): the location '%s' will be interpreted as a path",
                location);
            file = new File(location);
          } catch (IllegalArgumentException ex) {
            // fallback mechanism which interprets the location as a simple path.
            // this can happen if the location doesn't conform to the current system
            // (f.e. a location file for unix which is used while ANT is executed unter
            // windows)
            A4ELogging.debug("LocationFileParser.readLocation(): the location '%s' will be interpreted as a path",
                location);
            file = new File(location);
          }
        }
        return file;
      }
    } else {
      A4ELogging.warn("the file '%s' contains %d chunks instead of a single one", locationfile,
          Integer.valueOf(cf.getChunkCount()));
    }

    return null;
  }
}
