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
package org.ant4eclipse.jdt.internal.tools.ejc.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ClassName;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFile;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoader;

/**
 * <p>
 * Implementation of a class path based {@link ClassFileLoader}. An instance of this class contains an array of files
 * (jar files or directories) where the class file loader searches for classes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathClassFileLoaderImpl implements ClassFileLoader {

  /** the class path entries */
  private File[]                       _classpathEntries;

  /** the source */
  private File                         _location;

  /** the type of the associated bundle (PROJECT or LIBRARY) */
  private byte                         _type;

  /** maps packages to package providers */
  private Map<String, PackageProvider> _allPackages;

  public ClasspathClassFileLoaderImpl(final File entry, final byte type) {
    Assert.exists(entry);

    this._location = entry;
    this._type = type;

    // initialize
    initialize(new File[] { entry });
  }

  /**
   * <p>
   * Creates a new instance of type {@link FilteredClasspathClassFileLoader}.
   * </p>
   * 
   * @param location
   * @param type
   * @param classpathEntries
   */
  public ClasspathClassFileLoaderImpl(final File location, final byte type, final File[] classpathEntries) {
    Assert.exists(location);

    this._location = location;
    this._type = type;

    // initialize
    initialize(classpathEntries);
  }

  /**
   * <p>
   * Creates a new instance of type {@link FilteredClasspathClassFileLoader}.
   * </p>
   */
  protected ClasspathClassFileLoaderImpl() {
    // nothing to do here...
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFileLoader#hasPackage(java.lang.String)
   */
  public boolean hasPackage(final String packageName) {
    return this._allPackages.containsKey(packageName);
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFileLoader#getAllPackages()
   */
  public String[] getAllPackages() {
    final Set<String> keys = this._allPackages.keySet();
    return keys.toArray(new String[0]);
  }

  /**
   * <p>
   * </p>
   * 
   * @param location
   */
  protected void setLocation(final File location) {
    this._location = location;
  }

  /**
   * <p>
   * </p>
   * 
   * @param type
   */
  protected void setType(final byte type) {
    this._type = type;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected File getLocation() {
    return this._location;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected byte getType() {
    return this._type;
  }

  /**
   * <p>
   * Returns all class path entries of this {@link ClassFileLoader}.
   * </p>
   * 
   * @return all class path entries of this {@link ClassFileLoader}.
   */
  protected File[] getClasspathEntries() {
    return this._classpathEntries;
  }

  /**
   * <p>
   * Initializes this class file loader.
   * </p>
   * 
   * @param classpathEntries
   *          the class path entries.
   */
  protected void initialize(final File[] classpathEntries) {

    // assert not null
    Assert.notNull(classpathEntries);

    // assert that each entry exists
    for (final File classpathEntrie : classpathEntries) {
      Assert.exists(classpathEntrie);
    }

    // assign class path entries
    this._classpathEntries = classpathEntries;

    // create allPackages hash map
    this._allPackages = new HashMap<String, PackageProvider>();

    // add all existing packages to the hash map
    for (final File file : this._classpathEntries) {
      if (file.isDirectory()) {
        final String[] allPackages = getAllPackagesFromDirectory(file);
        addAllPackages(allPackages, file);
      } else {
        try {
          final String[] allPackages = getAllPackagesFromJar(file);
          addAllPackages(allPackages, file);
        } catch (final IOException e) {
          // TODO....
          e.printStackTrace();
        }
      }
    }

    // trace
    // System.out.println("Packages: " + this._allPackages.keySet());
  }

  /**
   * <p>
   * Returns a new {@link PackageProvider}. This method returns a {@link PackageProvider} of type
   * {@link PackageProvider}.
   * </p>
   * <p>
   * You can override this method to provide your own {@link PackageProvider} implementation.
   * </p>
   * 
   * @param classpathEntry
   * @return
   */
  protected PackageProvider newPackageProvider() {
    return new PackageProvider();
  }

  /**
   * <p>
   * </p>
   * 
   * @param packageName
   * @return
   */
  protected final PackageProvider getPackageProvider(final String packageName) {
    return this._allPackages.get(packageName);
  }

  /**
   * @param allPackages
   * @param jar
   */
  private void addAllPackages(final String[] allPackages, final File jar) {

    for (final String aPackage : allPackages) {
      if (this._allPackages.containsKey(aPackage)) {
        final PackageProvider provider = this._allPackages.get(aPackage);
        provider.addClasspathEntry(jar);
      } else {
        final PackageProvider provider = newPackageProvider();
        provider.addClasspathEntry(jar);
        this._allPackages.put(aPackage, provider);
      }
    }
  }

  /**
   * <p>
   * Returns all the names of the packages that are contained in the specified jar file. The package list contains the
   * packages that contain classes as well as all parent packages of those.
   * </p>
   * 
   * @param jar
   * @return
   * @throws IOException
   */
  private String[] getAllPackagesFromJar(final File jar) throws IOException {
    Assert.isFile(jar);

    // prepare result...
    final List<String> result = new LinkedList<String>();

    // create the jarFile wrapper...
    final JarFile jarFile = new JarFile(jar);

    // Iterate over entries...
    final Enumeration<?> enumeration = jarFile.entries();
    while (enumeration.hasMoreElements()) {
      final JarEntry jarEntry = (JarEntry) enumeration.nextElement();

      // add package for each found directory...
      String directoryName = null;

      // if the jar entry is a directory, the directory name is the name of the jar entry...
      if (jarEntry.isDirectory()) {
        directoryName = jarEntry.getName();
      }
      // otherwise the directory name has to be computed
      else {
        final int splitIndex = jarEntry.getName().lastIndexOf('/');
        if (splitIndex != -1) {
          directoryName = jarEntry.getName().substring(0, splitIndex);
        }
      }

      // directoryName can be null if a top level entry is processed
      if (directoryName != null) {
        // convert path to package name
        String packageName = directoryName.replace('/', '.');
        packageName = packageName.endsWith(".") ? packageName.substring(0, packageName.length() - 1) : packageName;

        // at package with all the parent packages (!) to the result list
        final String[] packages = allPackages(packageName);
        for (int i = 0; i < packages.length; i++) {
          if (!result.contains(packages[i])) {
            result.add(packages[i]);
          }
        }
      }
    }

    // return result...
    return result.toArray(new String[0]);

  }

  /**
   * <p>
   * Returns all package names (including parent package names) for the specified package.
   * </p>
   * <p>
   * <b>Example:</b><br/> Given the package name <code>net.sf.ant4eclipse.tools</code> this method will return {"net",
   * "net.sf", "net.sf.ant4eclipse", "net.sf.ant4eclipse.tools"}.
   * </p>
   * 
   * @param packageName
   *          the name of the package.
   * @return all package names (including parent package names) for the specified package.
   */
  private String[] allPackages(final String packageName) {

    // split the package name
    final StringTokenizer tokenizer = new StringTokenizer(packageName, ".");

    // declare result
    final String[] result = new String[tokenizer.countTokens()];

    // compute result
    for (int i = 0; i < result.length; i++) {
      if (i == 0) {
        result[i] = tokenizer.nextToken();
      } else {
        result[i] = result[i - 1] + "." + tokenizer.nextToken();
      }
    }

    // return result
    return result;
  }

  /**
   * @param directory
   * @return
   */
  private String[] getAllPackagesFromDirectory(final File directory) {

    final List<String> result = new LinkedList<String>();

    final File[] children = directory.listFiles(new FileFilter() {
      public boolean accept(final File pathname) {
        return pathname.isDirectory();
      }
    });

    for (final File element : children) {
      getAllPackagesFromDirectory(null, element, result);
    }

    return result.toArray(new String[0]);
  }

  /**
   * @param prefix
   * @param directory
   * @param result
   */
  private void getAllPackagesFromDirectory(final String prefix, final File directory, final List<String> result) {

    final String newPrefix = prefix == null ? "" : prefix + ".";

    result.add(newPrefix + directory.getName());

    final File[] children = directory.listFiles(new FileFilter() {
      public boolean accept(final File pathname) {
        return pathname.isDirectory();
      }
    });

    for (final File element : children) {
      getAllPackagesFromDirectory(newPrefix + directory.getName(), element, result);
    }
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.internal.FilteredClasspathClassFileLoader#loadClass(net.sf.ant4eclipse.tools.core.ejc.loader.ClassName
   *      )
   */
  public ClassFile loadClass(final ClassName className) {

    if (!hasPackage(className.getPackageName())) {
      return null;
    }

    return getPackageProvider(className.getPackageName()).loadClassFile(className);
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ClasspathClassFileLoader:");
    buffer.append(" { ");
    for (int i0 = 0; (this._classpathEntries != null) && (i0 < this._classpathEntries.length); i0++) {
      buffer.append(" _classpathEntries[" + i0 + "]: ");
      buffer.append(this._classpathEntries[i0]);
    }
    buffer.append(" } ");
    buffer.append(" _location: ");
    buffer.append(this._location);
    // buffer.append(" _type: ");
    // buffer.append(this._type);
    // buffer.append(" _allPackages: ");
    // buffer.append(this._allPackages);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * PackageProvider --
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class PackageProvider {

    /** the class path entries */
    private final List<File> _classpathEntries;

    /**
     * <p>
     * Creates a new instance of type {@link PackageProvider}.
     * </p>
     */
    public PackageProvider() {
      this._classpathEntries = new LinkedList<File>();
    }

    /**
     * @param classpathEntry
     */
    public void addClasspathEntry(final File classpathEntry) {
      Assert.exists(classpathEntry);

      this._classpathEntries.add(classpathEntry);
    }

    /**
     * @param className
     * @return
     */
    public ClassFile loadClassFile(final ClassName className) {

      for (final File file : this._classpathEntries) {
        final File classpathEntry = file;

        if (classpathEntry.isDirectory()) {
          final File result = new File(classpathEntry, className.asClassFileName());
          if (result.exists()) {
            return new FileClassFileImpl(result, classpathEntry.getAbsolutePath(),
                ClasspathClassFileLoaderImpl.this._type);
          }
        } else {
          try {
            final JarFile jarFile = new JarFile(classpathEntry);

            final JarEntry entry = jarFile.getJarEntry(className.asClassFileName());

            if ((entry != null)) {
              return new JarClassFileImpl(className.asClassFileName(), jarFile, classpathEntry.getAbsolutePath(),
                  ClasspathClassFileLoaderImpl.this._type);
            }
          } catch (final IOException e) {
            // nothing to do here...
          }
        }
      }
      return null;
    }
  }
}
