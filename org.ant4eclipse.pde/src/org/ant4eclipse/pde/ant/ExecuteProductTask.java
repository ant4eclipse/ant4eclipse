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
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.ant.ExtendedBuildException;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.util.StringMap;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pde.PdeExceptionCode;
import org.ant4eclipse.pde.model.product.ProductDefinition;
import org.ant4eclipse.pde.model.product.ProductDefinitionParser;
import org.ant4eclipse.pde.model.product.ProductOs;

import org.ant4eclipse.platform.PlatformExceptionCode;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.platform.ant.core.task.AbstractExecuteProjectTask;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
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
  private static final String         PROP_FEATUREID       = "feature.id";

  /** - */
  private static final String         PROP_FEATUREVERSION  = "feature.version";

  /** - */
  private static final String         PROP_PLUGINID        = "plugin.id";

  /** - */
  private static final String         PROP_PLUGINISSOURCE  = "plugin.isSource";

  // configini,
  // programargs,
  // vmargs,
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

    // execute scoped macro definitions
    for (ScopedMacroDefinition<String> scopedmacro : getScopedMacroDefinitions()) {
      Scope scope = Scope.valueOf(scopedmacro.getScope());
      switch (scope) {
      case ForProduct:
        executeForProduct(productdef, scopedmacro.getMacroDef());
        break;
      case ForEachFeature:
        executeForEachFeature(productdef, scopedmacro.getMacroDef());
        break;
      case ForEachPlugin:
        executeForEachPlugin(productdef, scopedmacro.getMacroDef());
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
   */
  private void executeForEachPlugin(ProductDefinition productdef, MacroDef macrodef) {

    StringMap properties = new StringMap();
    contributeForAll(properties, productdef);

    String[] pluginids = productdef.getPluginIds();
    for (String pluginid : pluginids) {
      if (getWorkspace().hasProject(pluginid)) {
        contributeForPlugin(properties, productdef, pluginid);
        executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties));
      }
    }

    String[] fragmentids = productdef.getFragmentIds();
    for (String fragmentid : fragmentids) {
      if (getWorkspace().hasProject(fragmentid)) {
        contributeForPlugin(properties, productdef, fragmentid);
        executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties));
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
   */
  private void executeForEachFeature(ProductDefinition productdef, MacroDef macrodef) {

    StringMap properties = new StringMap();
    contributeForAll(properties, productdef);

    String[] featureids = productdef.getFeatureIds();
    for (String featureid : featureids) {
      contributeForFeature(properties, productdef, featureid);
      executeMacroInstance(macrodef, new LocalMacroExecutionValuesProvider(properties));
    }

  }

  /**
   * Executes the macro for the scope: {@link Scope#ForProduct}.
   * 
   * @param productdef
   *          The productdefinition to be used. Not <code>null</code>.
   * @param macrodef
   *          The macro constituting the scope. Not <code>null</code>.
   */
  private void executeForProduct(ProductDefinition productdef, MacroDef macrodef) {
    StringMap properties = new StringMap();
    contributeForAll(properties, productdef);
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
      throw new ExtendedBuildException("The product configuration '%s' is not a regular file.", this._product);
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
   */
  private void contributeForAll(StringMap properties, ProductDefinition productdef) {
    properties.put(PROP_PRODUCTID, productdef.getId());
    properties.put(PROP_PRODUCTNAME, productdef.getName());
    properties.put(PROP_BASEDONFEATURES, String.valueOf(productdef.isBasedOnFeatures()));
    properties.put(PROP_APPLICATIONID, productdef.getApplication());
    properties.put(PROP_LAUNCHERNAME, productdef.getLaunchername());
    properties.put(PROP_VERSION, String.valueOf(productdef.getVersion()));
    properties.put(PROP_VMARGS, productdef.getVmArgs(this._os));
    properties.put(PROP_PROGRAMARGS, productdef.getProgramArgs(this._os));
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
   * @param productdef
   *          The product definition instance providing the necessary information. Not <code>null</code>.
   * @param pluginid
   *          The id of the plugin which specific settings shall be applied. Neither <code>null</code> nor empty.
   */
  private void contributeForPlugin(StringMap properties, ProductDefinition productdef, String pluginid) {
    properties.put(PROP_PLUGINID, pluginid);
    if (getWorkspace().getProject(pluginid) != null) {
      properties.put(PROP_PLUGINISSOURCE, "true");
    } else {
      properties.put(PROP_PLUGINISSOURCE, "false");
    }
  }

  /**
   * Implementation of a custom MacroExecutionValuesProvider. It's purpose is to provide the several properties for the
   * scoped macros to make them functional.
   */
  private static class LocalMacroExecutionValuesProvider implements MacroExecutionValuesProvider {

    /** - */
    private StringMap _properties;

    /**
     * Initialises this provider using a specified product definition file.
     * 
     * @param properties
     *          The properties to be set. Not <code>null</code>.
     */
    public LocalMacroExecutionValuesProvider(StringMap properties) {
      this._properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
      values.getProperties().putAll(this._properties);
      return values;
    }

  } /* ENDCLASS */

} /* ENDCLASS */
