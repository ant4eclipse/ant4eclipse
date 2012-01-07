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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * 
 * @param <T>
 *          the type of the vertices
 */
public final class DependencyGraph<T> {

  /** vertices */
  private List<T>           _vertices;

  /** edges */
  private List<Edge<T>>     _edges;

  /** renderer */
  private VertexRenderer<T> _renderer;

  /**
   * <p>
   * Creates a new instance of type DependencyGraph.
   * </p>
   */
  public DependencyGraph() {
    this._vertices = new ArrayList<T>();
    this._edges = new ArrayList<Edge<T>>();
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
  public DependencyGraph( VertexRenderer<T> renderer ) {
    this();
    Assure.notNull( "renderer", renderer );
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
  public void addVertex( T vertex ) {
    Assure.notNull( "vertex", vertex );
    if( !this._vertices.contains( vertex ) ) {
      this._vertices.add( vertex );
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
  public boolean containsVertex( T vertex ) {
    Assure.notNull( "vertex", vertex );
    return this._vertices.contains( vertex );
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
  public void addEdge( T parent, T child ) {
    Assure.notNull( "parent", parent );
    Assure.notNull( "child", child );
    addVertex( parent );
    addVertex( child );
    this._edges.add( new Edge<T>( parent, child ) );
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
    boolean[][] matrix = new boolean[this._vertices.size()][this._vertices.size()];

    // fill the diagonale
    for( boolean[] element : matrix ) {
      Arrays.fill( element, false );
    }

    // set each value to true iff there is a relationship form first to second...
    for( int i = 0; i < this._edges.size(); i++ ) {
      Edge<T> edge = this._edges.get( i );
      int fromidx = this._vertices.indexOf( edge.getParent() );
      int toidx = this._vertices.indexOf( edge.getChild() );

      if( (fromidx == -1) || (toidx == -1) ) {
        // one of the edge's vertices has not been
        // added to this graph (f.e. if it's a dependency
        // on the outside of a specific context)
        continue;
      }

      matrix[fromidx][toidx] = true;
    }

    List<T> list = new ArrayList<T>();

    // iterates across the matrix as long as we didn't found
    // a cycle and there are still edges to be processed
    while( reduce( list, matrix ) ) {
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
  private boolean reduce( List<T> result, boolean[][] matrix ) {
    int zeros = 0;
    int[] count = countEdges( matrix );

    List<T> removable = new ArrayList<T>();

    // add currently independent vertices to the list
    // of removable candidates
    for( int i = 0; i < count.length; i++ ) {
      if( count[i] == 0 ) {
        T vertex = this._vertices.get( i );

        if( !result.contains( vertex ) ) {
          removable.add( vertex );
        }

        zeros++;
      }
    }

    if( removable.isEmpty() ) {
      if( zeros < matrix.length ) {
        // get the first cycle and create an apropriate textual representation
        StringBuffer buffer = new StringBuffer();
        for( int i = 0; i < count.length; i++ ) {
          if( count[i] > 0 ) {
            cycleString( buffer, new HashSet<T>(), i, matrix );
            break;
          }
        }
        throw new Ant4EclipseException( CoreExceptionCode.CYCLIC_DEPENDENCIES_EXCEPTION, buffer.toString() );
      }
    } else {
      // we need to clear the removable vertices, so they won't
      // be processed within the next iteration
      for( int i = 0; i < matrix.length; i++ ) {
        for( int j = 0; j < matrix.length; j++ ) {
          if( matrix[i][j] ) {
            if( removable.contains( this._vertices.get( j ) ) ) {
              matrix[i][j] = false;
            }
          }
        }
      }

      result.addAll( removable );
    }

    return zeros < matrix.length;
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
  private void cycleString( StringBuffer buffer, Set<T> processed, int idx, boolean[][] matrix ) {
    T vertex = this._vertices.get( idx );
    if( this._renderer == null ) {
      buffer.append( String.valueOf( vertex ) );
    } else {
      buffer.append( this._renderer.renderVertex( vertex ) );
    }
    if( processed.contains( vertex ) ) {
      return;
    }
    buffer.append( " -> " );
    processed.add( vertex );
    for( int i = 0; i < matrix.length; i++ ) {
      if( matrix[idx][i] ) {
        cycleString( buffer, processed, i, matrix );
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
  private int[] countEdges( boolean[][] matrix ) {
    int[] result = new int[matrix.length];
    // count the number of edges for each row
    for( int i = 0; i < matrix.length; i++ ) {
      result[i] = sum( matrix[i] );
    }
    return result;
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
  private int sum( boolean[] row ) {
    int result = 0;

    for( boolean element : row ) {
      if( element ) {
        result++;
      }
    }

    return result;
  }

} /* ENDCLASS */
