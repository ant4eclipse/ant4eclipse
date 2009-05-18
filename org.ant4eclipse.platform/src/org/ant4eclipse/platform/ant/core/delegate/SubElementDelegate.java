package org.ant4eclipse.platform.ant.core.delegate;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.ant.SubElementContribution;
import org.ant4eclipse.platform.ant.core.SubElementComponent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SubElementDelegate extends AbstractAntDelegate implements SubElementComponent {

  /** The prefix of properties that holds a DynamicElementContributor class name */
  public final static String           SUB_ELEMENT_CONTRIBUTOR_PREFIX = "subElementContributor";

  /** - */
  private List<SubElementContribution> _subElementContributors;

  /** - */
  private List<Object>                 _subElements;

  /** - */
  private boolean                      _initialized                   = false;

  /**
   * @param component
   */
  public SubElementDelegate(ProjectComponent component) {
    super(component);
  }

  public List<Object> getSubElements() {
    init();

    return this._subElements;
  }

  public Object createDynamicElement(String name) throws BuildException {

    init();

    for (SubElementContribution dynamicElementContributor : this._subElementContributors) {
      if (dynamicElementContributor.canHandleSubElement(name, getProjectComponent())) {
        Object subElement = dynamicElementContributor.createSubElement(name, getProjectComponent());
        this._subElements.add(subElement);
        return subElement;
      }
    }

    return null;
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {

    if (this._initialized) {
      return;
    }

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

    this._initialized = true;
  }
}
