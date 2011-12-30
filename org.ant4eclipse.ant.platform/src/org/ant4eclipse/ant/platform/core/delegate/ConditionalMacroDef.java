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
package org.ant4eclipse.ant.platform.core.delegate;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.ldapfilter.LdapFilter;
import org.ant4eclipse.lib.core.ldapfilter.ParseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.StringMap;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicAttribute;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.taskdefs.MacroDef;

/**
 * <p>
 * Extends the {@link MacroDef} to support conditional execution.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ConditionalMacroDef extends MacroDef {

  /** the conditional nested sequential */
  private ConditionalNestedSequential _conditionalNestedSequential;

  /**
   * <p>
   * Returns the filter expression.
   * </p>
   * 
   * @return the filter expression.
   */
  public String getFilter() {
    return this._conditionalNestedSequential.getFilter();
  }

  /**
   * <p>
   * Returns the 'if' condition.
   * </p>
   * 
   * @return the 'if' condition.
   */
  public boolean isIf() {
    return this._conditionalNestedSequential.isIf();
  }

  /**
   * <p>
   * Returns the 'unless' condition.
   * </p>
   * 
   * @return the 'unless' condition.
   */
  public boolean isUnless() {
    return this._conditionalNestedSequential.isUnless();
  }

  /**
   * Returns the value of the given attribute or <tt>defaultValue</tt> if the attribute has not been set on this
   * ConditionalMacroDef
   * 
   * @param name
   *          The name of the attribute
   * @param defaultValue
   *          The default value that is returned when the attribute is not set
   * @return The attribute value or defaultValue
   */
  public String getAttribute(String name, String defaultValue) {
    return this._conditionalNestedSequential.getAttribute(name, defaultValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NestedSequential createSequential() {
    try {

      // object rape - access the nestedSequential field...
      Field field = MacroDef.class.getDeclaredField("nestedSequential");
      field.setAccessible(true);

      // copied from the original createSequential method
      if (field.get(this) != null) {
        throw new BuildException("Only one sequential allowed");
      }

      // set the conditional nested sequential
      this._conditionalNestedSequential = new ConditionalNestedSequential(this);
      field.set(this, this._conditionalNestedSequential);

      // return the result
      return this._conditionalNestedSequential;
    } catch (Throwable e) {
      // what should be do now!?
      e.printStackTrace();
      throw new BuildException("Internal Ant4Eclipse error...");
    }
  }

  /**
   * <p>
   * Extends the {@link NestedSequential} to support conditional execution.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class ConditionalNestedSequential extends MacroDef.NestedSequential implements DynamicAttribute {

    /** the sequential should only be executed if '_if == true' */
    private boolean             _if         = true;

    /** the sequential should only be executed if '_unless == false' */
    private boolean             _unless     = false;

    /** a filter expression to filter the elements to execute a sequential for */
    private String              _filter     = null;

    /** the parent conditional macro definition */
    private ConditionalMacroDef _conditionalMacroDef;

    /** a {@link StringMap} containing the (dynamic) attributes of this Sequential */
    private StringMap           _attributes = new StringMap();

    /**
     * <p>
     * </p>
     * 
     * @param conditionalMacroDef
     */
    public ConditionalNestedSequential(ConditionalMacroDef conditionalMacroDef) {
      Assure.notNull("conditionalMacroDef", conditionalMacroDef);

      this._conditionalMacroDef = conditionalMacroDef;
    }

    /**
     * <p>
     * Returns the filter expression.
     * </p>
     * 
     * @return the filter expression.
     */
    public String getFilter() {
      return this._filter;
    }

    /**
     * <p>
     * Sets the filter expression.
     * </p>
     * 
     * @param filter
     *          the filter expression.
     */
    @SuppressWarnings("unchecked")
    public void setFilter(String filter) {

      // try to parse the filter
      try {
        new LdapFilter(new HashMap<String, String>(), new StringReader(filter)).validate();
      }
      // in case of an exception we have create an useful BuildException
      catch (ParseException e) {
        try {

          if (A4ELogging.isDebuggingEnabled()) {
            A4ELogging.debug("Exception while parsing filter string '%s'.", filter);
          }

          // get the current UnknownElement
          UnknownElement element = (UnknownElement) this._conditionalMacroDef.getProject().getThreadTask(
              Thread.currentThread());

          if (A4ELogging.isDebuggingEnabled()) {
            A4ELogging.debug("Current UnknownElement is '%s'.", element);
          }

          // search for the unknown element that causes the problem
          for (UnknownElement unknownElement : (List<UnknownElement>) element.getChildren()) {
            if (equals(unknownElement.getWrapper().getProxy())) {
              throw new BuildException("Invalid filter '" + filter + "'.", unknownElement.getLocation());
            }
          }

          // no element found -> throw simple BuildException
          throw new BuildException("Invalid filter '" + filter + "'.");
        } catch (BuildException exception) {
          throw exception;
        } catch (Exception exception) {
          // 
          if (A4ELogging.isDebuggingEnabled()) {
            A4ELogging
                .debug("Exception while computing the line number for an exception: '%s'", exception.getMessage());
          }

          // no element found -> throw simple BuildException
          throw new BuildException("Invalid filter '" + filter + "'.");
        }
      }

      // set the filter
      this._filter = filter;
    }

    /**
     * <p>
     * Returns the 'if' condition.
     * </p>
     * 
     * @return the 'if' condition.
     */
    public boolean isIf() {
      return this._if;
    }

    /**
     * <p>
     * Sets the 'if' condition.
     * </p>
     * 
     * @param aIf
     *          the 'if' condition.
     */
    public void setIf(boolean aIf) {
      this._if = aIf;
    }

    /**
     * <p>
     * Returns the 'unless' condition.
     * </p>
     * 
     * @return the 'unless' condition.
     */
    public boolean isUnless() {
      return this._unless;
    }

    /**
     * <p>
     * Sets the 'unless' condition.
     * </p>
     * 
     * @param the
     *          'unless' condition.
     */
    public void setUnless(boolean unless) {
      this._unless = unless;
    }

    /**
     * Allows the specification of additional attributes on a MacroDefinition
     * 
     * @see org.apache.tools.ant.DynamicAttribute#setDynamicAttribute(java.lang.String, java.lang.String)
     * 
     */
    @Override
    public void setDynamicAttribute(String name, String value) throws BuildException {
      this._attributes.put(name, value);
    }

    /**
     * Returns the attribute with the given name or <tt>defaultValue</tt> if the attribute has not been set.
     * 
     * @param name
     *          the attribute name
     * @param defaultValue
     *          the default value
     * @return the attribute value or defaultValie
     */
    public String getAttribute(String name, String defaultValue) {
      return this._attributes.get(name, defaultValue);
    }
  }
}
