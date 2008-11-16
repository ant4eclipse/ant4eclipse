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
package net.sf.ant4eclipse.ant;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.util.Utilities;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TaskHelper {

  // public final static String WTP_WEBCONTAINER = "org.eclipse.jst.j2ee.internal.web.container";

  /**
   * Creates a "Path" containing all given directories
   * 
   * @param project
   * @param files
   * @return The populated Path instance
   */
  public static final Path createPathFromFile(final Project project, final File[] directories) {
    Assert.notNull(project);
    Assert.notNull(directories);

    final Path path = new Path(project);
    for (int i = 0; i < directories.length; i++) {
      final Path p = new Path(project, directories[i].getAbsolutePath());
      path.add(p);
    }

    return path;
  }

  /**
   * Converts an array of ResolvedClasspathEntry-objects to a String. The entries are separated by the separator char.
   * 
   * This method can be used to convert a complete eclipse classpath to a String, which could be used in an ant
   * property.
   * 
   * @param entries
   *          The resolved pathes that shall be converted.
   * @param pathseparator
   *          The separator which shall be used for each path element.
   * @param dirseparator
   *          The separator which shall be used for each directory element.
   * @param project
   *          The associated project.
   * 
   * @return A String containing the pathes.
   */
  public static final String convertToString(final File[] entries, final String pathseparator,
      final String dirseparator, final Project project) {
    Assert.notNull(entries);
    Assert.notNull(pathseparator);

    final List entriesAsString = new LinkedList();
    for (int i = 0; i < entries.length; i++) {
      final File entry = entries[i];
      // if (entry.isResolved()) {
      final String path = entry.getPath();
      if (!entriesAsString.contains(path)) {
        entriesAsString.add(path);
      }
      // } else if (entry.isContainer() || entry.isVariable()) {
      // final Path p = getPathForReference(entry, project);
      // if (p != null) {
      // final String[] pathes = p.list();
      // // TODO: Handling of absolute/relative Pathes
      // for (int j = 0; j < pathes.length; j++) {
      // if (!entriesAsString.contains(pathes[j])) {
      // entriesAsString.add(pathes[j]);
      // }
      // }
      // }
      // }
    }

    final StringBuffer buffer = new StringBuffer();
    final Iterator iterator = entriesAsString.iterator();
    while (iterator.hasNext()) {
      String path = iterator.next().toString().replace('\\', '/');
      path = Utilities.replace(path, '/', dirseparator);
      buffer.append(path);
      if (iterator.hasNext()) {
        buffer.append(pathseparator);
      }
    }

    return buffer.toString();
  }

  /**
   * Converts the supplied resolved pathes to a Path instance.
   * 
   * @param entries
   *          The resolved pathes that shall be converted.
   * @param project
   *          The associated project.
   * 
   * @return The Path instance containing the supplied pathes.
   */
  public static final Path convertToPath(final File[] entries, final Project project) {
    Assert.notNull(entries);
    Assert.notNull(project);
    final Path antPath = new Path(project);
    for (int i = 0; i < entries.length; i++) {
      final File entry = entries[i];
      // if (entry.isResolved()) {
      antPath.append(new Path(project, entry.getPath()));
      // } else if (entry.isContainer() || entry.isVariable()) {
      // final Path containerPath = getPathForReference(entry, project);
      // if (containerPath != null) {
      // antPath.add(containerPath);
      // }
      // }
    }
    return antPath;
  }

  /**
   * Returns an Ant-Path for the given reference.
   * 
   * The reference can either be a property which will be converted to a path or it can be a Path object itself
   * 
   * @param entry
   *          the reference id
   * @param project
   *          the current project
   * 
   * @return the Path or null if no path could be found with this id TODO: provide prefixes (SCR 1236934) TODO:
   *         Treatment of non existing references (SCR 1236930)
   */
  // private static final Path getPathForReference(final ResolvedClasspathEntry entry, final Project project) {
  // Assert.notNull(entry);
  // Assert.notNull(project);
  //
  // final String reference = entry.getPath();
  //
  // // ------- first check, if a Property or Peference is defined for reference
  //
  // // check if reference is an Ant property name
  // final Hashtable t = project.getProperties();
  // if (t.containsKey(reference)) {
  // final String pathString = (String) t.get(reference);
  // A4ELogging.debug("found property '%s' for path-reference '%s'", new Object[] { pathString, reference });
  // return (new Path(project, pathString));
  // }
  //
  // // check if this is a variable
  // if (entry.isVariable()) {
  // final String resolvedvar = resolveVariable(reference, project);
  // if (resolvedvar != null) {
  // return (new Path(project, resolvedvar));
  // }
  // }
  //
  // // check if reference points to a Path object
  // final Object o = project.getReference(reference);
  // if (o != null) {
  // if (o instanceof Path) {
  // A4ELogging.debug("Using Path '%s' for path-reference '%s'", new Object[] { o, reference });
  // return (Path) o;
  // }
  // A4ELogging.error("Invalid reference for Container-Variable. Reference with id '%s'"
  // + " must point to a Path object but is a '%s'", new Object[] { reference, o.getClass().getName() });
  // return null;
  // }
  //
  // // ------ second try to get default values for some containers
  // if (reference.startsWith(WTP_WEBCONTAINER + "/")) {
  // final Path path = getPathForWebContainer(entry, project);
  // if (path != null) {
  // return path;
  // }
  // }
  //
  // if (!ContainerTypes.JRE_CONTAINER.equals(reference)) {
  // A4ELogging.warn("No path found for Container-Variable '%s'", reference);
  // }
  // return null;
  // }
  /**
   * Resolves an Eclipse variable according to the supplied settings.
   * 
   * @param reference
   *          The Eclipse variable which can contain an extension.
   * @param project
   *          The currently used ANT project.
   * 
   * @return The resolved path or null in case this wasn't possible.
   */
  // private static final String resolveVariable(final String reference, final Project project) {
  // // check if we need to handle an extension
  // final int idxBck = reference.indexOf('\\');
  // final int idxFwd = reference.indexOf('/');
  // int idx = -1;
  // if ((idxBck == -1) || (idxFwd == -1)) {
  // idx = Math.max(idxBck, idxFwd);
  // } else {
  // idx = Math.min(idxBck, idxFwd);
  // }
  // String varname = reference;
  // String extension = null;
  // if (idx >= 0) {
  // varname = reference.substring(0, idx);
  // extension = reference.substring(idx);
  // }
  // // try to resolve the variable
  // String path = null;
  // final String key = "${" + varname + "}";
  // final String resolvedvar = getEclipseVariableResolver().resolveEclipseVariables(key, null, null);
  // if (!key.equals(resolvedvar)) {
  // path = resolvedvar;
  // } else {
  // // deprecated method: use an ANT path
  // final Object o = project.getReference(varname);
  // if (o instanceof Path) {
  // A4ELogging.warn("resolving eclipse variable '%s' using an ANT path [deprecated]", new Object[] { varname });
  // final Path antpath = (Path) o;
  // final String[] entries = antpath.list();
  // if (entries.length == 1) {
  // path = entries[0];
  // }
  // }
  // }
  // // extend the variable location if necessary
  // if ((path != null) && (extension != null)) {
  // path = path + extension;
  // }
  // return (path);
  // }
  // private static final Path getPathForWebContainer(final ResolvedClasspathEntry entry, final Project project) {
  //
  // final String containerPath = entry.getPath();
  //
  // // check if a module is specified after /
  // final int containerIdLength = (WTP_WEBCONTAINER + "/").length();
  // if (containerPath.length() <= containerIdLength) {
  // // no webModule specified: don't resolve
  // return null;
  // }
  //
  // final String webModuleName = containerPath.substring(containerIdLength);
  // // include everything webModuleName/WebContent/WEB-INF/
  // final String webInfLibDirName = webModuleName + File.separator + "WebContent" + File.separator + "WEB-INF"
  // + File.separator + "lib";
  // final File webInfLibDir = entry.getDeclaringProject().getChild(webInfLibDirName);
  // if (!webInfLibDir.exists()) {
  // A4ELogging.warn("Could not resolve WTP-Lib folder since it does not exists: %s", webInfLibDir.getAbsolutePath());
  // return null;
  // }
  // final Path path = new Path(project); // ,
  //
  // final FileSet fs = (FileSet) project.createDataType("fileset");
  // fs.setDir(webInfLibDir);
  //
  // fs.setIncludes("*");
  // path.addFileset(fs);
  // A4ELogging.debug("Using default path '%s' for container '%s'", new Object[] { path, containerPath });
  // return path;
  // }
  // private static EclipseVariableResolver getEclipseVariableResolver() {
  // final EclipseVariableResolver resolver = (EclipseVariableResolver) ServiceRegistry.instance().getService(
  // EclipseVariableResolver.class.getName());
  // return resolver;
  // }
}
