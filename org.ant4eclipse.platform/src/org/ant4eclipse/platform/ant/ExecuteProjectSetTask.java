package org.ant4eclipse.platform.ant;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ProjectReferenceAwareComponent;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.delegate.ProjectReferenceAwareDelegate;
import org.ant4eclipse.platform.ant.core.delegate.SubElementDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectSetPathBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.BuildOrderResolver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectSetTask extends AbstractProjectSetPathBasedTask implements DynamicElement,
    ProjectReferenceAwareComponent {

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate         _macroExecutionDelegate;

  private final SubElementDelegate             _subElementDelegate;

  private final ProjectReferenceAwareDelegate  _projectReferenceAwareDelegate;

  private final PlatformExecutorValuesProvider _platformExecutorValuesProvider;

  /** the list of all defined macro definitions */
  private final List<MacroDef>                 _macroDefs;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProjectSetTask}.
   * </p>
   */
  public ExecuteProjectSetTask() {
    // create the MacroExecutionDelegate
    this._macroExecutionDelegate = new MacroExecutionDelegate(this, "executeProjectSet");

    this._subElementDelegate = new SubElementDelegate(this);

    this._projectReferenceAwareDelegate = new ProjectReferenceAwareDelegate();

    this._platformExecutorValuesProvider = new PlatformExecutorValuesProvider(getPathDelegate());

    // create the macro definition list
    this._macroDefs = new LinkedList<MacroDef>();

  }

  public String[] getProjectReferenceTypes() {
    return this._projectReferenceAwareDelegate.getProjectReferenceTypes();
  }

  public boolean isProjectReferenceTypesSet() {
    return this._projectReferenceAwareDelegate.isProjectReferenceTypesSet();
  }

  public void requireProjectReferenceTypesSet() {
    this._projectReferenceAwareDelegate.requireProjectReferenceTypesSet();
  }

  public void setProjectReferenceTypes(String referenceTypes) {
    this._projectReferenceAwareDelegate.setProjectReferenceTypes(referenceTypes);
  }

  public String getPrefix() {
    return this._macroExecutionDelegate.getPrefix();
  }

  public void setPrefix(String prefix) {
    this._macroExecutionDelegate.setPrefix(prefix);
  }

  @Override
  protected void doExecute() {
    // check required attributes
    requireTeamProjectSetOrProjectNamesSet();
    requireWorkspaceSet();

    // calculate build order
    final List<EclipseProject> projects = BuildOrderResolver.resolveBuildOrder(getWorkspace(), getProjectNames(),
        this._projectReferenceAwareDelegate.getProjectReferenceTypes(), this._subElementDelegate.getSubElements());

    // execute the macro definitions
    for (final MacroDef macroDef : this._macroDefs) {
      for (final EclipseProject eclipseProject : projects) {

        // create the macro execution values
        MacroExecutionValues macroExecutionValues = new MacroExecutionValues();

        // set the values
        this._platformExecutorValuesProvider.provideExecutorValues(eclipseProject, macroExecutionValues);

        // execute macro instance
        this._macroExecutionDelegate.executeMacroInstance(macroDef, macroExecutionValues);
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
    final MacroDef macroDef = this._macroExecutionDelegate.createMacroDef();

    // put it to the list if macro definitions
    this._macroDefs.add(macroDef);

    // return the associated NestedSequential
    return macroDef.createSequential();
  }

  public Object createDynamicElement(String name) throws BuildException {
    return this._subElementDelegate.createDynamicElement(name);
  }
}
