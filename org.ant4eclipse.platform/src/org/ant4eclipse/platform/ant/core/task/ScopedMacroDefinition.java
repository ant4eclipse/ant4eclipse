package org.ant4eclipse.platform.ant.core.task;

import org.apache.tools.ant.taskdefs.MacroDef;

public class ScopedMacroDefinition<E extends Enum<E>> {

  /** the macro definition */
  private final MacroDef _macroDef;

  /** the scope of the macro definition */
  private final E        _scope;

  /**
   * @param def
   * @param scope
   */
  public ScopedMacroDefinition(final MacroDef def, final E scope) {
    this._macroDef = def;
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
