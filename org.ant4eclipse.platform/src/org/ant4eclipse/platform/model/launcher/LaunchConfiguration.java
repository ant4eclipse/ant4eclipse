/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.model.launcher;

import java.util.Collection;

public interface LaunchConfiguration {

  public String getType();

  public Collection<String> getAttributeNames();

  public boolean getBooleanAttribute(String attributeName);

  public String getAttribute(String attributeName);

  public String[] getListAttribute(String attributeName);

}
