package org.ant4eclipse.jdt.tools.classpathelements;

import java.io.File;

/**
 * <p>
 * Encapsulates a class path variable. A class path variable can be added to a project's class path. It can be used to
 * define the location of a folder or a JAR file that isn't part of the workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClassPathVariable extends ClassPathElement {

  /**
   * <p>
   * Returns the path of the class path variable.
   * </p>
   * 
   * @return the path of the class path variable.
   */
  public File getPath();
}
