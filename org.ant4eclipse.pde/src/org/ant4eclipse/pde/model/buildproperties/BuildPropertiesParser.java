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
package org.ant4eclipse.pde.model.buildproperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.pde.internal.model.featureproject.FeatureProjectRoleImpl;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * The BuildPropertiesParser reads and interprets the build.properties file of a Plugin Project.
 * 
 * <p>
 * The build.properties-file is automatically read when an EclipseProject with the {@link PluginProjectRole} is opened.
 * 
 * <p>
 * The properties can be received from an opened EclipseProject using:
 * 
 * <pre>
 *   PluginBuildProperties buildProperties =
 *       PluginProjectRole.getPluginProjectRole(eclipseProject).getBuildProperties()
 * </pre>
 * 
 * @see #parsePluginBuildProperties(EclipseProject)
 * @see PluginProjectRole#getBuildProperties()
 */
public class BuildPropertiesParser {

  public static final String BUILD_PROPERTIES = "build.properties";

  /**
   * Reads the build.properties of the given eclipse project. After the properties have been read they are assigned to
   * the project's PluginRole and can be accessed using {@link PluginProjectRole#getBuildProperties()}.
   * 
   * <p>
   * <b>Note</b>: <code>The build.properties</code> is automatically read when an EclipseProject containing the
   * PluginRole is opened. <b>There is normally no need to call this method otherwise.</b>
   * 
   * @param project
   *          The eclipse project. Has to be a plugin project
   * @throws FileParserException
   * @see PluginProjectRole#getBuildProperties()
   */
  public static void parsePluginBuildProperties(final PluginProjectRole pluginProjectRole) {

    Assert.notNull(pluginProjectRole);
    final Properties properties = loadBuildProperties(pluginProjectRole.getEclipseProject());
    final PluginBuildProperties buildProperties = initializePluginBuildProperties(properties);
    pluginProjectRole.setBuildProperties(buildProperties);
  }

  /**
   * Reads the build.properties of the given eclipse project. After the properties have been read they are assigned to
   * the project's PluginRole and can be accessed using {@link PluginProjectRole#getBuildProperties()}.
   * 
   * <p>
   * <b>Note</b>: <code>The build.properties</code> is automatically read when an EclipseProject containing the
   * PluginRole is opened. <b>There is normally no need to call this method otherwise.</b>
   * 
   * @param featureProjectRole
   *          the featureProjectRole
   * @see PluginProjectRole#getBuildProperties()
   */
  public static void parseFeatureBuildProperties(final FeatureProjectRole featureProjectRole) {
    Assert.notNull(featureProjectRole);

    final Properties buildProperties = loadBuildProperties(featureProjectRole.getEclipseProject());
    final FeatureBuildProperties featureBuildProperties = new FeatureBuildProperties();
    initializeAbstractBuildProperties(buildProperties, featureBuildProperties);
    // TODO
    ((FeatureProjectRoleImpl)featureProjectRole).setBuildProperties(featureBuildProperties);
  }

  private static Properties loadBuildProperties(final EclipseProject eclipseProject) {
    Assert.notNull(eclipseProject);
    final File file = eclipseProject.getChild(BUILD_PROPERTIES);
    FileInputStream inputStream = null;
    try {
      final Properties properties = new Properties();
      inputStream = new FileInputStream(file);
      properties.load(inputStream);
      return properties;
    } catch (final IOException ioe) {
      // TODO
      throw new RuntimeException(ioe.getMessage(), ioe);
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (final IOException ioe) {
        A4ELogging.warn("Could not close inputstream: %s", ioe.getMessage());
      }

    }
  }

  /**
   * Initializes the buildProperties with the build properties read from the given input stream (typically a
   * FileInputStream to a "build.properties" file). The input stream must be processable via java.util.Property's
   * load(InputStream) method.
   * 
   * <p>
   * This method is mainly public to make it accessible from tests (?)
   * </p>
   */
  public static PluginBuildProperties initializePluginBuildProperties(final Properties properties) {
    Assert.notNull(properties);

    final PluginBuildProperties buildProperties = new PluginBuildProperties();
    initializeAbstractBuildProperties(properties, buildProperties);

    // set jars.compile.order
    final String jarsCompileOrder = (String) properties.get("jars.compile.order");
    if (jarsCompileOrder != null) {
      buildProperties.setJarsCompileOrder(getAsList(jarsCompileOrder, ",", true));
    }

    // set source and target compatibility level
    final String javacSource = properties.getProperty("javacSource", "1.3");
    buildProperties.setJavacSource(javacSource);
    final String javacTarget = properties.getProperty("javacTarget", "1.2");
    buildProperties.setJavacTarget(javacTarget);

    // set libraries
    final Iterator<?> iterator = properties.keySet().iterator();
    while (iterator.hasNext()) {
      final String key = (String) iterator.next();

      if (key.startsWith("source.")) {
        final String libraryName = key.substring("source.".length());

        final Library library = new Library(libraryName);

        final String[] source = getAsList((String) properties.get("source." + libraryName), ",", true);
        library.setSource(source);

        final String[] output = getAsList((String) properties.get("output." + libraryName), ",", true);
        library.setOutput(output);

        final String manifest = (String) properties.get("manifest." + libraryName);
        library.setManifest(manifest);

        final String exclude = (String) properties.get("exclude." + libraryName);
        library.setExclude(exclude);

        buildProperties.addLibrary(library);

        final String extraKey = "extra." + libraryName;
        properties.get(extraKey);
      }
    }
    return buildProperties;
  }

  private static void initializeAbstractBuildProperties(final Properties allProperties,
      final AbstractBuildProperties abstractBuildProperties) {
    Assert.notNull(allProperties);
    Assert.notNull(abstractBuildProperties);

    // set qualifier
    abstractBuildProperties.setQualifier((String) allProperties.get("qualifier"));

    // set custom
    abstractBuildProperties.setCustom(Boolean.valueOf(allProperties.getProperty("custom", "false")).booleanValue());

    // set bin.includes
    final String includes = allProperties.getProperty("bin.includes", "");
    abstractBuildProperties.setBinaryIncludes(getAsList(includes, ",", true));

    // set bin.excludes
    final String excludes = allProperties.getProperty("bin.excludes", "");
    abstractBuildProperties.setBinaryExcludes(getAsList(excludes, ",", true));
  }

  /**
   * @param content
   * @param delimiter
   * @return
   */
  private static String[] getAsList(final String content, final String delimiter, final boolean removePathSeparator) {
    Assert.notNull(delimiter);

    if (content == null) {
      return new String[] {};
    }

    final List<String> result = new LinkedList<String>();

    final StringTokenizer tokenizer = new StringTokenizer(content, delimiter);
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken().trim();
      if (removePathSeparator && (token.length() > 1) && token.endsWith("/")) {
        token = token.substring(0, token.length() - 1);
      }
      result.add(token);
    }

    return result.toArray(new String[0]);
  }
}
