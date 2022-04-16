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

    ClassStructure(String code, String name, String filename, ArrayList<ClassStructure> containerClasses){
        sourceCode = code;
        className = name;
        sourceFile = filename;
        classes = identifyClasses(sourceCode);
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
            CodeStructure.Pair<String, Integer> currentClass = CodeStructure.getCodeBetweenBrackets(code, classStart, '{', '}');
            ArrayList<ClassStructure> newContainerList = new ArrayList<>(containers);
            newContainerList.add(this);
            classes.add(new ClassStructure(currentClass.first, className, sourceFile, newContainerList));
            index = currentClass.second;
        }

        return classes;
    }

}
