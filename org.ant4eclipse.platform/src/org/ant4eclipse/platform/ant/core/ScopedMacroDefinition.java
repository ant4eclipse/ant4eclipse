package org.ant4eclipse.platform.ant.core;

import org.ant4eclipse.core.Assert;

import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * <p>
 * Associates a ant {@link MacroDef} with a scope.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <E>
 *          the scope type
 */
public class ScopedMacroDefinition<E> {

  /** the macro definition */
  private MacroDef _macroDef;

  /** the scope of the macro definition */
  private E        _scope;

  /**
   * <p>
   * Creates a new instance of type {@link ScopedMacroDefinition}.
   * </p>
   * 
   * @param macroDefinition
   * @param scope
   */
  public ScopedMacroDefinition(MacroDef macroDefinition, E scope) {
    Assert.notNull(macroDefinition);
    Assert.notNull(scope);

    this._macroDef = macroDefinition;
    this._scope = scope;
  }

  /**
   * <p>
   * Returns the macro definition.
   * </p>
   * 
   * @return the macro definition.
   */
  public MacroDef getMacroDef() {
    return this._macroDef;
  }

  /**
   * <p>
   * Returns the scope.
   * </p>
   * 
   * @return the scope.
   */
  public E getScope() {
    return this._scope;
  }
}
