package org.ant4eclipse.platform.tools;

import java.util.List;
import java.util.Properties;

import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
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
  public List<EclipseProject> resolveReferencedProjects(final EclipseProject project, final Properties properties);
}
