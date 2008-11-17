/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package net.sf.ant4eclipse.core.xquery;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.CoreExceptionCode;
import net.sf.ant4eclipse.core.exception.Ant4EclipseException;
import net.sf.ant4eclipse.core.logging.A4ELogging;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * A SAX handler allowing to collect XML related data using simple queries (similar to XPath but currently not providing
 * it's complete functionality).
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class XQueryHandler extends DefaultHandler {

  /** - */
  private final StringBuffer      _buffer;

  /** - */
  private int                     _depth;

  /** - */
  private final Vector<XQuery>    _queries;

  private final String            _fileName;

  /** - */
  private static SAXParserFactory factory;

  /**
   * Initialises this handler.
   */
  public XQueryHandler(final String fileName) {
    super();
    this._buffer = new StringBuffer();
    this._queries = new Vector<XQuery>();
    this._depth = 0;
    this._fileName = fileName;
  }

  /**
   * Initialises this handler.
   */
  public XQueryHandler() {
    this("unknown");
  }

  /**
   * Creates a XQuery instance for the supplied query string.
   * 
   * @param query
   *          A XPath like query string.
   * 
   * @return A query instance allowing to retrieve the results.
   */
  public XQuery createQuery(final String query) {
    final XQuery result = new XQuery(this._fileName, query);
    this._queries.add(result);
    return (result);
  }

  /**
   * {@inheritDoc}
   */
  public void startDocument() throws SAXException {
    this._depth = 0;
    for (int i = 0; i < this._queries.size(); i++) {
      final XQuery query = this._queries.get(i);
      query.reset();
    }
    if (this._buffer.length() > 0) {
      this._buffer.delete(0, this._buffer.length());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void startElement(final String uri, final String localname, final String qname, final Attributes attributes)
      throws SAXException {

    for (int i = 0; i < this._queries.size(); i++) {
      final XQuery query = this._queries.get(i);
      query.visit(this._depth, qname, attributes);
    }

    this._depth++;

  }

  /**
   * {@inheritDoc}
   */
  public void endElement(final String uri, final String localname, final String qname) throws SAXException {

    this._depth--;

    final String str = this._buffer.toString().trim();
    for (int i = 0; i < this._queries.size(); i++) {
      final XQuery query = this._queries.get(i);
      query.endVisit(this._depth, str);
    }

    if (this._buffer.length() > 0) {
      this._buffer.delete(0, this._buffer.length());
    }

  }

  /**
   * {@inheritDoc}
   */
  public void characters(final char[] ch, final int start, final int length) throws SAXException {
    this._buffer.append(ch, start, length);
  }

  /**
   * Runs the queries against the supplied XML file.
   * 
   * @param xmlfile
   *          The XML file that shall be queried.
   * @param handler
   *          The handler which provides all queries.
   */
  public static void queryFile(final File xmlfile, final XQueryHandler handler) {
    Assert.isFile(xmlfile);
    Assert.notNull(handler);

    try {
      final SAXParserFactory factory = getSAXParserFactory();
      factory.newSAXParser().parse(new FileInputStream(xmlfile), handler);
    } catch (final Exception ex) {
      A4ELogging.error(ex.getMessage());
      throw (new Ant4EclipseException(CoreExceptionCode.X_QUERY_PARSE_EXCEPTION, ex));
    }
  }

  /**
   * Runs the queries against the supplied XML inputstream.
   * 
   * @param inputStream
   *          The XML inputstream that shall be queried.
   * @param handler
   *          The handler which provides all queries.
   */
  public static void queryInputStream(final InputStream inputStream, final XQueryHandler handler) {
    Assert.notNull(inputStream);
    Assert.notNull(handler);

    try {
      final SAXParserFactory factory = getSAXParserFactory();
      factory.newSAXParser().parse(inputStream, handler);
    } catch (final Exception ex) {
      A4ELogging.error(ex.getMessage());
      throw (new Ant4EclipseException(CoreExceptionCode.X_QUERY_PARSE_EXCEPTION, ex));
    }
  }

  private static SAXParserFactory getSAXParserFactory() throws FactoryConfigurationError, ParserConfigurationException,
      SAXNotRecognizedException, SAXNotSupportedException {

    if (factory == null) {
      factory = SAXParserFactory.newInstance();
      // factory.setFeature("http://xml.org/sax/features/string-interning", true);
      factory.setValidating(false);
    }
    return factory;
  }

} /* ENDCLASS */
