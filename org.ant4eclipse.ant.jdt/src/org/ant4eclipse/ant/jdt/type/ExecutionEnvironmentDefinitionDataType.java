/**********************************************************************
 * Copyright (c) 2005-2010 ant4eclipse project team.
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
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.jdt.internal.model.jre.JavaProfileReader;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.apache.tools.ant.Project;

import java.io.File;

/**
 * <p>
 * Represents a definition of a execution environment.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecutionEnvironmentDefinitionDataType extends AbstractAnt4EclipseDataType {

  /** the target platform definition */
  private File   _file;

  /** jre id */
  private String _jreId;

  /**
   * <p>
   * Creates a new instance of type {@link ExecutionEnvironmentDefinitionDataType}.
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public ExecutionEnvironmentDefinitionDataType( Project project ) {
    super( project );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doValidate() {
    if( _file == null || "".equals( _file ) ) {
      throw new Ant4EclipseException( PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "file" );
    }
    if( _jreId == null || "".equals( _jreId ) ) {
      throw new Ant4EclipseException( PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "jreId" );
    }

    if(! _file.exists() ) {
      throw new RuntimeException( String.format( "Missing file: %s", _file ) );
    }
    
    A4ECore.instance().getRequiredService( JavaProfileReader.class ).registerProfile( _file, _jreId );
  }

  /**
   * <p>
   * </p>
   * 
   * @param newfile
   */
  public void setFile( File newfile ) {
    _file = newfile;
  }

  /**
   * <p>
   * </p>
   * 
   * @param jreId
   */
  public void setJreId( String jreId ) {
    _jreId = jreId;
  }

  /**
   * @see org.apache.tools.ant.types.DataType#toString()
   */
  @Override
  public String toString() {
    return String.format( "ExecutionEnvironmentDefinitionDataType [_file=%s]", _file );
  }

} /* ENDCLASS */
