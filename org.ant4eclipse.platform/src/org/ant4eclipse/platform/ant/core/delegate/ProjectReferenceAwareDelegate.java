package org.ant4eclipse.platform.ant.core.delegate;

import org.ant4eclipse.platform.ant.core.ProjectReferenceAwareComponent;
import org.apache.tools.ant.BuildException;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectReferenceAwareDelegate implements ProjectReferenceAwareComponent {

  /** project reference types */
  private String[] _projectReferenceTypes;

  public String[] getProjectReferenceTypes() {
    return this._projectReferenceTypes;
  }

  public boolean isProjectReferenceTypesSet() {
    return this._projectReferenceTypes != null;
  }

  public void requireProjectReferenceTypesSet() {
    if (!isProjectReferenceTypesSet()) {
      // TODO
      throw new BuildException("referenceTypes has to be set!");
    }
  }

  public void setProjectReferenceTypes(String projectReferenceTypes) {
    //
    if (projectReferenceTypes == null) {
      this._projectReferenceTypes = new String[] {};
    } else {
      String[] names = projectReferenceTypes.split(",");

      // 
      this._projectReferenceTypes = new String[names.length];

      for (int i = 0; i < names.length; i++) {
        this._projectReferenceTypes[i] = names[i].trim();
      }
    }
  }
}
