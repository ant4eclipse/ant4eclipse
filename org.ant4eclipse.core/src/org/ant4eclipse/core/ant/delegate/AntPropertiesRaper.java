package org.ant4eclipse.core.ant.delegate;

import java.util.Hashtable;

import org.apache.tools.ant.Project;

public class AntPropertiesRaper extends AbstractAntProjectRaper<String> {

  public AntPropertiesRaper(Project antProject) {
    super(antProject);

    setValueAccessor(new AntProjectValueAccessor<String>() {
      public String getValue(String key) {
        return getAntProject().getProperty(key);
      }

      public void setValue(String key, String value) {
        getAntProject().setProperty(key, value);
      }

      public void unsetValue(String key) {
        removeProperty(key);
      }
    });
  }

  @SuppressWarnings("unchecked")
  private void removeProperty(final String name) {
    Hashtable properties = null;
    // Ant 1.5 stores properties in Project
    try {
      properties = (Hashtable) AbstractAntProjectRaper.getValue(getAntProject(), "properties");
      if (properties != null) {
        properties.remove(name);
      }
    } catch (final Exception e) {
      // ignore, could be Ant 1.6
    }
    try {
      properties = (Hashtable) AbstractAntProjectRaper.getValue(getAntProject(), "userProperties");
      if (properties != null) {
        properties.remove(name);
      }
    } catch (final Exception e) {
      // ignore, could be Ant 1.6
    }

    // Ant 1.6 uses a PropertyHelper, can check for it by checking for a
    // reference to "ant.PropertyHelper"
    try {
      final Object property_helper = getAntProject().getReference("ant.PropertyHelper");
      if (property_helper != null) {
        try {
          properties = (Hashtable) AbstractAntProjectRaper.getValue(property_helper, "properties");
          if (properties != null) {
            properties.remove(name);
          }
        } catch (final Exception e) {
          // ignore
        }
        try {
          properties = (Hashtable) AbstractAntProjectRaper.getValue(property_helper, "userProperties");
          if (properties != null) {
            properties.remove(name);
          }
        } catch (final Exception e) {
          // ignore
        }
      }
    } catch (final Exception e) {
      // ignore, could be Ant 1.5
    }
  }
}
