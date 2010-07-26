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

import org.ant4eclipse.ant.core.FileListHelper;
import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.ScopedMacroDefinition;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.ant.platform.core.task.AbstractExecuteProjectTask;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.model.product.ProductDefinition;
import org.ant4eclipse.lib.pde.model.product.ProductDefinitionParser;
import org.ant4eclipse.lib.pde.model.product.ProductOs;
import org.ant4eclipse.lib.pde.tools.PlatformConfiguration;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.types.FileList;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * The {@link ExecuteProductTask} can be used to iterate over a product. It implements a loop over all the bundles
 * and/or plug-in-projects contained in a <code>.product</code> file.
 * <p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ExecuteProductTask extends AbstractExecuteProjectTask implements PdeExecutorValues,
    TargetPlatformAwareComponent {

  /** - */
  private static enum Scope {

    /** - */
    ForProduct,

    /** - */
    ForEachFeature,

    /** - */
    ForEachPlugin
  }

  /** - */
  private static final String FRAGMENT_ORG_ECLIPSE_EQUINOX_LAUNCHER = "org.eclipse.equinox.launcher.%s.%s.%s";

  /** - */
  private static final String BUNDLE_ORG_ECLIPSE_EQUINOX_LAUNCHER   = "org.eclipse.equinox.launcher";

  /** - */
  private static final String         PROP_PRODUCTID               = "product.id";

  /** - */
  private static final String         PROP_PRODUCTNAME             = "product.name";

  /** - */
  private static final String         PROP_BASEDONFEATURES         = "product.basedonfeatures";

  /** - */
  private static final String         PROP_APPLICATIONID           = "product.applicationid";

  /** - */
  private static final String         PROP_LAUNCHERNAME            = "product.launchername";

  /** - */
  private static final String         PROP_VERSION                 = "product.version";

  /** - */
  private static final String         PROP_VMARGS                  = "product.vmargs";

  /** - */
  private static final String         PROP_PROGRAMARGS             = "product.programargs";

  /** - */
  private static final String         PROP_CONFIGINI               = "product.configini";

  /** - */
  private static final String         PROP_SPLASH_PLUGIN           = "product.splashplugin";

  /** - */
  private static final String         REF_NATIVE_LAUNCHER_FILELIST = "product.nativelauncher.filelist";

  /** - */
  private static final String         PROP_LAUNCHER_PLUGIN         = "product.launcherplugin";

  /** - */
  private static final String         PROP_LAUNCHER_FRAGMENT       = "product.launcherfragment";

  /** - */
  private static final String         PROP_FEATUREID               = "feature.id";

  /** - */
  private static final String         PROP_FEATUREVERSION          = "feature.version";

  /** - */
  private static final String         PROP_PLUGINID                = "plugin.id";

  /** - */
  private static final String         PROP_PLUGINISSOURCE          = "plugin.isSource";

  /** - */
  private static final String         PROP_OSGIBUNDLES             = "osgi.bundles";

  /** - */
  private static final String         PROP_PLUGINPROJECTNAME       = "plugin.projectName";

  /** - */
  private static final String         PROP_PLUGINFILE              = "plugin.file";

  /** - */
  private static final String         PROP_PLUGINFILELIST          = "plugin.filelist";

  /** - */
  private TargetPlatformAwareDelegate _targetPlatformAwareDelegate;

  /** - */
  private File                        _product;

  /** - */
  private ProductOs                   _os;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProductTask}.
   * </p>
   */
  public ExecuteProductTask() {
    super("executeProduct");
    this._targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();
  }

  /**
   * Changes the location of the product configuration file.
   * 
   * @param newproduct
   *          The new location of the product file. Not <code>null</code>.
   */
  public void setProduct(File newproduct) {
    this._product = newproduct;
  }

  /**
   * Changes the currently used os.
   * 
   * @param newos
   *          The os to be used for the execution process. Not <code>null</code>.
   */
  public void setOs(ProductOs newos) {
    this._os = newos;
  }

  /**
   * {@inheritDoc}
   */
  public String getTargetPlatformId() {
    return this._targetPlatformAwareDelegate.getTargetPlatformId();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTargetPlatformIdSet() {
    return this._targetPlatformAwareDelegate.isTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireTargetPlatformIdSet() {
    this._targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformAwareDelegate.setTargetPlatformId(targetPlatformId);
  }

  /**
   * {@inheritDoc}
   */
  public String getPlatformConfigurationId() {
    return this._targetPlatformAwareDelegate.getPlatformConfigurationId();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPlatformConfigurationIdSet() {
    return this._targetPlatformAwareDelegate.isPlatformConfigurationIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public void setPlatformConfigurationId(String platformConfigurationId) {
    this._targetPlatformAwareDelegate.setPlatformConfigurationId(platformConfigurationId);
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(String name) {
    for (Scope scope : Scope.values()) {
      if (scope.name().equalsIgnoreCase(name)) {
        return createScopedMacroDefinition(scope.name());
      }
    }
    return null;
  }

  /**
   * Loads the product definition.
   * 
   * @return The loaded product definition. Not <code>null</code>.
   */
  private ProductDefinition loadProductDefinition() {
    InputStream instream = null;
    try {
      instream = new FileInputStream(this._product);
      return ProductDefinitionParser.parseProductDefinition(instream);
    } catch (Ant4EclipseException ex) {
      if (ex.getExceptionCode() == PdeExceptionCode.INVALID_CONFIGURATION_VALUE) {
        throw new Ant4EclipseException(PdeExceptionCode.INVALID_PRODUCT_DEFINITION, this._product, ex.getMessage());
      } else {
        throw ex;
      }
    } catch (IOException ex) {
      throw new BuildException(ex);
    } finally {
      Utilities.close(instream);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doExecute() {

    ProductDefinition productdef = loadProductDefinition();

    // create a new target platform configuration
    PlatformConfiguration configuration = new PlatformConfiguration();
    configuration.setPreferProjects(true);

    // fetch the target platform
    TargetPlatform targetplatform = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace());

    // StringMap properties = new StringMap();
    // contributeForAll(properties, null, productdef, targetplatform);

    // execute scoped macro definitions
    for (ScopedMacroDefinition<String> scopedmacro : getScopedMacroDefinitions()) {
      Scope scope = Scope.valueOf(scopedmacro.getScope());
      switch (scope) {
      case ForProduct:
        executeForProduct(productdef, scopedmacro.getMacroDef(), targetplatform);
        break;
      case ForEachFeature:
        executeForEachFeature(productdef, scopedmacro.getMacroDef(), targetplatform);
        break;
      // case ForEachTargetFeature:
      // executeForEachTargetFeature(productdef, scopedmacro.getMacroDef(), targetplatform);
      // break;
      case ForEachPlugin:
        executeForEachPlugin(productdef, scopedmacro.getMacroDef(), targetplatform);
        break;
      }
    }

  }

  /**
   * Executes the macro for the scope: {@link Scope#ForEachPlugin}.
   * 
   * @param productdef
   *          The productdefinition to be used. Not <code>null</code>.
   * @param macrodef
   *          The macro constituting the scope. Not <code>null</code>.
   * @param forall
   *          A bunch of properties used for all execution macros. Not <code>null</code>.
   * @param targetplatform
   *          The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
   */
  private void executeForEachPlugin(final ProductDefinition productdef, MacroDef macrodef,
      final TargetPlatform targetplatform) {

    // iterate over all plug-in identifiers
    for (final String id : productdef.getPluginAndFragmentIds()) {

      // check if bundle exists
      if (!targetplatform.hasBundleDescription(id)) {
        throw new Ant4EclipseException(PdeExceptionCode.INVALID_PRODUCT_DEFINITION, productdef.getApplication(),
            "plugin '" + id + "' not found in workspace/target platform");
      }

      if (targetplatform.matchesPlatformFilter(id)) {

        executeMacroInstance(macrodef, new MacroExecutionValuesProvider() {

          public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

            // set 'general' properties
            contributeForAll(values, productdef, targetplatform);

            // set the plugin id
            values.getProperties().put(PROP_PLUGINID, id);

            BundleDescription bundledesc = targetplatform.getBundleDescription(id);
            BundleSource bundlesource = (BundleSource) bundledesc.getUserObject();

            if (bundlesource.isEclipseProject()) {
              // Plug-in is a source project contained in the workspace
              EclipseProject project = bundlesource.getAsEclipseProject();
              File location = project.getFolder();
              values.getProperties().put(PROP_PLUGINISSOURCE, "true");
              values.getProperties().put(PROP_PLUGINFILE, location.getAbsolutePath());
              values.getProperties().put(PROP_PLUGINPROJECTNAME, project.getSpecifiedName());
            } else {
              // Plug-in comes from the target platform
              File location = bundlesource.getAsFile();
              values.getProperties().put(PROP_PLUGINISSOURCE, "false");
              values.getProperties().put(PROP_PLUGINFILE, location.getAbsolutePath());

              values.getReferences().put(PROP_PLUGINFILELIST, FileListHelper.getFileList(location));
            }

            // return the result
            return values;
          }
        });
      }
    }

  }

  /**
   * Executes the macro for the scope: {@link Scope#ForEachFeature}.
   * 
   * @param productdef
   *          The productdefinition to be used. Not <code>null</code>.
   * @param macrodef
   *          The macro constituting the scope. Not <code>null</code>.
   * @param forall
   *          A bunch of properties used for all execution macros. Not <code>null</code>.
   * @param targetplatform
   *          The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
   */
  private void executeForEachFeature(final ProductDefinition productdef, MacroDef macrodef,
      final TargetPlatform targetplatform) {

    for (final String featureid : productdef.getFeatureIds()) {

      executeMacroInstance(macrodef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          // set 'general' properties
          contributeForAll(values, productdef, targetplatform);

          // set feature id
          values.getProperties().put(PROP_FEATUREID, featureid);

          // set version
          Version version = productdef.getFeatureVersion(featureid);
          values.getProperties().put(PROP_FEATUREVERSION, String.valueOf(version));

          // return result
          return values;
        }
      });
    }

  }

  // /**
  // * Executes the macro for the scope: {@link Scope#ForEachTargetFeature}.
  // *
  // * @param productdef
  // * The productdefinition to be used. Not <code>null</code>.
  // * @param macrodef
  // * The macro constituting the scope. Not <code>null</code>.
  // * @param targetplatform
  // * The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
  // */
  // private void executeForEachTargetFeature(ProductDefinition productdef, MacroDef macrodef,
  // TargetPlatform targetplatform) {
  //
  // StringMap properties = new StringMap();
  // contributeForAll(properties, productdef);
  //
  // String[] featureids = productdef.getFeatureIds();
  // for (String featureid : featureids) {
  // contributeForFeature(properties, productdef, featureid);
  // executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties));
  // }
  //
  // }

  /**
   * Executes the macro for the scope: {@link Scope#ForProduct}.
   * 
   * @param productdef
   *          The productdefinition to be used. Not <code>null</code>.
   * @param macrodef
   *          The macro constituting the scope. Not <code>null</code>.
   * @param forall
   *          A bunch of properties used for all execution macros. Not <code>null</code>.
   * @param targetplatform
   *          The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
   */
  private void executeForProduct(final ProductDefinition productdef, MacroDef macrodef,
      final TargetPlatform targetplatform) {

    // execute the macro
    executeMacroInstance(macrodef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

        // set 'general' properties
        contributeForAll(values, productdef, targetplatform);

        // return result
        return values;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {

    super.preconditions();

    requireWorkspaceDirectoryOrWorkspaceIdSet();
    requireTargetPlatformIdSet();

    if (this._product == null) {
      throw new BuildException("The attribute 'product' has to be set.");
    }

    if (!this._product.isFile()) {
      throw new BuildException(String.format("The product configuration '%s' is not a regular file.", this._product));
    }

    if (this._os == null) {
      throw new BuildException("The attribute 'os' has to be set.");
    }

    // check if all scopes are known. that way the execution doesn't start long during operations
    // while an intermediate step fails.
    for (ScopedMacroDefinition<String> scopedmacro : getScopedMacroDefinitions()) {
      try {
        Scope.valueOf(scopedmacro.getScope());
      } catch (IllegalArgumentException ex) {
        throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_EXECUTION_SCOPE, scopedmacro.getScope());
      }
    }

  }

  /**
   * <p>
   * Contributes general settings to the macro execution properties.
   * </p>
   * 
   * @param values
   * @param productdef
   *          The product definition instance providing the necessary information. Not <code>null</code>.
   * @param targetplatform
   *          The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
   */
  private void contributeForAll(MacroExecutionValues values, ProductDefinition productdef, TargetPlatform targetplatform) {

    values.getProperties().put(PROP_PRODUCTNAME, productdef.getName());
    values.getProperties().put(PROP_BASEDONFEATURES, String.valueOf(productdef.isBasedOnFeatures()));

    if (productdef.hasId()) {
      values.getProperties().put(PROP_PRODUCTID, productdef.getId());
    }

    if (productdef.hasApplication()) {
      values.getProperties().put(PROP_APPLICATIONID, productdef.getApplication());
    }

    values.getProperties().put(PROP_LAUNCHERNAME,
        productdef.hasLaunchername() ? productdef.getLaunchername() : "eclipse");

    values.getProperties().put(PROP_VERSION, String.valueOf(productdef.getVersion()));
    values.getProperties().put(PROP_VMARGS, productdef.getVmArgs(this._os));
    values.getProperties().put(PROP_PROGRAMARGS, productdef.getProgramArgs(this._os));
    values.getProperties().put(PROP_OSGIBUNDLES, ConfigurationHelper.getOsgiBundles(productdef, targetplatform));

    String configini = productdef.getConfigIni(this._os);
    if (configini == null) {
      values.getProperties().put(PROP_CONFIGINI, "");
    } else {
      int endofprojectname = configini.indexOf('/', 1);
      String projectname = configini.substring(1, endofprojectname);
      String childname = configini.substring(endofprojectname + 1);
      EclipseProject project = getWorkspace().getProject(projectname);
      File path = project.getChild(childname);
      values.getProperties().put(PROP_CONFIGINI, path.getAbsolutePath());
    }

    // get executables
    FileList fileList = NativeLauncherHelper.getNativeLauncher(targetplatform);
    values.getReferences().put(REF_NATIVE_LAUNCHER_FILELIST, fileList);

    // set the launcher jars
    // - set the host launcher jar
    BundleDescription launcherHost = targetplatform.getBundleDescription(BUNDLE_ORG_ECLIPSE_EQUINOX_LAUNCHER);
    if (launcherHost == null) {
      throw new Ant4EclipseException(PdeExceptionCode.LAUNCHER_BUNDLE_DOES_NOT_EXIST,
          BUNDLE_ORG_ECLIPSE_EQUINOX_LAUNCHER);
    }
    BundleSource launcherHostSource = (BundleSource) launcherHost.getUserObject();
    values.getProperties().put(PROP_LAUNCHER_PLUGIN, launcherHostSource.getAsFile().getAbsolutePath());

    // - set the fragment launcher jar
    PlatformConfiguration configuration = targetplatform.getTargetPlatformConfiguration();
    String launcherFragmentName = String.format(FRAGMENT_ORG_ECLIPSE_EQUINOX_LAUNCHER, configuration
        .getWindowingSystem(), configuration.getOperatingSystem(), configuration.getArchitecture());
    BundleDescription launcherFragment = targetplatform.getBundleDescription(launcherFragmentName);
    if (launcherFragment == null) {
      throw new Ant4EclipseException(PdeExceptionCode.LAUNCHER_BUNDLE_DOES_NOT_EXIST,
          FRAGMENT_ORG_ECLIPSE_EQUINOX_LAUNCHER);
    }
    BundleSource launcherFragmentSource = (BundleSource) launcherFragment.getUserObject();
    values.getProperties().put(PROP_LAUNCHER_FRAGMENT, launcherFragmentSource.getAsFile().getAbsolutePath());
  }

  // /**
  // * Contributes feature specific settings to the macro execution properties.
  // *
  // * @param properties
  // * The properties used to be filled with the settings. Not <code>null</code>.
  // * @param productdef
  // * The product definition instance providing the necessary information. Not <code>null</code>.
  // * @param featureid
  // * The id of the feature which specific settings shall be applied. Neither <code>null</code> nor empty.
  // */
  // private void contributeForFeature(StringMap properties, ProductDefinition productdef, String featureid) {
  //
  // Version version = productdef.getFeatureVersion(featureid);
  //
  // properties.put(PROP_FEATUREID, featureid);
  // properties.put(PROP_FEATUREVERSION, String.valueOf(version));
  //
  // }

  // /**
  // * Contributes plugin specific settings to the macro execution properties.
  // *
  // * @param properties
  // * The properties used to be filled with the settings. Not <code>null</code>.
  // * @param references
  // * The references that will be accessible by the macro. Not <code>null</code>.
  // * @param productdef
  // * The product definition instance providing the necessary information. Not <code>null</code>.
  // * @param pluginid
  // * The id of the plugin which specific settings shall be applied. Neither <code>null</code> nor empty.
  // * @param targetplatform
  // * The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
  // */
  // private void contributeForPlugin(StringMap properties, Map<String, Object> references, ProductDefinition
  // productdef,
  // String pluginid, TargetPlatform targetplatform) {
  // A4ELogging.info("contributeForPlugin pluginId: '%s'", pluginid);
  // properties.put(PROP_PLUGINID, pluginid);
  //
  // if (!targetplatform.hasBundleDescription(pluginid)) {
  // throw new Ant4EclipseException(PdeExceptionCode.INVALID_PRODUCT_DEFINITION, productdef.getApplication(),
  // "plugin '" + pluginid + "' not found in workspace/target platform");
  // }
  //
  // BundleDescription bundledesc = targetplatform.getBundleDescription(pluginid);
  //
  // BundleSource bundlesource = (BundleSource) bundledesc.getUserObject();
  //
  // if (bundlesource.isEclipseProject()) {
  // // Plug-in is a source project contained in the workspace
  // EclipseProject project = bundlesource.getAsEclipseProject();
  // File location = project.getFolder();
  // properties.put(PROP_PLUGINISSOURCE, "true");
  // properties.put(PROP_PLUGINFILE, location.getAbsolutePath());
  // properties.put(PROP_PLUGINPROJECTNAME, project.getSpecifiedName());
  // } else {
  // // Plug-in comes from the target platform
  // A4ELogging.info("contributeForPlugin bundlesource: '%s'", bundlesource);
  // File location = bundlesource.getAsFile();
  // properties.put(PROP_PLUGINISSOURCE, "false");
  // properties.put(PROP_PLUGINFILE, location.getAbsolutePath());
  // references.put(PROP_PLUGINFILELIST, FileListHelper.getFileList(location));
  // }
  //
  // }

  // /**
  // * Implementation of a custom MacroExecutionValuesProvider. It's purpose is to provide the several properties for
  // the
  // * scoped macros to make them functional.
  // */
  // private static class LocalMacroExecutionValuesProvider implements MacroExecutionValuesProvider {
  //
  // /** - */
  // private StringMap _properties;
  //
  // /** - */
  // private Map<String, Object> _references;
  //
  // /**
  // * Initialises this provider using a specified product definition file.
  // *
  // * @param properties
  // * The properties to be set. Not <code>null</code>.
  // */
  // public LocalMacroExecutionValuesProvider(StringMap properties) {
  // this(properties, null);
  // }
  //
  // /**
  // * Initialises this provider using a specified product definition file.
  // *
  // * @param properties
  // * The properties to be set. Not <code>null</code>.
  // * @param references
  // * The references that have to be applied. Maybe <code>null</code>.
  // */
  // public LocalMacroExecutionValuesProvider(StringMap properties, Map<String, Object> references) {
  // this._properties = properties;
  // this._references = references;
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
  // values.getProperties().putAll(this._properties);
  // if (this._references != null) {
  // values.getReferences().putAll(this._references);
  // }
  // return values;
  // }
  //
  // } /* ENDCLASS */

} /* ENDCLASS */
