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
package org.ant4eclipse.build.tools;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MergeProperties extends AbstractMergeTask {

  @Override
  @SuppressWarnings("unchecked")
  public void doExecute() throws IOException {

    List<Property> newFile = new ArrayList<Property>();

    for (ResourceCollection resourceCollection : getResourceCollections()) {
      for (Iterator iterator = resourceCollection.iterator(); iterator.hasNext();) {
        Resource resource = (Resource) iterator.next();
        loadFile(resource.getInputStream(), newFile);
      }
    }

    // iterate through source, and write to file with updated properties
    writeFile(newFile);
  }

  /**
   * Reads the contents of the selected file and returns them in a List that contains String objects that represent each
   * line of the file in the order that they were read.
   * 
   * @param file
   *          The file to load the contents into a List.
   * @return a List of the contents of the file where each line of the file is stored as an individual String object in
   *         the List in the same physical order it appears in the file.
   * @throws BuildException
   *           An exception can occur if the version file is corrupted or the process is in someway interrupted
   */
  private List<Property> loadFile(InputStream inputStream, List<Property> fileContents) throws BuildException {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
      String curLine;
      String comment = "";
      try {
        while ((curLine = in.readLine()) != null) {
          curLine = curLine.trim();
          if (curLine.startsWith("#")) {
            comment += curLine + "\r\n";
          } else if (curLine.indexOf("=") > 0) {
            while (curLine.endsWith("\\")) {
              curLine += "\r\n" + in.readLine().trim();
            }
            Property contents = new Property();
            contents.name = curLine.substring(0, curLine.indexOf("="));
            contents.value = curLine;
            contents.comment = comment;
            comment = "";
            if (fileContents.contains(contents)) {
              Property existing = getExistingElement(fileContents, contents.name);
              if (existing != null) {
                existing.value = contents.value;
              } else {
                fileContents.add(contents);
              }
            } else {
              fileContents.add(contents);
            }
          }
        }
      } catch (Exception e) {
        throw new BuildException("Could not read file:", e);
      } finally {
        in.close();
      }
    } catch (IOException IOe) {
      // had an exception trying to open the file
      throw new BuildException("Could not read file:", IOe);
    }
    return fileContents;
  }

  private Property getExistingElement(List<Property> list, String name) {
    for (Property fileContents : list) {
      if (fileContents.getName().equals(name)) {
        return fileContents;
      }
    }
    return null;
  }

  /**
   * Writes the merged properties to a single file while preserving any comments.
   * 
   * @param source
   *          A list containing the contents of the original source file
   * @param merge
   *          A list containing the contents of the file to merge
   * @param props
   *          A collection of properties with their values merged from both files
   * @throws BuildException
   *           if the destination file can't be created
   */
  private void writeFile(List<Property> fileContents) throws BuildException {
    Iterator<Property> iterate = fileContents.iterator();
    try {
      FileOutputStream out = new FileOutputStream(getDestinationFile());
      PrintStream p = new PrintStream(out);
      try {
        // write original file with updated values
        while (iterate.hasNext()) {
          Property fc = iterate.next();
          if (fc.comment != null && !fc.comment.equals("")) {
            p.println();
            p.print(fc.comment);
          }
          p.println(fc.value);
        }
      } catch (Exception e) {
        throw new BuildException("Could not write file: " + getDestinationFile(), e);
      } finally {
        out.close();
      }
    } catch (IOException IOe) {
      throw new BuildException("Could not write file: " + getDestinationFile(), IOe);
    }
  }

  protected class Property {

    public String comment;

    public String name;

    public String value;

    public String getName() {
      return this.name;
    }

    @Override
    public int hashCode() {
      int prime = 31;
      int result = 1;
      result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
      result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
      result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Property other = (Property) obj;
      if (this.comment == null) {
        if (other.comment != null) {
          return false;
        }
      } else if (!this.comment.equals(other.comment)) {
        return false;
      }
      if (this.name == null) {
        if (other.name != null) {
          return false;
        }
      } else if (!this.name.equals(other.name)) {
        return false;
      }
      if (this.value == null) {
        if (other.value != null) {
          return false;
        }
      } else if (!this.value.equals(other.value)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[FileContents:");
      buffer.append(" name: ");
      buffer.append(this.name);
      buffer.append(" value: ");
      buffer.append(this.value);
      buffer.append(" comment: ");
      buffer.append(this.comment);
      buffer.append("]");
      return buffer.toString();
    }
  }

}
