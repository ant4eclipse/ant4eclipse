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
package org.ant4eclipse.platform.internal.model.launcher;

import org.ant4eclipse.platform.model.launcher.LaunchConfiguration;
import org.ant4eclipse.platform.model.launcher.LaunchConfigurationReader;

import org.ant4eclipse.lib.core.Assert;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class LaunchConfigurationReaderImpl implements LaunchConfigurationReader {

  private SAXParserFactory _saxParserFactory;

  /**
   * {@inheritDoc}
   */
  public LaunchConfiguration readLaunchConfiguration(File launchConfigurationFile) {

    Assert.notNull("Parameter 'launchConfigurationFile' must not be null", launchConfigurationFile);
    Assert.isFile(launchConfigurationFile);

    try {
      SAXParser parser = getParserFactory().newSAXParser();
      LaunchConfigHandler handler = new LaunchConfigHandler();
      parser.parse(launchConfigurationFile, handler);

      LaunchConfigurationImpl launchConfigurationImpl = new LaunchConfigurationImpl(handler
          .getLaunchConfigurationType(), handler.getAttributes());
      return launchConfigurationImpl;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Could not parse launch config '" + launchConfigurationFile + ": " + ex, ex);
    }

  }

  protected SAXParserFactory getParserFactory() {
    if (this._saxParserFactory == null) {
      SAXParserFactory newInstance = SAXParserFactory.newInstance();
      newInstance.setValidating(false);
      newInstance.setNamespaceAware(false);
      newInstance.setXIncludeAware(false);
      this._saxParserFactory = newInstance;
    }

    return this._saxParserFactory;
  }

  class LaunchConfigHandler extends DefaultHandler {

    public final static String          LAUNCH_CONFIGURATION_ELEMENT_NAME = "launchConfiguration";

    public final static String          LIST_ENTRY_ELEMENT_NAME           = "listEntry";

    public final static String          LIST_ATTRIBUTE_ELEMENT_NAME       = "listAttribute";

    private List<LaunchConfigAttribute> _attributes                       = new LinkedList<LaunchConfigAttribute>();

    private String                      _launchConfigurationType;

    private LaunchConfigAttribute       _currentAttribute                 = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

      if (LAUNCH_CONFIGURATION_ELEMENT_NAME.equals(qName)) {
        this._launchConfigurationType = attributes.getValue("type");
        return;
      }

      if (LIST_ENTRY_ELEMENT_NAME.equals(qName)) {
        LaunchConfigAttribute.ListAttribute listAttribute = this._currentAttribute.getListAttributeValue();
        listAttribute.addEntry(attributes.getValue("value"));
        return;
      }

      String key = attributes.getValue("key");
      if (key == null) {
        // TODO
        throw new IllegalStateException("Invalid element: " + localName + " -> no key found!");
      }
      this._currentAttribute = new LaunchConfigAttribute(key);

      if (LIST_ATTRIBUTE_ELEMENT_NAME.equals(qName)) {
        this._currentAttribute.setValue(new LaunchConfigAttribute.ListAttribute());
      } else {
        this._currentAttribute.setValue(attributes.getValue("value"));
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if (!LIST_ENTRY_ELEMENT_NAME.equals(qName) && this._currentAttribute != null) {
        this._attributes.add(this._currentAttribute);
        this._currentAttribute = null;
      }
    }

    public String getLaunchConfigurationType() {
      return this._launchConfigurationType;
    }

    public List<LaunchConfigAttribute> getAttributes() {
      return this._attributes;
    }

  }

}
