package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * TODO
 * 
 * @author nils
 * 
 */
public interface EclipseVariableResolver {

  public void getResolvedVariables(StringMap resolvedVariables, EclipseProject eclipseProject);

}
