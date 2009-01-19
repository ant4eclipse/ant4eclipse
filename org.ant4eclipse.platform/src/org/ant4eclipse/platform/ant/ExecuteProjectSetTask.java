package org.ant4eclipse.platform.ant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.ant.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.base.AbstractProjectSetBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.BuildOrderResolver;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectSetTask extends AbstractProjectSetBasedTask {

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate _delegate;

  /** the list of all defined macro definitions */
  private final List<MacroDef>         _macroDefs;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProjectSetTask}.
   * </p>
   */
  public ExecuteProjectSetTask() {
    // create the MacroExecutionDelegate
    this._delegate = new MacroExecutionDelegate(this);

    // create the macro definition list
    this._macroDefs = new LinkedList<MacroDef>();
  }

  @Override
  protected void doExecute() {
    // check required attributes
    requireTeamProjectSetOrProjectNamesSet();
    requireWorkspaceSet();

    // get the project names to order
    final String[] projectNames = isTeamProjectSetSet() ? getTeamProjectSet().getProjectNames() : getProjectNames();

    // calculate build order
    // TODO Properties
    final List<EclipseProject> projects = BuildOrderResolver.resolveBuildOrder(getWorkspace(), projectNames, null);

    // execute the macro definitions
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
   * Creates a new {@link MacroDef} for each &lt;forEachProject&gt; element of the {@link ExecuteProjectSetTask}.
   * </p>
   * 
   * @return the {@link NestedSequential}
   */
  public final Object createForEachProject() {
    // create a new MacroDef
    final MacroDef macroDef = this._delegate.createMacroDef();

    // put it to the list if macro definitions
    this._macroDefs.add(macroDef);

    // return the associated NestedSequential
    return macroDef.createSequential();
  }
}
