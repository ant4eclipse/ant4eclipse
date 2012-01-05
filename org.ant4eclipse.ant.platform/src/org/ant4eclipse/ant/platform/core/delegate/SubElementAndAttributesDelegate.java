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
import org.ant4eclipse.ant.platform.SubAttributeContribution;
import org.ant4eclipse.ant.platform.SubElementContribution;
import org.ant4eclipse.ant.platform.core.SubElementAndAttributesComponent;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
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

  /** the list of all sub element contributors */
  private List<SubAttributeContribution> _subAttributeContributions;

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

    // iterate over all known SubAttributeContributions
    for (SubAttributeContribution subAttributeContribution : this._subAttributeContributions) {

      // if the subElementContribution can handle the element -> handle it
      if (subAttributeContribution.canHandleSubAttribute(name, getProjectComponent())) {
        this._subAttributes.put(name, value);
      }
    }

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

    Ant4EclipseConfiguration config = ServiceRegistryAccess.instance().getService(Ant4EclipseConfiguration.class);

    // /////
    // Create and set sub-elements...
    // ////

    // get all properties that defines a SubElementContributor
    Iterable<Pair<String, String>> subElementContributionEntries = config
        .getAllProperties(SUB_ELEMENT_CONTRIBUTOR_PREFIX);

    List<SubElementContribution> subElementContributions = new ArrayList<SubElementContribution>();

    // Instantiate the subElementContributions
    for (Pair<String, String> subElementContributionDefinition : subElementContributionEntries) {

      // we're not interested in the key of a DynamicElementContributor, only the class name (value of the entry) is
      // relevant
      SubElementContribution subElementContribution;
      try {
        subElementContribution = Utilities.newInstance(subElementContributionDefinition.getSecond(),
            new Class[] { ProjectComponent.class }, new Object[] { getProjectComponent() });
      } catch (Exception e) {
        subElementContribution = Utilities.newInstance(subElementContributionDefinition.getSecond());
      }
      subElementContributions.add(subElementContribution);
    }

    // assign subElementContributions
    this._subElementContributions = subElementContributions;

    // /////
    // Create and set sub-attributes...
    // ////

    // get all properties that defines a SubElementContributor
    Iterable<Pair<String, String>> subAttributeContributionEntries = config
        .getAllProperties(SUB_ATTRIBUTE_CONTRIBUTOR_PREFIX);

    List<SubAttributeContribution> subAttributeContributions = new ArrayList<SubAttributeContribution>();

    // Instantiate the subElementContributions
    for (Pair<String, String> subAttributeContributionDefintion : subAttributeContributionEntries) {

      // we're not interested in the key of a DynamicElementContributor, only the class name (value of the entry) is
      // relevant
      SubElementContribution subAttributeContribution = null;

      try {
        subAttributeContribution = Utilities.newInstance(subAttributeContributionDefintion.getSecond(),
            new Class[] { ProjectComponent.class }, new Object[] { getProjectComponent() });
      } catch (Exception e) {
        subAttributeContribution = Utilities.newInstance(subAttributeContributionDefintion.getSecond());
      }

      subElementContributions.add(subAttributeContribution);
    }

    // assign subElementContributions
    this._subAttributeContributions = subAttributeContributions;

    // set initialized
    this._initialized = true;
  }
}
