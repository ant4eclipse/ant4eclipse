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
package org.ant4eclipse.ant.platform;

import org.apache.tools.ant.ProjectComponent;

/**
 * <p>
 * A {@link SubAttributeContribution} can be used to extend tasks defined in lower layer (e.g. the platform layer) with
 * specific sub attributes defined in higher layers (e.g. the JDT layer). To provide a {@link SubAttributeContribution}
 * one has to implement this interface and a register the implemented class in the
 * <code>org/ant4eclipse/ant4eclipse-configuration.properties</code> file of the implementing project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface SubAttributeContribution {

  /**
   * <p>
   * Must return <code>true</code> if this {@link SubAttributeContribution} can handle the sub attibute with the given
   * name for the given {@link ProjectComponent}.
   * </p>
   * 
   * @param name
   *          The name of the attribute. Will always be lowercase. Neither <code>null</code> nor empty.
   * @param component
   * @return
   */
  boolean canHandleSubAttribute(String name, ProjectComponent component);
}
