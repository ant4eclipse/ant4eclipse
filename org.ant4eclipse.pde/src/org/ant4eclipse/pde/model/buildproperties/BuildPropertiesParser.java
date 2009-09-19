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

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.ExtendedProperties;

import org.ant4eclipse.pde.internal.model.featureproject.FeatureProjectRoleImpl;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

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

  /** -- */
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
  public static void parsePluginBuildProperties(PluginProjectRole pluginProjectRole) {

    Assert.notNull(pluginProjectRole);
    ExtendedProperties properties = loadBuildProperties(pluginProjectRole.getEclipseProject());
    PluginBuildProperties buildProperties = initializePluginBuildProperties(properties);
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
  public static void parseFeatureBuildProperties(FeatureProjectRole featureProjectRole) {
    Assert.notNull(featureProjectRole);

    ExtendedProperties buildProperties = loadBuildProperties(featureProjectRole.getEclipseProject());
    FeatureBuildProperties featureBuildProperties = new FeatureBuildProperties();
    initializeAbstractBuildProperties(buildProperties, featureBuildProperties);
    // TODO
    ((FeatureProjectRoleImpl) featureProjectRole).setBuildProperties(featureBuildProperties);
  }

  private static ExtendedProperties loadBuildProperties(EclipseProject eclipseProject) {
    Assert.notNull(eclipseProject);
    File file = eclipseProject.getChild(BUILD_PROPERTIES);
    return new ExtendedProperties(file);
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
  public static PluginBuildProperties initializePluginBuildProperties(ExtendedProperties properties) {
    Assert.notNull(properties);

    PluginBuildProperties buildProperties = new PluginBuildProperties();
    initializeAbstractBuildProperties(properties, buildProperties);

    // set jars.compile.order
    String jarsCompileOrder = properties.get("jars.compile.order");
    if (jarsCompileOrder != null) {
      buildProperties.setJarsCompileOrder(getAsList(jarsCompileOrder, ",", true));
    }

    // set source and target compatibility level
    String javacSource = properties.get("javacSource", "1.3");
    buildProperties.setJavacSource(javacSource);
    String javacTarget = properties.get("javacTarget", "1.2");
    buildProperties.setJavacTarget(javacTarget);

    // set libraries
    Iterator<?> iterator = properties.keySet().iterator();
    while (iterator.hasNext()) {
      String key = (String) iterator.next();

      if (key.startsWith("source.")) {
        String libraryName = key.substring("source.".length());

        Library library = new Library(libraryName);

        String[] source = getAsList((String) properties.get("source." + libraryName), ",", true);
        library.setSource(source);

        String[] output = getAsList((String) properties.get("output." + libraryName), ",", true);
        library.setOutput(output);

        String manifest = (String) properties.get("manifest." + libraryName);
        library.setManifest(manifest);

        String exclude = (String) properties.get("exclude." + libraryName);
        library.setExclude(exclude);

        buildProperties.addLibrary(library);

        String extraKey = "extra." + libraryName;
        properties.get(extraKey);
      }
    }
    return buildProperties;
  }

  private static void initializeAbstractBuildProperties(ExtendedProperties allProperties,
      AbstractBuildProperties abstractBuildProperties) {
    Assert.notNull(allProperties);
    Assert.notNull(abstractBuildProperties);

    // set qualifier
    abstractBuildProperties.setQualifier((String) allProperties.get("qualifier"));

    // set custom
    abstractBuildProperties.setCustom(Boolean.valueOf(allProperties.get("custom", "false")).booleanValue());

    // set bin.includes
    String includes = allProperties.get("bin.includes", "");
    abstractBuildProperties.setBinaryIncludes(getAsList(includes, ",", true));

    // set bin.excludes
    String excludes = allProperties.get("bin.excludes", "");
    abstractBuildProperties.setBinaryExcludes(getAsList(excludes, ",", true));
  }

  /**
   * @param content
   * @param delimiter
   * @return
   */
  private static String[] getAsList(String content, String delimiter, boolean removePathSeparator) {
    Assert.notNull(delimiter);

    if (content == null) {
      return new String[] {};
    }

    List<String> result = new LinkedList<String>();

    StringTokenizer tokenizer = new StringTokenizer(content, delimiter);
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
