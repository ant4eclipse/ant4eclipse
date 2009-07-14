/**********************************************************************
 * Copyright (c) 2005-2007 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.pde.ant;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.ant4eclipse.jdt.ant.CompilerArguments;
import org.ant4eclipse.jdt.ant.ExecuteJdtProjectTask;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.pde.tools.PdeBuildHelper;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;

import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.types.FileList;
import org.osgi.framework.Version;

public class ExecutePluginProjectTask extends ExecuteJdtProjectTask implements TargetPlatformAwareComponent {

  private TargetPlatformAwareDelegate _targetPlatformAwareDelegate;

  public static final String          SCOPE_LIBRARY = "SCOPE_LIBRARY";

  public ExecutePluginProjectTask() {
    super("executePluginProject");

    _targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();
  }

  public final String getTargetPlatformId() {
    return _targetPlatformAwareDelegate.getTargetPlatformId();
  }

  public final boolean isTargetPlatformId() {
    return _targetPlatformAwareDelegate.isTargetPlatformId();
  }

  public final void setTargetPlatformId(String targetPlatformId) {
    _targetPlatformAwareDelegate.setTargetPlatformId(targetPlatformId);
  }

  @Override
  protected void addAdditionalExecutionValues(MacroExecutionValues executionValues) {
    final PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());

    // "calculate" effective version, that is the version with replaced qualifier
    final Version effectiveVersion = PdeBuildHelper.resolveVersion(pluginProjectRole.getBundleDescription()
        .getVersion(), pluginProjectRole.getBuildProperties().getQualifier());

    // TODO
    executionValues.getProperties().put("bundle.effective.version", effectiveVersion.toString());
    executionValues.getProperties().put("bundle.version",
        pluginProjectRole.getBundleDescription().getVersion().toString());

    PluginBuildProperties buildProperties = pluginProjectRole.getBuildProperties();
    executionValues.getProperties()
        .put("build.properties.binary.includes", buildProperties.getBinaryIncludesAsString());
    executionValues.getProperties()
        .put("build.properties.binary.excludes", buildProperties.getBinaryExcludesAsString());
  }

  protected Object onCreateDynamicElement(final String name) {

    if ("ForEachLibrary".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_LIBRARY);
    }

    return null;
  }

  @Override
  protected void doExecute() {

    // TODO: CHECK!
    JdtClasspathContainerArgument containerArgument = createJdtClasspathContainerArgument();
    containerArgument.setKey("target.platform");
    containerArgument.setValue(getTargetPlatformId());

    super.doExecute();
  }

  @Override
  protected boolean onExecuteScopeMacroDefintion(ScopedMacroDefinition<String> scopedMacroDefinition) {

    // 1. Check required fields
    requireWorkspaceAndProjectNameSet();
    ensureRole(PluginProjectRole.class);

    // execute scoped macro definitions
    if (SCOPE_LIBRARY.equals(scopedMacroDefinition.getScope())) {
      executeLibraryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      return true;
    } else {
      return false;
    }

  }

  private void executeLibraryScopedMacroDef(MacroDef macroDef) {

    // 2. Get libraries
    final PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());
    final PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();
    final Library[] libraries = pluginBuildProperties.getOrderedLibraries();
    final List<String> binaryIncludes = Arrays.asList(pluginBuildProperties.getBinaryIncludes());

    for (final Library library : libraries) {

      if (binaryIncludes.contains(library.getName())) {

        executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

          public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {


            values.getProperties().put("library.name", library.getName());

            if (library.isSelf()) {
              values.getProperties().put("library.isSelf", "true");

              computeBinaryIncludeFilelist();
            }

            CompilerArguments compilerArguments = getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(),
                getJdtClasspathContainerArguments(), values);

            File[] sourceFiles = getEclipseProject().getChildren(library.getSource());
            File[] outputFiles = getEclipseProject().getChildren(library.getOutput());

            values.getProperties().put("source.directories", convertToString(sourceFiles));
            values.getProperties().put("output.directories", convertToString(outputFiles));

            values.getReferences().put("source.directories.path", convertToPath(sourceFiles));
            values.getReferences().put("output.directories.path", convertToPath(outputFiles));

            for (final String sourceFolderName : library.getSource()) {
              final String outputFolderName = getJavaProjectRole().getOutputFolderForSourceFolder(sourceFolderName);
              final File sourceFolder = getEclipseProject().getChild(sourceFolderName);
              final File outputFolder = getEclipseProject().getChild(outputFolderName);
              compilerArguments.addSourceFolder(sourceFolder, outputFolder);
            }

            addAdditionalExecutionValues(values);

            return values;
          }
        });
      }
    }
  }

  private void computeBinaryIncludeFilelist() {
    // TODO
    final PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());
    final PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();
    final Library[] libraries = pluginBuildProperties.getOrderedLibraries();
    final List<String> binaryIncludes = Arrays.asList(pluginBuildProperties.getBinaryIncludes());

    FileList fileList = new FileList();
    fileList.setDir(getEclipseProject().getFolder());

  }
}
