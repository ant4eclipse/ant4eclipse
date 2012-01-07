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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.junit.Assert;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Creates Eclipse projects for test purposes.
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class EclipseProjectBuilder {

  private String       _projectName;

  private List<String> _natures;

  private List<String> _builders;

  private List<String> _referencedProjects;

  private List<File>   _resources;

  public EclipseProjectBuilder( String projectName ) {
    Assert.assertNotNull( projectName );
    _projectName = projectName;
    _natures = new ArrayList<String>();
    _builders = new ArrayList<String>();
    _resources = new ArrayList<File>();
    _referencedProjects = new ArrayList<String>();
  }

  /**
   * @return the projectName
   */
  public String getProjectName() {
    return _projectName;
  }

  public EclipseProjectBuilder withNature( String natureId ) {
    Assert.assertNotNull( natureId );
    _natures.add( natureId );
    return this;
  }

  public EclipseProjectBuilder withProjectReference( String referencedProject ) {
    Assert.assertNotNull( referencedProject );
    _referencedProjects.add( referencedProject );
    return this;
  }

  public EclipseProjectBuilder withBuilder( String buildCmd ) {
    Assert.assertNotNull( buildCmd );
    _builders.add( buildCmd );
    return this;
  }

  public EclipseProjectBuilder withResource( File resource ) {
    Assert.assertNotNull( resource );
    _resources.add( resource );
    return this;
  }

  /**
   * Creates this project
   * 
   * @param destinationDirectory
   *          the directory where this project(directory) should be created to
   * @return The project directory
   */
  public File createIn( File destinationDirectory ) {
    Assure.isDirectory( "destinationDirectory", destinationDirectory );
    File projectDir = new File( destinationDirectory, _projectName );
    Utilities.mkdirs( projectDir );
    createArtefacts( projectDir );
    return projectDir;
  }

  protected void createArtefacts( File projectDir ) {
    createProjectFile( projectDir );
    importResources( projectDir );
  }

  protected void importResources( File projectDir ) {
    for( File resource : _resources ) {
      try {
        File destfile = new File( projectDir, resource.getName() );
        Utilities.copy( resource.toURI().toURL(), destfile );
      } catch( MalformedURLException ex ) {
        Assert.fail( ex.getMessage() );
      }
    }
  }

  protected void createProjectFile( File projectDir ) {
    TextEmitter emitter = new TextEmitter();
    emitter.appendln( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
    emitter.appendln( "<projectDescription>" );
    emitter.indent();

    // head
    emitter.appendln( "<name>%s</name>", _projectName );
    emitter.appendln( "<comment/>" );

    // referenced projects
    emitter.appendln( "<projects>" );
    emitter.indent();
    for( String referencedProject : _referencedProjects ) {
      emitter.appendln( "<project>%s</project>", referencedProject );
    }
    emitter.dedent();
    emitter.appendln( "</projects>" );

    // builders
    emitter.appendln( "<buildSpec>" );
    emitter.indent();
    Iterator<String> it = _builders.iterator();
    while( it.hasNext() ) {
      String builder = it.next();
      emitter.appendln( "<buildCommand>" );
      emitter.indent();
      emitter.appendln( "<name>%s</name>", builder );
      emitter.appendln( "<arguments/>" );
      emitter.dedent();
      emitter.appendln( "</buildCommand>" );
    }
    emitter.dedent();
    emitter.appendln( "</buildSpec>" );

    // natures
    emitter.appendln( "<natures>" );
    emitter.indent();
    it = _natures.iterator();
    while( it.hasNext() ) {
      String nature = it.next();
      emitter.appendln( "<nature>%s</nature>", nature );
    }
    emitter.dedent();
    emitter.appendln( "</natures>" );

    emitter.dedent();
    emitter.appendln( "</projectDescription>" );

    File dotProjectFile = new File( projectDir, ".project" );
    Utilities.writeFile( dotProjectFile, emitter.toString(), "UTF-8" );
  }

} /* ENDCLASS */
