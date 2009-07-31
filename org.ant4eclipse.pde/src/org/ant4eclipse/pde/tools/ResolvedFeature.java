package org.ant4eclipse.pde.tools;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Pair;

import org.ant4eclipse.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Includes;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;

import org.eclipse.osgi.service.resolver.BundleDescription;

import java.util.List;

public class ResolvedFeature {

  private Object                                   _source;

  private FeatureManifest                          _featureManifest;

  private List<Pair<Plugin, BundleDescription>>    _pluginToBundleDescptionList;

  private List<Pair<Includes, FeatureDescription>> _includesToFeatureDescriptionList;

  /**
   * <p>
   * Creates a new instance of type ResolvedFeature.
   * </p>
   * 
   * @param featureManifest
   */
  public ResolvedFeature(Object source, FeatureManifest featureManifest) {
    Assert.notNull(source);
    Assert.notNull(featureManifest);

    _featureManifest = featureManifest;
    _source = source;
  }
  
  

  public Object getSource() {
    return _source;
  }

  /**
   * <p>
   * </p>
   * 
   * @return the featureManifest
   */
  public FeatureManifest getFeatureManifest() {
    return _featureManifest;
  }

  /**
   * <p>
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
