package org.ant4eclipse.jdt.model.classpathvariables;

import org.ant4eclipse.core.service.ServiceRegistry;

import java.io.File;
import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ClasspathVariablesRegistry {

  /**
   * <p>
   * </p>
   */
  public void registerClassPathVariable(String name, File path);

  /**
   * <p>
   * </p>
   * 
   * @param name
   */
  public boolean hasClassPathVariable(String name);

  /**
   * <p>
   * </p>
   * 
   * @param name
   * @return
   */
  public ClasspathVariable getClassPathVariable(String name);

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public List<ClasspathVariable> getClasspathVariables();

  /**
   */
  static class Helper {

    /**
     * <p>
     * Fetches the {@link ClasspathVariablesRegistry} instance from the {@link ServiceRegistry}.
     * </p>
     * 
     * @return the registered {@link ClasspathVariablesRegistry}
     */
    public static ClasspathVariablesRegistry getRegistry() {
      return (ClasspathVariablesRegistry) ServiceRegistry.instance().getService(
          ClasspathVariablesRegistry.class.getName());
    }
  }
}
