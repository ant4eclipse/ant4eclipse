/**********************************************************************
 * Copyright (c) 2005-2012 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christoph Läubrich
 **********************************************************************/
package org.ant4eclipse.ant.pde;

import java.io.File;

import org.ant4eclipse.ant.pde.TargetPlatformDefinitionDataType.Location;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Reads the location entries of an eclipse target definition file
 * 
 * @author Christoph Läubrich
 * 
 */
public class TargetPlattformContentHandler extends DefaultHandler {

  /**
   * 
   */
  private static final String                    ECLIPSE_VAR_PROJECT_LOC = "${project_loc}";

  /**
   * The target file that is parsed
   */
  private final File                             targetFile;

  /**
   * the {@link TargetPlatformDefinitionDataType} for adding locations
   */
  private final TargetPlatformDefinitionDataType dataType;

  /**
   * The variable project loc
   */
  private String                                 projectLoc;

  /**
   * @param targetFile
   *          the file of the target to parse
   * @param dataType
   *          the {@link TargetPlatformDefinitionDataType} for adding locations
   */
  public TargetPlattformContentHandler(File targetFile, TargetPlatformDefinitionDataType dataType) {
    this.targetFile = targetFile;
    this.dataType = dataType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
   * org.xml.sax.Attributes)
   */
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if ("location".equals(qName)) {
      String type = attributes.getValue("type");
      if ("Directory".equals(type)) {
        String path = attributes.getValue("path");
        if (path != null) {
          if (path.contains(ECLIPSE_VAR_PROJECT_LOC)) {
            path = replaceProjectLoc(path);
          }
          A4ELogging.info("Add path '%s' from target definition.", path);
          File file = new File(path);
          Location location = new TargetPlatformDefinitionDataType.Location(file);
          this.dataType.addConfiguredLocation(location);
        } else {
          A4ELogging.warn("Directory entry without path encountered, ignored!");
        }
      } else {
        A4ELogging.warn("Only Directory entries are supported at the momment, entry with type %s ignored!", type);
      }

    }

  }

  /**
   * replaces the project location in a String
   * 
   * @param path
   * @return
   */
  private synchronized String replaceProjectLoc(String path) {
    if (this.projectLoc == null) {
      this.projectLoc = findProjectLoc(this.targetFile.getParentFile());
    }
    return path.replace(ECLIPSE_VAR_PROJECT_LOC, this.projectLoc);
  }

  /**
   * searches the project folder
   * 
   * @param folder
   * @return
   */
  private String findProjectLoc(File folder) {
    if (folder == null) {
      A4ELogging.warn("Can't resolve " + ECLIPSE_VAR_PROJECT_LOC + "!");
      return ".";
    }
    File file = new File(folder, ".project");
    if (file.exists()) {
      return folder.getAbsolutePath();
    }
    return findProjectLoc(folder.getParentFile());
  }

}
