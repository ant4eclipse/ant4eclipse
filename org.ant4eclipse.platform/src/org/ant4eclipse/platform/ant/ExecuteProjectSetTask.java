package org.ant4eclipse.platform.ant;

import java.util.List;

import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ProjectReferenceAwareComponent;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.delegate.ProjectReferenceAwareDelegate;
import org.ant4eclipse.platform.ant.core.delegate.SubElementDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectSetPathBasedTask;
import org.ant4eclipse.platform.ant.core.task.ScopedMacroDefinition;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.BuildOrderResolver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

enum Scope {
  PROJECT;
}

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectSetTask extends AbstractProjectSetPathBasedTask implements DynamicElement,
    ProjectReferenceAwareComponent, MacroExecutionComponent<Scope> {

  /** the {@link MacroExecutionDelegate} */
  private final MacroExecutionDelegate<Scope>  _macroExecutionDelegate;

  private final SubElementDelegate             _subElementDelegate;

  private final ProjectReferenceAwareDelegate  _projectReferenceAwareDelegate;

  private final PlatformExecutorValuesProvider _platformExecutorValuesProvider;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProjectSetTask}.
   * </p>
   */
  public ExecuteProjectSetTask() {
    // create the MacroExecutionDelegate
    this._macroExecutionDelegate = new MacroExecutionDelegate<Scope>(this, "executeProjectSet");
    this._subElementDelegate = new SubElementDelegate(this);
    this._projectReferenceAwareDelegate = new ProjectReferenceAwareDelegate();
    this._platformExecutorValuesProvider = new PlatformExecutorValuesProvider(getPathDelegate());
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

  public NestedSequential createScopedMacroDefinition(Scope scope) {
    return this._macroExecutionDelegate.createScopedMacroDefinition(scope);
  }

  public void executeMacroInstance(MacroDef macroDef, MacroExecutionValues macroExecutionValues) {
    this._macroExecutionDelegate.executeMacroInstance(macroDef, macroExecutionValues);
  }

  public List<ScopedMacroDefinition<Scope>> getScopedMacroDefinitions() {
    return this._macroExecutionDelegate.getScopedMacroDefinitions();
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
    for (final ScopedMacroDefinition<Scope> scopedMacroDefinition : getScopedMacroDefinitions()) {
      for (final EclipseProject eclipseProject : projects) {

        // create the macro execution values
        MacroExecutionValues macroExecutionValues = new MacroExecutionValues();

        // set the values
        this._platformExecutorValuesProvider.provideExecutorValues(eclipseProject, macroExecutionValues);

        // execute macro instance
        this._macroExecutionDelegate.executeMacroInstance(scopedMacroDefinition.getMacroDef(), macroExecutionValues);
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
    return createScopedMacroDefinition(Scope.PROJECT);
  }

  public Object createDynamicElement(String name) throws BuildException {
    return this._subElementDelegate.createDynamicElement(name);
  }
}
