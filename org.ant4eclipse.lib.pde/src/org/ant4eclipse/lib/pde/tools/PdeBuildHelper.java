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
package org.ant4eclipse.lib.pde.tools;



import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.pde.model.buildproperties.AbstractBuildProperties;
import org.apache.tools.ant.BuildException;
import org.osgi.framework.Version;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PdeBuildHelper
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
public class PdeBuildHelper {

  /**
   * Name of the system property that might contain a value that is used to replace the ".qualifier" string in a
   * Version. (if no property has been set a time stamp will be used)
   */
  public static final String CONTEXT_QUALIFIER_PROPERTY = "ant4eclipse.contextQualifier";

  /** - */
  public static String       CONTEXT_QUALIFIER          = null;

  // /**
  // * Returns the destination directory for the given plugin, that is destDir/plugins/pluginFileName, where
  // * pluginFileName is <code>smybolicname_version</code>
  // *
  // * <p>
  // * If the directory does not exist it will be created. If the directory cannot be created a BuildException will be
  // * thrown
  // * </p>
  // *
  // * @destdir An existing destination directory.
  // * @param effectiveVersion
  // * Plugin version - must be "resolved", i.e. with replaced qualifier
  // * @return An existing directory for the plugin
  // */
  // public static File getExistingPluginDestDirectory(File destdir, String symbolicPluginName, Version
  // effectiveVersion) {
  // Assert.notNull(destdir);
  // Assert.notNull(symbolicPluginName);
  // Assert.notNull(effectiveVersion);
  //
  // File pluginsDir = getExistingDirectory(destdir, "plugins", symbolicPluginName, effectiveVersion);
  // return pluginsDir;
  // }

  /**
   * Returns the destination directory for the given feature, that is destDir/features/featureFileName, where
   * featureFileName is <code>symbolicname_version</code>
   * 
   * <p>
   * If the directory does not exist it will be created. If the directory cannot be created a BuildException will be
   * thrown
   * </p>
   * 
   * @destdir An existing destination directory.
   * @param effectiveVersion
   *          Feature version - must be "resolved", i.e. with replaced qualifier
   * @return An existing directory for the feature
   */
  public static File getExistingFeaturesDestDirectory(File destdir, String featureId, Version effectiveVersion) {
    File pluginsDir = getExistingDirectory(destdir, "features", featureId, effectiveVersion);
    return pluginsDir;
  }

  private static File getExistingDirectory(File destdir, String kind, String id, Version effectiveVersion) {

    // the "feature" or "plugins" directory inside the destdir
    File basedir = new File(destdir, kind);
    if (basedir.exists() && !basedir.isDirectory()) {
      throw new BuildException("'" + kind + "' directory '" + basedir.getAbsolutePath()
          + "' exists, but is not a directory");
    }

    // split creation of plugin dir into two steps to get at slightly better
    // error diagnostics if creation doesn't work
    if (!basedir.exists()) {
      if (!basedir.mkdirs()) {
        throw new BuildException("Could not create '" + basedir + "' directory '" + basedir.getAbsolutePath()
            + "' for an unknown reason");
      }
    }

    String fileName = getFileName(id, effectiveVersion);

    File pluginDir = new File(basedir, fileName);
    if (!pluginDir.exists()) {
      if (!pluginDir.mkdirs()) {
        throw new BuildException("Could not create plugin directory '" + pluginDir.getAbsolutePath()
            + "' for an unknown reason");
      }
    }

    return pluginDir;
  }

  /**
   * Returns the file name that can be used either as target directory- or jar-name for this i.e.
   * "com.mycompany.mybundle_1.2.3". An extension (like ".jar" is not added).
   * 
   * @param effectiveVersion
   *          The resolved version (i.e. with replaced qualifier)
   * @return The filename for this bundle
   */
  private static String getFileName(String id, Version effectiveVersion) {

    StringBuffer pluginFileName = new StringBuffer();
    pluginFileName.append(id);
    pluginFileName.append('_');
    pluginFileName.append(effectiveVersion.toString());
    return pluginFileName.toString();
  }

  /**
   * Returns a "resolved" string representation of the given Version instance.
   * 
   * <p>
   * In the string returned the "qualifier" part of the version will be replaced according to the string passed in the
   * qualifier argument that usualy will contain the "qualifier" property from the build.properties of a plugin or
   * feature project.
   * <p>
   * If there is no ".qualifier" in the version the version will returned unchanged. This is also true if the version is
   * already "resolved", that a Version "1.2.3.20061117" would be returned unchanged.
   * 
   * <p>
   * If the <code>qualifier</code> parameter is:
   * <ul>
   * <li><b>null</b> or <b>"context"</b> the ".qualifier" in the version will be replaced by a "context qualifier", that
   * is the current date</li>
   * <li><b>"none"</b> the ".qualifier" string from the version will be removed</li>
   * <li><b>any other value</b>: the ".qualifier" will be replaced by this value</li>
   * </ul>.
   * 
   * @param version
   *          The version to resolve
   * @param qualifier
   *          The qualifier argument from the build.properties. Might be null
   * @return
   */
  public static Version resolveVersion(Version version, String qualifier) {
    Assure.notNull(qualifier);

    Version qualifiedVersion = null;

    if (hasUnresolvedQualifier(version)) {
      if (qualifier == null || AbstractBuildProperties.isContextQualifer(qualifier)) {
        qualifier = getResolvedContextQualifier();
      } else if (AbstractBuildProperties.isNoneQualifier(qualifier)) {
        qualifier = null;
      }
      qualifiedVersion = new Version(version.getMajor(), version.getMinor(), version.getMicro(), qualifier);
    } else {
      // no ".qualifier" in original version -> return unchanged
      qualifiedVersion = version;
    }

    return qualifiedVersion;
  }

  public static boolean hasUnresolvedQualifier(Version version) {
    Assure.notNull(version);

    return (version != null && "qualifier".equals(version.getQualifier()));
  }

  /**
   * Returns a qualifier that should be used if the plugin's version contains the word "qualifier" as qualifier.
   * <p>
   * 
   * <p>
   * The context qualifier can explizitly set by using the ant4eclipse.contextQualifier System property. Note that this
   * is intended mainly for testing, since otherwise you can specify a qualifier in the build.properties
   */
  public static String getResolvedContextQualifier() {
    if (CONTEXT_QUALIFIER == null) {
      CONTEXT_QUALIFIER = System.getProperty(CONTEXT_QUALIFIER_PROPERTY);
      if (CONTEXT_QUALIFIER == null) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        CONTEXT_QUALIFIER = simpleDateFormat.format(new Date());
      }
    }
    return CONTEXT_QUALIFIER;
  }
}
