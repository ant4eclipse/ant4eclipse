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
package org.ant4eclipse.lib.jdt.ecj;

import org.ant4eclipse.lib.core.ClassName;

import java.io.File;

/**
 * <p>
 * {@link ClassFileLoader} instances are used to load {@link ClassFile ClassFiles} that are required during the
 * compilation process.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public interface ClassFileLoader {

  /**
   * <p>
   * Returns all packages that can be loaded from this {@link ClassFileLoader}. The result contains visible packages as
   * well as packages with access restrictions.
   * </p>
   * 
   * @return all packages that can be loaded from this {@link ClassFileLoader}.
   */
  String[] getAllPackages();

  /**
   * <p>
   * This method returns <code>true</code> if {@link ClassFileLoader} has a package with the name (regardless of any
   * visibility restrictions).
   * </p>
   * 
   * @return <code>true</code> if the package is available via this {@link ClassFileLoader}
   */
  boolean hasPackage( String packageName );

  /**
   * <p>
   * Returns an instance of type {@link ClassFile} that represents the specified class or <code>null</code> if no such
   * class can be found.
   * </p>
   * 
   * @param className
   *          The class name of the class that should be loaded.
   * @return an instance of type {@link ClassFile} that represents the specified class or <code>null</code> if the class
   *         is not available.
   */
  ClassFile loadClass( ClassName className );

  /**
   * <p>
   * Returns an instance of type {@link SourceFile} that is the source file for the specified class or <code>null</code>
   * if no such source can be found.
   * </p>
   * 
   * @param className
   *          The class name of the class that should be loaded.
   * @return an instance of type {@link SourceFile} that is the source file for the specified class or <code>null</code>
   *         if no such source can be found.
   */
  ReferableSourceFile loadSource( ClassName className );

  /**
   * Returns a list of File entries describing classpath entries associated with this ClassFileLoader instance. Please
   * note that the list may not be complete if the implementation of this interface provides a class from an unknown
   * source. Each returned File instance has to be canonical.
   * 
   * @return A list of File entries constituting the classpath. Not <code>null</code>
   */
  File[] getClasspath();

} /* ENDINTERFACE */
