package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * TODO
 * 
 * @author nils
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class PlatformEclipseVariableResolver implements EclipseVariableResolver {

  /**
   * TODO implement
   */
  // Assure.notNull( "resolvedVariables", resolvedVariables );
  // Assure.notNull( "eclipseProject", eclipseProject );
  @Override
  public void getResolvedVariables( StringMap resolvedVariables, EclipseProject eclipseProject ) {
    resolvedVariables.put( "build_project", eclipseProject.getFolder().getAbsolutePath() );
    resolvedVariables.put( "build_type", "full" );
    resolvedVariables.put( "project_loc", eclipseProject.getFolder().getAbsolutePath() );
    resolvedVariables.put( "project_name", eclipseProject.getSpecifiedName() );
    resolvedVariables.put( "project_path", eclipseProject.getFolderName() );
    // TODO !!
    resolvedVariables.put( "workspace_loc", eclipseProject.getFolder().getParent() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }

} /* ENDCLASS */
