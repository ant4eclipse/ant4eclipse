package org.ant4eclipse.core.ant;

import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.core.ant.delegate.MacroExecutionDelegate;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;

public class MacroExecutionDelegateTest extends BuildFileTest {

  public void setUp() {
    configureProject("src/org/ant4eclipse/core/ant/MacroExecutionDelegateTest.xml");
  }

  public void testMacroExecute() {
    expectLog("testMacroExecute", "${hurz.test}test${hurz.test}test${hurz.test}");
  }

  public static class MacroExecuteTask extends AbstractAnt4EclipseTask implements DynamicElement {

    private MacroExecutionDelegate _macroExecutionDelegate;

    private Map<String, MacroDef>  _macroDefs;

    public MacroExecuteTask() {
      _macroExecutionDelegate = new MacroExecutionDelegate(this);
      _macroDefs = new HashMap<String, MacroDef>();
    }

    @Override
    protected void doExecute() {
      for (MacroDef macroDef : _macroDefs.values()) {
        Map<String, String> scopedProperties = new HashMap<String, String>();
        scopedProperties.put("test", "test");
        _macroExecutionDelegate.executeMacroInstance(macroDef, "hurz", scopedProperties);
      }
    }

    public Object createDynamicElement(String name) throws BuildException {
      MacroDef macroDef = this._macroExecutionDelegate.createMacroDef();
      _macroDefs.put(name, macroDef);
      return macroDef.createSequential();
    }
  }
}
