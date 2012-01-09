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
package org.ant4eclipse.ant.pde.analysis;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * <p>
 * Implements the JUnit class visitor.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JUnitVisitor extends EmptyVisitor {

  /** indicates whether the class has test annotations or not */
  private boolean _hasTestAnnotations;

  /** indicates whether the class is a test class or not */
  private boolean _isAbstract;

  /** the name of the class */
  private String  _className;

  /** the name of the class */
  private String  _superClassName;

  /**
   * {@inheritDoc}
   */
  @Override
  public MethodVisitor visitMethod( int arg0, String arg1, String arg2, String arg3, String[] arg4 ) {

    //
    if( !_hasTestAnnotations ) {
      return this;
    }

    //
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( int arg0, int arg1, String arg2, String arg3, String arg4, String[] arg5 ) {
    _isAbstract = ((arg1 & Opcodes.ACC_ABSTRACT) != 0);
    _className = arg2.replace( '/', '.' );
    _superClassName = arg4.replace( '/', '.' );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AnnotationVisitor visitAnnotation( String arg0, boolean arg1 ) {
    Type t = Type.getType( arg0 );
    if( t.getClassName().startsWith( "org.junit" ) ) {
      _hasTestAnnotations = true;
    }
    return null;
  }

  /**
   * <p>
   * Returns <code>true</code> if the tested class is a test class.
   * </p>
   * 
   * @return <code>true</code> if the tested class is a test class.
   */
  public boolean isTestClass() {
    return (!_isAbstract) && _hasTestAnnotations;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean hasTestAnnotations() {
    return _hasTestAnnotations;
  }

  /**
   * <p>
   * Returns the name of the class.
   * </p>
   * 
   * @return the name of the class.
   */
  public String getClassName() {
    return _className;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getSuperClassName() {
    return _superClassName;
  }
  
} /* ENDCLASS */

