package org.ant4eclipse.core.dependencygraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;


import org.ant4eclipse.core.dependencygraph.DependencyGraph;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.junit.Test;

/**
 * Testet den DependencyGraph.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DependencyGraphTest {
  /**
   * @throws CyclicDependencyException
   */
  @Test
  public void testDependencyGraph() {
    String o1 = new String("o1");
    String o11 = new String("o11");
    String o12 = new String("o12");
    String o121 = new String("o121");
    String o2 = new String("o2");

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
  public void testCyclicDependencyGraph() {
    String o1 = new String("o1");
    String o2 = new String("o2");
    String o3 = new String("o3");

    DependencyGraph<String> graph = new DependencyGraph<String>();

    graph.addVertex(o1);
    graph.addVertex(o2);
    graph.addVertex(o3);

    graph.addEdge(o1, o2);
    graph.addEdge(o2, o3);
    graph.addEdge(o3, o1);

    try {
      graph.calculateOrder();
      fail();
    } catch (Ant4EclipseException e) {
      assertEquals("The specified graph contains cyclic dependencies (f.e. 'o1 -> o2 -> o3 -> o1').", e.getMessage());
    }
  }

  @Test
  public void testEdge() {
    Object parent = new Object();
    Object child = new Object();

    DependencyGraph.Edge<Object> edge_1 = new DependencyGraph.Edge<Object>(parent, child);
    DependencyGraph.Edge<Object> edge_2 = new DependencyGraph.Edge<Object>(parent, child);
    assertEquals(edge_1.hashCode(), edge_2.hashCode());
    assertEquals(edge_1, edge_2);
    assertTrue(edge_1.equals(edge_1));
    assertTrue(edge_2.equals(edge_2));
    assertFalse(edge_1.equals(null));
    assertFalse(edge_2.equals(null));

    DependencyGraph.Edge<Object> edge_3 = new DependencyGraph.Edge<Object>(parent, new Object());
    DependencyGraph.Edge<Object> edge_4 = new DependencyGraph.Edge<Object>(new Object(), child);
    assertFalse(edge_1.equals(edge_3));
    assertFalse(edge_1.equals(edge_4));

    assertFalse(edge_1.equals(new RuntimeException()));
  }
}