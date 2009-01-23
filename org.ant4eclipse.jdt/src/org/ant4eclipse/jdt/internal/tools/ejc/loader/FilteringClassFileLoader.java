package org.ant4eclipse.jdt.internal.tools.ejc.loader;

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.ClassName;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFile;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoader;
import org.ant4eclipse.jdt.tools.ejc.loader.ModifiableClassFile;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.AccessRule;

/**
 * <p>
 * </p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class FilteringClassFileLoader implements ClassFileLoader {

  /** the class file loader that should be filtered */
  private final ClassFileLoader _classFileLoader;

  /** the filter string */
  private final String          _filter;

  /** the include patterns */
  private final List<String>    _includes;

  /** the exclude patterns */
  private final List<String>    _excludes;

  /**
   * <p>
   * </p>
   * 
   * @param classFileLoader
   * @param filter
   */
  public FilteringClassFileLoader(final ClassFileLoader classFileLoader, final String filter) {

    // TODO ASSERT

    this._classFileLoader = classFileLoader;
    this._filter = filter;

    this._includes = new LinkedList<String>();
    this._excludes = new LinkedList<String>();

    init();
  }

  /**
   * @see org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoader#getAllPackages()
   */
  public String[] getAllPackages() {
    return this._classFileLoader.getAllPackages();
  }

  /**
   * @see org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoader#hasPackage(java.lang.String)
   */
  public boolean hasPackage(final String packageName) {
    return this._classFileLoader.hasPackage(packageName);
  }

  /**
   * @see org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoader#loadClass(org.ant4eclipse.core.ClassName)
   */
  public ClassFile loadClass(final ClassName className) {

    final ClassFile result = this._classFileLoader.loadClass(className);

    final String classFileName = className.asClassFileName();

    for (final String includePattern : this._includes) {
      if (classFileName.matches(includePattern)) {
        return result;
      }
    }

    for (final String exludePattern : this._excludes) {
      if (classFileName.matches(exludePattern)) {

        if (result instanceof ModifiableClassFile) {
          final AccessRestriction accessRestriction = new AccessRestriction(new AccessRule("**".toCharArray(),
              IProblem.ForbiddenReference), result.getLibraryType(), result.getLibraryLocation());

          ((ModifiableClassFile) result).setAccessRestriction(accessRestriction);
        }
        return result;
      }
    }

    // TODO?? DEFAULT?
    return result;
  }

  /**
   * <p>
   * </p>
   */
  private void init() {
    final String[] parts = this._filter.split(";");
    for (final String part : parts) {

      // step 1: replace all occurrences of '**/*' with '###' (temporary step)
      String transformedPart = part.substring(1).replaceAll("\\*\\*/\\*", "###");

      // step 2: replace all occurrences of '*' with '[^\.]*'
      transformedPart = transformedPart.replaceAll("\\*", "[^\\\\.]*");

      // step 3: replace all occurrences of '###' (formally '**/*') with '.*'
      transformedPart = transformedPart.replaceAll("###", ".*");

      // step 4: append '\.class'
      transformedPart = transformedPart.concat("\\.class");

      // System.out.println(part.substring(1) + " -> " + transformedPart);

      if (part.startsWith("+")) {
        this._includes.add(transformedPart);
      }
      if (part.startsWith("-")) {
        this._excludes.add(transformedPart);
      }
    }
  }

}
