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
package org.ant4eclipse.jdt.test.builder;


import org.ant4eclipse.lib.core.ClassName;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.testframework.StringTemplate;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a dummy Java-Source file. This can be used to generate Source-File to test the compilation process.
 * 
 * <p>
 * Use {@link JdtProjectBuilder#withSourceClass(String)} to create a Source class. The creation of a Sourceclass must be
 * finished with {@link #finishClass()} which returns the original EclipseProjectCreator.
 * 
 * <p>
 * Complete Example:
 * </p>
 * <code>
 * 
 * new JdtProjectBuilder("my.project")
 *     withJavaNature(). // make this a java project
 *     withSourceClass("a.A").finishClass() // create class a.A
 *     withSourceClass("b.B").withClassUsed("a.A").finishClass() // create b.B that used a.A
 *     create(); // create the project including the source files
 *
 * </code>
 * 
 * 
 * 
 * @author Nils Hartmann <nils@nilshartmann.net>
 * @version $Revision: 1.5 $
 */
public class SourceClass {

  private String            _className;

  private List<String>      _usedClasses;

  private JdtProjectBuilder _eclipseProjectCreator;

  public SourceClass(JdtProjectBuilder eclipseProjectCreator, String className) {
    this._eclipseProjectCreator = eclipseProjectCreator;
    this._usedClasses = new LinkedList<String>();
    this._className = className;
  }

  public SourceClass withClassUsed(String className) {
    this._usedClasses.add(className);
    return this;
  }

  public JdtProjectBuilder finishClass() {
    return this._eclipseProjectCreator;
  }

  public String getClassName() {
    return this._className;
  }

  /**
   * Generates a Source-Snippet that uses all used classed
   * 
   * @return
   */
  String generateUsageCode() {
    StringBuffer source = new StringBuffer();
    Iterator<String> it = this._usedClasses.iterator();
    while (it.hasNext()) {
      String usedClassName = it.next().toString();
      source.append(asSource(ClassName.fromQualifiedClassName(usedClassName))).append(Utilities.NL);
    }
    return source.toString();
  }

  private String asSource(ClassName qualifiedClassName) {
    StringTemplate source = new StringTemplate();
    source.append("private static ${className} _${attributeName};").nl().append(
        "public static ${className} get${attributeName}() {").nl().append("  return _${attributeName};").nl().append(
        "}").nl().append("public static void set${attributeName} (${className} value) {").nl().append(
        "  _${attributeName} = value;").nl().append("}");
    source.replace("className", qualifiedClassName.getQualifiedClassName());
    String attributeName = qualifiedClassName.getQualifiedClassName().replace('.', '_');
    attributeName = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
    source.replace("attributeName", attributeName);
    return source.toString();
  }

}