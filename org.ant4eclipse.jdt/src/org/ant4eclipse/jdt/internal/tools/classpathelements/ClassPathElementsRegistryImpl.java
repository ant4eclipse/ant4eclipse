package org.ant4eclipse.jdt.internal.tools.classpathelements;

import org.ant4eclipse.jdt.tools.classpathelements.ClassPathContainer;
import org.ant4eclipse.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.ant4eclipse.jdt.tools.classpathelements.ClassPathVariable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Implementation of the {@link ClasspathVariablesRegistry}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassPathElementsRegistryImpl implements ClassPathElementsRegistry {

  /** the class path variables */
  private Map<String, ClassPathVariable>  _classpathVariables;

  /** the class path containers */
  private Map<String, ClassPathContainer> _classpathContainer;

  /**
   * <p>
   * Creates a new instance of type ClasspathVariablesRegistryImpl.
   * </p>
   */
  public ClassPathElementsRegistryImpl() {
    super();

    // create the class path variables map
    this._classpathVariables = new HashMap<String, ClassPathVariable>();

    // create the class path container map
    this._classpathContainer = new HashMap<String, ClassPathContainer>();
  }

  /**
   * {@inheritDoc}
   */
  public List<ClassPathContainer> getClasspathContainer() {
    return new LinkedList<ClassPathContainer>(this._classpathContainer.values());
  }

  /**
   * {@inheritDoc}
   */
  public ClassPathContainer getClassPathContainer(String name) {
    return this._classpathContainer.get(name);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasClassPathContainer(String name) {
    return this._classpathContainer.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  public void registerClassPathContainer(String name, File[] pathEntries) {
    this._classpathContainer.put(name, new ClassPathContainerImpl(name, pathEntries));
  }

  /**
   * {@inheritDoc}
   */
  public ClassPathVariable getClassPathVariable(String name) {
    return this._classpathVariables.get(name);
  }

  /**
   * {@inheritDoc}
   */
  public List<ClassPathVariable> getClasspathVariables() {
    return new LinkedList<ClassPathVariable>(this._classpathVariables.values());
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasClassPathVariable(String name) {
    return this._classpathVariables.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  public void registerClassPathVariable(String name, File path) {
    this._classpathVariables.put(name, new ClasspathVariableImpl(name, path));
  }
}
