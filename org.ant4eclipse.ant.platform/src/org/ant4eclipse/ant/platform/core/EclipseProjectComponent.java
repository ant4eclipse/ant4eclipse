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
package org.ant4eclipse.ant.platform.core;


import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * Interface for all ant4eclipse tasks, conditions and types that require a eclipse project. As this component is a
 * subclass of {@link WorkspaceComponent}, so the {@link EclipseProjectComponent} requires that a valid
 * {@link Workspace} is set.
 * </p>
 * 
 * <p>
 * E.g. if an ant task implements this interface, you are able to set a workspace and a project name on this component,
 * e.g.:
 * 
 * <pre>
 * &lt;code&gt;
 * &lt;myTask workspace=&quot;c:/dev/workspace&quot; projectName=&quot;myProject&quot; /&gt;
 * &lt;/code&gt;
 * </pre>
 * 
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface EclipseProjectComponent extends WorkspaceComponent {

  /**
   * <p>
   * Sets the project name.
   * </p>
   * 
   * @param projectName
   *          the project name.
   */
  void setProjectName( String projectName );

  /**
   * <p>
   * Returns <code>true</code> if the project name has been set.
   * </p>
   * 
   * @return <code>true</code> if the project name has been set.
   */
  boolean isProjectNameSet();

  /**
   * <p>
   * Throws an {@link BuildException} if the workspace directory or the project name is not set.
   * </p>
   */
  void requireWorkspaceAndProjectNameSet();

  /**
   * <p>
   * Returns the associated eclipse project.
   * </p>
   * 
   * @return the associated eclipse project.
   * 
   * @throws BuildException
   *           if the eclipse project with the given name does not exist in the given workspace.
   */
  EclipseProject getEclipseProject();

  /**
   * <p>
   * Ensures that the associated project has the specified project role.
   * </p>
   * 
   * @param projectRoleClass
   *          the project role class
   */
  void ensureRole( Class<? extends ProjectRole> projectRoleClass );
  
} /* ENDINTERFACE */
