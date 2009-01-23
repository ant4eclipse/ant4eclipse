package org.ant4eclipse.jdt.internal.tools;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.internal.tools.classpathentry.ClasspathEntryResolver;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * 
 */
public class ClasspathEntryResolverExecutor {

  /** stack of 'current projects' */
  private final Stack<EclipseProject> _currentProject;

  /** list with all projects that already have been parsed */
  private final List<EclipseProject>  _referencedProjects;

  /** array that contains all resolvers for raw class path entries * */
  private ClasspathEntryResolver[]    _entryResolvers;

  /** - */
  private ClasspathResolverContext    _resolverContext;

  /** - */
  private final boolean               _failOnNonHandledEntry;

  /**
   * 
   */
  public ClasspathEntryResolverExecutor(final boolean failOnNonHandledEntry) {
    this._referencedProjects = new LinkedList<EclipseProject>();
    this._currentProject = new Stack<EclipseProject>();

    this._failOnNonHandledEntry = failOnNonHandledEntry;
  }

  /**
   * {@inheritDoc}
   */
  public final EclipseProject getCurrentProject() {
    try {
      return this._currentProject.peek();
    } catch (final EmptyStackException e) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasCurrentProject() {
    return !this._currentProject.empty();
  }

  /**
   * @return
   */
  public List<EclipseProject> getReferencedProjects() {
    return this._referencedProjects;
  }

  /**
   * @param job
   * @return
   */
  public final void resolve(final EclipseProject rootProject, final ClasspathEntryResolver[] classpathEntryResolvers,
      final ClasspathResolverContext classpathResolverContext) {

    // Initialize the ProjectClasspathResolver instance
    this._referencedProjects.clear();
    this._currentProject.clear();

    this._entryResolvers = classpathEntryResolvers;
    this._resolverContext = classpathResolverContext;

    // Initialize Entry Resolvers
    for (final ClasspathEntryResolver entryResolver : this._entryResolvers) {
      if (entryResolver instanceof Lifecycle) {
        ((Lifecycle) entryResolver).initialize();
      }
    }

    // resolve the class path
    resolveReferencedProject(rootProject);

    // Dispose Entry Resolvers
    for (final ClasspathEntryResolver entryResolver : this._entryResolvers) {
      if (entryResolver instanceof Lifecycle) {
        ((Lifecycle) entryResolver).dispose();
      }
    }
  }

  /**
   * @param project
   */
  public final void resolveReferencedProject(final EclipseProject project) {
    Assert.notNull(project);

    if (this._currentProject.contains(project)) {
      // TODO it should be configurable if the task fails on circular
      // dependencies
      // TODO detect which projects reference each other
      A4ELogging.warn("Circular dependency detected! Project: '%s'", new String[] { project.getFolderName() });
      return;
    }

    if (this._referencedProjects.contains(project)) {
      return;
    }

    this._referencedProjects.add(project);
    this._currentProject.push(project);

    final JavaProjectRole javaProjectRole = JavaProjectRole.Helper.getJavaProjectRole(project);
    Assert.assertTrue(javaProjectRole.hasRawClasspathEntries(), "");
    final RawClasspathEntry[] eclipseClasspathEntries = javaProjectRole.getRawClasspathEntries();
    resolveClasspathEntries(eclipseClasspathEntries);

    this._currentProject.pop();
  }

  private void resolveClasspathEntries(final ClasspathEntry[] classpathEntries) {
    for (final ClasspathEntry classpathEntrie : classpathEntries) {
      try {
        resolveClasspathEntry(classpathEntrie);
      } catch (final Exception e) {
        // TODO
        e.printStackTrace();
        throw new RuntimeException("Exception whilst resolving the classpath ", e);
      }
    }
  }

  /**
   * <p>
   * Resolves a eclipse class path entry.
   * </p>
   * 
   * @param entry
   *          the class path entry to resolve.
   */
  private final void resolveClasspathEntry(final ClasspathEntry entry) {
    Assert.notNull(entry);

    // initialize handled
    boolean handled = false;

    // iterate over all the entry resolvers
    for (final ClasspathEntryResolver entryResolver : this._entryResolvers) {
      if (entryResolver.canResolve(entry)) {
        handled = true;
        entryResolver.resolve(entry, this._resolverContext);
        break;
      }
    }

    // if the entry is not handled, we have to throw an exception here
    if (!handled && this._failOnNonHandledEntry) {
      // TODO
      throw new RuntimeException("Unsupported Entrykind!" + entry);
    }
  }
}
