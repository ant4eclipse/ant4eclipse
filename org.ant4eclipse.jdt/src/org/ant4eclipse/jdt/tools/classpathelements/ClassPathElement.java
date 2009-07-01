package org.ant4eclipse.jdt.tools.classpathelements;

/**
 * <p>
 * Defines the common interface for a class path element that must be resolved during the resolution process.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClassPathElement {

  /**
   * <p>
   * Returns the name of the class path container.
   * </p>
   * 
   * @return the name the name of the class path container
   */
  public String getName();
}
