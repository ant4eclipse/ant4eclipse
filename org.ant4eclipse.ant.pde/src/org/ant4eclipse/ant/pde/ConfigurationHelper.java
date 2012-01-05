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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.lib.pde.model.product.ProductDefinition;
import org.ant4eclipse.lib.pde.model.product.ProductDefinition.FeatureId;
import org.ant4eclipse.lib.pde.tools.BundleStartRecord;
import org.ant4eclipse.lib.pde.tools.PdeBuildHelper;
import org.ant4eclipse.lib.pde.tools.SimpleConfiguratorBundles;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.osgi.framework.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Helper class to create a config.ini file based on a product definition.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ConfigurationHelper {

  /** - */
  private static final String MSG_USING_HARDCODED       = "Failed to detect bundles, so the following hard coded ones are used:";

  /** - */
  private static final String MSG_FAILED_BUNDLESINFO    = "Failed to load bundles info file '%s'. Cause: %s";

  /** - */
  private static final String MSG_ACCESSING_BUNDLESINFO = "Accessing bundles info file '%s' to identify start bundles...";

  /** - */
  private static final String MSG_ACCESSING_CONFIGINI   = "Accessing file '%s' to identify start bundles...";

  /**
   * <p>
   * Returns a list of all bundle that should be installed (and started) in the osgi framework.
   * </p>
   * 
   * @param productdef
   *          the product definition
   * @param targetplatform
   *          the target platform
   * @return a list of all bundle that should be installed (and started) in the osgi framework.
   */
  public static String getOsgiBundles(ProductDefinition productdef, TargetPlatform targetplatform) {

    StringBuilder result = new StringBuilder();

    String[] pluginIds = productdef.getPluginIds();

    for (int i = 0; i < pluginIds.length; i++) {

      String id = pluginIds[i];

      if (!targetplatform.matchesPlatformFilter(id)) {
        continue;
      }

      if ("org.eclipse.osgi".equals(id)) {
        continue;
      }

      if (productdef.hasConfigurationRecord(id)) {
        result.append(productdef.getConfigurationRecord(id).getShortDescription());
      } else {
        result.append(id);
      }

      if (i + 1 < pluginIds.length) {
        result.append(",");
      }
    }

    String[] fragmentIds = productdef.getFragmentIds();
    for (String fragmentId : fragmentIds) {

      if (!targetplatform.matchesPlatformFilter(fragmentId)) {
        continue;
      }

      result.append(",");
      result.append(fragmentId);
    }

    if (productdef.isBasedOnFeatures()) {

      //
      return getOsgiBundlesFromFeatures(productdef, targetplatform);

    } else {

      //
      return getOsgiBundlesFromPlugins(productdef, targetplatform);
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param productdef
   * @param targetplatform
   * @return
   */
  private static String getOsgiBundlesFromFeatures(ProductDefinition productdef, TargetPlatform targetplatform) {

    List<String> pluginIds = new ArrayList<String>();

    for (FeatureId featureId : productdef.getFeatureIdentifiers()) {
      pluginIds.addAll(getPluginIdsForFeature(featureId.getId(), featureId.getVersion(), targetplatform));
    }

    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i < pluginIds.size(); i++) {

      String id = pluginIds.get(i);

      if (!targetplatform.matchesPlatformFilter(id)) {
        continue;
      }

      if ("org.eclipse.osgi".equals(id)) {
        continue;
      }

      if (targetplatform.hasBundleDescription(id) && targetplatform.getBundleDescription(id).getHost() == null
          && productdef.hasConfigurationRecord(id)) {
        buffer.append(productdef.getConfigurationRecord(id).getShortDescription());
      } else {
        buffer.append(id);
      }

      if (i + 1 < pluginIds.size()) {
        buffer.append(",");
      }
    }

    String result = buffer.toString();
    result = result.endsWith(",") ? result.substring(0, result.length() - 1) : result;

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param featureId
   * @param featureVersion
   * @param targetplatform
   * @return
   */
  private static List<String> getPluginIdsForFeature(String featureId, Version featureVersion,
      TargetPlatform targetplatform) {

    List<String> result = new ArrayList<String>();

    Version resolvedVersion = PdeBuildHelper.resolveVersion(featureVersion, PdeBuildHelper
        .getResolvedContextQualifier());

    FeatureDescription featureDescription = targetplatform.getFeatureDescription(featureId, resolvedVersion);

    for (FeatureManifest.Plugin plugin : featureDescription.getFeatureManifest().getPlugins()) {
      if (targetplatform.matchesPlatformFilter(plugin.getId())) {
        result.add(plugin.getId());
      }
    }

    for (FeatureManifest.Includes includes : featureDescription.getFeatureManifest().getIncludes()) {

      String arch = targetplatform.getTargetPlatformConfiguration().getArchitecture();
      String os = targetplatform.getTargetPlatformConfiguration().getOperatingSystem();
      String ws = targetplatform.getTargetPlatformConfiguration().getWindowingSystem();

      if (matches(includes.getMachineArchitecture(), arch) && matches(includes.getOperatingSystem(), os)
          && matches(includes.getWindowingSystem(), ws)) {
        result.addAll(getPluginIdsForFeature(includes.getId(), includes.getVersion(), targetplatform));
      }
    }

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param commaSeparatedList
   * @param value
   * @return
   */
  private static boolean matches(String commaSeparatedList, String value) {
    Assure.notNull("value", value);

    if (!Utilities.hasText(commaSeparatedList)) {
      return true;
    }

    String[] values = commaSeparatedList.split(",");

    for (String listValue : values) {
      if (value.equals(listValue)) {
        return true;
      }
    }

    return false;
  }

  /**
   * <p>
   * </p>
   * 
   * @param productdef
   * @param targetplatform
   * @return
   */
  private static String getOsgiBundlesFromPlugins(ProductDefinition productdef, TargetPlatform targetplatform) {

    StringBuilder result = new StringBuilder();

    String[] pluginIds = productdef.getPluginIds();

    for (int i = 0; i < pluginIds.length; i++) {

      String id = pluginIds[i];

      if (!targetplatform.matchesPlatformFilter(id)) {
        continue;
      }

      if ("org.eclipse.osgi".equals(id)) {
        continue;
      }

      if (productdef.hasConfigurationRecord(id)) {
        result.append(productdef.getConfigurationRecord(id).getShortDescription());
      } else {
        result.append(id);
      }

      if (i + 1 < pluginIds.length) {
        result.append(",");
      }
    }

    String[] fragmentIds = productdef.getFragmentIds();
    for (String fragmentId : fragmentIds) {

      if (!targetplatform.matchesPlatformFilter(fragmentId)) {
        continue;
      }

      result.append(",");
      result.append(fragmentId);
    }

    //
    return result.toString();
  }

  /**
   * @see "http://help.eclipse.org/help32/index.jsp?topic=/org.eclipse.pde.doc.user/guide/tools/editors/product_editor/configuration.htm"
   * 
   * @param targetlocations
   *          The target platform locations currently registered. Not <code>null</code>.
   * @param records
   *          The start records provided the product configuration file. Not <code>null</code>.
   * 
   * @return A comma separated list of all osgi bundles. Not <code>null</code>.
   */
  public static String collectOsgiBundles(File[] targetlocations, BundleStartRecord[] records) {

    StringMap properties = new StringMap();

    List<BundleStartRecord> startrecords = new ArrayList<BundleStartRecord>();

    for (File targetlocation : targetlocations) {

      File configini = new File(targetlocation, "configuration/config.ini");
      if (configini.isFile()) {

        A4ELogging.debug(MSG_ACCESSING_CONFIGINI, configini);

        // load the current bundle list of a specific configuration
        properties.extendProperties(configini);

        boolean gotsimpleconfigurator = false;
        String bundlelist = properties.get("osgi.bundles", null);
        if (bundlelist != null) {
          // separate the bundle parts
          String[] parts = bundlelist.split(",");
          for (String bundlepart : parts) {
            BundleStartRecord record = new BundleStartRecord(bundlepart);
            startrecords.add(record);
            if (record.getId().indexOf(SimpleConfiguratorBundles.ID_SIMPLECONFIGURATOR) != -1) {
              gotsimpleconfigurator = true;
            }
          }
        }

        if (gotsimpleconfigurator) {
          File bundlesinfo = new File(targetlocation,
              "configuration/org.eclipse.equinox.simpleconfigurator/bundles.info");
          if (bundlesinfo.isFile()) {
            A4ELogging.debug(MSG_ACCESSING_BUNDLESINFO, bundlesinfo);
            try {
              SimpleConfiguratorBundles simpleconfig = new SimpleConfiguratorBundles(bundlesinfo);
              BundleStartRecord[] screcords = simpleconfig.getBundleStartRecords();
              for (BundleStartRecord record : screcords) {
                if (record.isAutoStart()) {
                  startrecords.add(record);
                }
              }
            } catch (RuntimeException ex) {
              A4ELogging.debug(MSG_FAILED_BUNDLESINFO, bundlesinfo, ex.getMessage());
            }
          }
        }

      }
    }

    // if none could be found we're setting up some defaults which are basically
    // a guess (should be probably provided as a resource in future)
    if (startrecords.isEmpty()) {
      startrecords.add(new BundleStartRecord("org.eclipse.core.runtime@-1:start"));
      startrecords.add(new BundleStartRecord("org.eclipse.osgi@2:start"));
      startrecords.add(new BundleStartRecord("org.eclipse.equinox.common@2:start"));
      startrecords.add(new BundleStartRecord("org.eclipse.update.configurator@3:start"));
      A4ELogging.debug(MSG_USING_HARDCODED);
      for (int i = 0; i < startrecords.size(); i++) {
        A4ELogging.debug("\t%s", startrecords.get(i).getShortDescription());
      }
    }

    for (BundleStartRecord record : records) {
      startrecords.add(record);
    }

    // merge records denoting the same plugin id
    Collections.sort(startrecords);
    for (int i = startrecords.size() - 1; i > 0; i--) {
      BundleStartRecord current = startrecords.get(i);
      BundleStartRecord previous = startrecords.get(i - 1);
      if (current.getId().equals(previous.getId())) {
        previous.setAutoStart(previous.isAutoStart() || current.isAutoStart());
        previous.setStartLevel(Math.min(previous.getStartLevel(), current.getStartLevel()));
        startrecords.remove(i);
      }
    }

    // create a textual description for the bundlelist
    StringBuffer buffer = new StringBuffer();
    buffer.append(startrecords.get(0).getShortDescription());
    for (int i = 1; i < startrecords.size(); i++) {
      buffer.append(",");
      buffer.append(startrecords.get(i).getShortDescription());
    }
    return buffer.toString();

  }
}
