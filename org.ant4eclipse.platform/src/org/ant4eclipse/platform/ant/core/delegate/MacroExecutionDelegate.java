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
package org.ant4eclipse.platform.ant.core.delegate;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.delegate.helper.AntPropertiesRaper;
import org.ant4eclipse.platform.ant.core.delegate.helper.AntReferencesRaper;
import org.ant4eclipse.platform.ant.core.task.ScopedMacroDefinition;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroInstance;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 */
public class MacroExecutionDelegate<E> extends AbstractAntDelegate implements MacroExecutionComponent<E> {

  private String                               _prefix = null;

  /** list of all macro definitions */
  private final List<ScopedMacroDefinition<E>> _macroDefs;

  /**
   * @param component
   */
  public MacroExecutionDelegate(final Task task, String prefix) {
    super(task);

    this._prefix = prefix;
    this._macroDefs = new LinkedList<ScopedMacroDefinition<E>>();
  }

  /**
   * @param component
   */
  public MacroExecutionDelegate(final Task task) {
    this(task, null);
  }

  /**
   * @return
   */
  public String getPrefix() {
    return this._prefix;
  }

  /**
   * @param prefix
   */
  public void setPrefix(String prefix) {
    this._prefix = prefix;
  }

  /**
   * @return
   */
  public List<ScopedMacroDefinition<E>> getScopedMacroDefinitions() {
    return this._macroDefs;
  }

  /**
   * @param scope
   * @return
   */
  public NestedSequential createScopedMacroDefinition(final E scope) {
    final MacroDef macroDef = new MacroDef();
    macroDef.setProject(getAntProject());
    this._macroDefs.add(new ScopedMacroDefinition<E>(macroDef, scope));
    return macroDef.createSequential();
  }

  /**
   * @param macroDef
   * @param prefix
   * @param scopedProperties
   * @param scopedReferences
   */
  public void executeMacroInstance(final MacroDef macroDef, final MacroExecutionValues macroExecutionValues) {
    Assert.notNull(macroDef);
    Assert.notNull(macroExecutionValues);

    // create MacroInstance
    final MacroInstance instance = new MacroInstance();
    instance.setProject(getAntProject());
    instance.setOwningTarget(((Task) getProjectComponent()).getOwningTarget());
    instance.setMacroDef(macroDef);

    AntPropertiesRaper antPropertiesRaper = new AntPropertiesRaper(getAntProject());
    AntReferencesRaper antReferencesRaper = new AntReferencesRaper(getAntProject());

    antPropertiesRaper.setScopedValues(macroExecutionValues.getProperties(), this._prefix);
    antReferencesRaper.setScopedValues(macroExecutionValues.getReferences(), this._prefix);

    instance.execute();

    // unset scoped values
    antPropertiesRaper.unsetScopedValues();
    antReferencesRaper.unsetScopedValues();
  }
}
