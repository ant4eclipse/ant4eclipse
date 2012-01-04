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
package org.ant4eclipse.lib.jdt.model.project;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.internal.model.project.ClasspathFileParser;
import org.ant4eclipse.lib.jdt.internal.model.project.JavaProjectRoleImpl;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRoleIdentifier;

import java.util.Set;

/**
 * <p>
 * {@link ProjectRoleIdentifier} for the java project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class JavaRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRoleSupported(EclipseProject project) {
    return project.hasNature(JavaProjectRole.JAVA_NATURE);
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Adds a {@link JavaProjectRole} to the given project and parses the class path.
   * </p>
   */
  @Override
  public ProjectRole createRole(EclipseProject project) {
    A4ELogging.trace("JavaRoleIdentifier.applyRole(%s)", project);
    Assure.notNull("project", project);
    JavaProjectRoleImpl javaProjectRole = new JavaProjectRoleImpl(project);
    ClasspathFileParser.parseClasspath(javaProjectRole);
    return javaProjectRole;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postProcess(EclipseProject project) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ProjectNature> getNatures() {
    return ProjectNature.createNatures( JavaProjectRole.JAVA_NATURE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getNatureNicknames() {
    return new String[] { "java" };
  }
  
} /* ENDCLASS */
