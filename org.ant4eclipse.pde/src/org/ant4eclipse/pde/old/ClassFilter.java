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

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.tools.core.ejc.loader.ClassName;

/**
 *
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassFilter {

  /** the include directive */
  private final String   _includeDirective;

  /** the exclude directive */
  private final String   _excludeDirective;

  private final String[] _includedStrings;

  private final String[] _excludedStrings;

  /**
   * @param includeDirective
   * @param excludeDirective
   */
  public ClassFilter(final String includeDirective, final String excludeDirective) {

    this._includeDirective = includeDirective;

    this._excludeDirective = excludeDirective;

    // set default value according to specification if (includeDirective == null)
    this._includedStrings = includeDirective == null ? new String[] { "*" } : includeDirective.split(",");

    // set default value according to specification if (excludeDirective == null)
    this._excludedStrings = excludeDirective == null ? new String[0] : excludeDirective.split(",");
  }

  /**
   * @return
   */
  public String getIncludeDirective() {
    return this._includeDirective;
  }

  /**
   * @return
   */
  public String getExcludeDirective() {
    return this._excludeDirective;
  }

  /**
   * Tests if the given className is exported by the exportedPackage according to the import and exclude directive of
   * the given exportPackage.
   *
   * @spec "3.6.7 Class Filtering", p. 48
   * @return true if the class is visible or false if the class is not visible
   */
  public boolean isClassVisible(final ClassName className) {
    Assert.notNull(className);

    // check if it's included
    if (!isIncluded(className.getClassName())) {
      // A4ELogging.debug("Class '%s' not visible since not included in exportpackage '%s'", new String[] {
      // className.getQualifiedClassName(), exportPackage.toString() });
      return false;
    }

    // check if it's explicitly excluded
    if (isExcluded(className.getClassName())) {
      // A4ELogging.debug("Class '%s' not visible since it excluded by '%s' from in exportpackage '%s'", new String[] {
      // className.getQualifiedClassName(), excludeMatched, exportPackage.toString() });
      return false;
    }

    return true;
  }

  /**
   * @param name
   * @return
   */
  private boolean isIncluded(final String name) {
    boolean included = false;
    for (int i = 0; i < this._includedStrings.length; i++) {
      final String includedString = this._includedStrings[i].trim();
      if (matches(includedString, name)) {
        included = true;
        break;
      }
    }
    return included;
  }

  private boolean isExcluded(final String name) {
    boolean excluded = false;
    for (int i = 0; i < this._excludedStrings.length; i++) {
      final String excludedString = this._excludedStrings[i].trim();
      if (matches(excludedString, name)) {
        excluded = true;
        break;
      }
    }
    return excluded;
  }

  /**
   * @param osgiPattern
   * @param string
   * @return
   */
  private boolean matches(final String osgiPattern, final String string) {
    Assert.notNull(osgiPattern);
    Assert.notNull(string);

    // replace OSGi wildcards (*) with regular expressions (.+)
    // NOTE: we're interpreting a * as "one or more characters here"
    // otherwise we have to use the regex ".*" which means "zero or more
    // characters here"
    final String regex = osgiPattern.replaceAll("\\*", ".+");

    return string.matches(regex);
  }
}
