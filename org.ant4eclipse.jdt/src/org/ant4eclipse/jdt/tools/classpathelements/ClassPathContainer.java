package org.ant4eclipse.jdt.tools.classpathelements;

import java.io.File;

/**
 * <p>
 * Encapsulates a class path container. A class path container groups several jar files and /or folders that belongs
 * together.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClassPathContainer extends ClassPathElement {

  /**
   * <p>
   * Returns the files that belongs to the class path container.
   * </p>
   * 
   * @return the files that belongs to the class path container.
   */
  public File[] getPathEntries();
}
