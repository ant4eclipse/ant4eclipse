package org.ant4eclipse.core.configuration;

import org.ant4eclipse.core.util.Pair;
import org.ant4eclipse.core.util.Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * Implementation of the {@link Ant4EclipseConfiguration} interface.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class Ant4EclipseConfigurationImpl implements Ant4EclipseConfiguration {

  /** the path of ant4eclipse configurations */
  public static final String  A4E_CONFIGURATION_PROPERTIES = "org/ant4eclipse/ant4eclipse-configuration.properties";

  /** <b>All</b> configuration properties */
  private Map<String, String> _properties;

  /**
   * <p>
   * Creates a new instance of type {@link Ant4EclipseConfigurationImpl}.
   * </p>
   * 
   * @param properties
   *          The backing properties
   */
  public Ant4EclipseConfigurationImpl(Map<String, String> properties) {
    if (properties == null) {
      throw new IllegalArgumentException("Parameter 'properties' must not be null ");
    }
    this._properties = properties;
  }

  /**
   * <p>
   * Creates a new instance of type {@link Ant4EclipseConfigurationImpl}.
   * </p>
   */
  public Ant4EclipseConfigurationImpl() {
    this._properties = loadConfigurationProperties();
  }

  /**
   * {@inheritDoc}
   */
  public String getProperty(String propertyName) {
    if (propertyName == null) {
      throw new IllegalArgumentException("Parameter 'propertyName' must not be null ");
    }
    return this._properties.get(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasProperty(String propertyName) {
    if (propertyName == null) {
      throw new IllegalArgumentException("Parameter 'propertyName' must not be null ");
    }

    return this._properties.containsKey(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  public Iterable<Pair<String, String>> getAllProperties(String prefix) {
    if (prefix == null) {
      throw new IllegalArgumentException("Parameter 'prefix' must not be null ");
    }
    if (!prefix.endsWith(".")) {
      prefix += ".";
    }
    Set<Map.Entry<String, String>> entries = this._properties.entrySet();

    List<Pair<String, String>> result = new LinkedList<Pair<String, String>>();

    for (Map.Entry<String, String> entry : entries) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (key.startsWith(prefix)) {
        key = key.substring(prefix.length());
        result.add(new Pair<String, String>(key, value));
      }
    }

    return result;
  }

  /**
   * <p>
   * Returns an Enumeration of URLs pointing to all configuration property files found on the classpath
   * </p>
   * 
   * @return An enumeration, never null
   */
  private Enumeration<URL> getPropertyFiles() {
    try {
      return getClass().getClassLoader().getResources(A4E_CONFIGURATION_PROPERTIES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not detect propery files on classpath: " + ioe, ioe);
    }
  }

  /**
   * <p>
   * Loads the properties from <b>all</b> configuration files that can be found on the classpath.
   * </p>
   * <p>
   * The properties will be merged into one {@link Properties} object
   * </p>
   * 
   * @return A Property object containing all loaded properties
   */
  private Map<String, String> loadConfigurationProperties() {
    Enumeration<URL> propertyFiles = getPropertyFiles();
    Map<String, String> allProperties = new Hashtable<String, String>();
    while (propertyFiles.hasMoreElements()) {
      URL url = propertyFiles.nextElement();
      allProperties.putAll(Utilities.readProperties(url));
    }
    return allProperties;
  }
}
