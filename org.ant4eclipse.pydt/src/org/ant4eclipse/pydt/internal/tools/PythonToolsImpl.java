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
package org.ant4eclipse.pydt.internal.tools;

import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pydt.tools.PythonTools;

import java.io.File;

/**
 * Implementation used to provide python specific tools.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonToolsImpl implements PythonTools, Lifecycle {

  private File    _epydoc      = null;

  private boolean _initialised = false;

  /**
   * {@inheritDoc}
   */
  public File getEpydocInstallation() {
    return _epydoc;
  }

  /**
   * {@inheritDoc}
   */
  public void dispose() {
    Utilities.delete(_epydoc);
    _epydoc = null;
    _initialised = false;
  }

  /**
   * {@inheritDoc}
   */
  public void initialize() {
    final File zip = Utilities.exportResource("/org/ant4eclipse/pydt/epydoc.zip");
    _epydoc = new File(zip.getParentFile(), "epydoc");
    Utilities.unpack(zip, _epydoc);
    _initialised = true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return _initialised;
  }

} /* ENDCLASS */
