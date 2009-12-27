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
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.model.product.ProductDefinition;
import org.ant4eclipse.lib.pde.model.product.ProductDefinitionParser;
import org.ant4eclipse.lib.pde.model.product.ProductOs;
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
import java.util.Hashtable;
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
  private static final String         PROP_PRODUCTID       = "product.id";

  /** - */
  private static final String         PROP_PRODUCTNAME     = "product.name";

  /** - */
  private static final String         PROP_BASEDONFEATURES = "product.basedonfeatures";

  /** - */
  private static final String         PROP_APPLICATIONID   = "product.applicationid";

  /** - */
  private static final String         PROP_LAUNCHERNAME    = "product.launchername";

  /** - */
  private static final String         PROP_VERSION         = "product.version";

  /** - */
  private static final String         PROP_VMARGS          = "product.vmargs";

  /** - */
  private static final String         PROP_PROGRAMARGS     = "product.programargs";

  /** - */
  private static final String         PROP_CONFIGINI       = "product.configini";

  /** - */
  private static final String         PROP_GUIEXE          = "product.guiexe";

  /** - */
  private static final String         PROP_CMDEXE          = "product.cmdexe";

  /** - */
  private static final String         PROP_FEATUREID       = "feature.id";

  /** - */
  private static final String         PROP_FEATUREVERSION  = "feature.version";

  /** - */
  private static final String         PROP_PLUGINID        = "plugin.id";

  /** - */
  private static final String         PROP_PLUGINISSOURCE  = "plugin.isSource";

  /** - */
  private static final String         PROP_PLUGINFILE      = "plugin.file";

  /** - */
  private static final String         PROP_PLUGINFILELIST  = "plugin.filelist";

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
    properties.put(PROP_PLUGINID, pluginid);
    if (getWorkspace().hasProject(pluginid)) {
      EclipseProject project = getWorkspace().getProject(pluginid);
      File location = project.getFolder();
      properties.put(PROP_PLUGINISSOURCE, "true");
      properties.put(PROP_PLUGINFILE, location.getAbsolutePath());
    } else {
      BundleDescription bundledesc = targetplatform.getBundleDescription(pluginid);
      BundleSource bundlesource = (BundleSource) bundledesc.getUserObject();
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
