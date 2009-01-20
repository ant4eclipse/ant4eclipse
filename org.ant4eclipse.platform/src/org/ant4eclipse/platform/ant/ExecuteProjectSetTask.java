package org.ant4eclipse.platform.ant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectSetPathBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.BuildOrderResolver;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectSetTask extends AbstractProjectSetPathBasedTask {

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate         _delegate;

  /** the list of all defined macro definitions */
  private final List<MacroDef>                 _macroDefs;

  private final PlatformExecutorValuesProvider _platformExecutorValuesProvider;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProjectSetTask}.
   * </p>
   */
  public ExecuteProjectSetTask() {
    // create the MacroExecutionDelegate
    this._delegate = new MacroExecutionDelegate(this, "executeProjectSet");

    // create the macro definition list
    this._macroDefs = new LinkedList<MacroDef>();

    this._platformExecutorValuesProvider = new PlatformExecutorValuesProvider(getPathDelegate());
  }

  public String getPrefix() {
    return this._delegate.getPrefix();
  }

  public void setPrefix(String prefix) {
    this._delegate.setPrefix(prefix);
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

        MacroExecutionValues macroExecutionValues = this._platformExecutorValuesProvider
            .getExecutorValues(eclipseProject);

        // create scoped properties
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PlatformExecutorValuesProvider.PROJECT_NAME, eclipseProject.getSpecifiedName());
        properties.put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY, convertToString(eclipseProject.getFolder()));

        // create scoped references
        final Map<String, Object> references = new HashMap<String, Object>();
        references
            .put(PlatformExecutorValuesProvider.PROJECT_DIRECTORY_PATH, convertToPath(eclipseProject.getFolder()));

        this._delegate.executeMacroInstance(macroDef, macroExecutionValues);
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
