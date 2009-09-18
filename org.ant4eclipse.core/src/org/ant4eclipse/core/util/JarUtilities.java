package org.ant4eclipse.core.util;

import org.ant4eclipse.core.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * <p>
 * Implements some utility methods to allow the unwrapping of a given jar file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JarUtilities {

  /**
   * <p>
   * Expands the specified jar file to the expansion directory.
   * </p>
   * 
   * @param jarFile
   *          the jar file to expand
   * @param expansionDirectory
   *          the expansion directory
   * @throws IOException
   */
  public static void expandJarFile(JarFile jarFile, File expansionDirectory) throws IOException {
    Assert.notNull(jarFile);
    Assert.notNull(expansionDirectory);

    if (!expansionDirectory.exists()) {
      if (!expansionDirectory.mkdirs()) {
        // TODO:
        throw new RuntimeException("Could not create expansion directory '" + expansionDirectory
            + "' for an unknown reason");
      }
    }

    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements()) {

      // TODO: not sure if we need this??
      ZipEntry zipEntry = fixDirectory(jarFile, entries.nextElement());

      File destFile = new File(expansionDirectory, zipEntry.getName());

      if (destFile.exists()) {
        continue;
      }

      // Create parent directory (if target is file) or complete directory (if target is directory)
      File directory = (zipEntry.isDirectory() ? destFile : destFile.getParentFile());
      if ((directory != null) && !directory.exists()) {
        if (!directory.mkdirs()) {
          throw new IOException("could not create directory '" + directory.getAbsolutePath() + " for an unkown reason.");
        }
      }

      if (!zipEntry.isDirectory()) {
        destFile.createNewFile();
        InputStream inputStream = jarFile.getInputStream(zipEntry);
        try {
          writeFile(inputStream, destFile);
        } finally {
          Utilities.close(inputStream);
        }
      }
    }
  }

  private static void writeFile(InputStream inputStream, File file) {
    Assert.notNull(inputStream);
    Assert.isFile(file);

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);

      byte buffer[] = new byte[1024];
      int count;
      while ((count = inputStream.read(buffer, 0, buffer.length)) > 0) {
        fos.write(buffer, 0, count);
      }
    } catch (IOException e) {
      /**
       * @todo [28-Jun-2009:KASI] The original code didn't take care of this, since it only closed the streams (now done
       *       within the finally sequence). Nevertheless the resource might not have been copied completely, so there's
       *       need to be a valid treatment.
       */
    } finally {
      // close open streams
      Utilities.close(fos);
      Utilities.close(inputStream);
    }
  }

  /**
   * <p>
   * Fixes a problem with <code>zipEntry.isDirectory()</code>
   * <p>
   * The <code>isDirectory()</code> method only returns true if the entry's name ends with a "/". This method checks if
   * the given entry is a directory even if the name does not end with a "/". If it is a directory the corresponding
   * entry from the jarFile will be returned (that is the entry with "/" at the end) In all other cases the entry
   * instance passed to this method is returned as-is.
   */
  private static ZipEntry fixDirectory(JarFile jarFile, ZipEntry entry) {
    if ((entry == null) || entry.isDirectory() || (entry.getSize() > 0)) {
      return entry;
    }

    String dirName = entry.getName() + "/";
    ZipEntry dirEntry = jarFile.getEntry(dirName);
    if (dirEntry != null) {
      return dirEntry;
    }

    return entry;
  }
}
