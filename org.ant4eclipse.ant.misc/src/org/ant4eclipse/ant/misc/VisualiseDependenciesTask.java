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
package org.ant4eclipse.ant.misc;

import org.ant4eclipse.ant.platform.core.SubElementAndAttributesComponent;
import org.ant4eclipse.ant.platform.core.delegate.SubElementAndAttributesDelegate;
import org.ant4eclipse.ant.platform.core.task.AbstractProjectBasedTask;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.TextBuffer;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolverService;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This task is used to create a visualisation of dependencies.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class VisualiseDependenciesTask extends AbstractProjectBasedTask implements SubElementAndAttributesComponent {

  /**
   * The reference type that is used for the resolving process of projects. A value of <code>null</code> means that all
   * reference types are being tried.
   */
  private String[]                        _referencetypes;

  /** The location of the destination file. */
  private File                            _destfile;

  /** */
  private SubElementAndAttributesDelegate _subElementAndAttributesDelegate;

  public VisualiseDependenciesTask() {
    this._subElementAndAttributesDelegate = new SubElementAndAttributesDelegate(this);
    this._referencetypes = null;
  }

  /**
   * Changes the reference type used to influence the project resolving process.
   * 
   * @param newreferencetype
   *          A reference type which depends on the current configuration of a4e.
   */
  public void setReferencetypes(String newreferencetypes) {
    String[] elements = null;
    if (newreferencetypes != null) {
      elements = newreferencetypes.split(",");
    }
    this._referencetypes = Utilities.cleanup(elements);
  }

  /**
   * Changes the location of the destination file to be stored.
   * 
   * @param newfile
   *          The new location of the destination file to be stored.
   */
  public void setDestination(File newfile) {
    this._destfile = newfile;
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(String name) throws BuildException {
    return this._subElementAndAttributesDelegate.createDynamicElement(name);
  }

  /**
   * {@inheritDoc}
   */
  public List<Object> getSubElements() {
    return this._subElementAndAttributesDelegate.getSubElements();
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, String> getSubAttributes() {
    return this._subElementAndAttributesDelegate.getSubAttributes();
  }

  /**
   * {@inheritDoc}
   */
  public void setDynamicAttribute(String name, String value) throws BuildException {
    this._subElementAndAttributesDelegate.setDynamicAttribute(name, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    requireWorkspaceAndProjectNameSet();
    if (this._referencetypes != null) {
      // check if we can use the provided reference type
      String[] allowed = getResolver().getReferenceTypes();
      for (String reftype : this._referencetypes) {
        if (!Utilities.contains(reftype, allowed)) {
          throw new BuildException("The 'referencetypes' value '" + reftype + "' is not supported.");
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    String[] types = getResolver().getReferenceTypes();
    for (String type : types) {
      System.err.println("type: " + type);
    }
    if (this._referencetypes != null) {
      // there's a restriction provided by the user
      types = this._referencetypes;
    }

    EclipseProject project = getEclipseProject();
    List<EclipseProject> referenced = new ArrayList<EclipseProject>();

    referenced.add(project);

    // load the directly referenced projects
    referenced.addAll(getResolver().resolveReferencedProjects(project, types, getSubElements()));

    TextBuffer text = new TextBuffer(null, null);
    DependencyEmitter emitter = new DotEmitter(text);
    emitter.begin();

    Map<String, String> namemappings = new Hashtable<String, String>();
    for (int i = 0; i < referenced.size(); i++) {
      String name = referenced.get(i).getSpecifiedName();
      namemappings.put(name, name.replace(' ', '_').replace('.', '_'));
      emitter.node(namemappings.get(name), name);
    }

    String current = namemappings.get(project.getSpecifiedName());
    for (int i = 1; i < referenced.size(); i++) {
      emitter.edge(current, namemappings.get(referenced.get(i).getSpecifiedName()));
    }

    emitter.end();
    Utilities.writeFile(this._destfile, text.toString(), "UTF-8");

  }

  /**
   * Returns the currently registered resolver service.
   * 
   * @return The currently registered resolver service. Not <code>null</code>.
   */
  private ReferencedProjectsResolverService getResolver() {
    /**
     * @todo [09-Jul-2009:KASI] The inner convenience classes located in service interfaces should be removed. I'm just
     *       using this shortcut here in order to support refactoring in future.
     */
    return ServiceRegistryAccess.instance().getService(ReferencedProjectsResolverService.class);
  }

  /*
   * graph graphname { // The label attribute can be used to change the label of a node a [label="Foo"]; // Here, the
   * node shape is changed. b [shape=box]; // These edges both have different line properties a -- b -- c [color=blue];
   * b -- d [style=dotted];
   */

  private static interface DependencyEmitter {

    void node(String name, String label);

    void edge(String from, String to);

    void begin();

    void end();

  } /* ENDINTERFACE */

  private static class DotEmitter implements DependencyEmitter {

    private TextBuffer buffer;

    public DotEmitter(TextBuffer storage) {
      this.buffer = storage;
    }

    public void begin() {
      this.buffer.writeLine("digraph example {");
      this.buffer.indent();
    }

    public void edge(String from, String to) {
      this.buffer.writeLineF("%s -> %s;", from, to);
    }

    public void end() {
      this.buffer.dedent();
      this.buffer.writeLine("}");
    }

    public void node(String name, String label) {
      this.buffer.writeLineF("%s [label=\"%s\", shape=box];", name, label);
    }

  } /* ENDCLASS */

} /* ENDCLASS */
