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
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pydt.model.project.DLTKProjectRole;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder which is used for the DLTK based python implementation.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class DLTKProjectBuilder extends AbstractPythonProjectBuilder {

  private static enum Kind {
    prj, src, con, lib
  }

  private static final String  NAME_BUILDPATH = ".buildpath";

  private static final String  ENC_UTF8       = "UTF-8";

  private static final String  INDENT         = "  ";

  private List<BuildPathEntry> _buildpathentries;

  private List<BuildPathEntry> _internallibs;

  private BuildPathEntry       _sourceentry;

  private BuildPathEntry       _runtime;

  /**
   * Initialises this builder using the supplied project name.
   * 
   * @param projectname
   *          The name of the project used to be created. Neither <code>null</code> nor empty.
   */
  public DLTKProjectBuilder( String projectname ) {
    super( projectname );
    withNature( DLTKProjectRole.NATURE );
    withBuilder( DLTKProjectRole.BUILDCOMMAND );
    _internallibs = new ArrayList<BuildPathEntry>();
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
  @Override
  public void useProject( String projectname, boolean export ) {
    BuildPathEntry entry = new BuildPathEntry();
    entry._exported = export;
    entry._kind = Kind.prj;
    entry._path = "/" + projectname;
    entry._combine = false;
    _buildpathentries.add( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createArtefacts( File projectdir ) {
    super.createArtefacts( projectdir );
    writeBuildpath( new File( projectdir, NAME_BUILDPATH ) );
    writeInternalLibraries( projectdir );
  }

  /**
   * Generates a <code>.buildpath</code> file representing the current pathes.
   * 
   * @param destination
   *          The destination where the file has to be written to.
   */
  private void writeBuildpath( File destination ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "<?xml version=\"1.0\" encoding=\"" + ENC_UTF8 + "\"?>" );
    buffer.append( Utilities.NL );
    buffer.append( "<buildpath>" );
    buffer.append( Utilities.NL );
    buffer.append( INDENT );
    buffer.append( _sourceentry );
    buffer.append( Utilities.NL );
    for( int i = 0; i < _buildpathentries.size(); i++ ) {
      if( _buildpathentries.get( i )._kind != Kind.prj ) {
        buffer.append( INDENT );
        buffer.append( _buildpathentries.get( i ) );
        buffer.append( Utilities.NL );
      }
    }
    for( int i = 0; i < _internallibs.size(); i++ ) {
      buffer.append( INDENT );
      buffer.append( _internallibs.get( i ) );
      buffer.append( Utilities.NL );
    }
    buffer.append( INDENT );
    buffer.append( _runtime );
    buffer.append( Utilities.NL );
    for( int i = 0; i < _buildpathentries.size(); i++ ) {
      if( _buildpathentries.get( i )._kind == Kind.prj ) {
        buffer.append( INDENT );
        buffer.append( _buildpathentries.get( i ) );
        buffer.append( Utilities.NL );
      }
    }
    buffer.append( "</buildpath>" );
    Utilities.writeFile( destination, buffer.toString(), ENC_UTF8 );
  }

  /**
   * Exports the internal libraries into the project folder.
   * 
   * @param destination
   *          The destination directory of the project. Not <code>null</code> and must be a valid directory.
   */
  private void writeInternalLibraries( File destination ) {
    for( int i = 0; i < _internallibs.size(); i++ ) {
      BuildPathEntry entry = _internallibs.get( i );
      File destfile = new File( destination, entry._path );
      Utilities.mkdirs( destfile.getParentFile() );
      Utilities.copy( entry._source, destfile );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSourceFolder( String sourcename ) {
    _sourceentry._path = sourcename;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSourceFolder( String additionalfolder ) {
    BuildPathEntry entry = new BuildPathEntry();
    entry._combine = true;
    entry._exported = true;
    entry._kind = Kind.src;
    entry._path = additionalfolder;
    _buildpathentries.add( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String importInternalLibrary( URL location ) {
    BuildPathEntry entry = new BuildPathEntry();
    entry._combine = true;
    entry._exported = true;
    entry._kind = Kind.lib;
    String file = location.getFile();
    int lidx = file.lastIndexOf( '/' );
    entry._path = "lib/" + (lidx == -1 ? file : file.substring( lidx + 1 ));
    entry._source = location;
    _internallibs.add( entry );
    return entry._path;
  }

  /**
   * Declaration of an ordinary datastructure.
   */
  private static class BuildPathEntry {

    public String  _path;

    public Kind    _kind;

    public boolean _exported;

    public boolean _combine;

    public URL     _source;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      return "<buildpathentry exported=\"" + _exported + "\" kind=\"" + _kind.name() + "\" path=\""
          + _path + "\" combineaccessrules=\"" + _combine + "\"/>";
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
