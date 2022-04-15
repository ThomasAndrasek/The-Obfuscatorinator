package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Intances of this class will be created by CodeStructure. These instances are used to represent java classes and contain information about them.
 * @author Carter Del Ciello
 */
public class ClassStructure {

    private String sourceCode; //This does not contain string literals or comments, they were removed
    private String className;
    private String sourceFile;

    //This should contain all of the classes that this class is nested in, if any.
    // The classes should be ordered largest to smallest by scope.
    private ArrayList<ClassStructure> containers;

    private ArrayList<ClassStructure> classes; //For nested classes
    private ArrayList<MethodStructure> methods;
    private ArrayList<String> templateClasses; //Types passed into this class as generics

    /**
     * This constructor is only intended to be called in controlled contexts when generating all of the information about an overall CodeStructure.
     * @param code Source code of the class
     * @param name Name of the class
     * @param filename Name of the file the class is defined in
     * @param containerClasses List of classes this class is contained in, from largest to smallest in scope
     * @param templates List of template arguments passed into this class. This may be empty.
     */
    protected ClassStructure(String code, String name, String filename, ArrayList<ClassStructure> containerClasses, ArrayList<String> templates){
        sourceCode = code;
        className = name;
        sourceFile = filename;
        containers = containerClasses;
        classes = identifyClasses();
        templateClasses = templates;
        methods = identifyMethods();
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
            String className = code.substring(classStart, code.indexOf("{", classStart)).trim();

            ArrayList<String> templates = new ArrayList<String>();
            //Handle template arguments
            if(className.contains("<")){
                className = code.substring(classStart, code
                        .indexOf("<", classStart)).trim();
                CodeStructure.Pair<String, Integer> templateContents = CodeStructure.getCodeBetweenBrackets(code, classStart, '<', '>');
                String arguments = templateContents.first;
                templates = CodeStructure.getCommaSeparatedValues(arguments);
            }

            CodeStructure.Pair<String, Integer> currentClass = CodeStructure.getCodeBetweenBrackets(code, classStart, '{', '}');
            ArrayList<ClassStructure> newContainerList = new ArrayList<ClassStructure>(containers);
            newContainerList.add(this);
            classes.add(new ClassStructure(currentClass.first, className, sourceFile, newContainerList, templates));
            index = currentClass.second;
        }

        return classes;
    }

    /**
     * Finds all methods defined in this class. Does not include methods defined in other classes nested within this class.
     * This will not include constructors.
     * @return An arraylist of MethodStructures that represents every method that was found.
     */
    private ArrayList<MethodStructure> identifyMethods(){
        String code = ignoreNestedClasses();
        ArrayList<MethodStructure> output = new ArrayList<MethodStructure>();
        Pattern methodFinder = Pattern.compile("[\\S]{1}(.)*[\\S]*[\\s]*[(]{1}(.)*[)]{1}[\\s]*[{]{1}");
        Matcher methodMatcher = methodFinder.matcher(code);
        int index = 0;
        while(methodMatcher.find(index)){
            ArrayList<String> templates = new ArrayList<String>();
            String method = "";
            if(methodMatcher.group(0) != null){
                method = methodMatcher.group(0);
            }

            method = method.replaceAll("\\s+", " ");

            if (method == "") {
                break;
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

            System.out.println(scope + "\n\t" + staticStatus + "\n\t" + template + "\n\t" + returnType + "\n\t" + name + "\n\t" + arguments);

            if (returnType.equals("")) {
                System.out.println("\t" + "INVALID");
            }
            
            // String rType = methodMatcher.group(3);
            // String methodName = methodMatcher.group(4);
            // String args = methodMatcher.group(5);

            // if(methodMatcher.group(2) != null){
            //     for(String s : CodeStructure.getCommaSeparatedValues(methodMatcher.group(2))) templates.add(s.trim());
            // }

            CodeStructure.Pair<String, Integer> detectBody = CodeStructure.getCodeBetweenBrackets(code, methodMatcher.start(), '{','}');
            index = detectBody.second;

            //Check for constructors
            // if(methodName.trim().equals(className)) continue;
            // if(methodMatcher.group().contains(" new ")) continue;

            // output.add(new MethodStructure(methodName, detectBody.first, sourceFile, containers, args, templates, rType));
        }

        return output;
    }

    /**
     *
     * @return Returns a version of this classes source code with all bodies of nested classes removed. This is useful for making sure methods and variables that are added
     * to this ClassStructure belong to this class scope and not that of a nested class.
     */
    private String ignoreNestedClasses(){
        String out = "";
        Pattern classFinder = Pattern.compile("\\s+class\\s+");
        Matcher classMatcher = classFinder.matcher(sourceCode);
        int index = 0;
        while(classMatcher.find(index)){
            int bodyStart = sourceCode.indexOf('{', classMatcher.end());
            out += sourceCode.substring(index, bodyStart);
            CodeStructure.Pair<String, Integer> currentClass = CodeStructure.getCodeBetweenBrackets(sourceCode, classMatcher.end(), '{', '}');
            index = currentClass.second;
            if(index + 1 < sourceCode.length()) index++;
        }
        out += sourceCode.substring(index);

        return out;
    }

    /**
     * Checks if this class, or any nested classes, contains a main method.
     * @return True if this class contains the main method or contains another class for which this function returns true.
     */
    protected boolean containsMainMethod(){
        for(ClassStructure c : classes){
            if(c.containsMainMethod()) return true;
        }
        for(MethodStructure m : methods){
            if(m.methodName.equals("main") && m.returnType.equals("void")) return true;
        }
        return false;
    }
}
