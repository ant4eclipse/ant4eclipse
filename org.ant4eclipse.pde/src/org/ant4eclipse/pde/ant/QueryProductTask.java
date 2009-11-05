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
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.core.ant.ExtendedBuildException;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pde.model.product.ProductDefinition;
import org.ant4eclipse.pde.model.product.ProductDefinitionParser;

import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
  public static enum Query {

    /** - */
    plugins,

    /** - */
    fragments,

    /** - */
    features;

  }

  /** - */
  private String _property;

  /** - */
  private File   _product;

  /** - */
  private Query  _query;

  /** - */
  private String _delimiter = ",";

  /**
   * Changes the query used to access the product information.
   * 
   * @param newquery
   *          The new query used to access product information. Not <code>null</code>.
   */
  public void setQuery(Query newquery) {
    this._query = newquery;
  }

  /**
   * Changes the name of the property which will be set to access the value.
   * 
   * @param newproperty
   *          The new property name. Neither <code>null</code> nor empty.
   */
  public void setProperty(String newproperty) {
    this._property = Utilities.cleanup(newproperty);
  }

  /**
   * Changes the location of the product configuration file.
   * 
   * @param newproduct
   *          The new location of the product file. Not <code>null</code>.
   */
  public void setProduct(File newproduct) {
    this._product = newproduct;
  }

  /**
   * Changes the delimiter which is used for list values.
   * 
   * @param newdelimiter
   *          The delimiter to be used for list values.
   */
  public void setDelimiter(String newdelimiter) {
    this._delimiter = Utilities.cleanup(newdelimiter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (this._property == null) {
      throw new BuildException("The attribute 'property' has to be set.");
    }
    if (this._product == null) {
      throw new BuildException("The attribute 'product' has to be set.");
    }
    if (!this._product.isFile()) {
      throw new ExtendedBuildException("The product configuration '%s' is not a regular file.", this._product);
    }
    if (this._query == null) {
      throw new ExtendedBuildException("The attribute 'query' has to be set to one of the following values: %s",
          Utilities.listToString(Query.values(), null));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {
    ProductDefinition productdef = null;
    InputStream instream = null;
    try {
      instream = new FileInputStream(this._product);
      productdef = ProductDefinitionParser.parseProductDefinition(instream);
    } catch (IOException ex) {
      throw new BuildException(ex);
    } finally {
      Utilities.close(instream);
    }
    String value = null;
    switch (this._query) {
    case plugins:
      value = Utilities.listToString(productdef.getPluginIds(), this._delimiter);
      break;
    case fragments:
      value = Utilities.listToString(productdef.getFragmentIds(), this._delimiter);
      break;
    default:
      throw new ExtendedBuildException("The query type '%s' is currently not implemented.", this._query);
    }
    getProject().setProperty(this._property, value);
  }

} /* ENDCLASS */
