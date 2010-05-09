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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.ClassName;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class ClassNameTest {

  @Test
  public void qualifiedName() {
    String CLASSNAME = "net.sf.ant4eclipse.ClasspathTask";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    Assert.assertNotNull(className);
    Assert.assertEquals(CLASSNAME, className.getQualifiedClassName());
    Assert.assertEquals(CLASSNAME, className.toString());
    Assert.assertEquals("net.sf.ant4eclipse", className.getPackageName());
    Assert.assertEquals("ClasspathTask", className.getClassName());
    Assert.assertEquals("net/sf/ant4eclipse/ClasspathTask.class", className.asClassFileName());
    Assert.assertEquals("net/sf/ant4eclipse/ClasspathTask.java", className.asSourceFileName());
    Assert.assertEquals("net/sf/ant4eclipse", className.getPackageAsDirectoryName());
  }

  @Test
  public void singlePackage() {
    String CLASSNAME = "net.ClasspathTask";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    Assert.assertNotNull(className);
    Assert.assertEquals(CLASSNAME, className.getQualifiedClassName());
    Assert.assertEquals(CLASSNAME, className.toString());
    Assert.assertEquals("net", className.getPackageName());
    Assert.assertEquals("ClasspathTask", className.getClassName());
    Assert.assertEquals("net/ClasspathTask.class", className.asClassFileName());
    Assert.assertEquals("net/ClasspathTask.java", className.asSourceFileName());
    Assert.assertEquals("net", className.getPackageAsDirectoryName());
  }

  @Test
  public void shortPackage() {
    String CLASSNAME = "n.ClasspathTask";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    Assert.assertNotNull(className);
    Assert.assertEquals(CLASSNAME, className.getQualifiedClassName());
    Assert.assertEquals(CLASSNAME, className.toString());
    Assert.assertEquals("n", className.getPackageName());
    Assert.assertEquals("ClasspathTask", className.getClassName());
    Assert.assertEquals("n/ClasspathTask.class", className.asClassFileName());
    Assert.assertEquals("n/ClasspathTask.java", className.asSourceFileName());
    Assert.assertEquals("n", className.getPackageAsDirectoryName());
  }

  @Test
  public void shortClassName() {
    String CLASSNAME = "n.C";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    Assert.assertNotNull(className);
    Assert.assertEquals(CLASSNAME, className.getQualifiedClassName());
    Assert.assertEquals(CLASSNAME, className.toString());
    Assert.assertEquals("n", className.getPackageName());
    Assert.assertEquals("C", className.getClassName());
    Assert.assertEquals("n/C.class", className.asClassFileName());
    Assert.assertEquals("n/C.java", className.asSourceFileName());
  }

  @Test
  public void equalObjects() {
    String CLASSNAME = "net.sf.ant4eclipse.ClasspathTask";
    ClassName className1 = ClassName.fromQualifiedClassName(CLASSNAME);
    ClassName className2 = ClassName.fromQualifiedClassName(CLASSNAME);
    Assert.assertNotSame(className1, className2);

    Assert.assertEquals(className1.hashCode(), className2.hashCode());

    Assert.assertTrue(className1.equals(className1));
    Assert.assertTrue(className2.equals(className2));
    Assert.assertTrue(className1.equals(className2));
    Assert.assertTrue(className2.equals(className1));

    Assert.assertFalse(className1.equals(null));
    Assert.assertFalse(className1.equals(new LinkedList<Object>()));
    Assert.assertFalse(className1.equals(ClassName.fromQualifiedClassName("com.wuetherich.Test")));
    Assert.assertFalse(className1.equals(ClassName.fromQualifiedClassName("net.sf.ant4eclipse.ClasspathTaskTest")));
    Assert.assertFalse(className1.equals(ClassName.fromQualifiedClassName("com.wuetherich.ClasspathTask")));
  }

  @Test
  public void withoutPackage() {
    String CLASSNAME = "C";
    ClassName className = ClassName.fromQualifiedClassName(CLASSNAME);
    Assert.assertNotNull(className);
    Assert.assertEquals(CLASSNAME, className.getQualifiedClassName());
    Assert.assertEquals(CLASSNAME, className.toString());
    Assert.assertEquals("", className.getPackageName());
    Assert.assertEquals("C", className.getClassName());
    Assert.assertEquals("C.class", className.asClassFileName());
    Assert.assertEquals("C.java", className.asSourceFileName());
  }

} /* ENDCLASS */
