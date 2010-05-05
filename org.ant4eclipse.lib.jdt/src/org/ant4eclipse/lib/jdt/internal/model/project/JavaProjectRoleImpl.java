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
package org.ant4eclipse.lib.jdt.internal.model.project;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.ContainerTypes;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRole;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
/**
 * <p>
 * Implements the java project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaProjectRoleImpl extends AbstractProjectRole implements JavaProjectRole {

  /**  */
  public static final String   NAME = "JavaProjectRole";

  /** the class path entries */
  private List<ClasspathEntry> _eclipseClasspathEntries;

  /**
   * <p>
   * Creates a new instance of type JavaProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          the eclipse project
   */
  public JavaProjectRoleImpl(EclipseProject eclipseProject) {
    super(NAME, eclipseProject);
    this._eclipseClasspathEntries = new LinkedList<ClasspathEntry>();
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
    return this._eclipseClasspathEntries.toArray(new RawClasspathEntry[0]);
  }

  /**
   * {@inheritDoc}
   */
  public RawClasspathEntry[] getRawClasspathEntries(int entrykind) {
    LinkedList<ClasspathEntry> templist = new LinkedList<ClasspathEntry>();
    for (ClasspathEntry entry : this._eclipseClasspathEntries) {
      if (entry.getEntryKind() == entrykind) {
        templist.add(entry);
      }
    }
    RawClasspathEntry[] result = new RawClasspathEntry[templist.size()];
    templist.toArray(result);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public JavaRuntime getJavaRuntime() {
    RawClasspathEntry runtimeEntry = getJreClasspathEntry();

    if (runtimeEntry == null) {
      return null;
    }

    return getJavaRuntimeRegistry().getJavaRuntimeForPath(runtimeEntry.getPath());
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
  public StringMap getCompilerOptions() {

    StringMap result = null;

    // read project-specific compiler settings if available
    File settingsDir = getEclipseProject().getChild(".settings");
    File prefsFile = new File(settingsDir, "org.eclipse.jdt.core.prefs");
    if (prefsFile.isFile()) {
      result = new StringMap(prefsFile);
    } else {
      A4ELogging.debug("No file with project specific compiler settings found at '%s'.", prefsFile);
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getSourceFolders() {

    RawClasspathEntry[] entries = getRawClasspathEntries(RawClasspathEntry.CPE_SOURCE);

    String[] result = new String[entries.length];
    for (int i = 0; i < entries.length; i++) {
      result[i] = entries[i].getPath();
    }

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean hasExcludeOrIncludeFiltersForSourceFolders() {

    for (RawClasspathEntry entry : getRawClasspathEntries()) {

      if (entry.getEntryKind() == RawClasspathEntry.CPE_SOURCE
          && (entry.getIncludes() != null || entry.getExcludes() != null)) {

        return true;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  public String getExcludePatternsForSourceFolder(String sourceFolder) {
    RawClasspathEntry rawClasspathEntry = getEntryForSourceFolder(sourceFolder);

    return (rawClasspathEntry != null && rawClasspathEntry.getExcludes() != null) ? rawClasspathEntry.getExcludes()
        .replace('|', ' ') : "";
  }

  /**
   * {@inheritDoc}
   */
  public String getIncludePatternsForSourceFolder(String sourceFolder) {
    RawClasspathEntry rawClasspathEntry = getEntryForSourceFolder(sourceFolder);

    return (rawClasspathEntry != null && rawClasspathEntry.getIncludes() != null) ? rawClasspathEntry.getIncludes()
        .replace('|', ' ') : "**";
  }

  /**
   * {@inheritDoc}
   */
  public String[] getAllOutputFolders() {

    EntryResolver.Condition<String> condition = new EntryResolver.Condition<String>() {
      public String resolve(RawClasspathEntry entry) {
        if (entry.getEntryKind() == RawClasspathEntry.CPE_OUTPUT) {
          return entry.getPath();
        } else if ((entry.getEntryKind() == RawClasspathEntry.CPE_SOURCE) && entry.hasOutputLocation()) {
          return entry.getOutputLocation();
        }
        return null;
      }
    };

    return EntryResolver.resolveEntries(condition, this).toArray(new String[0]);
  }

  /**
   * {@inheritDoc}
   */
  public String getDefaultOutputFolder() {

    EntryResolver.Condition<String> condition = new EntryResolver.Condition<String>() {
      public String resolve(RawClasspathEntry entry) {
        if (entry.getEntryKind() == RawClasspathEntry.CPE_OUTPUT) {
          return entry.getPath();
        }
        return null;
      }
    };

    String[] result = EntryResolver.resolveEntries(condition, this).toArray(new String[0]);

    if (result.length > 0) {
      return result[0];
    } else {
      // TODO
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getOutputFolderForSourceFolder(final String sourceFolder) {
    Assure.notNull("sourceFolder", sourceFolder);

    if ("".equals(sourceFolder)) {
      return getDefaultOutputFolder();
    }

    // normalize path
    final String normalizedSourceFolder = normalize(sourceFolder);

    // Implementation of the EntryResolver.Condition
    EntryResolver.Condition<String> condition = new EntryResolver.Condition<String>() {

      public String resolve(RawClasspathEntry entry) {

        A4ELogging.debug("Trying to resolve RawClasspathEntry '%s' as sourcefolder '%s'.", entry, sourceFolder);

        // try to retrieve the output folder for a 'normal' source folder
        if ((entry.getEntryKind() == RawClasspathEntry.CPE_SOURCE) && normalizedSourceFolder.equals(entry.getPath())) {
          return getOutputFolder(entry);
        }

        // try to retrieve the output folder for a 'linked' source folder
        A4ELogging.debug("Trying to resolve project child '%s' as sourcefolder '%s'.", getEclipseProject().getChild(
            entry.getPath()), sourceFolder);
        // if (getEclipseProject().hasChild(entry.getPath())) {
        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(), EclipseProject.PathStyle.ABSOLUTE)
            .getPath())) {
          return getOutputFolder(entry);
        }
        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(),
            EclipseProject.PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME).getPath())) {
          return getOutputFolder(entry);
        }
        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(),
            EclipseProject.PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME).getPath())) {
          return getOutputFolder(entry);
        }
        // }

        return null;
      }

      private String getOutputFolder(RawClasspathEntry entry) {
        if (entry.hasOutputLocation()) {
          return entry.getOutputLocation();
        } else {
          return getDefaultOutputFolder();
        }
      }
    };

    String[] result = EntryResolver.resolveEntries(condition, this).toArray(new String[0]);

    if (result.length == 0) {
      StringBuffer buffer = new StringBuffer();
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
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
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
  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = 31 * hashCode + (this._eclipseClasspathEntries == null ? 0 : this._eclipseClasspathEntries.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
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
    JavaProjectRoleImpl other = (JavaProjectRoleImpl) o;
    if (this._eclipseClasspathEntries == null) {
      return other._eclipseClasspathEntries == null;
    } else {
      return this._eclipseClasspathEntries.equals(other._eclipseClasspathEntries);
    }
  }

  /**
   * Sets the specified classpath entries.
   * 
   * @param classpathEntry
   *          the eclipse classpath entries to set.
   */
  public void addEclipseClasspathEntry(RawClasspathEntry classpathEntry) {
    Assure.notNull("classpathEntry", classpathEntry);
    this._eclipseClasspathEntries.add(classpathEntry);
  }

  private String normalize(String sourceFolder) {

    if (sourceFolder == null) {
      return sourceFolder;
    }
    String result = sourceFolder.replace('/', File.separatorChar);
    result = result.replace('\\', File.separatorChar);

    return result;
  }

  private JavaRuntimeRegistry getJavaRuntimeRegistry() {
    return ServiceRegistry.instance().getService(JavaRuntimeRegistry.class);
  }

  /**
   * @return
   */
  private RawClasspathEntry getJreClasspathEntry() {
    RawClasspathEntry[] containerEntries = getRawClasspathEntries(RawClasspathEntry.CPE_CONTAINER);

    for (RawClasspathEntry entry : containerEntries) {
      if (entry.getPath().startsWith(ContainerTypes.JRE_CONTAINER)) {
        return entry;
      }
    }

    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param sourceFolder
   * @return The RawClasspathEntry for the sourcefolder or <tt>null</tt>
   */
  private RawClasspathEntry getEntryForSourceFolder(final String sourceFolder) {
    Assure.notNull("sourceFolder", sourceFolder);

    if ("".equals(sourceFolder)) {
      return null;
    }

    // normalize path
    final String normalizedSourceFolder = normalize(sourceFolder);

    // Implementation of the EntryResolver.Condition
    EntryResolver.Condition<RawClasspathEntry> condition = new EntryResolver.Condition<RawClasspathEntry>() {

      public RawClasspathEntry resolve(RawClasspathEntry entry) {

        A4ELogging.debug("Trying to resolve RawClasspathEntry '%s' as sourcefolder '%s'.", entry, sourceFolder);

        // try to retrieve the output folder for a 'normal' source folder
        if ((entry.getEntryKind() == RawClasspathEntry.CPE_SOURCE) && normalizedSourceFolder.equals(entry.getPath())) {
          return entry;
        }

        // try to retrieve the output folder for a 'linked' source folder
        A4ELogging.debug("Trying to resolve project child '%s' as sourcefolder '%s'.", getEclipseProject().getChild(
            entry.getPath()), sourceFolder);

        // if (getEclipseProject().hasChild(entry.getPath())) {
        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(), EclipseProject.PathStyle.ABSOLUTE)
            .getPath())) {
          return entry;
        }

        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(),
            EclipseProject.PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME).getPath())) {
          return entry;
        }

        if (sourceFolder.equals(getEclipseProject().getChild(entry.getPath(),
            EclipseProject.PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME).getPath())) {
          return entry;
        }

        return null;
      }
    };

    List<RawClasspathEntry> result = EntryResolver.resolveEntries(condition, this);

    if (result.size() == 0) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("The source folder '");
      buffer.append(sourceFolder);
      buffer.append("' does not exist in project '");
      buffer.append(getEclipseProject().getFolderName());
      buffer.append("'!");
      throw new RuntimeException(buffer.toString());
    } else {
      return result.get(0);
    }

  }
}