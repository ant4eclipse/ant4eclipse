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
package org.ant4eclipse.jdt.ecj.internal.tools;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ClassName;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.ecj.ClassFile;
import org.ant4eclipse.jdt.ecj.ClassFileLoader;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

/**
 * <p>
 * Adapter class to utilize class file loaders in the eclipse java compiler.
 * </p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class NameEnvironmentImpl implements INameEnvironment {

  /** used to find binary type as requested by the compiler */
  private final ClassFileLoader _classFileLoader;

  /**
   * <p>
   * Create a new instance of type {@link NameEnvironmentImpl}.
   * </p>
   * 
   * @param classFileLoader
   *          the class file loader to use.
   */
  public NameEnvironmentImpl(final ClassFileLoader classFileLoader) {
    Assert.notNull(classFileLoader);

    this._classFileLoader = classFileLoader;
  }

  /**
   * @see org.eclipse.jdt.internal.compiler.env.INameEnvironment#cleanup()
   */
  public void cleanup() {
    // nothing to do here...
  }

  /**
   * @see org.eclipse.jdt.internal.compiler.env.INameEnvironment#findType(char[][])
   */
  public NameEnvironmentAnswer findType(final char[][] compoundTypeName) {

    // convert char array to string(buffer)
    final StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < compoundTypeName.length; i++) {
      buffer.append(new String(compoundTypeName[i]));
      if (i < compoundTypeName.length - 1) {
        buffer.append(".");
      }
    }

    // find class
    final NameEnvironmentAnswer answer = findClass(buffer.toString());
    A4ELogging
        .trace("findType('%s'): %s", new Object[] { asString(compoundTypeName), Boolean.valueOf(answer != null) });
    return answer;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jdt.internal.compiler.env.INameEnvironment#findType(char[], char[][])
   */
  public NameEnvironmentAnswer findType(final char[] typeName, final char[][] packageName) {
    A4ELogging.trace("findType('%s', %s)", new String(typeName), asString(packageName));

    final StringBuffer result = new StringBuffer();
    if (packageName != null) {
      for (final char[] element : packageName) {
        result.append(new String(element)).append(".");
      }
    }
    result.append(new String(typeName));

    return findClass(result.toString());
  }

  /**
   * This method returns true if the given packageName is a valid package.
   * 
   * <p>
   * Note that this method returns true regardless of any OSGi visibility constraints.
   * <p>
   * This method also returns true for top level packages
   * 
   * @see org.eclipse.jdt.internal.compiler.env.INameEnvironment#isPackage(char[][], char[])
   */
  public boolean isPackage(final char[][] parentPackageName, final char[] packageName) {
    A4ELogging.trace("isPackage('%s', %s)", asString(parentPackageName), new String(packageName));

    String qualifiedPackageName = toJavaName(parentPackageName);
    if (qualifiedPackageName.length() > 0) {
      qualifiedPackageName += "." + new String(packageName);
    } else {
      qualifiedPackageName = new String(packageName);
    }

    // this is a three-step check in order to gain performance resp. to avoid
    // the scanning of all jar files

    final boolean packageFound = this._classFileLoader.hasPackage(qualifiedPackageName);

    A4ELogging.trace("isPackage - package '%s' found: %s'", new Object[] { qualifiedPackageName, "" + packageFound });

    return packageFound;
  }

  /**
   * @param className
   * @return
   */
  protected NameEnvironmentAnswer findClass(final String className) {
    Assert.notNull(className);
    return findClass(ClassName.fromQualifiedClassName(className));
  }

  /**
   * <p>
   * Returns a {@link NameEnvironmentAnswer} for the class that is represented by the given {@link ClassName} instance.
   * </p>
   * 
   * @param className
   *          represents the class name
   * @return a {@link NameEnvironmentAnswer}
   */
  protected NameEnvironmentAnswer findClass(final ClassName className) {

    // load class file from class file loader
    final ClassFile classFile = this._classFileLoader.loadClass(className);

    // return new NameEnvironmentAnswer if classFile was found
    if (classFile != null) {
      return new NameEnvironmentAnswer(classFile.getBinaryType(), (classFile.hasAccessRestriction() ? classFile
          .getAccessRestriction() : null));
    }

    // else return null
    return null;
  }

  /**
   * <p>
   * Converts the given char array to a java name (e.g. "net.sf.ant4eclipse").
   * </p>
   * 
   * @param array
   *          the array to convert.
   * @return the
   */
  private static String toJavaName(final char[][] array) {
    final StringBuffer result = new StringBuffer();

    if (array != null) {
      for (int i = 0; i < array.length; i++) {
        result.append(new String(array[i]));
        if (i < array.length - 1) {
          result.append(".");
        }
      }
    }
    return result.toString();
  }

  /**
   * <p>
   * ONLY USED FOR DEBUGGING PURPOSE.
   * </p>
   * 
   * @param array
   * @return
   */
  private static String asString(final char[][] array) {
    // define result
    final StringBuffer result = new StringBuffer();

    // compute result
    if (array != null) {
      for (int i = 0; i < array.length; i++) {
        result.append("{").append(new String(array[i])).append("}");
        if (i < array.length - 1) {
          result.append(",");
        }
      }
    } else {
      result.append("(null)");
    }

    // return result
    return result.toString();
  }
}
