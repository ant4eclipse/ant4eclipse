package org.ant4eclipse.core.configuration;

import org.ant4eclipse.core.service.ServiceRegistry;

/**
 * <p>
 * Defines the interface to access the configuration of ant4eclipse.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public interface Ant4EclipseConfiguration {

  /**
   * <p>
   * Returns the property with the given name or <code>null</code> if there is no such property.
   * </p>
   * 
   * @param propertyName
   *          The name of the property
   * @return The property or <code>null</code> if there is no such property
   */
  public String getProperty(String propertyName);

  /**
   * <p>
   * Checks if there is a property with the given name.
   * </p>
   * 
   * @param propertyName
   *          The name of the property to check
   * @return <code>true</code> if there is a property with the given name otherwise <code>false</code>
   */
  public boolean hasProperty(String propertyName);

  /**
   * <p>
   * Returns an {@link Iterable} that contains all properties whose names start with the specified prefix.
   * </p>
   * <p>
   * The returned Iterable contains a String-array with two items: the first is the property key (with prefix removed!)
   * the second is the property value.
   * </p>
   * 
   * @param prefix
   *          The prefix that selected the properties. If it doesn't end with a "." a "." is added
   * @return see above
   */
  public Iterable<String[]> getAllProperties(String prefix);

  /**
   * <p>
   * Helper class to retrieve the {@link Ant4EclipseConfiguration} service from the {@link ServiceRegistry}.
   * </p>
   * 
   * @author Nils Hartmann (nils@nilshartmann.net)
   */
  static class Helper {

    /**
     * <p>
     * Returns the {@link Ant4EclipseConfiguration} service from the {@link ServiceRegistry}.
     * </p>
     * 
     * @return the {@link Ant4EclipseConfiguration} service
     */
    public static Ant4EclipseConfiguration getAnt4EclipseConfiguration() {
      return (Ant4EclipseConfiguration) ServiceRegistry.instance().getService(Ant4EclipseConfiguration.class.getName());
    }
  }
}
