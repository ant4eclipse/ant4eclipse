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
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.model.product.ProductDefinition;
import org.ant4eclipse.lib.pde.model.product.ProductDefinitionParser;
import org.ant4eclipse.lib.pde.model.product.ProductOs;
import org.ant4eclipse.lib.pde.tools.BundleStartRecord;
import org.ant4eclipse.lib.pde.tools.SimpleConfiguratorBundles;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.ant4eclipse.lib.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.lib.pde.tools.TargetPlatformRegistry;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
  private static final String MSG_USING_HARDCODED       = "Failed to detect bundles, so the following hard coded ones are used:";

  /** - */
  private static final String MSG_FAILED_BUNDLESINFO    = "Failed to load bundles info file '%s'. Cause: %s";

  /** - */
  private static final String MSG_ACCESSING_BUNDLESINFO = "Accessing bundles info file '%s' to identify start bundles...";

  /** - */
  private static final String MSG_ACCESSING_CONFIGINI   = "Accessing file '%s' to identify start bundles...";

  /** - */
  private static enum Scope {
    /** - */
    ForProduct,
    /** - */
    ForEachFeature,
    // /** - */
    // ForEachTargetFeature,
    /** - */
    ForEachPlugin
  }

  /** - */
  private static final String         PROP_PRODUCTID         = "product.id";

  /** - */
  private static final String         PROP_PRODUCTNAME       = "product.name";

  /** - */
  private static final String         PROP_BASEDONFEATURES   = "product.basedonfeatures";

  /** - */
  private static final String         PROP_APPLICATIONID     = "product.applicationid";

  /** - */
  private static final String         PROP_LAUNCHERNAME      = "product.launchername";

  /** - */
  private static final String         PROP_VERSION           = "product.version";

  /** - */
  private static final String         PROP_VMARGS            = "product.vmargs";

  /** - */
  private static final String         PROP_PROGRAMARGS       = "product.programargs";

  /** - */
  private static final String         PROP_CONFIGINI         = "product.configini";

  /** - */
  private static final String         PROP_GUIEXE            = "product.guiexe";

  /** - */
  private static final String         PROP_CMDEXE            = "product.cmdexe";

  /** - */
  private static final String         PROP_FEATUREID         = "feature.id";

  /** - */
  private static final String         PROP_FEATUREVERSION    = "feature.version";

  /** - */
  private static final String         PROP_PLUGINID          = "plugin.id";

  /** - */
  private static final String         PROP_PLUGINISSOURCE    = "plugin.isSource";

  /** - */
  private static final String         PROP_OSGIBUNDLES       = "osgi.bundles";

  /**
   * 
   */
  private static final String         PROP_PLUGINPROJECTNAME = "plugin.projectName";

  /** - */
  private static final String         PROP_PLUGINFILE        = "plugin.file";

  /** - */
  private static final String         PROP_PLUGINFILELIST    = "plugin.filelist";

  // programargs,
  // wsplugins,
  // wsfragments,
  // wsfeatures,

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
    TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
    configuration.setPreferProjects(true);

    // fetch the target platform
    TargetPlatformRegistry registry = ServiceRegistry.instance().getService(TargetPlatformRegistry.class);
    TargetPlatform targetplatform = registry.getInstance(getWorkspace(), getTargetPlatformId(), configuration);

    StringMap properties = new StringMap();
    contributeForAll(properties, productdef, targetplatform);

    // execute scoped macro definitions
    for (ScopedMacroDefinition<String> scopedmacro : getScopedMacroDefinitions()) {
      Scope scope = Scope.valueOf(scopedmacro.getScope());
      switch (scope) {
      case ForProduct:
        executeForProduct(productdef, scopedmacro.getMacroDef(), properties, targetplatform);
        break;
      case ForEachFeature:
        executeForEachFeature(productdef, scopedmacro.getMacroDef(), properties, targetplatform);
        break;
      // case ForEachTargetFeature:
      // executeForEachTargetFeature(productdef, scopedmacro.getMacroDef(), targetplatform);
      // break;
      case ForEachPlugin:
        executeForEachPlugin(productdef, scopedmacro.getMacroDef(), properties, targetplatform);
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
  private void executeForEachPlugin(ProductDefinition productdef, MacroDef macrodef, StringMap forall,
      TargetPlatform targetplatform) {

    StringMap properties = new StringMap();
    properties.putAll(forall);

    Map<String, Object> references = new Hashtable<String, Object>();

    String[] pluginids = productdef.getPluginIds();
    for (String pluginid : pluginids) {
      contributeForPlugin(properties, references, productdef, pluginid, targetplatform);
      executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties, references));
    }

    String[] fragmentids = productdef.getFragmentIds();
    for (String fragmentid : fragmentids) {
      contributeForPlugin(properties, references, productdef, fragmentid, targetplatform);
      executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties, references));
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
  private void executeForEachFeature(ProductDefinition productdef, MacroDef macrodef, StringMap forall,
      TargetPlatform targetplatform) {

    StringMap properties = new StringMap();
    properties.putAll(forall);

    String[] featureids = productdef.getFeatureIds();
    for (String featureid : featureids) {
      contributeForFeature(properties, productdef, featureid);
      executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties));
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
  private void executeForProduct(ProductDefinition productdef, MacroDef macrodef, StringMap forall,
      TargetPlatform targetplatform) {
    StringMap properties = new StringMap();
    properties.putAll(forall);
    executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {

    super.preconditions();

    requireWorkspaceDirectorySet();
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
   * @see "http://help.eclipse.org/help32/index.jsp?topic=/org.eclipse.pde.doc.user/guide/tools/editors/product_editor/configuration.htm"
   * 
   * @param targetlocations
   *          The target platform locations currently registered. Not <code>null</code>.
   * @param records
   *          The start records provided the product configuration file. Not <code>null</code>.
   * 
   * @return A comma separated list of all osgi bundles. Not <code>null</code>.
   */
  private String collectOsgiBundles(File[] targetlocations, BundleStartRecord[] records) {

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

  /**
   * Contributes general settings to the macro execution properties.
   * 
   * @param properties
   *          The properties used to be filled with the settings. Not <code>null</code>.
   * @param productdef
   *          The product definition instance providing the necessary information. Not <code>null</code>.
   * @param targetplatform
   *          The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
   */
  private void contributeForAll(StringMap properties, ProductDefinition productdef, TargetPlatform targetplatform) {

    properties.put(PROP_PRODUCTID, productdef.getId());
    properties.put(PROP_PRODUCTNAME, productdef.getName());
    properties.put(PROP_BASEDONFEATURES, String.valueOf(productdef.isBasedOnFeatures()));
    properties.put(PROP_APPLICATIONID, productdef.getApplication());
    properties.put(PROP_LAUNCHERNAME, productdef.getLaunchername());
    properties.put(PROP_VERSION, String.valueOf(productdef.getVersion()));
    properties.put(PROP_VMARGS, productdef.getVmArgs(this._os));
    properties.put(PROP_PROGRAMARGS, productdef.getProgramArgs(this._os));
    properties.put(PROP_OSGIBUNDLES, collectOsgiBundles(targetplatform.getLocations(), productdef
        .getConfigurationRecords()));

    String configini = productdef.getConfigIni(this._os);
    if (configini == null) {
      properties.put(PROP_CONFIGINI, "");
    } else {
      int endofprojectname = configini.indexOf('/', 1);
      String projectname = configini.substring(1, endofprojectname);
      String childname = configini.substring(endofprojectname + 1);
      EclipseProject project = getWorkspace().getProject(projectname);
      File path = project.getChild(childname);
      properties.put(PROP_CONFIGINI, path.getAbsolutePath());
    }

    String guiexe = "eclipse";
    String cmdexe = "eclipse";

    switch (this._os) {
    case win32:
      guiexe = "eclipse.exe";
      cmdexe = "eclipsec.exe";
      break;
    default:
      throw new RuntimeException("NYI");
    }

    File fileguiexe = null;
    File filecmdexe = null;

    File[] targetlocations = targetplatform.getLocations();
    for (File targetlocation : targetlocations) {
      if (fileguiexe == null) {
        File child = new File(targetlocation, guiexe);
        if (child.isFile()) {
          fileguiexe = child;
        }
      }
      if (filecmdexe == null) {
        File child = new File(targetlocation, cmdexe);
        if (child.isFile()) {
          filecmdexe = child;
        }
      }
    }

    if ((fileguiexe == null) || (filecmdexe == null)) {
      throw new RuntimeException("NYI");
    }

    properties.put(PROP_GUIEXE, fileguiexe.getAbsolutePath());
    properties.put(PROP_CMDEXE, filecmdexe.getAbsolutePath());

  }

  /**
   * Contributes feature specific settings to the macro execution properties.
   * 
   * @param properties
   *          The properties used to be filled with the settings. Not <code>null</code>.
   * @param productdef
   *          The product definition instance providing the necessary information. Not <code>null</code>.
   * @param featureid
   *          The id of the feature which specific settings shall be applied. Neither <code>null</code> nor empty.
   */
  private void contributeForFeature(StringMap properties, ProductDefinition productdef, String featureid) {

    Version version = productdef.getFeatureVersion(featureid);

    properties.put(PROP_FEATUREID, featureid);
    properties.put(PROP_FEATUREVERSION, String.valueOf(version));

  }

  /**
   * Contributes plugin specific settings to the macro execution properties.
   * 
   * @param properties
   *          The properties used to be filled with the settings. Not <code>null</code>.
   * @param references
   *          The references that will be accessible by the macro. Not <code>null</code>.
   * @param productdef
   *          The product definition instance providing the necessary information. Not <code>null</code>.
   * @param pluginid
   *          The id of the plugin which specific settings shall be applied. Neither <code>null</code> nor empty.
   * @param targetplatform
   *          The TargetPlatform used to resolve the bundles against. Not <code>null</code>.
   */
  private void contributeForPlugin(StringMap properties, Map<String, Object> references, ProductDefinition productdef,
      String pluginid, TargetPlatform targetplatform) {
    A4ELogging.info("contributeForPlugin pluginId: '%s'", pluginid);
    properties.put(PROP_PLUGINID, pluginid);

    if (!targetplatform.hasBundleDescription(pluginid)) {
      throw new Ant4EclipseException(PdeExceptionCode.INVALID_PRODUCT_DEFINITION, productdef.getApplication(),
          "plugin '" + pluginid + "' not found in workspace/target platform");
    }

    BundleDescription bundledesc = targetplatform.getBundleDescription(pluginid);

    BundleSource bundlesource = (BundleSource) bundledesc.getUserObject();

    if (bundlesource.isEclipseProject()) {
      // Plug-in is a source project contained in the workspace
      EclipseProject project = bundlesource.getAsEclipseProject();
      File location = project.getFolder();
      properties.put(PROP_PLUGINISSOURCE, "true");
      properties.put(PROP_PLUGINFILE, location.getAbsolutePath());
      properties.put(PROP_PLUGINPROJECTNAME, project.getSpecifiedName());
    } else {
      // Plug-in comes from the target platform
      A4ELogging.info("contributeForPlugin bundlesource: '%s'", bundlesource);
      File location = bundlesource.getAsFile();
      properties.put(PROP_PLUGINISSOURCE, "false");
      properties.put(PROP_PLUGINFILE, location.getAbsolutePath());
      references.put(PROP_PLUGINFILELIST, FileListHelper.getFileList(location));
    }

  }

  /**
   * Implementation of a custom MacroExecutionValuesProvider. It's purpose is to provide the several properties for the
   * scoped macros to make them functional.
   */
  private static class LocalMacroExecutionValuesProvider implements MacroExecutionValuesProvider {

    /** - */
    private StringMap           _properties;

    /** - */
    private Map<String, Object> _references;

    /**
     * Initialises this provider using a specified product definition file.
     * 
     * @param properties
     *          The properties to be set. Not <code>null</code>.
     */
    public LocalMacroExecutionValuesProvider(StringMap properties) {
      this(properties, null);
    }

    /**
     * Initialises this provider using a specified product definition file.
     * 
     * @param properties
     *          The properties to be set. Not <code>null</code>.
     * @param references
     *          The references that have to be applied. Maybe <code>null</code>.
     */
    public LocalMacroExecutionValuesProvider(StringMap properties, Map<String, Object> references) {
      this._properties = properties;
      this._references = references;
    }

    /**
     * {@inheritDoc}
     */
    public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
      values.getProperties().putAll(this._properties);
      if (this._references != null) {
        values.getReferences().putAll(this._references);
      }
      return values;
    }

  } /* ENDCLASS */

} /* ENDCLASS */
