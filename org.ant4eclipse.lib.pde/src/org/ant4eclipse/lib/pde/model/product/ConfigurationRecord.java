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
package org.ant4eclipse.lib.pde.model.product;

/**
 * <p>
 * A ConfigurationRecord just provides the information needed for the setup of the config.ini file.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ConfigurationRecord implements Comparable<ConfigurationRecord> {

  /** - */
  private boolean _autostart;

  /** - */
  private String  _id;

  /** - */
  private int     _startlevel;

  /**
   * Initialises this data structure.
   */
  public ConfigurationRecord() {
    this._autostart = false;
    this._id = null;
    this._startlevel = 4;
  }

  /**
   * Changes the id for the corresponding plugin.
   * 
   * @param newid
   *          The new id for the corresponding plugin. Neither <code>null</code> nor empty.
   */
  void setId(String newid) {
    this._id = newid;
  }

  /**
   * Returns the id for the corresponding plugin.
   * 
   * @return The id for the corresponding plugin. Neither <code>null</code> nor empty.
   */
  public String getId() {
    return this._id;
  }

  /**
   * Enables/disables the autostart for the corresponding plugin.
   * 
   * @param newautostart
   *          <code>true</code> <=> Enables the autostart for the corresponding plugin.
   */
  void setAutoStart(boolean newautostart) {
    this._autostart = newautostart;
  }

  /**
   * Returns <code>true</code> if autostart for the corressponding plugin is enabled.
   * 
   * @return <code>true</code> <=> Autostart for the corresponding plugin is enabled.
   */
  public boolean isAutoStart() {
    return this._autostart;
  }

  /**
   * Changes the current start level.
   * 
   * @param newstartlevel
   *          The new start level.
   */
  void setStartLevel(int newstartlevel) {
    this._startlevel = newstartlevel;
  }

  /**
   * Returns the current start level.
   * 
   * @return The current start level.
   */
  public int getStartLevel() {
    return this._startlevel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ConfigurationRecord:");
    buffer.append(" _id: ");
    buffer.append(this._id);
    buffer.append(", _autostart: ");
    buffer.append(this._autostart);
    buffer.append(", _startlevel: ");
    buffer.append(this._startlevel);
    buffer.append("]");
    return buffer.toString();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((this._id == null) ? 0 : this._id.hashCode());
    result = 31 * result + (this._autostart ? 0 : 1);
    result = 31 * result + this._startlevel;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public int compareTo(ConfigurationRecord other) {
    if (other != null) {
      return this._id.compareTo(other._id);
    }
    return 1;
  }

} /* ENDCLASS */
