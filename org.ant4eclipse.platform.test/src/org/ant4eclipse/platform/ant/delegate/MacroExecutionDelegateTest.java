package org.ant4eclipse.platform.ant.delegate;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

public class MacroExecutionDelegateTest extends BuildFileTest {

  @Override
  public void setUp() {
    configureProject("src/org/ant4eclipse/platform/ant/delegate/MacroExecutionDelegateTest.xml");
    getProject().setProperty("hurz.test", "initial");
  }

  public void testMacroExecute() {
    MacroExecuteTask.counter = 0;
    expectLog("testMacroExecute", "initial!0.test!1.test!0.test!initial!2.test!initial");
  }

  /**
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class MacroExecuteTask extends AbstractAnt4EclipseTask implements DynamicElement {

    public static int                            counter = 0;

    private final MacroExecutionDelegate<String> _macroExecutionDelegate;

    public MacroExecuteTask() {
      this._macroExecutionDelegate = new MacroExecutionDelegate<String>(this, "hurz");
    }

    /**
     * @param prefix
     */
    public void setPrefix(String prefix) {
      this._macroExecutionDelegate.setPrefix(prefix);
    }

    @Override
    protected void doExecute() {
      for (ScopedMacroDefinition<String> scopedMacroDefinition : this._macroExecutionDelegate
          .getScopedMacroDefinitions()) {

        MacroExecutionValues values = new MacroExecutionValues();
        values.getProperties().put("test", counter + ".test");
        counter++;
        this._macroExecutionDelegate.executeMacroInstance(scopedMacroDefinition.getMacroDef(), values);
      }
    }

    public NestedSequential createDynamicElement(String name) throws BuildException {
      return this._macroExecutionDelegate.createScopedMacroDefinition(name);
    }
  }
}
