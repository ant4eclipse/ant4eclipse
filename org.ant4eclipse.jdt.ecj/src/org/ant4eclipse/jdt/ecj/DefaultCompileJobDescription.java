package org.ant4eclipse.jdt.ecj;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.ExtendedProperties;

/**
 * <p>
 * Default implementation of a {@link CompileJobDescription}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultCompileJobDescription implements CompileJobDescription {

  /** the ClassFileLoader */
  private ClassFileLoader    _classFileLoader;

  /** the compiler options */
  private ExtendedProperties _compilerOptions;

  /** the source files */
  private SourceFile[]       _sourceFiles;

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
  public DefaultCompileJobDescription(ClassFileLoader classFileLoader, ExtendedProperties compilerOptions,
      SourceFile[] sourceFiles) {
    Assert.notNull(classFileLoader);
    Assert.notNull(compilerOptions);
    Assert.notNull(sourceFiles);

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
  public ExtendedProperties getCompilerOptions() {
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
  public void setCompilerOptions(ExtendedProperties compilerOptions) {
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
  public void setSourceFiles(SourceFile[] sourceFiles) {
    Assert.notNull(sourceFiles);

    this._sourceFiles = sourceFiles;
  }
}
