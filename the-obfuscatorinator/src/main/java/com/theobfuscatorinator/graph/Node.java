package com.theobfuscatorinator.graph;

import java.util.ArrayList;

/**
 * This class represents a node in a directed graph.
 * 
 * @author Thomas Andrasek
 */
public class Node {
    
    private String name;
    private ArrayList<Edge> edges;

    /**
     * Create a node with the given name.
     * 
     * @param name The name of the node.
     */
    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<Edge>();
    }

    /**
     * Get the name of the node.
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the node.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the edges of the node.
     * 
     * @return
     */
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    private boolean containsEdge(Edge edge) {
        int low = 0;
        int high = this.edges.size();
        while (low < high) {
            int mid = (low + high) / 2;
            Edge e = this.edges.get(mid);
            int dif = edge.getTo().getName().compareTo(e.getTo().getName());
            if (dif < 0) {
                high = mid;
            } else if (dif > 0) {
                low = mid + 1;
            } else {
                break;
            }
        }

        if (low < high) {
            int mid = (low + high) / 2;

            int i  = mid;
            while (i < this.edges.size() && this.edges.get(i).getTo().getName().equals(edge.getTo().getName())) {
                if (this.edges.get(i).getValue().equals(edge.getValue())) {
                    return true;
                }
                i++;
            }
        }

        return false;
    }

    /**
     * Add an edge to the node.
     * 
     * @param edge
     */
    public void addEdge(Edge edge) {
        if (this.containsEdge(edge)) {
            return;
        }

        int low = 0;
        int high = this.edges.size();
        while (low < high) {
            int mid = (low + high) / 2;
            Edge e = this.edges.get(mid);
            int dif = edge.getFrom().getName().compareTo(e.getFrom().getName());
            if (dif == 0) {
                dif = edge.getTo().getName().compareTo(e.getTo().getName());
            }
            if (dif < 0) {
                high = mid;
            } else if (dif > 0) {
                low = mid + 1;
            } else {
                break;
            }
        }
        this.edges.add(low, edge);
    }

}
