package org.ant4eclipse.jdt.tools.classpathelements;

/**
 * <p>
 * Defines the common interface for a class path element that must be resolved during the resolution process.
 * </p>
 * <p>
 * This interface is <b>not</b> intended to be implemented by clients.
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
  String getName();
}
