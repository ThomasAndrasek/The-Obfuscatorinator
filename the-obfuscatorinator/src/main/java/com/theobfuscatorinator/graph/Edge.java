package com.theobfuscatorinator.graph;

public class Edge {
    private int type;
    private Node<?> end;

    public Edge(int type, Node<?> node) {
        this.type = type;
        this.end = node;
    }

    public int getType() {
        return this.type;
    }

    public Node<?> getEnd() {
        return this.end;
    }
}
