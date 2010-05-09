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
package org.ant4eclipse.lib.platform.model.launcher;

import java.util.Collection;

/**
 * Represents the content of a Launch Configuration file
 * 
 * @author Nils Hartmann
 * 
 */
public interface LaunchConfiguration {

  /**
   * Returns the type of this launch configuration as specified in the 'type' attribute of the 'launchConfiguration'
   * element (e.g. 'launchConfiguration')
   * 
   * @return The type identifier of this launch configuration. Never null.
   */
  String getType();

  /**
   * Returns a {@link Collection} of all attributes that are present in this Launch Configuration
   * 
   * @return all attributes that in this Launch Configuration. Never null.
   */
  Collection<String> getAttributeNames();

  /**
   * Returns the specified attribute as a boolean value.
   * 
   * <p>
   * The string representation of the attribute is converted to a boolean using {@link Boolean#parseBoolean(String)}.
   * 
   * @param attributeName
   *          The name of the attribute that should be returned
   * @return The attribute value as boolean ('false' if the attribute is not present)
   */
  boolean getBooleanAttribute(String attributeName);

  /**
   * Returns the string value of the specified attribute from this launch configuration. <tt>null</tt> is returned, when
   * the specified attribute is not present in the launch configuration
   * 
   * @param attributeName
   * @return
   */
  String getAttribute(String attributeName);

  /**
   * Returns all list attributes with the given name
   * 
   * @param attributeName
   *          All values of the given list attribute. Null if the attribute is not a list or if there is no such
   *          attribute.
   * @return
   */
  String[] getListAttribute(String attributeName);

}
