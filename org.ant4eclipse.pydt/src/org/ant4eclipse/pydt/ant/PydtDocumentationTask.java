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
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pydt.model.PythonInterpreter;
import org.ant4eclipse.pydt.model.pyre.PythonRuntime;
import org.ant4eclipse.pydt.model.pyre.PythonRuntimeRegistry;
import org.ant4eclipse.pydt.tools.PythonTools;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

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

  private static final String MSG_MISSINGATTRIBUTE   = "The attribute '%s' has not been set !";

  private static final String MSG_MISSINGSOURCES     = "Neither the attribute 'sourcedir' nor a nested 'fileset' element has been specified !";

  private static final String MSG_NOTADIRECTORY      = "The path '%s' doesn't refer to a directory !";

  private static final String MSG_UNKNOWNRUNTIME     = "The runtime with the id '%s' is not registered !";

  private static final String MSG_UNSUPPORTEDRUNTIME = "The runtime with the id '%s' is not supported !";

  private String              _runtimeid;

  private File                _destdir;

  private File                _sourcedir;

  private List<FileSet>       _filesets;

  /**
   * Initialises this task.
   */
  public PydtDocumentationTask() {
    _runtimeid = null;
    _destdir = null;
    _sourcedir = null;
    _filesets = new ArrayList<FileSet>();
  }

  /**
   * Add a set of files used to create the documentation from.
   * 
   * @param set
   *          A set of files to create the documentation from.
   */
  public void addFileset(final FileSet set) {
    _filesets.add(set);
  }

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
    if (_destdir.exists() && (!_destdir.isDirectory())) {
      throw new ExtendedBuildException(MSG_NOTADIRECTORY, _destdir);
    }
    if (_sourcedir != null) {
      if (!_sourcedir.isDirectory()) {
        throw new ExtendedBuildException(MSG_NOTADIRECTORY, _sourcedir);
      }
    } else {
      if (_filesets.isEmpty()) {
        throw new BuildException(MSG_MISSINGSOURCES);
      }
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
    final PythonTools pythontools = ServiceRegistry.instance().getService(PythonTools.class);
    final PythonRuntime runtime = registry.getRuntime(_runtimeid);
    final PythonInterpreter interpreter = registry.lookupInterpreter(runtime);
    if (interpreter == null) {
      throw new ExtendedBuildException(MSG_UNSUPPORTEDRUNTIME, _runtimeid);
    }
    final File executable = interpreter.lookup(runtime.getLocation());
    String[] modules = collectModules();
    StringBuffer buffer = new StringBuffer();
    if (modules.length > 0) {
      buffer.append(pythonEscape(modules[0]));
      for (int i = 1; i < modules.length; i++) {
        buffer.append(pythonEscape(modules[i]));
      }
    }
    _destdir.mkdirs();
    final String args = String.format("\"--html\", \"-o\", \"%s\", \"%s\"", pythonEscape(_destdir.getAbsolutePath()),
        buffer);
    File install = pythontools.getEpydocInstallation();
    install = new File(install.getParentFile(), "epydoc.zip");
    final String name = Utilities.stripSuffix(install.getName());
    final String code = String.format(SCRIPT, pythonEscape(install.getAbsolutePath()), name, args);
    final File script = Utilities.createFile(code, ".py");
    StringBuffer output = new StringBuffer();
    StringBuffer error = new StringBuffer();
    int result = Utilities.execute(executable, output, error, script.getAbsolutePath());
    System.err.println("output: " + output);
    System.err.println("error: " + error);
    if (result != 0) {
      throw new BuildException("BLA");
    }
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

  private String[] collectModules() {
    final List<String> result = new ArrayList<String>();
    result.add(_sourcedir.getAbsolutePath());
    return result.toArray(new String[result.size()]);
  }

} /* ENDCLASS */
