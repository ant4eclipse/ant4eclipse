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
package org.ant4eclipse.ant.platform;

import java.util.Hashtable;
import java.util.Map;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.model.platform.resource.BuildCommand;
import org.ant4eclipse.model.platform.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Target;

/**
 * ExecuteBuildersTask -- Calls for each builder defined in the project's <code>.project</code> file an Ant target. The
 * dependend targets of a target for a builder are executed too. A target is not executed if it's <code>if</code> or
 * <code>unless</code> conditions are not satisfied.
 * 
 * TODO Allow to execute each target in an own project (as in the antcall-task) TODO Support different kind of triggers
 * TODO Support arguments of builders
 * 
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ExecuteBuildersTask extends AbstractProjectBasedTask {

  /**
   * This target name is executed if no other target for a builder could be found.
   */
  public final static String DEFAULT_TARGET_NAME       = "default-builder";

  /**
   * This property contains the name of the buildcommand that is currently executed.
   * 
   * <p>
   * A usage for this property could be the default-builder target that wants to know which builder was defined in the
   * <code>.project</code> but has no target defined
   */
  public final static String CURRENT_BUILDCOMMAND_NAME = "current.buildcommand";

  /**
   * A name of a target that is executed, if for a builder no target can be found. This value is optional; if not set
   * and there is no target for a builder, than the target defined in DEFAULT_TARGET_NAME will be executed
   * 
   * <p>
   * If this value is not null but does not exist in the project the ExecuteBuildersTask fails. If this task is null and
   * the target specified in DEFAULT_TARGET_NAME does not exists, the Task fails. (Both cases are only valid if a
   * default target is needed, i.e. for some builder no target can be found)
   * 
   */
  private String             _defaultTargetName        = null;

  public String getDefaultTargetName() {
    return _defaultTargetName;
  }

  public void setDefaultTargetName(String defaultTargetName) {
    this._defaultTargetName = defaultTargetName;
  }

  /**
   * {@inheritDoc}
   */
  public void execute() throws BuildException {
    requireWorkspaceAndProjectNameOrProjectSet();

    final EclipseProject project = getEclipseProject();
    final BuildCommand[] buildCommands = project.getBuildCommands();
    final String defaultTargetName = getDefaultTargetName() == null ? DEFAULT_TARGET_NAME : getDefaultTargetName();

    for (int i = 0; i < buildCommands.length; i++) {
      final BuildCommand buildCommand = buildCommands[i];
      String targetName = getTargetNameForBuildCommand(buildCommand, defaultTargetName);
      if (targetName == null) {
        throw new BuildException("No target could be found for builder command '" + buildCommand.getName() + "'");
      }

      A4ELogging.info("Executing target %s for build command %s", new String[] { targetName, buildCommand.getName() });
      getProjectBase().setStringProperty(CURRENT_BUILDCOMMAND_NAME, buildCommand.getName());
      getProject().executeTarget(targetName);

    }
  }

  /**
   * Returns the target for a buildCommand.
   * 
   * <p>
   * The name for a target is determined in the following order:
   * <ol>
   * <li>if a property called <code>builder.<i>full-buildcommand-name</i>.target</code> is defined, it's value will be
   * used as the target name</li>
   * <li>if a property called <code>builder.<i>buildcommand-name</i>.target</code> is defined, it's value will be used
   * as the target name</li>
   * <li>If a target exists in the current project, that is called exactly as the build command's name, this target will
   * be executed</li>
   * <li>If a target exists in the current project, that is called as the buildcommand-name this target will be executed
   * </li>
   * <li>If defaultTargetName is not null and exists in the project this target will be executed. If defaultTargetName
   * is not null, but the target does not exists this task fails</li>
   * <li>If a target exists with the name specified in DEFAULT_TARGET_NAME this target will be executed</li>
   * <li>In all other cases null is returned</li>
   * </ol>
   * <p>
   * <b>Note:</b> <i>full-buildcommand-name</i> is the complete name of a buildCommand as defined in a
   * <code>.project</code>-file, like <code>org.eclipse.jdt.core.javabuilder</code> while <i>buildcommand-name</i> is
   * the "unqualified" name of a builder, like <code>javabuilder</code>.
   * 
   * @return The target for this buildCommand or null if no target could be found that command.
   */
  protected String getTargetNameForBuildCommand(BuildCommand buildCommand, String defaultTargetName) {
    Assert.notNull(buildCommand);
    Assert.notNull(defaultTargetName);

    final String qualifiedCommandName = buildCommand.getName();
    final String unqualifiedCommandName = qualifiedCommandName.substring(qualifiedCommandName.lastIndexOf('.') + 1);

    @SuppressWarnings("unchecked")
    Hashtable<String, String> allProperties = getProject().getProperties();

    if (allProperties.containsKey("builder." + qualifiedCommandName + ".target")) {
      String targetName = allProperties.get("builder." + qualifiedCommandName + ".target");
      if (getProject().getTargets().containsKey(targetName)) {
        return targetName;
      }
    }

    if (allProperties.containsKey("builder." + unqualifiedCommandName + ".target")) {
      String targetName = allProperties.get("builder." + unqualifiedCommandName + ".target");
      if (getProject().getTargets().containsKey(targetName)) {
        return targetName;
      }
    }

    if (getProject().getTargets().containsKey(qualifiedCommandName)) {
      return qualifiedCommandName;
    }

    if (getProject().getTargets().containsKey(unqualifiedCommandName)) {
      return unqualifiedCommandName;
    }

    if (getProject().getTargets().containsKey(defaultTargetName)) {
      return defaultTargetName;
    }

    return null;
  }

  /**
   * Returns the target with the given targetName or null if no such target exists in the current project
   */
  protected Target getTarget(String targetName) {
    Assert.notNull(targetName);

    @SuppressWarnings("unchecked")
    final Map<String, Target> allTargets = getProject().getTargets();
    if (allTargets.containsKey(targetName)) {
      return allTargets.get(targetName);
    }

    return null;
  }
}
