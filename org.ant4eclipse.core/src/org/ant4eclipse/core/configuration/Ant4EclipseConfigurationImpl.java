package org.ant4eclipse.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class Ant4EclipseConfigurationImpl implements Ant4EclipseConfiguration {

  public static final String A4E_CONFIGURATION_PROPERTIES = "org/ant4eclipse/ant4eclipse-configuration.properties";

  /**
   * <b>All</b> configuration properties
   */
  private final Properties   _properties;

  /**
   * Used to construct a new instance of {@link Ant4EclipseConfigurationProperties}.
   * 
   * To get an instance of Ant4EclipseConfigurationProperties use {@link #getInstance()} instead
   * 
   * @param properties
   *          The backing properties
   */
  public Ant4EclipseConfigurationImpl(Properties properties) {
    if (properties == null) {
      throw new IllegalArgumentException("Parameter 'properties' must not be null ");
    }
    this._properties = properties;
  }

  public Ant4EclipseConfigurationImpl() {
    this._properties = loadConfigurationProperties();
  }

  /**
   * Returns the property with the given name or null if there is no such property
   * 
   * @param propertyName
   *          The name of the property
   * @return The property or null if there is no such property
   */
  public String getProperty(String propertyName) {
    if (propertyName == null) {
      throw new IllegalArgumentException("Parameter 'propertyName' must not be null ");
    }

    return this._properties.getProperty(propertyName);
  }

  /**
   * Checks if there is a property with the given name
   * 
   * @param propertyName
   *          The name of the property to check
   * @return true if there is a property with the given name otherwise false
   */
  public boolean hasProperty(String propertyName) {
    if (propertyName == null) {
      throw new IllegalArgumentException("Parameter 'propertyName' must not be null ");
    }

    return this._properties.containsKey(propertyName);
  }

  /**
   * Returns an {@link Iterable} that contains all properties whose names start with prefix.
   * <p>
   * The returned {@link Iterable} contains a String-array with two items: the first is the property key (with prefix
   * removed!) the second is the property value.
   * 
   * @param prefix
   *          The prefix that selected the properties. If it doesn't end with a "." a "." is added
   * @return see above
   */
  public Iterable<String[]> getAllProperties(String prefix) {
    if (prefix == null) {
      throw new IllegalArgumentException("Parameter 'prefix' must not be null ");
    }
    if (!prefix.endsWith(".")) {
      prefix += ".";
    }
    Set<Entry<Object, Object>> entries = this._properties.entrySet();

    final List<String[]> result = new LinkedList<String[]>();

    for (Entry<Object, Object> entry : entries) {
      String key = entry.getKey().toString();
      String value = entry.getValue().toString();
      if (key.startsWith(prefix)) {
        key = key.substring(prefix.length());
        result.add(new String[] { key, value });
      }
    }

    return result;
  }

  /**
   * Returns an Enumeration of URLs pointing to all configuration property files found on the classpath
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
   * Loads the properties from <b>all</b> configuration files that can be found on the classpath.
   * 
   * <p>
   * The properties will be merged into one {@link Properties} object
   * 
   * @return A Property object containing all loaded properties
   */
  private Properties loadConfigurationProperties() {
    Enumeration<URL> propertyFiles = getPropertyFiles();
    final Properties allProperties = new Properties();
    while (propertyFiles.hasMoreElements()) {
      URL url = propertyFiles.nextElement();

      try {
        Properties properties = new Properties();
        InputStream is = url.openStream();
        properties.load(is);
        allProperties.putAll(properties);
        try {
          is.close();
        } catch (IOException ie) {
          // ignore
        }
      } catch (IOException io) {
        throw new RuntimeException("Could not read ant4eclipse configuration properties from " + url + ": " + io, io);
      }
    }

    return allProperties;
  }
}
