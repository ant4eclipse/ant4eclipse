package org.ant4eclipse.platform.ant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.platform.ant.core.task.ScopedMacroDefinition;
import org.ant4eclipse.platform.model.resource.BuildCommand;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * @author
 */
public class ExecuteProjectBuildersTask extends AbstractProjectBasedTask implements DynamicElement,
    MacroExecutionComponent<String> {

  /** list of all builder macro definitions */
  private final Map<String, MacroDef>          _builderMacroDefs;

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate<String> _macroExecutionDelegate;

  /**
   *
   */
  public ExecuteProjectBuildersTask() {
    this._macroExecutionDelegate = new MacroExecutionDelegate<String>(this, "executeBuildCommands");
    this._builderMacroDefs = new HashMap<String, MacroDef>();
  }

  public String getPrefix() {
    return this._macroExecutionDelegate.getPrefix();
  }

  public void setPrefix(String prefix) {
    this._macroExecutionDelegate.setPrefix(prefix);
  }

  public NestedSequential createScopedMacroDefinition(String scope) {
    return this._macroExecutionDelegate.createScopedMacroDefinition(scope);
  }

  public void executeMacroInstance(MacroDef macroDef, MacroExecutionValues macroExecutionValues) {
    this._macroExecutionDelegate.executeMacroInstance(macroDef, macroExecutionValues);
  }

  public List<ScopedMacroDefinition<String>> getScopedMacroDefinitions() {
    return this._macroExecutionDelegate.getScopedMacroDefinitions();
  }

  /**
   * @param name
   * @return
   * @throws BuildException
   */
  public Object createDynamicElement(String name) {
    return this._macroExecutionDelegate.createScopedMacroDefinition(name);
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    EclipseProject eclipseProject = getEclipseProject();

    BuildCommand[] buildCommands = eclipseProject.getBuildCommands();

    for (BuildCommand buildCommand : buildCommands) {
      if (this._builderMacroDefs.containsKey(buildCommand.getName())) {
        MacroDef macroDef = this._builderMacroDefs.get(buildCommand.getName());
        // TODO: scoped Properties
        this._macroExecutionDelegate.executeMacroInstance(macroDef, null);
      }
    }
  }
}
