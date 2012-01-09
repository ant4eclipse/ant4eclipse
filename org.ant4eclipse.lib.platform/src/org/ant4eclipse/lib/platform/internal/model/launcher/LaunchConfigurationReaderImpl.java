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
package org.ant4eclipse.lib.platform.internal.model.launcher;

import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfigurationReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class LaunchConfigurationReaderImpl implements LaunchConfigurationReader {

  private SAXParserFactory _saxParserFactory;

  /**
   * {@inheritDoc}
   */
  // Assure.isFile( "launchConfigurationFile", launchConfigurationFile );
  @Override
  public LaunchConfiguration readLaunchConfiguration( File launchConfigurationFile ) {
    try {
      SAXParser parser = getParserFactory().newSAXParser();
      LaunchConfigHandler handler = new LaunchConfigHandler();
      parser.parse( launchConfigurationFile, handler );

      LaunchConfigurationImpl launchConfigurationImpl = new LaunchConfigurationImpl(
          handler.getLaunchConfigurationType(), handler.getAttributes() );
      return launchConfigurationImpl;
    } catch( Exception ex ) {
      throw new RuntimeException( String.format( "Could not parse launch config '%s': %s", launchConfigurationFile, ex ), ex );
    }
  }

  protected SAXParserFactory getParserFactory() {
    if( _saxParserFactory == null ) {
      SAXParserFactory newInstance = SAXParserFactory.newInstance();
      newInstance.setValidating( false );
      newInstance.setNamespaceAware( false );
      newInstance.setXIncludeAware( false );
      _saxParserFactory = newInstance;
    }

    return _saxParserFactory;
  }

  class LaunchConfigHandler extends DefaultHandler {

    public final static String          LAUNCH_CONFIGURATION_ELEMENT_NAME = "launchConfiguration";

    public final static String          LIST_ENTRY_ELEMENT_NAME           = "listEntry";

    public final static String          LIST_ATTRIBUTE_ELEMENT_NAME       = "listAttribute";

    private List<LaunchConfigAttribute> _attributes                       = new ArrayList<LaunchConfigAttribute>();

    private String                      _launchConfigurationType;

    private LaunchConfigAttribute       _currentAttribute                 = null;

    @Override
    public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException {

      if( LAUNCH_CONFIGURATION_ELEMENT_NAME.equals( qName ) ) {
        _launchConfigurationType = attributes.getValue( "type" );
        return;
      }

      if( LIST_ENTRY_ELEMENT_NAME.equals( qName ) ) {
        LaunchConfigAttribute.ListAttribute listAttribute = _currentAttribute.getListAttributeValue();
        listAttribute.addEntry( attributes.getValue( "value" ) );
        return;
      }

      String key = attributes.getValue( "key" );
      if( key == null ) {
        // TODO
        throw new IllegalStateException( String.format( "Invalid element: %s -> no key found!", localName ) );
      }
      _currentAttribute = new LaunchConfigAttribute( key );

      if( LIST_ATTRIBUTE_ELEMENT_NAME.equals( qName ) ) {
        _currentAttribute.setValue( new LaunchConfigAttribute.ListAttribute() );
      } else {
        _currentAttribute.setValue( attributes.getValue( "value" ) );
      }
    }

    @Override
    public void endElement( String uri, String localName, String qName ) throws SAXException {
      if( !LIST_ENTRY_ELEMENT_NAME.equals( qName ) && _currentAttribute != null ) {
        _attributes.add( _currentAttribute );
        _currentAttribute = null;
      }
    }

    public String getLaunchConfigurationType() {
      return _launchConfigurationType;
    }

    public List<LaunchConfigAttribute> getAttributes() {
      return _attributes;
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }

} /* ENDCLASS */
