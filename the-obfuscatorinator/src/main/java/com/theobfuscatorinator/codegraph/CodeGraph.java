package com.theobfuscatorinator.codegraph;

import com.theobfuscatorinator.codeInterpreter.ClassStructure;
import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.ImportStructure;
import com.theobfuscatorinator.codeInterpreter.MethodStructure;
import com.theobfuscatorinator.codeInterpreter.PackageStructure;
import com.theobfuscatorinator.codeInterpreter.VariableStructure;
import com.theobfuscatorinator.graph.Edge;
import com.theobfuscatorinator.graph.Graph;
import com.theobfuscatorinator.graph.Node;

import java.util.ArrayList;

/**
 * This class represents a directed graph of the file structure of a java project.
 * 
 * @author Thomas Andrasek
 */
public class CodeGraph {
    public static final int FILE_OWN_CLASS = 0;
    public static final int FILE_IS_PART_OF_PACKAGE = 1;
    public static final int FILE_USES_IMPORT = 2;
    public static final int CLASS_OWN_METHOD = 3;
    public static final int CLASS_OWN_CLASS = 4;
    public static final int CLASS_OWN_VARIABLE = 5;
    public static final int METHOD_OWN_PARAMETER = 6;
    public static final int METHOD_OWN_VARIABLE = 7;

    private Graph graph;
    private ArrayList<Node<CodeStructure>> codeStructureNodes;
    private ArrayList<Node<ClassStructure>> classStructureNodes;
    private ArrayList<Node<VariableStructure>> variableStructureNodes;
    private ArrayList<Node<MethodStructure>> methodStructureNodes;
    private ArrayList<Node<ImportStructure>> importStructureNodes;
    private ArrayList<Node<PackageStructure>> packageStructureNodes;

    public CodeGraph(ArrayList<CodeStructure> code) {
        this.graph = new Graph();
        this.classStructureNodes = new ArrayList<>();
        this.codeStructureNodes = new ArrayList<>();
        this.importStructureNodes = new ArrayList<>();
        this.methodStructureNodes = new ArrayList<>();
        this.packageStructureNodes = new ArrayList<>();
        this.variableStructureNodes = new ArrayList<>();

        for (CodeStructure codeStruct : code) {
            System.out.println(CodeStructure.removeInnerCodeOfBraces(codeStruct.getUnCommentedCode()));
            
            ArrayList<Node<ClassStructure>> classStructureNodes = new ArrayList<>();
            for (ClassStructure classStruct : codeStruct.getClasses()) {
                classStructureNodes.add(new Node<ClassStructure>(classStruct));
            }
            ArrayList<Node<MethodStructure>> methodStructureNodes = new ArrayList<>();

            Node<CodeStructure> codeStructureNode = new Node<CodeStructure>(codeStruct);
            this.graph.addNode(codeStructureNode);
            this.codeStructureNodes.add(codeStructureNode);

            PackageStructure packageStructure = PackageStructure.identifyPackage(codeStruct);
            if (packageStructure != null) {
                Node<PackageStructure> packageStructureNode = new Node<PackageStructure>(packageStructure);

                this.graph.addNode(packageStructureNode);
                this.packageStructureNodes.add(packageStructureNode);
                this.graph.addEdge(codeStructureNode, packageStructureNode, FILE_IS_PART_OF_PACKAGE);
            }

            ArrayList<ImportStructure> importStructures = ImportStructure.identifyImports(codeStruct);
            for (ImportStructure importStructure : importStructures) {
                Node<ImportStructure> importStructureNode = new Node<ImportStructure>(importStructure);

                this.graph.addNode(importStructureNode);
                this.importStructureNodes.add(importStructureNode);
                this.graph.addEdge(codeStructureNode, importStructureNode, FILE_USES_IMPORT);
            }

            while (classStructureNodes.size() > 0) {
                Node<ClassStructure> classStructNode = classStructureNodes.remove(0);

                this.graph.addNode(classStructNode);
                this.classStructureNodes.add(classStructNode);
                this.graph.addEdge(codeStructureNode, classStructNode, FILE_OWN_CLASS);

                for (ClassStructure classStruct : classStructNode.getValue().getClasses()) {
                    Node<ClassStructure> n = new Node<>(classStruct);
                    classStructureNodes.add(n);
                    this.graph.addEdge(classStructNode, n, CLASS_OWN_CLASS);
                }

                for (MethodStructure methodStruct : classStructNode.getValue().getMethods()) {
                    Node<MethodStructure> methodNode = new Node<>(methodStruct);
                    methodStructureNodes.add(methodNode);

                    this.graph.addNode(methodNode);
                    this.methodStructureNodes.add(methodNode);
                    this.graph.addEdge(classStructNode, methodNode, CLASS_OWN_METHOD);
                }

                for (VariableStructure variableStruct : VariableStructure.identifyClassVariables(classStructNode.getValue())) {
                    Node<VariableStructure> variableNode = new Node<VariableStructure>(variableStruct);

                    this.graph.addNode(variableNode);
                    this.variableStructureNodes.add(variableNode);
                    this.graph.addEdge(classStructNode, variableNode, CLASS_OWN_VARIABLE);
                }
            }

            for (Node<MethodStructure> methodNode : methodStructureNodes) {
                ArrayList<VariableStructure> parameters = VariableStructure.identifyParameters(methodNode.getValue().getArguments());

                for (VariableStructure parameter : parameters) {
                    Node<VariableStructure> parameterNode = new Node<VariableStructure>(parameter);

                    this.graph.addNode(parameterNode);
                    this.variableStructureNodes.add(parameterNode);
                    this.graph.addEdge(methodNode, parameterNode, METHOD_OWN_PARAMETER);
                }

                ArrayList<VariableStructure> variables = VariableStructure.identifyMethodVariables(methodNode.getValue());

                for (VariableStructure variable : variables) {
                    Node<VariableStructure> variableNode = new Node<VariableStructure>(variable);

                    this.graph.addNode(variableNode);
                    this.variableStructureNodes.add(variableNode);
                    this.graph.addEdge(methodNode, variableNode, METHOD_OWN_VARIABLE);
                }
            }
        }
    }

    public void printCodeGraph() {
        for (Node<?> node : this.graph.getNodes()) {
            if (node.getValue() instanceof ClassStructure) {
                ClassStructure classStruct = (ClassStructure) node.getValue();
                System.out.println(classStruct.getClassName());

                for (Edge edge : node.getEdges()) {
                    if (edge.getEnd().getValue() instanceof MethodStructure) {
                        MethodStructure methodStruct = (MethodStructure) edge.getEnd().getValue();
                        System.out.println("\t" + methodStruct.getMethodName());
                    }
                    else if (edge.getEnd().getValue() instanceof ClassStructure) {
                        ClassStructure innerClassStruct = (ClassStructure) edge.getEnd().getValue();
                        System.out.println("\t" + innerClassStruct.getClassName());
                    } else {
                        VariableStructure variableStructure = (VariableStructure) edge.getEnd().getValue();
                        System.out.println("\t" + variableStructure);
                    }
                    
                    System.out.println("\t" + edge.getType());
                }
            }
            else if (node.getValue() instanceof MethodStructure) {
                MethodStructure methodStruct = (MethodStructure) node.getValue();
                System.out.println(methodStruct.getMethodName());
                
                for (Edge edge : node.getEdges()) {
                    if (edge.getEnd().getValue() instanceof VariableStructure) {
                        VariableStructure variableStructure = (VariableStructure) edge.getEnd().getValue();
                        System.out.println("\t" + variableStructure);
                    }

                    System.out.println("\t" + edge.getType());
                }
            }
            else if (node.getValue() instanceof CodeStructure) {
                CodeStructure codeStructure = (CodeStructure) node.getValue();
                System.out.println(codeStructure.getCodeFile().getName());

                for (Edge edge : node.getEdges()) {
                    if (edge.getEnd().getValue() instanceof ClassStructure) {
                        ClassStructure classStructure = (ClassStructure) edge.getEnd().getValue();
                        System.out.println("\t" + classStructure.getClassName());
                    }
                    else if (edge.getEnd().getValue() instanceof PackageStructure) {
                        PackageStructure packageStructure = (PackageStructure) edge.getEnd().getValue();
                        System.out.println("\t" + packageStructure.getPackageId());
                    }
                    else if (edge.getEnd().getValue() instanceof ImportStructure) {
                        ImportStructure importStructure = (ImportStructure) edge.getEnd().getValue();
                        System.out.println("\t" + importStructure);
                    }

                    System.out.println("\t" + edge.getType());
                }
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

    public ArrayList<Node<CodeStructure>> getCodeStructureNodes() {
        return this.codeStructureNodes;
    }

    public ArrayList<Node<ClassStructure>> getclassStructureNodes() {
        return this.classStructureNodes;
    }

    public ArrayList<Node<MethodStructure>> getMethodStructureNodes() {
        return this.methodStructureNodes;
    }

    public ArrayList<Node<VariableStructure>> getVariableStructureNodes() {
        return this.variableStructureNodes;
    }

    public ArrayList<Node<ImportStructure>> getImportStructureNodes() {
        return this.importStructureNodes;
    }

    public ArrayList<Node<PackageStructure>> getPackageStructureNodes() {
        return this.packageStructureNodes;
    }
}
