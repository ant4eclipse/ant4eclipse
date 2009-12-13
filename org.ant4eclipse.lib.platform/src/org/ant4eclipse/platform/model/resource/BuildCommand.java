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
package org.ant4eclipse.platform.model.resource;

/**
 * <p>
 * A instance of type {@link BuildCommand} represents a build command associated with an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface BuildCommand {

  /**
   * <p>
   * Returns the name the build command.
   * </p>
   * 
   * @return the name of the build command.
   */
  String getName();

  /**
   * <p>
   * Return the triggers of this build command (comma separated),e.g. <code>full,incremental</code>.
   * </p>
   * 
   * @return the triggers as a comma-separated list
   */
  String getTriggers();

  /**
   * <p>
   * Returns whether this builder has triggers set.
   * </p>
   * 
   * @return <code>true</code> if builder has triggers set.
   */
  boolean hasTriggers();
} /* ENDCLASS */
