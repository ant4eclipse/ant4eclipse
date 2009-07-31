package org.ant4eclipse.platform.ant.core.delegate;

import java.lang.reflect.Field;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;

public class ConditionalMacroDef extends MacroDef {

  private ConditionalNestedSequential _conditionalNestedSequential;

  /**
   * @return
   */
  public String getFilter() {
    return this._conditionalNestedSequential.getFilter();
  }

  /**
   * @return
   */
  public boolean isIf() {
    return this._conditionalNestedSequential.isIf();
  }

  /**
   * @return
   */
  public boolean isUnless() {
    return this._conditionalNestedSequential.isUnless();
  }

  @Override
  public NestedSequential createSequential() {
    try {
      Field field = MacroDef.class.getDeclaredField("nestedSequential");
      field.setAccessible(true);

      if (field.get(this) != null) {
        throw new BuildException("Only one sequential allowed");
      }
      this._conditionalNestedSequential = new ConditionalNestedSequential();
      field.set(this, this._conditionalNestedSequential);

      return (NestedSequential) field.get(this);
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new BuildException();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new BuildException();
    } catch (NoSuchFieldException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new BuildException();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new BuildException();
    }
  }

  public static class ConditionalNestedSequential extends MacroDef.NestedSequential {

    /** - */
    private boolean _if     = true;

    /** - */
    private boolean _unless = false;

    /** - */
    private String  _filter = null;

    /**
     * @return
     */
    public String getFilter() {
      return this._filter;
    }

    /**
     * @param filter
     */
    public void setFilter(String filter) {
      this._filter = filter;
    }

    /**
     * @return
     */
    public boolean isIf() {
      return this._if;
    }

    public void setIf(boolean if1) {
      this._if = if1;
    }

    /**
     * @return
     */
    public boolean isUnless() {
      return this._unless;
    }

    /**
     * @param unless
     */
    public void setUnless(boolean unless) {
      this._unless = unless;
    }
  }
}
