package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import java.util.Vector;

/**
 * A class that is able to parse properties in Strings like <code>${project_loc}</echo>
 * 
 * @author nils
 * 
 */
public class PropertyParser {

  /**
   * based on org.apache.tools.ant.PropertyHelper#parsePropertyString
   */
  public final void parsePropertyString( String value, Vector<String> fragments, Vector<String> propertyRefs,
      Vector<String> propertyArgs ) {
    int prev = 0;
    int pos;
    // search for the next instance of $ from the 'prev' position
    while( (pos = value.indexOf( "$", prev )) >= 0 ) {

      // if there was any text before this, add it as a fragment
      // TODO, this check could be modified to go if pos>prev;
      // seems like this current version could stick empty strings
      // into the list
      if( pos > 0 ) {
        fragments.addElement( value.substring( prev, pos ) );
      }
      // if we are at the end of the string, we tack on a $
      // then move past it
      if( pos == (value.length() - 1) ) {
        fragments.addElement( "$" );
        prev = pos + 1;
      } else if( value.charAt( pos + 1 ) != '{' ) {
        // peek ahead to see if the next char is a property or not
        // not a property: insert the char as a literal
        /*
         * fragments.addElement(value.substring(pos + 1, pos + 2)); prev = pos + 2;
         */
        if( value.charAt( pos + 1 ) == '$' ) {
          // backwards compatibility two $ map to one mode
          fragments.addElement( "$" );
          prev = pos + 2;
        } else {
          // new behaviour: $X maps to $X for all values of X!='$'
          fragments.addElement( value.substring( pos, pos + 2 ) );
          prev = pos + 2;
        }

      } else {
        // property found, extract its name or bail on a typo
        int endName = value.indexOf( '}', pos );
        if( endName < 0 ) {
          throw new RuntimeException( String.format( "Syntax error in property: %s", value ) );
        }
        String propertyName = value.substring( pos + 2, endName );
        // cut off eclipse arguments, since they are not supported
        // by ant4eclipse
        int v = propertyName.indexOf( ':' );
        String propertyArg = null;
        if( v != -1 ) {
          propertyArg = propertyName.substring( v + 1 );
          propertyName = propertyName.substring( 0, v );
        }
        fragments.addElement( null );
        propertyRefs.addElement( propertyName );
        propertyArgs.addElement( propertyArg );
        prev = endName + 1;
      }
    }
    // no more $ signs found
    // if there is any tail to the file, append it
    if( prev < value.length() ) {
      fragments.addElement( value.substring( prev ) );
    }
  }

} /* ENDCLASS */
