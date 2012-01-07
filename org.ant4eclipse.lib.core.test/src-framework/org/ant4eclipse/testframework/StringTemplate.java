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
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class StringTemplate {

  private StringBuilder _stringTemplate;

  private StringMap     _stringsToReplace;

  /**
   * 
   */
  public StringTemplate() {
    _stringTemplate = new StringBuilder();
    _stringsToReplace = new StringMap();
  }

  /**
   * 
   */
  public StringTemplate( String content ) {
    this();
    Assure.notNull( "content", content );
    _stringTemplate.append( content );
  }

  /**
   * @param content
   */
  public StringTemplate append( String content ) {
    Assure.notNull( "content", content );
    _stringTemplate.append( content );
    return this;
  }

  /**
   * Adds a NewLine to the Template
   * 
   * @return
   */
  public StringTemplate nl() {
    _stringTemplate.append( Utilities.NL );
    return this;
  }

  /**
   * @param name
   * @param value
   */
  public void replace( String name, String value ) {
    Assure.notNull( "name", name );
    Assure.notNull( "value", value );
    _stringsToReplace.put( name, value );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return Utilities.replaceTokens( _stringTemplate.toString(), _stringsToReplace );
  }

} /* ENDCLASS */
