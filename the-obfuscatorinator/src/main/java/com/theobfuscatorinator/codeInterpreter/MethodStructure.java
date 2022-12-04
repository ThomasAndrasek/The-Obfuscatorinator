package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class tracks basic information about a method. Intended for protected initialization by
 *  ClassStructure objects.
 * @author Carter Del Ciello
 */
public class MethodStructure {

    protected String methodName;
    String sourceCode;

    protected String returnType;
    private ArrayList<String> args;
    private boolean isStatic;
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
    protected MethodStructure(String methodName, String scope, boolean isStatic, String template,
                              String arguments, String returnType, String code) {
        this.methodName = methodName;
        this.sourceCode = code;
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
     * Returns if the method is static.
     * 
     * @return Whether the method is static.
     */
    public boolean isStatic() {
        return this.isStatic;
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
     * Gets the template of the method.
     * 
     * @return Template of the mehtod.
     */
    public String getTemplate() {
        return this.template;
    }


    /**
     * Gets the scope of the method.
     * 
     * @return Scope of the method.
     */
    public String getScope() {
        return this.scope;
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

    /**
     * Finds all methods defined in this class. Does not include methods defined in other classes 
     * nested within this class. This will not include constructors.
     * 
     * @param classStructure The class to search for methods in.
     * 
     * @return An arraylist of MethodStructures that represents every method that was found.
     */
    public static ArrayList<MethodStructure> identifyMethods(ClassStructure classStructure){
        String code = classStructure.getInnerCode();
        ArrayList<MethodStructure> output = new ArrayList<MethodStructure>();
        // Regex adopted from: https://stackoverflow.com/a/16118844/5956948
        Pattern methodFinder = Pattern.compile("(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
        Matcher methodMatcher = methodFinder.matcher(code);
        int index = 0;
        while(methodMatcher.find(index)){
            String method = "";
            int i = 0;
            // Build a String of the method.
            while (i <= methodMatcher.groupCount() && methodMatcher.group(i) != null){
                method += methodMatcher.group(i);
                i++;
            }
            String fullMethod = method.substring(0);

            method = method.replaceAll("\\s+", " ");

            // Ignore empty methods.
            if (method == "") {
                break;
            }

            int indexB = code.indexOf(fullMethod) - 1;

            String inbetween = "";
            while (indexB >= 0) {
                char c = code.charAt(indexB);
                if (c == '{' || c == '}' || c == ';') {
                    break;
                }
                inbetween = c + inbetween;
                indexB--;
            }

            boolean valid = true;
            if (inbetween.toString().contains("@Override")) {
                valid = false;
            }

            // Check the scope of the method.
            String scope = "";
            if (method.startsWith("public")) {
                scope = "public";
                method = method.substring(7);
            } else if (method.startsWith("private")) {
                scope = "private";
                method = method.substring(8);
            } else if (method.startsWith("protected")) {
                scope = "protected";
                method = method.substring(9);
            }

            // Check if the method is sstatic.
            boolean staticStatus = false;
            if (method.startsWith("static")) {
                staticStatus = true;
                method = method.substring(7);
            }

            // Get the name of the method.
            String name = "";
            if (method.contains("(")) {
                int indexC = method.indexOf("(");
                int extraSpace = 0;
                if (method.charAt(indexC - 1) == ' ') {
                    extraSpace = 1;
                    indexC -= 2;
                }
                while (indexC != -1 && method.charAt(indexC) != ' ') {
                    indexC--;
                }
                name = method.substring(indexC+1, method.indexOf("(") - extraSpace);
                method = method.substring(0, indexC+1) + method.substring(method.indexOf("("));
            }

            // Get the arguments of the method.
            String arguments = "";
            if (method.contains("(")) {
                arguments = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
                method = method.substring(0, method.indexOf("("));
            }

            // Get the template of the method.
            String template = "";
            if (method.startsWith("<")) {
                template = method.substring(0, method.indexOf(">") + 1);
                method = method.substring(method.indexOf(">") + 2);
            }

            // Return type of the method.
            String returnType = method;

            // Get the body of the method.
            CodeStructure.Pair<String, Integer> detectBody =
                 CodeStructure.getCodeBetweenBrackets(code, methodMatcher.start(), '{','}');
            index = detectBody.second;

            // Build the MethodStructure.
            if (!returnType.equals("") && valid) {
                output.add(
                    new MethodStructure(name, scope, staticStatus, template, arguments, 
                                        returnType, detectBody.first));
            } 
        }

        return output;
    }

    /**
     * Finds all methods defined in this interface.. Does not include methods defined in other classes 
     * or interfaces nested within this interface. This will not include constructors.
     * 
     * @param interfaceStructure The interface to search for methods in.
     * 
     * @return An arraylist of MethodStructures that represents every method that was found.
     */
    public static ArrayList<MethodStructure> identifyMethods(InterfaceStructure interfaceStructure){
        String code = interfaceStructure.getInnerCode();
        ArrayList<MethodStructure> output = new ArrayList<MethodStructure>();
        // Regex adopted from: https://stackoverflow.com/a/16118844/5956948
        Pattern methodFinder = Pattern.compile("(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
        Matcher methodMatcher = methodFinder.matcher(code);
        while(methodMatcher.find()){
            String method = "";
            int i = 0;
            while (i <= methodMatcher.groupCount() && methodMatcher.group(i) != null){
                method += methodMatcher.group(i);
                i++;
            }

            String fullMethod = method.substring(0);

            method = method.replaceAll("\\s+", " ");

            // Ignore empty methods.
            if (method == "") {
                break;
            }

            int indexB = code.indexOf(fullMethod) - 1;

            String inbetween = "";
            while (indexB >= 0) {
                char c = code.charAt(indexB);
                if (c == '{' || c == '}' || c == ';') {
                    break;
                }
                inbetween = c + inbetween;
                indexB--;
            }

            boolean valid = true;
            if (inbetween.toString().contains("@Override")) {
                valid = false;
            }

            // Get the scope of the method.
            String scope = "";
            if (method.startsWith("public")) {
                scope = "public";
                method = method.substring(7);
            } else if (method.startsWith("private")) {
                scope = "private";
                method = method.substring(8);
            } else if (method.startsWith("protected")) {
                scope = "protected";
                method = method.substring(9);
            }

            // Get if the method is static.
            boolean staticStatus = false;
            if (method.startsWith("static")) {
                staticStatus = true;
                method = method.substring(7);
            }

            // Get the name of the method.
            String name = "";
            if (method.contains("(")) {
                int indexC = method.indexOf("(");
                int extraSpace = 0;
                if (method.charAt(indexC - 1) == ' ') {
                    extraSpace = 1;
                    indexC -= 2;
                }
                while (indexC != -1 && method.charAt(indexC) != ' ') {
                    indexC--;
                }
                name = method.substring(indexC+1, method.indexOf("(") - extraSpace);
                method = method.substring(0, indexC+1) + method.substring(method.indexOf("("));
            }

            // Get the arguments of the method.
            String arguments = "";
            if (method.contains("(")) {
                arguments = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
                method = method.substring(0, method.indexOf("("));
            }

            // Get the tempalte of the method.
            String template = "";
            if (method.startsWith("<")) {
                template = method.substring(0, method.indexOf(">") + 1);
                method = method.substring(method.indexOf(">") + 2);
            }

            // Get the return type of the method.
            String returnType = method;

            // Get the inner code of the method.
            // If the method does not have an inner body ignore trying to get the inner code.
            try {
                CodeStructure.Pair<String, Integer> detectBody =
                    CodeStructure.getCodeBetweenBrackets(code, methodMatcher.start(), '{','}');

                if (!returnType.equals("") && valid) {
                    output.add(
                        new MethodStructure(name, scope, staticStatus, template, arguments, 
                                            returnType, detectBody.first));
                } 
            }
            catch (Exception e) {
                if (!returnType.equals("") && valid) {
                    output.add(
                        new MethodStructure(name, scope, staticStatus, template, arguments, 
                                            returnType, ""));
                } 
            }
        }

        return output;
    }
}
