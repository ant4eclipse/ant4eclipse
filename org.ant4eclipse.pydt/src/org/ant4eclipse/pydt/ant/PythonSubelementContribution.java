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
package org.ant4eclipse.pydt.ant;

import org.ant4eclipse.platform.ant.SubElementContribution;

import org.ant4eclipse.pydt.ant.usedargs.UsedProjectsArgumentComponent;
import org.apache.tools.ant.ProjectComponent;

/**
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonSubelementContribution implements SubElementContribution {

  /** @todo [02-Aug-2009:KASI] Why do we only get lowercase names ? */
  private static final String NAME_REFERENCEDPROJECT = "pydtReferencedProject".toLowerCase();

  /**
   * {@inheritDoc}
   */
  public boolean canHandleSubElement(String name, ProjectComponent component) {
    return NAME_REFERENCEDPROJECT.equals(name);
  }

  /**
   * {@inheritDoc}
   */
  public Object createSubElement(String name, ProjectComponent component) {
    if (NAME_REFERENCEDPROJECT.equals(name)) {
      return new UsedProjectsArgumentComponent();
    } else {
      return null;
    }
  }

} /* ENDCLASS */
