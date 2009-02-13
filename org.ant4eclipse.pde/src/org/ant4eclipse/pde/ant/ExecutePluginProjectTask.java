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

import org.ant4eclipse.jdt.ant.CompilerArguments;
import org.ant4eclipse.jdt.ant.ExecuteJdtProjectTask;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.pde.tools.PdeBuildHelper;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.apache.tools.ant.taskdefs.MacroDef;
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

    // // "calculate" effective version, that is the version with replaced qualifier
    // final Version effectiveVersion = PdeBuildHelper.resolveVersion(getPluginDescriptor().getVersion(),
    // this._buildProperties.getQualifier());
    //
    // // pass effectiveVErsion's qualifier instead of build.properties qualifier to make sure
    // // the value will be used that was calculated just before
    // this._pluginDirectory = PdeBuildHelper.getExistingPluginDestDirectory(getDestDirectory(), getPluginDescriptor()
    // .getSymbolicName(), effectiveVersion);

    // execute scoped macro definitions
    if (SCOPE_LIBRARY.equals(scopedMacroDefinition.getScope())) {
      executeLibraryScopedMacroDef(scopedMacroDefinition.getMacroDef());
      return true;
    } else {
      return false;
    }
    // copy resources
    // exclude the Library entries as they are already copied by the LibraryBuilder
    // final List includes = new LinkedList();
    // for (int i = 0; i < this._buildProperties.getBinaryIncludes().length; i++) {
    // final String include = this._buildProperties.getBinaryIncludes()[i];
    // if (!this._buildProperties.hasLibrary(include)) {
    // includes.add(include);
    // }
    // }
    //
    // final boolean copyResources = this._pluginBuildListener.preCopyResources(getPluginDescriptor(), (String[])
    // includes
    // .toArray(new String[0]), this._buildProperties.getBinaryExcludes(), this._pluginDirectory);
    // if (copyResources) {
    // this._pluginResourceBuilder.buildResources(getPluginDescriptor(), (String[]) includes.toArray(new String[0]),
    // this._buildProperties.getBinaryExcludes(), this._pluginDirectory);
    // this._pluginBuildListener.postCopyResources(getPluginDescriptor(), (String[]) includes.toArray(new String[0]),
    // this._buildProperties.getBinaryExcludes(), this._pluginDirectory);
    // } else {
    // A4ELogging.debug("Copying resources of '%s' has been skipped", this._bundleDescription.getSymbolicName());
    // }
    //
    // if (getPluginDescriptor().getVersion().getQualifier().length() > 0) {
    // fixManifest(this._pluginDirectory, effectiveVersion);
    // }
  }

  private void executeLibraryScopedMacroDef(MacroDef macroDef) {

    // 2. Get libraries
    final PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(getEclipseProject());
    final PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();
    final Library[] libraries = pluginBuildProperties.getOrderedLibraries();

    for (Library library : libraries) {

      final MacroExecutionValues executionValues = new MacroExecutionValues();

      executionValues.getProperties().put("library.name", library.getName());

      if (library.isSelf()) {
        executionValues.getProperties().put("library.isSelf", "true");
      }

      CompilerArguments compilerArguments = getExecutorValuesProvider().provideExecutorValues(getJavaProjectRole(),
          getJdtClasspathContainerArguments(), executionValues);

      File[] sourceFiles = getEclipseProject().getChildren(library.getSource());
      File[] outputFiles = getEclipseProject().getChildren(library.getOutput());

      executionValues.getProperties().put("source.directories", this.convertToString(sourceFiles));
      executionValues.getProperties().put("output.directories", this.convertToString(outputFiles));

      executionValues.getReferences().put("source.directories.path", this.convertToPath(sourceFiles));
      executionValues.getReferences().put("output.directories.path", this.convertToPath(outputFiles));

      for (final String sourceFolderName : library.getSource()) {
        final String outputFolderName = getJavaProjectRole().getOutputFolderForSourceFolder(sourceFolderName);
        final File sourceFolder = getEclipseProject().getChild(sourceFolderName);
        final File outputFolder = getEclipseProject().getChild(outputFolderName);
        compilerArguments.addSourceFolder(sourceFolder, outputFolder);
      }

      executeMacroInstance(macroDef, executionValues);
    }
  }
}
