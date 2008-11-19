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
package org.ant4eclipse.jdt.model.project;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.model.project.internal.ClasspathFileParser;
import org.ant4eclipse.jdt.model.project.internal.JavaProjectRoleImpl;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;

/**
 * <p>
 * {@link ProjectRoleIdentifier} for the java project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class JavaRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * <p>
   * Returns <code>true</code> is the given project has the nature <code>"org.eclipse.jdt.core.javanature"</code>.
   * </p>
   */
  public boolean isRoleSupported(final EclipseProject project) {
    return (project.hasNature(JavaProjectRole.JAVA_NATURE));
  }

  /**
   * <p>
   * Adds a {@link JavaProjectRole} to the given project and parses the class path.
   * </p>
   */
  public ProjectRole createRole(final EclipseProject project) {
    A4ELogging.trace("JavaRoleIdentifier.applyRole(%s)", project);
    Assert.notNull(project);
    final JavaProjectRoleImpl javaProjectRole = new JavaProjectRoleImpl(project);
    ClasspathFileParser.parseClasspath(javaProjectRole);
    return javaProjectRole;
  }
} /* ENDCLASS */
