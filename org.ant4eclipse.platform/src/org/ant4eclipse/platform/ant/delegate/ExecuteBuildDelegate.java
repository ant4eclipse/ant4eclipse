/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.ant.delegate;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.ant4eclipse.core.Assert;

import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.taskdefs.CallTarget;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.taskdefs.Ant.Reference;

public class ExecuteBuildDelegate extends AbstractAntDelegate {

  /** the prefix to be used */
  private String                _prefix = "";

  /** the parameter list */
  private final List<Property>  _parameters;

  /** the references list */
  private final List<Reference> _references;

  /**
   * <p>
   * Creates a new instance of type AbstractPdeBuildTask.
   * </p>
   * 
   */
  public ExecuteBuildDelegate(final ProjectComponent component) {
    super(component);

    this._parameters = new LinkedList<Property>();
    this._references = new LinkedList<Reference>();
  }

  /**
   * <p>
   * Corresponds to <code>&lt;antcall&gt;</code>'s nested <code>&lt;param&gt;</code> element.
   * </p>
   * 
   * @param property
   */
  public void addParam(final Property property) {
    this._parameters.add(property);
  }

  /**
   * <p>
   * Corresponds to <code>&lt;antcall&gt;</code>'s nested <code>&lt;reference&gt;</code> element.
   * </p>
   * 
   * @param reference
   */
  public void addReference(final Reference reference) {
    this._references.add(reference);
  }

  /**
   * @param prefix
   */
  public void setPrefix(final String prefix) {
    this._prefix = prefix;
  }

  /**
   * <p>
   * Returns <code>true</code> if a target with the given name exists in the current project.
   * </p>
   * 
   * @param targetName
   *          the name of the target.
   */
  public final boolean isCallTarget(final String targetName) {
    Assert.nonEmpty(targetName);

    @SuppressWarnings("unchecked")
    final Map<String, Target> allTargets = getAntProject().getTargets();

    if (allTargets.containsKey(targetName)) {
      return true;
    }

    return false;
  }

  /**
   * <p>
   * Creates a new CallTarget instance.
   * </p>
   * 
   *@param owningTarget
   *          Target in whose scope this task belongs. May be <code>null</code>, indicating a top-level task.
   * @param targetName
   *          the name of the target.
   * @param inheritAll
   * @param inheritRefs
   * @param implicitParameters
   * @return the new CallTarget
   */
  public CallTarget createCallTarget(final Target owningTarget, final String targetName, final boolean inheritAll,
      final boolean inheritRefs, final Map<String, String> implicitParameters) {
    Assert.nonEmpty(targetName);

    // create the CallTarget
    final CallTarget callTarget = (CallTarget) getAntProject().createTask("antcall");
    // callTarget.setOwningTarget(owningTarget);
    callTarget.init();
    callTarget.setTarget(targetName);
    callTarget.setInheritAll(inheritAll);
    callTarget.setInheritRefs(inheritRefs);

    // set parameters set via the parameter map
    Property toSet = null;

    // initialize prefix
    final String prefix = "";
    if ((this._prefix != null) && (this._prefix.trim().length() != 0)) {
      this._prefix = this._prefix.trim() + ".";
    }

    if (implicitParameters != null) {
      Iterator<Entry<String, String>> iterator = implicitParameters.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<String, String> entry = iterator.next();
        toSet = callTarget.createParam();
        toSet.setName(prefix + entry.getKey());
        toSet.setValue("" + entry.getValue());
      }
    }

    // set parameters set via ant's the nested parameter element
    for (final Iterator<Property> iterator = this._parameters.iterator(); iterator.hasNext();) {
      final Property param = iterator.next();

      toSet = callTarget.createParam();
      toSet.setName(param.getName());
      if (param.getValue() != null) {
        toSet.setValue(param.getValue());
      }
      if (param.getFile() != null) {
        toSet.setFile(param.getFile());
      }
      if (param.getResource() != null) {
        toSet.setResource(param.getResource());
      }
      if (param.getPrefix() != null) {
        toSet.setPrefix(param.getPrefix());
      }
      if (param.getRefid() != null) {
        toSet.setRefid(param.getRefid());
      }
      if (param.getEnvironment() != null) {
        toSet.setEnvironment(param.getEnvironment());
      }
      if (param.getClasspath() != null) {
        toSet.setClasspath(param.getClasspath());
      }
    }

    // set all references
    for (final Iterator<Reference> iterator = this._references.iterator(); iterator.hasNext();) {
      final Reference reference = iterator.next();
      callTarget.addReference(reference);
    }

    // return the result
    return callTarget;
  }

  /**
   * <p>
   * Executes the list of tasks.
   * </p>
   * 
   * @param tasks
   *          the list of tasks.
   */
  public final void executeSequential(final List<Task> tasks) {
    Assert.notNull(tasks);

    final TaskContainer tc = (TaskContainer) getAntProject().createTask("sequential");

    for (final Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
      final Task task = (Task) iterator.next();
      tc.addTask(task);
    }

    ((Task) tc).execute();
  }
}
