package org.ant4eclipse.platform.ant.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class MacroExecutionValues {

  private final Map<String, String> properties;

  private final Map<String, Object> references;

  public MacroExecutionValues() {
    this.properties = new HashMap<String, String>();
    this.references = new HashMap<String, Object>();
  }

  public Map<String, String> getProperties() {
    return this.properties;
  }

  public Map<String, Object> getReferences() {
    return this.references;
  }
}
