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
package org.ant4eclipse.ant.jdt.internal.tools.classpathelements;

import org.ant4eclipse.ant.core.AntService;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.internal.tools.classpathelements.ClassPathElementsRegistryImpl;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathVariable;
import org.apache.tools.ant.Project;

import java.io.File;

/**
 * <p>
 * Registry extension which is capable to provide a fallback mechanism to the ant environment.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class AntClassPathElementsRegistryImpl extends ClassPathElementsRegistryImpl implements AntService {

  private Project _project;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure(Project project) {
    this._project = project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClassPathVariable getClassPathVariable(String name) {
    ClassPathVariable result = super.getClassPathVariable(name);
    if ((result == null) && (this._project != null)) {
      // fallback to ant
      checkForVariable(name);
      // if it has not been registered, the variable wasn't available
      result = super.getClassPathVariable(name);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasClassPathVariable(String name) {
    boolean result = super.hasClassPathVariable(name);
    if ((!result) && (this._project != null)) {
      // fallback to ant
      checkForVariable(name);
      result = Utilities.cleanup(this._project.getProperty(name)) != null;
    }
    return result;
  }

  /**
   * This function tests whether the supplied variable name can be accessed through the ant environment. If the variable
   * can be found it will be registered, so a second lookup will be satisfied directly.
   * 
   * @param name
   *          The name of the potential variable.
   */
  private void checkForVariable(String name) {
    if (this._project != null) {
      String value = Utilities.cleanup(this._project.getProperty(name));
      if (value != null) {
        File file = new File(value);
        if (file.exists()) {
          super.registerClassPathVariable(name, file);
        }
      }
    }
  }

} /* ENDCLASS */
