package org.ant4eclipse.platform.ant.core.delegate;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.ant.SubElementContribution;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.ProjectComponent;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SubElementDelegate extends AbstractAntDelegate implements DynamicElement {

  /** The prefix of properties that holds a DynamicElementContributor class name */
  public final static String           SUB_ELEMENT_CONTRIBUTOR_PREFIX = "subElementContributor";

  /** - */
  private List<SubElementContribution> _subElementContributors;

  /** - */
  private List<Object>                 _subElements;

  /**
   * @param component
   */
  public SubElementDelegate(ProjectComponent component) {
    super(component);

    init();
  }

  public List<Object> getSubElements() {
    return this._subElements;
  }

  public Object createDynamicElement(String name) throws BuildException {

    for (SubElementContribution dynamicElementContributor : this._subElementContributors) {
      if (dynamicElementContributor.canHandleSubElement(name, getProjectComponent())) {
        return dynamicElementContributor.createSubElement(name, getProjectComponent());
      }
    }

    return null;
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {

    // create the lists of dynamic elements
    this._subElements = new LinkedList<Object>();

    // get all properties that defines a DynamicElementContributor
    Iterable<String[]> dynamicElementContributorEntries = Ant4EclipseConfiguration.Helper.getAnt4EclipseConfiguration()
        .getAllProperties(SUB_ELEMENT_CONTRIBUTOR_PREFIX);

    final List<SubElementContribution> dynamicElementContributors = new LinkedList<SubElementContribution>();

    // Instantiate all ProjectRoleIdentifiers
    for (String[] dynamicElementContributorEntry : dynamicElementContributorEntries) {
      // we're not interested in the key of a DynamicElementContributor, only the class name (value of the entry) is
      // relevant
      String dynamicElementContributorClassName = dynamicElementContributorEntry[1];
      SubElementContribution dynamicElementContributor = Utilities.newInstance(dynamicElementContributorClassName);
      dynamicElementContributors.add(dynamicElementContributor);
    }

    this._subElementContributors = dynamicElementContributors;
  }
}
