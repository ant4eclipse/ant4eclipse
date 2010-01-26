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
package org.ant4eclipse.lib.core.configuration;

import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.StringMap;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
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
  public static final String A4E_CONFIGURATION_PROPERTIES = "org/ant4eclipse/lib/ant4eclipse-configuration.properties";

  /** <b>All</b> configuration properties */
  private StringMap          _properties;

  /**
   * <p>
   * Creates a new instance of type {@link Ant4EclipseConfigurationImpl}.
   * </p>
   * 
   * @param properties
   *          The backing properties
   */
  public Ant4EclipseConfigurationImpl(StringMap properties) {
    Assure.notNull("properties", properties);
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
    Assure.notNull("propertyName", propertyName);
    return this._properties.get(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasProperty(String propertyName) {
    Assure.notNull("propertyName", propertyName);
    return this._properties.containsKey(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  public Iterable<Pair<String, String>> getAllProperties(String prefix) {
    Assure.notNull("prefix", prefix);
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
  private StringMap loadConfigurationProperties() {
    Enumeration<URL> propertyFiles = getPropertyFiles();
    StringMap allProperties = new StringMap();
    while (propertyFiles.hasMoreElements()) {
      URL url = propertyFiles.nextElement();
      allProperties.extendProperties(url);
    }
    return allProperties;
  }

} /* ENDCLASS */
