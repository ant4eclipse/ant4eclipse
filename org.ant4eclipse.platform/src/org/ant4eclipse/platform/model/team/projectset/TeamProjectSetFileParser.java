/**********************************************************************
 * Copyright (c) 2005-2006 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.model.team.projectset;

import java.io.File;

import org.ant4eclipse.core.service.ServiceRegistry;


public interface TeamProjectSetFileParser {

  public TeamProjectSet parseTeamProjectSetFile(File psfFile);

  public static class Helper {
    public static TeamProjectSetFileParser getInstance() {
      return (TeamProjectSetFileParser) ServiceRegistry.instance().getService(TeamProjectSetFileParser.class.getName());
    }
  }

}
