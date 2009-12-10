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
package org.ant4eclipse.lib.core.dependencygraph;

/**
 * <p>
 * Interface for a vertex renderer. A vertex renderer is used to create a custom string representation of a vertex for
 * further usage in an exception message.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <T>
 *          the type of the vertices
 */
public interface VertexRenderer<T> {

  /**
   * <p>
   * Must return an not-null string that represents the given vertex.
   * </p>
   * 
   * @param vertex
   *          the vertex to render.
   * @return must return an not-null string that represents the given vertex.
   */
  String renderVertex(T vertex);

} /* ENDINTERFACE */
