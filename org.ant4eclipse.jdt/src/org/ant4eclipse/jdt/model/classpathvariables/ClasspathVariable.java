package org.ant4eclipse.jdt.model.classpathvariables;

import java.io.File;

/**
 * <p>
 * Encapsulates a class path variable.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClasspathVariable {

  /**
   * <p>
   * Returns the name of the class path variable.
   * </p>
   * 
   * @return the name the name of the class path variable
   */
  public String getName();

  /**
   * <p>
   * Returns the path of the class path variable.
   * </p>
   * 
   * @return the path of the class path variable.
   */
  public File getPath();
}
