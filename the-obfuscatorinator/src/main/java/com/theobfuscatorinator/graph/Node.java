package com.theobfuscatorinator.graph;

import java.util.ArrayList;

/**
 * This class represents a node in a directed graph.
 * 
 * @author Thomas Andrasek
 */
public class Node<T> {
    private long id;
    private T value;
    private ArrayList<Edge> edges;

    private static long currentId = 0;

    /**
     * Create a node with the given name.
     * 
     * @param name The name of the node.
     */
    public Node(T value) {
        currentId++;
        this.id = currentId;
        this.value = value;
        this.edges = new ArrayList<Edge>();
    }

    /**
     * Get the name of the node.
     * 
     * @return
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Get the things owned by the Node
     * 
     * @return
     */
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Add an owned entity to the node.
     * 
     * @param entity
     */
    public void addEdge(int type, Node<?> node) {
        this.edges.add(new Edge(type, node));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        else {
            Node<?> n = (Node<?>) o;

            if (n.id == this.id) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
