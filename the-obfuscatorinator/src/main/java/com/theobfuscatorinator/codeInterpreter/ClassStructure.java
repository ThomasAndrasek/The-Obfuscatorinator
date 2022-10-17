package com.theobfuscatorinator.codeInterpreter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Intances of this class will be created by CodeStructure. These instances are used to represent 
 * java classes and contain information about them.
 * @author Carter Del Ciello
 */
public class ClassStructure {

    private String sourceCode; //This does not contain string literals or comments, they were removed.
    private String className;
    private String sourceFile;

    //This should contain all of the classes that this class is nested in, if any.
    // The classes should be ordered largest to smallest by scope.
    private ArrayList<ClassStructure> containers;

    private ArrayList<ClassStructure> classes; //For nested classes.
    private ArrayList<MethodStructure> methods;
    private ArrayList<String> templateClasses; //Types passed into this class as generics.

    /**
     * This constructor is only intended to be called in controlled contexts when generating all of
     * the information about an overall CodeStructure.
     * @param code Source code of the class
     * @param name Name of the class
     * @param filename Name of the file the class is defined in
     * @param containerClasses List of classes this class is contained in, from largest to smallest
     *  in scope
     * @param templates List of template arguments passed into this class. This may be empty.
     */
    protected ClassStructure(String code, String name, String filename,
     ArrayList<ClassStructure> containerClasses, ArrayList<String> templates, boolean implement, String[] implementedClasses){
        sourceCode = code;
        className = name;
        sourceFile = filename;
        containers = containerClasses;
        classes = identifyClasses();
        templateClasses = templates;
        if (!implement) {
            methods = identifyMethods();
        } else {
            methods = new ArrayList<MethodStructure>();
        }
    }

    /**
     *
     * @return Name of this class
     */
    public String getClassName(){
        return className;
    }

    /**
     *
     * @return ArrayList of ClassStructures that represents classes found in the code.
     */
    private ArrayList<ClassStructure> identifyClasses() {
        String code = sourceCode;
        ArrayList<ClassStructure> classes = new ArrayList<>();
        Pattern classFinder = Pattern.compile("\\s+class\\s+");
        Matcher classMatcher = classFinder.matcher(code);
        int index = 0;
        while(classMatcher.find(index)){
            int classStart = classMatcher.end();
            int i = classStart;
            while (i < code.length() && code.charAt(i) != '{') {
                i++;
            }
            String className = code.substring(classStart, i);

            String full = className.substring(0);

            ArrayList<String> templates = new ArrayList<String>();
            //Handle template arguments
            if(className.contains("<")){
                className = code.substring(classStart, code
                        .indexOf("<", classStart)).trim();
                CodeStructure.Pair<String, Integer> templateContents =
                         CodeStructure.getCodeBetweenBrackets(code, classStart, '<', '>');
                String arguments = templateContents.first;
                templates = CodeStructure.getCommaSeparatedValues(arguments);
            }

            CodeStructure.Pair<String, Integer> currentClass =
                 CodeStructure.getCodeBetweenBrackets(code, classStart, '{', '}');
            ArrayList<ClassStructure> newContainerList = new ArrayList<ClassStructure>(containers);
            newContainerList.add(this);
            int classEnd = className.indexOf(" ");
            
            if (classEnd == -1) {
                classEnd = className.length();
            }
            className = className.substring(0, classEnd);
            className = className.replaceAll("\\s+", "");
            boolean implement = full.indexOf("implements") != -1;
            String[] implementedClassesArray = new String[0];
            if (implement) {
                String implementedClasses = full.substring(full.indexOf("implements") + 10);
                implementedClasses.trim();
                implementedClassesArray = implementedClasses.split(",");
            }
            classes.add(new ClassStructure(currentClass.first, className, sourceFile, 
                                           newContainerList, templates, implement, implementedClassesArray));
            index = currentClass.second;
        }

        return classes;
    }

    /**
     * Finds all methods defined in this class. Does not include methods defined in other classes 
     * nested within this class. This will not include constructors.
     * @return An arraylist of MethodStructures that represents every method that was found.
     */
    private ArrayList<MethodStructure> identifyMethods(){
        String code = ignoreNestedClasses();
        ArrayList<MethodStructure> output = new ArrayList<MethodStructure>();
        // Regex adopted from: https://stackoverflow.com/a/16118844/5956948
        Pattern methodFinder = Pattern.compile("(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
        Matcher methodMatcher = methodFinder.matcher(code);
        int index = 0;
        while(methodMatcher.find(index)){
            String method = "";
            int i = 0;
            while (i <= methodMatcher.groupCount() && methodMatcher.group(i) != null){
                method += methodMatcher.group(i);
                i++;
            }

            String fullMethod = method.substring(0);

            method = method.replaceAll("\\s+", " ");

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

            String staticStatus = "";
            if (method.startsWith("static")) {
                staticStatus = "static";
                method = method.substring(7);
            }

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

            String arguments = "";
            if (method.contains("(")) {
                arguments = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
                method = method.substring(0, method.indexOf("("));
            }

            String template = "";
            if (method.startsWith("<")) {
                template = method.substring(0, method.indexOf(">") + 1);
                method = method.substring(method.indexOf(">") + 2);
            }

            String returnType = method;

            CodeStructure.Pair<String, Integer> detectBody =
                 CodeStructure.getCodeBetweenBrackets(code, methodMatcher.start(), '{','}');
            index = detectBody.second;

            if (!returnType.equals("") && valid) {
                output.add(
                    new MethodStructure(name, scope, staticStatus, template, arguments, 
                                        returnType, code, containers, sourceFile));
            } 
        }

        return output;
    }

    /**
     *
     * @return Returns a version of this classes source code with all bodies of nested classes 
     * removed. This is useful for making sure methods and variables that are added to this 
     * ClassStructure belong to this class scope and not that of a nested class.
     */
    private String ignoreNestedClasses(){
        String out = "";
        Pattern classFinder = Pattern.compile("\\s+class\\s+");
        Matcher classMatcher = classFinder.matcher(sourceCode);
        int index = 0;
        while(classMatcher.find(index)){
            int bodyStart = sourceCode.indexOf('{', classMatcher.end());
            out += sourceCode.substring(index, bodyStart);
            CodeStructure.Pair<String, Integer> currentClass =
                 CodeStructure.getCodeBetweenBrackets(sourceCode, classMatcher.end(), '{', '}');
            index = currentClass.second;
            if(index + 1 < sourceCode.length()) index++;
        }
        out += sourceCode.substring(index);

        return out;
    }

    /**
     * Checks if this class, or any nested classes, contains a main method.
     * @return True if this class contains the main method or contains another class for which this
     *  function returns true.
     */
    protected boolean containsMainMethod(){
        for(ClassStructure c : classes){
            if(c.containsMainMethod()) return true;
        }
        for(MethodStructure m : methods){
            if(m.methodName.equals("main")) return true;
        }
        return false;
    }

    public void setName(String newName) {
        className = newName;
    }

    public String getName() {
        return className;
    }

    public ArrayList<MethodStructure> getMethods() {
        return methods;
    }

    public ArrayList<ClassStructure> getClasses() {
        return classes;
    }

    public String getCode() {
        return sourceCode;
    }
}
