/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.pde.tools.ejc;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.ejc.CompileJobResult;
import org.ant4eclipse.jdt.tools.ejc.EjcAdapter;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * A LibraryBuilder implementation that uses the Eclipse compiler to compile the classes for the library.
 * 
 * <p>
 * The advantage of using this builder over the JavacLibraryCompiler is a more OSGi spec compliant behaviour of the
 * compiler. While the JavacLibraryCompiler is <b>not</b> able to correctly work with <code>import-package</code> and
 * <code>export-package</code> directives defined in the bundle's MANIFEST file, the EclipseLibraryCompiler is. The
 * EclipseLibraryCompiler only makes those classes visible to the Compiler that are im- resp. exported by the
 * appropriate entries in the bundle's MANIFEST.
 * 
 * <p>
 * <b>Using this task requires the Eclipse java compiler on the classpath!</b>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class EclipseLibraryCompiler implements PluginLibraryCompiler {

  /** - */
  private EclipseLibraryCompileJobDescription _compileJobDescription;

  /** - */
  private final EjcAdapter                    _ejcAdapter;

  /**
   * 
   */
  public EclipseLibraryCompiler() {
    this._ejcAdapter = EjcAdapter.Factory.create();
  }

  public String getName() {
    return "Eclipse Compiler for Java";
  }

  public void setEclipseProject(final EclipseProject eclipseProject) {
    this._compileJobDescription = new EclipseLibraryCompileJobDescription(eclipseProject);
  }

  public void compile(final PluginLibraryBuilderContext context) {
    Assert.notNull(context);

    this._compileJobDescription.setContext(context);
    final CompileJobResult result = this._ejcAdapter.compile(this._compileJobDescription);

    if (!result.succeeded()) {
      result.dumpProblems();
      // TODO
      throw new RuntimeException("Not successful");
    }
  }

}
