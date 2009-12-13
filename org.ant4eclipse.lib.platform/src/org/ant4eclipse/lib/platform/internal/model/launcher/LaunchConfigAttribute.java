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
package org.ant4eclipse.lib.platform.internal.model.launcher;

import org.ant4eclipse.lib.core.Assure;

import java.util.LinkedList;
import java.util.List;

class LaunchConfigAttribute {
  private String _name;

  private Object _value;

  public LaunchConfigAttribute(String name) {
    Assure.notNull("Parameter 'name' must not be null", name);
    this._name = name;
  }

  public String getName() {
    return this._name;
  }

  public Object getValue() {
    return this._value;
  }

  public boolean isStringBasedAttribute() {
    return (this._value instanceof String);
  }

  public boolean isListAttribute() {
    return (this._value instanceof ListAttribute);
  }

  public ListAttribute getListAttributeValue() {
    Assure.assertTrue(isListAttribute(), "LauchConfigAttribute '" + this._name + "' must be a list-based attribute");

    return (ListAttribute) this._value;
  }

  public String getStringValue() {
    Assure.assertTrue(isStringBasedAttribute(), "LauchConfigAttribute '" + this._name
        + "' must be a string-based attribute");

    return (String) this._value;
  }

  public void setValue(Object value) {
    this._value = value;
  }

  static class ListAttribute {
    private List<String> _entries = new LinkedList<String>();

    public List<String> getEntries() {
      return this._entries;
    }

    void addEntry(String entry) {
      this._entries.add(entry);
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < this._entries.size(); i++) {
        builder.append(this._entries.get(i));
        if (i < this._entries.size() - 1) {
          builder.append(",");
        }
      }
      return builder.toString();
    }
  }

}