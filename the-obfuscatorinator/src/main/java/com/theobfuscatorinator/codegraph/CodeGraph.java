package com.theobfuscatorinator.codegraph;

import com.theobfuscatorinator.codeInterpreter.ClassStructure;
import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.MethodStructure;
import com.theobfuscatorinator.graph.Edge;
import com.theobfuscatorinator.graph.Graph;
import com.theobfuscatorinator.graph.Node;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a directed graph of the file structure of a java project.
 * 
 * @author Thomas Andrasek
 */
public class CodeGraph {
    public static final int CLASS_OWN_METHOD = 0;

    private Graph graph;

    /**
     * Construct a new graph from the given file path.
     * 
     * The start of the graph will be from the first file found with a main method.
     */
    public CodeGraph(ArrayList<CodeStructure> code) {
        this.graph = new Graph();

        for (CodeStructure codeStruct : code) {
            ArrayList<ClassStructure> classStructures = new ArrayList<>();
            classStructures.addAll(codeStruct.getClasses());
            while (classStructures.size() > 0) {
                ClassStructure classStruct = classStructures.remove(0);
                classStructures.addAll(classStruct.getClasses());
                Node<ClassStructure> classNode = new Node<>(classStruct);
                this.graph.addNode(classNode);

                for (MethodStructure methodStruct : classStruct.getMethods()) {
                    Node<MethodStructure> methodNode = new Node<>(methodStruct);

                    this.graph.addNode(methodNode);
                    this.graph.addEdge(classNode, methodNode, CLASS_OWN_METHOD);
                }
            }
        }


        for (Node<?> node : this.graph.getNodes()) {
            if (!(node.getValue() instanceof ClassStructure)) {
                continue;
            }

            ClassStructure classStruct = (ClassStructure) node.getValue();
            System.out.println(classStruct.getClassName());

            for (Edge edge : node.getEdges()) {
                MethodStructure methodStruct = (MethodStructure) edge.getEnd().getValue();
                System.out.println("\t" + methodStruct.getMethodName());
                System.out.println("\t" + edge.getType());
            }
        }
    }

    /**
     * Finds the CodeStructure object of a project that contains the main method, if one exists.
     * @param code Set of codestructures that represents a project's source code
     * @return The CodeStructure object in the HashSet that contains the main method. Null if no main method exists.
     */
    public static CodeStructure findMainMethod(ArrayList<CodeStructure> code){
        for(CodeStructure file : code){
            if(file.containsMainMethod()) return file;
        }
        return null;
    }
}
