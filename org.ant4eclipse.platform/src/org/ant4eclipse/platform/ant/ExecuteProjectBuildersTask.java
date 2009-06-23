package org.ant4eclipse.platform.ant;

import java.util.List;

import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.platform.model.resource.BuildCommand;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * <p>
 * Executes all the project builders that are defined in an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectBuildersTask extends AbstractProjectBasedTask implements DynamicElement,
    MacroExecutionComponent<String> {

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate<String> _macroExecutionDelegate;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProjectBuildersTask}.
   * </p>
   */
  public ExecuteProjectBuildersTask() {
    // create the delegate
    this._macroExecutionDelegate = new MacroExecutionDelegate<String>(this, "executeBuildCommands");
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
    NestedSequential sequential = createScopedMacroDefinition(name);
    return sequential;
  }

  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // get all build commands
    BuildCommand[] buildCommands = getEclipseProject().getBuildCommands();

    for (BuildCommand buildCommand : buildCommands) {

      ScopedMacroDefinition<String> macroDefinition = getScopedMacroDefinition(buildCommand.getName());

      if (macroDefinition != null) {
        MacroDef macroDef = macroDefinition.getMacroDef();

        MacroExecutionValues values = new MacroExecutionValues();
        // values.getProperties().put("", buildCommand.getName());

        this._macroExecutionDelegate.executeMacroInstance(macroDef, values);
      } else {
        throw new BuildException();
      }
    }
  }

  /**
   * @param name
   * @return
   */
  protected ScopedMacroDefinition<String> getScopedMacroDefinition(String name) {
    //
    List<ScopedMacroDefinition<String>> list = getScopedMacroDefinitions();

    for (ScopedMacroDefinition<String> scopedMacroDefinition : list) {
      if (name.equalsIgnoreCase(scopedMacroDefinition.getScope())) {
        return scopedMacroDefinition;
      }
    }
    return null;
  }
}
