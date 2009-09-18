package org.ant4eclipse.platform.ant.delegate;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseBuildFileTest;
import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

public class MacroExecutionDelegateTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "MacroExecutionDelegateTest.xml";
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
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

    public static int                      counter = 0;

    private MacroExecutionDelegate<String> _macroExecutionDelegate;

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

        this._macroExecutionDelegate.executeMacroInstance(scopedMacroDefinition.getMacroDef(),
            new MacroExecutionValuesProvider() {

              public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
                values.getProperties().put("test", counter + ".test");
                counter++;
                return values;
              }
            });
      }
    }

    public NestedSequential createDynamicElement(String name) throws BuildException {
      return this._macroExecutionDelegate.createScopedMacroDefinition(name);
    }
  }
}
