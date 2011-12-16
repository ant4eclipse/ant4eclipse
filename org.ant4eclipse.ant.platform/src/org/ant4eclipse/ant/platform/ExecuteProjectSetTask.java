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
package org.ant4eclipse.ant.platform;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.ant4eclipse.ant.platform.core.MacroExecutionComponent;
import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.ProjectReferenceAwareComponent;
import org.ant4eclipse.ant.platform.core.ScopedMacroDefinition;
import org.ant4eclipse.ant.platform.core.SubElementAndAttributesComponent;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.ant.platform.core.delegate.ProjectReferenceAwareDelegate;
import org.ant4eclipse.ant.platform.core.delegate.SubElementAndAttributesDelegate;
import org.ant4eclipse.ant.platform.core.task.AbstractProjectSetPathBasedTask;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.StopWatchService;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.BuildOrderResolver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectSetTask extends AbstractProjectSetPathBasedTask implements MacroExecutionComponent<Scope>,
    SubElementAndAttributesComponent, ProjectReferenceAwareComponent {

  /** the {@link MacroExecutionDelegate} */
  private MacroExecutionDelegate<Scope>   _macroExecutionDelegate;

  /** the {@link SubElementAndAttributesDelegate} */
  private SubElementAndAttributesDelegate _subElementAndAttributeDelegate;

  /** the {@link ProjectReferenceAwareDelegate} */
  private ProjectReferenceAwareDelegate   _projectReferenceAwareDelegate;

  /** the {@link PlatformExecutorValuesProvider} */
  private PlatformExecutorValuesProvider  _platformExecutorValuesProvider;

  /** indicates if the build order should be resolved */
  private boolean                         _resolveBuildOrder = true;

  /** indicates the number of concurrent threads */
  private int                             _threadCount       = 1;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProjectSetTask}.
   * </p>
   */
  public ExecuteProjectSetTask() {
    // create the MacroExecutionDelegate
    this._macroExecutionDelegate = new MacroExecutionDelegate<Scope>(this, "executeProjectSet");
    this._subElementAndAttributeDelegate = new SubElementAndAttributesDelegate(this);
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

  public int getThreadCount() {
    return this._threadCount;
  }

  public void setThreadCount(int threads) {
    this._threadCount = threads;
  }

  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();

    if (this._resolveBuildOrder && this._threadCount > 1) {
      throw new BuildException("In parallel mode (threadCount>1) build order can not be resolved");
    }
    if (this._threadCount < 1) {
      throw new BuildException("ThreadCount must at least be 1");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {
    StopWatchService stopWatchService = ServiceRegistryAccess.instance().getService(StopWatchService.class);
    stopWatchService.getOrCreateStopWatch("executeProjectSet").start();

    // check required attributes
    requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet();
    requireWorkspaceDirectoryOrWorkspaceIdSet();

    // get all eclipse projects and calculate the build order if necessary
    List<EclipseProject> projects = null;
    if (this._resolveBuildOrder) {

      // resolve the build order
      projects = BuildOrderResolver.resolveBuildOrder(getWorkspace(), getProjectNames(),
          this._projectReferenceAwareDelegate.getProjectReferenceTypes(),
          this._subElementAndAttributeDelegate.getSubElements());
    } else {
      // only get the specified projects
      projects = Arrays.asList(getWorkspace().getProjects(getProjectNames(), false));
    }

    final BuildCallable[] buildCallables = new BuildCallable[this._threadCount];
    for (int i = 0; i < buildCallables.length; i++) {
      buildCallables[i] = new BuildCallable();
    }

    int callableIndex = 0;
    for (final EclipseProject eclipseProject : projects) {
      buildCallables[callableIndex].addProject(eclipseProject);
      callableIndex++;
      if (callableIndex >= buildCallables.length) {
        callableIndex = 0;
      }
    }

    // execute the macro definitions
    for (ScopedMacroDefinition<Scope> scopedMacroDefinition : getScopedMacroDefinitions()) {

      //
      for (BuildCallable buildCallable : buildCallables) {
        buildCallable.setScopedMacroDefinition(scopedMacroDefinition);
      }

      if (buildCallables.length > 1) {

        // create the future tasks
        @SuppressWarnings("unchecked")
        FutureTask<Void>[] futureTasks = new FutureTask[this._threadCount];
        for (int i = 0; i < futureTasks.length; i++) {
          futureTasks[i] = new FutureTask<Void>(buildCallables[i]);

          Thread thread = new Thread(futureTasks[i]);
          thread.setName("A4E-" + thread.getName());
          thread.start();
        }
        // collect the result
        for (FutureTask<Void> futureTask : futureTasks) {
          try {
            futureTask.get();
          } catch (Exception e) {
            Throwable t = e;
            if (e instanceof ExecutionException) {
              t = ((ExecutionException) e).getCause();
            }
            if (t instanceof BuildException) {
              throw (BuildException) t;
            }
            throw new BuildException(e);
          }
        }
      } else {
        try {
          buildCallables[0].call();
        } catch (Exception e) {
          Throwable t = e;
          if (e instanceof ExecutionException) {
            t = ((ExecutionException) e).getCause();
          }
          if (t instanceof BuildException) {
            throw (BuildException) t;
          }
          throw new BuildException(e);

        }
      }

    }

    stopWatchService.getOrCreateStopWatch("executeProjectSet").stop();

  }

  class BuildCallable implements Callable<Void> {
    private final List<EclipseProject>   _projects = new LinkedList<EclipseProject>();

    private ScopedMacroDefinition<Scope> _scopedMacroDefinition;

    public void addProject(EclipseProject project) {
      this._projects.add(project);

    }

    public void setScopedMacroDefinition(ScopedMacroDefinition<Scope> scopedMacroDefinition) {
      this._scopedMacroDefinition = scopedMacroDefinition;

    }

    public Void call() throws Exception {

      // System.out.println(String.format("ExecuteProjectSetTask[%s] 1: %s", Thread.currentThread(), this._projects));

      for (final EclipseProject eclipseProject : this._projects) {

        // System.out.println(String.format("ExecuteProjectSetTask[%s] 2: %s", Thread.currentThread(),
        // eclipseProject.getSpecifiedName()));

        // execute macro instance
        ExecuteProjectSetTask.this._macroExecutionDelegate.executeMacroInstance(
            this._scopedMacroDefinition.getMacroDef(), new MacroExecutionValuesProvider() {

              public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
                // set the values
                ExecuteProjectSetTask.this._platformExecutorValuesProvider
                    .provideExecutorValues(eclipseProject, values);

                // System.out.println(String.format("ExecuteProjectSetTask[%s] 3: %s", Thread.currentThread(),
                // values.getProperties()));

                // return result
                return values;
              }
            });

      }
      return null;
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
    return this._subElementAndAttributeDelegate.createDynamicElement(name);
  }

  /**
   * {@inheritDoc}
   */
  public List<Object> getSubElements() {
    return this._subElementAndAttributeDelegate.getSubElements();
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, String> getSubAttributes() {
    return this._subElementAndAttributeDelegate.getSubAttributes();
  }

  /**
   * {@inheritDoc}
   */
  public void setDynamicAttribute(String name, String value) throws BuildException {
    this._subElementAndAttributeDelegate.setDynamicAttribute(name, value);
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
