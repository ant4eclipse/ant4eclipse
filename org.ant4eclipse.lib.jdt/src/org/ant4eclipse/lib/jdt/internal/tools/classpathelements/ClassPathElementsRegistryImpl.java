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
package org.ant4eclipse.lib.jdt.internal.tools.classpathelements;

import org.ant4eclipse.lib.jdt.JdtExceptionCode;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathContainer;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathVariable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.io.File;

/**
 * <p>
 * Implementation of the {@link ClasspathVariablesRegistry}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassPathElementsRegistryImpl implements ClassPathElementsRegistry {

  /** the class path variables */
  private Map<String, ClassPathVariable>  _classpathVariables;

  /** the class path containers */
  private Map<String, ClassPathContainer> _classpathContainer;

  /**
   * <p>
   * Creates a new instance of type ClasspathVariablesRegistryImpl.
   * </p>
   */
  public ClassPathElementsRegistryImpl() {
    super();

    // create the class path variables map
    this._classpathVariables = new HashMap<String, ClassPathVariable>();

    // create the class path container map
    this._classpathContainer = new HashMap<String, ClassPathContainer>();
  }

  /**
   * {@inheritDoc}
   */
  public List<ClassPathContainer> getClasspathContainer() {
    return new LinkedList<ClassPathContainer>(this._classpathContainer.values());
  }

  /**
   * {@inheritDoc}
   */
  public ClassPathContainer getClassPathContainer(String name) {
    return this._classpathContainer.get(name);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasClassPathContainer(String name) {
    return this._classpathContainer.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  public void registerClassPathContainer(String name, File[] pathEntries) {
    this._classpathContainer.put(name, new ClassPathContainerImpl(name, pathEntries));
  }

  /**
   * {@inheritDoc}
   */
  public ClassPathVariable getClassPathVariable(String name) {
    return this._classpathVariables.get(name);
  }

  /**
   * {@inheritDoc}
   */
  public List<ClassPathVariable> getClasspathVariables() {
    return new LinkedList<ClassPathVariable>(this._classpathVariables.values());
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasClassPathVariable(String name) {
    return this._classpathVariables.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  public void registerClassPathVariable(String name, File path) {
    ClassPathVariable newvar = new ClassPathVariableImpl(name, path);
    ClassPathVariable oldvar = this._classpathVariables.get(name);
    if (oldvar != null) {
      // we already got an entry, so check if they're unequal (equal locations won't do any harm, so we consider them
      // legal
      if (!oldvar.getPath().equals(newvar.getPath())) {
        throw new Ant4EclipseException(JdtExceptionCode.CONFLICTING_CLASSPATH_VARIABLES, name, oldvar.getPath(),
            newvar.getPath());
      }
    }
    this._classpathVariables.put(name, newvar);
  }

  /**
   * {@inheritDoc}
   */
  public void registerClassPathVariables(Map<String, File> vars) {
    for (Map.Entry<String, File> var : vars.entrySet()) {
      registerClassPathVariable(var.getKey(), var.getValue());
    }
  }
}
