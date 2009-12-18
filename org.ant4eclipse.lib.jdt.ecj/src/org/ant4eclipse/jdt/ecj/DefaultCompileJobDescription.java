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
package org.ant4eclipse.jdt.ecj;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.StringMap;

import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Default implementation of a {@link CompileJobDescription}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultCompileJobDescription implements CompileJobDescription {

  /** the ClassFileLoader */
  private ClassFileLoader     _classFileLoader;

  /** the compiler options */
  private Map<String, String> _compilerOptions;

  /** the source files */
  private SourceFile[]        _sourceFiles;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultCompileJobDescription}.
   * </p>
   */
  public DefaultCompileJobDescription() {
    super();
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultCompileJobDescription}.
   * </p>
   * 
   * @param classFileLoader
   * @param compilerOptions
   * @param sourceFiles
   */
  public DefaultCompileJobDescription(ClassFileLoader classFileLoader, StringMap compilerOptions,
      SourceFile[] sourceFiles) {
    Assure.paramNotNull("classFileLoader", classFileLoader);
    Assure.paramNotNull("compilerOptions", compilerOptions);
    Assure.paramNotNull("sourceFiles", sourceFiles);
    this._classFileLoader = classFileLoader;
    this._compilerOptions = compilerOptions;
    this._sourceFiles = sourceFiles;
  }

  /**
   * {@inheritDoc}
   */
  public ClassFileLoader getClassFileLoader() {
    return this._classFileLoader;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, String> getCompilerOptions() {
    return this._compilerOptions;
  }

  /**
   * {@inheritDoc}
   */
  public SourceFile[] getSourceFiles() {
    return this._sourceFiles;
  }

  /**
   * <p>
   * Sets the class file loader.
   * </p>
   * 
   * @param classFileLoader
   *          the class file loader.
   */
  public void setClassFileLoader(ClassFileLoader classFileLoader) {
    Assure.paramNotNull("classFileLoader", classFileLoader);
    this._classFileLoader = classFileLoader;
  }

  /**
   * <p>
   * Sets the compiler options.
   * </p>
   * 
   * @param compilerOptions
   *          the compiler options.
   */
  public void setCompilerOptions(Map<String, String> compilerOptions) {
    Assure.paramNotNull("compilerOptions", compilerOptions);
    this._compilerOptions = compilerOptions;
  }

  /**
   * <p>
   * Sets the source files.
   * </p>
   * 
   * @param sourceFiles
   *          the source files.
   */
  public void setSourceFiles(SourceFile[] sourceFiles) {
    Assure.paramNotNull("sourceFiles", sourceFiles);
    this._sourceFiles = sourceFiles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[DefaultCompileJobDescription:");
    buffer.append(" _classFileLoader: ");
    buffer.append(this._classFileLoader);
    buffer.append(", _compilerOptions: {");
    if ((this._compilerOptions != null) && (this._compilerOptions.size() > 0)) {
      Iterator<Map.Entry<String, String>> iterator = this._compilerOptions.entrySet().iterator();
      Map.Entry<String, String> current = iterator.next();
      buffer.append("(");
      buffer.append(current.getKey());
      buffer.append(",");
      buffer.append(current.getValue());
      buffer.append(")");
      while (iterator.hasNext()) {
        buffer.append(",");
        current = iterator.next();
        buffer.append("(");
        buffer.append(current.getKey());
        buffer.append(",");
        buffer.append(current.getValue());
        buffer.append(")");
      }
    }
    buffer.append(this._compilerOptions);
    buffer.append("}");
    buffer.append(", _sourceFiles: {");
    if ((this._sourceFiles != null) && (this._sourceFiles.length > 0)) {
      buffer.append(this._sourceFiles[0]);
      for (int i = 1; i < this._sourceFiles.length; i++) {
        buffer.append(", ");
        buffer.append(this._sourceFiles[i]);
      }
    }
    buffer.append(this._sourceFiles);
    buffer.append("}");
    buffer.append("]");
    return buffer.toString();
  }

}
