package org.ant4eclipse.jdt.ecj.internal.tools.loader;

import org.ant4eclipse.core.ClassName;

import org.ant4eclipse.jdt.ecj.ClassFile;
import org.ant4eclipse.jdt.ecj.ClassFileLoader;
import org.ant4eclipse.jdt.ecj.internal.tools.ModifiableClassFile;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.AccessRule;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class FilteringClassFileLoader implements ClassFileLoader {

  /** the class file loader that should be filtered */
  private ClassFileLoader _classFileLoader;

  /** the filter string */
  private String          _filter;

  /** the include patterns */
  private List<String>    _includes;

  /** the exclude patterns */
  private List<String>    _excludes;

  /**
   * <p>
   * </p>
   * 
   * @param classFileLoader
   * @param filter
   */
  public FilteringClassFileLoader(ClassFileLoader classFileLoader, String filter) {

    // TODO ASSERT

    this._classFileLoader = classFileLoader;
    this._filter = filter;

    this._includes = new LinkedList<String>();
    this._excludes = new LinkedList<String>();

    init();
  }

  /**
   * @see org.ant4eclipse.jdt.ecj.ClassFileLoader#getAllPackages()
   */
  public String[] getAllPackages() {
    return this._classFileLoader.getAllPackages();
  }

  /**
   * @see org.ant4eclipse.jdt.ecj.ClassFileLoader#hasPackage(java.lang.String)
   */
  public boolean hasPackage(String packageName) {
    return this._classFileLoader.hasPackage(packageName);
  }

  /**
   * @see org.ant4eclipse.jdt.ecj.ClassFileLoader#loadClass(org.ant4eclipse.core.ClassName)
   */
  public ClassFile loadClass(ClassName className) {

    ClassFile result = this._classFileLoader.loadClass(className);

    String classFileName = className.asClassFileName();

    for (String includePattern : this._includes) {
      if (classFileName.matches(includePattern)) {
        return result;
      }
    }

    for (String exludePattern : this._excludes) {
      if (classFileName.matches(exludePattern)) {

        if (result instanceof ModifiableClassFile) {
          AccessRestriction accessRestriction = new AccessRestriction(new AccessRule("**".toCharArray(),
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
    String[] parts = this._filter.split(";");
    for (String part : parts) {

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
