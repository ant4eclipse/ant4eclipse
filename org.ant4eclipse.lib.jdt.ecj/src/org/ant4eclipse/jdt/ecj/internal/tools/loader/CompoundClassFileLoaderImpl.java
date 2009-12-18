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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.ClassName;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CompoundClassFileLoaderImpl implements ClassFileLoader {

  private ClassFileLoader[]                  _classFileLoaders;

  /** maps packages to a package provider that contains a list of one or more class path entries */
  private Map<String, List<ClassFileLoader>> _allPackages;

  public CompoundClassFileLoaderImpl(ClassFileLoader[] classFileLoaders) {
    Assure.paramNotNull("classFileLoaders", classFileLoaders);

    this._classFileLoaders = classFileLoaders;

    this._allPackages = new HashMap<String, List<ClassFileLoader>>();

    initialise();
  }

  /**
   * {@inheritDoc}
   */
  public String[] getAllPackages() {
    return this._allPackages.keySet().toArray(new String[0]);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasPackage(String packageName) {
    return this._allPackages.containsKey(packageName);
  }

  /**
   * {@inheritDoc}
   */
  public ClassFile loadClass(ClassName className) {

    // get the class file loader list
    List<ClassFileLoader> classFileLoaderList = this._allPackages.get(className.getPackageName());

    // return if class file loader list is null
    if (classFileLoaderList == null) {
      return null;
    }

    // declare the result
    ClassFile result = null;

    // try to find the class file...
    for (ClassFileLoader classFileLoader : classFileLoaderList) {

      // try to load class file...
      ClassFile classFile = classFileLoader.loadClass(className);

      // class file was found...
      if (classFile != null) {

        // if the class file has no access restrictions, return the class file...
        if (!classFile.hasAccessRestriction()) {
          return classFile;
        }
        // else set the class file a result, if result is null
        else if (result == null) {
          result = classFile;
        }
      }
    }

    // return the result
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public ReferableSourceFile loadSource(ClassName className) {

    // TODO: Access restrictions for source files!!

    // if the package name is not in the map of all packages, return immediately
    List<ClassFileLoader> classFileLoaderList = this._allPackages.get(className.getPackageName());
    if (classFileLoaderList == null) {
      return null;
    }

    // search for the source file
    for (ClassFileLoader classFileLoader : classFileLoaderList) {
      ReferableSourceFile sourceFile = classFileLoader.loadSource(className);
      if (sourceFile != null) {
        return sourceFile;
      }
    }

    // last resort: return null
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[CompoundClassFileLoader:");
    buffer.append(" { ");
    for (int i0 = 0; (this._classFileLoaders != null) && (i0 < this._classFileLoaders.length); i0++) {
      buffer.append(" _classFileLoaders[" + i0 + "]: ");
      buffer.append(this._classFileLoaders[i0]);
    }
    buffer.append(" } ");
    // buffer.append(" _allPackages: ");
    // buffer.append(this._allPackages);
    buffer.append("]");
    return buffer.toString();
  }

  private void initialise() {

    for (ClassFileLoader classFileLoader : this._classFileLoaders) {
      String[] packages = classFileLoader.getAllPackages();

      for (String aPackage : packages) {
        if (this._allPackages.containsKey(aPackage)) {
          List<ClassFileLoader> classFileLoaderList = this._allPackages.get(aPackage);
          if (!classFileLoaderList.contains(classFileLoader)) {
            classFileLoaderList.add(classFileLoader);
          }
        } else {
          List<ClassFileLoader> classFileLoaderList = new LinkedList<ClassFileLoader>();
          classFileLoaderList.add(classFileLoader);
          this._allPackages.put(aPackage, classFileLoaderList);
        }
      }
    }
  }
}
