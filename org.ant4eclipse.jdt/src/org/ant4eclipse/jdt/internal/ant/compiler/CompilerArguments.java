package org.ant4eclipse.jdt.internal.ant.compiler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.core.Assert;

public class CompilerArguments {

  private final Map<File, File> _outputFolderMap;

  private String                _bootClassPathAccessRestrictions;

  public CompilerArguments() {
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
}
