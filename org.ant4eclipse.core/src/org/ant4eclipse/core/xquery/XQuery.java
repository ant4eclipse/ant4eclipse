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
package org.ant4eclipse.core.xquery;

import java.util.Arrays;
import java.util.Vector;


import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.xml.sax.Attributes;

/**
 * This object stores a simple query used to access XML content. These queries will be visited by the SAXParser, so they
 * can collect their values. The query will be visited each time the ContentHandler of the SAXParser processes an
 * element. The query ignores these calls in case it is related to a higher level within the XML hierarchie or it has
 * been abandoned within a higher level.
 * 
 * @techres [03-Feb-2006:KASI] Only one indexed element is allowed within a query.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class XQuery {

  // the complete path in fragments, each fragment indicates the next deeper
  // level
  private String[]          _splitted;

  // a corresponding list of conditions
  private final Condition[] _conditions;

  // the attribute name in case we're handling an attribute
  private String            _attribute;

  // the counter is used in case there's a reference to a n-th element
  private final int[]       _counter;

  // the result may be a String or a Vector instance
  private Vector<String>    _values;

  // keeps the depth which is acceptable for this query
  private int               _accept;

  // a flag which indicates that some data has to be collected. this
  // is necessary because element data will be set within the call
  // of the method 'endVisit'
  private boolean           _matched;

  // a depth level from which we're willing to generate default values.
  // this means that an 'unsatisfied' query forces to generate a null value
  // entry.
  private int               _forcedepth;

  // the original query
  private final String      _xquery;

  private final String      _fileName;

  /**
   * Initializes this instance with the supplied query.
   * 
   * @param query
   *          A query used to retrieve XML data.
   */
  XQuery(final String fileName, String query) {

    this._fileName = fileName;

    this._attribute = null;
    this._forcedepth = -1;
    this._xquery = query;

    if (!query.startsWith("//")) {
      invalid(query, "Query needs to starts with two slashes !");
    }

    // reduce the first two slashes for the root indication
    query = query.substring(2);

    // create the single fragments
    this._splitted = query.split("/");

    // check if we're querying an attribute
    if (this._splitted[this._splitted.length - 1].startsWith("@")) {
      final String[] temp = new String[this._splitted.length - 1];
      this._attribute = this._splitted[this._splitted.length - 1].substring(1);
      System.arraycopy(this._splitted, 0, temp, 0, temp.length);
      this._splitted = temp;
    }

    // try to check for a forced depth
    for (int i = this._splitted.length - 1; i >= 0; i--) {
      if (this._splitted[i].startsWith("{")) {
        if (!this._splitted[i].endsWith("}")) {
          invalid(query, "Element opened with '{' lacks a corresponding '}' brace.");
        }
        this._forcedepth = i;
        this._splitted[i] = this._splitted[i].substring(1, this._splitted[i].length() - 1);
        break;
      }
    }

    // strip conditions
    this._conditions = new Condition[this._splitted.length];
    this._counter = new int[this._splitted.length];
    Arrays.fill(this._conditions, null);
    for (int i = 0; i < this._splitted.length; i++) {
      final int idx1 = this._splitted[i].indexOf('[');
      if (idx1 != -1) {
        final int idx2 = this._splitted[i].indexOf(']');
        if (idx2 == -1) {
          invalid(query, "Condition openend with '[' lacks a corresponding ']' brace.");
        }
        this._conditions[i] = createCondition(query, this._splitted[i].substring(idx1 + 1, idx2), i);
        this._splitted[i] = this._splitted[i].substring(0, idx1);
      }
    }

    reset();

  }

  /**
   * Creates a condition instance from a condition string.
   * 
   * @param query
   *          The query which has been used.
   * @param condition
   *          The condition which shall be translated into a Condition instance.
   * @param depth
   *          The depth where the newly created condition will be used.
   * 
   * @return The Condition instance which will be used while evaluating.
   */
  private Condition createCondition(final String query, final String condition, final int depth) {

    try {

      // indexed element
      return (new IndexCompare(Integer.parseInt(condition), depth));

    } catch (final NumberFormatException ex) {

      // create a count-function used to calculate the number of occurrences
      if ("count()".equals(condition)) {
        return (new CounterFunction(depth));
      }

      // this must be an attributed expression
      if (!condition.startsWith("@")) {
        /**
         * @note [03-Feb-2006:KASI] We're not checking if the attribute is on the right side.
         */
        invalid(query, "An attributed expression must start with a '@' character.");
      }

      // we're having an attribute based expression. currently only
      // the equality operator is supported
      final int idx = condition.indexOf('=');
      if (idx == -1) {
        invalid(query, "Only '=' operations are supported within an attributed expression.");
      }

      // strip the testvalue and do the comparison
      final String attrname = condition.substring(1, idx);
      final int idx1 = condition.indexOf('\'');
      final int idx2 = condition.indexOf('\'', idx1 + 1);

      if ((idx1 == -1) || (idx2 == -1)) {
        invalid(query, "The attributed expression doesn't contain a comparison value.");
      }

      final String testvalue = condition.substring(idx1 + 1, idx2);
      return (new StringCompare(attrname, testvalue));

    }

  }

  /**
   * Checks whether the current element matches this query or not.
   * 
   * @param element
   *          The element that shall be tested.
   * @param attrs
   *          The currently used attributes in case we need to check an optional condition.
   * 
   * @return true <=> The element is acceptable for this query.
   */
  private boolean matches(final String element, final Attributes attrs) {

    // there's a named element, so check it
    if ((!"*".equals(this._splitted[this._accept])) && (!element.equals(this._splitted[this._accept]))) {
      // it doesn't apply to this query, so leave here
      return (false);
    }

    // check if the condition applies to the current state
    if (this._conditions[this._accept] != null) {
      return (this._conditions[this._accept].check(element, attrs));
    }

    // the current state is supported by this query
    return (true);

  }

  /**
   * Modifies the counters.
   * 
   * @param depth
   *          The depth of the current counter.
   * @param element
   *          The currently used element.
   */
  private void adjustCounter(final int depth, final String element) {
    if (depth < this._splitted.length) {
      if (element.equals(this._splitted[depth])) {
        this._counter[depth] = this._counter[depth] + 1;
        for (int i = depth + 1; i < this._splitted.length; i++) {
          this._counter[i] = 0;
        }
      }
    }
  }

  /**
   * This function will be called whenever a new element has been entered.
   * 
   * @param depth
   *          The current depth within the XML document.
   * @param element
   *          The name of the current element.
   * @param attrs
   *          The attributes associated with this element.
   */
  void visit(final int depth, final String element, final Attributes attrs) {

    if (depth >= this._splitted.length) {
      // this element is to deep for this query
      return;
    }

    // modify the element counters
    adjustCounter(depth, element);

    if (depth == this._accept) {

      // this might be a candidate for checking
      if (matches(element, attrs)) {

        // mark the requirement to generate a value
        if ((depth == this._forcedepth) && (this._attribute == null)) {
          this._matched = true;
        }

        // all conditions met, so we can increase the depth
        // to prepare for the next level
        this._accept++;

        // it was the last part of the query, so fetch the attribute
        // if this query is used for an attribute
        if (depth == (this._splitted.length - 1)) {
          if (this._attribute != null) {
            addValue(attrs.getValue(this._attribute));
          } else {
            if (this._forcedepth == -1) {
              this._matched = true;
            }
          }
        }

      }

    }

  }

  /**
   * The element has been left.
   * 
   * @param depth
   *          The current depth within the XML document.
   * @param content
   *          The trimmed text content of the XML element.
   */
  void endVisit(final int depth, final String content) {

    if (this._accept == (depth + 1)) {
      if (this._matched) {
        if (depth == this._splitted.length - 1) {
          // nice, the query could be satisfied
          addValue(content);
          this._matched = false;
        } else if (depth == this._forcedepth) {
          // no match for this query, but we're advised to
          // create a default value
          addValue(null);
          this._matched = false;
        }
      }
      this._accept--;
    }

  }

  /**
   * Prepare this query for another XML document.
   */
  void reset() {
    this._values = null;
    this._accept = 0;
    Arrays.fill(this._counter, 0);
    this._matched = false;
  }

  /**
   * Adds the supplied value to this query.
   * 
   * @param newvalue
   *          The value that shall be added.
   */
  private void addValue(final String newvalue) {
    if (this._values == null) {
      this._values = new Vector<String>();
    }
    this._values.add(newvalue);
  }

  /**
   * Returns the data collected by this query.
   * 
   * @return The data collected by this query (Vector{String})
   */
  public String[] getResult() {
    if (this._values == null) {
      return (new String[0]);
    }
    final String[] result = new String[this._values.size()];
    this._values.toArray(result);
    return (result);
  }

  /**
   * Returns the data collected by this query. If no or one result were found, this method returns null or the result.
   * If more than one result were found, an XQueryException will be thrown.
   * 
   * @return The data collected by this query (String)
   */
  public String getSingleResult() {
    final String[] results = getResult();
    if (results.length > 1) {
      throw new Ant4EclipseException(CoreExceptionCode.X_QUERY_DUCPLICATE_ENTRY_EXCEPTION, new Object[] { this._xquery,
          this._fileName });
    }
    if (results.length == 0) {
      return (null);
    }
    return (results[0]);
  }

  /**
   * Returns true if at least one result is available.
   * 
   * @return true <=> At least one result is available.
   */
  public boolean gotResult() {
    return (this._values != null);
  }

  /**
   * Returns the counter for the supplied level.
   * 
   * @param depth
   *          The depth which counter will be returned.
   * 
   * @return The depth of the supplied level.
   */
  private int counter(final int depth) {
    // -1 because the method 'adjustCounter' will be called before
    // the use of this function.
    return (this._counter[depth] - 1);
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return (this._xquery);
  }

  /**
   * Creates an exception indicating that the query is invalid.
   * 
   * @param query
   *          The invalid query.
   * @param msg
   *          An error message.
   */
  private void invalid(final String query, final String msg) {
    throw new Ant4EclipseException(CoreExceptionCode.X_QUERY_INVALID_QUERY_EXCEPTION, new Object[] { query, msg });
  }

  /**
   * A Condition used to test a query expression.
   */
  private interface Condition {

    /**
     * Tests a query expression using the supplied attributes.
     * 
     * @param element
     *          The element name.
     * @param attrs
     *          The attributes which has to be used for the test.
     * 
     * @return true <=> The condition is met.
     */
    boolean check(String element, Attributes attrs);

  } /* ENDINTERFACE */

  /**
   * Condition implementation which checks for a specific element level.
   */
  private class IndexCompare implements Condition {

    private final int level;

    private final int index;

    /**
     * Initializes this condition using the supplied element level.
     * 
     * @param lvlindex
     *          The element count used for the check.
     * @param cnt
     *          The index used for the comparison.
     */
    public IndexCompare(final int lvlindex, final int cnt) {
      this.level = lvlindex;
      this.index = cnt;
    }

    /**
     * {@inheritDoc}
     */
    public boolean check(final String element, final Attributes attrs) {
      return (XQuery.this.counter(this.index) == this.level);
    }

  } /* ENDCLASS */

  /**
   * Implements a simple function that generates the values.
   */
  private class CounterFunction implements Condition {

    private final int index;

    /**
     * Initializes this counter.
     * 
     * @param idx
     *          The index of the counter that shall be used.
     */
    public CounterFunction(final int idx) {
      this.index = idx;
    }

    /**
     * {@inheritDoc}
     */
    public boolean check(final String element, final Attributes attrs) {

      if (XQuery.this.counter(this.index) == 0) {
        // create a new entry
        XQuery.this.addValue("0");
      }

      // increment the current value
      final Vector<String> values = XQuery.this._values;
      final String lastval = (String) values.get(values.size() - 1);
      final int value = Integer.parseInt(lastval);
      values.set(values.size() - 1, String.valueOf(value + 1));

      // function evaluation, so we don't need to process anything else
      return (false);

    }

  } /* ENDCLASS */

  /**
   * Condition implementation which checks for a specific attribute value.
   */
  private class StringCompare implements Condition {

    private final String attr;

    private final String value;

    /**
     * Initializes this string comparison condition.
     * 
     * @param attrname
     *          The name of the attribute which has to be tested.
     * @param expected
     *          The value of the expected attribute value.
     */
    public StringCompare(final String attrname, final String expected) {
      this.attr = attrname;
      this.value = expected;
    }

    /**
     * {@inheritDoc}
     */
    public boolean check(final String element, final Attributes attrs) {
      return (this.value.equals(attrs.getValue(this.attr)));
    }

  } /* ENDCLASS */

} /* ENDCLASS */
