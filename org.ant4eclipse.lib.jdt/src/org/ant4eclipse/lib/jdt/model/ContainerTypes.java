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
package org.ant4eclipse.lib.jdt.model;

/**
 * Definition of container types currently supported by Ant4Eclipse. A container type specifies a classpath bundle that
 * is used by an Eclipse project.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface ContainerTypes {

  /** Reference to JRE specific classpathes. */
  String JRE_CONTAINER = "org.eclipse.jdt.launching.JRE_CONTAINER";

  /** VMTYPE_PREFIX */
  String VMTYPE_PREFIX = ContainerTypes.JRE_CONTAINER + "/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/";

} /* ENDINTERFACE */
