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
package org.ant4eclipse.platform.ant.core.delegate;

import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.core.ldapfilter.LdapFilter;
import org.ant4eclipse.core.ldapfilter.ParseException;

import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.helper.AntPropertiesRaper;
import org.ant4eclipse.platform.ant.core.delegate.helper.AntReferencesRaper;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.StringMap;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroInstance;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that execute macros.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <E>
 *          the scope
 */
public class MacroExecutionDelegate<E> extends AbstractAntDelegate implements MacroExecutionComponent<E> {

  /** the prefix for all scoped properties and references */
  private String                         _prefix = null;

  /** list of all macro definitions */
  private List<ScopedMacroDefinition<E>> _macroDefs;

  /**
   * <p>
   * Creates a new instance of type {@link MacroExecutionDelegate}.
   * </p>
   * 
   * @param task
   *          the task
   * @param prefix
   *          the prefix for all scoped properties and references
   */
  public MacroExecutionDelegate(Task task, String prefix) {
    super(task);

    this._prefix = prefix;
    this._macroDefs = new LinkedList<ScopedMacroDefinition<E>>();
  }

  /**
   * <p>
   * Creates a new instance of type {@link MacroExecutionDelegate}.
   * </p>
   * 
   * @param task
   *          the task
   */
  public MacroExecutionDelegate(Task task) {
    this(task, null);
  }

  /**
   * {@inheritDoc}
   */
  public String getPrefix() {
    return this._prefix;
  }

  /**
   * {@inheritDoc}
   */
  public void setPrefix(String prefix) {
    this._prefix = prefix;
  }

  /**
   * {@inheritDoc}
   */
  public List<ScopedMacroDefinition<E>> getScopedMacroDefinitions() {
    return this._macroDefs;
  }

  /**
   * {@inheritDoc}
   */
  public NestedSequential createScopedMacroDefinition(E scope) {
    MacroDef macroDef = new ConditionalMacroDef();
    macroDef.setProject(getAntProject());
    this._macroDefs.add(new ScopedMacroDefinition<E>(macroDef, scope));
    return macroDef.createSequential();
  }

  /**
   * {@inheritDoc}
   */
  public void executeMacroInstance(MacroDef macroDef, MacroExecutionValuesProvider provider) {
    Assure.notNull(macroDef);
    Assure.notNull(provider);

    if (macroDef instanceof ConditionalMacroDef) {
      ConditionalMacroDef conditionalMacroDef = (ConditionalMacroDef) macroDef;

      if (!conditionalMacroDef.isIf() || conditionalMacroDef.isUnless()) {
        return;
      }
    }

    MacroExecutionValues values = new MacroExecutionValues();

    executeMacroInstance(macroDef, provider.provideMacroExecutionValues(values));
  }

  /**
   * {@inheritDoc}
   */
  private void executeMacroInstance(MacroDef macroDef, MacroExecutionValues macroExecutionValues) {
    Assure.notNull(macroDef);
    Assure.notNull(macroExecutionValues);

    if (macroDef instanceof ConditionalMacroDef) {
      ConditionalMacroDef conditionalMacroDef = (ConditionalMacroDef) macroDef;

      String filter = conditionalMacroDef.getFilter();
      if (filter != null) {
        StringMap properties = macroExecutionValues.getProperties(getPrefix());
        try {
          if (!(new LdapFilter(properties, new StringReader(filter)).validate())) {
            return;
          }
        } catch (ParseException e) {
          throw new BuildException(e.getMessage(), e);
        }
      }
    }

    // create MacroInstance
    MacroInstance instance = new MacroInstance();
    instance.setProject(getAntProject());
    instance.setOwningTarget(((Task) getProjectComponent()).getOwningTarget());
    instance.setMacroDef(macroDef);

    // create raper
    AntPropertiesRaper antPropertiesRaper = new AntPropertiesRaper(getAntProject());
    AntReferencesRaper antReferencesRaper = new AntReferencesRaper(getAntProject());

    // set scoped values
    antPropertiesRaper.setScopedValues(macroExecutionValues.getProperties(), this._prefix);
    antReferencesRaper.setScopedValues(macroExecutionValues.getReferences(), this._prefix);

    // execute macro instance
    instance.execute();

    // unset scoped values
    antPropertiesRaper.unsetScopedValues();
    antReferencesRaper.unsetScopedValues();
  }
}
