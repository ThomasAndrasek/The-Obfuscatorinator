package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Intances of this class will be created by CodeStructure. These instances are used to represent 
 * java classes and contain information about them.
 * @author Carter Del Ciello, Thomas Andrasek
 */
public class ClassStructure {

    private String scope;
    private String name;
    private String[] generics;
    private String extended;
    private String[] implemented;

    private String code;

    public ClassStructure(String classHeader, String fullCode){
        this.code = fullCode;

        classHeader = classHeader.trim();

        Pattern classFinder = Pattern.compile("(public|private|protected)?[\\s]*class[\\s]*([^\\s<]+){1}");
        Matcher classMatcher = classFinder.matcher(classHeader);

        this.scope = "";
        this.name = "";

        if (classMatcher.find()) {
            if (classMatcher.group(1) != null) {
                this.scope = classMatcher.group(1);
            }

            this.name = classMatcher.group(2);

            classHeader= classHeader.substring(classMatcher.end());
        }

        classHeader = classHeader.trim();

        this.generics = new String[0];
        if (classHeader.indexOf('<') == 0) {
            String genericList = classHeader.substring(1, classHeader.indexOf('>')+1);
            ArrayList<String> tempGenericList = new ArrayList<>();
            while (genericList.indexOf(',') != -1 && genericList.indexOf(',') < genericList.indexOf('>')) {
                String generic = genericList.substring(0, genericList.indexOf(',')).trim();
                tempGenericList.add(generic.trim());
                genericList = genericList.substring(genericList.indexOf(',') + 1).trim();
            }

            String generic = genericList.substring(0, genericList.indexOf('>'));
            tempGenericList.add(generic.trim());
            this.generics = (String[]) tempGenericList.toArray(new String[tempGenericList.size()]);
            classHeader = classHeader.substring(classHeader.indexOf('>') + 1);
            classHeader = classHeader.trim();
        }

        this.extended = "";
        if (classHeader.indexOf("extends") == 0) {
            classHeader = classHeader.substring(7).trim();
            
            Pattern namePattern = Pattern.compile("[^\\s]+");
            Matcher extendsNameMatcher = namePattern.matcher(classHeader);

            if (extendsNameMatcher.find()) {
                this.extended = extendsNameMatcher.group(0);
            }

            classHeader = classHeader.substring(extendsNameMatcher.end()).trim();
        }

        this.implemented = new String[0];
        if (classHeader.indexOf("implements") == 0) {
            classHeader = classHeader.substring(10).trim();
            ArrayList<String> tempImplementList = new ArrayList<>();
            while (classHeader.indexOf(',') != -1) {
                tempImplementList.add(classHeader.substring(0, classHeader.indexOf(',')).trim());
                classHeader = classHeader.substring(classHeader.indexOf(',') + 1).trim();
            }
            tempImplementList.add(classHeader.trim());
            this.implemented = (String[]) tempImplementList.toArray(new String[tempImplementList.size()]);
        }
    }

    /**
     *
     * @return Name of this class
     */
    public String getName(){
        return name;
    }

    public static ArrayList<ClassStructure> identifyClasses(CodeStructure codeStructure) {
        ArrayList<ClassStructure> classes = new ArrayList<>();

        Pattern classFinder = Pattern.compile("(public|private|protected)?[\\s]*class[\\s]*([^\\s<]+){1}");
        String code = CodeStructure.removeInnerCodeOfBraces(codeStructure.getUnCommentedCode());
        Matcher classMatcher = classFinder.matcher(code);

        while (classMatcher.find()) {
            int start = classMatcher.start(0);
            int end = start;
            while (code.charAt(end) != '{') {
                end++;
            }
            String classHeader = code.substring(start, end).trim();
            
            Pattern actualClassFinder = Pattern.compile(classHeader);
            Matcher actualClassMatcher = actualClassFinder.matcher(codeStructure.getUnCommentedCode());

            while (actualClassMatcher.find()) {
                int actualEnd = actualClassMatcher.end();
                String innerCode = codeStructure.getUnCommentedCode().substring(actualClassMatcher.start(), CodeStructure.getCodeBetweenBrackets(codeStructure.getUnCommentedCode(), actualEnd, '{', '}').second + 1);
                
                classes.add(new ClassStructure(classHeader, innerCode));
            }
        }

        return classes;
    }

    public static ArrayList<ClassStructure> identifyClasses(ClassStructure classStructure) {
        ArrayList<ClassStructure> classes = new ArrayList<>();

        Pattern classFinder = Pattern.compile("(public|private|protected)?[\\s]*class[\\s]*([^\\s<]+){1}");
        String code = CodeStructure.removeInnerCodeOfBraces(CodeStructure.removeInnerCodeOfBraces(classStructure.getInnerCode()));
        Matcher classMatcher = classFinder.matcher(code);

        while (classMatcher.find()) {
            int start = classMatcher.start(0);
            int end = start;
            while (code.charAt(end) != '{') {
                end++;
            }
            String classHeader = code.substring(start, end).trim();
            
            Pattern actualClassFinder = Pattern.compile(classHeader);
            Matcher actualClassMatcher = actualClassFinder.matcher(classStructure.getInnerCode());

            while (actualClassMatcher.find()) {
                int actualEnd = actualClassMatcher.end();
                String innerCode = classStructure.getInnerCode().substring(actualClassMatcher.start(), CodeStructure.getCodeBetweenBrackets(classStructure.getInnerCode(), actualEnd, '{', '}').second + 1);
                
                classes.add(new ClassStructure(classHeader, innerCode));
            }
        }

        return classes;
    }

    /**
     * Checks if this class, or any nested classes, contains a main method.
     * @return True if this class contains the main method or contains another class for which this
     *  function returns true.
     */
    // protected boolean containsMainMethod(){
    //     for(ClassStructure c : classes){
    //         if(c.containsMainMethod()) return true;
    //     }
    //     for(MethodStructure m : methods){
    //         if(m.methodName.equals("main")) return true;
    //     }
    //     return false;
    // }

    public void setName(String newName) {
        name = newName;
    }

    public ArrayList<MethodStructure> getMethods() {
        return null;
    }

    public ArrayList<ClassStructure> getClasses() {
        return null;
    }

    public String getCode() {
        return "";
    }

    public String getInnerCode() {
        return CodeStructure.getCodeBetweenBrackets(this.code, this.code.indexOf('{'), '{', '}').first;
    }

    @Override
    public String toString() {
        StringBuilder header = new StringBuilder();

        if (!scope.equals("")) {
            header.append(this.scope + " ");
        }

        header.append("class ");
        header.append(this.name);

        if (this.generics.length > 0) {
            header.append("<");
            header.append(this.generics[0]);
            for (int i = 1; i < this.generics.length; i++) {
                header.append(",");
                header.append(this.generics[i]);
            }
            header.append(">");
        }

        if (!extended.equals("")) {
            header.append(" extends ");
            header.append(this.extended);
        }

        if (this.implemented.length > 0) {
            header.append(" implements ");
            header.append(this.implemented[0]);
            for (int i = 1; i < this.implemented.length; i++) {
                header.append(", ");
                header.append(this.implemented[i]);
            }
        }

        return header.toString();
    }
}
