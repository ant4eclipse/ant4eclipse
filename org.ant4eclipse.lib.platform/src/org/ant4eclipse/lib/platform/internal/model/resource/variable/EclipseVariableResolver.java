package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * TODO
 * 
 * @author nils
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 * 
 */
public interface EclipseVariableResolver extends A4EService {

  void getResolvedVariables( StringMap resolvedVariables, EclipseProject eclipseProject );

} /* ENDINTERFACE */
