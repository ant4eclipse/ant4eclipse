package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * TODO
 * 
 * @author nils
 * 
 */
public class PlatformEclipseVariableResolver implements EclipseVariableResolver {

  public PlatformEclipseVariableResolver(String key) {
    // 
  }

  /**
   * TODO implement
   */
  @Override
  public void getResolvedVariables(StringMap resolvedVariables, EclipseProject eclipseProject) {
    Assure.notNull("resolvedVariables", resolvedVariables);
    Assure.notNull("eclipseProject", eclipseProject);

    resolvedVariables.put("build_project", eclipseProject.getFolder().getAbsolutePath());
    resolvedVariables.put("build_type", "full");
    resolvedVariables.put("project_loc", eclipseProject.getFolder().getAbsolutePath());
    resolvedVariables.put("project_name", eclipseProject.getSpecifiedName());
    resolvedVariables.put("project_path", eclipseProject.getFolderName());
    // TODO !!
    resolvedVariables.put("workspace_loc", eclipseProject.getFolder().getParent());
  }

}
