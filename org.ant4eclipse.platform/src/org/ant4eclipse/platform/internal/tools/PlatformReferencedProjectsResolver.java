package org.ant4eclipse.platform.internal.tools;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PlatformReferencedProjectsResolver implements ReferencedProjectsResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canHandle(EclipseProject project) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements) {

    // get referenced projects
    EclipseProject[] eclipseProjects = project.getWorkspace().getProjects(project.getReferencedProjects(), false);

    // copy to result
    List<EclipseProject> result = new LinkedList<EclipseProject>();
    for (EclipseProject eclipseProject : eclipseProjects) {
      result.add(eclipseProject);
    }

    // return result
    return result;
  }
}
