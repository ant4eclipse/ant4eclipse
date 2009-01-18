package org.ant4eclipse.jdt.ant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.ant.delegate.MacroExecutionDelegate;
import org.ant4eclipse.jdt.tools.JdtResolver;
import org.ant4eclipse.platform.ant.base.AbstractProjectSetBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.taskdefs.MacroDef;

public class ExecuteProjectSetTask extends AbstractProjectSetBasedTask {

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate _delegate;

  private final List<MacroDef>         _macroDefs;

  /**
   * 
   */
  public ExecuteProjectSetTask() {
    this._delegate = new MacroExecutionDelegate(this);
    this._macroDefs = new LinkedList<MacroDef>();
  }

  @Override
  protected void doExecute() {
    requireTeamProjectSetOrProjectNamesSet();
    requireWorkspaceSet();

    final String[] projectNames = isTeamProjectSetSet() ? getTeamProjectSet().getProjectNames() : getProjectNames();

    // TODO Properties
    final List<EclipseProject> projects = JdtResolver.resolveBuildOrder(getWorkspace(), projectNames, null);

    for (final MacroDef macroDef : this._macroDefs) {

      for (final EclipseProject eclipseProject : projects) {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("project.name", eclipseProject.getSpecifiedName());
        this._delegate.executeMacroInstance(macroDef, "executeProjectSet", properties, new HashMap<String, Object>());
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Object createForEachProject() {
    final MacroDef macroDef = this._delegate.createMacroDef();
    this._macroDefs.add(macroDef);
    return macroDef.createSequential();
  }
}
