package org.ant4eclipse.ant.jdt;

import java.io.File;
import java.util.List;

import org.ant4eclipse.ant.platform.core.task.AbstractAnt4EclipseFileSet;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * A {@link ResourceCollection} that includes all contents of source and/or output folders of a jdt project
 * 
 * @author nils
 * 
 */
public class JdtProjectFileSet extends AbstractAnt4EclipseFileSet {

  private boolean _includeSourceFolders = false;

  private boolean _includeOutputFolders = true;

  public boolean isIncludeSourceFolders() {
    return this._includeSourceFolders;
  }

  /**
   * Set to true if contents of source folders should be included in the fileset
   * 
   * @param includeSourceFolders
   */
  public void setIncludeSourceFolders(boolean includeSourceFolders) {
    this._includeSourceFolders = includeSourceFolders;
  }

  public boolean isIncludeOutputFolders() {
    return this._includeOutputFolders;
  }

  /**
   * Set to true if contents of output folders should be included in the fileset.
   * 
   * @param includeSourceFolders
   */
  public void setIncludeOutputFolders(boolean includeOutputFolders) {
    this._includeOutputFolders = includeOutputFolders;
  }

  public JdtProjectFileSet(Project project) {
    super(project);
  }

  @Override
  protected void doComputeFileSet(List<Resource> resourceList) {

    if (!(isIncludeSourceFolders() || isIncludeOutputFolders())) {
      A4ELogging.warn("Neither output nor source folders are included in the fileset.");
      return;
    }

    JavaProjectRole javaProjectRole = getEclipseProject().getRole(JavaProjectRole.class);

    // include output folder contents
    if (isIncludeOutputFolders()) {
      A4ELogging.trace("Adding output folders to file set");
      final String[] allOutputFolders = javaProjectRole.getAllOutputFolders();
      for (String outputFolder : allOutputFolders) {
        addFolderContent(resourceList, outputFolder);
      }
    }

    // include source folder contents
    if (isIncludeSourceFolders()) {
      A4ELogging.trace("Adding source folders to file set");
      final String[] sourceFolders = javaProjectRole.getSourceFolders();
      for (String sourceFolder : sourceFolders) {
        addFolderContent(resourceList, sourceFolder);
      }
    }

  }

  /**
   * adds the content of the specified project folder to the resourceList
   * 
   * @param resourceList
   * @param folder
   */
  protected void addFolderContent(List<Resource> resourceList, String folder) {
    A4ELogging.trace("adding folder '%s' to resourceList", folder);

    if (!getEclipseProject().hasChild(folder)) {
      A4ELogging.warn("Folder '%s' does not exists in project '%s' - ignored", folder, getEclipseProject()
          .getSpecifiedName());
      return;
    }

    // get the project child with the given name
    File directory = getEclipseProject().getChild(folder);
    if (!directory.isDirectory()) {
      A4ELogging.warn("Folder '%s' in project '%s' is not a directory - ignored", directory, getEclipseProject()
          .getSpecifiedName());
      return;
    }

    DirectoryScanner directoryScanner = new DirectoryScanner();
    directoryScanner.setBasedir(directory);
    directoryScanner.setCaseSensitive(isCaseSensitive());
    directoryScanner.setIncludes(null);
    if (getDefaultexcludes()) {
      directoryScanner.addDefaultExcludes();
    }

    // do the job
    directoryScanner.scan();

    // get the included files and add it to the resource list
    String[] files = directoryScanner.getIncludedFiles();

    // add files to result resourceList
    for (String fileName : files) {
      resourceList.add(new FileResource(directory, fileName));
    }
  }
}
