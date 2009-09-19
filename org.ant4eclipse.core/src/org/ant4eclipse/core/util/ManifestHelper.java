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
import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.exception.Ant4EclipseException;

import java.util.LinkedList;
import java.util.List;
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
   * Returns the 'Bundle-SymbolicName' entry for the given manifest. If not such entry exists, an Exception will be
   * thrown.
   * </p>
   * 
   * @param manifest
   *          the manifest
   * 
   * @return The symbolic name.
   * 
   * @throws Ant4EclipseException
   *           if the symbolic name could not be found.
   */
  public static String getBundleSymbolicName(Manifest manifest) {
    Assert.notNull(manifest);

    // get the manifest header elements
    ManifestHelper.ManifestHeaderElement[] elements = ManifestHelper.getManifestHeaderElements(manifest,
        ManifestHelper.BUNDLE_SYMBOLICNAME);

    // assert that entry exists
    if (elements == null || elements.length == 0 || elements[0].getValues() == null
        || elements[0].getValues().length == 0) {

      throw new Ant4EclipseException(CoreExceptionCode.MANIFEST_HEADER_DOES_NOT_EXIST,
          ManifestHelper.BUNDLE_SYMBOLICNAME);
    }

    // return the symbolic name
    return elements[0].getValues()[0];
  }

  /**
   * <p>
   * Returns the 'Bundle-Classpath' entries or <code>.</code> if no 'Bundle-Classpath' has been specified (default
   * value).
   * </p>
   * 
   * @param manifest
   *          The manifest providing the necessary information. Not <code>null</code>.
   * 
   * @return the 'Bundle-Classpath' entries or <code>.</code> if no 'Bundle-Classpath' has been specified.
   */
  public static String[] getBundleClasspath(Manifest manifest) {

    // parse the 'Bundle-Classpath' manifest entry
    String[] bundleClasspath = splitHeader(manifest.getMainAttributes().getValue(BUNDLE_CLASSPATH));

    // set default if necessary
    if ((bundleClasspath == null) || (bundleClasspath.length < 1)) {
      bundleClasspath = new String[] { "." };
    }

    // return result
    return bundleClasspath;
  }

  public static String getManifestHeader(Manifest manifest, String header) {
    Assert.notNull(manifest);
    Assert.nonEmpty(header);

    return manifest.getMainAttributes().getValue(header);
  }

  public static ManifestHeaderElement[] getManifestHeaderElements(Manifest manifest, String header) {
    Assert.notNull(manifest);
    Assert.nonEmpty(header);

    String manifestValue = manifest.getMainAttributes().getValue(header);

    if ((manifestValue == null) || "".equals(manifestValue.trim())) {
      return new ManifestHeaderElement[0];
    }

    return getManifestHeaderElements(manifestValue);
  }

  public static ManifestHeaderElement[] getManifestHeaderElements(String manifestValue) {
    Assert.nonEmpty(manifestValue);

    String[] elements = splitHeader(manifestValue);
    List<ManifestHeaderElement> result = new LinkedList<ManifestHeaderElement>();
    for (String element : elements) {
      ManifestHeaderElement manifestHeaderElement = new ManifestHeaderElement();
      result.add(manifestHeaderElement);
      String[] elementParts = splitHeader(element, ";");

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
  public static String[] splitHeader(String header) {
    return splitHeader(header, ",");
  }

  /**
   * @param header
   * @param separator
   * @return
   */
  public static String[] splitHeader(String header, String separator) {
    if ((header == null) || (header.trim().length() == 0)) {
      return new String[0];
    }
    List<String> result = new LinkedList<String>();

    char[] chars = header.toCharArray();
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
  private static boolean lookup(char[] array, int index, String pattern) {
    Assert.nonEmpty(pattern);

    if (index + pattern.length() > array.length) {
      return false;
    }

    char[] patternChars = pattern.toCharArray();

    for (int i = 0; i < patternChars.length; i++) {
      if (array[index + i] != patternChars[i]) {
        return false;
      }
    }

    return true;
  }

  private static String removeQuotes(String value) {
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
    protected List<String>       _values;

    /** the attributes */
    protected ExtendedProperties _attributes;

    /** the directives */
    protected ExtendedProperties _directives;

    public ManifestHeaderElement() {
      this._values = new LinkedList<String>();
      this._attributes = new ExtendedProperties();
      this._directives = new ExtendedProperties();
    }

    void addAttribute(String key, String value) {
      this._attributes.put(key, value);
    }

    void addDirective(String key, String value) {
      this._directives.put(key, value);
    }

    public boolean addValue(String o) {
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
    public ExtendedProperties getAttributes() {
      return this._attributes;
    }

    /**
     * @return
     */
    public ExtendedProperties getDirectives() {
      return this._directives;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      StringBuffer buffer = new StringBuffer();
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
}
