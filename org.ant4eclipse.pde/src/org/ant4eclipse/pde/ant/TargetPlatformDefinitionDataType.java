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
package org.ant4eclipse.pde.ant;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.pde.tools.TargetPlatformDefinition;
import org.ant4eclipse.pde.tools.target.TargetPlatformRegistry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;

/**
 * <p>
 * Represents a definition of a target platfrom. A target platfrom contains one or more locations. Each location must
 * contain bundles in a subdirectory named 'plugins'.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetPlatformDefinitionDataType extends AbstractAnt4EclipseDataType {

  private TargetPlatformDefinition _targetPlatformDefinition;

  /**
   * @param project
   */
  public TargetPlatformDefinitionDataType(Project project) {
    super(project);

    _targetPlatformDefinition = new TargetPlatformDefinition();
  }

  /**
   * <p>
   * Sets the id of the target platform location.
   * </p>
   * 
   * @param id
   *          the id of the target platform location.
   */
  public void setId(String id) {
    Assert.assertTrue(!isReference(), "Attribute 'refid' must not be set together with attribute 'id'!");
    Assert.nonEmpty(id);

    TargetPlatformRegistry targetPlatformRegistry = TargetPlatformRegistry.Helper.getRegistry();
    targetPlatformRegistry.addTargetPlatformDefinition(id, _targetPlatformDefinition);
  }

  /**
   * <p>
   * </p>
   * 
   * @param location
   */
  public void addConfiguredLocation(Location location) {
    Assert.notNull(location);

//    if (!_locations.contains(location)) {
//      _locations.add(location);
//    }
  }

//  /**
//   * <p>
//   * Returns all the locations defined in this target platfrom location.
//   * </p>
//   * 
//   * @return all the locations defined in this target platfrom location.
//   */
//  public final File[] getLocations() {
//    if (isReference()) {
//      return getRef(getProject()).getLocations();
//    } else {
//      File[] files = new File[_locations.size()];
//
//      for (int i = 0; i < files.length; i++) {
//        Location location = (Location) _locations.get(i);
//        files[i] = location.getDirectory();
//      }
//
//      return files;
//    }
//  }

//  /**
//   * Performs the check for circular references and returns the referenced FileSet.
//   * 
//   * @param p
//   */
//  protected TargetPlatformDefinitionDataType getRef(Project p) {
//    if (!isChecked()) {
//      Stack<DataType> stk = new Stack<DataType>();
//      stk.push(this);
//      dieOnCircularReference(stk, p);
//    }
//    Object o = getRefid().getReferencedObject(p);
//    if (!getClass().isAssignableFrom(o.getClass())) {
//      throw new BuildException(getRefid().getRefId() + " doesn\'t denote a TargetPlatformDefinition");
//    }
//    return (TargetPlatformDefinitionDataType) o;
//  }

  /**
   * <p>
   * Implements a target platform location.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Location extends DataType {

    /** the location path */
    private File _directory;

    /**
     * <p>
     * Creates a new instance of type TargetPlatformLocation.
     * </p>
     */
    public Location() {
      // empty constructor...
    }

    /**
     * <p>
     * Creates a new instance of type Location.
     * </p>
     * 
     * @param directory
     *          the directory.
     */
    public Location(File directory) {
      Assert.isDirectory(directory);

      _directory = directory;
    }

    /**
     * <p>
     * 
     * </p>
     * 
     * @return
     */
    public File getDirectory() {
      return _directory;
    }

    /**
     * <p>
     * Sets the directory.
     * </p>
     * 
     * @param directory
     */
    public void setDir(File directory) {
      Assert.isDirectory(directory);

      _directory = directory;
    }

    /**
     * <p>
     * Returns <code>true</code>, if the directory is set, <code>false</code> otherwise.
     * </p>
     * 
     * @return <code>true</code>, if the directory is set, <code>false</code> otherwise.
     */
    public boolean hasDirectory() {
      return _directory != null;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
      final int PRIME = 31;
      int result = 1;
      result = PRIME * result + ((_directory == null) ? 0 : _directory.hashCode());
      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Location other = (Location) obj;
      if (_directory == null) {
        if (other._directory != null)
          return false;
      } else if (!_directory.equals(other._directory))
        return false;
      return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      StringBuffer result = new StringBuffer();
      result.append("[Location path=");
      result.append(_directory);
      result.append("]");
      return result.toString();
    }
  }
}
