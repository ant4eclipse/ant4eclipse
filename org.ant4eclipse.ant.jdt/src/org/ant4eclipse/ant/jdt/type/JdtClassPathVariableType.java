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
package org.ant4eclipse.ant.jdt.type;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * Ant type to define class path variables. A class path variable can be added to a project's class path. It can be used
 * to define the location of a JAR file or a directory that isn't part of the workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtClassPathVariableType extends AbstractAnt4EclipseDataType {

  /** the name of the class path variable */
  private String _name    = null;

  /** the path of this class path variable */
  private File   _path    = null;

  /** the location of the properties file containing the declarations */
  private File   _varfile = null;

  /**
   * <p>
   * Creates a new instance of type ClasspathVariables.
   * </p>
   * 
   * @param project
   */
  public JdtClassPathVariableType( Project project ) {
    super( project );
  }

  /**
   * <p>
   * Sets the location of a properties file which contains the class path variables.
   * </p>
   * 
   * @param varfile
   *          A properties file pointing to a set of class path variables.
   */
  public void setFile( File varfile ) {
    this._varfile = varfile;
  }

  /**
   * <p>
   * Returns the name of the class path variable.
   * </p>
   * 
   * @return the name the name of the class path variable
   */
  public String getName() {
    return this._name;
  }

  /**
   * <p>
   * Sets the name of the class path variable.
   * </p>
   * 
   * @param name
   *          the name to set
   */
  public void setName( String name ) {
    this._name = Utilities.cleanup( name );
  }

  /**
   * <p>
   * Returns the path of the class path variable.
   * </p>
   * 
   * @return the path of the class path variable.
   */
  public File getPath() {
    return this._path;
  }

  /**
   * <p>
   * Sets the path of the class path variable.
   * </p>
   * 
   * @param path
   *          the path of the class path variable.
   */
  public void setPath( File path ) {
    this._path = path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doValidate() {

    Map<String,File> classpathvars = new Hashtable<String,File>();

    if( (this._path != null) && (this._name != null) ) {
      classpathvars.put( this._name, this._path );
    } else if( this._path != null ) {
      throw new BuildException( "Missing parameter 'name' on classpathVariable!" );
    } else if( this._name != null ) {
      throw new BuildException( "Missing parameter 'path' on classpathVariable!" );
    }

    // load the classpath variables from the file
    if( this._varfile != null ) {
      StringMap map = new StringMap( this._varfile );
      for( Map.Entry<String,String> pair : map.entrySet() ) {
        classpathvars.put( pair.getKey(), new File( pair.getValue() ) );
      }
    }

    ClassPathElementsRegistry variablesRegistry = A4ECore.instance().getRequiredService(
        ClassPathElementsRegistry.class );
    variablesRegistry.registerClassPathVariables( classpathvars );
  }

} /* ENDCLASS */
