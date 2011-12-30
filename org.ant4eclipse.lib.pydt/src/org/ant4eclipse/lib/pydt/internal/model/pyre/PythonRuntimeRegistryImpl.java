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
package org.ant4eclipse.lib.pydt.internal.model.pyre;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.data.Version;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pydt.PydtExceptionCode;
import org.ant4eclipse.lib.pydt.model.PythonInterpreter;
import org.ant4eclipse.lib.pydt.model.pyre.PythonRuntime;
import org.ant4eclipse.lib.pydt.model.pyre.PythonRuntimeRegistry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Implementation of a registry for {@link PythonRuntime} instances.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonRuntimeRegistryImpl implements PythonRuntimeRegistry, Lifecycle {

  private static final String        PROP_INTERPRETER         = "interpreter.";

  private static final String        MSG_FAILEDTOREADOUTPUT   = "Failed to read the output. Cause: %s";

  private static final String        MSG_INVALIDOUTPUT        = "The executable '%s' produced invalid output.\nOutput:\n%sError:\n%s";

  private static final String        MSG_REGISTEREDRUNTIME    = "Registered runtime with id '%s' for the location '%s'.";

  private static final String        MSG_REPEATEDREGISTRATION = "A python runtime with the id '%s' and the location '%s' has been registered multiple times !";

  private static final String        MARKER_BEGIN             = "ANT4ECLIPSE-BEGIN";

  private static final String        MARKER_END               = "ANT4ECLIPSE-END";

  private static final String        NAME_SITEPACKAGES        = "site-packages";

  private Map<String, PythonRuntime> _runtimes                = new Hashtable<String, PythonRuntime>();

  private String                     _defaultid               = null;

  private File                       _pythonlister            = null;

  private File                       _listerdir               = null;

  private File                       _currentdir              = null;

  private PythonInterpreter[]        _interpreters            = null;

  private boolean                    _initialised             = false;

  /**
   * Tries to determine the location of a python interpreter.
   * 
   * @param location
   *          The location of a python installation. Not <code>null</code>.
   * 
   * @return The interpreter or <code>null</code> if none could be found.
   */
  private PythonInterpreter lookupInterpreter(File location) {
    for (PythonInterpreter interpreter : this._interpreters) {
      File result = interpreter.lookup(location);
      if (result != null) {
        return interpreter;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRuntime(String id) {
    Assure.nonEmpty("id", id);
    return this._runtimes.containsKey(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerRuntime(String id, File location, boolean sitepackages) {

    Assure.nonEmpty("id", id);
    Assure.notNull("location", location);

    location = Utilities.getCanonicalFile(location);

    PythonRuntime existing = getRuntime(id);
    if (existing != null) {

      // check the current setting

      if (!location.equals(existing.getLocation())) {
        // same id for different locations is not allowed
        throw new Ant4EclipseException(PydtExceptionCode.DUPLICATERUNTIME, id, existing.getLocation(), location);
      } else {
        // same record, so skip this registration while creating a message only
        A4ELogging.debug(MSG_REPEATEDREGISTRATION, id, location);
        return;
      }

    }

    // register the new runtime but we need to identify the corresponding libraries
    PythonInterpreter python = lookupInterpreter(location);
    if (python == null) {
      throw new Ant4EclipseException(PydtExceptionCode.UNSUPPORTEDRUNTIME, id, location);
    }
    File interpreter = python.lookup(location);

    // launch the python lister script to access the python path
    StringBuffer output = new StringBuffer();
    StringBuffer error = new StringBuffer();
    Utilities.execute(interpreter, output, error, this._pythonlister.getAbsolutePath());

    String[] extraction = extractOutput(output.toString(), sitepackages);
    if (extraction == null) {
      // returncode is 0
      A4ELogging.debug(MSG_INVALIDOUTPUT, interpreter, output, error);
      throw new Ant4EclipseException(PydtExceptionCode.UNSUPPORTEDRUNTIME, id, location);
    }

    // load version number and library records
    Version version = Version.newStandardVersion(extraction[0]);
    File[] libs = new File[extraction.length - 1];
    for (int i = 0; i < libs.length; i++) {
      libs[i] = new File(extraction[i + 1]);
    }

    PythonRuntime newruntime = new PythonRuntimeImpl(id, location, version, libs, python);
    A4ELogging.debug(MSG_REGISTEREDRUNTIME, id, location);
    this._runtimes.put(id, newruntime);

  }

  /**
   * This function parses the output and returns the output as a list of strings.
   * 
   * @param content
   *          The output that has to be parsed. Neither <code>null</code> nor empty.
   * @param sitepackages
   *          <code>true</code> <=> Enable support for site packages on the runtime.
   * 
   * @return A list of strings containing the runtime information or <code>null</code> in case of a failure.
   */
  private String[] extractOutput(String content, boolean sitepackages) {
    List<String> list = new ArrayList<String>();
    boolean collect = false;
    boolean first = true;
    BufferedReader reader = new BufferedReader(new StringReader(content));
    try {
      String line = reader.readLine();
      while (line != null) {
        line = line.trim();
        if (MARKER_BEGIN.equals(line)) {
          collect = true;
        } else if (MARKER_END.equals(line)) {
          collect = true;
        } else if (collect) {
          if (first) {
            // the first line provides the versioning information
            list.add(line);
            first = false;
          } else {
            // the brackets are just a security precaution just for the case that a path
            // starts or ends with whitespace characters
            int open = line.indexOf('[');
            int close = line.lastIndexOf(']');
            line = line.substring(open + 1, close);
            if (!isHiddenDir(line, sitepackages)) {
              list.add(line);
            }
          }
        }
        line = reader.readLine();
      }
      if (list.size() < 2) {
        // there must be at least the version information and one directory
        return null;
      } else {
        return list.toArray(new String[list.size()]);
      }
    } catch (IOException ex) {
      A4ELogging.debug(MSG_FAILEDTOREADOUTPUT, ex.getMessage());
      return null;
    }
  }

  /**
   * Returns <code>true</code> if the supplied directory path is considered to be hidden. This is the case when the path
   * is just a symbol (f.e. __classpath__ under jython), the path is the working directory of our python script or the
   * path is the current directory.
   * 
   * @param dir
   *          The potential directory used to be tested. Neither <code>null</code> nor empty.
   * @param sitepackages
   *          <code>true</code> <=> Enable support for site packages on the runtime.
   * 
   * @return <code>true</code> <=> The supplied path is not a valid part of the runtime and shall be hidden for that
   *         reason.
   * 
   * @throws IOException
   *           Path calculation failed for some reason.
   */
  private boolean isHiddenDir(String dir, boolean sitepackages) throws IOException {
    File file = new File(dir);
    if (!file.exists()) {
      // this directory does not exist (f.e. __classpath__ symbols provided by jython)
      return true;
    }
    if ((!sitepackages) && NAME_SITEPACKAGES.equals(file.getName())) {
      return true;
    }
    file = file.getCanonicalFile();
    return this._listerdir.equals(file) || this._currentdir.equals(file);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDefaultRuntime(String id) {
    Assure.nonEmpty("id", id);
    if (!hasRuntime(id)) {
      throw new Ant4EclipseException(PydtExceptionCode.INVALIDDEFAULTID, id);
    }
    this._defaultid = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PythonRuntime getRuntime() {
    if (this._defaultid != null) {
      return this._runtimes.get(this._defaultid);
    } else if (this._runtimes.size() == 1) {
      return this._runtimes.values().iterator().next();
    }
    throw new Ant4EclipseException(PydtExceptionCode.NODEFAULTRUNTIME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PythonRuntime getRuntime(String id) {
    Assure.nonEmpty("id", id);
    return this._runtimes.get(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PythonInterpreter[] getSupportedInterpreters() {
    return this._interpreters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    this._runtimes.clear();
    Utilities.delete(this._pythonlister);
    this._defaultid = null;
    this._pythonlister = null;
    this._listerdir = null;
    this._currentdir = null;
    this._interpreters = null;
    this._initialised = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize() {

    // export the python lister script, so it can be executed in order to access the pythonpath
    this._pythonlister = Utilities.exportResource("/org/ant4eclipse/lib/pydt/lister.py");
    if (!this._pythonlister.isAbsolute()) {
      this._pythonlister = this._pythonlister.getAbsoluteFile();
    }
    this._listerdir = this._pythonlister.getParentFile();
    this._currentdir = new File(".");
    this._listerdir = Utilities.getCanonicalFile(this._listerdir);
    this._currentdir = Utilities.getCanonicalFile(this._currentdir);

    // load the python interpreter configurations
    URL cfgurl = getClass().getResource("/org/ant4eclipse/lib/pydt/python.properties");
    if (cfgurl == null) {
      throw new Ant4EclipseException(PydtExceptionCode.MISSINGPYTHONPROPERTIES);
    }
    StringMap props = new StringMap(cfgurl);
    List<PythonInterpreter> interpreters = new ArrayList<PythonInterpreter>();
    for (Map.Entry<String, String> entry : props.entrySet()) {
      if (entry.getKey().startsWith(PROP_INTERPRETER)) {
        String name = entry.getKey().substring(PROP_INTERPRETER.length());
        String[] exes = Utilities.cleanup(entry.getValue().split(","));
        if (exes == null) {
          throw new Ant4EclipseException(PydtExceptionCode.MISSINGEXECUTABLES, entry.getKey());
        }
        Arrays.sort(exes);
        interpreters.add(new PythonInterpreter(name, exes));
      }
    }
    this._interpreters = interpreters.toArray(new PythonInterpreter[interpreters.size()]);
    Arrays.sort(this._interpreters);
    this._initialised = true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInitialized() {
    return this._initialised;
  }

} /* ENDCLASS */
