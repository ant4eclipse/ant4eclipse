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
package org.ant4eclipse.lib.jdt.model.userlibrary;

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

import java.io.File;

/**
 * <p>
 * Parsing class used to process an eclipse user library configuration file.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface UserLibrariesFileParser extends A4EService {

  /**
   * Parses the supplied eclipse user library configuration file.
   * 
   * @param configuration
   *          The eclipse user library configuration file.
   * @param workspace
   *          The workspace definition that is used to resolve relative paths or null
   */
  UserLibraries parseUserLibrariesFile(File configuration, Workspace workspace);

} /* ENDCLASS */
