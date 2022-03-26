package com.theobfuscatorinator.codeInterpreter;

public class ClassStructure {

    private String sourceCode; //This does not contain string literals or comments, they were removed
    private String className;
    private String sourceFile;

    ClassStructure(String code, String name, String filename){
        sourceCode = code;
        className = name;
        sourceFile = filename;
    }

    public String getClassName(){
        return className;
    }

}
