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
package org.ant4eclipse.core.ant.delegate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroInstance;

/**
 */
public class MacroExecutionDelegate extends AbstractAntDelegate {

  /**
   * @param component
   */
  public MacroExecutionDelegate(final Task task) {
    super(task);
  }

  public MacroDef createMacroDef() {
    final MacroDef macroDef = new MacroDef();
    macroDef.setProject(getAntProject());
    return macroDef;
  }

  public void executeMacroInstance(final MacroDef macroDef, final String prefix,
      final Map<String, String> scopedProperties) {

    Assert.notNull(macroDef);
    Assert.nonEmpty(prefix);

    // create MacroInstance
    final MacroInstance instance = new MacroInstance();
    instance.setProject(getAntProject());
    instance.setOwningTarget(((Task) getProjectComponent()).getOwningTarget());
    instance.setMacroDef(macroDef);

    // we need a temporary map here to store all those properties that have to be reset after executing the macro
    Map<String, String> propertiesToReset = null;

    // set scopes properties
    if (scopedProperties != null) {

      propertiesToReset = new HashMap<String, String>();

      // iterate over all scoped properties
      Iterator<Map.Entry<String, String>> iterator = scopedProperties.entrySet().iterator();
      while (iterator.hasNext()) {

        final Map.Entry<String, String> entry = iterator.next();
        final String key = (prefix + "." + entry.getKey());

        // store the property if it already exists
        String existingProperty = getAntProject().getProperty(key);
        if (existingProperty != null) {
          propertiesToReset.put(key, existingProperty);
        }

        final String value = entry.getValue();
        getAntProject().setProperty(key, value);
      }
    }

    // execute macro instance
    instance.execute();

    // unset scopes properties
    if (scopedProperties != null) {
      Iterator<String> keyIterator = scopedProperties.keySet().iterator();
      while (keyIterator.hasNext()) {
        final String key = (prefix + "." + keyIterator.next());
        removeProperty(key);

        // reset the property if it existed before executing the macro
        if (propertiesToReset.containsKey(key)) {
          getAntProject().setProperty(key, propertiesToReset.get(key));
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void removeProperty(final String name) {
    Hashtable properties = null;
    // Ant 1.5 stores properties in Project
    try {
      properties = (Hashtable) getValue(getAntProject(), "properties");
      if (properties != null) {
        properties.remove(name);
      }
    } catch (final Exception e) {
      // ignore, could be Ant 1.6
    }
    try {
      properties = (Hashtable) getValue(getAntProject(), "userProperties");
      if (properties != null) {
        properties.remove(name);
      }
    } catch (final Exception e) {
      // ignore, could be Ant 1.6
    }

    // Ant 1.6 uses a PropertyHelper, can check for it by checking for a
    // reference to "ant.PropertyHelper"
    try {
      final Object property_helper = getAntProject().getReference("ant.PropertyHelper");
      if (property_helper != null) {
        try {
          properties = (Hashtable) getValue(property_helper, "properties");
          if (properties != null) {
            properties.remove(name);
          }
        } catch (final Exception e) {
          // ignore
        }
        try {
          properties = (Hashtable) getValue(property_helper, "userProperties");
          if (properties != null) {
            properties.remove(name);
          }
        } catch (final Exception e) {
          // ignore
        }
      }
    } catch (final Exception e) {
      // ignore, could be Ant 1.5
    }
  }

  /**
   * Object rape: fondle the private parts of an object without it's permission.
   * 
   * @param instance
   *          the object instance
   * @param fieldName
   *          the name of the field
   * @return an object representing the value of the field
   * @exception IllegalAccessException
   *              foiled by the security manager
   * @exception NoSuchFieldException
   *              Darn, nothing to fondle
   */
  private Object getValue(final Object instance, final String fieldName) throws IllegalAccessException,
      NoSuchFieldException {
    final Field field = getField(instance.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }

  /**
   * Object rape: fondle the private parts of an object without it's permission.
   * 
   * @param thisClass
   *          The class to rape.
   * @param fieldName
   *          The field to fondle
   * @return The field value
   * @exception NoSuchFieldException
   *              Darn, nothing to fondle.
   */
  @SuppressWarnings("unchecked")
  private Field getField(final Class thisClass, final String fieldName) throws NoSuchFieldException {
    if (thisClass == null) {
      throw new NoSuchFieldException("Invalid field : " + fieldName);
    }
    try {
      return thisClass.getDeclaredField(fieldName);
    } catch (final NoSuchFieldException e) {
      return getField(thisClass.getSuperclass(), fieldName);
    }
  }
}
