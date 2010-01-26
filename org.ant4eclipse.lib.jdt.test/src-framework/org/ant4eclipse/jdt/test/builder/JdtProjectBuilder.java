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

import static java.lang.String.format;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.jdt.model.project.JavaProjectRole;

import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;
import org.ant4eclipse.platform.test.builder.StringTemplate;

import org.ant4eclipse.lib.core.ClassName;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Adds JDT-specific features to {@link EclipseProjectBuilder}
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class JdtProjectBuilder extends EclipseProjectBuilder {

  private List<String>               _classpathEntries;

  /**
   * Holds all SourceClasses (grouped by their source folders) that should be added to this project.
   * 
   * <ul>
   * <li>Key: the source folder
   * <li>Value: {@link SourceClasses} the SourceClasses in the source folder
   * </ul>
   */
  private Map<String, SourceClasses> _sourceClasses;

  /**
   * Returns a "pre-configured" {@link JdtProjectBuilder}, that already has set:
   * <ul>
   * <li>a java builder</li>
   * <li>the java nature
   * <li>
   * <li>the JRE container classpath entry</li>
   * <li>a source folder (<tt>src</tt>)</li>
   * <li>a default output folder (<tt>bin</tt>)</li>
   * </ul>
   * 
   * The builder returned can be used to further customize the project
   * 
   * @param projectName
   *          The name of the project
   * @return the pre-configured JdtEclipseProjectBuilder
   */
  public static JdtProjectBuilder getPreConfiguredJdtBuilder(String projectName) {
    return new JdtProjectBuilder(projectName).withJreContainerClasspathEntry().withSrcClasspathEntry("src", false)
        .withOutputClasspathEntry("bin");
  }

  /**
   * Constructs a new JdtEclipseProjectBuilder instance with the project name.
   * 
   * <p>
   * The constructor adds a {@link #withJavaBuilder() javaBuilder} and a {@link #withJavaNature() javaNature} to the
   * project
   * 
   * @param projectName
   */
  public JdtProjectBuilder(String projectName) {
    super(projectName);

    this._sourceClasses = new Hashtable<String, SourceClasses>();
    this._classpathEntries = new LinkedList<String>();

    // All JDT-Projects have a java builder and a java nature
    withJavaNature();
    withJavaBuilder();
  }

  /**
   * Adds a {@link SourceClass} to this project
   * 
   * @param sourceFolder
   *          the sourcefolder for this class
   * @param className
   *          The full qualified classname for this class
   * @return the {@link SourceClass} instance
   */
  public SourceClass withSourceClass(String sourceFolder, String className) {
    SourceClass sourceClass = new SourceClass(this, className);
    SourceClasses sourceClasses = this._sourceClasses.get(sourceFolder);
    if (sourceClasses == null) {
      sourceClasses = new SourceClasses();
      this._sourceClasses.put(sourceFolder, sourceClasses);
    }
    sourceClasses.addSourceClass(sourceClass);
    return sourceClass;
  }

  protected JdtProjectBuilder withJavaBuilder() {
    return (JdtProjectBuilder) withBuilder("org.eclipse.jdt.core.javabuilder");
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
  public JdtProjectBuilder withClasspathEntry(String entry) {
    assertNotNull(entry);

    this._classpathEntries.add(entry);
    return this;
  }

  protected JdtProjectBuilder withJavaNature() {
    return (JdtProjectBuilder) withNature(JavaProjectRole.JAVA_NATURE);
  }

  public JdtProjectBuilder withSrcClasspathEntry(String path, boolean exported) {
    String line = format("<classpathentry kind='src' path='%s' exported='%s'/>", path, Boolean.valueOf(exported));
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withSrcClasspathEntry(String path, String output, boolean exported) {
    String line = format("<classpathentry kind='src' path='%s' output='%s' exported='%s'/>", path, output, Boolean
        .valueOf(exported));
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withContainerClasspathEntry(String path) {
    String line = format("<classpathentry kind='con' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withVarClasspathEntry(String path) {
    String line = format("<classpathentry kind='var' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  public JdtProjectBuilder withOutputClasspathEntry(String path) {
    String line = format(" <classpathentry kind='output' path='%s'/>", path);
    return withClasspathEntry(line);
  }

  /**
   * Adds a JRE_CONTAINER to the classpath
   * 
   * @return
   */
  public JdtProjectBuilder withJreContainerClasspathEntry() {
    return withClasspathEntry("<classpathentry kind='con' path='org.eclipse.jdt.launching.JRE_CONTAINER'/>");
  }

  /**
   * Adds a JRE_CONTAINER with the given containerName to the classpath
   * 
   * @return
   */
  public JdtProjectBuilder withJreContainerClasspathEntry(String containerName) {
    return withClasspathEntry(format("<classpathentry kind='con' path='org.eclipse.jdt.launching.JRE_CONTAINER/%s'/>",
        containerName));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createArtefacts(File projectDir) {
    super.createArtefacts(projectDir);

    createClasspathFile(projectDir);

    for (Map.Entry<String, SourceClasses> entry : this._sourceClasses.entrySet()) {
      String sourceFolder = entry.getKey();
      List<SourceClass> sourceClasses = entry.getValue().getSourceClasses();
      for (SourceClass sourceClass : sourceClasses) {
        createSourceClass(projectDir, sourceFolder, sourceClass);
      }
    }
  }

  protected void createSourceClass(File projectDir, String sourceFolder, SourceClass sourceClass) {
    File sourceDir = new File(projectDir, sourceFolder);
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
    try {
      sourcefile.createNewFile();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    StringTemplate classTemplate = new StringTemplate();
    classTemplate.append("package ${packageName};").nl().append("public class ${className} {").nl().append(
        sourceClass.generateUsageCode()).nl().append("} // end of class").nl();

    classTemplate.replace("packageName", className.getPackageName());
    classTemplate.replace("className", className.getClassName());

    Utilities.writeFile(sourcefile, classTemplate.toString(), Utilities.ENCODING);
  }

  protected void createClasspathFile(File projectDir) {
    StringBuilder dotClasspath = new StringBuilder();
    dotClasspath.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(Utilities.NL).append("<classpath>")
        .append(Utilities.NL);

    for (String entry : this._classpathEntries) {
      dotClasspath.append(entry).append(Utilities.NL);
    }
    dotClasspath.append("</classpath>").append(Utilities.NL);

    File dotClasspathFile = new File(projectDir, ".classpath");
    Utilities.writeFile(dotClasspathFile, dotClasspath.toString(), "UTF-8");
  }

  /**
   * Holds a list of {@link SourceClass SourceClasses}
   * 
   * @author Nils Hartmann (nils@nilshartmann.net)
   */
  class SourceClasses {
    private List<SourceClass> _sourceClasses;

    public SourceClasses() {
      this._sourceClasses = new LinkedList<SourceClass>();
    }

    public void addSourceClass(SourceClass sourceClass) {
      this._sourceClasses.add(sourceClass);
    }

    public List<SourceClass> getSourceClasses() {
      return this._sourceClasses;
    }
  }

}
