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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseTask;
import org.ant4eclipse.ant.platform.core.delegate.WorkspaceDelegate;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.model.product.ProductDefinition;
import org.ant4eclipse.lib.pde.model.product.ProductDefinitionParser;
import org.ant4eclipse.lib.pde.model.product.ProductOs;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject.PathStyle;
import org.apache.tools.ant.BuildException;
import org.osgi.framework.Version;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This task allows to request several information from an eclipse product configuration.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class QueryProductTask extends AbstractAnt4EclipseTask {

  /**
   * The query type is used to select the kind of data that is being requested.
   */
  public static enum QueryType {

    /** - */
    id,

    /** - */
    name,

    /** - */
    basedonfeatures,

    /** - */
    configini,

    /** - */
    programargs,

    /** - */
    vmargs,

    /** - */
    application,

    /** - */
    version,

    /** - */
    launchername,

    /** - */
    wsplugins,

    /** - */
    wsfragments,

    /** - */
    wsfeatures,

    /** - */
    plugins,

    /** - */
    fragments,

    /** - */
    features;

  }

  /** - */
  private File              _product;

  /** - */
  private String            _delimiter;

  /** - */
  private WorkspaceDelegate _workspacedelegate;

  /** - */
  private boolean           _defaultisempty;

  /** - */
  private List<Query>       _queries;

  /**
   * Initialises this task with default values.
   */
  public QueryProductTask() {
    super();
    _workspacedelegate = new WorkspaceDelegate( this );
    _queries = new ArrayList<Query>();
    _delimiter = ",";
    _product = null;
    _defaultisempty = true;
  }

  /**
   * Enables/disables the property setting for the case no value is available.
   * 
   * @param newdefaultisempty
   *          <code>true</code> <=> Enables the property setting (empty value).
   */
  public void setDefaultIsEmpty( boolean newdefaultisempty ) {
    _defaultisempty = newdefaultisempty;
  }

  /**
   * Sets the workspace directory. Only necessary for queries trying to access filesystem information.
   * 
   * @param workspace
   *          The workspace directory. Not <code>null</code>.
   */
  public void setWorkspaceDirectory( String workspace ) {
    _workspacedelegate.setWorkspaceDirectory( workspace );
  }

  /**
   * Changes the location of the product configuration file.
   * 
   * @param newproduct
   *          The new location of the product file. Not <code>null</code>.
   */
  public void setProduct( File newproduct ) {
    _product = newproduct;
  }

  /**
   * Changes the delimiter which is used for list values.
   * 
   * @param newdelimiter
   *          The delimiter to be used for list values.
   */
  public void setDelimiter( String newdelimiter ) {
    _delimiter = Utilities.cleanup( newdelimiter );
  }

  /**
   * Adds the supplied query to this task.
   * 
   * @param newquery
   *          The new query which has to be added.
   */
  public void addConfiguredQuery( Query newquery ) {
    _queries.add( newquery );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if( _product == null ) {
      throw new BuildException( "The attribute 'product' has to be set." );
    }
    if( !_product.isFile() ) {
      throw new BuildException( String.format( "The product configuration '%s' is not a regular file.", _product ) );
    }
    if( _queries.isEmpty() ) {
      throw new BuildException( "There must be at least one <query> element." );
    }
    for( Query query : _queries ) {
      if( query._property == null ) {
        throw new BuildException( "The attribute 'property' has to be set on a query." );
      }
      if( query._type == null ) {
        throw new BuildException( String.format(
            "The attribute 'query' has to be set to one of the following values: %s",
            Utilities.listToString( QueryType.values(), null ) ) );
      }
      if( (query._type == QueryType.configini) || (query._type == QueryType.wsplugins)
          || (query._type == QueryType.wsfragments) || (query._type == QueryType.wsfeatures) ) {
        _workspacedelegate.requireWorkspaceDirectoryOrWorkspaceIdSet();
      }
    }
  }

  /**
   * Loads the product definition.
   * 
   * @return The loaded product definition. Not <code>null</code>.
   */
  private ProductDefinition loadProductDefinition() {
    InputStream instream = null;
    try {
      instream = new FileInputStream( _product );
      return ProductDefinitionParser.parseProductDefinition( instream );
    } catch( Ant4EclipseException ex ) {
      if( ex.getExceptionCode() == PdeExceptionCode.INVALID_CONFIGURATION_VALUE ) {
        throw new Ant4EclipseException( PdeExceptionCode.INVALID_PRODUCT_DEFINITION, _product, ex.getMessage() );
      } else {
        throw ex;
      }
    } catch( IOException ex ) {
      throw new BuildException( ex );
    } finally {
      Utilities.close( (Closeable) instream );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {
    ProductDefinition productdef = loadProductDefinition();
    for( Query query : _queries ) {
      String value = runQuery( query, productdef );
      if( value != null ) {
        getProject().setProperty( query._property, value );
      } else {
        if( _defaultisempty ) {
          getProject().setProperty( query._property, "" );
        }
      }
    }
  }

  /**
   * Evaluates a single query.
   * 
   * @param query
   *          The query that has to be evaluated. Not <code>null</code>.
   * @param productdef
   *          The product definition providing the necessary information. Not <code>null</code>.
   * 
   * @return The value of this query. Maybe <code>null</code>.
   */
  private String runQuery( Query query, ProductDefinition productdef ) {
    switch( query._type ) {
    case plugins:
      return Utilities.listToString( productdef.getPluginIds(), _delimiter );
    case fragments:
      return Utilities.listToString( productdef.getFragmentIds(), _delimiter );
    case features:
      return Utilities.listToString( productdef.getFeatureIds(), _delimiter );
    case wsplugins:
      return getWorkspaceContained( productdef.getPluginIds() );
    case wsfragments:
      return getWorkspaceContained( productdef.getFragmentIds() );
    case wsfeatures:
      return getWorkspaceContained( productdef.getFeatureIds() );
    case launchername:
      return productdef.getLaunchername();
    case application:
      return productdef.getApplication();
    case programargs:
      return getArgs( productdef.getProgramArgs( query._os ) );
    case vmargs:
      return getArgs( productdef.getVmArgs( query._os ) );
    case basedonfeatures:
      return String.valueOf( productdef.isBasedOnFeatures() );
    case id:
      return productdef.getId();
    case name:
      return productdef.getName();
    case version:
      Version version = productdef.getVersion();
      if( version != null ) {
        return version.toString();
      } else {
        return null;
      }
    case configini:
      String configini = productdef.getConfigIni( query._os );
      if( configini != null ) {
        int idx = configini.indexOf( '/', 1 );
        String projectname = configini.substring( 1, idx );
        String path = configini.substring( idx + 1 );
        EclipseProject project = _workspacedelegate.getWorkspace().getProject( projectname );
        return project.getChild( path, PathStyle.ABSOLUTE ).getAbsolutePath();
      } else {
        return null;
      }
    default:
      throw new BuildException( String.format( "The query type '%s' is currently not implemented.", query._type ) );
    }
  }

  /**
   * Filters the supplied list of ids by those that are located within the workspace.
   * 
   * @param ids
   *          The ids which have to be filtered. Not <code>null</code>.
   * 
   * @return The value providing the list of ids available within the workspace. Not <code>null</code>.
   */
  private String getWorkspaceContained( String[] ids ) {
    List<String> list = new ArrayList<String>();
    for( String id : ids ) {
      if( _workspacedelegate.getWorkspace().getProject( id ) != null ) {
        list.add( id );
      }
    }
    if( list.isEmpty() ) {
      return null;
    }
    return Utilities.listToString( list.toArray(), _delimiter );
  }

  /**
   * Concatenates the supplied arguments/argumentlists.
   * 
   * @param args
   *          The arguments/argumentlists that have to be concatenated.
   * 
   * @return The resulting argumentlist. Maybe <code>null</code>.
   */
  private String getArgs( String ... args ) {
    if( args == null ) {
      return null;
    }
    StringBuffer buffer = new StringBuffer();
    for( String arg : args ) {
      if( arg != null ) {
        if( buffer.length() > 0 ) {
          buffer.append( ' ' );
        }
        buffer.append( arg );
      }
    }
    return Utilities.cleanup( buffer.toString() );
  }

  /**
   * Representation of a single query element for this task.
   */
  public static class Query {

    /** - */
    private String    _property;

    /** - */
    private QueryType _type;

    /** - */
    private ProductOs _os;

    /**
     * Initialises this query instance with default values.
     */
    public Query() {
      _os = ProductOs.win32;
      _type = null;
      _property = null;
    }

    /**
     * Changes the current os used for the querying of the product configuration.
     * 
     * @param newos
     *          The new os used for the querying of the product configuration. Not <code>null</code>.
     */
    public void setOs( ProductOs newos ) {
      _os = newos;
    }

    /**
     * Changes the query used to access the product information.
     * 
     * @param newquery
     *          The new query used to access product information. Not <code>null</code>.
     */
    public void setType( QueryType newquery ) {
      _type = newquery;
    }

    /**
     * Changes the name of the property which will be set to access the value.
     * 
     * @param newproperty
     *          The new property name. Neither <code>null</code> nor empty.
     */
    public void setProperty( String newproperty ) {
      _property = Utilities.cleanup( newproperty );
    }

  } /* ENDCLASS */

} /* ENDCLASS */
