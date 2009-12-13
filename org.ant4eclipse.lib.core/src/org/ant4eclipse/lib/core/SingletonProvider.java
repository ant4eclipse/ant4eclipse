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

import java.util.Hashtable;
import java.util.Map;

/**
 * Simple class which is used to manage singleton instances. The main purpose of this class is the different context
 * this library can run with. As a library singletons can obviously managed in their classes but within Ant multiple
 * taskdefs might occure so the reloading of classes would case these singletons to get lost. This is perfectly legal in
 * Ant as references etc. have to be managed within an Ant project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class SingletonProvider {

  private static SingletonProvider PROVIDER = new SingletonProvider();

  private Map<String, Object>      singletons;

  protected SingletonProvider() {
    this.singletons = new Hashtable<String, Object>();
  }

  /**
   * Returns a currently registered singleton.
   * 
   * @param key
   *          The key used to access the singleton. Neither <code>null</code> nor empty.
   * 
   * @return The singleton itself if it has been registered before.
   */
  public Object getSingleton(String key) {
    Assure.paramNotNull("key", key);
    return this.singletons.get(key);
  }

  /**
   * Changes/sets a current singleton.
   * 
   * @param key
   *          The key used to identify the singleton. Neither <code>null</code> nor empty.
   * @param singleton
   *          The singleton instance which has to be registered. Not <code>null</code>.
   */
  public void setSingleton(String key, Object singleton) {
    Assure.paramNotNull("key", key);
    Assure.paramNotNull("singleton", singleton);
    this.singletons.put(key, singleton);
  }

  /**
   * Returns the currently used SingletonProvider instance.
   * 
   * @return The currently used SingletonProvider instance. Not <code>null</code>.
   */
  public static final SingletonProvider instance() {
    return PROVIDER;
  }

  /**
   * Changes the currently used SingletonProvider instance.
   * 
   * @param newprovider
   *          The new SingletonProvider instance. Not <code>null</code>.
   */
  public static final void setInstance(SingletonProvider newprovider) {
    Assure.paramNotNull("newprovider", newprovider);
    PROVIDER = newprovider;
  }

} /* ENDCLASS */
