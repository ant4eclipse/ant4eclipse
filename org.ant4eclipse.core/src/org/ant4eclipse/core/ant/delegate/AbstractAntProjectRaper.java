package org.ant4eclipse.core.ant.delegate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.ant4eclipse.core.Assert;
import org.apache.tools.ant.Project;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <V>
 */
public abstract class AbstractAntProjectRaper<V> {

  /** the ant project */
  private final Project              _antProject;

  /** the prefix used for the scoped values */
  private String                     _prefix;

  /** the scoped values */
  private Map<String, V>             _scopedValues;

  /** the overridden values */
  private Map<String, V>             _overriddenValues;

  /** the value accessor */
  private AntProjectValueAccessor<V> _valueAccessor;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractAntProjectRaper}.
   * </p>
   * 
   * @param antProject
   *          the ant project
   */
  public AbstractAntProjectRaper(Project antProject) {
    this._antProject = antProject;
  }

  /**
   * <p>
   * Returns the ant project.
   * </p>
   * 
   * @return the ant project.
   */
  public final Project getAntProject() {
    return this._antProject;
  }

  /**
   * <p>
   * Sets the specified scoped values.
   * </p>
   * 
   * @param scopedValues
   *          the scoped values.
   * @param prefix
   *          the prefix used for the scoped values.
   */
  public final void setScopedValues(Map<String, V> scopedValues, String prefix) {
    Assert.assertTrue(this._scopedValues == null, "Scoped values are already set!");
    Assert.notNull(scopedValues);

    // set the scoped values
    this._scopedValues = scopedValues;
    this._overriddenValues = new HashMap<String, V>();

    // set the prefix
    this._prefix = prefix != null ? prefix + "." : "";

    // iterate over all scoped properties
    Iterator<Entry<String, V>> iterator = this._scopedValues.entrySet().iterator();
    while (iterator.hasNext()) {

      final Map.Entry<String, V> entry = iterator.next();
      final String key = (this._prefix + entry.getKey());

      // store the property if it already exists
      V existingValue = this._valueAccessor.getValue(key);
      if (existingValue != null) {
        this._overriddenValues.put(key, existingValue);
      }

      final V newValue = entry.getValue();
      this._valueAccessor.setValue(key, newValue);
    }
  }

  public final void unsetScopedValues() {
    Assert.assertTrue(this._scopedValues != null, "Scoped values must be set!");

    // unset scopes value
    Iterator<String> keyIterator = this._scopedValues.keySet().iterator();
    while (keyIterator.hasNext()) {
      final String key = (this._prefix + keyIterator.next());
      this._valueAccessor.unsetValue(key);

      // reset the property if it existed before executing the macro
      if (this._overriddenValues.containsKey(key)) {
        this._valueAccessor.setValue(key, this._overriddenValues.get(key));
      }
    }

    this._scopedValues = null;
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
  public static Object getValue(final Object instance, final String fieldName) throws IllegalAccessException,
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
  public static Field getField(final Class thisClass, final String fieldName) throws NoSuchFieldException {
    if (thisClass == null) {
      throw new NoSuchFieldException("Invalid field : " + fieldName);
    }
    try {
      return thisClass.getDeclaredField(fieldName);
    } catch (final NoSuchFieldException e) {
      return getField(thisClass.getSuperclass(), fieldName);
    }
  }

  public void setValueAccessor(AntProjectValueAccessor<V> valueAccessor) {
    this._valueAccessor = valueAccessor;
  }

  /**
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   * 
   * @param <V>
   */
  public interface AntProjectValueAccessor<V> {

    /**
     * @param key
     * @return
     */
    public V getValue(String key);

    /**
     * @param key
     * @param value
     */
    public void setValue(String key, V value);

    /**
     * @param key
     */
    public void unsetValue(String key);
  }
}
