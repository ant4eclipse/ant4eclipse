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
package org.ant4eclipse.ant.jdt.type;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;


import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Ant type to define class path containers. A class path container can be added to a project's class path.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClassPathContainerType extends AbstractAnt4EclipseDataType {

  /** - */
  private Union  _resources = null;

  /** - */
  private String _name;

  /**
   * <p>
   * Creates a new instance of type {@link JdtClassPathContainerType}.
   * </p>
   * 
   * @param project
   */
  public JdtClassPathContainerType(Project project) {
    super(project);
  }

  /**
   * <p>
   * </p>
   * 
   * @return the name
   */
  public String getName() {
    return this._name;
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this._name = name;
  }

  /**
   * Add a collection of resources upon which to operate.
   * 
   * @param rc
   *          resource collection to add.
   */
  public void add(ResourceCollection rc) {
    if (this._resources == null) {
      this._resources = new Union();
    }
    this._resources.add(rc);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doValidate() {
    // TODO: validate

    // fetch the ClassPathElementsRegistry
    ClassPathElementsRegistry variablesRegistry = ClassPathElementsRegistry.Helper.getRegistry();

    // fetch the provided files
    List<File> files = new LinkedList<File>();
    @SuppressWarnings("unchecked")
    Iterator iterator = this._resources.iterator();
    while (iterator.hasNext()) {
      FileResource fileResource = (FileResource) iterator.next();
      files.add(fileResource.getFile());
    }

    // TODO: what to do if classpathContainer already registered?
    variablesRegistry.registerClassPathContainer(this._name, files.toArray(new File[0]));
  }
}
