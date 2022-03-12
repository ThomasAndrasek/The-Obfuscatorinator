package com.theobfuscatorinator.graph;

import java.util.ArrayList;

/**
 * This class represents a directed graph.
 * 
 * @author Thomas Andrasek
 */
public class Graph {
    
    private ArrayList<Node> nodes;

    /**
     * Create an empty directed graph.
     */
    public Graph() {
        this.nodes = new ArrayList<Node>();
    }

    /**
     * Add a node to the graph.
     * 
     * @param node The node to add.
     */
    public void addNode(String node) {
        if (getNode(node) != null) {
            return;
        }

        int low = 0;
        int high = this.nodes.size();
        while (low < high) {
            int mid = (low + high) / 2;
            Node n = this.nodes.get(mid);
            int dif = node.compareTo(n.getName());
            if (dif < 0) {
                high = mid;
            } else if (dif > 0) {
                low = mid + 1;
            } else {
                break;
            }
        }

        this.nodes.add(low, new Node(node));
    }

    /**
     * Get a node from the graph.
     * 
     * @param name The name of the node to get.
     * @return The node with the given name.
     */
    private Node getNode(String node) {
        int low = 0;
        int high = this.nodes.size();
        while (low < high) {
            int mid = (low + high) / 2;
            Node n = this.nodes.get(mid);
            int dif = node.compareTo(n.getName());
            if (dif < 0) {
                high = mid;
            } else if (dif > 0) {
                low = mid + 1;
            } else {
                return n;
            }
        }

        return null;
    }

    /**
     * Add an edge to the graph.
     * 
     * @param from The node to add the edge from.
     * @param to The node to add the edge to.
     * @param value The value of the edge.
     */
    public void addEdge(String from, String to, String value) {
        this.addNode(from);
        this.addNode(to);
        
        Node n1 = this.getNode(from);
        Node n2 = this.getNode(to);
        n1.addEdge(new Edge(n1, n2, value));
    }

    /**
     * Get the nodes of the graph.
     * 
     * @return
     */
    public ArrayList<Node> getNodes() {
        return this.nodes;
    }

    /**
     * Get the edges of the graph.
     * 
     * @return
     */
    public ArrayList<Edge> getEdges() {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (Node n : this.nodes) {
            edges.addAll(n.getEdges());
        }
        return edges;
    }
}
