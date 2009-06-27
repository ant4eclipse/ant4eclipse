package org.ant4eclipse.jdt.ecj;

import java.util.Map;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultCompileJobDescription implements CompileJobDescription {

  private ClassFileLoader     _classFileLoader;

  private Map<String, String> _compilerOptions;

  private SourceFile[]        _sourceFiles;

  public void setClassFileLoader(final ClassFileLoader classFileLoader) {
    this._classFileLoader = classFileLoader;
  }

  public void setCompilerOptions(final Map<String, String> compilerOptions) {
    this._compilerOptions = compilerOptions;
  }

  public void setSourceFiles(final SourceFile[] sourceFiles) {
    this._sourceFiles = sourceFiles;
  }

  public ClassFileLoader getClassFileLoader() {
    return this._classFileLoader;
  }

  public Map<String, String> getCompilerOptions() {
    return this._compilerOptions;
  }

  public SourceFile[] getSourceFiles() {
    return this._sourceFiles;
  }

}
