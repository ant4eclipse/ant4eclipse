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
package org.ant4eclipse.lib.pde.model.featureproject;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;


import org.ant4eclipse.lib.pde.internal.model.featureproject.FeatureManifestImpl;
import org.osgi.framework.Version;

import java.io.InputStream;

/**
 * <p>
 * Implements a parser for feature manifest files.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FeatureManifestParser {

  /**
   * <p>
   * Creates a FeatureManifest instance from the supplied content.
   * </p>
   * 
   * @param inputStream
   *          The stream which provides the content.
   * 
   * @return a Feature instance providing the content.
   */
  public static FeatureManifest parseFeature(InputStream inputStream) {
    Assert.notNull(inputStream);

    FeatureManifestImpl feature = new FeatureManifestImpl();

    XQueryHandler queryhandler = new XQueryHandler();

    XQuery idQuery = queryhandler.createQuery("/feature/@id");
    XQuery versionQuery = queryhandler.createQuery("/feature/@version");
    XQuery labelQuery = queryhandler.createQuery("/feature/@label");
    XQuery providerNameQuery = queryhandler.createQuery("/feature/@provider-name");
    XQuery imageQuery = queryhandler.createQuery("/feature/@image");
    XQuery osQuery = queryhandler.createQuery("/feature/@os");
    XQuery archQuery = queryhandler.createQuery("/feature/@arch");
    XQuery wsQuery = queryhandler.createQuery("/feature/@ws");
    XQuery nlQuery = queryhandler.createQuery("/feature/@nl");
    XQuery colocationAffinityQuery = queryhandler.createQuery("/feature/@colocation-affinity");
    XQuery primaryQuery = queryhandler.createQuery("/feature/@primary");
    XQuery exclusiveQuery = queryhandler.createQuery("/feature/@exclusive");
    XQuery pluginQuery = queryhandler.createQuery("/feature/@plugin");
    XQuery applicationQuery = queryhandler.createQuery("/feature/@application");

    XQuery pluginIdQuery = queryhandler.createQuery("/feature/{plugin}/@id");
    XQuery pluginversionQuery = queryhandler.createQuery("/feature/{plugin}/@version");
    XQuery pluginFragmentQuery = queryhandler.createQuery("/feature/{plugin}/@fragment");
    XQuery pluginOsQuery = queryhandler.createQuery("/feature/{plugin}/@os");
    XQuery pluginArchQuery = queryhandler.createQuery("/feature/{plugin}/@arch");
    XQuery pluginWsQuery = queryhandler.createQuery("/feature/{plugin}/@ws");
    XQuery pluginNlQuery = queryhandler.createQuery("/feature/{plugin}/@nl");
    XQuery pluginDownloadSizeQuery = queryhandler.createQuery("/feature/{plugin}/@download-size");
    XQuery pluginInstallSizeQuery = queryhandler.createQuery("/feature/{plugin}/@install-size");
    XQuery pluginUnpackQuery = queryhandler.createQuery("/feature/{plugin}/@unpack");

    XQuery includesIdQuery = queryhandler.createQuery("/feature/{includes}/@id");
    XQuery includesVersionQuery = queryhandler.createQuery("/feature/{includes}/@version");
    XQuery includesNameQuery = queryhandler.createQuery("/feature/{includes}/@name");
    XQuery includesOptionalQuery = queryhandler.createQuery("/feature/{includes}/@optional");
    XQuery includesSearchLocationQuery = queryhandler.createQuery("/feature/{includes}/@search-location");
    XQuery includesOsQuery = queryhandler.createQuery("/feature/{includes}/@os");
    XQuery includesArchQuery = queryhandler.createQuery("/feature/{includes}/@arch");
    XQuery includesWsQuery = queryhandler.createQuery("/feature/{includes}/@ws");
    XQuery includesNlQuery = queryhandler.createQuery("/feature/{includes}/@nl");

    // parse the file
    XQueryHandler.queryInputStream(inputStream, queryhandler);

    feature.setId(idQuery.getSingleResult());
    feature.setVersion(new Version(versionQuery.getSingleResult()));
    feature.setLabel(labelQuery.getSingleResult());
    feature.setProviderName(providerNameQuery.getSingleResult());
    feature.setImage(imageQuery.getSingleResult());
    feature.setOperatingSystem(osQuery.getSingleResult());
    feature.setMachineArchitecture(archQuery.getSingleResult());
    feature.setWindowingSystem(wsQuery.getSingleResult());
    feature.setLocale(nlQuery.getSingleResult());
    feature.setColocationAffinity(colocationAffinityQuery.getSingleResult());
    feature.setPrimary(Boolean.valueOf(primaryQuery.getSingleResult()).booleanValue());
    feature.setExclusive(Boolean.valueOf(exclusiveQuery.getSingleResult()).booleanValue());
    feature.setPlugin(pluginQuery.getSingleResult());
    feature.setApplication(applicationQuery.getSingleResult());

    String[] pluginIds = pluginIdQuery.getResult();
    String[] pluginVersions = pluginversionQuery.getResult();
    String[] pluginFragments = pluginFragmentQuery.getResult();
    String[] pluginOSs = pluginOsQuery.getResult();
    String[] pluginArchs = pluginArchQuery.getResult();
    String[] pluginWSs = pluginWsQuery.getResult();
    String[] pluginNls = pluginNlQuery.getResult();
    String[] pluginDownloadSizes = pluginDownloadSizeQuery.getResult();
    String[] pluginInstallSizes = pluginInstallSizeQuery.getResult();
    String[] pluginUnpacks = pluginUnpackQuery.getResult();

    for (int i = 0; i < pluginIds.length; i++) {
      FeatureManifestImpl.PluginImpl plugin = new FeatureManifestImpl.PluginImpl();
      plugin.setId(pluginIds[i]);
      plugin.setVersion(new Version(pluginVersions[i]));
      plugin.setFragment(Boolean.valueOf(pluginFragments[i]).booleanValue());
      plugin.setOperatingSystem(pluginOSs[i]);
      plugin.setMachineArchitecture(pluginArchs[i]);
      plugin.setWindowingSystem(pluginWSs[i]);
      plugin.setLocale(pluginNls[i]);
      plugin.setDownloadSize(pluginDownloadSizes[i]);
      plugin.setInstallSize(pluginInstallSizes[i]);
      plugin.setUnpack(Boolean.valueOf(pluginUnpacks[i]).booleanValue());
      feature.addPlugin(plugin);
    }

    String[] includesId = includesIdQuery.getResult();
    String[] includesVersion = includesVersionQuery.getResult();
    String[] includesName = includesNameQuery.getResult();
    String[] includesOptional = includesOptionalQuery.getResult();
    String[] includesSearchLocation = includesSearchLocationQuery.getResult();
    String[] includesOs = includesOsQuery.getResult();
    String[] includesArch = includesArchQuery.getResult();
    String[] includesWs = includesWsQuery.getResult();
    String[] includesNl = includesNlQuery.getResult();

    for (int i = 0; i < includesId.length; i++) {
      FeatureManifestImpl.IncludesImpl includes = new FeatureManifestImpl.IncludesImpl();
      includes.setId(includesId[i]);
      includes.setVersion(new Version(includesVersion[i]));
      includes.setName(includesName[i]);
      includes.setOptional(Boolean.valueOf(includesOptional[i]).booleanValue());
      includes.setSearchLocation(includesSearchLocation[i]);
      includes.setOperatingSystem(includesOs[i]);
      includes.setMachineArchitecture(includesArch[i]);
      includes.setWindowingSystem(includesWs[i]);
      includes.setLocale(includesNl[i]);
      feature.addIncludes(includes);
    }

    return feature;
  }
}
