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
package org.ant4eclipse.ant.jdt;

import org.ant4eclipse.ant.jdt.ecj.EcjCompilerAdapter;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.compilers.CompilerAdapter;

import java.io.File;

/**
 * <p>
 * This task is an extension to the Javac compiler task which makes use of the {@link EcjCompilerAdapter}. The most
 * important advantage is that the compiler implementation is provided using the classloader of the a4e package which
 * means that the package itself is not required to be provided together with the ant distribution.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class JdtCompilerTask extends Javac {

  private static final String MSG_INVALID_ATTRIBUTE = "The attribute 'compiler' for the task '%s' is not allowed to be used.";

  private static final String MSG_FAILURE           = "The compilation failed. Check the output for more information.";

  private String              errprop               = null;

  private String              updateprop            = null;

  /**
   * As we're responsible for the compilation we're handling the property values as well.
   * 
   * {@inheritDoc}
   */
  @Override
  public void setErrorProperty(String prop) {
    this.errprop = prop;
  }

  /**
   * As we're responsible for the compilation we're handling the property values as well.
   * 
   * {@inheritDoc}
   */
  @Override
  public void setUpdatedProperty(String prop) {
    this.updateprop = prop;
  }

  /**
   * <p>
   * Returns a CompilerAdapter instance used for the compilation process. Everything would be much easier if the Ant
   * developers would have provided such a method in the first place.
   * </p>
   * 
   * @return The CompilerAdapter instance used for the compilation process. Not <code>null</code>.
   */
  protected CompilerAdapter getNewCompilerAdapter() {
    return new EcjCompilerAdapter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void compile() {

    File destdir = super.getDestdir();

    if (this.compileList.length > 0) {

      File current = new File(".");
      String dest = destdir != null ? String.valueOf(destdir) : String.valueOf(current);
      if (this.compileList.length == 1) {
        A4ELogging.info("Compiling 1 source file '%s' to '%s' !", this.compileList[0], dest);
      } else {
        A4ELogging.info("Compiling %d source files to '%s' !", Integer.valueOf(this.compileList.length), dest);
      }

      if (this.listFiles) {
        for (File element : this.compileList) {
          A4ELogging.info("\t%s", element.getAbsolutePath());
        }
      }

      // obtain an adapter used to run the compilation process
      CompilerAdapter adapter = getNewCompilerAdapter();
      adapter.setJavac(this);

      // launch the compilation process
      if (adapter.execute()) {

        // everything went fine, so we can mark this using a proeprty if desired
        if (this.updateprop != null) {
          getProject().setNewProperty(this.updateprop, "true");
        }

      } else {

        // damn it. we need to mark the error.
        if (this.errprop != null) {
          getProject().setNewProperty(this.errprop, "true");
        }
        if (this.failOnError) {
          throw new BuildException(MSG_FAILURE, getLocation());
        } else {
          A4ELogging.error(MSG_FAILURE);
        }
      }

    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCompiler(String compiler) {
    A4ELogging.error(MSG_INVALID_ATTRIBUTE, getTaskName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws BuildException {
    super.setCompiler(EcjCompilerAdapter.class.getName());
    super.execute();
  }

} /* ENDCLASS */
