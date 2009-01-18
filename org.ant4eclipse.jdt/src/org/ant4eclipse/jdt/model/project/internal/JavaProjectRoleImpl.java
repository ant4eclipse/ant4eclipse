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
package org.ant4eclipse.jdt.model.project.internal;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.jdt.model.ContainerTypes;
import org.ant4eclipse.jdt.model.jre.JavaProfile;
import org.ant4eclipse.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.jdt.model.jre.JavaRuntimeRegistry;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.AbstractProjectRole;


/**
 * <p>
 * Implements the java project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaProjectRoleImpl extends AbstractProjectRole implements JavaProjectRole {

  /**  */
  public static final String NAME = "JavaProjectRole";

  /** the class path entries */
  private final List        /* EclipseClasspathEntry */_eclipseClasspathEntries;

  /**
   * <p>
   * Creates a new instance of type JavaProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          the eclipse project
   */
  public JavaProjectRoleImpl(final EclipseProject eclipseProject) {
    super(NAME, eclipseProject);
    this._eclipseClasspathEntries = new LinkedList();
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasRawClasspathEntries() {
    return !this._eclipseClasspathEntries.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  public RawClasspathEntry[] getRawClasspathEntries() {
    return (RawClasspathEntry[]) this._eclipseClasspathEntries.toArray(new RawClasspathEntry[0]);
  }

  /**
   * {@inheritDoc}
   */
  public RawClasspathEntry[] getRawClasspathEntries(final int entrykind) {
    final LinkedList templist = new LinkedList();
    for (int i = 0; i < this._eclipseClasspathEntries.size(); i++) {
      final RawClasspathEntry entry = (RawClasspathEntry) this._eclipseClasspathEntries.get(i);
      if (entry.getEntryKind() == entrykind) {
        templist.add(entry);
      }
    }
    final RawClasspathEntry[] result = new RawClasspathEntry[templist.size()];
    templist.toArray(result);
    return (result);
  }

  /**
   * {@inheritDoc}
   */
  public JavaRuntime getJavaRuntime() {
    final RawClasspathEntry runtimeEntry = getJreClasspathEntry();

    if (runtimeEntry == null) {
      return null;
    }

    if (ContainerTypes.JRE_CONTAINER.equals(runtimeEntry.getPath())) {
      return getJavaRuntimeRegistry().getDefaultJavaRuntime();
    }

    JavaRuntime javaRuntime = null;

    if (runtimeEntry.getPath().startsWith(ContainerTypes.VMTYPE_PREFIX)) {
      javaRuntime = getJavaRuntimeRegistry().getJavaRuntime(
          runtimeEntry.getPath().substring(ContainerTypes.VMTYPE_PREFIX.length()));
    }

    if (javaRuntime != null) {
      return javaRuntime;
    }

    A4ELogging.warn("No java runtime '%s' defined - using default runtime.", runtimeEntry.getPath());

    return getJavaRuntimeRegistry().getDefaultJavaRuntime();
  }

  /**
   * {@inheritDoc}
   */
  public JavaProfile getJavaProfile() {
    if (getJreClasspathEntry().getPath().startsWith(ContainerTypes.VMTYPE_PREFIX)) {
      return getJavaRuntimeRegistry().getJavaProfile(
          getJreClasspathEntry().getPath().substring(ContainerTypes.VMTYPE_PREFIX.length()));
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public Map getCompilerOptions() {

    Map result = null;

    // read project-specific compiler settings if available
    final File settingsDir = getEclipseProject().getChild(".settings");
    final File prefsFile = new File(settingsDir, "org.eclipse.jdt.core.prefs");
    if (prefsFile.isFile()) {
      result = Utilities.readProperties(prefsFile);
    } else {
      A4ELogging.debug("No file with project specific compiler settings found at '%s'.", prefsFile);
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getSourceFolders() {

    final RawClasspathEntry[] entries = getRawClasspathEntries(RawClasspathEntry.CPE_SOURCE);

    final String[] result = new String[entries.length];
    for (int i = 0; i < entries.length; i++) {
      result[i] = entries[i].getPath();
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getAllOutputFolders() {

    final EntryResolver.Condition condition = new EntryResolver.Condition() {
      public String resolve(final RawClasspathEntry entry) {
        if (entry.getEntryKind() == RawClasspathEntry.CPE_OUTPUT) {
          return entry.getPath();
        } else if ((entry.getEntryKind() == RawClasspathEntry.CPE_SOURCE) && entry.hasOutputLocation()) {
          return entry.getOutputLocation();
        }
        return null;
      }
    };

    return EntryResolver.resolveEntries(condition, this);
  }

  /**
   * {@inheritDoc}
   */
  public String getDefaultOutputFolder() {

    final EntryResolver.Condition condition = new EntryResolver.Condition() {
      public String resolve(final RawClasspathEntry entry) {
        if (entry.getEntryKind() == RawClasspathEntry.CPE_OUTPUT) {
          return entry.getPath();
        }
        return null;
      }
    };

    final String[] result = EntryResolver.resolveEntries(condition, this);

    if (result.length > 0) {
      return result[0];
    } else {
      // TODO
      return null;
    }
  }

  /**
   * @param sourceFolder
   * @param resolveRelative
   * @return
   */
  public String getOutputFolderForSourceFolder(final String sourceFolder) {
    Assert.nonEmpty(sourceFolder);

    // normalize path
    final String normalizedSourceFolder = normalize(sourceFolder);

    // Implementation of the EntryResolver.Condition
    final EntryResolver.Condition condition = new EntryResolver.Condition() {

      public String resolve(final RawClasspathEntry entry) {

        A4ELogging.debug("Trying to resolve RawClasspathEntry '" + entry + "' as sourcefolder '" + sourceFolder + "'");

        // try to retrieve the output folder for a 'normal' source folder
        if ((entry.getEntryKind() == RawClasspathEntry.CPE_SOURCE) && normalizedSourceFolder.equals(entry.getPath())) {
          return getOutputFolder(entry);
        }

        // try to retrieve the output folder for a 'linked' source folder
        A4ELogging.debug("Trying to resolve project child '" + getEclipseProject().getChild(entry.getPath())
            + "' as sourcefolder '" + sourceFolder + "'");
        // if (getEclipseProject().hasChild(entry.getPath())) {
        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(), EclipseProject.ABSOLUTE).getPath())) {
          return getOutputFolder(entry);
        }
        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(),
            EclipseProject.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME).getPath())) {
          return getOutputFolder(entry);
        }
        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(),
            EclipseProject.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME).getPath())) {
          return getOutputFolder(entry);
        }
        // }

        return null;
      }

      private String getOutputFolder(final RawClasspathEntry entry) {
        if (entry.hasOutputLocation()) {
          return entry.getOutputLocation();
        } else {
          return getDefaultOutputFolder();
        }
      }
    };

    final String[] result = EntryResolver.resolveEntries(condition, this);

    if (result.length == 0) {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("The source folder '");
      buffer.append(sourceFolder);
      buffer.append("' does not exist in project '");
      buffer.append(getEclipseProject().getFolderName());
      buffer.append("'!");
      throw new RuntimeException(buffer.toString());
    } else {
      return result[0];
    }
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[JavaProjectRole:");
    buffer.append(" NAME: ");
    buffer.append(NAME);
    buffer.append(" _eclipseClasspathEntries: ");
    buffer.append(this._eclipseClasspathEntries);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = 31 * hashCode + (this._eclipseClasspathEntries == null ? 0 : this._eclipseClasspathEntries.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!super.equals(o)) {
      return false;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    final JavaProjectRoleImpl castedObj = (JavaProjectRoleImpl) o;
    return ((this._eclipseClasspathEntries == null ? castedObj._eclipseClasspathEntries == null
        : this._eclipseClasspathEntries.equals(castedObj._eclipseClasspathEntries)));
  }

  /**
   * Sets the specified classpath entries.
   * 
   * @param classpathEntry
   *          the eclipse classpath entries to set.
   */
  public void addEclipseClasspathEntry(final RawClasspathEntry classpathEntry) {
    Assert.notNull(classpathEntry);

    this._eclipseClasspathEntries.add(classpathEntry);
  }

  private String normalize(final String sourceFolder) {

    if (sourceFolder == null) {
      return sourceFolder;
    }
    String result = sourceFolder.replace('/', File.separatorChar);
    result = result.replace('\\', File.separatorChar);

    return result;
  }

  private JavaRuntimeRegistry getJavaRuntimeRegistry() {
    return (JavaRuntimeRegistry) ServiceRegistry.instance().getService(JavaRuntimeRegistry.class.getName());
  }

  /**
   * @return
   */
  private RawClasspathEntry getJreClasspathEntry() {
    final RawClasspathEntry[] containerEntries = getRawClasspathEntries(RawClasspathEntry.CPE_CONTAINER);

    for (int i = 0; i < containerEntries.length; i++) {
      final RawClasspathEntry entry = containerEntries[i];
      if (entry.getPath().startsWith(ContainerTypes.JRE_CONTAINER)) {
        return entry;
      }
    }

    return null;
  }
}