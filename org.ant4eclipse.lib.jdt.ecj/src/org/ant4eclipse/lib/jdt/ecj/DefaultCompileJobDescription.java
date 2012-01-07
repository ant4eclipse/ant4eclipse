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
package org.ant4eclipse.lib.jdt.ecj;

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
  private ClassFileLoader    _classFileLoader;

  /** the compiler options */
  private Map<String,String> _compilerOptions;

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
  public DefaultCompileJobDescription( ClassFileLoader classFileLoader, StringMap compilerOptions,
      SourceFile[] sourceFiles ) {
    Assure.notNull( "classFileLoader", classFileLoader );
    Assure.notNull( "compilerOptions", compilerOptions );
    Assure.notNull( "sourceFiles", sourceFiles );
    _classFileLoader = classFileLoader;
    _compilerOptions = compilerOptions;
    _sourceFiles = sourceFiles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClassFileLoader getClassFileLoader() {
    return _classFileLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String,String> getCompilerOptions() {
    return _compilerOptions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SourceFile[] getSourceFiles() {
    return _sourceFiles;
  }

  /**
   * <p>
   * Sets the class file loader.
   * </p>
   * 
   * @param classFileLoader
   *          the class file loader.
   */
  public void setClassFileLoader( ClassFileLoader classFileLoader ) {
    Assure.notNull( "classFileLoader", classFileLoader );
    _classFileLoader = classFileLoader;
  }

  /**
   * <p>
   * Sets the compiler options.
   * </p>
   * 
   * @param compilerOptions
   *          the compiler options.
   */
  public void setCompilerOptions( Map<String,String> compilerOptions ) {
    Assure.notNull( "compilerOptions", compilerOptions );
    _compilerOptions = compilerOptions;
  }

  /**
   * <p>
   * Sets the source files.
   * </p>
   * 
   * @param sourceFiles
   *          the source files.
   */
  public void setSourceFiles( SourceFile[] sourceFiles ) {
    Assure.notNull( "sourceFiles", sourceFiles );
    _sourceFiles = sourceFiles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[DefaultCompileJobDescription:" );
    buffer.append( " _classFileLoader: " );
    buffer.append( _classFileLoader );
    buffer.append( ", _compilerOptions: {" );
    if( (_compilerOptions != null) && (_compilerOptions.size() > 0) ) {
      Iterator<Map.Entry<String,String>> iterator = _compilerOptions.entrySet().iterator();
      Map.Entry<String,String> current = iterator.next();
      buffer.append( "(" );
      buffer.append( current.getKey() );
      buffer.append( "," );
      buffer.append( current.getValue() );
      buffer.append( ")" );
      while( iterator.hasNext() ) {
        buffer.append( "," );
        current = iterator.next();
        buffer.append( "(" );
        buffer.append( current.getKey() );
        buffer.append( "," );
        buffer.append( current.getValue() );
        buffer.append( ")" );
      }
    }
    buffer.append( _compilerOptions );
    buffer.append( "}" );
    buffer.append( ", _sourceFiles: {" );
    if( (_sourceFiles != null) && (_sourceFiles.length > 0) ) {
      buffer.append( _sourceFiles[0] );
      for( int i = 1; i < _sourceFiles.length; i++ ) {
        buffer.append( ", " );
        buffer.append( _sourceFiles[i] );
      }
    }
    buffer.append( _sourceFiles );
    buffer.append( "}" );
    buffer.append( "]" );
    return buffer.toString();
  }

} /* ENDCLASS */
