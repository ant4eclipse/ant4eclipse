/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FeatureDescription {

  /** - */
  private Object          _source;

  /** - */
  private FeatureManifest _featureManifest;

  /**
   * <p>
   * Creates a new instance of type FeatureDescription.
   * </p>
   * 
   * @param source
   *          the source of this feature (e.g. an eclipse feature project, a jar file or a directory)
   * @param featureManifest
   *          the {@link FeatureManifest}
   */
  public FeatureDescription(Object source, FeatureManifest featureManifest) {
    super();

    this._source = source;
    this._featureManifest = featureManifest;
  }

  /**
   * <p>
   * The source of this feature (e.g. an eclipse feature project, a jar file or a directory).
   * </p>
   * 
   * @return the source of this feature (e.g. an eclipse feature project, a jar file or a directory)
   */
  public Object getSource() {
    return this._source;
  }

  public boolean isFeatureProject() {
    return this._source instanceof EclipseProject;
  }

  public boolean isJarFile() {
    return this._source instanceof File && ((File) this._source).isFile();
  }

  public boolean isDirectory() {
    return this._source instanceof File && ((File) this._source).isDirectory();
  }

  /**
   * <p>
   * </p>
   * 
   * @return the featureManifest
   */
  public FeatureManifest getFeatureManifest() {
    return this._featureManifest;
  }
}
