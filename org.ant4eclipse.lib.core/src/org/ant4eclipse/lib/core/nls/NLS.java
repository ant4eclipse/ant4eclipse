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
package org.ant4eclipse.lib.core.nls;

import org.ant4eclipse.lib.core.exception.ExceptionCode;
import org.ant4eclipse.lib.core.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * <b>Support for I18N</b>
 * <p>
 * This class sets <tt>public static</tt> fields of a class with values read from (internationalized) properties files
 * 
 * <p>
 * To initialize the fields, a class needs to call {@link #initialize(Class)}, passing itself as the parameter. This can
 * be done in the <code>static</code> block of a class:
 * 
 * <pre>
 * package org.ant4eclipse;
 * 
 * public class MyClass {
 *   public static String myMessage;
 *   public static ExceptionCode myExceptionCode;
 * 
 *   static {
 *     initialize(MyClass.class)
 *   }
 * }
 * </pre>
 * 
 * <p>
 * To get the localized message a Property file will be read at runtime.
 * 
 * <p>
 * It is possible to specify default messages using the {@link NLSMessage} annotation:
 * 
 * <pre>
 * package org.ant4eclipse;
 * 
 * public class MyClass {
 *   &#064;NLSMessage(&quot;My Default Message&quot;)
 *   public static String myMessage;
 *   &#064;NLSMessage(&quot;My default exception code&quot;)
 *   public static ExceptionCode myExceptionCode;
 * 
 *   static {
 *     initialize(MyClass.class)
 *   }
 * }
 * </pre>
 * 
 * <p>
 * The NLSInitializer class has built-in support for Strings, Exception Codes and any <tt>public static</tt> fields that
 * are marked with {@link NLSMessage} and have a type with a single-argument constructor taking a String.
 * 
 * @todo [10-Dec-2009:KASI] I need to recheck this. I suspect that the properties should be loaded completely for the
 *       setup so applying the values would not happen within a custom Properties implementation (allows to remove some
 *       checking code and to simplify this code).
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public abstract class NLS {

  /** - */
  private static final String MSG_MISUSEDNLSANNOTATION     = "NLS-Annotation detected on field with wrong modifiers '%s.%s'. Field is %s";

  /** - */
  private static final String MSG_DEFAULTMESSAGE           = "[WARN: No (default) message for field '%s' found]";

  /** - */
  private static final String MSG_MISSINGCONSTRUCTOR       = "Could not find constructor '%s' (String) on type : %s";

  /** - */
  private static final String MSG_COULDNOTINSTANTIATECLASS = "The class '%s' could not be instantiated using constructor '%s'";

  /** The file extensions for files that contain messages */
  private static final String EXTENSION                    = ".properties";

  /** all suffixes for current locale ("en_En", "en", "") */
  private static String[]     nlSuffixes;

  /**
   * Initializes all (NLS) fields of the given class
   * 
   * @param clazz
   *          The class which field values will be setup with internationalised information. Not <code>null</code>.
   */
  public static final void initialize(Class<?> clazz) {
    Field[] fields = clazz.getDeclaredFields();

    Map<String, Field> nlsFields = new Hashtable<String, Field>();

    // Detect NLS fields (public String fields)
    for (Field field : fields) {
      if (isNLSField(field)) {
        nlsFields.put(field.getName(), field);
      }
    }

    // get a list of potential property files accoring to the current locale
    // for this class (MyClass_en_EN.properties, MyClass_en.properties, MyClass.properties)
    String baseName = clazz.getName();
    String[] variants = buildVariants(baseName);

    // Create holder for the messages. The put()-method of MessageProperties
    // will set a new property not only to the properties instance but
    // also to the appropriate field on "clazz"
    Properties messages = new MessageProperties(clazz, nlsFields);

    // Load messages from properties files and set them (via MessageProperties)
    // to the appropriate fields on clazz
    loadProperties(messages, variants);

    // Determine fields that have not been set (because the property
    // is missing in the properties-file)
    // Assign a dummy message to fields that don't have a message to
    // avoid null pointer at runtime
    for (Map.Entry<String, Field> entry : nlsFields.entrySet()) {
      String fieldName = entry.getKey().toString();
      if (!messages.containsKey(fieldName)) {
        Field field = entry.getValue();
        String defaultMessage = getDefaultMessage(field);
        try {
          field.set(null, getFieldValue(field, defaultMessage));
        } catch (Exception ex) {
          // should not happen
        }
      }
    }
  }

  /**
   * Returns a default message that will be set to the given field.
   * 
   * <p>
   * The default message is read from the field's {@link NLSMessage} annotation. If there is no annotation or if the
   * annotation has no value, a dummy message will be returned to avoid null pointer exceptions later at runtime when
   * the field get accessed
   * 
   * @param field
   *          The field
   * @return A message for the field. Never null
   */
  private static final String getDefaultMessage(Field field) {
    NLSMessage nlsMessage = field.getAnnotation(NLSMessage.class);
    if ((nlsMessage.value() == null) || (nlsMessage.value().trim().length() == 0)) {
      return String.format(MSG_DEFAULTMESSAGE, field);
    }
    return nlsMessage.value();
  }

  /**
   * Converts the given String <tt>value</tt> to an object that can be set to the given field
   * 
   * @param field
   *          The field that should take the converted object
   * @param value
   *          The value to convert
   * @return
   */
  private static final Object getFieldValue(Field field, String value) {
    if (String.class == field.getType()) {
      // for fields of type String return value as-is
      return value;
    } else {
      // in all other cases try to construct an object using it's class single-arg constructor
      return newObjectFromString(field.getType(), value);
    }
  }

  /**
   * Instantiates a new object of the given type with the given message
   * 
   * <p>
   * The type must have a (declared) constructor that takes a single string parameter
   * 
   * @param type
   *          The concrete type that should be instantiated.
   * @param message
   *          The message for the ExceptionCodes's constructor
   * @return the instantiated ExceptionCode
   */
  private static final Object newObjectFromString(Class<?> type, String message) {
    Constructor<?> constructor;
    try {
      constructor = type.getDeclaredConstructor(String.class);
      constructor.setAccessible(true);
    } catch (Exception ex) {
      throw new RuntimeException(String.format(MSG_MISSINGCONSTRUCTOR, type.getSimpleName(), ex.getMessage()), ex);
    }
    try {
      return constructor.newInstance(message);
    } catch (Exception ex) {
      throw new RuntimeException(String.format(MSG_COULDNOTINSTANTIATECLASS, type.getName(), type.getSimpleName()), ex);
    }
  }

  /**
   * Checks if the given field is an "NLS field".
   * 
   * A NLS field must be public static and not final.
   * 
   * @param field
   *          the field to check
   * @return true if it is a "NLS field" that should be set to a localized value
   */
  private static final boolean isNLSField(Field field) {
    int modifier = field.getModifiers();

    String problem = null;

    // check if modifiers are correct
    if (!Modifier.isStatic(modifier)) {
      problem = "not static";
    } else if (Modifier.isFinal(modifier)) {
      problem = "final";
    } else if (!Modifier.isPublic(modifier)) {
      problem = "not public";
    }

    NLSMessage nlsMessage = field.getAnnotation(NLSMessage.class);
    if (nlsMessage == null) {
      // not an NLS field, no problem, just ignore it
      return false;
    }

    if (problem != null) {
      // NLS-annotation on a field with wrong modifiers (not public static non-final)
      throw new RuntimeException(String.format(MSG_MISUSEDNLSANNOTATION, field.getDeclaringClass().getName(), field
          .getName(), problem));
    }

    // NLS fields are fields with @NLSMessage annotation and fields with type String and Exception code (and subclasses)
    return (field.getType() == String.class || ExceptionCode.class.isAssignableFrom(field.getType()));
  }

  /**
   * Build an array of property files to search. The returned array contains the property fields in order from most
   * specific to most generic. So, in the FR_fr locale, it will return file_fr_FR.properties, then file_fr.properties,
   * and finally file.properties.
   */
  private static final String[] buildVariants(String root) {
    if (nlSuffixes == null) {
      // build list of suffixes for loading resource bundles
      String nl = Locale.getDefault().toString();
      ArrayList<String> result = new ArrayList<String>(4);
      int lastSeparator;
      while (true) {
        result.add('_' + nl + EXTENSION);
        lastSeparator = nl.lastIndexOf('_');
        if (lastSeparator == -1) {
          break;
        }
        nl = nl.substring(0, lastSeparator);
      }
      // add the empty suffix last (most general)
      result.add(EXTENSION);
      nlSuffixes = result.toArray(new String[result.size()]);
    }
    root = root.replace('.', '/');
    String[] variants = new String[nlSuffixes.length];
    for (int i = 0; i < variants.length; i++) {
      variants[i] = root + nlSuffixes[i];
    }
    return variants;
  }

  /**
   * Load all (existing) properties files, that are specified in <tt>variants</tt>.
   * <p>
   * Properties files listed in variants, that are not existing, are ignored
   * </p>
   * 
   * @param messages
   *          A properties object that will contain the properties
   * @param variants
   *          file names to read
   */
  private static final void loadProperties(Properties messages, String[] variants) {
    for (String variant : variants) {
      InputStream is = null;
      try {
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(variant);
        if (is != null) {
          messages.load(is);
        }
      } catch (IOException ex) {
        System.err.println("Could not read properties file '" + variant + "': " + ex);
        continue;
      } finally {
        Utilities.close(is);
      }
    }
  }

  /**
   * MessageProperties holds the messages for a class
   * 
   * @author Nils Hartmann <nils@nilshartmann.net>
   * @version $Revision$
   */
  private static class MessageProperties extends Properties {

    /** the class this MessageProperties objects holds the messages for */
    private Class<?>           _targetClass;

    /** all the fields on the target class that can have messages assigned to */
    private Map<String, Field> _fields;

    public MessageProperties(Class<?> targetClass, Map<String, Field> fields) {
      this._targetClass = targetClass;
      this._fields = fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object put(Object key, Object value) {
      if (containsKey(key)) {
        // the given field has already been set. simply return it's first value (we don't want to override)
        return super.get(key);
      }

      Field field = this._fields.get(key);

      if (field == null) {
        // Property not known
        System.out.println("Message-Property '" + key + "' existiert nicht an Ziel-Klasse '" + this._targetClass + "'");
        return null;
      }

      Object fieldValue = getFieldValue(field, (String) value);

      try {
        field.set(null, fieldValue);
      } catch (Exception ex) {
        System.err.println("Could not set field '" + field + "': " + ex);
        return null;
      }

      // Avoid setting the field more than one time
      super.put(key, fieldValue);

      return null;
    }

  } /* ENDCLASS */

} /* ENDCLASS */
