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
import org.ant4eclipse.ant.platform.SubElementContribution;
import org.ant4eclipse.ant.platform.core.SubElementComponent;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Delegate class for all tasks and types working with (dymanic) sub elements.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SubElementDelegate extends AbstractAntDelegate implements SubElementComponent {

  /** The prefix of properties that holds a DynamicElementContributor class name */
  public static final String           SUB_ELEMENT_CONTRIBUTOR_PREFIX = "subElementContributor";

  /** the list of all sub element contributors */
  private List<SubElementContribution> _subElementContributions;

  /** list that holds all parsed sub elements */
  private List<Object>                 _subElements;

  /** indicates if this instance has been initialized or not */
  private boolean                      _initialized                   = false;

  /**
   * <p>
   * Creates a new instance of type {@link SubElementDelegate}.
   * </p>
   * 
   * @param component
   *          the project component
   */
  public SubElementDelegate(ProjectComponent component) {
    super(component);
  }

  /**
   * {@inheritDoc}
   */
  public List<Object> getSubElements() {
    init();

    return this._subElements;
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(String name) throws BuildException {

    // initialize the delegate
    init();

    // iterate over all known SubElementContributions
    for (SubElementContribution subElementContribution : this._subElementContributions) {

      // if the subElementContribution can handle the element -> handle it
      if (subElementContribution.canHandleSubElement(name, getProjectComponent())) {
        Object subElement = subElementContribution.createSubElement(name, getProjectComponent());
        this._subElements.add(subElement);
        return subElement;
      }
    }

    // no subElementContribution was able to handle the element -> return null
    return null;
  }

  /**
   * <p>
   * Loads the configured subElementContributors.
   * </p>
   */
  protected void init() {

    // Return if already initialized
    if (this._initialized) {
      return;
    }

    // create the lists of dynamic elements
    this._subElements = new LinkedList<Object>();

    // get all properties that defines a SubElementContributor
    Iterable<Pair<String, String>> subElementContributionEntries = Ant4EclipseConfiguration.Helper
        .getAnt4EclipseConfiguration().getAllProperties(SUB_ELEMENT_CONTRIBUTOR_PREFIX);

    List<SubElementContribution> subElementContributions = new LinkedList<SubElementContribution>();

    // Instantiate the subElementContributions
    for (Pair<String, String> subElementContributionDefinition : subElementContributionEntries) {

      // we're not interested in the key of a DynamicElementContributor, only the class name (value of the entry) is
      // relevant
      SubElementContribution subElementContribution = Utilities.newInstance(subElementContributionDefinition
          .getSecond());
      subElementContributions.add(subElementContribution);
    }

    // assign subElementContributions
    this._subElementContributions = subElementContributions;

    // set initialized
    this._initialized = true;
  }
}
