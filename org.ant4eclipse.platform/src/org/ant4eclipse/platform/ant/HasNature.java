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
package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.ant.core.EclipseProjectComponent;
import org.ant4eclipse.platform.ant.core.condition.AbstractProjectBasedCondition;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * An ant condition that allows to check if a project has a specific nature.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class HasNature extends AbstractProjectBasedCondition implements EclipseProjectComponent {

  /** Comment for <code>_nature</code> */
  private String _nature;

  /**
   * <p>
   * Creates a new instance of type HasNature.
   * </p>
   */
  public HasNature() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean doEval() throws BuildException {
    requireWorkspaceAndProjectNameSet();
    requireNatureSet();
    try {
      final EclipseProject project = getEclipseProject();
      return project.hasNature(this._nature);
    } catch (final BuildException e) {
      throw e;
    } catch (final Exception e) {
      throw new BuildException(e.getMessage(), e);
    }
  }

  /**
   * <p>
   * Sets the nature to check for.
   * </p>
   * 
   * @param nature
   *          the nature to set.
   */
  public void setNature(final String nature) {
    this._nature = nature;
  }

  /**
   * <p>
   * Returns <code>true</code> if the nature has been set.
   * </p>
   * 
   * @return <code>true</code> if the nature has been set.
   */
  public boolean isNatureSet() {
    return this._nature != null;
  }

  /**
   * <p>
   * Makes sure the nature attribute has been set. Otherwise throws a BuildException
   * </p>
   */
  public final void requireNatureSet() {
    if (!isNatureSet()) {
      throw new BuildException("Attribute 'nature' has to be set!");
    }
  }
}