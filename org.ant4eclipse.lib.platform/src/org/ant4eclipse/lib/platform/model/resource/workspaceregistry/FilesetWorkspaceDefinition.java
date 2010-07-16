package org.ant4eclipse.lib.platform.model.resource.workspaceregistry;

import org.ant4eclipse.lib.core.Assure;

import java.io.File;

/**
 * <p>
 * Very simple WorkspaceDefinition implementation which simply maintains an array of project directories. The
 * directories are applied at instantiation by a task that registers the workspace.
 * </p>
 * 
 * @author mriley
 */
public class FilesetWorkspaceDefinition implements WorkspaceDefinition {

  /** the set of project directories */
  private final File[] directories;

  /**
   * <p>
   * Creates a new instance of type {@link FilesetWorkspaceDefinition}.
   * </p>
   * 
   * @param directories
   */
  public FilesetWorkspaceDefinition(File[] directories) {
    Assure.notNull("directories", directories);

    this.directories = directories;
  }

  /**
   * {@inheritDoc}
   */
  public File[] getProjectFolders() {
    return this.directories;
  }
}
