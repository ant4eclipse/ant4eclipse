package org.ant4eclipse.pde.test.builder;

import java.io.File;

import org.ant4eclipse.jdt.test.builder.JdtProjectBuilder;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.testframework.FileHelper;

/**
 *
 */
public class PdeProjectBuilder extends JdtProjectBuilder {

	/** - */
	private BundleManifest _manifest;

	/**
	 * @param projectName
	 */
	public PdeProjectBuilder(String projectName) {
		super(projectName);

		withDefaultBundleManifest();
		withPdeNature();
	}

	public static PdeProjectBuilder getPreConfiguredPdeProjectBuilder(
			String projectName) {
		return (PdeProjectBuilder) new PdeProjectBuilder(projectName)
				.withDefaultBundleManifest().withJreContainerClasspathEntry()
				.withSrcClasspathEntry("src", false).withOutputClasspathEntry(
						"bin");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ant4eclipse.jdt.test.builder.JdtProjectBuilder#createArtefacts(java
	 * .io.File)
	 */
	@Override
	protected void createArtefacts(File projectDir) {
		super.createArtefacts(projectDir);

		createBundleManifestFile(projectDir);
	}

	protected PdeProjectBuilder withPdeNature() {
		withContainerClasspathEntry("org.eclipse.pde.core.requiredPlugins");
		return (PdeProjectBuilder) withNature(PluginProjectRole.PLUGIN_NATURE);
	}

	protected PdeProjectBuilder withDefaultBundleManifest() {
		_manifest = new BundleManifest(getProjectName());
		return this;
	}

	protected PdeProjectBuilder withBundleManifest(BundleManifest bundleManifest) {
		_manifest = bundleManifest;
		return this;
	}

	protected void createBundleManifestFile(File projectDir) {
		FileHelper.createDirectory(new File(projectDir, "META-INF"));
		File manifestFile = new File(new File(projectDir, "META-INF"),
				"MANIFEST.MF");
		FileHelper.createFile(manifestFile, _manifest.toString());
	}
}
