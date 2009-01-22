package org.ant4eclipse.platform.tools;

import java.util.List;

import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * <p>
 * A {@link ReferencedProjectsResolver} is used to resolve projects that are (directly) referenced by given project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ReferencedProjectsResolver {

  /**
   * <p>
   * Returns if this {@link ReferencedProjectsResolver} can handle resolve referenced projects for the given project.
   * </p>
   * 
   * @param project
   * @return
   */
  public boolean canHandle(final EclipseProject project);

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @param properties
   * @return
   */
  public List<EclipseProject> resolveReferencedProjects(final EclipseProject project,
      final List<Object> additionalElements);
}
