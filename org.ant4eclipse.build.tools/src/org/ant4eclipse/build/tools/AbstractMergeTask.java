package org.ant4eclipse.build.tools;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;

public abstract class AbstractMergeTask extends Task {

  /**  */
  private List<ResourceCollection> _resourceCollections;

  /** determines where final properties are saved */
  private File                       _destinationFile;

  /** */
  public AbstractMergeTask() {
    //
    _resourceCollections = new LinkedList<ResourceCollection>();
  }

  /**
   * Add a set of files to copy.
   * 
   * @param set
   *          a set of files to copy.
   */
  public void addFileset(FileSet set) {
    add(set);
  }

  /**
   * Add a collection of files to copy.
   * 
   * @param res
   *          a resource collection to copy.
   * @since Ant 1.7
   */
  public void add(ResourceCollection res) {
    _resourceCollections.add(res);
  }

  public void setDestinationFile(File destinationFile) {
    _destinationFile = destinationFile;
  }
  
  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public List<ResourceCollection> getResourceCollections() {
    return _resourceCollections;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public File getDestinationFile() {
    return _destinationFile;
  }

  public final void execute() throws BuildException {

    validate();

    try {
      doExecute();
    } catch (Exception e) {
      e.printStackTrace();
      throw new BuildException(e.getMessage(), e);
    }
  }

  protected abstract void doExecute() throws Exception;

  /**
   * Validate that the task parameters are valid.
   * 
   * @throws BuildException
   *           if parameters are invalid
   */
  protected void validate() throws BuildException {

    if (!_destinationFile.canWrite()) {
      try {
        _destinationFile.createNewFile();
      } catch (IOException e) {
        throw new BuildException("Unable to write to " + _destinationFile + ".");
      }
    }
  }
}
