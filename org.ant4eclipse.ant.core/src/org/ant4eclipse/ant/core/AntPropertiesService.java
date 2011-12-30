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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.lib.core.util.SystemPropertiesService;
import org.ant4eclipse.lib.core.util.Utilities;
import org.apache.tools.ant.Project;

/**
 * Uses an org.apache.tools.ant.Project as the property source, and delegates to java.lang.System as a secondary source.
 */
public class AntPropertiesService extends SystemPropertiesService implements AntService {

  private Project _project;

  @Override
  public void configure(Project project) {
    this._project = project;
  }

  @Override
  public String getProperty(String propertyName) {
    String value = getAntProperty(propertyName);
    return value != null ? value : super.getProperty(propertyName);
  }

  protected String getAntProperty(String propertyName) {
    return this._project != null ? Utilities.cleanup(this._project.getProperty(propertyName)) : null;
  }
}
