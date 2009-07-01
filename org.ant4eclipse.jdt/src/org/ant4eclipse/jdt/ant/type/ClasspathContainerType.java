package org.ant4eclipse.jdt.ant.type;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.jdt.tools.classpathelements.ClassPathElementsRegistry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import java.io.File;

/**
 * <p>
 * Ant type to define class path variables. A class path variable can be added to a project's class path. It can be used
 * to define the location of a JAR file or a directory that isn't part of the workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathContainerType extends AbstractAnt4EclipseDataType {

  /**
   * <p>
   * Creates a new instance of type ClasspathVariables.
   * </p>
   * 
   * @param project
   */
  public ClasspathContainerType(final Project project) {
    super(project);
  }

  /**
   * <p>
   * Adds a configured {@link ClasspathVariableType}.
   * </p>
   * 
   * @param classpathVariable
   *          the {@link ClasspathVariableType}.
   */
  public void addConfiguredClasspathVariable(final ClasspathVariableType classpathVariable) {

    // assert path set
    if (classpathVariable.getPath() == null) {
      // TODO: NLS
      throw new BuildException("Missing parameter 'path' on classpathVariable!");
    }

    // assert name set
    if (!Utilities.hasText(classpathVariable.getPath().getName())) {
      // TODO: NLS
      throw new BuildException("Missing parameter 'name' on classpathVariable!");
    }

    final ClassPathElementsRegistry variablesRegistry = ClassPathElementsRegistry.Helper.getRegistry();

    // TODO: what to do if classpathVariable already registered?
    variablesRegistry.registerClassPathVariable(classpathVariable.getName(), classpathVariable.getPath());
  }

  /**
   * <p>
   * Creates the entry for a class path variable.
   * </p>
   * 
   * @return an entry for a class path variable.
   */
  public ClasspathVariableType createClasspathVariable() {
    return (new ClasspathVariableType());
  }

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class ClasspathVariableType {

    /** the name of the class path variable */
    private String _name;

    /** the path of this class path variable */
    private File   _path;

    /**
     * <p>
     * Returns the name of the class path variable.
     * </p>
     * 
     * @return the name the name of the class path variable
     */
    public String getName() {
      return this._name;
    }

    /**
     * <p>
     * Sets the name of the class path variable.
     * </p>
     * 
     * @param name
     *          the name to set
     */
    public void setName(final String name) {
      this._name = name;
    }

    /**
     * <p>
     * Returns the path of the class path variable.
     * </p>
     * 
     * @return the path of the class path variable.
     */
    public File getPath() {
      return this._path;
    }

    /**
     * <p>
     * Sets the path of the class path variable.
     * </p>
     * 
     * @param path
     *          the path of the class path variable.
     */
    public void setPath(final File path) {
      this._path = path;
    }
  }
}
