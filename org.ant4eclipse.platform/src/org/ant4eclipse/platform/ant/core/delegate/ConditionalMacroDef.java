package org.ant4eclipse.platform.ant.core.delegate;

import java.lang.reflect.Field;

import org.apache.tools.ant.BuildException;
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
      this._conditionalNestedSequential = new ConditionalNestedSequential();
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
  public static class ConditionalNestedSequential extends MacroDef.NestedSequential {

    /** the sequential should only be executed if '_if == true' */
    private boolean _if     = true;

    /** the sequential should only be executed if '_unless == false' */
    private boolean _unless = false;

    /** a filter expression to filter the elements to execute a sequential for */
    private String  _filter = null;

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
    public void setFilter(String filter) {
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
  }
}
