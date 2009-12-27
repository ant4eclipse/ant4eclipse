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
package org.ant4eclipse.ant.platform.ant.core.delegate;


import org.ant4eclipse.ant.platform.ant.core.ProjectReferenceAwareComponent;
import org.apache.tools.ant.BuildException;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectReferenceAwareDelegate implements ProjectReferenceAwareComponent {

  /** project reference types */
  private String[] _projectReferenceTypes;

  /**
   * {@inheritDoc}
   */
  public String[] getProjectReferenceTypes() {
    return this._projectReferenceTypes;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isProjectReferenceTypesSet() {
    return this._projectReferenceTypes != null;
  }

  /**
   * {@inheritDoc}
   */
  public void requireProjectReferenceTypesSet() {
    if (!isProjectReferenceTypesSet()) {
      // TODO
      throw new BuildException("referenceTypes has to be set!");
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setProjectReferenceTypes(String projectReferenceTypes) {
    //
    if (projectReferenceTypes == null) {
      this._projectReferenceTypes = new String[] {};
    } else {
      String[] names = projectReferenceTypes.split(",");

      //
      this._projectReferenceTypes = new String[names.length];

      for (int i = 0; i < names.length; i++) {
        this._projectReferenceTypes[i] = names[i].trim();
      }
    }
  }
}
