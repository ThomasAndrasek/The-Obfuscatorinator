package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;

public class MethodStructure {

    String methodName;
    String sourceCode;
    String sourceFile;

    //This should contain all of the classes that this method is nested in, if any.
    // The classes should be ordered largest to smallest by scope.
    private ArrayList<ClassStructure> containers;

    private ArrayList<String> args;

    public MethodStructure(String name, String code, String filename, ArrayList<ClassStructure> classStructure){
        methodName = name;
        sourceCode = code;
        sourceFile = filename;
        containers = classStructure;
    }

}
