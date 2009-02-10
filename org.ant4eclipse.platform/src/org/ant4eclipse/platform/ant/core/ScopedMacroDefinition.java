package org.ant4eclipse.platform.ant.core;

import org.ant4eclipse.core.Assert;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <E>
 */
public class ScopedMacroDefinition<E> {

  /** the macro definition */
  private final MacroDef _macroDef;

  /** the scope of the macro definition */
  private final E        _scope;

  /**
   * <p>
   * </p>
   * 
   * @param macroDefinition
   * @param scope
   */
  public ScopedMacroDefinition(final MacroDef macroDefinition, final E scope) {
    Assert.notNull(macroDefinition);
    Assert.notNull(scope);

    this._macroDef = macroDefinition;
    this._scope = scope;
  }

  /**
   * @return
   */
  public MacroDef getMacroDef() {
    return this._macroDef;
  }

  /**
   * @return
   */
  public E getScope() {
    return this._scope;
  }
}
