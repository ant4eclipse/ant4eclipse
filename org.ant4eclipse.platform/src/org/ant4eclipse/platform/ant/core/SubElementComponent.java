package org.ant4eclipse.platform.ant.core;

import java.util.List;

import org.apache.tools.ant.DynamicElement;

/**
 * <p>
 * Interface for all ant4eclipse tasks, conditions and types that have to deal with dynamic sub elements.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface SubElementComponent extends DynamicElement {

  /**
   * <p>
   * Returns a list with all known sub elements.
   * </p>
   * 
   * @return a list with all known sub elements.
   */
  public List<Object> getSubElements();
}
