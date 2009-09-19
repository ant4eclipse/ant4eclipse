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
package org.ant4eclipse.platform.ant.core.delegate.helper;

import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;

import org.apache.tools.ant.Project;

import java.util.Hashtable;

/**
 * <p>
 * Helper class that provides access to the ant project's reference field. During macro execution with the
 * {@link MacroExecutionDelegate} several references are (temporarily) set. Once the execution has finished, all
 * references have to be reset.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AntReferencesRaper extends AbstractAntProjectRaper<Object> {

  /**
   * <p>
   * Creates a new instance of type {@link AntReferencesRaper}.
   * </p>
   * 
   * @param antProject
   *          the ant project
   */
  public AntReferencesRaper(Project antProject) {
    super(antProject);

    // set the value accessor
    setValueAccessor(new AntProjectValueAccessor<Object>() {

      /**
       * <p>
       * Returns the ant project reference with the given key
       * </p>
       * 
       * @param key
       *          the key
       */
      public Object getValue(String key) {
        return getAntProject().getReference(key);
      }

      /**
       * <p>
       * Sets the given value as a ant project reference with the given key.
       * </p>
       * 
       * @param key
       *          the key
       * @param value
       *          the value to set
       */
      public void setValue(String key, Object value) {
        setReference(key, value);
      }

      /**
       * <p>
       * Removes the given value from the ant project references.
       * </p>
       * 
       * @param key
       *          the key
       */
      public void unsetValue(String key) {
        removeReference(key);
      }
    });
  }

  /**
   * <p>
   * Removes the value with the given key from the ant project references.
   * </p>
   * 
   * @param key
   *          the key
   */
  @SuppressWarnings("unchecked")
  private void removeReference(String key) {
    try {
      Hashtable references = (Hashtable) AbstractAntProjectRaper.getValue(getAntProject(), "references");
      if (references != null) {
        references.remove(key);
      }
    } catch (Exception e) {
      // ignore
    }
  }

  /**
   * <p>
   * Sets the given value as an ant project references.
   * </p>
   * 
   * @param key
   *          the key
   * @param value
   *          the value
   */
  @SuppressWarnings("unchecked")
  private void setReference(String key, Object value) {
    try {
      Hashtable references = (Hashtable) AbstractAntProjectRaper.getValue(getAntProject(), "references");
      if (references != null) {
        references.put(key, value);
      }
    } catch (Exception e) {
      // ignore
    }
  }
}
