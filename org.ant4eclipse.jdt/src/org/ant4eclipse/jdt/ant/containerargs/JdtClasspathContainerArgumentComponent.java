package org.ant4eclipse.jdt.ant.containerargs;

import java.util.Map;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface JdtClasspathContainerArgumentComponent {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public JdtClasspathContainerArgument createJdtClasspathContainerArgument();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Map<String, Object> getJdtClasspathContainerArguments();
}
