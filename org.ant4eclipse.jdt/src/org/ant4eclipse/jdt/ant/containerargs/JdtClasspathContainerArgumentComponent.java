package org.ant4eclipse.jdt.ant.containerargs;

import java.util.List;

import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

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
  public List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments();
}
