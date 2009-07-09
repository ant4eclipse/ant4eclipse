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
package org.ant4eclipse.core.util;

import org.ant4eclipse.core.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ManifestHelper {

  /**
   * Manifest header (named &quot;Bundle-ClassPath&quot;) identifying a list of directories and embedded JAR files,
   * which are bundle resources used to extend the bundle's classpath.
   * 
   * <p>
   * The attribute value may be retrieved from the <code>Dictionary</code> object returned by the
   * <code>Bundle.getHeaders</code> method.
   */
  public static final String BUNDLE_CLASSPATH    = "Bundle-ClassPath";

  /**
   * Manifest header (named &quot;Export-Package&quot;) identifying the packages that the bundle offers to the Framework
   * for export.
   * 
   * <p>
   * The attribute value may be retrieved from the <code>Dictionary</code> object returned by the
   * <code>Bundle.getHeaders</code> method.
   */
  public static final String EXPORT_PACKAGE      = "Export-Package";

  /**
   * Manifest header (named &quot;Bundle-Version&quot;) identifying the bundle's version.
   * 
   * <p>
   * The attribute value may be retrieved from the <code>Dictionary</code> object returned by the
   * <code>Bundle.getHeaders</code> method.
   */
  public static final String BUNDLE_VERSION      = "Bundle-Version";

  /**
   * Manifest header (named &quot;Bundle-SymbolicName&quot;) identifying the bundle's symbolic name.
   * <p>
   * The attribute value may be retrieved from the <code>Dictionary</code> object returned by the
   * <code>Bundle.getHeaders</code> method.
   */
  public static final String BUNDLE_SYMBOLICNAME = "Bundle-SymbolicName";

  /**
   * <p>
   * Returns the 'Bundle-Classpath' entries or <code>.</code> if no 'Bundle-Classpath' has been specified (default
   * value).
   * </p>
   * 
   * @return the 'Bundle-Classpath' entries or <code>.</code> if no 'Bundle-Classpath' has been specified.
   */
  public static String[] getBundleClasspath(final Manifest manifest) {

    // parse the 'Bundle-Classpath' manifest entry
    String[] bundleClasspath = splitHeader(manifest.getMainAttributes().getValue(BUNDLE_CLASSPATH));

    // set default if necessary
    if ((bundleClasspath == null) || (bundleClasspath.length < 1)) {
      bundleClasspath = new String[] { "." };
    }

    // return result
    return bundleClasspath;
  }

  public static String getManifestHeader(final Manifest manifest, final String header) {
    Assert.notNull(manifest);
    Assert.nonEmpty(header);

    return manifest.getMainAttributes().getValue(header);
  }

  public static ManifestHeaderElement[] getManifestHeaderElements(final Manifest manifest, final String header) {
    Assert.notNull(manifest);
    Assert.nonEmpty(header);

    final String manifestValue = manifest.getMainAttributes().getValue(header);

    if ((manifestValue == null) || "".equals(manifestValue.trim())) {
      return new ManifestHeaderElement[0];
    }

    return getManifestHeaderElements(manifestValue);
  }

  public static ManifestHeaderElement[] getManifestHeaderElements(final String manifestValue) {
    Assert.nonEmpty(manifestValue);

    final String[] elements = splitHeader(manifestValue);
    final List<ManifestHeaderElement> result = new LinkedList<ManifestHeaderElement>();
    for (String element : elements) {
      final ManifestHeaderElement manifestHeaderElement = new ManifestHeaderElement();
      result.add(manifestHeaderElement);
      final String[] elementParts = splitHeader(element, ";");

      for (String elementPart : elementParts) {
        String[] splitted = splitHeader(elementPart, ":=");
        // Directive found...
        if (splitted.length > 1) {
          manifestHeaderElement.addDirective(splitted[0], removeQuotes(splitted[1]));
        } else {
          splitted = splitHeader(elementPart, "=");
          if (splitted.length > 1) {
            manifestHeaderElement.addAttribute(splitted[0], removeQuotes(splitted[1]));
          } else {
            manifestHeaderElement.addValue(elementPart);
          }
        }
      }
    }

    return result.toArray(new ManifestHeaderElement[0]);
  }

  /**
   * @param header
   * @return
   */
  public static String[] splitHeader(final String header) {
    return splitHeader(header, ",");
  }

  /**
   * @param header
   * @param separator
   * @return
   */
  public static String[] splitHeader(final String header, final String separator) {
    if ((header == null) || (header.trim().length() == 0)) {
      return new String[0];
    }
    final List<String> result = new LinkedList<String>();

    final char[] chars = header.toCharArray();
    StringBuilder currentValue = new StringBuilder();
    boolean inQuotedString = false;
    for (int i = 0; i < chars.length; i++) {

      // separator string is not found:
      // - append char to current value
      // - check for the beginning or ending of quoted strings
      if (!lookup(chars, i, separator)) {

        // single '"' found: quoted string begins or ends
        if (lookup(chars, i, "\"")) {
          inQuotedString = !inQuotedString;
        }

        // filter for '\"'
        else if (lookup(chars, i, "\\\"")) {
          currentValue.append(chars[i]);
          i++;
        }

        // append char to current value
        currentValue.append(chars[i]);
      }

      // separator char found:
      // - split if we are not in a quoted string
      // - no split if we are
      else {

        // - split if we are not in a quoted string
        if (!inQuotedString) {
          result.add(currentValue.toString());
          currentValue = new StringBuilder();
          i = i + (separator.length() - 1);
        }

        // - no split if we are in a quoted string
        else {
          currentValue.append(chars[i]);
        }
      }
    }

    // add the current value
    result.add(currentValue.toString());

    return result.toArray(new String[0]);
  }

  /**
   * @param array
   * @param index
   * @param pattern
   * @return
   */
  private static boolean lookup(final char[] array, final int index, final String pattern) {
    Assert.nonEmpty(pattern);

    if (index + pattern.length() > array.length) {
      return false;
    }

    final char[] patternChars = pattern.toCharArray();

    for (int i = 0; i < patternChars.length; i++) {
      if (array[index + i] != patternChars[i]) {
        return false;
      }
    }

    return true;
  }

  private static String removeQuotes(final String value) {
    if (value == null) {
      return null;
    }

    String result = value;

    if (result.startsWith("\"")) {
      result = result.substring(1);
    }

    if (result.endsWith("\"")) {
      result = result.substring(0, result.length() - 1);
    }

    return result;
  }

  /**
   * ManifestHeaderElement --
   * 
   * @author Gerd W&uml,therich (gerd@gerd-wuetherich.de)
   */
  public static class ManifestHeaderElement {

    /** the values */
    protected List<String>        _values;

    /** the attributes */
    protected Map<String, String> _attributes;

    /** the directives */
    protected Map<String, String> _directives;

    public ManifestHeaderElement() {
      this._values = new LinkedList<String>();
      this._attributes = new HashMap<String, String>();
      this._directives = new HashMap<String, String>();
    }

    void addAttribute(final String key, final String value) {
      this._attributes.put(key, value);
    }

    void addDirective(final String key, final String value) {
      this._directives.put(key, value);
    }

    public boolean addValue(final String o) {
      return this._values.add(o);
    }

    /**
     * @return
     */
    public String[] getValues() {
      return this._values.toArray(new String[0]);
    }

    /**
     * @return
     */
    public Map<String, String> getAttributes() {
      return this._attributes;
    }

    /**
     * @return
     */
    public Map<String, String> getDirectives() {
      return this._directives;
    }

    /**
     * @generated by CodeSugar http://sourceforge.net/projects/codesugar
     */

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("[ManifestHeaderElement:");
      buffer.append(" _values: ");
      buffer.append(this._values);
      buffer.append(" _attributes: ");
      buffer.append(this._attributes);
      buffer.append(" _directives: ");
      buffer.append(this._directives);
      buffer.append("]");
      return buffer.toString();
    }

  }
  //
  // public static void main(final String[] args) {
  // final String test = "test2;include:=\"Intern\"";
  // // final String test = "test1;fest1;res1t=\"\\\"ferd1,nerd1\\\"\",tes2t;fest2;rest2=\"ferd2,nerd2\"";
  //
  // // System.out.println(test);
  // final ManifestHeaderElement[] result = ManifestHelper.getManifestHeaderElements(test);
  //
  // for (int i = 0; i < result.length; i++) {
  // System.err.println(i + " : " + result[i]);
  // }
  // }
}
