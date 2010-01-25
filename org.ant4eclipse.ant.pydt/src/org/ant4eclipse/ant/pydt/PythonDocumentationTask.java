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
package org.ant4eclipse.ant.pydt;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.lib.pydt.PydtExceptionCode;
import org.ant4eclipse.lib.pydt.model.pyre.PythonRuntime;
import org.ant4eclipse.lib.pydt.model.pyre.PythonRuntimeRegistry;
import org.ant4eclipse.lib.pydt.tools.PythonTools;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonDocumentationTask extends AbstractAnt4EclipseTask {

  private static final String SCRIPT                 = "import sys\n" + "if __name__ == \"__main__\":\n"
                                                         + "  sys.path.append(\"%s\")\n" + "  from %s import cli\n"
                                                         + "  sys.argv=[]\n%s" + "  cli.cli()\n";

  private static final String MSG_DOCS_NOT_SUPPORTED = "Generation of documentation is currently not supported for python with major version >= 3 !";

  private String              _runtimeid             = null;

  private File                _destdir               = null;

  private File                _sourcedir             = null;

  /**
   * Changes the id of the runtime used to access the python interpreter.
   * 
   * @param runtimeid
   *          The id of the runtime used to access the python interpreter.
   */
  public void setRuntime(String runtimeid) {
    this._runtimeid = Utilities.cleanup(runtimeid);
  }

  /**
   * Changes the destination directory where the documentation shall be written to.
   * 
   * @param destdir
   *          The destination directory where the documentation shall be written to.
   */
  public void setDestdir(File destdir) {
    this._destdir = destdir;
  }

  /**
   * Changes the sources directory used to create the documentation from.
   * 
   * @param sourcedir
   *          The sources directory used to create the documentation from.
   */
  public void setSourcedir(File sourcedir) {
    this._sourcedir = sourcedir;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (this._destdir == null) {
      throw new Ant4EclipseException(PydtExceptionCode.MISSINGATTRIBUTE, "destdir");
    }
    if (this._sourcedir == null) {
      throw new Ant4EclipseException(PydtExceptionCode.MISSINGATTRIBUTE, "sourcedir");
    }
    if (this._destdir.exists() && (!this._destdir.isDirectory())) {
      throw new Ant4EclipseException(PydtExceptionCode.NOTADIRECTORY, this._destdir);
    }
    if (!this._sourcedir.isDirectory()) {
      throw new Ant4EclipseException(PydtExceptionCode.NOTADIRECTORY, this._sourcedir);
    }
    if (this._runtimeid == null) {
      throw new Ant4EclipseException(PydtExceptionCode.MISSINGATTRIBUTE, "runtime");
    }
    PythonRuntimeRegistry registry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
    if (!registry.hasRuntime(this._runtimeid)) {
      throw new Ant4EclipseException(PydtExceptionCode.UNKNOWN_PYTHON_RUNTIME, this._runtimeid);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    PythonRuntimeRegistry registry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
    PythonRuntime runtime = registry.getRuntime(this._runtimeid);

    if (runtime.getVersion().getMajor() >= 3) {
      // unfortunately the syntax has changed, so we can't use epydoc with it
      A4ELogging.warn(MSG_DOCS_NOT_SUPPORTED);
      return;
    }

    PythonTools pythontools = ServiceRegistry.instance().getService(PythonTools.class);
    File executable = runtime.getExecutable();

    Utilities.mkdirs(this._destdir);

    // setup some options for the commandline
    StringBuffer options = new StringBuffer();
    appendOption(options, "--html");
    appendOption(options, "-o");
    appendOption(options, pythonEscape(this._destdir.getAbsolutePath()));
    collectModules(options);

    // generate the python script used to generate the documentation
    File install = pythontools.getEpydocInstallation();
    String name = Utilities.stripSuffix(install.getName());
    String code = String.format(SCRIPT, pythonEscape(install.getAbsolutePath()), name, options);

    // save the script
    File script = Utilities.createFile(code, ".py", "ASCII");

    // execute the script
    Utilities.execute(executable, null, script.getAbsolutePath());

  }

  /**
   * Makes sure that backslashes come in double packs. Otherwise the used interpreter may fail.
   * 
   * @param str
   *          The path which requires to be altered. Neither <code>null</code> nor empty.
   * 
   * @return The altered path. Neither <code>null</code> nor empty.
   */
  private String pythonEscape(String str) {
    return str.replaceAll("\\\\", "\\\\\\\\");
  }

  /**
   * Appends a single commandline option to a buffer.
   * 
   * @param buffer
   *          The buffer used to be extended with an additional option. Not <code>null</code>.
   * @param option
   *          The option that has to be added. Neither <code>null</code> nor empty.
   */
  private void appendOption(StringBuffer buffer, String option) {
    buffer.append("  sys.argv.append(\"" + option + "\")\n");
  }

  /**
   * Generates a comma separated list containing the modules used for the documentation generation.
   * 
   * @param options
   *          The buffer used to collect the package locations as single options added to the commandline. Not
   *          <code>null</code>.
   */
  private void collectModules(StringBuffer options) {
    List<File> result = new ArrayList<File>();
    collectPackages(result, this._sourcedir);
    if (result.size() > 0) {
      appendOption(options, pythonEscape(result.get(0).getAbsolutePath()));
      for (int i = 1; i < result.size(); i++) {
        appendOption(options, pythonEscape(result.get(i).getAbsolutePath()));
      }
    }
  }

  /**
   * Returns <code>true</code> if the supplied directory refers to a package.
   * 
   * @param dir
   *          The directory that has to be tested. Not <code>null</code> and must be a directory.
   * 
   * @return <code>true</code> <=> The supplied directory is a package.
   */
  private boolean isPackage(File dir) {
    File child = new File(dir, "__init__.py");
    return child.isFile();
  }

  /**
   * This collector recursively traverses a filesystem while collecting each directory corresponding to a package. If a
   * package will be detected it will no longer be traversed since this is performed by the <i>epydoc</i> tool.
   * 
   * @param receiver
   *          The list used to collect the package location. Not <code>null</code>.
   * @param current
   *          The current location within the filesystem. Not <code>null</code> and must be a directory.
   */
  private void collectPackages(List<File> receiver, File current) {
    if (isPackage(current)) {
      receiver.add(current);
      return;
    }
    File[] children = current.listFiles();
    for (File child : children) {
      if (child.isDirectory()) {
        collectPackages(receiver, child);
      }
    }
  }

} /* ENDCLASS */
