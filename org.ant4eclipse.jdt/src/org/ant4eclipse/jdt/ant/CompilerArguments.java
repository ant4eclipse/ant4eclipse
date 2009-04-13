package org.ant4eclipse.jdt.ant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.core.Assert;

public class CompilerArguments {

  private final Map<File, File>   _outputFolderMap;

  private final Map<File, String> _accessRestrictions;

  private String                  _bootClassPathAccessRestrictions;

  public CompilerArguments() {
    this._accessRestrictions = new HashMap<File, String>();
    this._outputFolderMap = new HashMap<File, File>();
  }

  public String getBootClassPathAccessRestrictions() {
    return this._bootClassPathAccessRestrictions;
  }

  public File getOutputFolder(final File sourceFolder) {
    Assert.isDirectory(sourceFolder);

    return this._outputFolderMap.get(sourceFolder);
  }

  public void addSourceFolder(final File sourceFolder, final File outputFolder) {
    this._outputFolderMap.put(sourceFolder, outputFolder);
  }

  public boolean hasBootClassPathAccessRestrictions() {
    return this._bootClassPathAccessRestrictions != null;
  }

  public void setBootClassPathAccessRestrictions(final String bootClassPathAccessRestrictions) {
    Assert.nonEmpty(bootClassPathAccessRestrictions);

    this._bootClassPathAccessRestrictions = bootClassPathAccessRestrictions;
  }

  public boolean hasAccessRestrictions(final File classpathentry) {
    return this._accessRestrictions.containsKey(classpathentry);
  }

  public String getAccessRestrictions(final File classpathentry) {
    return this._accessRestrictions.get(classpathentry);
  }

  public void addAccessRestrictions(final File classpathentry, final String accessRestrictions) {
// System.err.println("AccessRestriction: " + classpathentry + " - " + accessRestrictions);
    this._accessRestrictions.put(classpathentry, accessRestrictions);
  }
}
