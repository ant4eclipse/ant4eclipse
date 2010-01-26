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
package org.ant4eclipse.lib.pde.model.target;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;

import java.io.InputStream;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetDefinitionParser {

  /**
   * <p>
   * Creates a target definition from the supplied content.
   * </p>
   * 
   * @param inputStream
   *          The stream which provides the content.
   * 
   * @return a target definition instance.
   */
  public static TargetDefinition parseTargetDefinition(InputStream inputStream) {
    Assure.notNull("inputStream", inputStream);

    // create query handler
    XQueryHandler queryhandler = new XQueryHandler();

    // create queries
    XQuery nameQuery = queryhandler.createQuery("/target/@name");

    XQuery pathQuery = queryhandler.createQuery("/target/location/@path");
    XQuery useDefaultQuery = queryhandler.createQuery("/target/location/@useDefault");

    XQuery environmentOsQuery = queryhandler.createQuery("/target/environment/os");
    XQuery environmentWsQuery = queryhandler.createQuery("/target/environment/ws");
    XQuery environmentArchQuery = queryhandler.createQuery("/target/environment/arch");
    XQuery environmentNlQuery = queryhandler.createQuery("/target/environment/nl");

    XQuery pluginIdQuery = queryhandler.createQuery("/target/content/plugins/{plugin}/@id");
    XQuery featureIdQuery = queryhandler.createQuery("/target/content/features/{feature}/@id");
    XQuery extraLocationPathQuery = queryhandler.createQuery("/target/content/extraLocations/{location}/@path");

    XQuery targetJreNameQuery = queryhandler.createQuery("/target/targetJRE/jreName");
    XQuery targetJreExecutionEnvironmentQuery = queryhandler.createQuery("/target/targetJRE/execEnv");

    // parse the file
    XQueryHandler.queryInputStream(inputStream, queryhandler);

    // create the target definition
    TargetDefinition targetDefinition = new TargetDefinition();
    targetDefinition.setName(nameQuery.getSingleResult());

    // create the environment
    TargetDefinition.Environment environment = new TargetDefinition.Environment();
    targetDefinition.setEnvironment(environment);
    environment.setOs(environmentOsQuery.getSingleResult());
    environment.setWs(environmentWsQuery.getSingleResult());
    environment.setArch(environmentArchQuery.getSingleResult());
    environment.setNl(environmentNlQuery.getSingleResult());

    // create the location
    TargetDefinition.Location location = new TargetDefinition.Location();
    targetDefinition.setLocation(location);
    location.setPath(pathQuery.getSingleResult());
    location.setUseDefault(Boolean.getBoolean(useDefaultQuery.getSingleResult()));

    // create the content
    TargetDefinition.Content content = new TargetDefinition.Content();
    targetDefinition.setContent(content);
    String[] pluginIds = pluginIdQuery.getResult();
    for (String pluginId : pluginIds) {
      content.addPlugin(pluginId);
    }
    String[] featureIds = featureIdQuery.getResult();
    for (String featureId : featureIds) {
      content.addFeature(featureId);
    }
    String[] extraLocationPaths = extraLocationPathQuery.getResult();
    for (String extraLocationPath : extraLocationPaths) {
      content.addExtraLocation(extraLocationPath);
    }

    // create the target jre
    TargetDefinition.TargetJRE targetJRE = new TargetDefinition.TargetJRE();
    targetDefinition.setTargetJRE(targetJRE);
    targetJRE.setJreName(targetJreNameQuery.getSingleResult());
    targetJRE.setExecutionEnvironment(targetJreExecutionEnvironmentQuery.getSingleResult());

    // return the result
    return targetDefinition;
  }
}
