/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.model.platform.team.cvssupport.project;

import org.ant4eclipse.model.platform.resource.EclipseProject;
import org.ant4eclipse.model.platform.resource.role.ProjectRole;
import org.ant4eclipse.model.platform.resource.role.ProjectRoleIdentifier;
import org.ant4eclipse.model.platform.team.cvssupport.CvsRoot;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.logging.A4ELogging;

/**
 * Identifier for a CVS role.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CvsRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * {@inheritDoc}
   */
  public boolean isRoleSupported(final EclipseProject project) {
    return (CvsParser.isCvsProject(project));
  }

  /**
   * {@inheritDoc}
   */
  public ProjectRole createRole(final EclipseProject project) {
    A4ELogging.trace("CvsRoleIdentifier.applyRole(%s)", project);
    Assert.notNull(project);
    final CvsRoot cvsRoot = CvsParser.readCvsRoot(project);
    final String repositoryName = CvsParser.readCvsRepositoryName(project);
    final String tag = CvsParser.readTag(project);
    final CvsProjectRole cvsProjectRole = new CvsProjectRole(project, repositoryName, cvsRoot, tag);
    return cvsProjectRole;
  }

} /* ENDCLASS */
