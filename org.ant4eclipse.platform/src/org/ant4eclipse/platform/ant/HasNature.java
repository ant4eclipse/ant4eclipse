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

import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.ant.core.condition.AbstractProjectBasedCondition;
import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.apache.tools.ant.BuildException;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * An ant condition that allows to check if a project has a specific nature.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class HasNature extends AbstractProjectBasedCondition {

  private static final String        RESOURCE_NATURES = "platform/natures.properties";

  private static Map<String, String> NATURES          = null;

  private String                     _nature;

  /**
   * <p>
   * Creates a new instance of type HasNature.
   * </p>
   */
  public HasNature() {
    if (NATURES == null) {
      // load abbreviations for the nature
      URL resource = HasNature.class.getClassLoader().getResource(RESOURCE_NATURES);
      if (resource != null) {
        try {
          NATURES = Utilities.readProperties(resource);
        } catch (IOException ex) {
          A4ELogging.error("failed to load nature abbreviations from '%s'. cause: %s", resource.toExternalForm(), ex
              .getMessage());
        }
      }
      if (NATURES == null) {
        // there was a failure or no natures list with abbreviations,
        // so create an instance to prevent further instantiations
        NATURES = new Hashtable<String, String>();
      }
    }
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
      if (project.hasNature(this._nature)) {
        // the nature matches directly
        return true;
      } else {
        // try if the user supplied an abbreviation
        String abbreviation = this._nature.toLowerCase();
        if (NATURES.containsKey(abbreviation)) {
          // check the nature with the full id now
          String natureid = NATURES.get(abbreviation);
          return project.hasNature(natureid);
        } else {
          // there's no mapping so we don't have an abbreviation here
          return false;
        }
      }
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