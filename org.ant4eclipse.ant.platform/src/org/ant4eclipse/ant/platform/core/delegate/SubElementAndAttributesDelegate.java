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
import org.ant4eclipse.ant.platform.core.SubElementAndAttributesComponent;
import org.ant4eclipse.lib.core.A4ECore;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Delegate class for all tasks and types working with (dymanic) sub elements.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SubElementAndAttributesDelegate extends AbstractAntDelegate implements SubElementAndAttributesComponent {

  /** The prefix of properties that holds a DynamicElementContributor class name */
  public static final String             SUB_ELEMENT_CONTRIBUTOR_PREFIX   = "subElementContributor";

  /** The prefix of properties that holds a DynamicElementContributor class name */
  public static final String             SUB_ATTRIBUTE_CONTRIBUTOR_PREFIX = "subAttributeContributor";

  /** the list of all sub element contributors */
  private List<SubElementContribution>   _subElementContributions;

  /** list that holds all parsed sub elements */
  private List<Object>                   _subElements;

  /** map that holds all parsed sub attributes */
  private Map<String, String>            _subAttributes;

  /** indicates if this instance has been initialized or not */
  private boolean                        _initialized                     = false;

  /**
   * <p>
   * Creates a new instance of type {@link SubElementAndAttributesDelegate}.
   * </p>
   * 
   * @param component
   *          the project component
   */
  public SubElementAndAttributesDelegate(ProjectComponent component) {
    super(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Object> getSubElements() {
    init();

    return this._subElements;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getSubAttributes() {
    init();

    return this._subAttributes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
   * {@inheritDoc}
   */
  @Override
  public void setDynamicAttribute(String name, String value) throws BuildException {
    // initialize the delegate
    init();
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
    this._subElements = new ArrayList<Object>();

    // create the lists of dynamic attributes
    this._subAttributes = new HashMap<String, String>();

    // /////
    // Create and set sub-elements...
    // ////

    // assign subElementContributions
    this._subElementContributions = A4ECore.instance().getServices(SubElementContribution.class);
    
    // set initialized
    this._initialized = true;
  }
}
