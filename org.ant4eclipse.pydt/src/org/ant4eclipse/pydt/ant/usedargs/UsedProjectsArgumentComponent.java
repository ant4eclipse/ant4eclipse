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
package org.ant4eclipse.pydt.ant.usedargs;

import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Subelement representation used as an python specific argument for the GetUsedProjectsTask.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class UsedProjectsArgumentComponent {

  public static final UsedProjectsArgumentComponent DEFAULT     = new UsedProjectsArgumentComponent();

  public static final String                        ELEMENTNAME = "pydtReferencedProject";

  /** resolve projects that are declared as exported (thus their dependencies) */
  private boolean                                   _export     = false;

  /** generally resolve all referenced projects */
  private boolean                                   _all        = true;

  /** do not resolve dependencies of referenced projects (even if they're declared as exported) */
  private boolean                                   _direct     = false;

  /**
   * Only return directly referenced projects.
   * 
   * @return <code>true</code> <=> Directly returned projects.
   */
  public boolean isDirect() {
    return _direct;
  }

  /**
   * Returns <code>true</code> if projects generally shall be resolved.
   * 
   * @return <code>true</code> <=> Projects shall be resolved in general.
   */
  public boolean isAll() {
    return _all;
  }

  /**
   * Returns <code>true</code> if exported projects shall be resolved, too.
   * 
   * @return <code>true</code> <=> Resolve exported projects, too.
   */
  public boolean isExport() {
    return _export;
  }

  /**
   * Changes the current resolving mode.
   * 
   * @param mode
   *          The new resolving mode.
   */
  public void setMode(final Mode mode) {
    _all = false;
    _export = false;
    _direct = false;
    if (mode != null) {
      if (mode.isExported()) {
        _export = true;
      } else if (mode.isDirect()) {
        _direct = true;
      } else /* if (mode.isAll()) */{
        _all = true;
      }
    } else {
      // the default is 'all'
      _all = true;
    }
  }

  /**
   * EnumeratedAttribute used to control the resolving process.
   */
  public static final class Mode extends EnumeratedAttribute {

    private static final String   NAME_ALL      = "all";

    private static final String   NAME_EXPORTED = "exported";

    private static final String   NAME_DIRECT   = "direct";

    private static final String[] VALUES        = { NAME_ALL, NAME_EXPORTED, NAME_DIRECT };

    /**
     * {@inheritDoc}.
     */
    public String[] getValues() {
      return VALUES;
    }

    /**
     * Returns <code>true</code> if the supplied value is mode <code>all</code>.
     * 
     * @return <code>true</code> <=> The supplied value is mode <code>all</code>.
     */
    public boolean isAll() {
      return NAME_ALL.equalsIgnoreCase(getValue());
    }

    /**
     * Returns <code>true</code> if the supplied value is mode <code>exported</code>.
     * 
     * @return <code>true</code> <=> The supplied value is mode <code>exported</code>.
     */
    public boolean isExported() {
      return NAME_EXPORTED.equalsIgnoreCase(getValue());
    }

    /**
     * Returns <code>true</code> if the supplied value is mode <code>exported</code>.
     * 
     * @return <code>true</code> <=> The supplied value is mode <code>exported</code>.
     */
    public boolean isDirect() {
      return NAME_DIRECT.equalsIgnoreCase(getValue());
    }

  } /* ENDCLASS */

} /* ENDCLASS */
