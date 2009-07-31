package org.ant4eclipse.pde.ant;

import org.ant4eclipse.jdt.ant.JdtExecutorValues;

public interface PdeExecutorValues extends JdtExecutorValues {

  /** - */
  static final String FEATURE_ID                        = "feature.id";

  /** - */
  static final String FEATURE_VERSION                   = "feature.version";

  /** - */
  static final String FEATURE_RESOLVED_VERSION          = "feature.resolved.version";

  /** - */
  static final String FEATURE_IS_SOURCE                 = "feature.isSource";

  /** the path of the feature jar, directory or project directory */
  static final String FEATURE_FILE_PATH                 = "feature.file.path";

  /********/

  /** - */
  static final String BUILD_PROPERTIES_BINARY_EXCLUDES  = "build.properties.binary.excludes";

  /** - */
  static final String BUILD_PROPERTIES_BINARY_INCLUDES  = "build.properties.binary.includes";

  /** - */
  static final String FEATURE_PROVIDERNAME              = "feature.providername";

  /** - */
  static final String FEATURE_LABEL                     = "feature.label";

  /** - */
  static final String FEATURE_PLUGINS_RESOLVED_VERSIONS = "feature.plugins.resolved.versions";

  /** - */
  static final String FEATURE_FILELIST                  = "feature.filelist";

  /** - */
  static final String FEATURE_FILE                      = "feature.file";

  /** - */
  static final String PLUGIN_UNPACK                     = "plugin.unpack";

  /** - */
  static final String PLUGIN_FRAGMENT                   = "plugin.fragment";

  /** - */
  static final String PLUGIN_LOCALE                     = "plugin.locale";

  /** - */
  static final String PLUGIN_OPERATINGSYSTEM            = "plugin.operatingsystem";

  /** - */
  static final String PLUGIN_MACHINEARCHITECTURE        = "plugin.machinearchitecture";

  /** - */
  static final String PLUGIN_WINDOWINGSYSTEM            = "plugin.windowingsystem";

  /** - */
  static final String PLUGIN_INSTALLSIZE                = "plugin.installsize";

  /** - */
  static final String PLUGIN_DOWNLOADSIZE               = "plugin.downloadsize";

  /** - */
  static final String PLUGIN_RESOLVED_VERSION           = "plugin.resolved.version";

  /** - */
  static final String PLUGIN_VERSION                    = "plugin.version";

  /** - */
  static final String PLUGIN_FILELIST                   = "plugin.filelist";

  /** - */
  static final String PLUGIN_FILENAME                   = "plugin.filename";

  /** - */
  static final String PLUGIN_FILE                       = "plugin.file";

  /** - */
  static final String PLUGIN_IS_SOURCE                  = "plugin.isSource";

  /** - */
  static final String PLUGIN_ID                         = "plugin.id";

  /** - */
  static final String BUNDLE_VERSION                    = "bundle.version";

  /** - */
  static final String BUNDLE_RESOLVED_VERSION           = "bundle.resolved.version";

  /** - */
  static final String LIBRARY_IS_SELF                   = "library.isSelf";

  /** - */
  static final String LIBRARY_NAME                      = "library.name";

}
