/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.ant;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.ant4eclipse.jdt.tools.ClasspathResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.platform.ant.AbstractGetProjectPathTask;


/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetEclipseClassPathTask extends AbstractGetProjectPathTask {

  /**
   * Indicates whether the classpath should be resolved as a runtime classpath or not.
   */
  private boolean    _runtime           = false;

  // TODO: MOVE
  private final List containerArguments = new LinkedList();

  // /**
  // * The target platform plugin target (required only for building plugins).
  // */
  // private TargetPlatformDefinition _targetPlatformDefinition;

  // /**
  // * <p>
  // * Returns the target platform against which the workspace plugins will be compiled and tested.
  // * </p>
  // *
  // * @return the target platform against which the workspace plugins will be compiled and tested.
  // */
  // public final File[] getTargetPlatformLocations() {
  // if (this._targetPlatformDefinition != null) {
  // return this._targetPlatformDefinition.getLocations();
  // } else {
  // return new File[] {};
  // }
  // }

  // /**
  // * <p>
  // * Sets the target platform against which the workspace plugins will be compiled and tested.
  // * </p>
  // *
  // * @param targetPlatformLocation
  // * the target platform against which the workspace plugins will be compiled and tested.
  // */
  // public final void setTargetPlatformLocation(final File targetPlatformLocation) {
  // Assert.isDirectory(targetPlatformLocation);
  // final TargetPlatformDefinition platformDefinition = new TargetPlatformDefinition();
  // platformDefinition.addConfiguredLocation(new TargetPlatformDefinition.Location(targetPlatformLocation));
  // this.addTargetPlatform(platformDefinition);
  // }

  // public final void addTargetPlatform(final TargetPlatformDefinition platformDefinition) {
  // Assert.notNull(platformDefinition);
  //
  // this._targetPlatformDefinition = platformDefinition;
  // }
  //
  // /**
  // * <p>
  // * Returns whether the target platform location is set.
  // * </p>
  // *
  // * @return whether the target platform location is set.
  // */
  // public final boolean isTargetPlatformLocationSet() {
  // return this._targetPlatformDefinition != null;
  // }

  public ContainerArgument createContainerArg() {
    final ContainerArgument arg = new ContainerArgument();
    this.containerArguments.add(arg);
    return arg;
  }

  /**
   * @param id
   */
  public void setClasspathId(final String id) {
    super.setPathId(id);
  }

  /**
   * @return Returns the runtime.
   */
  public boolean isRuntime() {
    return this._runtime;
  }

  /**
   * @param runtime
   *          The runtime to set.
   */
  public void setRuntime(final boolean runtime) {
    this._runtime = runtime;
  }

  /**
   * Changes the separator which has to be used to separate directory elements.
   * 
   * @param newseparator
   *          The new separator for directory elements.
   */
  public void setDirSeparator(final String newseparator) {
    getProjectBase().setDirSeparator(newseparator);
  }

  /**
   * {@inheritDoc}
   */
  protected File[] resolvePath() throws Exception {
    // if (isTargetPlatformLocationSet()) {
    // return ClasspathResolver.resolveProjectClasspath(getEclipseProject(), isRelative(), isRuntime());
    // }

    final Properties properties = new Properties();
    for (final Iterator iterator = this.containerArguments.iterator(); iterator.hasNext();) {
      final ContainerArgument argument = (ContainerArgument) iterator.next();
      properties.put(argument.getKey(), argument.getValue());
    }

    final ResolvedClasspath resolvedClasspath = ClasspathResolver.resolveProjectClasspath(getEclipseProject(),
        isRelative(), isRuntime(), properties);

    return resolvedClasspath.getClasspathFiles();
  }

  public class ContainerArgument {
    private String key;

    private String value;

    /**
     * @return the key
     */
    public final String getKey() {
      return this.key;
    }

    /**
     * @param key
     *          the key to set
     */
    public final void setKey(final String key) {
      this.key = key;
    }

    /**
     * @return the value
     */
    public final String getValue() {
      return this.value;
    }

    /**
     * @param value
     *          the value to set
     */
    public final void setValue(final String value) {
      this.value = value;
    }
  }
}
