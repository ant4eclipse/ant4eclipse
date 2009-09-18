package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentComponent;
import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgumentDelegate;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import org.ant4eclipse.platform.ant.core.task.AbstractExecuteProjectTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.util.List;

/**
 * <p>
 * Abstract base class for all tasks that allow to iterate over a JDT (or JDT-based) project. This class can be
 * subclassed to implement a custom executor task for specific project types.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractExecuteJdtProjectTask extends AbstractExecuteProjectTask implements
    JdtClasspathContainerArgumentComponent {

  /** the class path container argument delegates */
  private JdtClasspathContainerArgumentDelegate _jdtClasspathContainerArgumentDelegate;

  /** the JDT executor values provider */
  private JdtExecutorValuesProvider             _executorValuesProvider;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractExecuteJdtProjectTask}.
   * </p>
   * 
   * @param prefix
   *          the prefix for all scoped values
   */
  public AbstractExecuteJdtProjectTask(String prefix) {
    super(prefix);

    // create the delegates
    this._jdtClasspathContainerArgumentDelegate = new JdtClasspathContainerArgumentDelegate();

    // create the JdtExecutorValuesProvider
    this._executorValuesProvider = new JdtExecutorValuesProvider(this);
  }

  /**
   * {@inheritDoc}
   */
  public final JdtClasspathContainerArgument createJdtClasspathContainerArgument() {
    return this._jdtClasspathContainerArgumentDelegate.createJdtClasspathContainerArgument();
  }

  /**
   * {@inheritDoc}
   */
  public final List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return this._jdtClasspathContainerArgumentDelegate.getJdtClasspathContainerArguments();
  }

  /**
   * <p>
   * Helper method that returns the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   * </p>
   * 
   * @return the {@link JavaProjectRole} role for the set {@link EclipseProject}.
   */
  protected final JavaProjectRole getJavaProjectRole() {
    return JavaProjectRole.Helper.getJavaProjectRole(getEclipseProject());
  }

  /**
   * <p>
   * Returns the {@link JdtExecutorValuesProvider}.
   * </p>
   * 
   * @return the {@link JdtExecutorValuesProvider}.
   */
  protected final JdtExecutorValuesProvider getExecutorValuesProvider() {
    return this._executorValuesProvider;
  }
}
