package org.ant4eclipse.jdt.internal.tools.container;

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.classpathelements.ClassPathContainer;
import org.ant4eclipse.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.ant4eclipse.jdt.tools.container.ClasspathContainerResolver;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassPathElementsRegistryResolver implements ClasspathContainerResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canResolveContainer(final ClasspathEntry classpathEntry) {
    return getClassPathElementsRegistry().hasClassPathContainer(classpathEntry.getPath());
  }

  /**
   * {@inheritDoc}
   */
  public void resolveContainer(final ClasspathEntry classpathEntry, final ClasspathResolverContext context) {
    final ClassPathContainer container = getClassPathElementsRegistry().getClassPathContainer(classpathEntry.getPath());
    context.addClasspathEntry(new ResolvedClasspathEntry(container.getPathEntries()));
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private ClassPathElementsRegistry getClassPathElementsRegistry() {
    return ClassPathElementsRegistry.Helper.getRegistry();
  }
}
