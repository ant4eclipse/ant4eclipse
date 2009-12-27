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
package org.ant4eclipse.ant.platform.ant;


import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.ScopedMacroDefinition;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.ant.platform.core.task.AbstractExecuteProjectTask;
import org.ant4eclipse.lib.platform.model.resource.BuildCommand;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;

import java.util.List;

/**
 * <p>
 * Executes all the project builders that are defined in an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteProjectBuildersTask extends AbstractExecuteProjectTask {

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteProjectBuildersTask}.
   * </p>
   */
  public ExecuteProjectBuildersTask() {
    super("executeBuildCommands");
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(String name) {
    NestedSequential sequential = createScopedMacroDefinition(name);
    return sequential;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // check require fields
    requireWorkspaceAndProjectNameSet();

    // get all build commands
    BuildCommand[] buildCommands = getEclipseProject().getBuildCommands();

    for (BuildCommand buildCommand : buildCommands) {

      ScopedMacroDefinition<String> macroDefinition = getScopedMacroDefinition(buildCommand.getName());

      if (macroDefinition != null) {
        executeMacroInstance(macroDefinition.getMacroDef(), new MacroExecutionValuesProvider() {
          public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
            // values.getProperties().put("", buildCommand.getName());
            return values;
          }
        });
      } else {
        throw new BuildException();
      }
    }
  }

  /**
   * @param name
   * @return
   */
  protected ScopedMacroDefinition<String> getScopedMacroDefinition(String name) {
    //
    List<ScopedMacroDefinition<String>> list = getScopedMacroDefinitions();

    for (ScopedMacroDefinition<String> scopedMacroDefinition : list) {
      if (name.equalsIgnoreCase(scopedMacroDefinition.getScope())) {
        return scopedMacroDefinition;
      }
    }
    return null;
  }
}
