package org.ant4eclipse.pde.test.builder;

public class BundleManifest {

  private String _bundleName;

  private String _bundleSymbolicName;

  private String _bundleVersion;

  private String _bundleRequiredExecutionEnvironment = "J2SE-1.5";

  private String _importPackage;

  private String _exportPackage;

  /**
   * @param bundleSymbolicName
   */
  public BundleManifest(String bundleSymbolicName) {
    super();
    _bundleSymbolicName = bundleSymbolicName;
    _bundleName = _bundleSymbolicName;
  }

  /**
   * @param bundleName
   *          the bundleName to set
   */
  public BundleManifest withBundleName(String bundleName) {
    _bundleName = bundleName;
    return this;
  }

  /**
   * @param bundleVersion
   *          the bundleVersion to set
   */
  public BundleManifest withBundleVersion(String bundleVersion) {
    _bundleVersion = bundleVersion;
    return this;
  }

  /**
   * @param bundleRequiredExecutionEnvironment
   *          the bundleRequiredExecutionEnvironment to set
   */
  public BundleManifest withBundleRequiredExecutionEnvironment(String bundleRequiredExecutionEnvironment) {
    _bundleRequiredExecutionEnvironment = bundleRequiredExecutionEnvironment;
    return this;
  }

  /**
   * @param importPackage
   *          the importPackage to set
   */
  public BundleManifest withImportPackage(String importPackage) {
    _importPackage = importPackage;
    return this;
  }

  /**
   * @param exportPackage
   *          the exportPackage to set
   */
  public BundleManifest withExportPackage(String exportPackage) {
    _exportPackage = exportPackage;
    return this;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Manifest-Version: 1.0\n");
    buffer.append("Bundle-ManifestVersion: 2\n");

    buffer.append("Bundle-Name: ");
    buffer.append(_bundleName);
    buffer.append("\n");

    buffer.append("Bundle-SymbolicName: ");
    buffer.append(_bundleSymbolicName);
    buffer.append("\n");

    // Bundle-Version: 1.0.0
    // Bundle-Activator: test.Activator
    // Bundle-ActivationPolicy: lazy
    // Bundle-RequiredExecutionEnvironment: J2SE-1.5
    // Import-Package: org.osgi.framework;version="1.3.0"

    return buffer.toString();
  }
}
