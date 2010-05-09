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


import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

import java.util.List;

/**
 */
public interface MacroExecutionComponent<E> {

  /**
   * @return
   */
  String getPrefix();

  /**
   * @param prefix
   */
  void setPrefix(String prefix);

  NestedSequential createScopedMacroDefinition(E scope);

  List<ScopedMacroDefinition<E>> getScopedMacroDefinitions();

  // /**
  // * @param macroDef
  // * @param prefix
  // * @param scopedProperties
  // * @param scopedReferences
  // */
  // void executeMacroInstance(MacroDef macroDef, MacroExecutionValues macroExecutionValues);

  void executeMacroInstance(MacroDef macroDef, MacroExecutionValuesProvider provider);
}
