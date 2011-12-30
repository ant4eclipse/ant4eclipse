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
package org.ant4eclipse.lib.pydt.internal.tools;

import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pydt.tools.PythonTools;

import java.io.File;

/**
 * Implementation used to provide python specific tools.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonToolsImpl implements PythonTools, Lifecycle {

  private File    _epydoc      = null;

  /**
   * {@inheritDoc}
   */
  @Override
  public File getEpydocInstallation() {
    if (this._epydoc == null) {
      try {
        File zip = Utilities.exportResource("/org/ant4eclipse/lib/pydt/epydoc.zip");
        this._epydoc = new File(zip.getParentFile(), "epydoc");
        Utilities.unpack(zip, this._epydoc);
      } catch (Ant4EclipseException ex) {
        // if unpacking failed we consider the python doc feature unavailable
        if (ex.getExceptionCode() != CoreExceptionCode.UNPACKING_FAILED) {
          throw ex;
        }
      }
    }
    return this._epydoc;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    if (this._epydoc != null) {
      Utilities.delete(this._epydoc);
    }
    this._epydoc = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize() {
  }

} /* ENDCLASS */
