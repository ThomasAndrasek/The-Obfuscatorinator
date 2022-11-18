package com.theobfuscatorinator.obfuscation;

import java.util.ArrayList;

import com.theobfuscatorinator.codeInterpreter.ClassStructure;
import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.MethodStructure;
import com.theobfuscatorinator.codeInterpreter.VariableStructure;
import com.theobfuscatorinator.codegraph.CodeGraph;
import com.theobfuscatorinator.graph.Edge;
import com.theobfuscatorinator.graph.Node;

public class Renamer {
    /**
     * Generate random name for methods or variables.
     * <br/><br/>
     * 
     * Names generated with a length of random characters between 10 and 110. All generated names
     * start with a lower case letter. The rest of the name is made up of random letters, upper or
     * lower case, and numbers between 0 and 9.
     * 
     * @return A random name.
     */
    public static String generateName() {
        String charactersToChooseFrom =
         "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String basicLetters = "abcdefghijklmnopqrstuvwxyz";

        StringBuilder className = new StringBuilder();

        className.append(basicLetters.charAt((int) (Math.random() * basicLetters.length())));

        int numberOfLetters = (int) (Math.random() * 100) + 10;
        for (int i = 0; i < numberOfLetters; i++) {
            className.append(charactersToChooseFrom.charAt(
                (int) (Math.random() * charactersToChooseFrom.length())));
        }

        return className.toString();
    }

    public static void renamePrivateStaticVariableForClass(ClassStructure classStructure, VariableStructure variableStructure) {
        String cleanedCode = CodeStructure.removeInnerCode(classStructure.getCode());
        System.out.println(variableStructure);
        System.out.println(classStructure.getClassName());
        System.out.println();
        //System.out.println(cleanedCode);
    }

    public static void renamePrivateStaticVariables(CodeGraph graph) {
        ArrayList<Node<ClassStructure>> classStructureNodes = graph.getclassStructureNodes();
        for (Node<ClassStructure> classStructureNode : classStructureNodes) {
            ArrayList<VariableStructure> privateStaticVariables = new ArrayList<>();
            ArrayList<Node<?>> classStructNodesToVisit = new ArrayList<>();
            ArrayList<ClassStructure> classStructures = new ArrayList<>();
            classStructures.add(classStructureNode.getValue());
            for (Edge edgeFromClass : classStructureNode.getEdges()) {
                if (edgeFromClass.getEnd().getValue() instanceof VariableStructure && edgeFromClass.getType() == CodeGraph.CLASS_OWN_VARIABLE) {
                    VariableStructure potentialVar = (VariableStructure) edgeFromClass.getEnd().getValue();
                    if (potentialVar.isStatic() && potentialVar.getScope().equals("private")) {
                        privateStaticVariables.add(potentialVar);
                    }
                }
                else if (edgeFromClass.getEnd().getValue() instanceof ClassStructure && edgeFromClass.getType() == CodeGraph.CLASS_OWN_CLASS) {
                    classStructNodesToVisit.add(edgeFromClass.getEnd());
                    classStructures.add((ClassStructure) edgeFromClass.getEnd().getValue());
                }
            }

            if (privateStaticVariables.size() > 0) {
                // System.out.println(privateStaticVariables.get(0));
                // System.out.println(privateStaticVariables.size());
                // System.out.println(classStructNodesToVisit.size());
                for (int i = 0; i < classStructNodesToVisit.size(); i++) {
                    for (Edge edgeFromClass : classStructNodesToVisit.get(i).getEdges()) {
                        if (edgeFromClass.getEnd().getValue() instanceof ClassStructure && edgeFromClass.getType() == CodeGraph.CLASS_OWN_CLASS) {
                            classStructNodesToVisit.add(edgeFromClass.getEnd());
                            classStructures.add((ClassStructure) edgeFromClass.getEnd().getValue());
                        }
                    }
                } 
    
                for (VariableStructure varStructure : privateStaticVariables) {
                    for (ClassStructure classStructure : classStructures) {
                        renamePrivateStaticVariableForClass(classStructure, varStructure);
                    }
                }
            }
        }
    }

    public static void renameCodeGraph(CodeGraph graph) {
        
    }
}
