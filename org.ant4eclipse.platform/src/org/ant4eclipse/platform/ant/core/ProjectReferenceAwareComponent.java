package org.ant4eclipse.platform.ant.core;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ProjectReferenceAwareComponent {

  /**
   * @param referenceTypes
   */
  public void setProjectReferenceTypes(String referenceTypes);

  /**
   * @return
   */
  public String[] getProjectReferenceTypes();

  /**
   * @return
   */
  public boolean isProjectReferenceTypesSet();

  /**
   * 
   */
  public void requireProjectReferenceTypesSet();
}
