/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ProjectReferenceAwareComponent;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.SubElementComponent;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.platform.ant.core.delegate.ProjectReferenceAwareDelegate;
import org.ant4eclipse.platform.ant.core.delegate.SubElementDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectSetPathBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.BuildOrderResolver;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

import java.util.Arrays;
import java.util.List;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectSetTask extends AbstractProjectSetPathBasedTask implements MacroExecutionComponent<Scope>,
    SubElementComponent, ProjectReferenceAwareComponent {

  /** the {@link MacroExecutionDelegate} */
  private MacroExecutionDelegate<Scope>  _macroExecutionDelegate;

  /** the {@link SubElementDelegate} */
  private SubElementDelegate             _subElementDelegate;

  /** the {@link ProjectReferenceAwareDelegate} */
  private ProjectReferenceAwareDelegate  _projectReferenceAwareDelegate;

  /** the {@link PlatformExecutorValuesProvider} */
  private PlatformExecutorValuesProvider _platformExecutorValuesProvider;

  /** indicates if the build order should be resolved */
  private boolean                        _resolveBuildOrder = true;

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

  /**
   * <p>
   * Returns is the build order should be resolved or not.
   * </p>
   * 
   * @return the resolveBuildOrder
   */
  public boolean isResolveBuildOrder() {
    return this._resolveBuildOrder;
  }

  /**
   * <p>
   * Sets if the build order should be resolved or not.
   * </p>
   * 
   * @param resolveBuildOrder
   *          the resolveBuildOrder to set
   */
  public void setResolveBuildOrder(boolean resolveBuildOrder) {
    this._resolveBuildOrder = resolveBuildOrder;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getProjectReferenceTypes() {
    return this._projectReferenceAwareDelegate.getProjectReferenceTypes();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isProjectReferenceTypesSet() {
    return this._projectReferenceAwareDelegate.isProjectReferenceTypesSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireProjectReferenceTypesSet() {
    this._projectReferenceAwareDelegate.requireProjectReferenceTypesSet();
  }

  /**
   * {@inheritDoc}
   */
  public void setProjectReferenceTypes(String referenceTypes) {
    this._projectReferenceAwareDelegate.setProjectReferenceTypes(referenceTypes);
  }

  /**
   * {@inheritDoc}
   */
  public String getPrefix() {
    return this._macroExecutionDelegate.getPrefix();
  }

  /**
   * {@inheritDoc}
   */
  public void setPrefix(String prefix) {
    this._macroExecutionDelegate.setPrefix(prefix);
  }

  /**
   * {@inheritDoc}
   */
  public NestedSequential createScopedMacroDefinition(Scope scope) {
    return this._macroExecutionDelegate.createScopedMacroDefinition(scope);
  }

  /**
   * {@inheritDoc}
   */
  public void executeMacroInstance(MacroDef macroDef, MacroExecutionValuesProvider provider) {
    this._macroExecutionDelegate.executeMacroInstance(macroDef, provider);
  }

  /**
   * {@inheritDoc}
   */
  public List<ScopedMacroDefinition<Scope>> getScopedMacroDefinitions() {
    return this._macroExecutionDelegate.getScopedMacroDefinitions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {
    // check required attributes
    requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet();
    requireWorkspaceDirectorySet();

    // get all eclipse projects and calculate the build order if necessary
    List<EclipseProject> projects = null;
    if (this._resolveBuildOrder) {
      // resolve the build order
      projects = BuildOrderResolver.resolveBuildOrder(getWorkspace(), getProjectNames(),
          this._projectReferenceAwareDelegate.getProjectReferenceTypes(), this._subElementDelegate.getSubElements());
    } else {
      // only get the specified projects
      projects = Arrays.asList(getWorkspace().getProjects(getProjectNames(), false));
    }

    // execute the macro definitions
    for (ScopedMacroDefinition<Scope> scopedMacroDefinition : getScopedMacroDefinitions()) {
      for (final EclipseProject eclipseProject : projects) {

        // execute macro instance
        this._macroExecutionDelegate.executeMacroInstance(scopedMacroDefinition.getMacroDef(),
            new MacroExecutionValuesProvider() {

              public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
                // set the values
                ExecuteProjectSetTask.this._platformExecutorValuesProvider
                    .provideExecutorValues(eclipseProject, values);

                // return result
                return values;
              }
            });
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

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(String name) throws BuildException {
    return this._subElementDelegate.createDynamicElement(name);
  }

  /**
   * {@inheritDoc}
   */
  public List<Object> getSubElements() {
    return this._subElementDelegate.getSubElements();
  }

}

/**
 * <p>
 * Within the ExecuteProjectSetTask, we only have the PROJECT scope.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
enum Scope {
  PROJECT;
}
