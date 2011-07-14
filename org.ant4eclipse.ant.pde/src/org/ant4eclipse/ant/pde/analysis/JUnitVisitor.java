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
final class JUnitVisitor extends EmptyVisitor {

  /** indicates whether the class has test annotations or not */
  private boolean _hasTestAnnotations;

  /** indicates whether the class is a test class or not */
  private boolean _isAbstract;

  /** the name of the class */
  private String  _className;

  /**
   * {@inheritDoc}
   */
  @Override
  public MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4) {

    //
    if (!this._hasTestAnnotations) {
      return this;
    }

    //
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit(int arg0, int arg1, String arg2, String arg3, String arg4, String[] arg5) {
    this._isAbstract = ((arg1 & Opcodes.ACC_ABSTRACT) != 0);
    this._className = arg2.replace('/', '.');
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
    Type t = Type.getType(arg0);
    if (t.getClassName().startsWith("org.junit")) {
      this._hasTestAnnotations = true;
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
    return (!this._isAbstract) && this._hasTestAnnotations;
  }

  /**
   * <p>
   * Returns the name of the class.
   * </p>
   * 
   * @return the name of the class.
   */
  public String getClassName() {
    return this._className;
  }
}