package org.ant4eclipse.jdt.tools.classpathelements;

import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;

import java.io.File;

/**
 * <p>
 * Encapsulates a class path container. A class path container groups several jar files and /or folders that belongs
 * together. One can create a {@link ClassPathContainer ClassPathContainers} and register it with the
 * {@link ClassPathElementsRegistry} to statically define the content of an eclipse class path container.
 * </p>
 * <p>
 * <b>Note:</b> Registering class path containers manually should be used as the last resort to define class path
 * containers. Take a look at the {@link ClasspathContainerResolver} interface for more information about how to
 * implement 'dynamic' container resolver.
 * </p>
 * <p>
 * This interface is <b>not</b> intended to be implemented by clients.
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
  File[] getPathEntries();
}
