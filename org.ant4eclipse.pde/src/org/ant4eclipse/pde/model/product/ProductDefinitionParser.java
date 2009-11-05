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
package org.ant4eclipse.pde.model.product;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.data.Version;
import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;

import java.io.InputStream;

/**
 * <p>
 * Parser for an eclipse product definition (<code>*.product</code>).
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ProductDefinitionParser {

  /**
   * <p>
   * Creates a product definition from the supplied content.
   * </p>
   * 
   * @param inputStream
   *          The stream which provides the content. Not <code>null</code>.
   * 
   * @return A product definition instance. Not <code>null</code>.
   */
  public static ProductDefinition parseProductDefinition(InputStream inputStream) {

    Assert.notNull(inputStream);

    XQueryHandler queryhandler = new XQueryHandler();

    XQuery namequery = queryhandler.createQuery("//product/@name");
    XQuery idquery = queryhandler.createQuery("//product/@id");
    XQuery applicationquery = queryhandler.createQuery("//product/@application");
    XQuery versionquery = queryhandler.createQuery("//product/@version");
    XQuery usefeaturesquery = queryhandler.createQuery("//product/@useFeatures");

    XQuery pluginidquery = queryhandler.createQuery("//product/plugins/plugin/@id");
    XQuery fragmentquery = queryhandler.createQuery("//product/plugins/plugin/@fragment");

    XQueryHandler.queryInputStream(inputStream, queryhandler);

    ProductDefinition result = new ProductDefinition();

    result.setApplication(applicationquery.getSingleResult());
    result.setId(idquery.getSingleResult());
    result.setName(namequery.getSingleResult());
    result.setVersion(new Version(versionquery.getSingleResult()));
    result.setBasedOnFeatures(Boolean.parseBoolean(usefeaturesquery.getSingleResult()));

    String[] pluginids = pluginidquery.getResult();
    String[] isfragment = fragmentquery.getResult();
    for (int i = 0; i < pluginids.length; i++) {
      result.addPlugin(pluginids[i], Boolean.parseBoolean(isfragment[i]));
    }

    return result;

  }

  /*
   * public static final void main(String[] args) throws Exception { InputStream instream = new FileInputStream(new
   * File("mmt2.product.txt")); ProductDefinition result = parseProductDefinition(instream); System.err.println(result);
   * }
   */

} /* ENDCLASS */
