package com.theobfuscatorinator;

import static org.junit.Assert.assertTrue;

import com.theobfuscatorinator.graph.Edge;
import com.theobfuscatorinator.graph.Graph;
import com.theobfuscatorinator.graph.Node;

import org.junit.Test;

public class GraphTest {
    
    @Test
    public void testNodeAdded() {
        Graph graph = new Graph();
        graph.addNode("test");
        boolean found = false;
        for (Node node : graph.getNodes()) {
            if (node.getName().equals("test")) {
                found = true;
            }
        }
        assertTrue("Node added was not in the list of nodes in the graph!", found);
    }

    @Test
    public void testMultipleNodesAdded() {
        Graph graph = new Graph();
        graph.addNode("test");
        graph.addNode("test2");
        graph.addNode("test3");
        assertTrue("Some of the nodes added were not in the list of nodes in the graph!", graph.getNodes().size() == 3);
    }

    @Test
    public void testUniqueAdaded() {
        Graph graph = new Graph();
        graph.addNode("test");
        graph.addNode("test");
        int count = 0;
        for (Node node : graph.getNodes()) {
            if (node.getName().equals("test")) {
                count++;
            }
        }
        assertTrue("The same node was added twice! Count of unique node: " + count, count == 1);
    }

    @Test
    public void testNodesSorted() {
        Graph graph = new Graph();
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addNode("b");
        graph.addNode("a");
        graph.addNode("z");
        graph.addNode("y");
        graph.addNode("x");
        graph.addNode("x");
        graph.addNode("y");
        graph.addNode("gamer");

        for (int i = 0; i < graph.getNodes().size() - 1; i++) {
            assertTrue("Nodes were not sorted!", graph.getNodes().get(i).getName().compareTo(graph.getNodes().get(i + 1).getName()) <= 0);
        }
    }

    @Test
    public void testAddEdge() {
        Graph graph = new Graph();
        graph.addNode("a");
        graph.addNode("b");
        graph.addEdge("a", "b", "test");

        boolean found = false;
        for (Edge edge : graph.getEdges()) {
            if (edge.getFrom().getName().equals("a") && edge.getTo().getName().equals("b") && edge.getValue().equals("test")) {
                found = true;
            }
        }

        assertTrue("Edge was not added!", found);
    }

    @Test
    public void testAddMultipleEdges() {
        Graph graph = new Graph();
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addEdge("a", "b", "test");
        graph.addEdge("a", "c", "test2");
        graph.addEdge("b", "c", "test3");

        assertTrue("Edge was not added!", graph.getEdges().size() == 3);
    }

    @Test
    public void testAddUniqueEdges() {
        Graph graph = new Graph();
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addEdge("a", "b", "test");
        graph.addEdge("a", "c", "test2");
        graph.addEdge("b", "c", "test3");
        graph.addEdge("a", "b", "test");
        graph.addEdge("a", "c", "test2");
        graph.addEdge("b", "c", "test3");

        assertTrue("Edge was not added!", graph.getEdges().size() == 3);
    }

    @Test
    public void testAddMultipleEdgesToSameNode() {
        Graph graph = new Graph();
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addEdge("a", "b", "test");
        graph.addEdge("a", "b", "test2");

        assertTrue("Edge was not added!", graph.getEdges().size() == 2);
    }

    @Test
    public void testAddEdgeToItself() {
        Graph graph = new Graph();
        graph.addNode("a");
        graph.addEdge("a", "a", "test");

        assertTrue("Edge was not added!", graph.getEdges().size() == 1);
    }
}
