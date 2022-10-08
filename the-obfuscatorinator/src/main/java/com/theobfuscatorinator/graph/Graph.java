package com.theobfuscatorinator.graph;

import java.util.ArrayList;

/**
 * This class represents a directed graph.
 * 
 * @author Thomas Andrasek
 */
public class Graph {
    
    private ArrayList<Node<?>> nodes;

    /**
     * Create an empty directed graph.
     */
    public Graph() {
        this.nodes = new ArrayList<Node<?>>();
    }

    /**
     * Add a node to the graph.
     * 
     * @param node The node to add.
     */
    public void addNode(Node<?> node) {
        this.nodes.add(node);
    }

    public ArrayList<Node<?>> getNodes() {
        return this.nodes;
    }

    public void addEdge(Node<?> nodeA, Node<?> nodeB, int type) {
        nodeA.addEdge(type, nodeB);
    }
}
