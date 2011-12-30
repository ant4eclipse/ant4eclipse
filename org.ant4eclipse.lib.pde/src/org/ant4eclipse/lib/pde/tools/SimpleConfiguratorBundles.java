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

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Collection of bundle description as used by the SimpleConfigurator bundle.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class SimpleConfiguratorBundles {

  private static final String     MSG_INVALID_LINE      = "SimpleConfiguratorBundles: Failed to recognize line '%s'";

  public static final String      ID_SIMPLECONFIGURATOR = "org.eclipse.equinox.simpleconfigurator";

  private List<BundleStartRecord> _records;

  /**
   * Initialises this list of BundleStartRecords based upon a configuration file of the simple configuration.
   * 
   * @param location
   *          The location of the file providing the simple configuration setup.
   */
  public SimpleConfiguratorBundles(File location) {

    this._records = new ArrayList<BundleStartRecord>();

    StringBuffer textcontent = Utilities.readTextContent(location, "UTF-8", true);
    String[] list = textcontent.toString().split("\n");
    for (String line : list) {
      line = line.trim();
      if (!line.startsWith("#")) {
        String[] splitted = line.split(",");
        if (splitted.length != 5) {
          A4ELogging.debug(MSG_INVALID_LINE, line);
        } else {
          BundleStartRecord record = new BundleStartRecord();
          record.setId(splitted[0]);
          record.setStartLevel(Integer.parseInt(splitted[3]));
          record.setAutoStart(Boolean.parseBoolean(splitted[4]));
          this._records.add(record);
        }
      }
    }

  }

  /**
   * Returns a list of all BundleStartRecords provided by a specific bundles list used by the simple configurator.
   * 
   * @return A list of all BundleStartRecords. Not <code>null</code>.
   */
  public BundleStartRecord[] getBundleStartRecords() {
    BundleStartRecord[] result = new BundleStartRecord[this._records.size()];
    this._records.toArray(result);
    Arrays.sort(result);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[SimpleConfiguratorBundles:");
    buffer.append(" _records {");
    for (int i = 0; i < this._records.size(); i++) {
      buffer.append(" ");
      buffer.append(this._records.get(i));
    }
    buffer.append("}]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = 1;
    for (int i = 0; i < this._records.size(); i++) {
      result = 31 * result + this._records.get(i).hashCode();
    }
    return result;
  }

} /* ENDCLASS */
