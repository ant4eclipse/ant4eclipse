package org.ant4eclipse.platform.ant.delegate;

import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;

public class MacroExecutionDelegateTest extends BuildFileTest {

  @Override
  public void setUp() {
    configureProject("src/org/ant4eclipse/platform/ant/delegate/MacroExecutionDelegateTest.xml");
    getProject().setProperty("hurz.test", "initial");
  }

  public void testMacroExecute() {
    expectLog("testMacroExecute", "initialtest1.testtest2.testtest1.testinitialtest3.testinitial");
  }

  public static class MacroExecuteTask extends AbstractAnt4EclipseTask implements DynamicElement {

    private final MacroExecutionDelegate _macroExecutionDelegate;

    private final Map<String, MacroDef>  _macroDefs;

    private String                       _prefix;

    public MacroExecuteTask() {
      this._macroExecutionDelegate = new MacroExecutionDelegate(this, "hurz");
      this._macroDefs = new HashMap<String, MacroDef>();
    }

    /**
     * @param prefix
     */
    public void setPrefix(String prefix) {
      this._prefix = prefix;
    }

    @Override
    protected void doExecute() {
      for (MacroDef macroDef : this._macroDefs.values()) {

        MacroExecutionValues values = new MacroExecutionValues();
        values.getProperties().put("test", this._prefix + ".test");
        values.getReferences().put("test", this._prefix + ".reference");
        this._macroExecutionDelegate.executeMacroInstance(macroDef, values);
      }
    }

    public Object createDynamicElement(String name) throws BuildException {
      MacroDef macroDef = this._macroExecutionDelegate.createMacroDef();
      this._macroDefs.put(name, macroDef);
      return macroDef.createSequential();
    }
  }
}
