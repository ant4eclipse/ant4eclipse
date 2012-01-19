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
package org.ant4eclipse.ant.jdt.containerargs;

import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;
import org.apache.tools.ant.DynamicAttribute;

import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface JdtClasspathContainerArgumentComponent extends DynamicAttribute {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments();
  
} /* ENDINTERFACE */
