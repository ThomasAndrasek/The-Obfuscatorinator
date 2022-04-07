package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassStructure {

    private String sourceCode; //This does not contain string literals or comments, they were removed
    private String className;
    private String sourceFile;

    //This should contain all of the classes that this class is nested in, if any.
    // The classes should be ordered largest to smallest by scope.
    private ArrayList<ClassStructure> containers;

    private ArrayList<ClassStructure> classes;
    private ArrayList<MethodStructure> methods;
    private ArrayList<String> templateClasses;

    ClassStructure(String code, String name, String filename, ArrayList<ClassStructure> containerClasses, ArrayList<String> templates){
        sourceCode = code;
        className = name;
        sourceFile = filename;
        containers = containerClasses;
        classes = identifyClasses(sourceCode);
        templateClasses = templates;
        System.out.println(name);
        System.out.println(ignoreNestedClasses());
    }


    public String getClassName(){
        return className;
    }

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
