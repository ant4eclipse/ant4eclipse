/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.pde.old;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.util.ManifestHelper;
import net.sf.ant4eclipse.tools.core.ejc.loader.ClassFile;
import net.sf.ant4eclipse.tools.core.ejc.loader.ClassFileLoader;
import net.sf.ant4eclipse.tools.core.ejc.loader.ClassName;
import net.sf.ant4eclipse.tools.core.ejc.loader.ModifiableClassFile;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.AccessRule;

/**
 * <p>
 * </p>
 *
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class FilteredClasspathClassFileLoader implements ClassFileLoader {

  /** manifest */
  private final Manifest[]      _manifest;

  /** the class file loader */
  private final ClassFileLoader _classFileLoader;

  /** - */
  private final Map            /* <String, ExportedPackage> */_exportedPackages;

  /**
   * @param manifest
   * @param classFileLoader
   */
  public FilteredClasspathClassFileLoader(final Manifest manifest, final ClassFileLoader classFileLoader) {
    this(new Manifest[] { manifest }, classFileLoader);
  }

  /**
   * @param manifests
   * @param classFileLoader
   */
  public FilteredClasspathClassFileLoader(final Manifest[] manifests, final ClassFileLoader classFileLoader) {
    Assert.notNull(manifests);
    Assert.notNull(classFileLoader);

    // set up
    this._manifest = manifests;
    this._classFileLoader = classFileLoader;
    this._exportedPackages = new HashMap();

    // initialize
    initialize();
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFileLoader#getAllPackages()
   */
  public String[] getAllPackages() {
    return this._classFileLoader.getAllPackages();
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFileLoader#hasPackage(java.lang.String)
   */
  public boolean hasPackage(final String packageName) {
    return this._classFileLoader.hasPackage(packageName);
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.internal.FilteredClasspathClassFileLoader#loadClass(net.sf.ant4eclipse.tools.core.ejc.loader.ClassName
   *      )
   */
  public ClassFile loadClass(final ClassName className) {

    final ModifiableClassFile result = (ModifiableClassFile) this._classFileLoader.loadClass(className);

    if (result == null) {
      return null;
    }

    final ExportedPackage exportedPackage = (ExportedPackage) this._exportedPackages.get(className.getPackageName());

    // package is not exported and therefore is not visible...
    if ((exportedPackage == null)) {

      final AccessRestriction accessRestriction = new AccessRestriction(new AccessRule("**".toCharArray(),
          IProblem.ForbiddenReference), result.getLibraryType(), result.getLibraryLocation());

      result.setAccessRestriction(accessRestriction);
    }

    // package is exported but the requested class is filtered out...
    if ((exportedPackage != null) && exportedPackage.hasFilter()
        && !exportedPackage.getFilter().isClassVisible(className)) {

      final AccessRestriction accessRestriction = new AccessRestriction(new AccessRule("**".toCharArray(),
          IProblem.ForbiddenReference), result.getLibraryType(), result.getLibraryLocation());

      result.setAccessRestriction(accessRestriction);
    }

    return result;
  }

  protected void initialize() {

    // iterate over all manifests
    for (int i = 0; i < this._manifest.length; i++) {

      // get all exported packages
      final ManifestHelper.ManifestHeaderElement[] elements = ManifestHelper.getManifestHeaderElements(
          this._manifest[i], ManifestHelper.EXPORT_PACKAGE);

      // set all the exported packages
      for (int j = 0; j < elements.length; j++) {

        final String include = (String) elements[i].getDirectives().get("include");
        final String exclude = (String) elements[i].getDirectives().get("exclude");

        ExportedPackage exportedPackage = null;

        if ((include != null) || (exclude != null)) {
          exportedPackage = new ExportedPackage(new ClassFilter(include, exclude));
        } else {
          exportedPackage = new ExportedPackage();
        }

        final String[] packages = elements[j].getValues();

        for (int k = 0; k < packages.length; k++) {
          this._exportedPackages.put(packages[k], exportedPackage);
        }
      }
    }
  }

  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[FilteredClasspathClassFileLoader:");
    // buffer.append(" { ");
    // for (int i0 = 0; (this._manifest != null) && (i0 < this._manifest.length); i0++) {
    // buffer.append(" _manifest[" + i0 + "]: ");
    // buffer.append(this._manifest[i0]);
    // }
    // buffer.append(" } ");
    buffer.append(" _classFileLoader: ");
    buffer.append(this._classFileLoader);
    // buffer.append(" _exportedPackages: ");
    // buffer.append(this._exportedPackages);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   *
   * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
   */
  public class ExportedPackage {

    /** the filter - may be null */
    private ClassFilter _filter;

    public ExportedPackage() {
      //
    }

    public ExportedPackage(final ClassFilter filter) {
      this._filter = filter;
    }

    /**
     * <p>
     * Returns the {@link ClassFilter}.
     * </p>
     *
     * @return the {@link ClassFilter}.
     */
    public ClassFilter getFilter() {
      return this._filter;
    }

    /**
     * <p>
     * Returns <code>true</code> if a {@link ClassFilter} has been set, <code>false</code> otherwise.
     * </p>
     *
     * @return <code>true</code> if a {@link ClassFilter} has been set, <code>false</code> otherwise.
     */
    public boolean hasFilter() {
      return this._filter != null;
    }
  }
}
