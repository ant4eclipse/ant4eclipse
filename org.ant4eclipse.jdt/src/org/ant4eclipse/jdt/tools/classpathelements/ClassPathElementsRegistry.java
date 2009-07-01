package org.ant4eclipse.jdt.tools.classpathelements;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;

import java.io.File;
import java.util.List;

/**
 * <p>
 * The {@link ClassPathElementsRegistry} allows to register class path container and variables:
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClassPathElementsRegistry {

  /**
   * <p>
   * Registers a new class path container with the given name.
   * </p>
   * <p>
   * <b>Note:</b> Registering class path containers manually should be used as the last resort to define class path
   * containers. Take a look at the {@link ClasspathContainerResolver} interface for more information about how to
   * implement 'dynamic' container resolver.
   * </p>
   * 
   * @param name
   * @param pathEntries
   */
  public void registerClassPathContainer(String name, File[] pathEntries);

  /**
   * <p>
   * Returns <code>true</code> if a class path container with the given name exists.
   * </p>
   * <p>
   * <b>Note:</b> Registering class path containers manually should be used as the last resort to define class path
   * containers. Take a look at the {@link ClasspathContainerResolver} interface for more information about how to
   * implement 'dynamic' container resolver.
   * </p>
   * 
   * @param name
   *          <code>true</code> if a class path container with the given name exists.
   */
  public boolean hasClassPathContainer(String name);

  /**
   * <p>
   * Returns the class path container with the given name or <code>null</code>.
   * </p>
   * <p>
   * <b>Note:</b> Registering class path containers manually should be used as the last resort to define class path
   * containers. Take a look at the {@link ClasspathContainerResolver} interface for more information about how to
   * implement 'dynamic' container resolver.
   * </p>
   * 
   * @param name
   * @return
   */
  public ClassPathContainer getClassPathContainer(String name);

  /**
   * <p>
   * Returns a list with all defined class path containers.
   * </p>
   * <p>
   * <b>Note:</b> Registering class path containers manually should be used as the last resort to define class path
   * containers. Take a look at the {@link ClasspathContainerResolver} interface for more information about how to
   * implement 'dynamic' container resolver.
   * </p>
   * 
   * @return a list with all defined class path containers.
   */
  public List<ClassPathContainer> getClasspathContainer();

  /**
   * <p>
   * Registers a new class path variable with the given name.
   * </p>
   * 
   * @param name
   *          the name of the class path variable.
   * @param path
   *          the path
   */
  public void registerClassPathVariable(String name, File path);

  /**
   * <p>
   * Returns <code>true</code> if a class path variable with the given name exists.
   * </p>
   * 
   * @param name
   *          the name of the class path variable.
   * @return <code>true</code> if a class path variable with the given name exists.
   */
  public boolean hasClassPathVariable(String name);

  /**
   * <p>
   * Returns the class path variable with the given name or <code>null</code>.
   * </p>
   * 
   * @param name
   *          the name of the class path variable.
   * @return the class path variable with the given name or <code>null</code>.
   */
  public ClassPathVariable getClassPathVariable(String name);

  /**
   * <p>
   * Returns a list with all defined class path variables.
   * </p>
   * 
   * @return a list with all defined class path variables.
   */
  public List<ClassPathVariable> getClasspathVariables();

  /**
   * <p>
   * Helper class to fetch the {@link ClassPathElementsRegistry} instance from the {@link ServiceRegistry}.
   * </p>
   */
  static class Helper {

    /**
     * <p>
     * Fetches the {@link ClassPathElementsRegistry} instance from the {@link ServiceRegistry}.
     * </p>
     * 
     * @return the registered {@link ClassPathElementsRegistry}
     */
    public static ClassPathElementsRegistry getRegistry() {
      return (ClassPathElementsRegistry) ServiceRegistry.instance().getService(
          ClassPathElementsRegistry.class.getName());
    }
  }
}
