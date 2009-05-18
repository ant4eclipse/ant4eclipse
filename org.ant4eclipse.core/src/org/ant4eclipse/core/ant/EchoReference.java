package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.logging.A4ELogging;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class EchoReference extends Task {

  private String _refId = null;

  /**
   * @param refId
   *          the refId to set
   */
  public void setRefId(String refId) {
    this._refId = refId;
  }

  /**
   * @return the refId
   */
  public String getRefId() {
    return this._refId;
  }

  /**
   * @see org.apache.tools.ant.Task#execute()
   */
  @Override
  public void execute() throws BuildException {
    if (this._refId != null) {
      Object reference = getProject().getReference(this._refId);
      A4ELogging.warn("Reference '" + this._refId + "': " + reference != null ? reference.toString() : "null");
    }
  }
}
