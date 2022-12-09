package com.theobfuscatorinator.codegraph;

import com.theobfuscatorinator.codeInterpreter.ClassStructure;
import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.ImportStructure;
import com.theobfuscatorinator.codeInterpreter.InterfaceStructure;
import com.theobfuscatorinator.codeInterpreter.MethodStructure;
import com.theobfuscatorinator.codeInterpreter.PackageStructure;
import com.theobfuscatorinator.codeInterpreter.VariableStructure;
import com.theobfuscatorinator.graph.Edge;
import com.theobfuscatorinator.graph.Graph;
import com.theobfuscatorinator.graph.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * This class represents a directed graph of the file structure of a java project.
 * 
 * Nodes in this graph represent different structurs such as Files, Classes, Interfaces, Methods,
 * Variables, Imports, and Packages.
 * 
 * Edges in this graph represent usage or owernship of different structures. For instance File ->
 * Class represents that the class is in the outermost scope of the File and File has ownership
 * over the class.
 * 
 * @author Thomas Andrasek
 */
public class CodeGraph {
    public static final int FILE = 0;
    public static final int CLASS = 1;
    public static final int INTERFACE = 2;
    public static final int METHOD = 3;
    public static final int VARIABLE = 4;
    public static final int IMPORT = 5;
    public static final int PACKAGE = 6;

    public static final int FILE_OWN_CLASS = 0;
    public static final int FILE_OWN_INTERFACE = 1;
    public static final int FILE_IS_PART_OF_PACKAGE = 2;
    public static final int FILE_USES_IMPORT = 3;
    public static final int CLASS_OWN_METHOD = 4;
    public static final int CLASS_OWN_CLASS = 5;
    public static final int CLASS_OWN_VARIABLE = 6;
    public static final int CLASS_OWN_INTERFACE = 7;
    public static final int METHOD_OWN_PARAMETER = 8;
    public static final int METHOD_OWN_VARIABLE = 9;
    public static final int INTERFACE_OWN_INTERFACE = 10;
    public static final int INTERFACE_OWN_VARIABLE = 11;
    public static final int INTERFACE_OWN_METHOD = 12;

    private Graph graph;
    private ArrayList<Node<CodeStructure>> codeStructureNodes;
    private ArrayList<Node<ClassStructure>> classStructureNodes;
    private ArrayList<Node<VariableStructure>> variableStructureNodes;
    private ArrayList<Node<MethodStructure>> methodStructureNodes;
    private ArrayList<Node<ImportStructure>> importStructureNodes;
    private ArrayList<Node<PackageStructure>> packageStructureNodes;
    private ArrayList<Node<InterfaceStructure>> interfaceStructureNodes;

    /**
     * Constructs the Code Graph of a Java Project.
     * 
     * @param code Code Structures of all of the Java files in a Java Project.
     */
    public CodeGraph(ArrayList<CodeStructure> code) {
        this.graph = new Graph();
        this.classStructureNodes = new ArrayList<>();
        this.codeStructureNodes = new ArrayList<>();
        this.importStructureNodes = new ArrayList<>();
        this.methodStructureNodes = new ArrayList<>();
        this.packageStructureNodes = new ArrayList<>();
        this.variableStructureNodes = new ArrayList<>();
        this.interfaceStructureNodes = new ArrayList<>();

        // Go through each file.
        for (CodeStructure codeStruct : code) {
            ArrayList<Node<MethodStructure>> methodStructureNodes = new ArrayList<>();

            // Add the file to the graph.
            Node<CodeStructure> codeStructureNode = new Node<CodeStructure>(codeStruct);
            this.graph.addNode(codeStructureNode);
            this.codeStructureNodes.add(codeStructureNode);

            // Find the outermost classes in the Java file.
            ArrayList<Node<ClassStructure>> classStructureNodes = new ArrayList<>();
            for (ClassStructure classStruct : ClassStructure.identifyClasses(codeStruct)) {
                Node<ClassStructure> classStructureNode = new Node<ClassStructure>(classStruct);
                this.graph.addEdge(codeStructureNode, classStructureNode, FILE_OWN_CLASS);
                classStructureNodes.add(classStructureNode);
            }

            // If the file is part of a package identify the package and add it to the graph.
            PackageStructure packageStructure = PackageStructure.identifyPackage(codeStruct);
            if (packageStructure != null) {
                Node<PackageStructure> packageStructureNode = new Node<PackageStructure>(packageStructure);

                this.graph.addNode(packageStructureNode);
                this.packageStructureNodes.add(packageStructureNode);
                this.graph.addEdge(codeStructureNode, packageStructureNode, FILE_IS_PART_OF_PACKAGE);
            }

            // If the file utilizes any imports identify them and add them to the graph.
            ArrayList<ImportStructure> importStructures = ImportStructure.identifyImports(codeStruct);
            for (ImportStructure importStructure : importStructures) {
                Node<ImportStructure> importStructureNode = new Node<ImportStructure>(importStructure);

                this.graph.addNode(importStructureNode);
                this.importStructureNodes.add(importStructureNode);
                this.graph.addEdge(codeStructureNode, importStructureNode, FILE_USES_IMPORT);
            }

            // Find the outermost interfaces in the Java file.
            ArrayList<InterfaceStructure> interfaceStructures = InterfaceStructure.identifyInterfaceStructures(codeStruct);
            ArrayList<Node<InterfaceStructure>> interfaceStructureNodes = new ArrayList<>();
            for (InterfaceStructure interfaceStructure : interfaceStructures) {
                Node<InterfaceStructure> interfaceStructureNode = new Node<InterfaceStructure>(interfaceStructure);
                interfaceStructureNodes.add(interfaceStructureNode);
            }

            // Continue searching through classes and interfaces as inner/nested classes and
            // interfaces can be found inside of classes or other interfaces.
            while (classStructureNodes.size() > 0 || interfaceStructureNodes.size() > 0) {
                // If there are still nested classes to explore.
                if (classStructureNodes.size() > 0) {
                    // Add the class to the graph.
                    Node<ClassStructure> classStructNode = classStructureNodes.remove(0);
                    this.graph.addNode(classStructNode);
                    this.classStructureNodes.add(classStructNode);
    
                    // Identify nested classes and add them to the graph.
                    for (ClassStructure classStruct : ClassStructure.identifyClasses(classStructNode.getValue())) {
                        Node<ClassStructure> n = new Node<>(classStruct);
                        classStructureNodes.add(n);
                        this.graph.addEdge(classStructNode, n, CLASS_OWN_CLASS);
                    }
    
                    // Identify nested methods and add them to the graph.
                    for (MethodStructure methodStruct : MethodStructure.identifyMethods(classStructNode.getValue())) {
                        Node<MethodStructure> methodNode = new Node<>(methodStruct);
                        methodStructureNodes.add(methodNode);
    
                        this.graph.addNode(methodNode);
                        this.methodStructureNodes.add(methodNode);
                        this.graph.addEdge(classStructNode, methodNode, CLASS_OWN_METHOD);
                    }
    
                    // Identify nested variables and add them to the graph..
                    for (VariableStructure variableStruct : VariableStructure.identifyClassVariables(classStructNode.getValue())) {
                        Node<VariableStructure> variableNode = new Node<VariableStructure>(variableStruct);
    
                        this.graph.addNode(variableNode);
                        this.variableStructureNodes.add(variableNode);
                        this.graph.addEdge(classStructNode, variableNode, CLASS_OWN_VARIABLE);
                    }

                    // Identify nested interfaces and add them to the graph.
                    for (InterfaceStructure interfaceStruct : InterfaceStructure.identifyInterfaceStructures(classStructNode.getValue())) {
                        Node <InterfaceStructure> n = new Node<>(interfaceStruct);
                        interfaceStructureNodes.add(n);
                        this.graph.addEdge(classStructNode, n, CLASS_OWN_INTERFACE);
                    }
                }
                
                // If there are unexplored interfaces explore them.
                if (interfaceStructureNodes.size() > 0) {
                    // Add interface to graph.
                    Node<InterfaceStructure> interfaceStructNode = interfaceStructureNodes.remove(0);
                    this.graph.addNode(interfaceStructNode);
                    this.interfaceStructureNodes.add(interfaceStructNode);
                    this.graph.addEdge(codeStructureNode, interfaceStructNode, FILE_OWN_INTERFACE);

                    // Identify nested interfaces and add them to the graph.
                    for (InterfaceStructure interfaceStruct : InterfaceStructure.identifyInterfaceStructures(interfaceStructNode.getValue())) {
                        Node <InterfaceStructure> n = new Node<>(interfaceStruct);
                        interfaceStructureNodes.add(n);
                        this.graph.addEdge(interfaceStructNode, n, INTERFACE_OWN_INTERFACE);
                    }

                    // Identify nested variables and add them to the graph.
                    for (VariableStructure variableStruct : VariableStructure.identifyInterfaceVariables(interfaceStructNode.getValue())) {
                        Node<VariableStructure> variableNode = new Node<VariableStructure>(variableStruct);
    
                        this.graph.addNode(variableNode);
                        this.variableStructureNodes.add(variableNode);
                        this.graph.addEdge(interfaceStructNode, variableNode, INTERFACE_OWN_VARIABLE);
                    }

                    // Identify nested methods and add them to the graph.
                    for (MethodStructure methodStruct : MethodStructure.identifyMethods(interfaceStructNode.getValue())) {
                        Node<MethodStructure> methodNode = new Node<MethodStructure>(methodStruct);

                        this.graph.addNode(methodNode);
                        this.methodStructureNodes.add(methodNode);
                        this.graph.addEdge(interfaceStructNode, methodNode, INTERFACE_OWN_METHOD);
                    }
                }
            }

            // For found methods search them for parameters and variables.
            // Add those parameters and variables to the graph.
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

    /**
     * Prints the graph to the console by node -> node, edge type.
     */
    public void printCodeGraph() {
        for (Node<?> node : this.graph.getNodes()) {
            if (node.getValue() instanceof ClassStructure) {
                ClassStructure classStruct = (ClassStructure) node.getValue();
                System.out.println(classStruct.getName());

                for (Edge edge : node.getEdges()) {
                    if (edge.getEnd().getValue() instanceof MethodStructure) {
                        MethodStructure methodStruct = (MethodStructure) edge.getEnd().getValue();
                        System.out.println("\t" + methodStruct.getMethodName());
                    }
                    else if (edge.getEnd().getValue() instanceof ClassStructure) {
                        ClassStructure innerClassStruct = (ClassStructure) edge.getEnd().getValue();
                        System.out.println("\t" + innerClassStruct.getName());
                    } 
                    else if (edge.getEnd().getValue() instanceof InterfaceStructure) {
                        InterfaceStructure interfaceStruct = (InterfaceStructure) edge.getEnd().getValue();
                        System.out.println("\t" + interfaceStruct.getName());
                    }
                    else {
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
                        System.out.println("\t" + classStructure.getName());
                    }
                    else if (edge.getEnd().getValue() instanceof PackageStructure) {
                        PackageStructure packageStructure = (PackageStructure) edge.getEnd().getValue();
                        System.out.println("\t" + packageStructure.getPackageId());
                    }
                    else if (edge.getEnd().getValue() instanceof ImportStructure) {
                        ImportStructure importStructure = (ImportStructure) edge.getEnd().getValue();
                        System.out.println("\t" + importStructure);
                    }
                    else if (edge.getEnd().getValue() instanceof InterfaceStructure) {
                        InterfaceStructure interfaceStructure = (InterfaceStructure) edge.getEnd().getValue();
                        System.out.println("\t" + interfaceStructure.getName());
                    }

                    System.out.println("\t" + edge.getType());
                }
            }
            else if (node.getValue() instanceof InterfaceStructure) {
                InterfaceStructure interfaceStructure = (InterfaceStructure) node.getValue();
                System.out.println(interfaceStructure.getName());

                for (Edge edge : node.getEdges()) {
                    if (edge.getEnd().getValue() instanceof InterfaceStructure) {
                        InterfaceStructure innerInterfaceStructure = (InterfaceStructure) edge.getEnd().getValue();
                        System.out.println("\t" + innerInterfaceStructure.getName());
                    }
                    else if (edge.getEnd().getValue() instanceof VariableStructure) {
                        VariableStructure variableStructure = (VariableStructure) edge.getEnd().getValue();
                        System.out.println("\t" + variableStructure);
                    }
                    else if (edge.getEnd().getValue() instanceof MethodStructure) {
                        MethodStructure methodStruct = (MethodStructure) edge.getEnd().getValue();
                        System.out.println("\t" + methodStruct.getMethodName());
                    }

                    System.out.println("\t" + edge.getType());
                }
            }
        }
    }

    /**
     * Writes graph to external file.
     * 
     * @format nodeA,typeA,nodeB,typeB,edgeType
     * 
     * @param pathToFile Path to the file.
     */
    public void writeCodeGraph(String pathToFile) {
        File fileToWrite = new File(pathToFile);

        try {
            Files.createDirectories(fileToWrite.getParentFile().toPath());
            fileToWrite.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(fileToWrite);

            for (Node<?> node : this.graph.getNodes()) {
                if (node.getValue() instanceof ClassStructure) {
                    ClassStructure classStruct = (ClassStructure) node.getValue();

                    for (Edge edge : node.getEdges()) {
                        StringBuilder lineToWrite = new StringBuilder();

                        if (edge.getEnd().getValue() instanceof MethodStructure) {
                            MethodStructure methodStruct = (MethodStructure) edge.getEnd().getValue();
                            lineToWrite.append(classStruct.getName() + "," + CLASS + "," + methodStruct.getMethodName() + "," + METHOD + "," + edge.getType());
                        }
                        else if (edge.getEnd().getValue() instanceof ClassStructure) {
                            ClassStructure innerClassStruct = (ClassStructure) edge.getEnd().getValue();
                            lineToWrite.append(classStruct.getName() + "," + CLASS + "," + innerClassStruct.getName() + "," + CLASS + "," + edge.getType());
                        } 
                        else if (edge.getEnd().getValue() instanceof InterfaceStructure) {
                            InterfaceStructure interfaceStruct = (InterfaceStructure) edge.getEnd().getValue();
                            lineToWrite.append(classStruct.getName() + "," + CLASS + "," + interfaceStruct.getName() + "," + INTERFACE + "," + edge.getType());
                        }
                        else {
                            VariableStructure variableStructure = (VariableStructure) edge.getEnd().getValue();
                            lineToWrite.append(classStruct.getName() + "," + CLASS + "," + variableStructure.getName() + "," + VARIABLE + "," + edge.getType());
                        }

                        writer.write(lineToWrite.toString() + "\n");
                    }
                }
                else if (node.getValue() instanceof MethodStructure) {
                    MethodStructure methodStruct = (MethodStructure) node.getValue();
                    
                    for (Edge edge : node.getEdges()) {
                        StringBuilder lineToWrite = new StringBuilder();

                        if (edge.getEnd().getValue() instanceof VariableStructure) {
                            VariableStructure variableStructure = (VariableStructure) edge.getEnd().getValue();
                            lineToWrite.append(methodStruct.getMethodName() + "," + METHOD + "," + variableStructure.getName() + "," + VARIABLE + "," + edge.getType());
                        }

                        writer.write(lineToWrite.toString() + "\n");
                    }
                }
                else if (node.getValue() instanceof CodeStructure) {
                    CodeStructure codeStructure = (CodeStructure) node.getValue();
    
                    for (Edge edge : node.getEdges()) {
                        StringBuilder lineToWrite = new StringBuilder();

                        if (edge.getEnd().getValue() instanceof ClassStructure) {
                            ClassStructure classStructure = (ClassStructure) edge.getEnd().getValue();
                            lineToWrite.append(codeStructure.getCodeFileName() + "," + FILE + "," + classStructure.getName() + "," + CLASS + "," + edge.getType());
                        }
                        else if (edge.getEnd().getValue() instanceof PackageStructure) {
                            PackageStructure packageStructure = (PackageStructure) edge.getEnd().getValue();
                            lineToWrite.append(codeStructure.getCodeFileName() + "," + FILE + "," + packageStructure.getPackageId() + "," + PACKAGE + "," + edge.getType());
                        }
                        else if (edge.getEnd().getValue() instanceof ImportStructure) {
                            ImportStructure importStructure = (ImportStructure) edge.getEnd().getValue();
                            lineToWrite.append(codeStructure.getCodeFileName() + "," + FILE + "," + importStructure.toString() + "," + IMPORT + "," + edge.getType());
                        }
                        else if (edge.getEnd().getValue() instanceof InterfaceStructure) {
                            InterfaceStructure interfaceStructure = (InterfaceStructure) edge.getEnd().getValue();
                            lineToWrite.append(codeStructure.getCodeFileName() + "," + FILE + "," + interfaceStructure.getName() + "," + INTERFACE + "," + edge.getType());
                        }

                        writer.write(lineToWrite.toString() + "\n");
                    }
                }
                else if (node.getValue() instanceof InterfaceStructure) {
                    InterfaceStructure interfaceStructure = (InterfaceStructure) node.getValue();
    
                    for (Edge edge : node.getEdges()) {
                        StringBuilder lineToWrite = new StringBuilder();

                        if (edge.getEnd().getValue() instanceof InterfaceStructure) {
                            InterfaceStructure innerInterfaceStructure = (InterfaceStructure) edge.getEnd().getValue();
                            lineToWrite.append(interfaceStructure.getName() + "," + INTERFACE + "," + innerInterfaceStructure.getName() + "," + INTERFACE + "," + edge.getType());
                        }
                        else if (edge.getEnd().getValue() instanceof VariableStructure) {
                            VariableStructure variableStructure = (VariableStructure) edge.getEnd().getValue();
                            lineToWrite.append(interfaceStructure.getName() + "," + INTERFACE + "," + variableStructure.getName() + "," + VARIABLE + "," + edge.getType());
                        }
                        else if (edge.getEnd().getValue() instanceof MethodStructure) {
                            MethodStructure methodStruct = (MethodStructure) edge.getEnd().getValue();
                            lineToWrite.append(interfaceStructure.getName() + "," + INTERFACE + "," + methodStruct.getMethodName() + "," + METHOD + "," + edge.getType());
                        }

                        writer.write(lineToWrite.toString() + "\n");
                    }
                }
            }

            writer.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the CodeStructure object of a project that contains the main method, if one exists.
     * @param code Set of codestructures that represents a project's source code
     * @return The CodeStructure object in the HashSet that contains the main method. Null if no main method exists.
     */
    public static CodeStructure findMainMethod(ArrayList<CodeStructure> code){
        // for(CodeStructure file : code){
        //     if(file.containsMainMethod()) return file;
        // }
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

    public ArrayList<Node<InterfaceStructure>> getInterfaceStructureNodes() {
        return this.interfaceStructureNodes;
    }
}
