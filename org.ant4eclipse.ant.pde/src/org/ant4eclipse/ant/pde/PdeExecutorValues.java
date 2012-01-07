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

import org.ant4eclipse.ant.jdt.JdtExecutorValues;

/**
 * <p>
 * Definition of the keys for PDE specific executor values.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface PdeExecutorValues extends JdtExecutorValues {

  String FEATURE_ID                        = "feature.id";
  String FEATURE_VERSION                   = "feature.version";
  String FEATURE_RESOLVED_VERSION          = "feature.resolved.version";
  String FEATURE_IS_SOURCE                 = "feature.isSource";
  String FEATURE_NL                        = "feature.nl";
  String FEATURE_WS                        = "feature.ws";
  String FEATURE_ARCH                      = "feature.arch";
  String FEATURE_OS                        = "feature.os";
  String FEATURE_SEARCH_LOCATION           = "feature.search-location";
  String FEATURE_OPTIONAL                  = "feature.optional";
  String FEATURE_NAME                      = "feature.name";
  String FEATURE_FILE_PATH                 = "feature.file.path";

  String BUILD_PROPERTIES_BINARY_EXCLUDES  = "build.properties.binary.excludes";
  String BUILD_PROPERTIES_BINARY_INCLUDES  = "build.properties.binary.includes";
  String BUILD_LIBRARYSOURCEROOTS          = "build.librarysourceroots";
  String FEATURE_PROVIDERNAME              = "feature.providername";
  String FEATURE_LABEL                     = "feature.label";
  String FEATURE_PLUGINS_RESOLVED_VERSIONS = "feature.plugins.resolved.versions";
  String FEATURE_FILELIST                  = "feature.filelist";
  String FEATURE_FILE                      = "feature.file";
  String FEATURE_FILE_NAME                 = "feature.file.name";
  
  String PLUGIN_UNPACK                     = "plugin.unpack";
  String PLUGIN_FRAGMENT                   = "plugin.fragment";
  String PLUGIN_LOCALE                     = "plugin.locale";
  String PLUGIN_OPERATINGSYSTEM            = "plugin.operatingsystem";
  String PLUGIN_MACHINEARCHITECTURE        = "plugin.machinearchitecture";
  String PLUGIN_WINDOWINGSYSTEM            = "plugin.windowingsystem";
  String PLUGIN_INSTALLSIZE                = "plugin.installsize";
  String PLUGIN_DOWNLOADSIZE               = "plugin.downloadsize";
  String PLUGIN_RESOLVED_VERSION           = "plugin.resolved.version";
  String PLUGIN_VERSION                    = "plugin.version";
  String PLUGIN_FILELIST                   = "plugin.filelist";
  String PLUGIN_FILENAME                   = "plugin.filename";
  String PLUGIN_FILE                       = "plugin.file";
  String PLUGIN_IS_SOURCE                  = "plugin.isSource";
  String PLUGIN_ID                         = "plugin.id";
  
  String BUNDLE_VERSION                    = "bundle.version";
  String BUNDLE_RESOLVED_VERSION           = "bundle.resolved.version";
  String BUNDLE_SYMBOLIC_NAME              = "bundle.symbolicname";

  String LIBRARY_IS_SELF                   = "library.isSelf";
  String LIBRARY_NAME                      = "library.name";
  String LIBRARY_SOURCE_NAME               = "library.source.name";
  
} /* ENDINTERFACE */
