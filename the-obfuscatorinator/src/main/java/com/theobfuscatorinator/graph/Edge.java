package com.theobfuscatorinator.graph;

/**
 * This class represents a directed edge in a graph.
 * 
 * @author Thomas Andrasek
 */
public class Edge {
    
    private Node from;
    private Node to;
    private String value;

    /**
     * Create an edge with the given nodes and value.
     * 
     * @param from The node to add the edge from.
     * @param to The node to add the edge to.
     * @param value The value of the edge.
     */
    public Edge(Node from, Node to, String value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    /**
     * Get the node the edge is from.
     * 
     * @return The node the edge is from.
     */
    public Node getFrom() {
        return this.from;
    }

    /**
     * Get the node the edge is to.
     * 
     * @return The node the edge is to.
     */
    public Node getTo() {
        return this.to;
    }

    /**
     * Get the value of the edge.
     * 
     * @return The value of the edge.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set the value of the edge.
     * 
     * @param value The new value of the edge.
     */
    public void setValue(String value) {
        this.value = value;
    }

}
