package org.ant4eclipse.platform.ant.core.delegate;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.ant.DynamicElementContributor;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.ProjectComponent;

public class DynamicElementDelegate extends AbstractAntDelegate implements DynamicElement {

  /** The prefix of properties that holds a DynamicElementContributor class name */
  public final static String              DYNAMIC_ELEMENT_CONTRIBUTOR_PREFIX = "dynamicElementContributor";

  /** - */
  private List<DynamicElementContributor> _dynamicElementContributors;

  /** - */
  private List<Object>                    _dynamicElements;

  /**
   * @param component
   */
  public DynamicElementDelegate(ProjectComponent component) {
    super(component);

    init();
  }

  public List<Object> getDynamicElements() {
    return this._dynamicElements;
  }

  public Object createDynamicElement(String name) throws BuildException {

    for (DynamicElementContributor dynamicElementContributor : this._dynamicElementContributors) {
      if (dynamicElementContributor.canHandle(name, getProjectComponent())) {
        return dynamicElementContributor.createDynamicElement(name, getProjectComponent());
      }
    }

    // TODO: BuildException?
    return null;
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {

    // create the lists of dynamic elements
    this._dynamicElements = new LinkedList<Object>();

    // get all properties that defines a DynamicElementContributor
    Iterable<String[]> dynamicElementContributorEntries = Ant4EclipseConfiguration.Helper.getAnt4EclipseConfiguration()
        .getAllProperties(DYNAMIC_ELEMENT_CONTRIBUTOR_PREFIX);

    final List<DynamicElementContributor> dynamicElementContributors = new LinkedList<DynamicElementContributor>();

    // Instantiate all ProjectRoleIdentifiers
    for (String[] dynamicElementContributorEntry : dynamicElementContributorEntries) {
      // we're not interested in the key of a DynamicElementContributor, only the class name (value of the entry) is
      // relevant
      String dynamicElementContributorClassName = dynamicElementContributorEntry[1];
      DynamicElementContributor dynamicElementContributor = Utilities.newInstance(dynamicElementContributorClassName);
      A4ELogging.trace("Register ProjectRoleIdentifier '%s'", new Object[] { dynamicElementContributor });
      dynamicElementContributors.add(dynamicElementContributor);
    }

    this._dynamicElementContributors = dynamicElementContributors;
  }
}
