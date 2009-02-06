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


public class ExecutePluginProjectTask extends AbstractPdeBuildTask {

//  /** the execute build delegate */
//  private final ExecuteBuildDelegate _executeBuildDelegate;
//
//  private String                     _executeLibraryTarget;
//
//  public ExecutePluginProjectTask() {
//    this._executeBuildDelegate = new ExecuteBuildDelegate(this);
//  }
//
//  public void addParam(final Property property) {
//    this._executeBuildDelegate.addParam(property);
//  }
//
//  public void addReference(final Reference reference) {
//    this._executeBuildDelegate.addReference(reference);
//  }
//
//  public void setPrefix(final String prefix) {
//    this._executeBuildDelegate.setPrefix(prefix);
//  }
//
//  public void setExecuteLibraryTarget(final String executeLibraryTarget) {
//    this._executeLibraryTarget = executeLibraryTarget;
//  }
//
//  public void execute() throws BuildException {
//
//    // 1. Check required fields
//    requireWorkspaceAndProjectNameOrProjectSet();
//    ensurePluginProject();
//
//    // 2. Get libraries
//    final PluginProjectRole pluginProjectRole = PluginProjectRole.getPluginProjectRole(getEclipseProject());
//    final PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();
//    final Library[] libraries = pluginBuildProperties.getOrderedLibraries();
//
//    // // "calculate" effective version, that is the version with replaced qualifier
//    // final Version effectiveVersion = PdeBuildHelper.resolveVersion(getPluginDescriptor().getVersion(),
//    // this._buildProperties.getQualifier());
//    //
//    // // pass effectiveVErsion's qualifier instead of build.properties qualifier to make sure
//    // // the value will be used that was calculated just before
//    // this._pluginDirectory = PdeBuildHelper.getExistingPluginDestDirectory(getDestDirectory(), getPluginDescriptor()
//    // .getSymbolicName(), effectiveVersion);
//
//    // 3. Create call targets
//    final List antCallTargets = new LinkedList();
//
//    // 3.1 Create call target 'PreExecuteLibraries'
//    // antCallTargets.add(createPreExecuteFeaturePluginsCallTarget());
//
//    // 3.2 Create call targets 'ExecutePlugin'
//    for (int i = 0; i < libraries.length; i++) {
//      final CallTarget target = createExecuteLibraryCallTarget(libraries[i]);
//      antCallTargets.add(target);
//    }
//
//    // 3.3 Create call targets 'PostExecuteLibraries'
//    // antCallTargets.add(createPostExecuteFeaturePluginsCallTarget());
//
//    // 4. Execute call targets
//    this._executeBuildDelegate.executeSequential(antCallTargets);
//
//    // copy resources
//    // exclude the Library entries as they are already copied by the LibraryBuilder
//    // final List includes = new LinkedList();
//    // for (int i = 0; i < this._buildProperties.getBinaryIncludes().length; i++) {
//    // final String include = this._buildProperties.getBinaryIncludes()[i];
//    // if (!this._buildProperties.hasLibrary(include)) {
//    // includes.add(include);
//    // }
//    // }
//    //
//    // final boolean copyResources = this._pluginBuildListener.preCopyResources(getPluginDescriptor(), (String[])
//    // includes
//    // .toArray(new String[0]), this._buildProperties.getBinaryExcludes(), this._pluginDirectory);
//    // if (copyResources) {
//    // this._pluginResourceBuilder.buildResources(getPluginDescriptor(), (String[]) includes.toArray(new String[0]),
//    // this._buildProperties.getBinaryExcludes(), this._pluginDirectory);
//    // this._pluginBuildListener.postCopyResources(getPluginDescriptor(), (String[]) includes.toArray(new String[0]),
//    // this._buildProperties.getBinaryExcludes(), this._pluginDirectory);
//    // } else {
//    // A4ELogging.debug("Copying resources of '%s' has been skipped", this._bundleDescription.getSymbolicName());
//    // }
//    //
//    // if (getPluginDescriptor().getVersion().getQualifier().length() > 0) {
//    // fixManifest(this._pluginDirectory, effectiveVersion);
//    // }
//  }
//
//  private CallTarget createExecuteLibraryCallTarget(final Library library) {
//    Assert.notNull(library);
//
//    final Map parameters = new HashMap();
//
//    parameters.put("library.name", library.getName());
//
//    if (library.getOutput() != null) {
//      parameters.put("library.output", arrayAsString(library.getOutput(), ","));
//    }
//
//    if (library.getSource() != null) {
//      parameters.put("library.source", arrayAsString(library.getSource(), ","));
//    }
//
//    return this._executeBuildDelegate.createCallTarget(this.getOwningTarget(), this._executeLibraryTarget, true, true,
//        parameters);
//  }
//
//  /**
//   * <p>
//   * </p>
//   * 
//   * @param objects
//   * @param delimiter
//   * @return
//   */
//  private String arrayAsString(final Object[] objects, final String delimiter) {
//    final StringBuffer buffer = new StringBuffer();
//
//    for (int i = 0; i < objects.length; i++) {
//      buffer.append(objects[i]);
//      if (i < objects.length - 1) {
//        buffer.append(delimiter);
//      }
//    }
//
//    return buffer.toString();
//  }
}
