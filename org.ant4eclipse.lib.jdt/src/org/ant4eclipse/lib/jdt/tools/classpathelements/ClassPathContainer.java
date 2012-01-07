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
package org.ant4eclipse.lib.jdt.tools.classpathelements;

import org.ant4eclipse.lib.jdt.tools.container.ClasspathContainerResolver;

import java.io.File;
import java.util.List;

/**
 * <p>
 * Encapsulates a class path container. A class path container groups several jar files and /or folders that belongs
 * together. One can create a {@link ClassPathContainer ClassPathContainers} and register it with the
 * {@link ClassPathElementsRegistry} to statically define the content of an eclipse class path container.
 * </p>
 * <p>
 * <b>Note:</b> Registering class path containers manually should be used as the last resort to define class path
 * containers. Take a look at the {@link ClasspathContainerResolver} interface for more information about how to
 * implement 'dynamic' container resolver.
 * </p>
 * <p>
 * This interface is <b>not</b> intended to be implemented by clients.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClassPathContainer extends ClassPathElement {

  /**
   * <p>
   * Returns the files that belongs to the class path container.
   * </p>
   * 
   * @return the files that belongs to the class path container.
   */
  List<File> getPathEntries();
  
} /* ENDINTERFACE */
