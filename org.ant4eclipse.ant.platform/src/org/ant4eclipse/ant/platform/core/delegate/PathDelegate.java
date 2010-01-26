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
package org.ant4eclipse.ant.platform.core.delegate;



import org.ant4eclipse.ant.core.delegate.AbstractAntDelegate;
import org.ant4eclipse.ant.platform.core.PathComponent;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PathDelegate extends AbstractAntDelegate implements PathComponent {

  /** the path separator (e.g. ':' or ';') */
  private String _pathSeparator;

  /** the directory separator (e.g. '/' or '\' */
  private String _dirSeparator;

  /**
   * <p>
   * Creates a new instance of type {@link PathDelegate}.
   * </p>
   * 
   * @param component
   *          the ProjectComponent
   */
  public PathDelegate(ProjectComponent component) {
    super(component);

    // set default separators
    this._pathSeparator = File.pathSeparator;
    this._dirSeparator = File.separator;
  }

  /**
   * {@inheritDoc}
   */
  public final void setPathSeparator(String newpathseparator) {
    Assure.nonEmpty("newpathseparator", newpathseparator);
    this._pathSeparator = newpathseparator;
  }

  /**
   * {@inheritDoc}
   */
  public final String getPathSeparator() {
    return this._pathSeparator;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPathSeparatorSet() {
    return this._pathSeparator != null;
  }

  /**
   * {@inheritDoc}
   */
  public final void setDirSeparator(String newdirseparator) {
    Assure.nonEmpty("newdirseparator", newdirseparator);
    this._dirSeparator = newdirseparator;
  }

  /**
   * {@inheritDoc}
   */
  public final String getDirSeparator() {
    return this._dirSeparator;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isDirSeparatorSet() {
    return this._dirSeparator != null;
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File entry) {
    return convertToString(new File[] { entry });
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File[] entries) {
    Assure.notNull("entries", entries);

    // convert Files to String
    List<String> entriesAsString = new LinkedList<String>();
    for (File entry : entries) {
      String path = entry.getPath();
      if (!entriesAsString.contains(path)) {
        entriesAsString.add(path);
      }
    }

    // replace path and directory separator
    StringBuilder buffer = new StringBuilder();
    Iterator<String> iterator = entriesAsString.iterator();
    while (iterator.hasNext()) {
      String path = iterator.next().replace('\\', '/');
      path = Utilities.replace(path, '/', this._dirSeparator);
      buffer.append(path);
      if (iterator.hasNext()) {
        buffer.append(this._pathSeparator);
      }
    }

    // return result
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File entry) {
    return convertToPath(new File[] { entry });
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File[] entries) {
    Assure.notNull("entries", entries);
    Path antPath = new Path(getAntProject());
    for (File entry : entries) {
      // TODO getPath() vs. getAbsolutePath()
      antPath.append(new Path(getAntProject(), entry.getPath()));
    }
    return antPath;
  }
}
