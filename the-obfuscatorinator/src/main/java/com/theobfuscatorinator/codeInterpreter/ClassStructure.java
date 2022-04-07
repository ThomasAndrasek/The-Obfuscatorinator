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
        classes = identifyClasses(sourceCode);
        templateClasses = templates;
        System.out.println(name);
        System.out.println(ignoreNestedClasses());
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
     * @param code Generally this should be the sourceCode. Should already have comments and string literals removed or false positives are possible.
     * @return ArrayList of ClassStructures that represents classes found in the code.
     */
    private ArrayList<ClassStructure> identifyClasses(String code) {
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

}
