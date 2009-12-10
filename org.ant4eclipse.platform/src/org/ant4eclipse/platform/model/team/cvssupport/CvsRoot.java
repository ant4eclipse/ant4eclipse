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
package org.ant4eclipse.platform.model.team.cvssupport;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.PlatformExceptionCode;

/**
 * <p>
 * Encapsulates a cvsroot.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class CvsRoot implements Cloneable {
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
  public CvsRoot(String root) throws IllegalArgumentException {
    Assert.nonEmpty(root);
    parse(root);
  }

  /**
   * Sets an encoded password.
   * 
   * @param encodedPassword
   *          The encodedPassword to set.
   */
  public void setEncodedPassword(String encodedPassword) {
    Assert.notNull(encodedPassword);
    this._encodedPassword = encodedPassword;
  }

  /**
   * Sets a cvs user.
   * 
   * @param user
   *          Sets a cvs user.
   */
  public void setUser(String user) {
    Assert.notNull(user);
    this._user = user;
  }

  /**
   * Returns the connection type.
   * 
   * @return Returns the connection type.
   */
  public String getConnectionType() {
    return this._connectionType;
  }

  /**
   * Returns the host.
   * 
   * @return Returns the host.
   */
  public String getHost() {
    return this._host;
  }

  /**
   * Returns the repository.
   * 
   * @return Returns the repository.
   */
  public String getRepository() {
    return this._repository;
  }

  /**
   * Returns the user.
   * 
   * @return Returns the user.
   */
  public String getUser() {
    return this._user;
  }

  /**
   * Returns whether an user is set or not.
   * 
   * @return whether an user is set or not.
   */
  public boolean hasUser() {
    return this._user != null;
  }

  /**
   * Returns the encoded password.
   * 
   * @return Returns the encoded password.
   */
  public String getEncodedPassword() {
    return this._encodedPassword;
  }

  /**
   * Returns whether an encoded password is set or not.
   * 
   * @return whether an encoded password is set or not.
   */
  public boolean hasEncodedPassword() {
    return this._encodedPassword != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    CvsRoot inst = new CvsRoot();
    inst._connectionType = this._connectionType == null ? null : new String(this._connectionType);
    inst._user = this._user == null ? null : new String(this._user);
    inst._host = this._host == null ? null : new String(this._host);
    inst._repository = this._repository == null ? null : new String(this._repository);
    inst._encodedPassword = this._encodedPassword == null ? null : new String(this._encodedPassword);
    return inst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffy = new StringBuffer();

    buffy.append(":");
    buffy.append(this._connectionType);
    buffy.append(":");

    if (this._user != null) {
      buffy.append(this._user);

      if (this._encodedPassword != null) {
        buffy.append(":");
        buffy.append(this._encodedPassword);
      }

      buffy.append("@");
    }

    buffy.append(this._host);
    buffy.append(":");
    buffy.append(this._repository);

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
  public CvsRoot getResolvedRoot(String username, String password) {
    Assert.notNull(username);

    CvsRoot cvsRoot = null;

    try {
      cvsRoot = (CvsRoot) clone();
    } catch (Exception e) {
      A4ELogging.debug(e.getMessage());
      return null;
    }

    cvsRoot.setUser(username);

    if (password != null) {
      cvsRoot.setEncodedPassword(password);
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
  private void parse(String cvsRoot) {

    String root = cvsRoot;

    // parse the connection method
    if (root.charAt(0) == ':') {
      // first element is the connection method
      int second = root.indexOf(':', 1);
      if (second == -1) {
        throw (new IllegalArgumentException());
      }
      this._connectionType = root.substring(1, second);
      root = root.substring(second + 1);
      int semicolon = this._connectionType.indexOf(';');
      if (semicolon != -1) {
        /**
         * @todo [01-Apr-2006:KASI] We need to handle method options as well. f.e.
         *       :pserver;proxy=www.myproxy.net;proxyport=8000:
         */
        // we need to remove some method options here
        this._connectionType = this._connectionType.substring(0, semicolon);
      }
    } else {
      // we need to set the default connection method which
      // will be changed when the repository location is
      // known
      this._connectionType = "default";
    }

    // parse the user/password information
    int separator = root.indexOf('@');
    if (separator != -1) {
      String[] userpass = root.substring(0, separator).split(":");
      root = root.substring(separator + 1);
      this._user = userpass[0];
      if (userpass.length > 1) {
        this._encodedPassword = userpass[1];
      }
    }

    // now we got the repository location, so let's see
    // if we need to adjust the connection method
    if ("default".equals(this._connectionType)) {
      if (root.charAt(0) == '/') {
        // we need to handle a local repository
        this._connectionType = "local";
      } else {
        // we need to handle a remote repository
        this._connectionType = "ext";
      }
    }

    if ((root.length() > 0) && (root.charAt(0) == '/')) {
      /**
       * @todo [01-Apr-2006:KASI] Running a local repository. Needs better support.
       */
      this._repository = root;
    } else {
      /**
       * @todo [01-Apr-2006:KASI] Currently the port parameter is not parsed.
       */
      separator = root.indexOf('/');
      if (separator == -1) {
        throw (new IllegalArgumentException());
      }
      this._host = root.substring(0, separator);
      this._repository = root.substring(separator);
    }

    if (isEmpty(this._connectionType)) {
      throw new Ant4EclipseException(PlatformExceptionCode.INVALID_CVS_ROOT, cvsRoot,
          PlatformExceptionCode.MISSING_CONNECTION_TYPE);
    }

    if (isEmpty(this._repository)) {
      throw new Ant4EclipseException(PlatformExceptionCode.INVALID_CVS_ROOT, cvsRoot,
          PlatformExceptionCode.MISSING_REPOSITORY);
    }
  }

  private boolean isEmpty(String string) {
    return (string == null || string.length() < 1);
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
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (this._connectionType == null ? 0 : this._connectionType.hashCode());
    hashCode = 31 * hashCode + (this._user == null ? 0 : this._user.hashCode());
    hashCode = 31 * hashCode + (this._host == null ? 0 : this._host.hashCode());
    hashCode = 31 * hashCode + (this._repository == null ? 0 : this._repository.hashCode());
    hashCode = 31 * hashCode + (this._encodedPassword == null ? 0 : this._encodedPassword.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    CvsRoot castedObj = (CvsRoot) o;
    return ((this._connectionType == null ? castedObj._connectionType == null : this._connectionType
        .equals(castedObj._connectionType))
        && (this._user == null ? castedObj._user == null : this._user.equals(castedObj._user))
        && (this._host == null ? castedObj._host == null : this._host.equals(castedObj._host))
        && (this._repository == null ? castedObj._repository == null : this._repository.equals(castedObj._repository)) && (this._encodedPassword == null ? castedObj._encodedPassword == null
        : this._encodedPassword.equals(castedObj._encodedPassword)));
  }
}
