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
package org.ant4eclipse.lib.platform.model.team.cvssupport;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;

/**
 * <p>
 * Encapsulates a cvsroot.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CvsRoot implements Cloneable {

  /** the connectiontype * */
  private String _connectionType;

  /** the user * */
  private String _user;

  /** the host * */
  private String _host;

  /** the repository * */
  private String _repository;

  /** the encoded password * */
  private String _encodedPassword;

  /**
   * Creates a new instance of type CvsRoot.
   * 
   * @param root
   *          the cvsroot as a string.
   * @throws IllegalArgumentException
   */
  public CvsRoot( String root ) throws IllegalArgumentException {
    Assure.nonEmpty( "root", root );
    parse( root );
  }

  /**
   * Sets an encoded password.
   * 
   * @param encodedPassword
   *          The encodedPassword to set.
   */
  public void setEncodedPassword( String encodedPassword ) {
    Assure.notNull( "encodedPassword", encodedPassword );
    _encodedPassword = encodedPassword;
  }

  /**
   * Sets a cvs user.
   * 
   * @param user
   *          Sets a cvs user.
   */
  public void setUser( String user ) {
    Assure.notNull( "user", user );
    _user = user;
  }

  /**
   * Returns the connection type.
   * 
   * @return Returns the connection type.
   */
  public String getConnectionType() {
    return _connectionType;
  }

  /**
   * Returns the host.
   * 
   * @return Returns the host.
   */
  public String getHost() {
    return _host;
  }

  /**
   * Returns the repository.
   * 
   * @return Returns the repository.
   */
  public String getRepository() {
    return _repository;
  }

  /**
   * Returns the user.
   * 
   * @return Returns the user.
   */
  public String getUser() {
    return _user;
  }

  /**
   * Returns whether an user is set or not.
   * 
   * @return whether an user is set or not.
   */
  public boolean hasUser() {
    return _user != null;
  }

  /**
   * Returns the encoded password.
   * 
   * @return Returns the encoded password.
   */
  public String getEncodedPassword() {
    return _encodedPassword;
  }

  /**
   * Returns whether an encoded password is set or not.
   * 
   * @return whether an encoded password is set or not.
   */
  public boolean hasEncodedPassword() {
    return _encodedPassword != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    CvsRoot inst = new CvsRoot();
    inst._connectionType = _connectionType == null ? null : new String( _connectionType );
    inst._user = _user == null ? null : new String( _user );
    inst._host = _host == null ? null : new String( _host );
    inst._repository = _repository == null ? null : new String( _repository );
    inst._encodedPassword = _encodedPassword == null ? null : new String( _encodedPassword );
    return inst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffy = new StringBuffer();

    buffy.append( ":" );
    buffy.append( _connectionType );
    buffy.append( ":" );

    if( _user != null ) {
      buffy.append( _user );

      if( _encodedPassword != null ) {
        buffy.append( ":" );
        buffy.append( _encodedPassword );
      }

      buffy.append( "@" );
    }

    buffy.append( _host );
    buffy.append( ":" );
    buffy.append( _repository );

    return buffy.toString();
  }

  /**
   * Returns the resolved cvsroot.
   * 
   * @param username
   *          the username to use in the cvsroot.
   * @param password
   *          the password to use in the cvsroot. Might be null
   * @return the resolved root.
   */
  public CvsRoot getResolvedRoot( String username, String password ) {
    Assure.notNull( "username", username );

    CvsRoot cvsRoot = null;

    try {
      cvsRoot = (CvsRoot) clone();
    } catch( Exception e ) {
      A4ELogging.debug( e.getMessage() );
      return null;
    }

    cvsRoot.setUser( username );

    if( password != null ) {
      cvsRoot.setEncodedPassword( password );
    }

    return cvsRoot;
  }

  /**
   * Parse the specified root. Throws an IllegalArgumentException if the cvsroot has the wrong format.
   * 
   * <p>
   * Support Format: <tt>[:method:][[user][:password]@]hostname[:[port]]/path/to/repository</tt>
   * 
   * @see http://ximbiot.com/cvs/manual/cvs-1.11.23/cvs_2.html#SEC26
   * 
   * @param cvsRoot
   *          the cvsroot to parse.
   */
  private void parse( String cvsRoot ) {

    String root = cvsRoot;

    // parse the connection method
    if( root.charAt( 0 ) == ':' ) {
      // first element is the connection method
      int second = root.indexOf( ':', 1 );
      if( second == -1 ) {
        throw(new IllegalArgumentException());
      }
      _connectionType = root.substring( 1, second );
      root = root.substring( second + 1 );
      int semicolon = _connectionType.indexOf( ';' );
      if( semicolon != -1 ) {
        /**
         * @todo [01-Apr-2006:KASI] We need to handle method options as well. f.e.
         *       :pserver;proxy=www.myproxy.net;proxyport=8000:
         */
        // we need to remove some method options here
        _connectionType = _connectionType.substring( 0, semicolon );
      }
    } else {
      // we need to set the default connection method which
      // will be changed when the repository location is
      // known
      _connectionType = "default";
    }

    // parse the user/password information
    int separator = root.indexOf( '@' );
    if( separator != -1 ) {
      String[] userpass = root.substring( 0, separator ).split( ":" );
      root = root.substring( separator + 1 );
      _user = userpass[0];
      if( userpass.length > 1 ) {
        _encodedPassword = userpass[1];
      }
    }

    // now we got the repository location, so let's see
    // if we need to adjust the connection method
    if( "default".equals( _connectionType ) ) {
      if( root.charAt( 0 ) == '/' ) {
        // we need to handle a local repository
        _connectionType = "local";
      } else {
        // we need to handle a remote repository
        _connectionType = "ext";
      }
    }

    if( (root.length() > 0) && (root.charAt( 0 ) == '/') ) {
      /**
       * @todo [01-Apr-2006:KASI] Running a local repository. Needs better support.
       */
      _repository = root;
    } else {
      /**
       * @todo [01-Apr-2006:KASI] Currently the port parameter is not parsed.
       */
      separator = root.indexOf( '/' );
      if( separator == -1 ) {
        throw(new IllegalArgumentException());
      }

      // handle empty port (both host:/repositorypath and host/repositorypath are allowed)
      if( root.charAt( separator - 1 ) == ':' ) {
        _host = root.substring( 0, separator - 1 );
      } else {
        _host = root.substring( 0, separator );
      }
      _repository = root.substring( separator );
    }

    if( isEmpty( _connectionType ) ) {
      throw new Ant4EclipseException( PlatformExceptionCode.INVALID_CVS_ROOT, cvsRoot,
          PlatformExceptionCode.MISSING_CONNECTION_TYPE );
    }

    if( isEmpty( _repository ) ) {
      throw new Ant4EclipseException( PlatformExceptionCode.INVALID_CVS_ROOT, cvsRoot,
          PlatformExceptionCode.MISSING_REPOSITORY );
    }
  }

  private boolean isEmpty( String string ) {
    return (string == null) || (string.length() < 1);
  }

  /**
   * Creates a new instance of type CvsRoot
   * 
   * Private, just needed to support the clone-method
   */
  private CvsRoot() {
    // Private, just needed to support the clone-method
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_connectionType == null ? 0 : _connectionType.hashCode());
    hashCode = 31 * hashCode + (_user == null ? 0 : _user.hashCode());
    hashCode = 31 * hashCode + (_host == null ? 0 : _host.hashCode());
    hashCode = 31 * hashCode + (_repository == null ? 0 : _repository.hashCode());
    hashCode = 31 * hashCode + (_encodedPassword == null ? 0 : _encodedPassword.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null ) {
      return false;
    }
    if( o.getClass() != getClass() ) {
      return false;
    }
    CvsRoot castedObj = (CvsRoot) o;
    return((_connectionType == null ? castedObj._connectionType == null : _connectionType
        .equals( castedObj._connectionType ))
        && (_user == null ? castedObj._user == null : _user.equals( castedObj._user ))
        && (_host == null ? castedObj._host == null : _host.equals( castedObj._host ))
        && (_repository == null ? castedObj._repository == null : _repository.equals( castedObj._repository )) && (_encodedPassword == null ? castedObj._encodedPassword == null
        : _encodedPassword.equals( castedObj._encodedPassword )));
  }
  
} /* ENDCLASS */
