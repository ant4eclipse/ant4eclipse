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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * <p>
 * Helper class to echo an ant reference. This task is for debugging purpose only.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EchoReference extends Task {

  /** the refid */
  private String _refId = null;

  /**
   * <p>
   * Sets the reference id.
   * </p>
   * 
   * @param refId
   *          the refId to set
   */
  public void setRefId(String refId) {
    this._refId = refId;
  }

  /**
   * <p>
   * Returns the reference id.
   * </p>
   * 
   * @return the refId
   */
  public String getRefId() {
    return this._refId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws BuildException {
    if (this._refId != null) {
      Object reference = getProject().getReference(this._refId);
      A4ELogging.debug("Reference '%s': %s", this._refId, reference != null ? reference.toString() : "null");
    }
  }
}
