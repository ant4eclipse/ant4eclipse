package org.ant4eclipse.platform.ant.core.task;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <E>
 */
public abstract class AbstractExecuteProjectTask<E extends Enum<E>> extends AbstractProjectPathTask {

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate         _macroExecutionDelegate;

  /** list of all macro definitions */
  private final List<ScopedMacroDefinition<E>> _macroDefs;

  /**
   * 
   */
  public AbstractExecuteProjectTask() {

    this._macroExecutionDelegate = new MacroExecutionDelegate(this, "executeJdtProject");
    this._macroDefs = new LinkedList<ScopedMacroDefinition<E>>();
  }

  /**
   * @return
   */
  public List<ScopedMacroDefinition<E>> getScopedMacroDefinitions() {
    return this._macroDefs;
  }

  /**
   * @param macroDef
   * @param macroExecutionValues
   */
  public void executeMacroInstance(final MacroDef macroDef, final MacroExecutionValues macroExecutionValues) {
    this._macroExecutionDelegate.executeMacroInstance(macroDef, macroExecutionValues);
  }

  /**
   * @param scope
   * @return
   */
  protected Object createMacroDef(final E scope) {
    final MacroDef macroDef = this._macroExecutionDelegate.createMacroDef();
    this._macroDefs.add(new ScopedMacroDefinition<E>(macroDef, scope));
    return macroDef.createSequential();
  }
}
