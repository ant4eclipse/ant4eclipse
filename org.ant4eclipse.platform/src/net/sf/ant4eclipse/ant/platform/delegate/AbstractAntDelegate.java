/**********************************************************************
 * Copyright (c) 2005-2007 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package net.sf.ant4eclipse.ant.platform.delegate;

import net.sf.ant4eclipse.core.Assert;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

public abstract class AbstractAntDelegate {

  private final ProjectComponent _component;

  public AbstractAntDelegate(final ProjectComponent component) {
    Assert.notNull(component);

    this._component = component;
  }

  protected ProjectComponent getProjectComponent() {
    return this._component;
  }

  protected Project getAntProject() {
    return this._component.getProject();
  }
}
