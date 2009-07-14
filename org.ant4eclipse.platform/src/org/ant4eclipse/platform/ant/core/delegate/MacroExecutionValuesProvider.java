package org.ant4eclipse.platform.ant.core.delegate;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;

public interface MacroExecutionValuesProvider {

  public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values);
}
