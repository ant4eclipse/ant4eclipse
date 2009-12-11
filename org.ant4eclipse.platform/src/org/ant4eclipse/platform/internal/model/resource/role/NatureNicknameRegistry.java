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
package org.ant4eclipse.platform.internal.model.resource.role;


import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Pair;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class NatureNicknameRegistry implements Lifecycle {

  /** The prefix of properties that holds a nature nickname */
  public static final String        NATURE_NICKNAME_PREFIX = "natureNickname";

  /** all known nicknames */
  private Map<String, List<String>> _nicknames;

  /** - */
  private boolean                   _initialized           = false;

  /**
   * {@inheritDoc}
   */
  public void initialize() {
    if (this._initialized) {
      return;
    }

    // get all properties that defines a nature nickname
    Iterable<Pair<String, String>> natureNicknameEntries = Ant4EclipseConfiguration.Helper
        .getAnt4EclipseConfiguration().getAllProperties(NATURE_NICKNAME_PREFIX);

    Map<String, List<String>> nicknames = new Hashtable<String, List<String>>();
    for (Pair<String, String> natureNicknameEntry : natureNicknameEntries) {
      String nature = natureNicknameEntry.getFirst();
      String nickname = natureNicknameEntry.getSecond();
      A4ELogging.trace("Register nickname '%s' for nature '%s'", nickname, nature);
      List<String> natureids = nicknames.get(nickname);
      if (natureids == null) {
        natureids = new ArrayList<String>();
        nicknames.put(nickname, natureids);
      }
      natureids.add(nature);
    }

    this._nicknames = nicknames;

    this._initialized = true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return this._initialized;
  }

  /**
   * {@inheritDoc}
   */
  public void dispose() {
    // nothing to do...
  }

  /**
   * <p>
   * Returns <code>true</code> if the registry contains a nature for the given nickname.
   * </p>
   * 
   * @param nickname
   * @return
   */
  public boolean hasNatureForNickname(String nickname) {
    return this._nicknames.containsKey(nickname);
  }

  /**
   * <p>
   * Returns the nature for the given nickname or <code>null</code>.
   * </p>
   * 
   * @param nickname
   * @return
   */
  public String[] getNaturesForNickname(String nickname) {
    List<String> natureids = this._nicknames.get(nickname);
    return natureids.toArray(new String[natureids.size()]);
  }
}
