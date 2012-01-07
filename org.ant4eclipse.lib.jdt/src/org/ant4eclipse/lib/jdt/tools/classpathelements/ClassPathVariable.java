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

import java.io.File;

/**
 * <p>
 * Encapsulates a class path variable. A class path variable can be added to a project's class path. It can be used to
 * define the location of a folder or a JAR file that isn't part of the workspace.
 * </p>
 * <p>
 * This interface is <b>not</b> intended to be implemented by clients.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClassPathVariable extends ClassPathElement {

  /**
   * <p>
   * Returns the path of the class path variable. The returned path is canonical.
   * </p>
   * 
   * @return the path of the class path variable. Not <code>null</code>.
   */
  File getPath();

} /* ENDINTERFACE */
