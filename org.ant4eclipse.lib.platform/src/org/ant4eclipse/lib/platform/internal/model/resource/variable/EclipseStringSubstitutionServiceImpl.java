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
package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.variable.EclipseStringSubstitutionService;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class EclipseStringSubstitutionServiceImpl implements EclipseStringSubstitutionService {

  private EclipseVariableResolver[] _eclipseVariableResolvers;

  private PropertyParser            _propertyParser;

  public EclipseStringSubstitutionServiceImpl() {
    _propertyParser = new PropertyParser();
    List<EclipseVariableResolver> resolvers = A4ECore.instance().getServices( EclipseVariableResolver.class );
    _eclipseVariableResolvers = new EclipseVariableResolver[resolvers.size()];
    resolvers.toArray( _eclipseVariableResolvers );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String substituteEclipseVariables( String string, EclipseProject project, StringMap otherProperties ) {
    Assure.notNull( "string", string );
    // resolve Eclipse variables
    StringMap eclipseVariables = getEclipseVariables( project );

    // overwrite "default" values for eclipse variables with values as specified in otherProperties
    if( otherProperties != null ) {
      eclipseVariables.putAll( otherProperties );
    }

    // resolve string
    String resolvedString = resolveProperties( string, eclipseVariables );
    return resolvedString;
  }

  protected StringMap getEclipseVariables( EclipseProject eclipseProject ) {
    StringMap eclipseVariables = new StringMap();
    for( EclipseVariableResolver resolver : _eclipseVariableResolvers ) {
      resolver.getResolvedVariables( eclipseVariables, eclipseProject );
    }
    return eclipseVariables;
  }

  /**
   * <p>
   * </p>
   * 
   * @param value
   * @param properties
   * @return
   */
  private String resolveProperties( String value, StringMap properties ) {

    Vector<String> fragments = new Vector<String>();
    Vector<String> propertyRefs = new Vector<String>();
    Vector<String> propertyArgs = new Vector<String>();
    _propertyParser.parsePropertyString( value, fragments, propertyRefs, propertyArgs );

    StringBuffer sb = new StringBuffer();
    Enumeration<String> i = fragments.elements();
    Enumeration<String> j = propertyRefs.elements();
    Enumeration<String> k = propertyArgs.elements();

    while( i.hasMoreElements() ) {
      String fragment = i.nextElement();
      if( fragment == null ) {
        String propertyName = j.nextElement();
        String propertyArg = k.nextElement();
        Object replacement = null;
        if( properties != null ) {
          if( "workspace_loc".equals( propertyName ) ) {
            replacement = properties.get( propertyName );
            if( (propertyArg != null) && (propertyArg.length() > 0) ) {
              replacement = replacement + File.separator + propertyArg;
            }
          } else if( "env_var".equals( propertyName ) ) {
            if( (propertyArg != null) && (propertyArg.length() > 0) ) {
              replacement = System.getProperty( propertyArg );
            }
          } else {
            replacement = properties.get( propertyName );
          }
        }
        String arg = propertyArg != null ? ":" + propertyArg : "";
        fragment = (replacement != null) ? replacement.toString() : "${" + propertyName + arg + "}";
      }
      sb.append( fragment );
    }

    return sb.toString();
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
