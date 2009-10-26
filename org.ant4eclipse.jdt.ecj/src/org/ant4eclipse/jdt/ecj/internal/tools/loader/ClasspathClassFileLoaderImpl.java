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

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ClassName;

import org.ant4eclipse.jdt.ecj.ClassFile;
import org.ant4eclipse.jdt.ecj.ClassFileLoader;
import org.ant4eclipse.jdt.ecj.ReferableSourceFile;
import org.ant4eclipse.jdt.ecj.internal.tools.ReferableSourceFileImpl;

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

  /** the class path entries */
  private File[]                       _sourcepathEntries;

  /** the source */
  private File                         _location;

  /** the type of the associated bundle (PROJECT or LIBRARY) */
  private byte                         _type;

  /** maps packages to package providers */
  private Map<String, PackageProvider> _allPackages;

  /**
   * <p>
   * Creates a new instance of type ClasspathClassFileLoaderImpl.
   * </p>
   * 
   * @param entry
   *          the file entry
   * @param type
   *          type
   */
  public ClasspathClassFileLoaderImpl(File entry, byte type) {
    Assert.notNull(entry);

    this._location = entry;
    this._type = type;

    // initialize
    initialize(new File[] { entry }, new File[] {});
  }

  public ClasspathClassFileLoaderImpl(File classPathEntry, byte type, File sourcePathEntry) {
    Assert.notNull(classPathEntry);
    Assert.notNull(sourcePathEntry);

    this._location = classPathEntry;
    this._type = type;

    // initialize
    initialize(new File[] { classPathEntry }, new File[] { sourcePathEntry });
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
  public ClasspathClassFileLoaderImpl(File location, byte type, File[] classpathEntries) {
    Assert.notNull(location);

    this._location = location;
    this._type = type;

    // initialize
    initialize(classpathEntries, new File[] {});
  }

  public ClasspathClassFileLoaderImpl(File location, byte type, File[] classpathEntries, File[] sourcePathEntries) {
    Assert.notNull(location);

    this._location = location;
    this._type = type;

    // initialize
    initialize(classpathEntries, sourcePathEntries);
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
   * {@inheritDoc}
   */
  public boolean hasPackage(String packageName) {
    return this._allPackages.containsKey(packageName);
  }

  /**
   * {@inheritDoc}
   */
  public String[] getAllPackages() {
    Set<String> keys = this._allPackages.keySet();
    return keys.toArray(new String[0]);
  }

  /**
   * <p>
   * Sets the location.
   * </p>
   * 
   * @param location
   */
  protected void setLocation(File location) {
    this._location = location;
  }

  /**
   * <p>
   * Sets the type.
   * </p>
   * 
   * @param type
   */
  protected void setType(byte type) {
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
  protected void initialize(File[] classpathEntries, File[] sourcepathEntries) {

    // assert not null
    Assert.notNull(classpathEntries);
    Assert.notNull(sourcepathEntries);

    // assert that each entry is not null
    for (File classpathEntrie : classpathEntries) {
      Assert.notNull(classpathEntrie);
    }
    for (File sourcepathEntry : sourcepathEntries) {
      Assert.notNull(sourcepathEntry);
    }

    // assign path entries
    this._classpathEntries = classpathEntries;
    this._sourcepathEntries = sourcepathEntries;

    // create allPackages hash map
    this._allPackages = new HashMap<String, PackageProvider>();

    // add all existing packages to the hash map
    for (File file : this._classpathEntries) {
      if (file.isDirectory()) {
        String[] allPackages = getAllPackagesFromDirectory(file);
        addAllPackagesFromClassPathEntry(allPackages, file);
      } else if (file.isFile()) {
        try {
          String[] allPackages = getAllPackagesFromJar(file);
          addAllPackagesFromClassPathEntry(allPackages, file);
        } catch (IOException e) {
          // TODO....
          e.printStackTrace();
        }
      }
    }

    // add all existing packages to the hash map
    for (File file : this._sourcepathEntries) {
      if (file.isDirectory()) {
        String[] allPackages = getAllPackagesFromDirectory(file);
        addAllPackagesFromSourcePathEntry(allPackages, file);
      }
      // TODO: Support for sources in jars/zips
    }
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
  protected final PackageProvider getPackageProvider(String packageName) {
    return this._allPackages.get(packageName);
  }

  /**
   * @param allPackages
   * @param classPathEntry
   */
  private void addAllPackagesFromClassPathEntry(String[] allPackages, File classPathEntry) {

    for (String aPackage : allPackages) {
      if (this._allPackages.containsKey(aPackage)) {
        PackageProvider provider = this._allPackages.get(aPackage);
        provider.addClasspathEntry(classPathEntry);
      } else {
        PackageProvider provider = newPackageProvider();
        provider.addClasspathEntry(classPathEntry);
        this._allPackages.put(aPackage, provider);
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param allPackages
   * @param sourcePathEntry
   */
  private void addAllPackagesFromSourcePathEntry(String[] allPackages, File sourcePathEntry) {

    for (String aPackage : allPackages) {
      if (this._allPackages.containsKey(aPackage)) {
        PackageProvider provider = this._allPackages.get(aPackage);
        provider.addSourcepathEntry(sourcePathEntry);
      } else {
        PackageProvider provider = newPackageProvider();
        provider.addSourcepathEntry(sourcePathEntry);
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
  private String[] getAllPackagesFromJar(File jar) throws IOException {
    Assert.isFile(jar);

    // prepare result...
    List<String> result = new LinkedList<String>();

    // create the jarFile wrapper...
    JarFile jarFile = new JarFile(jar);

    // Iterate over entries...
    Enumeration<?> enumeration = jarFile.entries();
    while (enumeration.hasMoreElements()) {
      JarEntry jarEntry = (JarEntry) enumeration.nextElement();

      // add package for each found directory...
      String directoryName = null;

      // if the jar entry is a directory, the directory name is the name of the jar entry...
      if (jarEntry.isDirectory()) {
        directoryName = jarEntry.getName();
      }
      // otherwise the directory name has to be computed
      else {
        int splitIndex = jarEntry.getName().lastIndexOf('/');
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
        String[] packages = allPackages(packageName);
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
   * <b>Example:</b><br/>
   * Given the package name <code>net.sf.ant4eclipse.tools</code> this method will return {"net", "net.sf",
   * "net.sf.ant4eclipse", "net.sf.ant4eclipse.tools"}.
   * </p>
   * 
   * @param packageName
   *          the name of the package.
   * @return all package names (including parent package names) for the specified package.
   */
  private String[] allPackages(String packageName) {

    // split the package name
    StringTokenizer tokenizer = new StringTokenizer(packageName, ".");

    // declare result
    String[] result = new String[tokenizer.countTokens()];

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
  private String[] getAllPackagesFromDirectory(File directory) {

    List<String> result = new LinkedList<String>();

    File[] children = directory.listFiles(new FileFilter() {
      public boolean accept(File pathname) {
        return pathname.isDirectory();
      }
    });

    for (File element : children) {
      getAllPackagesFromDirectory(null, element, result);
    }

    return result.toArray(new String[0]);
  }

  /**
   * @param prefix
   * @param directory
   * @param result
   */
  private void getAllPackagesFromDirectory(String prefix, File directory, List<String> result) {

    String newPrefix = prefix == null ? "" : prefix + ".";

    result.add(newPrefix + directory.getName());

    File[] children = directory.listFiles(new FileFilter() {
      public boolean accept(File pathname) {
        return pathname.isDirectory();
      }
    });

    for (File element : children) {
      getAllPackagesFromDirectory(newPrefix + directory.getName(), element, result);
    }
  }

  /**
   * {@inheritDoc}
   */
  public ClassFile loadClass(ClassName className) {

    if (!hasPackage(className.getPackageName())) {
      return null;
    }

    return getPackageProvider(className.getPackageName()).loadClassFile(className);
  }

  /**
   * {@inheritDoc}
   */
  public ReferableSourceFile loadSource(ClassName className) {
    if (!hasPackage(className.getPackageName())) {
      return null;
    }

    return getPackageProvider(className.getPackageName()).loadSourceFile(className);
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
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
   * <p>
   * Encapsulates all class and source path entries that provide a specific package.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class PackageProvider {

    /** the class path entries */
    private List<File> _classpathEntries;

    /** the source path entries */
    private List<File> _sourcepathEntries;

    /**
     * <p>
     * Creates a new instance of type {@link PackageProvider}.
     * </p>
     */
    public PackageProvider() {
      this._classpathEntries = new LinkedList<File>();
      this._sourcepathEntries = new LinkedList<File>();
    }

    /**
     * <p>
     * Adds the specified file to the class path.
     * </p>
     * 
     * @param classpathEntry
     *          the class path entry to add.
     */
    public void addClasspathEntry(File classpathEntry) {
      Assert.exists(classpathEntry);

      this._classpathEntries.add(classpathEntry);
    }

    /**
     * <p>
     * Adds the specified file to the source path.
     * </p>
     * 
     * @param sourcepathEntry
     *          the source path entry to add.
     */
    public void addSourcepathEntry(File sourcepathEntry) {
      // TODO: support for zip'd or jar'd source files
      Assert.isDirectory(sourcepathEntry);

      this._sourcepathEntries.add(sourcepathEntry);
    }

    /**
     * <p>
     * </p>
     * 
     * @param className
     * @return
     */
    public ClassFile loadClassFile(ClassName className) {

      for (File file : this._classpathEntries) {
        File classpathEntry = file;

        if (classpathEntry.isDirectory()) {
          File result = new File(classpathEntry, className.asClassFileName());
          if (result.exists()) {
            return new FileClassFileImpl(result, classpathEntry.getAbsolutePath(),
                ClasspathClassFileLoaderImpl.this._type);
          }
        } else {
          try {
            JarFile jarFile = new JarFile(classpathEntry);

            JarEntry entry = jarFile.getJarEntry(className.asClassFileName());

            if ((entry != null)) {
              return new JarClassFileImpl(className.asClassFileName(), jarFile, classpathEntry.getAbsolutePath(),
                  ClasspathClassFileLoaderImpl.this._type);
            }
          } catch (IOException e) {
            // nothing to do here...
          }
        }
      }
      return null;
    }

    /**
     * <p>
     * </p>
     * 
     * @param className
     * @return
     */
    public ReferableSourceFile loadSourceFile(ClassName className) {

      // TODO Support for source jars

      for (File classpathEntry : this._sourcepathEntries) {

        if (classpathEntry.isDirectory()) {
          File packageDir = new File(classpathEntry, className.getPackageAsDirectoryName());

          if (packageDir.isDirectory()) {
            for (String name : packageDir.list()) {

              if (className.asSourceFileName().endsWith(name)) {
                return new ReferableSourceFileImpl(classpathEntry, className.asSourceFileName().replace('/',
                    File.separatorChar).replace('\\', File.separatorChar), classpathEntry.getAbsolutePath(),
                    ClasspathClassFileLoaderImpl.this._type);
              }
            }
          }
        }

        // else {
        // try {
        // JarFile jarFile = new JarFile(classpathEntry);
        //
        // JarEntry entry = jarFile.getJarEntry(className.asClassFileName());
        //
        // if ((entry != null)) {
        // return new JarClassFileImpl(className.asClassFileName(), jarFile, classpathEntry.getAbsolutePath(),
        // ClasspathClassFileLoaderImpl.this._type);
        // }
        // } catch (IOException e) {
        // // nothing to do here...
        // }
        // }

      }
      return null;
    }
  }
}
