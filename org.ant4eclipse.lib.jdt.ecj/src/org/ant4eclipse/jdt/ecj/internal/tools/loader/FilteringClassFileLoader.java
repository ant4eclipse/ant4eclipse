/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.ecj.internal.tools.loader;

import org.ant4eclipse.jdt.ecj.ClassFile;
import org.ant4eclipse.jdt.ecj.ClassFileLoader;
import org.ant4eclipse.jdt.ecj.ReferableSourceFile;
import org.ant4eclipse.jdt.ecj.ReferableType;
import org.ant4eclipse.jdt.ecj.internal.tools.DefaultReferableType;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.ClassName;
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

    Assure.notNull("classFileLoader", classFileLoader);
    Assure.nonEmpty("filter", filter);

    this._classFileLoader = classFileLoader;
    this._filter = filter;

    this._includes = new LinkedList<String>();
    this._excludes = new LinkedList<String>();

    init();
  }

  /**
   * {@inheritDoc}
   */
  public String[] getAllPackages() {
    return this._classFileLoader.getAllPackages();
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasPackage(String packageName) {
    return this._classFileLoader.hasPackage(packageName);
  }

  /**
   * {@inheritDoc}
   */
  public ClassFile loadClass(ClassName className) {

    ClassFile result = this._classFileLoader.loadClass(className);

    setAccessRestrictions(result, className);

    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ReferableSourceFile loadSource(ClassName className) {

    ReferableSourceFile result = this._classFileLoader.loadSource(className);

    setAccessRestrictions(result, className);

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param referableType
   * @return
   */
  private ReferableType setAccessRestrictions(ReferableType referableType, ClassName className) {

    String classFileName = className.asClassFileName();

    for (String includePattern : this._includes) {
      if (classFileName.matches(includePattern)) {
        return referableType;
      }
    }

    for (String exludePattern : this._excludes) {
      if (classFileName.matches(exludePattern)) {

        if (referableType instanceof DefaultReferableType) {

          AccessRestriction accessRestriction = new AccessRestriction(new AccessRule("**".toCharArray(),
              IProblem.ForbiddenReference), referableType.getLibraryType(), referableType.getLibraryLocation());

          ((DefaultReferableType) referableType).setAccessRestriction(accessRestriction);
        }
        return referableType;
      }
    }

    return referableType;
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

      if (part.startsWith("+")) {
        this._includes.add(transformedPart);
      }
      if (part.startsWith("-")) {
        this._excludes.add(transformedPart);
      }
    }
  }

}
