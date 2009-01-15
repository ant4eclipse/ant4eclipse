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
import java.util.StringTokenizer;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.tools.alt.BuildOrderResolver;
import org.ant4eclipse.platform.ant.team.AbstractProjectSetBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
/**
 * @author admin
 * 
 */
public class GetJdtBuildOrderTask extends AbstractProjectSetBasedTask {

	/** Comment for <code>_buildorderProperty</code> */
	private String _buildorderProperty;

	/**
	 * Takes a list of project names - separated by comma - that should be
	 * computed
	 */
	private String _projectNames;

	private boolean _allprojects;

	private File _targetPlatformLocation;

	private NonJavaProjectHandling _nonjavaProjectHandling;

	public NonJavaProjectHandling getNonJavaProjectHandling() {
		if (this._nonjavaProjectHandling == null) {
			this._nonjavaProjectHandling = new NonJavaProjectHandling("fail");
		}
		return this._nonjavaProjectHandling;
	}

	public void setNonJavaProjectHandling(
			final NonJavaProjectHandling nonjavaProjectHandling) {
		this._nonjavaProjectHandling = nonjavaProjectHandling;
	}

	/**
	 * Returns the location of the Target Platform that should be used to
	 * resolve the build order of <b>plugins</b>. May return null.
	 * 
	 * @return The location of the Target Platform.
	 */
	public File getTargetPlatformLocation() {
		return this._targetPlatformLocation;
	}

	/**
	 * Sets the location of the Target Platform that should be used to resolve
	 * the build order of <b>plugins</b>. May be null. If the location is null,
	 * the workspace is used as the only target platform. If not null, it must
	 * point to an existing directory.
	 */
	public void setTargetPlatformLocation(final File targetPlatformLocation) {
		this._targetPlatformLocation = targetPlatformLocation;
	}

	/**
	 * Ensures, that the targetPlatformLocation parameter has been set
	 * correctly, i.e. either be null or point to a directory.
	 * 
	 * @throws BuildException
	 *             if the parameter is set incorrectly (i.e. not null and not a
	 *             directory)
	 * 
	 */
	public void ensureCorrectTargetPlatformLocation() {
		if ((this._targetPlatformLocation != null)
				&& !this._targetPlatformLocation.isDirectory()) {
			throw new BuildException(
					"If 'targetPlatformLocation' is set, it must point to an existing directory.");
		}
	}

	/**
	 * Changes a flag which indicates whether all projects within a workspace
	 * shall be recognized or not.
	 * 
	 * @param allprojects
	 *            true <=> Take all projects within the workspace into account.
	 */
	public void setAllProjects(final boolean allprojects) {
		this._allprojects = allprojects;
	}

	/**
	 * @param buildorderProperty
	 */
	public final void setBuildorderProperty(final String buildorderProperty) {
		this._buildorderProperty = buildorderProperty;
	}

	/**
	 * Returns the name of the build order property.
	 * 
	 * @return The name of the build order property.
	 */
	public final String getBuildorderProperty() {
		return this._buildorderProperty;
	}

	/**
	 * Returns true if the build order property has been set.
	 * 
	 * @return true <=> The build order property has been set.
	 */
	public final boolean isBuildorderPropertySet() {
		return ((this._buildorderProperty != null) && (this._buildorderProperty
				.length() > 0));
	}

	public String getProjectNames() {
		return this._projectNames;
	}

	public void setProjectNames(final String projectNames) {
		this._projectNames = projectNames;
	}

	public boolean isProjectNamesSet() {
		return ((this._projectNames != null) && (this._projectNames.length() > 0));
	}

	/**
   *
   */
	public final void requireBuildorderPropertySet() {
		if (!isBuildorderPropertySet()) {
			throw new BuildException("buildorderProperty has to be set!");
		}
	}

	protected void requireProjectSetOrProjectNamesSet() {
		if (!this._allprojects) {
			if (!isProjectSetSet() && !isProjectNamesSet()) {
				throw new BuildException(
						"Missing parameter: neither 'projectSet' nor 'projectNames' has been set.");
			}
			if (isProjectSetSet() && isProjectNamesSet()) {
				throw new BuildException(
						"Duplicate parameter: either 'projectSet' or 'projectNames' must be set.");
			}
		}
	}

	@Override
	protected void doExecute() {
		requireWorkspaceSet();
		requireProjectSetOrProjectNamesSet();
		requireBuildorderPropertySet();
		ensureCorrectTargetPlatformLocation();

		try {

			EclipseProject[] orderedProjects = null;
			String[] projectNames = null;
			if (isProjectNamesSet()) {
				projectNames = split(getProjectNames(), ',');
			} else if (isProjectSetSet()) {
				projectNames = getProjectSet().getProjectNames();
			} else {
				final EclipseProject[] projects = getWorkspace()
						.getAllProjects();
				projectNames = new String[projects.length];
				for (int i = 0; i < projects.length; i++) {
					projectNames[i] = projects[i].getSpecifiedName();
				}
			}

			orderedProjects = BuildOrderResolver.resolveBuildOrder(
					getWorkspace(), getTargetPlatformLocation(), projectNames,
					getNonJavaProjectHandling().asBuildOrderResolverConstant());

			getProjectSetBase().setStringProperty(this._buildorderProperty,
					convertToString(orderedProjects, ','));

		} catch (final BuildException ex) {
			throw ex;
		} catch (final Exception e) {
			A4ELogging.debug(e.getMessage());
			throw new BuildException(e.getMessage(), e);
		}
	}

	/**
	 * Splits the supplied string using a specific separator.
	 * 
	 * @param value
	 *            The value that shall be splitted.
	 * @param separator
	 *            The separation character. Sequences of separators will be
	 *            ignored.
	 * 
	 * @return A list of splitted strings. The elements will be trimmed. Not
	 *         null.
	 */
	private String[] split(final String value, final char separator) {
		Assert.notNull(value);
		final StringTokenizer tokenizer = new StringTokenizer(value, ""
				+ separator);
		final String[] result = new String[tokenizer.countTokens()];
		for (int i = 0; i < result.length; i++) {
			result[i] = tokenizer.nextToken().trim();
		}
		return (result);
	}

	/**
	 * Joins the names of projects using a specific separator character.
	 * 
	 * @param projects
	 *            The list of projects that will be joined.
	 * @param separator
	 *            The separator character.
	 * 
	 * @return A String which contains the list of names.
	 */
	private String convertToString(final EclipseProject[] projects,
			final char separator) {
		Assert.notNull(projects);
		final StringBuffer buffer = new StringBuffer();
		if (projects.length > 0) {
			buffer.append(projects[0].getFolderName());
			for (int i = 1; i < projects.length; i++) {
				buffer.append(separator);
				buffer.append(projects[i].getFolderName());
			}
		}
		return buffer.toString();
	}

	public static class NonJavaProjectHandling extends EnumeratedAttribute {

		public NonJavaProjectHandling() {
			// needed by Ant to instantiate
		}

		public NonJavaProjectHandling(final String value) {
			super();
			setValue(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String[] getValues() {
			return new String[] { "fail", "ignore", "prepend", "append" };
		}

		public int asBuildOrderResolverConstant() {
			return getIndex() + 1;
		}

	} /* ENDCLASS */

} /* ENDCLASS */