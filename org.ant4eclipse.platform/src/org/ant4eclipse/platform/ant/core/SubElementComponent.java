package org.ant4eclipse.platform.ant.core;

import org.apache.tools.ant.DynamicElement;

import java.util.List;

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
  List<Object> getSubElements();
}
