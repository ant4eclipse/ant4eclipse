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
package org.ant4eclipse.pydt.test.builder;

import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

import org.ant4eclipse.pydt.model.project.PyDLTKProjectRole;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder which is used for the DLTK based python implementation.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class DLTKProjectBuilder extends EclipseProjectBuilder implements PythonProjectBuilder {

  private static enum Kind {
    prj, src, con
  }

  private static final String  NAME_BUILDPATH = ".buildpath";

  private static final String  ENC_UTF8       = "UTF-8";

  private List<BuildPathEntry> _buildpathentries;

  private BuildPathEntry       _sourceentry;

  private BuildPathEntry       _runtime;

  /**
   * Initialises this builder using the supplied project name.
   * 
   * @param projectname
   *          The name of the project used to be created. Neither <code>null</code> nor empty.
   */
  public DLTKProjectBuilder(final String projectname) {
    super(projectname);
    withNature(PyDLTKProjectRole.NATURE);
    withBuilder(PyDLTKProjectRole.BUILDCOMMAND);
    _buildpathentries = new ArrayList<BuildPathEntry>();
    _sourceentry = new BuildPathEntry();
    _sourceentry._combine = true;
    _sourceentry._exported = true;
    _sourceentry._kind = Kind.src;
    _sourceentry._path = "";
    _runtime = new BuildPathEntry();
    _runtime._combine = true;
    _runtime._exported = false;
    _runtime._kind = Kind.con;
    _runtime._path = "org.eclipse.dltk.launching.INTERPRETER_CONTAINER";
  }

  /**
   * {@inheritDoc}
   */
  public void useProject(final String projectname, final boolean export) {
    final BuildPathEntry entry = new BuildPathEntry();
    entry._exported = export;
    entry._kind = Kind.prj;
    entry._path = "/" + projectname;
    entry._combine = false;
    _buildpathentries.add(entry);
  }

  /**
   * {@inheritDoc}
   */
  protected void createArtefacts(final File projectdir) {
    super.createArtefacts(projectdir);
    writeBuildpath(new File(projectdir, NAME_BUILDPATH));
  }

  /**
   * Generates a <code>.buildpath</code> file representing the current pathes.
   * 
   * @param destination
   *          The destination where the file has to be written to.
   */
  private void writeBuildpath(final File destination) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"" + ENC_UTF8 + "\"?>");
    buffer.append(NL);
    buffer.append("<buildpath>");
    buffer.append(NL);
    buffer.append("  ");
    buffer.append(_sourceentry);
    buffer.append(NL);
    for (int i = 0; i < _buildpathentries.size(); i++) {
      buffer.append("  ");
      buffer.append(_buildpathentries.get(i));
      buffer.append(NL);
    }
    buffer.append("  ");
    buffer.append(_runtime);
    buffer.append(NL);
    buffer.append("</buildpath>");
    Utilities.writeFile(destination, buffer.toString(), ENC_UTF8);
  }

  /**
   * {@inheritDoc}
   */
  public void setSourceFolder(final String sourcename) {
    _sourceentry._path = sourcename;
  }

  /**
   * {@inheritDoc}
   */
  public void addSourceFolder(final String additionalfolder) {
    final BuildPathEntry entry = new BuildPathEntry();
    entry._combine = true;
    entry._exported = true;
    entry._kind = Kind.src;
    entry._path = additionalfolder;
    _buildpathentries.add(entry);
  }

  /**
   * Declaration of an ordinary datastructure.
   */
  private static class BuildPathEntry {

    public String  _path;

    public Kind    _kind;

    public boolean _exported;

    public boolean _combine;

    /**
     * {@inheritDoc}
     */
    public String toString() {
      return "<buildpathentry exported=\"" + _exported + "\" kind=\"" + _kind.name() + "\" path=\"" + _path
          + "\" combineaccessrules=\"" + _combine + "\"/>";
    }
  } /* ENDCLASS */

} /* ENDCLASS */
