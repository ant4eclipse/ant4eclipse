package org.ant4eclipse.platform.test.builder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ant4eclipse.core.Assert;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class StringTemplate {

  /** - */
  private static String       PREFIX  = "${";

  /** - */
  private static String       POSTFIX = "}";

  public final static String  NL      = System.getProperty("line.separator");

  /** - */
  private StringBuilder       _stringTemplate;

  /** - */
  private Map<String, String> _stringsToReplace;

  /**
   * 
   */
  public StringTemplate() {
    _stringTemplate = new StringBuilder();
    _stringsToReplace = new HashMap<String, String>();
  }

  /**
   * 
   */
  public StringTemplate(String content) {
    this();

    Assert.notNull(content);

    _stringTemplate.append(content);
  }

  /**
   * @param content
   */
  public StringTemplate append(String content) {
    Assert.notNull(content);

    _stringTemplate.append(content);
    return this;
  }

  /**
   * Adds a NewLine to the Template
   * 
   * @return
   */
  public StringTemplate nl() {
    _stringTemplate.append(NL);
    return this;

  }

  /**
   * @param name
   * @param value
   */
  public void replace(String name, String value) {
    Assert.notNull(name);
    Assert.notNull(value);

    _stringsToReplace.put(name, value);
  }

  public String toString() {

    String template = _stringTemplate.toString();
    Iterator<String> iterator = _stringsToReplace.keySet().iterator();

    while (iterator.hasNext()) {

      String name = iterator.next();
      String stringToReplace = PREFIX + name + POSTFIX;
      String value = _stringsToReplace.get(name);

      int index = template.indexOf(stringToReplace);
      while (index != -1) {
        template = template.substring(0, index) + value + template.substring(index + stringToReplace.length());
        index = template.indexOf(stringToReplace);
      }
    }

    return template;
  }
}
