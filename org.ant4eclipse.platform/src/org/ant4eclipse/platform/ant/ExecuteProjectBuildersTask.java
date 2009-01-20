package org.ant4eclipse.platform.ant;

import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.platform.model.resource.BuildCommand;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * @author
 */
public class ExecuteProjectBuildersTask extends AbstractProjectBasedTask implements DynamicElement {

  /** list of all builder macro definitions */
  private final Map<String, MacroDef>  _builderMacroDefs;

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate _delegate;

  /**
   * 
   */
  public ExecuteProjectBuildersTask() {
    this._delegate = new MacroExecutionDelegate(this, "executeBuildCommands");
    this._builderMacroDefs = new HashMap<String, MacroDef>();
  }

  public String getPrefix() {
    return this._delegate.getPrefix();
  }

  public void setPrefix(String prefix) {
    this._delegate.setPrefix(prefix);
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
        this._delegate.executeMacroInstance(macroDef, null);
      }
    }
  }

  /**
   * @param name
   * @return
   * @throws BuildException
   */
  public Object createDynamicElement(String name) {
    MacroDef macroDef = this._delegate.createMacroDef();
    this._builderMacroDefs.put(name, macroDef);
    return macroDef.createSequential();
  }
}
