package org.ant4eclipse.jdt.ecj;

import org.ant4eclipse.core.Assert;

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
  public DefaultCompileJobDescription(ClassFileLoader classFileLoader, Map<String, String> compilerOptions,
      SourceFile[] sourceFiles) {
    Assert.notNull(classFileLoader);
    Assert.notNull(compilerOptions);
    Assert.notNull(sourceFiles);

    _classFileLoader = classFileLoader;
    _compilerOptions = compilerOptions;
    _sourceFiles = sourceFiles;
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
  public void setClassFileLoader(final ClassFileLoader classFileLoader) {
    Assert.notNull(classFileLoader);

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
  public void setCompilerOptions(final Map<String, String> compilerOptions) {
    Assert.notNull(compilerOptions);

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
  public void setSourceFiles(final SourceFile[] sourceFiles) {
    Assert.notNull(sourceFiles);

    this._sourceFiles = sourceFiles;
  }
}
