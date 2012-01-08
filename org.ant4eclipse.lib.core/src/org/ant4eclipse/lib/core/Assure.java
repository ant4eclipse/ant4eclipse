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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.nls.NLS;
import org.ant4eclipse.lib.core.nls.NLSMessage;

import java.io.File;

/**
 * <p>
 * Implements utility methods to support design-by-contract. If a condition is evaluated to false, a RuntimeException
 * will be thrown.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class Assure {

  @NLSMessage( "Parameter '%s' should be of type '%s' but is a '%s'" )
  public static String MSG_INVALIDTYPE;

  @NLSMessage( "The parameter '%s' is not supposed to be null." )
  public static String MSG_NOTNULL;

  @NLSMessage( "The parameter '%s' is not supposed to be empty." )
  public static String MSG_NOTEMPTY;

  @NLSMessage( "The supplied string is not allowed to be empty." )
  public static String MSG_STRINGMUSTBENONEMPTY;

  @NLSMessage( "The resource '%s' does not exist !" )
  public static String MSG_RESOURCEDOESNOTEXIST;

  @NLSMessage( "The resource '%s' is not a regular file !" )
  public static String MSG_RESOURCEISNOTAREGULARFILE;

  @NLSMessage( "The resource '%s' is not a directory !" )
  public static String MSG_RESOURCEISNOTADIRECTORY;

  @NLSMessage( "The value %d is supposed to be in the range [%d..%d] !" )
  public static String MSG_VALUEOUTOFRANGE;

  static {
    NLS.initialize( Assure.class );
  }

  /**
   * <p>
   * Assert that the specified object is not null.
   * </p>
   * 
   * @param parameterName
   *          The name of the parameter that is checked
   * @param object
   *          the object that must be set.
   */
  public static final void notNull( String parametername, Object object ) {
//    if( object == null ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_NOTNULL,
//          parametername ) );
//    }
  }

  /**
   * Asserts that the specified array is neither <code>null</code> nor empty.
   * 
   * @param parametername
   *          The name of the parameter that has to be tested.
   * @param object
   *          The object that has to be tested.
   */
  public static final void nonEmpty( String parametername, byte[] object ) {
    notNull( parametername, (Object) object );
//    if( object.length == 0 ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_NOTEMPTY,
//          parametername ) );
//    }
  }

  /**
   * Asserts that the specified array is neither <code>null</code> nor empty.
   * 
   * @param parametername
   *          The name of the parameter that has to be tested.
   * @param object
   *          The object that has to be tested.
   */
  public static final void nonEmpty( String parametername, boolean[] object ) {
//    notNull( parametername, object );
//    if( object.length == 0 ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_NOTEMPTY,
//          parametername ) );
//    }
  }

  /**
   * Asserts that the specified array is neither <code>null</code> nor empty.
   * 
   * @param parametername
   *          The name of the parameter that has to be tested.
   * @param object
   *          The object that has to be tested.
   */
  public static final void nonEmpty( String parametername, char[] object ) {
//    notNull( parametername, object );
//    if( object.length == 0 ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_NOTEMPTY,
//          parametername ) );
//    }
  }

  /**
   * Asserts that the specified array is neither <code>null</code> nor empty.
   * 
   * @param parametername
   *          The name of the parameter that has to be tested.
   * @param object
   *          The object that has to be tested.
   */
  public static final void nonEmpty( String parametername, short[] object ) {
//    notNull( parametername, object );
//    if( object.length == 0 ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_NOTEMPTY,
//          parametername ) );
//    }
  }

  /**
   * Asserts that the specified array is neither <code>null</code> nor empty.
   * 
   * @param parametername
   *          The name of the parameter that has to be tested.
   * @param object
   *          The object that has to be tested.
   */
  public static final void nonEmpty( String parametername, int[] object ) {
//    notNull( parametername, object );
//    if( object.length == 0 ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_NOTEMPTY,
//          parametername ) );
//    }
  }

  /**
   * Asserts that the specified array is neither <code>null</code> nor empty.
   * 
   * @param parametername
   *          The name of the parameter that has to be tested.
   * @param object
   *          The object that has to be tested.
   */
  public static final void nonEmpty( String parametername, long[] object ) {
//    notNull( parametername, object );
//    if( object.length == 0 ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_NOTEMPTY,
//          parametername ) );
//    }
  }

  /**
   * Asserts that the given parameter is an instance of the given type
   * 
   * @param parameterName
   *          The name of the parameter that is checked
   * @param parameter
   *          The actual parameter value
   * @param expectedType
   *          The type the parameter should be an instance of
   */
  public static final void instanceOf( String parameterName, Object parameter, Class<?> expectedType ) {
//    notNull( parameterName, parameter );
//    if( !expectedType.isInstance( parameter ) ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_INVALIDTYPE,
//          parameterName, expectedType.getName(), parameter.getClass().getName() ) );
//    }
  }

  /**
   * <p>
   * Assert that the supplied string provides a value or not.
   * </p>
   * 
   * @param string
   *          the string that must provide a value.
   */
  public static final void nonEmpty( String param, String string ) {
//    notNull( param, string );
//    if( string.length() == 0 ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, MSG_STRINGMUSTBENONEMPTY );
//    }
  }

  /**
   * <p>
   * Assert that the specified file is not null and exists.
   * </p>
   * 
   * @param file
   *          the file that must exist.
   */
  public static final void exists( String param, File file ) {
//    notNull( param, file );
//    if( !file.exists() ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format(
//          MSG_RESOURCEDOESNOTEXIST, file.getAbsolutePath() ) );
//    }
  }

  /**
   * <p>
   * Assert that the specified file is not null, exists and is a file.
   * </p>
   * 
   * @param file
   *          the file that must be a file.
   */
  public static final void isFile( String param, File file ) {
//    Assure.exists( param, file );
//    if( !file.isFile() ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format(
//          MSG_RESOURCEISNOTAREGULARFILE, file.getAbsolutePath() ) );
//    }
  }

  /**
   * <p>
   * Assert that the specified file is not null, exists and is a directory.
   * </p>
   * 
   * @param file
   *          the file that must be a directory.
   */
  public static final void isDirectory( String param, File file ) {
//    Assure.exists( param, file );
//    if( !file.isDirectory() ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format(
//          MSG_RESOURCEISNOTADIRECTORY, file.getAbsolutePath() ) );
//    }
  }

  /**
   * <p>
   * Assert that the given condition is <code>true</code>
   * </p>
   * 
   * @param condition
   *          the condition
   * @param msg
   *          the message
   */
  public static final void assertTrue( boolean condition, String msg ) {
//    if( !condition ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, msg );
//    }
  }

  /**
   * <p>
   * Checks whether a value is in a specific range or not.
   * </p>
   * 
   * @param value
   *          the value that shall be tested.
   * @param from
   *          the lower bound inclusive.
   * @param to
   *          the upper bound inclusive.
   */
  public static final void inRange( int value, int from, int to ) {
//    if( (value < from) || (value > to) ) {
//      throw new Ant4EclipseException( CoreExceptionCode.PRECONDITION_VIOLATION, String.format( MSG_VALUEOUTOFRANGE,
//          Integer.valueOf( value ), Integer.valueOf( from ), Integer.valueOf( to ) ) );
//    }
  }

} /* ENDCLASS */
