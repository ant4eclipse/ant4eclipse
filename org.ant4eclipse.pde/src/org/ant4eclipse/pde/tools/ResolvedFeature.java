package org.ant4eclipse.pde.tools;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Pair;

import org.ant4eclipse.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Includes;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.eclipse.osgi.service.resolver.BundleDescription;

import java.io.File;
import java.util.List;

public class ResolvedFeature {

  private Object                                   _source;

  private FeatureManifest                          _featureManifest;

  private List<Pair<Plugin, BundleDescription>>    _pluginToBundleDescptionList;

  private List<Pair<Includes, FeatureDescription>> _includesToFeatureDescriptionList;

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedFeature}.
   * </p>
   * 
   * @param source
   *          the source of the fragment (an eclipse project, a jar file or a directory)
   * @param featureManifest
   *          the FeatureManifest
   */
  public ResolvedFeature(Object source, FeatureManifest featureManifest) {
    Assert.notNull(source);
    Assert.assertTrue(source instanceof EclipseProject || source instanceof File, String.format(
        "Feature source must be instance of %s or %s.", EclipseProject.class.getName(), File.class.getName()));
    Assert.notNull(featureManifest);

    _featureManifest = featureManifest;
    _source = source;
  }

  /**
   * <p>
   * Returns the source of the feature.
   * </p>
   * 
   * @return the source of the feature.
   */
  public Object getSource() {
    return _source;
  }

  /**
   * <p>
   * Returns <code>true</code> if the source of the feature is an {@link EclipseProject}.
   * </p>
   * 
   * @return <code>true</code> if the source of the feature is an {@link EclipseProject}.
   */
  public boolean isEclipseProject() {
    return _source instanceof EclipseProject;
  }

  /**
   * <p>
   * Returns <code>true</code> if the source of the feature is a directory.
   * </p>
   * 
   * @return <code>true</code> if the source of the feature is a directory.
   */
  public boolean isDirectory() {
    return _source instanceof File && ((File) _source).isDirectory();
  }

  /**
   * <p>
   * Returns <code>true</code> if the source of the feature is a file.
   * </p>
   * 
   * @return <code>true</code> if the source of the feature is a file.
   */
  public boolean isFile() {
    return _source instanceof File && ((File) _source).isFile();
  }

  /**
   * <p>
   * Returns the {@link FeatureManifest}.
   * </p>
   * 
   * @return the featureManifest
   */
  public FeatureManifest getFeatureManifest() {
    return _featureManifest;
  }

  /**
   * <p>
   * Returns the list with the plug-in / bundle description pairs.
   * </p>
   * 
   * @return the pluginToBundleDescptionList
   */
  public List<Pair<Plugin, BundleDescription>> getPluginToBundleDescptionList() {
    return _pluginToBundleDescptionList;
  }

  /**
   * <p>
   * </p>
   * 
   * @param pluginToBundleDescptionList
   *          the pluginToBundleDescptionList to set
   */
  public void setPluginToBundleDescptionList(List<Pair<Plugin, BundleDescription>> pluginToBundleDescptionList) {
    _pluginToBundleDescptionList = pluginToBundleDescptionList;
  }

  /**
   * <p>
   * Returns the list with
   * </p>
   * 
   * @return the includesToFeatureDescriptionList
   */
  public List<Pair<Includes, FeatureDescription>> getIncludesToFeatureDescriptionList() {
    return _includesToFeatureDescriptionList;
  }

  /**
   * <p>
   * </p>
   * 
   * @param includesToFeatureDescriptionList
   *          the includesToFeatureDescriptionList to set
   */
  public void setIncludesToFeatureDescriptionList(
      List<Pair<Includes, FeatureDescription>> includesToFeatureDescriptionList) {
    _includesToFeatureDescriptionList = includesToFeatureDescriptionList;
  }

}
