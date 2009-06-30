package org.ant4eclipse.jdt.internal.model.classpathvariables;

import org.ant4eclipse.jdt.model.classpathvariables.ClasspathVariable;
import org.ant4eclipse.jdt.model.classpathvariables.ClasspathVariablesRegistry;

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
public class ClasspathVariablesRegistryImpl implements ClasspathVariablesRegistry {

  /** the class path variables */
  private final Map<String, ClasspathVariable> _classpathVariables;

  /**
   * <p>
   * Creates a new instance of type ClasspathVariablesRegistryImpl.
   * </p>
   */
  public ClasspathVariablesRegistryImpl() {
    super();

    // create the class path variables list
    this._classpathVariables = new HashMap<String, ClasspathVariable>();
  }

  /**
   * {@inheritDoc}
   */
  public ClasspathVariable getClassPathVariable(final String name) {
    return this._classpathVariables.get(name);
  }

  /**
   * {@inheritDoc}
   */
  public List<ClasspathVariable> getClasspathVariables() {
    return new LinkedList<ClasspathVariable>(this._classpathVariables.values());
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasClassPathVariable(final String name) {
    return this._classpathVariables.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  public void registerClassPathVariable(final String name, final File path) {
    this._classpathVariables.put(name, new ClasspathVariableImpl(name, path));
  }
}
