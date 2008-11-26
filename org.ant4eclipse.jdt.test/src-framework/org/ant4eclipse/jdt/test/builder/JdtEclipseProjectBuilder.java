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
package org.ant4eclipse.jdt.test.builder;

import static java.lang.String.format;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.ClassName;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.platform.test.builder.FileHelper;
import org.ant4eclipse.platform.test.builder.StringTemplate;
import org.ant4eclipse.platform.test.builder.builder.EclipseProjectBuilder;

/**
 * Adds JDT-specific features to {@link EclipseProjectBuilder}
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class JdtEclipseProjectBuilder extends EclipseProjectBuilder {

  private final List<String>      _classpathEntries;

  private final List<SourceClass> _sourceClasses;

  /**
   * @param projectName
   */
  public JdtEclipseProjectBuilder(String projectName) {
    super(projectName);

    this._sourceClasses = new LinkedList<SourceClass>();
    this._classpathEntries = new LinkedList<String>();
  }

  public SourceClass withSourceClass(String className) {
    SourceClass sourceClass = new SourceClass(this, className);
    _sourceClasses.add(sourceClass);
    return sourceClass;
  }

  public JdtEclipseProjectBuilder withJavaBuilder() {
    return (JdtEclipseProjectBuilder) withBuilder("org.eclipse.jdt.core.javabuilder");
  }

  /**
   * Adds <tt>entry</tt> to the list of classpath entries for this project.
   * 
   * <p>
   * the classpath entry must be a well formatted line of xml, that is valid in a <tt>.classpath</tt> file.
   * 
   * @param entry
   *          The xml-entry
   * @return this
   */
  public JdtEclipseProjectBuilder withClasspathEntry(String entry) {
    assertNotNull(entry);

    _classpathEntries.add(entry);
    return this;
  }

  public JdtEclipseProjectBuilder withJavaNature() {
    return (JdtEclipseProjectBuilder) withNature(JavaProjectRole.JAVA_NATURE);
  }

  public JdtEclipseProjectBuilder withSrcClasspathEntry(String path, boolean exported) {
    String line = format("<classpathentry kind='src' path='%s' exported='%s'/>", path, exported);
    return withClasspathEntry(line);
  }

  public JdtEclipseProjectBuilder withSrcClasspathEntry(String path, String output, boolean exported) {
    String line = format("<classpathentry kind='src' path='%s' output='%s' exported='%s'/>", path, output, exported);
    return withClasspathEntry(line);
  }

  public JdtEclipseProjectBuilder withContainerClasspathEntry(String path) {
    String line = format("<classpathentry kind='con' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  public JdtEclipseProjectBuilder withVarClasspathEntry(String path) {
    String line = format("<classpathentry kind='var' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  public JdtEclipseProjectBuilder withOutputClasspathEntry(String path) {
    String line = format(" <classpathentry kind='output' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  /**
   * 
   * @return
   */
  public JdtEclipseProjectBuilder withJreContainerClasspathEntry() {
    return withClasspathEntry("<classpathentry kind='con' path='org.eclipse.jdt.launching.JRE_CONTAINER'/>");
  }

  @Override
  protected void createArtefacts(File projectDir) throws Exception {
    super.createArtefacts(projectDir);

    createClasspathFile(projectDir);

    Iterator<SourceClass> it = _sourceClasses.iterator();
    while (it.hasNext()) {
      SourceClass sourceClass = it.next();
      createSourceClass(projectDir, sourceClass);
    }
  }

  protected void createSourceClass(File projectDir, SourceClass sourceClass) throws Exception {
    File sourceDir = new File(projectDir, "source");
    if (!sourceDir.exists()) {
      sourceDir.mkdirs();
    }
    assertTrue(sourceDir.isDirectory());

    ClassName className = ClassName.fromQualifiedClassName(sourceClass.getClassName());
    File packageDir = new File(sourceDir, className.getPackageAsDirectoryName());
    if (!packageDir.exists()) {
      packageDir.mkdirs();
    }
    assertTrue(packageDir.isDirectory());

    File sourcefile = new File(sourceDir, className.asSourceFileName());
    sourcefile.createNewFile();

    StringTemplate classTemplate = new StringTemplate();
    classTemplate.append("package ${packageName};").nl().append("public class ${className} {").nl().append(
        sourceClass.generateUsageCode()).nl().append("} // end of class").nl();

    classTemplate.replace("packageName", className.getPackageName());
    classTemplate.replace("className", className.getClassName());

    FileHelper.createFile(sourcefile, classTemplate.toString());
  }

  protected void createClasspathFile(File projectDir) throws Exception {
    final StringBuilder dotClasspath = new StringBuilder();
    dotClasspath.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(NL).append("<classpath>").append(NL);

    for (String entry : _classpathEntries) {
      dotClasspath.append(entry).append(NL);
    }
    dotClasspath.append("</classpath>").append(NL);

    File dotClasspathFile = new File(projectDir, ".classpath");
    FileHelper.createFile(dotClasspathFile, dotClasspath.toString());
  }

}
