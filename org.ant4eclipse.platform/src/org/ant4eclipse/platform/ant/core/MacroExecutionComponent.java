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
package org.ant4eclipse.platform.ant.core;

import java.util.List;

import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 */
public interface MacroExecutionComponent<E> {

  /**
   * @return
   */
  public String getPrefix();

  /**
   * @param prefix
   */
  public void setPrefix(String prefix);

  public NestedSequential createScopedMacroDefinition(final E scope);

  public List<ScopedMacroDefinition<E>> getScopedMacroDefinitions();

  /**
   * @param macroDef
   * @param prefix
   * @param scopedProperties
   * @param scopedReferences
   */
  public void executeMacroInstance(final MacroDef macroDef, final MacroExecutionValues macroExecutionValues);
}
