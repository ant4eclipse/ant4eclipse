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

import org.ant4eclipse.ant.jdt.ecj.JDTCompilerAdapter;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Javac;

/**
 * <p>
 * This task is an extension to the Javac compiler task which makes use of the {@link JDTCompilerAdapter}. The most
 * important advantage is that the compiler implementation is provided using the classloader of the a4e package which
 * means that the package itself is not required to be provided together with the ant distribution.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class JdtCompilerTask extends Javac {

  private static final String MSG_INVALID_ATTRIBUTE = "The attribute 'compiler' for the task '%s' is not allowed to be used.";

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
    super.setCompiler(JDTCompilerAdapter.class.getName());
    super.execute();
  }

} /* ENDCLASS */
