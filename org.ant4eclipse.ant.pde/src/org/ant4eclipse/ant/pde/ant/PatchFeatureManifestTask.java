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
package org.ant4eclipse.ant.pde.ant;


import org.ant4eclipse.ant.core.AbstractAnt4EclipseTask;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.tools.PdeBuildHelper;
import org.osgi.framework.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.util.StringTokenizer;

/**
 * <p>
 * e.g.
 * testproject=1.0.0;org.eclipse.osgi=3.4.2.R34x_v20080826-1230;org.eclipse.osgi.util=3.1.300.v20080303;org.eclipse.
 * osgi.services=3.1.200.v20071203;example_bundle=1.0.0.200907270913
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PatchFeatureManifestTask extends AbstractAnt4EclipseTask {

  /** - */
  private File   _featureXmlFile;

  /** - */
  private String _qualifier = PdeBuildHelper.getResolvedContextQualifier();

  /** - */
  private String _pluginVersions;

  /**
   * <p>
   * </p>
   * 
   * @return the featureXmlFile
   */
  public File getFeatureXmlFile() {
    return this._featureXmlFile;
  }

  /**
   * <p>
   * </p>
   * 
   * @param featureXmlFile
   *          the featureXmlFile to set
   */
  public void setFeatureXmlFile(File featureXmlFile) {
    this._featureXmlFile = featureXmlFile;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the qualifier
   */
  public String getQualifier() {
    return this._qualifier;
  }

  /**
   * <p>
   * </p>
   * 
   * @param qualifier
   *          the qualifier to set
   */
  public void setQualifier(String qualifier) {
    this._qualifier = qualifier;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the pluginVersions
   */
  public String getPluginVersions() {
    return this._pluginVersions;
  }

  /**
   * <p>
   * </p>
   * 
   * @param pluginVersions
   *          the pluginVersions to set
   */
  public void setPluginVersions(String pluginVersions) {
    this._pluginVersions = pluginVersions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    StringMap versions = new StringMap();

    if (Utilities.hasText(this._pluginVersions)) {

      StringTokenizer tokenizer = new StringTokenizer(this._pluginVersions, ";");

      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();

        String[] elements = token.split("=");

        if (elements.length == 2) {
          versions.put(elements[0], elements[1]);
        }
      }
    }

    try {
      replaceVersions(this._featureXmlFile, this._qualifier, versions);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() {

    if (this._featureXmlFile == null) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "featureXmlFile");
    }
    if (!this._featureXmlFile.exists()) {
      // TODO
      throw new RuntimeException();
    }
    if (!this._featureXmlFile.isFile()) {
      // TODO
      throw new RuntimeException();
    }
  }

  /**
   * Replaces the given plug-in-versions in given feature.xml-File.
   * 
   * @param featureXml
   *          The feature.xml file. NOTE: this file will be <b>changed</b> and thus must be writable
   * @param qualifier
   *          The new version for this feature. If set to null, the "version"-attribute of the "feature"-tag won't be
   *          changed
   * @param newBundleVersions
   *          A map containing plugin-id (String) - version (String) associations
   * @throws Exception
   */
  protected void replaceVersions(File featureXml, String qualifier, StringMap newBundleVersions) throws Exception {
    Assure.notNull("featureXml", featureXml);
    Assure.assertTrue(featureXml.isFile(), "featureXml (" + featureXml + ") must point to an existing file");

    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document featureDom = builder.parse(featureXml);

    if (qualifier != null) {
      Element featureElement = featureDom.getDocumentElement();
      String featureVersion = featureElement.getAttribute("version");
      if (featureVersion != null && featureVersion.endsWith(".qualifier")) {
        featureElement.setAttribute("version", PdeBuildHelper.resolveVersion(new Version(featureVersion), qualifier)
            .toString());
      }
    }

    NodeList pluginNodes = featureDom.getDocumentElement().getElementsByTagName("plugin");
    for (int i = 0; i < pluginNodes.getLength(); i++) {
      Element element = (Element) pluginNodes.item(i);
      String id = element.getAttribute("id");
      if (newBundleVersions.containsKey(id)) {
        String version = newBundleVersions.get(id);
        element.setAttribute("version", version);
      }
    }

    DOMSource domSource = new DOMSource(featureDom);
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    StreamResult result = new StreamResult(featureXml);
    transformer.transform(domSource, result);
  }
}
