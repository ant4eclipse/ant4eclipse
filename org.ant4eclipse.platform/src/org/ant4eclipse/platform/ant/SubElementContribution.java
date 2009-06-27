package org.ant4eclipse.platform.ant;

import org.apache.tools.ant.*;

/**
 * <p>
 * A {@link SubElementContribution} can be used to extend tasks defined in lower layer (e.g. the platform layer) with
 * specific sub elements defined in higher layers (e.g. the JDT layer). To provide a {@link SubElementContribution} one
 * has to implement this interface and a register the implemented class in the
 * <code>org/ant4eclipse/ant4eclipse-configuration.properties</code> file of the implementing project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface SubElementContribution {

  /**
   * <p>
   * Must return <code>true</code> if this {@link SubElementContribution} can handle the sub element with the given name
   * for the given {@link ProjectComponent}.
   * </p>
   * 
   * @param name
   * @param component
   * @return
   */
  boolean canHandleSubElement(String name, ProjectComponent component);

  /**
   * <p>
   * Creates an element with the given name.
   * </p>
   * 
   * @param name
   *          the element name
   * @return the element created
   */
  Object createSubElement(String name, ProjectComponent component);
}
