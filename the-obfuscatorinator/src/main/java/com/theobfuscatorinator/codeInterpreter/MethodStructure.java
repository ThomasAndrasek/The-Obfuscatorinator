package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;

/**
 * This class tracks basic information about a method. Intended for protected initialization by
 *  ClassStructure objects.
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

    /**
     * Constructor for MethodStructure.
     * 
     * @param methodName The name of the method.
     * @param scope The scope of the method.
     * @param isStatic Whether or not the method is static.
     * @param template The template of the method.
     * @param arguments The arguments of the method.
     * @param returnType The return type of the method.
     * @param code The code of the method.
     * @param containerStack The stack of containers that this method is nested in.
     * @param sourceFile The source file of the method.
     */
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

    /**
     * Returns the name of the method.
     * 
     * @return The name of the method.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Set the name of the method.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Returns the source code of the method.
     */
    public String getMethodCode() {
        return this.code;
    }

    /**
     * Set the source code of the method.
     */
    public void setMethodCode(String sourceCode) {
        this.code = this.sourceCode;
    }

    /**
     * Returns the list of arguments of the method.
     */
    public ArrayList<String> getArguments() {
        return args;
    }

}
