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
package org.ant4eclipse.core.dependencygraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.dependencygraph.DependencyGraph;
import org.ant4eclipse.lib.core.dependencygraph.Edge;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.junit.Test;

import java.util.List;

import junit.framework.Assert;

/**
 * Test: DependencyGraph
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DependencyGraphTest {

  @Test
  public void dependencyGraph() {
    String o1 = "o1";
    String o11 = "o11";
    String o12 = "o12";
    String o121 = "o121";
    String o2 = "o2";

    DependencyGraph<String> graph = new DependencyGraph<String>();

    graph.addVertex(o1);
    graph.addVertex(o11);
    graph.addVertex(o12);
    graph.addVertex(o121);
    graph.addVertex(o2);

    graph.addEdge(o1, o11);
    graph.addEdge(o1, o12);
    graph.addEdge(o12, o121);
    graph.addEdge(o1, o2);

    List<String> result = graph.calculateOrder();

    assertEquals(5, result.size());

    assertEquals(o11, result.get(0));
    assertEquals(o121, result.get(1));
    assertEquals(o2, result.get(2));
    assertEquals(o12, result.get(3));
    assertEquals(o1, result.get(4));
  }

  /**
   *
   */
  @Test
  public void cyclicDependencyGraph() {
    String o1 = "o1";
    String o2 = "o2";
    String o3 = "o3";

    DependencyGraph<String> graph = new DependencyGraph<String>();

    graph.addVertex(o1);
    graph.addVertex(o2);
    graph.addVertex(o3);

    graph.addEdge(o1, o2);
    graph.addEdge(o2, o3);
    graph.addEdge(o3, o1);

    try {
      graph.calculateOrder();
      Assert.fail();
    } catch (Ant4EclipseException ex) {
      assertEquals(CoreExceptionCode.CYCLIC_DEPENDENCIES_EXCEPTION, ex.getExceptionCode());
    }
  }

  @Test
  public void edge() {
    Object parent = new Object();
    Object child = new Object();

    Edge<Object> edge_1 = new Edge<Object>(parent, child);
    Edge<Object> edge_2 = new Edge<Object>(parent, child);
    assertEquals(edge_1.hashCode(), edge_2.hashCode());
    assertEquals(edge_1, edge_2);
    assertTrue(edge_1.equals(edge_1));
    assertTrue(edge_2.equals(edge_2));
    assertFalse(edge_1.equals(null));
    assertFalse(edge_2.equals(null));

    Edge<Object> edge_3 = new Edge<Object>(parent, new Object());
    Edge<Object> edge_4 = new Edge<Object>(new Object(), child);
    assertFalse(edge_1.equals(edge_3));
    assertFalse(edge_1.equals(edge_4));

    assertFalse(edge_1.equals(new RuntimeException()));
  }

  @Test
  public void forrest() {
    String o1 = "o1";
    String o2 = "o2";
    String o3 = "o3";
    String t1 = "t1";
    String t2 = "t2";

    DependencyGraph<String> graph = new DependencyGraph<String>();

    graph.addVertex(o1);
    graph.addVertex(o2);
    graph.addVertex(o3);
    graph.addVertex(t1);
    graph.addVertex(t2);

    graph.addEdge(o1, o2);
    graph.addEdge(o2, o3);
    graph.addEdge(t1, t2);

    List<String> result = graph.calculateOrder();

    assertEquals("o3", result.get(0));
    assertEquals("t2", result.get(1));
    assertEquals("o2", result.get(2));
    assertEquals("t1", result.get(3));
    assertEquals("o1", result.get(4));

  }

} /* ENDCLASS */