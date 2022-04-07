package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodStructure {

    String methodName;
    String sourceCode;
    String sourceFile;

    //This should contain all of the classes that this method is nested in, if any.
    // The classes should be ordered largest to smallest by scope.
    private ArrayList<ClassStructure> containers;

    private ArrayList<String> templateClasses;
    String returnType;
    private ArrayList<String> args;

    protected MethodStructure(String name, String code, String filename, ArrayList<ClassStructure> containerStack,
                              String argumentsString, ArrayList<String> templates, String rType){
        methodName = name;
        sourceCode = code;
        sourceFile = filename;
        containers = containerStack;
        templateClasses = templates;
        returnType = rType;
        args = new ArrayList<String>();

        ArrayList<String> argStrings = CodeStructure.getCommaSeparatedValues(argumentsString);
        Pattern variableName = Pattern.compile("(\\w*)\\z");
        for(String arg : argStrings){
            Matcher argumentMatcher = variableName.matcher(arg);
            argumentMatcher.find(0);
            args.add(argumentMatcher.group(1).trim());
        }
    }

}
