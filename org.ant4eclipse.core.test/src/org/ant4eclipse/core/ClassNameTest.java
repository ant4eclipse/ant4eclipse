/**********************************************************************
 * Copyright (c) 2005-2006 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

public class ClassNameTest {

  @Test
  public void test_QualifiedName() {
    final String CLASSNAME = "net.sf.ant4eclipse.ClasspathTask";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    assertNotNull(className);
    assertEquals(CLASSNAME, className.getQualifiedClassName());
    assertEquals(CLASSNAME, className.toString());
    assertEquals("net.sf.ant4eclipse", className.getPackageName());
    assertEquals("ClasspathTask", className.getClassName());
    assertEquals("net/sf/ant4eclipse/ClasspathTask.class", className.asClassFileName());
    assertEquals("net/sf/ant4eclipse/ClasspathTask.java", className.asSourceFileName());
    assertEquals("net/sf/ant4eclipse", className.getPackageAsDirectoryName());
  }

  @Test
  public void test_SinglePackage() {
    final String CLASSNAME = "net.ClasspathTask";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    assertNotNull(className);
    assertEquals(CLASSNAME, className.getQualifiedClassName());
    assertEquals(CLASSNAME, className.toString());
    assertEquals("net", className.getPackageName());
    assertEquals("ClasspathTask", className.getClassName());
    assertEquals("net/ClasspathTask.class", className.asClassFileName());
    assertEquals("net/ClasspathTask.java", className.asSourceFileName());
    assertEquals("net", className.getPackageAsDirectoryName());
  }

  @Test
  public void test_ShortPackage() {
    final String CLASSNAME = "n.ClasspathTask";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    assertNotNull(className);
    assertEquals(CLASSNAME, className.getQualifiedClassName());
    assertEquals(CLASSNAME, className.toString());
    assertEquals("n", className.getPackageName());
    assertEquals("ClasspathTask", className.getClassName());
    assertEquals("n/ClasspathTask.class", className.asClassFileName());
    assertEquals("n/ClasspathTask.java", className.asSourceFileName());
    assertEquals("n", className.getPackageAsDirectoryName());
  }

  @Test
  public void test_ShortClassName() {
    final String CLASSNAME = "n.C";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    assertNotNull(className);
    assertEquals(CLASSNAME, className.getQualifiedClassName());
    assertEquals(CLASSNAME, className.toString());
    assertEquals("n", className.getPackageName());
    assertEquals("C", className.getClassName());
    assertEquals("n/C.class", className.asClassFileName());
    assertEquals("n/C.java", className.asSourceFileName());
  }

  @Test
  public void test_Equals() {
    final String CLASSNAME = "net.sf.ant4eclipse.ClasspathTask";
    ClassName className1 = ClassName.fromQualifiedClassName(CLASSNAME);
    ClassName className2 = ClassName.fromQualifiedClassName(CLASSNAME);
    assertNotSame(className1, className2);

    assertEquals(className1.hashCode(), className2.hashCode());

    assertTrue(className1.equals(className1));
    assertTrue(className2.equals(className2));
    assertTrue(className1.equals(className2));
    assertTrue(className2.equals(className1));

    assertFalse(className1.equals(null));
    assertFalse(className1.equals(new LinkedList()));
    assertFalse(className1.equals(ClassName.fromQualifiedClassName("com.wuetherich.Test")));
    assertFalse(className1.equals(ClassName.fromQualifiedClassName("net.sf.ant4eclipse.ClasspathTaskTest")));
    assertFalse(className1.equals(ClassName.fromQualifiedClassName("com.wuetherich.ClasspathTask")));
  }

  @Test
  public void test_WithoutPackage() {
    final String CLASSNAME = "C";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    assertNotNull(className);
    assertEquals(CLASSNAME, className.getQualifiedClassName());
    assertEquals(CLASSNAME, className.toString());
    assertEquals("", className.getPackageName());
    assertEquals("C", className.getClassName());
    assertEquals("C.class", className.asClassFileName());
    assertEquals("C.java", className.asSourceFileName());
  }
}
