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
package org.ant4eclipse.pydt.ant;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.core.ant.ExtendedBuildException;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pydt.model.pyre.PythonRuntime;
import org.ant4eclipse.pydt.model.pyre.PythonRuntimeRegistry;
import org.ant4eclipse.pydt.tools.PythonTools;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PydtDocumentationTask extends AbstractAnt4EclipseTask {

  private static final String SCRIPT                 = "import sys\n" + "if __name__ == \"__main__\":\n"
                                                         + "  sys.path.append(\"%s\")\n" + "  from %s import cli\n"
                                                         + "  sys.argv=[%s]\n" + "  cli.cli()\n";

  private static final String MSG_DOCS_NOT_SUPPORTED = "Generation of documentation is currently not supported for python with major version >= 3 !";

  private static final String MSG_MISSINGATTRIBUTE   = "The attribute '%s' has not been set !";

  private static final String MSG_NOTADIRECTORY      = "The path '%s' doesn't refer to a directory !";

  private static final String MSG_UNKNOWNRUNTIME     = "The runtime with the id '%s' is not registered !";

  private String              _runtimeid             = null;

  private File                _destdir               = null;

  private File                _sourcedir             = null;

  /**
   * Changes the id of the runtime used to access the python interpreter.
   * 
   * @param runtimeid
   *          The id of the runtime used to access the python interpreter.
   */
  public void setRuntime(final String runtimeid) {
    _runtimeid = Utilities.cleanup(runtimeid);
  }

  /**
   * Changes the destination directory where the documentation shall be written to.
   * 
   * @param destdir
   *          The destination directory where the documentation shall be written to.
   */
  public void setDestdir(final File destdir) {
    _destdir = destdir;
  }

  /**
   * Changes the sources directory used to create the documentation from.
   * 
   * @param sourcedir
   *          The sources directory used to create the documentation from.
   */
  public void setSourcedir(final File sourcedir) {
    _sourcedir = sourcedir;
  }

  /**
   * {@inheritDoc}
   */
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (_destdir == null) {
      throw new ExtendedBuildException(MSG_MISSINGATTRIBUTE, "destdir");
    }
    if (_sourcedir == null) {
      throw new ExtendedBuildException(MSG_MISSINGATTRIBUTE, "sourcedir");
    }
    if (_destdir.exists() && (!_destdir.isDirectory())) {
      throw new ExtendedBuildException(MSG_NOTADIRECTORY, _destdir);
    }
    if (!_sourcedir.isDirectory()) {
      throw new ExtendedBuildException(MSG_NOTADIRECTORY, _sourcedir);
    }
    if (_runtimeid == null) {
      throw new ExtendedBuildException(MSG_MISSINGATTRIBUTE, "runtime");
    }
    final PythonRuntimeRegistry registry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
    if (!registry.hasRuntime(_runtimeid)) {
      throw new ExtendedBuildException(MSG_UNKNOWNRUNTIME, _runtimeid);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    final PythonRuntimeRegistry registry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
    final PythonRuntime runtime = registry.getRuntime(_runtimeid);

    if (runtime.getVersion().getMajor() >= 3) {
      // unfortunately the syntax has changed, so we can't use epydoc with it
      A4ELogging.warn(MSG_DOCS_NOT_SUPPORTED);
      return;
    }

    final PythonTools pythontools = ServiceRegistry.instance().getService(PythonTools.class);
    final File executable = runtime.getExecutable();

    Utilities.mkdirs(_destdir);
    final String args = String.format("\"--html\", \"-o\", \"%s\", %s", pythonEscape(_destdir.getAbsolutePath()),
        collectModules());

    // generate the python script used to generate the documentation
    final File install = pythontools.getEpydocInstallation();
    final String name = Utilities.stripSuffix(install.getName());
    final String code = String.format(SCRIPT, pythonEscape(install.getAbsolutePath()), name, args);

    // save the script
    final File script = Utilities.createFile(code, ".py");

    // execute the script
    Utilities.execute(executable, null, script.getAbsolutePath());

  }

  private String pythonEscape(String str) {
    final StringTokenizer tokenizer = new StringTokenizer(str, "\\", true);
    final StringBuffer buffer = new StringBuffer();
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();
      buffer.append(token);
      if ("\\".equals(token)) {
        buffer.append("\\");
      }
    }
    return buffer.toString();
  }

  /**
   * Generates a comma separated list containing the modules used for the documentation generation.
   * 
   * @return A comma separated list containing the modules used for the documentation generation. Neither
   *         <code>null</code> nor empty.
   */
  private String collectModules() {
    final List<File> result = new ArrayList<File>();
    collectPackages(result, _sourcedir);
    StringBuffer buffer = new StringBuffer();
    if (result.size() > 0) {
      buffer.append("\"");
      buffer.append(pythonEscape(result.get(0).getAbsolutePath()));
      buffer.append("\"");
      for (int i = 1; i < result.size(); i++) {
        buffer.append(", \"");
        buffer.append(pythonEscape(result.get(i).getAbsolutePath()));
        buffer.append("\"");
      }
    }
    return buffer.toString();
  }

  /**
   * Returns <code>true</code> if the supplied directory refers to a package.
   * 
   * @param dir
   *          The directory that has to be tested. Not <code>null</code> and must be a directory.
   * 
   * @return <code>true</code> <=> The supplied directory is a package.
   */
  private boolean isPackage(final File dir) {
    final File child = new File(dir, "__init__.py");
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
  private void collectPackages(final List<File> receiver, final File current) {
    if (isPackage(current)) {
      receiver.add(current);
      return;
    }
    final File[] children = current.listFiles();
    for (final File child : children) {
      if (child.isDirectory()) {
        collectPackages(receiver, child);
      }
    }
  }

} /* ENDCLASS */
