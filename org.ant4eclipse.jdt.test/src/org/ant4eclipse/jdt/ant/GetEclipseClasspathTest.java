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

import org.ant4eclipse.jdt.test.builder.JdtProjectBuilder;
import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;

public class GetEclipseClasspathTest extends AbstractWorkspaceBasedBuildFileTest {

	public final static String TEST_PATH_SEPARATOR = "#";

	private File _simpleProjectBinDir;

	private File _projectBBinDir;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		File simpleProjectDir = JdtProjectBuilder
				.getPreConfiguredJdtBuilder("simpleproject").createIn(
						getTestWorkspaceDirectory());
		_simpleProjectBinDir = new File(simpleProjectDir, "bin");

		// projectB depends on simpleProject but doesn't re-export it
		File projectbDir = JdtProjectBuilder.getPreConfiguredJdtBuilder(
				"projectb").withSrcClasspathEntry("/simpleproject", false)
				.createIn(getTestWorkspaceDirectory());
		_projectBBinDir = new File(projectbDir, "bin");

		setupBuildFile("getEclipseClasspath.xml");
	}

	public void testSimple() throws Exception {
		String classpath = executeTestTarget("simpleproject", false, false);
		assertClasspath(classpath, _simpleProjectBinDir);
	}

	public void testSimple_Relative() throws Exception {
		String classpath = executeTestTarget("simpleproject", false, true);
		assertClasspath(classpath, new File("simpleproject/bin"));
	}

	public void testSimple_TwoClassFolders() throws Exception {
		File projectcDir = JdtProjectBuilder.getPreConfiguredJdtBuilder(
				"projectc").withSrcClasspathEntry("gen-src", "gen-classes",
				false).createIn(getTestWorkspaceDirectory());
		String classpath = executeTestTarget("projectc", false, false);
		assertClasspath(classpath, new File(projectcDir, "bin"), new File(
				projectcDir, "gen-classes"));

	}

	public void test_WithProjectReferences() throws Exception {
		String classpath = executeTestTarget("projectb", false, false);
		assertClasspath(classpath, _projectBBinDir, _simpleProjectBinDir);
	}

	public void test_WithMultipleReferences() throws Exception {

		// projectC references b. Since b doesn't reexport simple project,
		// simpleproject will be invisible
		File projectcDir = JdtProjectBuilder.getPreConfiguredJdtBuilder(
				"projectc").withSrcClasspathEntry("/projectb", false).createIn(
				getTestWorkspaceDirectory());

		String classpath = executeTestTarget("projectc", false, false);
		File projectCBinDir = new File(projectcDir, "bin");
		assertClasspath(classpath, projectCBinDir, _projectBBinDir);
	}

	public void test_WithMultipleReferencesRuntime() throws Exception {

		// projectC references b. Since b doesn't reexport simple project,
		// anyway: simpleproject will be visible
		// since we ask for runtime classpath
		File projectcDir = JdtProjectBuilder.getPreConfiguredJdtBuilder(
				"projectc").withSrcClasspathEntry("/projectb", false).createIn(
				getTestWorkspaceDirectory());

		String classpath = executeTestTarget("projectc", true, false);
		File projectCBinDir = new File(projectcDir, "bin");
		assertClasspath(classpath, projectCBinDir, _projectBBinDir,
				_simpleProjectBinDir);
	}

	/**
	 * Makes sure that the given classpath contains the expected entries
	 * 
	 * @param classpath
	 * @param expectedEntries
	 */
	protected void assertClasspath(String classpath, File... expectedEntries) {
		assertNotNull(classpath);
		String[] classpathItems = classpath.split(TEST_PATH_SEPARATOR);
		assertEquals(expectedEntries.length, classpathItems.length);

		for (int i = 0; i < expectedEntries.length; i++) {
			File expectedDir = expectedEntries[i];
			File classpathItem = new File(classpathItems[i]);
			assertEquals(
					String
							.format(
									"Classpath-Item '%d' does not match. Expected: '%s' Actual: '%s'",
									i, expectedDir, classpathItem),
					expectedDir, classpathItem);
		}
	}

	protected String executeTestTarget(String projectName,
			boolean runtimeClasspath, boolean relative) throws Exception {
		getProject().setProperty("projectName", projectName);
		getProject().setProperty("runtimeClasspath",
				Boolean.toString(runtimeClasspath));
		getProject().setProperty("pathSeparator", TEST_PATH_SEPARATOR);
		getProject().setProperty("relative", Boolean.toString(relative));

		executeTarget("getEclipseClasspath");

		String classpath = getProject().getProperty("classpath");
		System.out.println(classpath);
		assertNotNull(classpath);
		return classpath;
	}
}
