package org.ant4eclipse.core.configuration;

import org.ant4eclipse.core.service.ServiceRegistry;

public interface Ant4EclipseConfiguration {

  /**
   * Returns the property with the given name or null if there is no such property
   * 
   * @param propertyName
   *          The name of the property
   * @return The property or null if there is no such property
   */
  public String getProperty(String propertyName);

  /**
   * Checks if there is a property with the given name
   * 
   * @param propertyName
   *          The name of the property to check
   * @return true if there is a property with the given name otherwise false
   */
  public boolean hasProperty(String propertyName);

  /**
   * Returns an {@link Iterable} that contains all properties whose names start with prefix.
   * <p>
   * The returned Iterable contains a String-array with two items: the first is the property key (with prefix removed!)
   * the second is the property value.
   * 
   * @param prefix
   *          The prefix that selected the properties. If it doesn't end with a "." a "." is added
   * @return see above
   */
  public Iterable<String[]> getAllProperties(String prefix);

  static class Helper {

    public static Ant4EclipseConfiguration getAnt4EclipseConfiguration() {
      return (Ant4EclipseConfiguration) ServiceRegistry.instance().getService(Ant4EclipseConfiguration.class.getName());
    }
  }
}
