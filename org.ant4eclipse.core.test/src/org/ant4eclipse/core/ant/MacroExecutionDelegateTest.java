package org.ant4eclipse.core.ant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.ant.delegate.MacroExecutionDelegate;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.taskdefs.MacroDef;

public class MacroExecutionDelegateTest extends BuildFileTest {

  public void setUp() {
    configureProject("src/org/ant4eclipse/core/ant/MacroExecutionDelegateTest.xml");
  }

  public void testMacroExecute() {
    expectLog("testMacroExecute", "${hurz.test}test${hurz.test}test${hurz.test}");
  }

  public static class MacroExecuteTask extends AbstractAnt4EclipseTask {

    private MacroExecutionDelegate _macroExecutionDelegate;

    private List<MacroDef>         _macroDefs;

    public MacroExecuteTask() {
      _macroExecutionDelegate = new MacroExecutionDelegate(this);
      _macroDefs = new LinkedList<MacroDef>();
    }

    @Override
    protected void doExecute() {

      for (MacroDef macroDef : _macroDefs) {
        Map<String, String> scopedProperties = new HashMap<String, String>();
        scopedProperties.put("test", "test");
        _macroExecutionDelegate.executeMacroInstance(macroDef, "hurz", scopedProperties);
      }

    }

    /**
     * @return
     */
    public Object createTestSequential() {
      MacroDef macroDef = this._macroExecutionDelegate.createMacroDef();
      _macroDefs.add(macroDef);
      return macroDef.createSequential();
    }
  }
}
