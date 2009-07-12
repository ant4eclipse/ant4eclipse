package org.ant4eclipse.jdt.ant.containerargs;

import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import java.util.List;

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
  JdtClasspathContainerArgument createJdtClasspathContainerArgument();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments();
}
