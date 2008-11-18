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
package org.ant4eclipse.core.dependencygraph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.exception.Ant4EclipseException;


/**
 * <p>
 * A {@link DependencyGraph} can be used to serialize tree structures. The tree will be transformed in a linear
 * structure (List).
 * </p>
 * 
 * <p>
 * A referenced node will always appear <b>prior to</b> the referencing node. Example:
 * 
 * <pre>
 *                                 A
 *                                 |
 *                           -------------
 *                           |     |     |
 *                           B     C     D
 *                                 |
 *                                 E
 * </pre>
 * 
 * will be transformed to E,B,D,C,A or B,E,D,C,A.
 * </p>
 * 
 * <p>
 * The order of the nodes depends on the order of the tree definition via {@link DependencyGraph#addVertex(Object)} and
 * {@link DependencyGraph#addEdge(Object, Object)}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public final class DependencyGraph<T> {

  /** vertices */
  private final List<T>       _vertices;

  /** edges */
  private final List<Edge<T>> _edges;

  /** renderer */
  private VertexRenderer<T>   _renderer;

  /**
   * <p>
   * Creates a new instance of type DependencyGraph.
   * </p>
   */
  public DependencyGraph() {
    this._vertices = new LinkedList<T>();
    this._edges = new LinkedList<Edge<T>>();
  }

  /**
   * <p>
   * Creates a new instance of type {@link DependencyGraph}.
   * </p>
   * 
   * @param renderer
   *          the provided renderer is used to create a custom string representation of a vertex for further usage in an
   *          exception message.
   */
  public DependencyGraph(final VertexRenderer<T> renderer) {
    this();
    Assert.notNull(renderer);
    this._renderer = renderer;
  }

  /**
   * <p>
   * Adds a vertex to the {@link DependencyGraph}.
   * </p>
   * 
   * @param vertex
   *          the vertex that will be added.
   */
  public void addVertex(final T vertex) {
    Assert.notNull(vertex);

    if (!this._vertices.contains(vertex)) {
      this._vertices.add(vertex);
    }
  }

  /**
   * <p>
   * Returns <code>true</code>, if the given vertex has already been added to the {@link DependencyGraph}.
   * </p>
   * 
   * @param vertex
   *          the vertex
   * @return <code>true</code>, if the given vertex has already been added to the {@link DependencyGraph}, otherwise
   *         <code>false</code>.
   */
  public boolean containsVertex(final T vertex) {
    Assert.notNull(vertex);

    return this._vertices.contains(vertex);
  }

  /**
   * <p>
   * Adds an edge to the {@link DependencyGraph}.
   * </p>
   * 
   * @param parent
   *          the parent node
   * @param child
   *          the child node
   */
  public void addEdge(final T parent, final T child) {
    Assert.notNull(parent);
    Assert.notNull(child);

    addVertex(parent);
    addVertex(child);
    this._edges.add(new Edge<T>(parent, child));
  }

  /**
   * <p>
   * Computers the order of all the nodes.
   * </p>
   * 
   * @return the ordered list of all the nodes..
   */
  public List<T> calculateOrder() {

    // setup a matrix that contains a true value iff there is a the first index donates a parent of the second value
    final boolean[][] matrix = new boolean[this._vertices.size()][this._vertices.size()];

    // fill the diagonale
    for (int i = 0; i < matrix.length; i++) {
      Arrays.fill(matrix[i], false);
    }

    // set each value to true iff there is a relationship form first to second...
    for (int i = 0; i < this._edges.size(); i++) {
      final Edge<T> edge = this._edges.get(i);
      final int fromidx = this._vertices.indexOf(edge.parent);
      final int toidx = this._vertices.indexOf(edge.child);

      if ((fromidx == -1) || (toidx == -1)) {
        // one of the edge's vertices has not been
        // added to this graph (f.e. if it's a dependency
        // on the outside of a specific context)
        continue;
      }

      matrix[fromidx][toidx] = true;
    }

    final List<T> list = new LinkedList<T>();

    // iterates across the matrix as long as we didn't found
    // a cycle and there are still edges to be processed
    while (reduce(list, matrix)) {
      // nothing to do...
    }

    return list;
  }

  /**
   * <p>
   * Reduces the internal matrix. Reduction means that the supplied matrix will be modified while vertices that doesn't
   * refer to any other one can be added to the list since they no longer depend on another vertex. Reduction is an
   * iterative process, so it's necessary to run this function until the matrix doesn't contain an edge.
   * </p>
   * 
   * @param result
   *          The list that will receive the vertices.
   * @param matrix
   *          The current state of the matrix representing the graph.
   * 
   * @return true <=> There are still edges within the matrix so a succeeding iteration is required.
   */
  private boolean reduce(final List<T> result, final boolean[][] matrix) {
    int zeros = 0;
    final int[] count = countEdges(matrix);

    final List<T> removable = new LinkedList<T>();

    // add currently independent vertices to the list
    // of removable candidates
    for (int i = 0; i < count.length; i++) {
      if (count[i] == 0) {
        final T vertex = this._vertices.get(i);

        if (!result.contains(vertex)) {
          removable.add(vertex);
        }

        zeros++;
      }
    }

    if (removable.isEmpty()) {
      if (zeros < matrix.length) {
        // get the first cycle and create an apropriate textual representation
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < count.length; i++) {
          if (count[i] > 0) {
            cycleString(buffer, new HashSet<T>(), i, matrix);
            break;
          }
        }
        throw new Ant4EclipseException(CoreExceptionCode.CYCLIC_DEPENDENCIES_EXCEPTION, new Object[] { buffer
            .toString() });
      }
    } else {
      // we need to clear the removable vertices, so they won't
      // be processed within the next iteration
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix.length; j++) {
          if (matrix[i][j]) {
            if (removable.contains(this._vertices.get(j))) {
              matrix[i][j] = false;
            }
          }
        }
      }

      result.addAll(removable);
    }

    return (zeros < matrix.length);
  }

  /**
   * <p>
   * Creates a String which is used to create a textual representation of a cycle.
   * </p>
   * 
   * @param buffer
   *          The buffer used to collect the data.
   * @param processed
   *          A list of added vertices which is required for the abortion criteria.
   * @param idx
   *          The index of the vertex which is part of the cycle.
   * @param matrix
   *          The current state of the matrix.
   */
  private void cycleString(final StringBuffer buffer, final Set<T> processed, final int idx, final boolean[][] matrix) {
    final T vertex = this._vertices.get(idx);
    if (this._renderer == null) {
      buffer.append(String.valueOf(vertex));
    } else {
      buffer.append(this._renderer.renderVertex(vertex));
    }
    if (processed.contains(vertex)) {
      return;
    }
    buffer.append(" -> ");
    processed.add(vertex);
    for (int i = 0; i < matrix.length; i++) {
      if (matrix[idx][i]) {
        cycleString(buffer, processed, i, matrix);
        break;
      }
    }
  }

  /**
   * <p>
   * Returns a list with the counted number of edges.
   * </p>
   * 
   * @param matrix
   *          The matrix which provides graph representation.
   * 
   * @return A list with the length of the matrix where each element holds the number of edges of the related row.
   */
  private int[] countEdges(final boolean[][] matrix) {
    final int[] result = new int[matrix.length];
    // count the number of edges for each row
    for (int i = 0; i < matrix.length; i++) {
      result[i] = sum(matrix[i]);
    }
    return (result);
  }

  /**
   * <p>
   * Returns the number of 'true'-values in the given array.
   * </p>
   * 
   * @param row
   *          the array of boolean
   * @return the number of 'true'-values in the given array.
   */
  private int sum(final boolean[] row) {
    int result = 0;

    for (int i = 0; i < row.length; i++) {
      if (row[i]) {
        result++;
      }
    }

    return (result);
  }

  /**
   * <p>
   * Interface for a vertex renderer. A vertex renderer is used to create a custom string representation of a vertex for
   * further usage in an exception message.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
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
    public String renderVertex(T vertex);
  }

  /**
   * <p>
   * Internal representation of an edge.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Edge<T> {
    /** parent */
    private final T parent;

    /** child */
    private final T child;

    /**
     * <p>
     * Creates a new instance of type Edge.
     * </p>
     * 
     * @param aParent
     *          the parent object
     * @param aChild
     *          the child object
     */
    public Edge(final T aParent, final T aChild) {
      Assert.notNull(aParent);
      Assert.notNull(aChild);

      this.parent = aParent;
      this.child = aChild;
    }

    /**
     * <p>
     * Returns the child object.
     * </p>
     * 
     * @return the child object.
     */
    public T getChild() {
      return this.child;
    }

    /**
     * <p>
     * Returns the parent object.
     * </p>
     * 
     * @return the parent object.
     */
    public T getParent() {
      return this.parent;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
      final int PRIME = 31;
      int result = 1;
      result = PRIME * result + this.parent.hashCode();
      result = PRIME * result + this.child.hashCode();
      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      @SuppressWarnings("unchecked")
      final Edge<T> other = (Edge<T>) obj;
      if (!this.parent.equals(other.parent)) {
        return false;
      }
      if (!this.child.equals(other.child)) {
        return false;
      }
      return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      final StringBuffer result = new StringBuffer();
      result.append("[Edge");
      result.append(" parent:");
      result.append(this.parent);
      result.append(" child:");
      result.append(this.child);
      result.append("]");
      return result.toString();
    }
  }

} /* ENDCLASS */
