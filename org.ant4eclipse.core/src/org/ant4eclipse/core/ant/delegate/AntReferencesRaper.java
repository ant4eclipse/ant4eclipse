package org.ant4eclipse.core.ant.delegate;

import java.util.Hashtable;

import org.apache.tools.ant.Project;

public class AntReferencesRaper extends AbstractAntProjectRaper<Object> {

  public AntReferencesRaper(Project antProject) {
    super(antProject);

    setValueAccessor(new AntProjectValueAccessor<Object>() {

      public Object getValue(String key) {
        return getAntProject().getReference(key);
      }

      public void setValue(String key, Object value) {
        setReference(key, value);
      }

      public void unsetValue(String key) {
        removeReference(key);
      }
    });
  }

  @SuppressWarnings("unchecked")
  private void removeReference(final String key) {
    try {
      Hashtable references = (Hashtable) AbstractAntProjectRaper.getValue(getAntProject(), "references");
      if (references != null) {
        references.remove(key);
      }
    } catch (final Exception e) {
      // ignore
    }
  }

  @SuppressWarnings("unchecked")
  private void setReference(final String key, Object value) {
    try {
      Hashtable references = (Hashtable) AbstractAntProjectRaper.getValue(getAntProject(), "references");
      if (references != null) {
        references.put(key, value);
      }
    } catch (final Exception e) {
      // ignore
    }
  }
}
