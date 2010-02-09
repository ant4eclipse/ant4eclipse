package org.ant4eclipse.ant.platform;

import java.io.File;
import java.util.Collection;

import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.ScopedMacroDefinition;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.ant.platform.core.task.AbstractExecuteProjectTask;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfigurationReader;
import org.ant4eclipse.lib.platform.model.resource.variable.EclipseStringSubstitutionService;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;

public class ExecuteLauncherTask extends AbstractExecuteProjectTask {

  private static final String SCOPE_NAME_LAUNCHER = "forLauncher";

  private File                _launchConfiguration;

  public File getLaunchConfiguration() {
    return this._launchConfiguration;
  }

  public void setLaunchConfiguration(File launchConfiguration) {
    this._launchConfiguration = launchConfiguration;
  }

  public ExecuteLauncherTask() {
    super("executeLauncher");
  }

  @Override
  protected void doExecute() {

    // execute scoped macro definitions
    for (ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      MacroDef macroDef = scopedMacroDefinition.getMacroDef();

      // execute SCOPE_LIBRARY
      if (SCOPE_NAME_LAUNCHER.equals(scopedMacroDefinition.getScope())) {
        executeLauncherScopedMacroDef(macroDef);
      }
      // scope unknown
      else {
        throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_EXECUTION_SCOPE, scopedMacroDefinition.getScope());
      }
    }

  }

  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    // check require fields
    requireWorkspaceAndProjectNameSet();

    if (this._launchConfiguration == null) {
      throw new BuildException("You must specify the 'launchConfiguration' property");
    }

    if (!this._launchConfiguration.exists()) {
      throw new BuildException("The launch configuration file '" + this._launchConfiguration + "' does not exists");
    }

    if (!this._launchConfiguration.isFile()) {
      throw new BuildException("The launch configuration file '" + this._launchConfiguration + "' is not a file");
    }

  }

  private void executeLauncherScopedMacroDef(MacroDef macroDef) {

    LaunchConfigurationReader launchConfigurationReader = ServiceRegistry.instance().getService(
        LaunchConfigurationReader.class);

    System.out.println(" * * * READING LAUNCH CONFIG: " + getLaunchConfiguration());

    final LaunchConfiguration launchConfiguration = launchConfigurationReader
        .readLaunchConfiguration(getLaunchConfiguration());

    final EclipseStringSubstitutionService eclipseVariableResolver = ServiceRegistry.instance().getService(
        EclipseStringSubstitutionService.class);

    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
        final Collection<String> attributeNames = launchConfiguration.getAttributeNames();
        for (String attributeName : attributeNames) {
          String rawAttributeValue = launchConfiguration.getAttribute(attributeName);
          String attributeValue = eclipseVariableResolver.substituteEclipseVariables(rawAttributeValue,
              getEclipseProject(), null);
          System.out.println(" setting '" + attributeName + "' to '" + attributeValue + "'");
          values.getProperties().put(attributeName, attributeValue);
        }
        return values;
      }
    });
  }

  public Object createDynamicElement(String name) throws BuildException {
    // handle 'ForLauncher' element
    if (SCOPE_NAME_LAUNCHER.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_NAME_LAUNCHER);
    }

    // default: not handled
    return null;
  }

}
