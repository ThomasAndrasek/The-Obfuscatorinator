package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class tracks basic information about a method. Intended for protected initialization by ClassStructure objects.
 * @author Carter Del Ciello
 */
public class MethodStructure {

    protected String methodName;
    String sourceCode;
    String sourceFile;

    //This should contain all of the classes that this method is nested in, if any.
    // The classes should be ordered largest to smallest by scope.
    private ArrayList<ClassStructure> containers;

    private ArrayList<String> templateClasses;
    protected String returnType;
    private ArrayList<String> args;
    private String isStatic;
    private String scope;
    private String template;
    private String code;

    protected MethodStructure(String methodName, String scope, String isStatic, String template,
                              String arguments, String returnType, String code,
                              ArrayList<ClassStructure> containerStack, String sourceFile) {
        this.methodName = methodName;
        this.sourceCode = code;
        this.sourceFile = sourceFile;
        this.containers = containerStack;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.scope = scope;
        this.template = template;
        this.code = code;
        this.args = new ArrayList<String>();

        args = new ArrayList<String>();
        String[] temp = arguments.split(",");
        for (String s : temp) {
            if (s.equals("")) {
                continue;
            }
            args.add(s.trim());
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodCode() {
        return this.code;
    }

    public void setMethodCode(String sourceCode) {
        this.code = this.sourceCode;
    }

    public ArrayList<String> getArguments() {
        return args;
    }

}
