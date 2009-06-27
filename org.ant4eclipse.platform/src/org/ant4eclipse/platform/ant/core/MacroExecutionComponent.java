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

import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.taskdefs.MacroDef.*;

import java.util.*;

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

  NestedSequential createScopedMacroDefinition(final E scope);

  List<ScopedMacroDefinition<E>> getScopedMacroDefinitions();

  /**
   * @param macroDef
   * @param prefix
   * @param scopedProperties
   * @param scopedReferences
   */
  void executeMacroInstance(final MacroDef macroDef, final MacroExecutionValues macroExecutionValues);
}
