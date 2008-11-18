/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package net.sf.ant4eclipse.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Ant4EclipseConfigurationProperties -- Provides access to all ant4eclipse configuration properties.
 * 
 * <p>
 * The properties must be specified in one or more <tt>ant4eclipse-configuration.properties</tt> files, that must be
 * available from classpath.
 * 
 * <p>
 * <b>Note:</b> This class is used <b>before</b> the service registry is constructed. This implies that it is not
 * possible to obtain services from the registry i.e. for logging purposes !
 * 
 * @author Nils Hartmann <nils@nilshartmann.net>
 * @version $Revision$
 */
public class Ant4EclipseConfigurationProperties {

  public static final String                        A4E_CONFIGURATION_PROPERTIES = "net/sf/ant4eclipse/ant4eclipse-configuration.properties";

  /**
   * The only Ant4EclipseConfigurationProperties-instance
   */
  private static Ant4EclipseConfigurationProperties _instance;

  /**
   * <b>All</b> configuration properties
   */
  private final Properties                          _properties;

  public static Ant4EclipseConfigurationProperties getInstance() {
    if (_instance == null) {
      _instance = createInstance();
    }

    return _instance;
  }

  /**
   * Returns an Enumeration of URLs pointing to all configuration property files found on the classpath
   * 
   * @return An enumeration, never null
   */
  private static Enumeration<URL> getPropertyFiles() {
    try {
      return Ant4EclipseConfigurationProperties.class.getClassLoader().getResources(A4E_CONFIGURATION_PROPERTIES);
    } catch (IOException ioe) {
      throw new RuntimeException("Could not detect propery files on classpath: " + ioe, ioe);
    }
  }

  private static Ant4EclipseConfigurationProperties createInstance() {
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

    return new Ant4EclipseConfigurationProperties(allProperties);
  }

  /**
   * Used to construct a new instance of {@link Ant4EclipseConfigurationProperties}. This constructor is for internal
   * use only.
   * 
   * To get an instance of Ant4EclipseConfigurationProperties use {@link #getInstance()} instead
   * 
   * @param properties
   *          The backing properties
   */
  public Ant4EclipseConfigurationProperties(Properties properties) {
    if (properties == null) {
      throw new IllegalArgumentException("Parameter 'properties' must not be null ");
    }
    _properties = properties;
  }

  public String getProperty(String propertyName) {
    if (propertyName == null) {
      throw new IllegalArgumentException("Parameter 'propertyName' must not be null ");
    }

    return _properties.getProperty(propertyName);
  }

  public boolean hasProperty(String propertyName) {
    if (propertyName == null) {
      throw new IllegalArgumentException("Parameter 'propertyName' must not be null ");
    }

    return _properties.containsKey(propertyName);
  }

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
  public Iterable<String[]> getAllProperties(String prefix) {
    if (prefix == null) {
      throw new IllegalArgumentException("Parameter 'prefix' must not be null ");
    }
    if (!prefix.endsWith(".")) {
      prefix += ".";
    }
    Set<Entry<Object, Object>> entries = _properties.entrySet();

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

}
