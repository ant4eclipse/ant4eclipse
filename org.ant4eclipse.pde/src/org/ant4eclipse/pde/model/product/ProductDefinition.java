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
package org.ant4eclipse.pde.model.product;

import org.ant4eclipse.core.data.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Represents an eclipse product definition (<code>*.product</code>).
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProductDefinition {

  /*
   * <configIni use="default" path="config.ini"> <win32>/org.autosar.mmt2.product/config.ini</win32> </configIni>
   * 
   * <launcherArgs> <vmArgs>-Xmx768m</vmArgs> </launcherArgs>
   * 
   * <windowImages/>
   * 
   * <launcher name="mmt2"> <solaris/> <win useIco="false"> <bmp/> </win> </launcher>
   * 
   * <vm> </vm>
   */

  /** - */
  private String       _name;

  /** - */
  private String       _id;

  /** - */
  private String       _application;

  /** - */
  private Version      _version;

  /** - */
  private boolean      _basedonfeatures;

  /** - */
  private List<String> _pluginids;

  /** - */
  private List<String> _fragmentids;

  /**
   * Initialises this data structure.
   */
  public ProductDefinition() {
    this._pluginids = new ArrayList<String>();
    this._fragmentids = new ArrayList<String>();
    this._name = null;
    this._id = null;
    this._application = null;
    this._version = null;
    this._basedonfeatures = false;
  }

  /**
   * Registers the supplied plugin id.
   * 
   * @param pluginid
   *          The id of the included plugin. Neither <code>null</code> nor empty.
   * @param isfragment
   *          <code>true</code> <=> The plugin is a framgent.
   */
  void addPlugin(String pluginid, boolean isfragment) {
    if (isfragment) {
      this._fragmentids.add(pluginid);
    } else {
      this._pluginids.add(pluginid);
    }
  }

  /**
   * Returns a list of all currently registered plugin ids.
   * 
   * @return A list of all currently registered plugin ids. Not <code>null</code>.
   */
  public String[] getPluginIds() {
    return this._pluginids.toArray(new String[this._pluginids.size()]);
  }

  /**
   * Returns a list of all currently registered fragment ids.
   * 
   * @return A list of all currently registered fragment ids. Not <code>null</code>.
   */
  public String[] getFragmentIds() {
    return this._fragmentids.toArray(new String[this._fragmentids.size()]);
  }

  /**
   * Changes the name for this product.
   * 
   * @param newname
   *          The new name for this product. Neither <code>null</code> nor empty.
   */
  void setName(String newname) {
    this._name = newname;
  }

  /**
   * Returns the name for this product.
   * 
   * @return The name for this product. Neither <code>null</code> nor empty.
   */
  public String getName() {
    return this._name;
  }

  /**
   * Changes the id for this product.
   * 
   * @param newid
   *          The new id for this product. Neither <code>null</code> nor empty.
   */
  void setId(String newid) {
    this._id = newid;
  }

  /**
   * Returns the id for this product.
   * 
   * @return The id for this product. Neither <code>null</code> nor empty.
   */
  public String getId() {
    return this._id;
  }

  /**
   * Changes the application id associated with this product.
   * 
   * @param newapplication
   *          The new application id. Neither <code>null</code> nor empty.
   */
  void setApplication(String newapplication) {
    this._application = newapplication;
  }

  /**
   * Returns the id of the product application.
   * 
   * @return The id of the product application. Neither <code>null</code> nor empty.
   */
  public String getApplication() {
    return this._application;
  }

  /**
   * Changes the version for this product.
   * 
   * @param newversion
   *          The new version for this product. Not <code>null</code>.
   */
  void setVersion(Version newversion) {
    this._version = newversion;
  }

  /**
   * Returns the version of the product.
   * 
   * @return The version of the product. Not <code>null</code>.
   */
  public Version getVersion() {
    return this._version;
  }

  /**
   * (Un)Marks this product as based on features.
   * 
   * @param newbasedonfeatures
   *          <code>true</code> <=> This product is based on features.
   */
  void setBasedOnFeatures(boolean newbasedonfeatures) {
    this._basedonfeatures = newbasedonfeatures;
  }

  /**
   * Returns <code>true</code> if this product is based on features rather than plugins.
   * 
   * @return <code>true</code> <=> This product is based on features.
   */
  public boolean isBasedOnFeatures() {
    return this._basedonfeatures;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ProductDefinition:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(", _id: ");
    buffer.append(this._id);
    buffer.append(", _application: ");
    buffer.append(this._application);
    buffer.append(", _version: ");
    buffer.append(this._version);
    buffer.append(", _basedonfeatures: ");
    buffer.append(this._basedonfeatures);
    if (this._basedonfeatures) {
    } else {
      buffer.append(", _pluginids: {");
      if (!this._pluginids.isEmpty()) {
        buffer.append(this._pluginids.get(0));
        for (int i = 1; i < this._pluginids.size(); i++) {
          buffer.append(",");
          buffer.append(this._pluginids.get(i));
        }
      }
      buffer.append("}");
      buffer.append(", _fragmentids: {");
      if (!this._fragmentids.isEmpty()) {
        buffer.append(this._fragmentids.get(0));
        for (int i = 1; i < this._fragmentids.size(); i++) {
          buffer.append(",");
          buffer.append(this._fragmentids.get(i));
        }
      }
      buffer.append("}");
    }
    buffer.append("]");
    return buffer.toString();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((this._name == null) ? 0 : this._name.hashCode());
    result = 31 * result + ((this._id == null) ? 0 : this._id.hashCode());
    result = 31 * result + ((this._application == null) ? 0 : this._application.hashCode());
    result = 31 * result + ((this._version == null) ? 0 : this._version.hashCode());
    result = 31 * result + (this._basedonfeatures ? 1 : 0);
    for (int i = 0; i < this._pluginids.size(); i++) {
      result = 31 * result + this._pluginids.get(i).hashCode();
    }
    for (int i = 0; i < this._fragmentids.size(); i++) {
      result = 31 * result + this._fragmentids.get(i).hashCode();
    }
    return result;
  }

} /* ENDCLASS */
