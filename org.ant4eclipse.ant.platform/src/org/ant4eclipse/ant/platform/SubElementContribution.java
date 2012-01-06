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

import org.ant4eclipse.lib.core.A4EService;
import org.apache.tools.ant.ProjectComponent;

/**
 * <p>
 * A {@link SubElementContribution} can be used to extend tasks defined in lower layer (e.g. the platform layer) with
 * specific sub elements defined in higher layers (e.g. the JDT layer). To provide a {@link SubElementContribution} one
 * has to implement this interface and a register the implemented class in the
 * <code>org/ant4eclipse/ant4eclipse-configuration.properties</code> file of the implementing project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public interface SubElementContribution extends A4EService {

  /**
   * <p>
   * Must return <code>true</code> if this {@link SubElementContribution} can handle the sub element with the given name
   * for the given {@link ProjectComponent}.
   * </p>
   * 
   * @param name
   *          The name of the subelement. Will always be lowercase. Neither <code>null</code> nor empty.
   * @param component
   * @return
   */
  boolean canHandleSubElement(String name, ProjectComponent component);

  /**
   * <p>
   * Creates an element with the given name.
   * </p>
   * 
   * @param name
   *          The name of the subelement. Will always be lowercase. Neither <code>null</code> nor empty.
   * @return the element created
   */
  Object createSubElement(String name, ProjectComponent component);
}
